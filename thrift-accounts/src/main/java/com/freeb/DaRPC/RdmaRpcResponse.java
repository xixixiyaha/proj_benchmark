package com.freeb.DaRPC;

import com.ibm.darpc.DaRPCMessage;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class RdmaRpcResponse implements DaRPCMessage {
    public static int SERIALIZED_SIZE = 200;
    public static int PARAM_SIZE = 184;
    private int cmd_;
    private long time_;
    private byte[] length_ = new byte[4];
    private byte[] param_ = new byte[PARAM_SIZE];

    @Override
    public int write(ByteBuffer byteBuffer) throws IOException {
        return 0;
    }

    @Override
    public void update(ByteBuffer byteBuffer) throws IOException {

    }

    @Override
    public int size() {
        return SERIALIZED_SIZE;
    }

    public int readFromParam(byte[] bytes, int offset, int len, int pos,int size) {
        //TODO Notice
        int i=0,j = offset;
        for(;(i<len)&&(pos+i)<size;i++){
            bytes[j++] = this.param_[pos+i];
        }
        return i;
    }
    public byte[] getParam_(){
        return this.param_;
    }
    public void getLength_(byte[] i32buf) {
        System.arraycopy(this.length_, 0, i32buf, 0, 4);
    }
    public void clear(){
        cmd_ = 0;
        time_ = 0L;
        Arrays.fill(length_, (byte)0);
        Arrays.fill(param_, (byte)0);
    }

}
