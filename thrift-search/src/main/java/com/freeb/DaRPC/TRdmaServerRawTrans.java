package com.freeb.DaRPC;

import org.apache.thrift.TByteArrayOutputStream;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TRdmaServerRawTrans extends TTransport {

    private Boolean testMode  = false;
    private Boolean isOpen = false;
    private Boolean isRead = false;

    /* Rdma startup IpAddr */
    private String host_;
    private int port_;

    private int rdmaTimeout__;
    // TODO raw 模式下可以remove
    private final byte[] i32buf = new byte[4];
    private final TByteArrayOutputStream writeBuffer_ = new TByteArrayOutputStream(1024);

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

    public TRdmaServerRawTrans(String host, int port){
        this.host_ = host;
        this.port_ = port;
    }

    public TRdmaServerRawTrans(RdmaRpcRequest req, RdmaRpcResponse resp){
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
        got =  this.req_.readFromParam(bytes, offset, len);
        return got;
    }

    private void readFrame() throws Exception {

        //TODO@feature switch mode
        this.req_.readFromParam(this.i32buf,0,4);
        int size = decodeFrameSize(this.i32buf);
        if (size < 0) {
            this.close();
            throw new TTransportException(5, "Read a negative frame size (" + size + ")!");
        } else if (size > RdmaRpcResponse.SERIALIZED_SIZE) {
            this.close();
            throw new TTransportException(5, "Frame size (" + size + ") larger than max length (" + RdmaRpcResponse.SERIALIZED_SIZE + ")!");
        } else {
            this.req_.setLimit(size+4);
            this.req_.setPos(4);
            this.resp_.clear();
        }
    }

    @Override
    public void write(byte[] bytes, int offset, int len) throws TTransportException {

        writeBuffer_.write(bytes,offset,len);
    }

    @Override
    public byte[] getBuffer() {
        if (!isRead) {
            try {
                this.readFrame();
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.isRead = true;
        }
        return this.req_.getParam_();
    }

    @Override
    public int getBytesRemainingInBuffer(){
        if (!isRead) {
            try {
                this.readFrame();
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.isRead = true;
        }
        return this.req_.getBytesRemainingInBuffer();
    }

    @Override
    public void consumeBuffer(int len) {
        if (!isRead) {
            try {
                this.readFrame();
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.isRead = true;
        }
        this.req_.consumeBuffer(len);
    }

    @Override
    public int getBufferPosition() {
        if (!isRead) {
            try {
                this.readFrame();
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.isRead = true;
        }
        return this.req_.getBufferPosition();
    }

    public void flush(){
        this.isRead = false;
        byte[] buf = writeBuffer_.get();
        this.req_.clear();
        int len = writeBuffer_.len();
        writeBuffer_.reset();
        encodeFrameSize(len,this.i32buf);
        this.resp_.setLimit(4+len);
        this.resp_.writeToParam(i32buf,0,4);
        this.resp_.writeToParam(buf,0,len);
        this.req_.clear();
        if(testMode){
            System.out.println(this.resp_.getBufferPosition());
            File file = new File("./testResp.txt");
            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            try {
                fos = new FileOutputStream(file);
                bos = new BufferedOutputStream(fos);
                bos.write(this.resp_.getParam_());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                if(fos!=null){
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        System.out.println("TRdmaClientRaw flush end");

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
