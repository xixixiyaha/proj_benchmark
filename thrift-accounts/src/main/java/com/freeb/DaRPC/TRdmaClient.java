package com.freeb.DaRPC;

import com.ibm.darpc.*;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class TRdmaClient extends TTransport {
    private static final Logger logger = LoggerFactory.getLogger(TRdmaClient.class.getName());
    private int rdmaTimeout__;
    private int connectTimeout__;

    /* Notice DaRPC初始化参数*/
    private DaRPCClientGroup<RdmaRpcRequest, RdmaRpcResponse> group_;
    private DaRPCClientEndpoint<? extends DaRPCMessage,? extends DaRPCMessage> endpoint;
    private DaRPCStream<RdmaRpcRequest, RdmaRpcResponse> stream_;

    private int mode;
    private int batchSize;
    private int maxInline;
    private int recvQueueDepth;
    private int sendQueueDepth;

    private RdmaRpcRequest req_;
    private RdmaRpcResponse resp_;
    private DaRPCFuture<RdmaRpcRequest,RdmaRpcResponse> future_;

    /* Rdma 交换信息时用的 IpAddr */
    private String host_;
    private int port_;


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
        this.endpoint = clientEp;
        DaRPCStream<RdmaRpcRequest, RdmaRpcResponse> stream = clientEp.createStream();
    }

    @Override
    public boolean isOpen() {
        return this.endpoint != null;
    }

    @Override
    public void open() throws TTransportException {
        if (this.isOpen()) {
            throw new TTransportException(2, "Socket already connected.");
        } else if (this.host_ != null && this.host_.length() != 0) {
            if (this.port_ > 0 && this.port_ <= 65535) {
                if (this.endpoint == null) {
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
            this.endpoint.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int read(byte[] bytes, int offset, int len) throws TTransportException {
        //TODO Notice
        if(this.future_==null){
            try {
                throw new Exception("Future is null! not waiting a resp");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            this.future_.get(this.rdmaTimeout__, TimeUnit.MILLISECONDS);
            return this.future_.getReceiveMessage().getParam(bytes,offset,len);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void write(byte[] bytes, int offset, int len) throws TTransportException {
        if(this.req_ == null){
            this.req_ = new RdmaRpcRequest();
        }
        //TODO cmd length
        // TODO check 这个的获得方法 别是系统中断啥的
        this.req_.setTime(System.currentTimeMillis());
        //TODO check 这个不会被多次调用
        this.req_.setParam(bytes,offset,len);
    }

    public void flush(){
        //TODO 差流控
        this.resp_ = new RdmaRpcResponse();
        try {
            this.future_ = this.stream_.request(this.req_, this.resp_, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setRdmaTimeout__(int rdmaTimeout__) {
        this.rdmaTimeout__ = rdmaTimeout__;
    }

    public void setConnectTimeout__(int connectTimeout__) {
        this.connectTimeout__ = connectTimeout__;
    }

    public void setEndpoint(DaRPCClientEndpoint<? extends DaRPCMessage, ? extends DaRPCMessage> endpoint) {
        this.endpoint = endpoint;
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
