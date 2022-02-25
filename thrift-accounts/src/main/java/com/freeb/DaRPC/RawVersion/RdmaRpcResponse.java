package com.freeb.DaRPC.RawVersion;

import com.ibm.darpc.DaRPCMessage;
import org.apache.commons.math3.exception.OutOfRangeException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class RdmaRpcResponse implements DaRPCMessage {
    public static int SERIALIZED_SIZE = 200;
    private int pos,limit;
    private byte[] param_ = new byte[SERIALIZED_SIZE];

    @Override
    public int write(ByteBuffer byteBuffer) throws IOException {
        byteBuffer.put(byteBuffer.position(), param_,0, SERIALIZED_SIZE);
        return SERIALIZED_SIZE;
    }

    @Override
    public void update(ByteBuffer byteBuffer) throws IOException {
        byteBuffer.get(byteBuffer.position(), param_,0, SERIALIZED_SIZE);
    }

    @Override
    public int size() {
        return SERIALIZED_SIZE;
    }

    public int readFromParam(byte[] bytes, int offset, int len) {
        if(pos >= limit){
            throw new OutOfRangeException(pos,0, limit);
        }
        int i=0,j = offset;
        for(;i<len;i++){
            bytes[j++] = this.param_[pos+i];
        }
        return i;
    }
    public int writeToParam(byte[] bytes, int offset, int len) {
        if(pos >= limit){
            throw new OutOfRangeException(pos,0, limit);
        }
        int i = 0,j=offset;
        for(; j<offset+len&&pos< limit; i++,j++){
            this.param_[pos++] = bytes[j];
        }

        return i;
    }

    public byte[] getParam_(){
        return this.param_;
    }

    public int getBytesRemainingInBuffer(){
        return this.limit-this.pos;
    }
    public void consumeBuffer(int len) {
        this.pos+=len;
    }
    public int getBufferPosition() {
        return this.pos;
    }
    public void clear(){
        this.pos = 0;
        this.limit = SERIALIZED_SIZE;
        Arrays.fill(param_, (byte)0);
    }

    public void setLimit(int i) {
        this.limit = i;
    }
    public void setPos(int i) {
        this.pos = i;
    }
}
