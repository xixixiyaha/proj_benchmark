package com.freeb.DaRPC;

import com.ibm.darpc.DaRPCMessage;

import java.io.IOException;
import java.nio.ByteBuffer;

public class RdmaRpcResponse implements DaRPCMessage {
    public static int SERIALIZED_SIZE = 200;
    public static int SERIALIZE_LENGTH = 184;
    private int cmd_;
    private long time_;
    private byte[] length_ = new byte[4];
    private byte[] param_ = new byte[SERIALIZE_LENGTH];
    private int pos = 0;

    public void clear(){
        this.pos = 0;
        //TODO clear buffer
    }

    @Override
    public int write(ByteBuffer byteBuffer) throws IOException {
        return 0;
    }

    @Override
    public void update(ByteBuffer byteBuffer) throws IOException {

    }

    @Override
    public int size() {
        return 0;
    }

    public int readFromParam(byte[] bytes, int offset, int len) {
        //TODO Notice
        int i = 0,j = offset;
        for(;i<len&&i<SERIALIZE_LENGTH;i++){
            bytes[j++] = this.param_[i];
        }
        return i;
    }
    public byte[] getParam_(){
        return this.param_;
    }
    public void getLength_(byte[] i32buf) {
        System.arraycopy(this.length_, 0, i32buf, 0, 4);
    }

}
