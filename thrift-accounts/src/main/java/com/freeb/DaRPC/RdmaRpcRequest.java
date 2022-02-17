package com.freeb.DaRPC;
import com.ibm.darpc.DaRPCMessage;
import org.apache.commons.math3.exception.OutOfRangeException;

import java.io.IOException;
import java.nio.ByteBuffer;

public class RdmaRpcRequest implements DaRPCMessage{

    public static int SERIALIZED_SIZE = 200;
    public static int SERIALIZE_LENGTH = 184;
    private int cmd_;
    private long time_;
    private byte[] length_ = new byte[4];
    private byte[] param_ = new byte[SERIALIZE_LENGTH];
    private int pos = 0;

    public void clear(){
        this.pos = 0;
    }
    public int getPosition(){
        return this.pos;
    }

    @Override
    public int write(ByteBuffer byteBuffer) throws IOException {
        byteBuffer.putInt(cmd_);
        byteBuffer.putLong(time_);
        byteBuffer.put(byteBuffer.position(), length_,0,4);
        byteBuffer.put(byteBuffer.position(), param_,0,SERIALIZE_LENGTH);
        return SERIALIZED_SIZE;
    }

    @Override
    public void update(ByteBuffer byteBuffer) throws IOException {
        cmd_ = byteBuffer.getInt();
        time_ = byteBuffer.getLong();
        byteBuffer.get(byteBuffer.position(), length_,0,4);
        byteBuffer.get(byteBuffer.position(), param_,0,SERIALIZE_LENGTH);
    }

    @Override
    public int size() {
        return SERIALIZED_SIZE;
    }

    @Override
    public String toString() {
        String strParam = new String(param_,0,SERIALIZE_LENGTH);
        return "RdmaRpcRequest{" +
                "cmd=" + cmd_ +
                ", time=" + time_ +
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


    public void writeToParam(byte[] bytes, int offset, int len) {
        //TODO Notice
        for(int j=offset;j<offset+len&&pos<SERIALIZE_LENGTH;j++){
            this.param_[pos++] = bytes[j];
        }
        if(pos == SERIALIZE_LENGTH){
            throw new OutOfRangeException(pos,0,SERIALIZE_LENGTH);
        }
    }



    public void setCmd_(int cmd_) {
        this.cmd_ = cmd_;
    }

    public void setTime_(long time_) {
        this.time_ = time_;
    }

    public void setLength_(byte[] i32buf) {
        System.arraycopy(i32buf, 0,this.length_, 0, 4);
    }
}
