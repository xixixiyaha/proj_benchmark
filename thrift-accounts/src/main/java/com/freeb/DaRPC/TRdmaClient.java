package com.freeb.DaRPC;

import com.ibm.darpc.*;
import org.apache.thrift.TByteArrayOutputStream;
import org.apache.thrift.transport.TMemoryInputTransport;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class TRdmaClient extends TTransport {
    /*
    * Todo: 复用req&resp 以及 清空 req的时机
    *
    * */
    Boolean isRead = false;
    private static final Logger logger = LoggerFactory.getLogger(TRdmaClient.class.getName());
    private int rdmaTimeout__;
    private int connectTimeout__;

    /* Notice DaRPC初始化参数*/
    private DaRPCClientGroup<RdmaRpcRequest, RdmaRpcResponse> group_;
    private DaRPCClientEndpoint<RdmaRpcRequest,RdmaRpcResponse> endpoint_;

    private int pos_,size_;


    /* Real transport */
    private RdmaRpcRequest req_;
    private RdmaRpcResponse resp_;
    private DaRPCFuture<RdmaRpcRequest,RdmaRpcResponse> future_;
    private DaRPCStream<RdmaRpcRequest, RdmaRpcResponse> stream_;



    /* 传输内部 buf*/
    private final byte[] i32buf = new byte[4];
    private final TByteArrayOutputStream writeBuffer_ = new TByteArrayOutputStream(1024);
    private final TMemoryInputTransport readBuffer_ = new TMemoryInputTransport(new byte[0]);

    public TRdmaClient(TRdma rdmaInstance){

    }

    public TRdmaClient(int timeout){
        this.rdmaTimeout__ = timeout;
        this.connectTimeout__ = timeout;
    }

    @Override
    public boolean isOpen() {
        return this.endpoint_ != null;
    }

    @Override
    public void open() throws TTransportException {

    }

    @Override
    public void close() {
        try {
            //TODO Notice
            this.endpoint_.close();
            this.group_.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int read(byte[] bytes, int offset, int len) throws TTransportException {
        if(!isRead){
            this.readFrame();
            this.isRead = true;
            return this.readBuffer_.read(bytes, offset, len);
        }
        return this.readBuffer_.read(bytes, offset, len);
    }




    private void readFrame() throws TTransportException {

        if(this.future_==null){
            try {
                throw new Exception("Future is null! not waiting a resp");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            //TODO switch mode
            this.future_.get(this.rdmaTimeout__, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        this.resp_ = this.future_.getReceiveMessage();

        int size = decodeFrameSize(this.i32buf);
        if (size < 0) {
            this.close();
            throw new TTransportException(5, "Read a negative frame size (" + size + ")!");
        } else if (size > RdmaRpcResponse.SERIALIZED_SIZE) {
            this.close();
            throw new TTransportException(5, "Frame size (" + size + ") larger than max length (" + RdmaRpcResponse.SERIALIZED_SIZE + ")!");
        } else {
            byte[] buff = new byte[size];
            this.pos_ = 0;
            this.size_ = size;
            this.req_.clear();
            this.resp_.readFromParam(buff,0, size);
            this.readBuffer_.reset(buff);
        }
        // TODO 这里是不是可以释放 send & recv
    }

    @Override
    public void write(byte[] bytes, int offset, int len) throws TTransportException {
        int wrote= this.req_.writeToParam(bytes,offset,len);
        this.pos_+=wrote;    }

    public void flush(){
        this.isRead = false;
        this.resp_.clear();
        int len = this.req_.getBufferPosition();
        this.pos_ = 0;
        encodeFrameSize(len,this.i32buf);
        try {
            this.future_ = this.stream_.request(this.req_, this.resp_, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void setRdmaTimeout__(int rdmaTimeout__) {
        this.rdmaTimeout__ = rdmaTimeout__;
    }

    public void setConnectTimeout__(int connectTimeout__) {
        this.connectTimeout__ = connectTimeout__;
    }

    public void setEndpoint_(DaRPCClientEndpoint<RdmaRpcRequest, RdmaRpcResponse> endpoint_) {
        this.endpoint_ = endpoint_;
    }


}
