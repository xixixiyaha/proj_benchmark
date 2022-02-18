package com.freeb.DaRPC;

import com.ibm.darpc.DaRPCServerEndpoint;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.io.IOException;

public class TRdmaServerRaw extends TTransport {
    private Boolean isOpen = false;
    private Boolean isRead = false;
    private int pos_,size_;

    /* Rdma startup IpAddr */
    private String host_;
    private int port_;

    private int rdmaTimeout__;
    // TODO raw 模式下可以remove
    private final byte[] i32buf = new byte[4];

    /* rdma Conn */
//    private DaRPCServerEndpoint<RdmaRpcRequest,RdmaRpcResponse> endpoint_;

    /* rdma var*/
//    private static int DEFAULT_QUEUE_SIZE = 20;
    private int maxInline;
    private int recvQueueDepth = 1;
    private int sendQueueDepth = 1;

    /* Real transport */
    private RdmaRpcRequest req_;
    private RdmaRpcResponse resp_;

    private int mode;
    private int batchSize;

    public TRdmaServerRaw(String host, int port){
        this.host_ = host;
        this.port_ = port;
        this.pos_ = 0;
        this.size_ = -1;
    }

    public TRdmaServerRaw(RdmaRpcRequest req, RdmaRpcResponse resp){
        this.pos_ = 0;
        this.size_ = -1;
        this.req_ = req;
        this.resp_ = resp;
    }

    public void init() throws Exception {

    }
    @Override
    public boolean isOpen() {
        return true;
    }


    @Override
    public void open() throws TTransportException {

    }
    @Override
    public void close() {

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
            got =  this.req_.readFromParam(bytes, offset, len,this.pos_,this.size_);
            this.pos_+=got;
            return got;


    }

    private void readFrame() throws Exception {

            this.req_.getLength_(this.i32buf);
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
                this.resp_.clear();
            }


    }

    @Override
    public void write(byte[] bytes, int offset, int len) throws TTransportException {

        int wrote = this.resp_.writeToParam(bytes,offset,len,this.pos_);

        this.pos_+=wrote;
    }

    @Override
    public byte[] getBuffer() {
        return this.req_.getParam_();
    }

    @Override
    public int getBytesRemainingInBuffer(){
        return this.size_-this.pos_;
    }

    @Override
    public void consumeBuffer(int len) {
        this.pos_+=len;
    }

    @Override
    public int getBufferPosition() {

        return this.pos_;
    }

    public void flush(){
        this.isRead = false;
            this.resp_.setTime_(System.currentTimeMillis());
            int len = this.pos_;
            this.pos_ = 0;
            encodeFrameSize(len,this.i32buf);
            this.resp_.setLength_(this.i32buf);


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
