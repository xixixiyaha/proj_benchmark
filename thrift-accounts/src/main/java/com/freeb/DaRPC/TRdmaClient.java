package com.freeb.DaRPC;

import com.ibm.darpc.*;
import org.apache.thrift.TByteArrayOutputStream;
import org.apache.thrift.transport.TMemoryInputTransport;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class TRdmaClient extends TTransport {
    /*
    * Todo: 复用req&resp 以及 清空 req的时机
    *
    * */

    private static final Logger logger = LoggerFactory.getLogger(TRdmaClient.class.getName());
    private int rdmaTimeout__;
    private int connectTimeout__;

    /* Notice DaRPC初始化参数*/
    private DaRPCClientGroup<RdmaRpcRequest, RdmaRpcResponse> group_;
    private DaRPCClientEndpoint<RdmaRpcRequest,RdmaRpcResponse> endpoint_;

    private int mode;
    private int batchSize;
    private int maxInline;
    private int recvQueueDepth;
    private int sendQueueDepth;

    /* Real transport */
    private RdmaRpcRequest req_;
    private RdmaRpcResponse resp_;
    private DaRPCFuture<RdmaRpcRequest,RdmaRpcResponse> future_;
    private DaRPCStream<RdmaRpcRequest, RdmaRpcResponse> stream_;

    /* Rdma 交换信息时用的 IpAddr */
    private String host_;
    private int port_;

    /* 传输内部 buf*/
    private final byte[] i32buf = new byte[4];
    private final TByteArrayOutputStream writeBuffer_ = new TByteArrayOutputStream(1024);
    private final TMemoryInputTransport readBuffer_ = new TMemoryInputTransport(new byte[0]);



    public TRdmaClient(String host,int port){
        this.host_ = host;
        this.port_ = port;
    }

    public TRdmaClient(String host,int port,int timeout){
        this.host_ = host;
        this.port_ = port;
        this.rdmaTimeout__ = timeout;
        this.connectTimeout__ = timeout;
    }


    private void initRdma() throws Exception {

        RdmaRpcProtocol rpcProtocol = new RdmaRpcProtocol();
        if(this.group_!=null){
            this.group_ = DaRPCClientGroup.createClientGroup(rpcProtocol, 100, this.maxInline, this.recvQueueDepth, this.sendQueueDepth);
        }
        InetSocketAddress address = new InetSocketAddress(this.host_, this.port_);

        DaRPCClientEndpoint<RdmaRpcRequest, RdmaRpcResponse> clientEp = this.group_.createEndpoint();
        clientEp.connect(address, 1000);
        this.endpoint_ = clientEp;
        this.stream_ = this.endpoint_.createStream();
    }

    @Override
    public boolean isOpen() {
        return this.endpoint_ != null;
    }

    @Override
    public void open() throws TTransportException {
        if (this.isOpen()) {
            throw new TTransportException(2, "Socket already connected.");
        } else if (this.host_ != null && this.host_.length() != 0) {
            if (this.port_ > 0 && this.port_ <= 65535) {
                if (this.endpoint_ == null) {
                    try {
                        this.initRdma();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                throw new TTransportException(1, "Invalid port " + this.port_);
            }
        } else {
            throw new TTransportException(1, "Cannot open null host.");
        }
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

        int got = this.readBuffer_.read(bytes, offset, len);
        if (got > 0) {
            return got;
        } else {
            this.readFrame();
            return this.readBuffer_.read(bytes, offset, len);
        }
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
        this.resp_.getLength_(this.i32buf);
        int size = decodeFrameSize(this.i32buf);
        if (size < 0) {
            this.close();
            throw new TTransportException(5, "Read a negative frame size (" + size + ")!");
        } else if (size > RdmaRpcResponse.SERIALIZE_LENGTH) {
            this.close();
            throw new TTransportException(5, "Frame size (" + size + ") larger than max length (" + RdmaRpcResponse.SERIALIZE_LENGTH + ")!");
        } else {
            byte[] buff = new byte[size];
            this.resp_.readFromParam(buff,0, size);
            this.readBuffer_.reset(buff);
        }
        // TODO 这里是不是可以释放 send & recv
    }

    @Override
    public void write(byte[] bytes, int offset, int len) throws TTransportException {
        if(this.req_ == null){
            this.req_ = new RdmaRpcRequest();
        }
        this.req_.writeToParam(bytes,offset,len);
    }

    public void flush(){
        //TODO cmd
        this.req_.setTime_(System.currentTimeMillis());
        int len = this.req_.getPosition();
        encodeFrameSize(len,this.i32buf);
        this.req_.setLength_(this.i32buf);
        //TODO 差流控
        this.resp_ = new RdmaRpcResponse();
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

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public void setMaxInline(int maxInline) {
        this.maxInline = maxInline;
    }

    public void setRecvQueueDepth(int recvQueueDepth) {
        this.recvQueueDepth = recvQueueDepth;
    }

    public void setSendQueueDepth(int sendQueueDepth) {
        this.sendQueueDepth = sendQueueDepth;
    }
}
