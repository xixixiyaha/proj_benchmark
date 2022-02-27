package com.freeb.jVerb;

import com.ibm.disni.RdmaActiveEndpoint;
import com.ibm.disni.RdmaActiveEndpointGroup;
import com.ibm.disni.verbs.*;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;

public class TRdmaServerEndpoint extends TRdmaEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(TRdmaServerEndpoint.class);

    private TRdmaServerGroup<? extends RdmaActiveEndpoint> group_;

    public TRdmaServerEndpoint(TRdmaServerGroup<?extends RdmaActiveEndpoint> group, RdmaCmId idPriv, boolean serverSide) throws IOException {
        super(group, idPriv, serverSide);
        this.group_=group;
    }

    @Override
    public void dispatchCqEvent(IbvWC wc) throws IOException {
        if (wc.getOpcode() == 128){
            //receiving a message
            int index = (int) wc.getWr_id();
            ByteBuffer recvBuffer = recvBufs[index];
            dispatchReceive(index,recvBuffer, -1);
        } else if (wc.getOpcode() == 0) {
            //send completion
            int index = (int) wc.getWr_id();
            dispatchSend(index);
        } else {
            throw new IOException("Unkown opcode " + wc.getOpcode());
        }
    }

    @Override
    public void dispatchReceive(int recvIndex, ByteBuffer recvBuffer, int ticket) throws IOException {
        //Step1 获取freeTrans
        TProtocol protocol;
        TRdmaServerJVerbTrans trans = (TRdmaServerJVerbTrans) protocol.getTransport();
//      trans = group.getTrans();

        //Step2
        trans.setEndpoint(this);
        trans.updateReadBuff(recvBuffer);

    }

    @Override
    public void dispatchSend(int index) throws IOException {

    }

    @Override
    public SVCPostSend getSendWQ() throws IOException {
        return null;
    }



}
