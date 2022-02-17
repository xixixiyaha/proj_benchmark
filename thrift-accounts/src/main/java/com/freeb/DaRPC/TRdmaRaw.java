package com.freeb.DaRPC;

import com.ibm.darpc.DaRPCClientEndpoint;
import com.ibm.darpc.DaRPCClientGroup;
import com.ibm.darpc.DaRPCFuture;
import com.ibm.darpc.DaRPCStream;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class TRdmaRaw extends TTransport {

    private Boolean isOpen = false;
    private Boolean isRead = false;
    private int pos_,size_;

    /* Rdma startup IpAddr */
    private String host_;
    private int port_;

    private int rdmaTimeout__;
    private final byte[] i32buf = new byte[4];

    /* rdma Conn */
    private DaRPCClientGroup<RdmaRpcRequest, RdmaRpcResponse> group_;
    private DaRPCClientEndpoint<RdmaRpcRequest,RdmaRpcResponse> endpoint_;
    private DaRPCStream<RdmaRpcRequest, RdmaRpcResponse> stream_;

    /* rdma var*/
//    private static int DEFAULT_QUEUE_SIZE = 20;
    private int maxInline;
    private int recvQueueDepth = 1;
    private int sendQueueDepth = 1;

    /* Real transport */
    private RdmaRpcRequest req_;
    private RdmaRpcResponse resp_;
    private DaRPCFuture<RdmaRpcRequest,RdmaRpcResponse> future_;

    private int mode;
    private int batchSize;

    public TRdmaRaw(String host,int port){
        this.host_ = host;
        this.port_ = port;
        this.pos_ = 0;
        this.size_ = -1;
    }

    public void init() throws Exception {
        RdmaRpcProtocol rpcProtocol = new RdmaRpcProtocol();
        if(this.group_!=null){
            this.group_ = DaRPCClientGroup.createClientGroup(rpcProtocol, 100, this.maxInline, this.recvQueueDepth, this.sendQueueDepth);
        }
        InetSocketAddress address = new InetSocketAddress(this.host_, this.port_);

        DaRPCClientEndpoint<RdmaRpcRequest, RdmaRpcResponse> clientEp = this.group_.createEndpoint();
        clientEp.connect(address, 1000);
        this.endpoint_ = clientEp;
        this.stream_ = this.endpoint_.createStream();
        this.req_ = new RdmaRpcRequest();
        this.resp_ = new RdmaRpcResponse();
    }
    @Override
    public boolean isOpen() {
        return this.endpoint_ != null;
    }


    @Override
    public void open() throws TTransportException {
        if (this.isOpen) {
            throw new TTransportException(2, "QP already connected.");
        } else if (this.host_ != null && this.host_.length() != 0) {
            if (this.port_ > 0 && this.port_ <= 65535) {
                if (this.endpoint_ == null) {
                    try {
                        this.init();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                this.isOpen = true;
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
        int got;
        if (!isRead) {
            try {
                this.readFrame();
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.isRead = true;
        }
        got =  this.resp_.readFromParam(bytes, offset, len,this.pos_,this.size_);
        this.pos_+=got;
        return got;
    }




    private void readFrame() throws Exception {

        if(this.future_==null){
            throw new Exception("Future is null! not waiting a resp");
        }

        //TODO switch mode
        this.future_.get(this.rdmaTimeout__, TimeUnit.MILLISECONDS);

        this.resp_ = this.future_.getReceiveMessage();
        this.resp_.getLength_(this.i32buf);
        int size = decodeFrameSize(this.i32buf);
        if (size < 0) {
            this.close();
            throw new TTransportException(5, "Read a negative frame size (" + size + ")!");
        } else if (size > RdmaRpcResponse.PARAM_SIZE) {
            this.close();
            throw new TTransportException(5, "Frame size (" + size + ") larger than max length (" + RdmaRpcResponse.PARAM_SIZE + ")!");
        } else {
            this.pos_ = 0;
            this.size_ = size;
            this.req_.clear();
        }
    }

    @Override
    public void write(byte[] bytes, int offset, int len) throws TTransportException {
        int wrote= this.req_.writeToParam(bytes,offset,len,this.pos_);
        this.pos_+=wrote;
    }

    public void flush(){
        this.isRead = false;
        this.resp_.clear();
        //TODO cmd
        this.req_.setTime_(System.currentTimeMillis());
        int len = this.pos_;
        this.pos_ = 0;
        encodeFrameSize(len,this.i32buf);
        this.req_.setLength_(this.i32buf);
        //TODO 流控
//        this.resp_ = new RdmaRpcResponse();

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

    public void setMaxInline(int maxInline) {
        this.maxInline = maxInline;
    }

    public void setRecvQueueDepth(int recvQueueDepth) {
        this.recvQueueDepth = recvQueueDepth;
    }

    public void setSendQueueDepth(int sendQueueDepth) {
        this.sendQueueDepth = sendQueueDepth;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }
}
