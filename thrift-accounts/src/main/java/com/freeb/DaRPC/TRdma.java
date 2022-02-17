package com.freeb.DaRPC;

import com.ibm.darpc.DaRPCClientEndpoint;
import com.ibm.darpc.DaRPCClientGroup;
import com.ibm.darpc.DaRPCStream;
import org.apache.thrift.transport.TTransportException;

import java.net.InetSocketAddress;
import java.util.concurrent.ArrayBlockingQueue;

public class TRdma {

    private Boolean isOpen = false;
    /* Rdma startup IpAddr */
    private String host_;
    private int port_;

    /* rdma Conn */
    private DaRPCClientGroup<RdmaRpcRequest, RdmaRpcResponse> group_;
    private DaRPCClientEndpoint<RdmaRpcRequest,RdmaRpcResponse> endpoint_;
    private DaRPCStream<RdmaRpcRequest, RdmaRpcResponse> stream_;

    /* rdma var*/
    private static int DEFAULT_QUEUE_SIZE = 20;
    private int maxInline;
    private int recvQueueDepth;
    private int sendQueueDepth;

    private int mode;
    private int batchSize;

    /* provided var */
    ArrayBlockingQueue<RdmaRpcResponse> freeResponsePool;

    public TRdma(String host,int port){
        this.host_ = host;
        this.port_ = port;
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
        if(this.recvQueueDepth>0){
            this.freeResponsePool = new ArrayBlockingQueue<RdmaRpcResponse>(this.recvQueueDepth);
        }else {
            this.freeResponsePool = new ArrayBlockingQueue<>(DEFAULT_QUEUE_SIZE);
            this.sendQueueDepth = this.recvQueueDepth = DEFAULT_QUEUE_SIZE;
        }
        for (int i = 0; i < this.recvQueueDepth; i++){
            RdmaRpcResponse response = new RdmaRpcResponse();
            this.freeResponsePool.add(response);
        }
    }


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
