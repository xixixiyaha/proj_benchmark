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
import java.util.concurrent.locks.ReentrantLock;

public class TRdmaClientEndpoint extends TRdmaEndpoint {
    //TODO :
    // 1 并发控制: 包括 类型选择 锁 hasFree发送
    //                          比如 hasFree 如果并发lock太麻烦 也可以改成令牌式的一个线程安全的queue 但如果线程安全的queue本身也是lock实现的 那就不如直接用lock
    // 2. PostSend的参数可以放进Trans层 让trans决定 signal与否 inline与否 长度 etc.
    // 3. 临界控制 null/不存在的ticket etc
    private static final Logger logger = LoggerFactory.getLogger(TRdmaClientEndpoint.class);

    protected ConcurrentHashMap<Integer,byte[]> ticket2readBuf = new ConcurrentHashMap<>();
    protected AtomicInteger hasFreeRecv = new AtomicInteger(0);
    protected Set<Integer> finishedTickets = ConcurrentHashMap.newKeySet();

    protected ConcurrentLinkedQueue<SVCPostSend> pendingPostSend; // TODO 和 ArrayBlocking Queue的区别

    public TRdmaClientEndpoint(RdmaActiveEndpointGroup<? extends RdmaActiveEndpoint> group, RdmaCmId idPriv, boolean serverSide) throws IOException {
        super(group, idPriv, serverSide);
        this.pendingPostSend = new ConcurrentLinkedQueue<>();

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
            dispatchReceive(index,recvBuffer, ticket);
        } else if (wc.getOpcode() == 0) {
            //send completion
            int index = (int) wc.getWr_id();
            dispatchSend(index);
        } else {
            throw new IOException("Unkown opcode " + wc.getOpcode());
        }
    }

    public void dispatchReceive(int recvIndex,ByteBuffer recvBuffer, int ticket) throws IOException {
        // Step1 把buffer内容 放进 Map<ticket,buffer>
        byte[] buf = this.ticket2readBuf.remove(ticket);
        recvBuffer.get(buf,0,Math.min(MAX_MESSAGE_SIZE,buf.length));
        this.finishedTickets.add(ticket);

        // TODO notifyAll()/signal

        //Step2 postRecv
        recvCall[recvIndex].execute();

        //Step3 check if there is pending Send. if true, get and execute()/ if false, tell the endpoint there is available recv
        SVCPostSend p = pendingPostSend.poll();
        if (p == null){
            logger.info("no pending (postSend) for ticket " + ticket);
            //TODO@ 并发控制
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
            //TODO 这里应该初始化时侯就set了不用改的 =>notice
            p.getWrMod(0).getSgeMod(0).setLength(4+MAX_MESSAGE_SIZE);
            // TODO@后期的selected signaling
            p.getWrMod(0).setSend_flags(IbvSendWR.IBV_SEND_SIGNALED);
            // TODO 真的支持 inline吗 TODO@high 这里是 this.maxInline()
            if (4+MAX_MESSAGE_SIZE <= 1024) {
                p.getWrMod(0).setSend_flags(p.getWrMod(0).getSend_flags() | IbvSendWR.IBV_SEND_INLINE);
            }
            sendBufs[idx].putInt(tkt);
            return p;
        }
        return null;
    }

    public int writeBuf(int index,byte[] buf,int offset,int len){
        sendBufs[index].put(buf,offset,len);
        return sendBufs.length;
    }

    public int request(int idx,SVCPostSend postSend,byte[] readBuf) throws IOException {

        int ticket = sendBufs[idx].clear().getInt();
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
        return ticket;
    }
}
