package com.freeb.jVerb;

import com.ibm.disni.verbs.SVCPostSend;
import org.apache.thrift.TByteArrayOutputStream;
import org.apache.thrift.transport.TMemoryInputTransport;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;

public class TRdmaServerJVerbTrans extends TTransport {
    private static final Logger logger = LoggerFactory.getLogger(TRdmaServerJVerbTrans.class.getName());


    private TRdmaServerEndpoint endpoint_;
    private final byte[] i32buf = new byte[4];
    private final TByteArrayOutputStream writeBuffer_ = new TByteArrayOutputStream(1024);
    private final TMemoryInputTransport readBuffer_ = new TMemoryInputTransport(new byte[1024]);
    private int ticket_;
    private boolean isRead = false;


    public void setEndpoint(TRdmaServerEndpoint endpoint){
        this.endpoint_ = endpoint;
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public void open() throws TTransportException {

    }

    @Override
    public void close() {
        //TODO 这个销毁可以直接杀线程吗
    }

    @Override
    public int read(byte[] bytes, int offset, int len) throws TTransportException {
        if(!isRead){
            //todo throw exception
            return -1;
        }
        return this.readBuffer_.read(bytes,offset,len);
    }

    public byte[] getI32buf(){
        return this.i32buf;
    }
    public byte[] getRawReadBuf(){
        return this.readBuffer_.getBuffer();
    }

    public void updateReadBuff(ByteBuffer buffer){
        ticket_ = buffer.getInt();
        buffer.get(this.i32buf,0,4);
        int size = decodeFrameSize(this.i32buf);
        buffer.get(readBuffer_.getBuffer(),0,size);
        isRead = true;
    }

    @Override
    public void flush(){

        this.isRead = false;
        SVCPostSend p;
        while (true){
            try {
                if (!((p = endpoint_.getSendWQ())==null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                endpoint_.pollOnce();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        int len = this.writeBuffer_.len();
        encodeFrameSize(len,this.i32buf);
        try {
            int idx = (int)p.getWrMod(0).getWr_id();
            endpoint_.writeInt(idx,ticket);
            endpoint_.writeBuf(idx,this.i32buf,0,4);
            endpoint_.writeBuf(idx,writeBuffer_.get(),0,len);
            writeBuffer_.reset();
            try {
                this.endpoint_.request(p);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void write(byte[] bytes, int offset, int len) throws TTransportException {
        writeBuffer_.write(bytes,offset,len);
    }


    public static final void encodeFrameSize(int frameSize, byte[] buf) {
        buf[0] = (byte)(255 & frameSize >> 24);
        buf[1] = (byte)(255 & frameSize >> 16);
        buf[2] = (byte)(255 & frameSize >> 8);
        buf[3] = (byte)(255 & frameSize);
    }

    public static final int decodeFrameSize(byte[] buf) {
        return (buf[0] & 255) << 24 | (buf[1] & 255) << 16 | (buf[2] & 255) << 8 | buf[3] & 255;
    }
}
