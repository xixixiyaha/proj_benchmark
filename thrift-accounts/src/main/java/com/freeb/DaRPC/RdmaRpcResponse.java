package com.freeb.DaRPC;

import com.ibm.darpc.DaRPCMessage;

import java.io.IOException;
import java.nio.ByteBuffer;

public class RdmaRpcResponse implements DaRPCMessage {
    public static int SERIALIZED_SIZE = 200;
    public static int SERIALIZE_LENGTH = 184;
    private int cmd;
    private int length;
    private long time;
    private byte[] param = new byte[SERIALIZE_LENGTH];
    private int pos = 0;

    public void clear(){
        this.pos = 0;
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

    public int getParam(byte[] bytes, int offset, int len) {
        //TODO Notice
        int i = 0,j = offset;
        for(;i<len&&i<SERIALIZE_LENGTH;i++){
            bytes[j++] = this.param[i];
        }
        return i;
    }
}
