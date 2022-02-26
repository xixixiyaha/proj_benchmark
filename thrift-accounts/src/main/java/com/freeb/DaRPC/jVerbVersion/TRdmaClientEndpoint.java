package com.freeb.DaRPC.jVerbVersion;

import com.ibm.darpc.DaRPCFuture;
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
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

public class TRdmaClientEndpoint extends RdmaActiveEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(TRdmaClientEndpoint.class);

    /* Endpoint Usage */
    private ArrayBlockingQueue<SVCPostSend> freePostSend;
    private ConcurrentLinkedQueue<SVCPostSend> pendingPostSend; // TODO 和 ArrayBlocking Queue的区别
    private ConcurrentHashMap<Integer,byte[]> ticket2readBuf = new ConcurrentHashMap<>();
    private AtomicInteger hasFreeRecv = new AtomicInteger(0);
    private Set<Integer> finishedTickets = ConcurrentHashMap.newKeySet();


    private AtomicInteger ticket;


    /*=== Raw Usage ===*/
    private ReentrantLock lock;
    private SVCPollCq poll;
    private IbvWC[] wcList; //work complete list

    /* === Buffer ===*/

//    private ConcurrentHashMap<Integer, SVCPostSend> pendingPostSend;


    private ByteBuffer[] sendBufs;
    private SVCPostSend[] sendCall;
    private int rawMsgSize_;

    private ByteBuffer[] recvBufs;
    private SVCPostRecv[] recvCall;

    private ByteBuffer dataBuffer;
    private IbvMr dataMr;


    private void allocatePostRecvRes() throws IOException {
        this.sendCall = new SVCPostSend[this.pipeLength_];
        this.sendBufs = new ByteBuffer[this.pipeLength_];
        this.freePostSend = new ArrayBlockingQueue<SVCPostSend>(this.pipeLength_);
        this.pendingPostSend = new ConcurrentLinkedQueue<>();

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
            //TODO
            recvCall[i].execute();
        }
    }


    // TODO@high
    private static int MAX_MESSAGE_SIZE = 1024;
    private int pipeLength_ = 1;


    public TRdmaClientEndpoint(RdmaActiveEndpointGroup<? extends RdmaActiveEndpoint> group, RdmaCmId idPriv, boolean serverSide) throws IOException {
        super(group, idPriv, serverSide);
        this.rawMsgSize_ = 4+MAX_MESSAGE_SIZE;
//        this.pipeLength = //TODO@high
    }

    @Override
    public synchronized boolean isConnected() {
        return super.isConnected();
    }

    @Override
    public void dispatchCqEvent(IbvWC wc) throws IOException {
        // SEND complete => free()
        // RECV complete => sendBase.take() callback in ReadFrame
        if (wc.getStatus() == 5){
            //flush
            //TODO@notice
            return;
        } else if (wc.getStatus() != 0){
            throw new IOException("Faulty operation! wc.status " + wc.getStatus());
        }

        if (wc.getOpcode() == 128){
            //receiving a message
            int index = (int) wc.getWr_id();
            ByteBuffer recvBuffer = recvBufs[index];
            int ticket = recvBuffer.getInt(0);
            recvBuffer.position(4);
            dispatchReceive(recvBuffer, ticket, index);
        } else if (wc.getOpcode() == 0) {
            //send completion
            int index = (int) wc.getWr_id();
            ByteBuffer sendBuffer = sendBufs[index];
            dispatchSend(index);
        } else {
            throw new IOException("Unkown opcode " + wc.getOpcode());
        }
    }

    public void dispatchReceive(ByteBuffer recvBuffer, int ticket, int recvIndex) throws IOException {
        // Step1 把buffer内容 放进 Map<ticket,buffer>
        byte[] buf = this.ticket2readBuf.get(ticket);
        recvBuffer.get(buf,0,MAX_MESSAGE_SIZE);
        this.finishedTickets.add(ticket);

        // TODO notifyAll()/signal

        //Step2 postRecv
        recvCall[recvIndex].execute();

        //Step3 check if there is pending Send. if true, get and execute()/ if false, tell the endpoint there is available recv
        SVCPostSend p = pendingPostSend.poll();
        if (p == null){
            logger.info("no pending (postSend) for ticket " + ticket);
            this.hasFreeRecv.incrementAndGet();
        }else{
            p.execute();
        }

//        freePostSend.add(p);
//        future.signal(0);
    }

    public void dispatchSend(int index) throws IOException {
        this.freePostSend.add(sendCall[index]);
    }

    public Boolean eventTake(int ticket){
        return this.finishedTickets.contains(ticket);
    }

    public SVCPostSend getSendWQ() throws IOException {
        SVCPostSend p = freePostSend.poll();
        if(p!=null){
            int tkt = ticket.incrementAndGet();
            int idx = (int)p.getWrMod(0).getWr_id();
            //TODO 这里应该初始化时侯就set了不用改的 =>notice 这里 把该这些参数挪进了Trans层
//            p.getWrMod(0).getSgeMod(0).setLength(4+MAX_MESSAGE_SIZE);
            // TODO@后期的selected signaling
//            p.getWrMod(0).setSend_flags(IbvSendWR.IBV_SEND_SIGNALED);
            // TODO 真的支持 inline吗 TODO@high 这里是 this.maxInline()
            if (4+MAX_MESSAGE_SIZE <= 1024) {
                p.getWrMod(0).setSend_flags(p.getWrMod(0).getSend_flags() | IbvSendWR.IBV_SEND_INLINE);
            }
            sendBufs[idx].putInt(tkt);
            return p;
        }
        return null;
    }

    public void request(SVCPostSend postSend,int ticket,byte[] readBuf) throws IOException {
        //Step1
        this.ticket2readBuf.put(ticket,readBuf);

        boolean flag = false;
        //Step2 TODO@大优化
        synchronized (this){
            if(this.hasFreeRecv.get()>0){
                this.hasFreeRecv.getAndDecrement();
                flag = true;
            }
        }
        if(flag){
            postSend.execute();
        }else{
            pendingPostSend.add(postSend);
        }
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
