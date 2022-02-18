package com.freeb.DaRPC;

import com.ibm.darpc.DaRPCMessage;
import org.apache.commons.math3.exception.OutOfRangeException;

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
        byteBuffer.putInt(cmd_);
        byteBuffer.putLong(time_);
        byteBuffer.put(byteBuffer.position(), length_,0,4);
        byteBuffer.put(byteBuffer.position(), param_,0, PARAM_SIZE);
        return SERIALIZED_SIZE;
    }

    @Override
    public void update(ByteBuffer byteBuffer) throws IOException {
        cmd_ = byteBuffer.getInt();
        time_ = byteBuffer.getLong();
        byteBuffer.get(byteBuffer.position(), length_,0,4);
        byteBuffer.get(byteBuffer.position(), param_,0, PARAM_SIZE);
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
    public int writeToParam(byte[] bytes, int offset, int len,int pos) {
        int i = 0,j=offset;
        for(; j<offset+len&&(pos+i)< PARAM_SIZE; i++,j++){
            this.param_[pos+i] = bytes[j];
        }
        if(pos == PARAM_SIZE){
            throw new OutOfRangeException(pos+i,0, PARAM_SIZE);
        }
        return i;
    }
    public byte[] getParam_(){
        return this.param_;
    }
    public void getLength_(byte[] i32buf) {
        System.arraycopy(this.length_, 0, i32buf, 0, 4);
    }
    public void setLength_(byte[] i32buf) {
        System.arraycopy(i32buf, 0,this.length_, 0, 4);
    }
    public void setTime_(long time_) {
        this.time_ = time_;
    }
    public void clear(){
        cmd_ = 0;
        time_ = 0L;
        Arrays.fill(length_, (byte)0);
        Arrays.fill(param_, (byte)0);
    }

}
