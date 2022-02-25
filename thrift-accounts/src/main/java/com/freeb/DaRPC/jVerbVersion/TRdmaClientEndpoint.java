package com.freeb.DaRPC.jVerbVersion;

import com.ibm.disni.RdmaActiveEndpoint;
import com.ibm.disni.RdmaActiveEndpointGroup;
import com.ibm.disni.verbs.IbvWC;
import com.ibm.disni.verbs.RdmaCmId;
import com.ibm.disni.verbs.SVCPostSend;
import org.apache.thrift.transport.TMemoryInputTransport;

import java.io.IOException;
import java.nio.ByteBuffer;

public class TRdmaClientEndpoint extends RdmaActiveEndpoint {
    public TRdmaClientEndpoint(RdmaActiveEndpointGroup<? extends RdmaActiveEndpoint> group, RdmaCmId idPriv, boolean serverSide) throws IOException {
        super(group, idPriv, serverSide);
    }

    @Override
    public synchronized boolean isConnected() {
        return super.isConnected();
    }

    @Override
    public void dispatchCqEvent(IbvWC ibvWC) throws IOException {
        // SEND complete => free()
        // RECV complete => sendBase.take() callback in ReadFrame
    }

    public ByteBuffer eventTake(int ticket){
        //TODO@high
        return null;
    }

    public void freeRecv(int ticket){

    }

    public ByteBuffer getSendWQ(int ticket){
        return null;
    }
}
