package com.freeb.jVerb;

import com.ibm.disni.RdmaActiveEndpointGroup;
import com.ibm.disni.RdmaEndpoint;
import com.ibm.disni.RdmaEndpointGroup;
import com.ibm.disni.verbs.*;

import java.io.IOException;

public abstract class TRdmaGroup <E extends RdmaEndpoint> extends RdmaEndpointGroup<E> {
    private int recvQueueSize;
    private int sendQueueSize;
    private int timeout;
    private int bufferSize;
    private int maxInline;

    public TRdmaGroup(int timeout, int maxinline, int recvQueue, int sendQueue,int bufferSize) throws IOException {
        super(timeout);
//        super(timeout);
        this.recvQueueSize = recvQueue;
        this.sendQueueSize = Math.max(recvQueue, sendQueue);
        this.timeout = timeout;
        this.bufferSize = bufferSize;
        this.maxInline = maxinline;
    }

    // qp is equivalent to sockets
    protected synchronized IbvQP createQP(RdmaCmId id, IbvPd pd, IbvCQ cq) throws IOException{
        IbvQPInitAttr attr = new IbvQPInitAttr();
        attr.cap().setMax_recv_wr(recvQueueSize);
        attr.cap().setMax_send_wr(sendQueueSize);
        attr.cap().setMax_recv_sge(1);
        attr.cap().setMax_send_sge(1);
        attr.cap().setMax_inline_data(maxInline);
        attr.setQp_type(IbvQP.IBV_QPT_RC);
        attr.setRecv_cq(cq);
        attr.setSend_cq(cq);
        return id.createQP(pd, attr);
    }

    public int getTimeout() {
        return timeout;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void close() throws IOException, InterruptedException {
        super.close();
    }

    public int recvQueueSize() {
        return recvQueueSize;
    }

    public int sendQueueSize() {
        return sendQueueSize;
    }

    public int getMaxInline() {
        return maxInline;
    }


}
