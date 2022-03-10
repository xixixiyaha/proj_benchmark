package com.freeb.jVerb;

import com.ibm.disni.RdmaActiveEndpoint;
import com.ibm.disni.RdmaActiveEndpointGroup;
import com.ibm.disni.verbs.*;
import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;

public class TRdmaServerEndpoint extends TRdmaEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(TRdmaServerEndpoint.class);

    private TRdmaServerGroup<? extends RdmaActiveEndpoint> group_;
//    private TProcessor processor_;
    private TProtocolFactory protocolFactory_;

    private int clusterId;


    public TRdmaServerEndpoint(TRdmaServerGroup<?extends RdmaActiveEndpoint> group, RdmaCmId idPriv, boolean serverSide) throws IOException {
        super(group, idPriv, serverSide);
        this.group_=group;
//        this.processor_ = processor;
    }

    @Override
    public synchronized boolean isConnected() {
        return super.isConnected();
    }

    @Override
    public void dispatchCqEvent(IbvWC wc) throws IOException {
        if (wc.getOpcode() == 128){
            //receiving a message
            int index = (int) wc.getWr_id();
            ByteBuffer recvBuffer = recvBufs[index];
            dispatchReceive(recvBuffer, -1,index);
        } else if (wc.getOpcode() == 0) {
            //send completion
            int index = (int) wc.getWr_id();
            dispatchSend(index);
        } else {
            throw new IOException("Unkown opcode " + wc.getOpcode());
        }
    }

    @Override
    public void dispatchReceive( ByteBuffer recvBuffer, int ticket,int recvIndex) throws IOException {
        //Step1 获取freeTrans TODO:trans可不可以池化 而不是新声明
        TRdmaServerJVerbTrans trans = new TRdmaServerJVerbTrans();
        TProtocol protocol = protocolFactory_.getProtocol(trans);
//      trans = group.getTrans();

        //Step2 填充trans数据
        trans.setEndpoint(this);
        trans.updateReadBuff(recvBuffer);

        //Step3 执行
        group_.processServerEvent(protocol);

    }

    @Override
    public void dispatchSend(int index) throws IOException {
        this.freePostSend.add(sendCall[index]);
    }

    @Override
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

    public void sendResponse(SVCPostSend wq) throws IOException{
        wq.execute();
    }

    public void setProtocolFactory(TProtocolFactory factory){
        this.protocolFactory_ = factory;
    }


    public int getClusterId() {
        return clusterId;
    }
}
