package com.freeb.DaRPC;
import com.ibm.darpc.DaRPCMessage;
import org.apache.commons.math3.exception.OutOfRangeException;

import java.io.IOException;
import java.nio.ByteBuffer;

public class RdmaRpcRequest implements DaRPCMessage{

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
        byteBuffer.putInt(cmd);
        byteBuffer.putInt(length);
        byteBuffer.putLong(time);
        byteBuffer.put(byteBuffer.position(),param,0,length);
        return SERIALIZED_SIZE;
    }

    @Override
    public void update(ByteBuffer byteBuffer) throws IOException {
        cmd = byteBuffer.getInt();
        length = byteBuffer.getInt();
        time = byteBuffer.getLong();
        byteBuffer.get(byteBuffer.position(),param,0,length);
    }

    @Override
    public int size() {
        return SERIALIZED_SIZE;
    }

    @Override
    public String toString() {
        String strParam = new String(param,0,length);
        return "RdmaRpcRequest{" +
                "cmd=" + cmd +
                ", length=" + length +
                ", time=" + time +
                ", param=" + strParam +
                '}';
    }

    public static void main(String[] args) throws IOException {
        RdmaRpcRequest req = new RdmaRpcRequest();
        String str = "This is a buffer test";
        ByteBuffer buffer = ByteBuffer.allocate(RdmaRpcRequest.SERIALIZED_SIZE);
        buffer.putInt(25);
        buffer.putInt(str.length());
        buffer.putLong(System.currentTimeMillis());
        System.out.println(buffer.position());
        buffer.put(16,str.getBytes(),0,str.length());
        buffer.clear();
        req.update(buffer);
        System.out.println("=============================");
        System.out.println(req.toString());

        ByteBuffer emptyBuf = ByteBuffer.allocate(RdmaRpcRequest.SERIALIZED_SIZE);
        req.write(emptyBuf);
        emptyBuf.clear();
        System.out.println("=============================");
        System.out.println(emptyBuf.toString());

        RdmaRpcRequest testReq = new RdmaRpcRequest();
        testReq.update(emptyBuf);
        System.out.println("=============================");
        System.out.println(testReq.toString());
    }


    public void setParam(byte[] bytes, int offset, int len) {
        //TODO Notice
        for(int j=offset;j<offset+len&&pos<SERIALIZE_LENGTH;j++){
            this.param[pos++] = bytes[j];
        }
        if(pos == SERIALIZE_LENGTH){
            throw new OutOfRangeException(pos,0,SERIALIZE_LENGTH);
        }
    }



    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
