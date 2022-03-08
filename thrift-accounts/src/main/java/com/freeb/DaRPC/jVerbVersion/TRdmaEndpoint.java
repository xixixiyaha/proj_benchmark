package com.freeb.DaRPC.jVerbVersion;

import com.ibm.disni.RdmaActiveEndpoint;
import com.ibm.disni.RdmaActiveEndpointGroup;
import com.ibm.disni.util.MemoryUtils;
import com.ibm.disni.verbs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public abstract class TRdmaEndpoint extends RdmaActiveEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(TRdmaEndpoint.class);

    /* Endpoint Usage */
    protected ArrayBlockingQueue<SVCPostSend> freePostSend;


    protected AtomicInteger ticket;


    /*=== Raw Usage ===*/
    protected ReentrantLock lock;
    protected SVCPollCq poll;
    protected IbvWC[] wcList; //work complete list

    /* === Buffer ===*/

    protected ByteBuffer[] sendBufs;
    protected SVCPostSend[] sendCall;
    protected int rawMsgSize_;

    protected ByteBuffer[] recvBufs;
    protected SVCPostRecv[] recvCall;

    protected ByteBuffer dataBuffer;
    protected IbvMr dataMr;


    private void allocatePostRecvRes() throws IOException {
        this.sendCall = new SVCPostSend[this.pipeLength_];
        this.sendBufs = new ByteBuffer[this.pipeLength_];
        this.freePostSend = new ArrayBlockingQueue<SVCPostSend>(this.pipeLength_);

        this.recvCall = new SVCPostRecv[this.pipeLength_];
        this.recvBufs = new ByteBuffer[this.pipeLength_];

        int sendBufferOffset = pipeLength_ * rawMsgSize_;
        dataBuffer = ByteBuffer.allocateDirect(sendBufferOffset*2);
        dataMr = registerMemory(dataBuffer).execute().free().getMr();

        ByteBuffer recvBuffer;
        ByteBuffer sendBuffer;

        // TODO@high 为何这么分, recv在前 send在后
        /* Receive memory region is the first half of the main buffer. */
        dataBuffer.limit(dataBuffer.position() + sendBufferOffset);
        recvBuffer = dataBuffer.slice();

        /* Send memory region is the second half of the main buffer. */
        dataBuffer.position(sendBufferOffset);
        dataBuffer.limit(dataBuffer.position() + sendBufferOffset);
        sendBuffer = dataBuffer.slice();

        for(int i = 0; i < pipeLength_; i++) {
            /* Create single receive buffers within the receive region in form of slices. */
            recvBuffer.position(i * rawMsgSize_);
            recvBuffer.limit(recvBuffer.position() + rawMsgSize_);
            recvBufs[i] = recvBuffer.slice();

            /* Create single send buffers within the send region in form of slices. */
            sendBuffer.position(i * rawMsgSize_);
            sendBuffer.limit(sendBuffer.position() + rawMsgSize_);
            sendBufs[i] = sendBuffer.slice();

            this.recvCall[i] = setupRecvTask(i);
            this.sendCall[i] = setupSendTask(i);
            freePostSend.add(sendCall[i]);
            //TODO 增加freeSend
            recvCall[i].execute();
//            this.hasFreeRecv.addAndGet(1);
        }
    }


    // TODO@high
    protected static int MAX_MESSAGE_SIZE = 1024;
    protected int pipeLength_ = 1;


    public TRdmaEndpoint(RdmaActiveEndpointGroup<? extends RdmaActiveEndpoint> group, RdmaCmId idPriv, boolean serverSide) throws IOException {
        super(group, idPriv, serverSide);
        this.rawMsgSize_ = 4+MAX_MESSAGE_SIZE;
//        this.pipeLength = //TODO@high
    }

    @Override
    public synchronized boolean isConnected() {
        return super.isConnected();
    }

    @Override
    public abstract void dispatchCqEvent(IbvWC wc) throws IOException;

    public abstract void dispatchReceive(int recvIndex,ByteBuffer recvBuffer, int ticket) throws IOException;

    public abstract void dispatchSend(int index) throws IOException;


    public abstract SVCPostSend getSendWQ() throws IOException;

    public int writeBuf(int index,byte[] buf,int offset,int len){
        sendBufs[index].put(buf,offset,len);
        return sendBufs[index].position();
    }

    private SVCPostSend setupSendTask(int wrid) throws IOException {
        ArrayList<IbvSendWR> sendWRs = new ArrayList<IbvSendWR>(1);
        LinkedList<IbvSge> sgeList = new LinkedList<IbvSge>();

        IbvSge sge = new IbvSge();
        sge.setAddr(MemoryUtils.getAddress(sendBufs[wrid]));
        sge.setLength(rawMsgSize_);
        sge.setLkey(dataMr.getLkey());
        sgeList.add(sge);

        IbvSendWR sendWR = new IbvSendWR();
        sendWR.setSg_list(sgeList);
        sendWR.setWr_id(wrid);
        sendWRs.add(sendWR);
        sendWR.setSend_flags(IbvSendWR.IBV_SEND_SIGNALED);
        sendWR.setOpcode(IbvSendWR.IbvWrOcode.IBV_WR_SEND.ordinal());
        //TODO@track batching
        return postSend(sendWRs);
    }

    private SVCPostRecv setupRecvTask(int wrid) throws IOException {
        ArrayList<IbvRecvWR> recvWRs = new ArrayList<IbvRecvWR>(1);
        LinkedList<IbvSge> sgeList = new LinkedList<IbvSge>();

        IbvSge sge = new IbvSge();
        sge.setAddr(MemoryUtils.getAddress(recvBufs[wrid]));
        sge.setLength(rawMsgSize_);
        sge.setLkey(dataMr.getLkey());
        sgeList.add(sge);

        IbvRecvWR recvWR = new IbvRecvWR();
        recvWR.setWr_id(wrid);
        recvWR.setSg_list(sgeList);
        recvWRs.add(recvWR);

        return postRecv(recvWRs);
    }

    public void pollOnce() throws IOException {
        if (!lock.tryLock()){
            return;
        }

        try {
            _pollOnce();
        } finally {
            lock.unlock();
        }
    }



    private int _pollOnce() throws IOException {
        int res = poll.execute().getPolls();
        if (res > 0) {
            for (int i = 0; i < res; i++){
                IbvWC wc = wcList[i];
                dispatchCqEvent(wc);
            }

        }
        return res;
    }
}
