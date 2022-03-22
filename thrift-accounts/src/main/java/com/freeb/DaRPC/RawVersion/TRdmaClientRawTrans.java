package com.freeb.DaRPC.RawVersion;

import com.ibm.darpc.*;
import org.apache.thrift.TByteArrayOutputStream;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class TRdmaClientRawTrans extends TTransport {

    protected static final int DEFAULT_MAX_LENGTH = 16384000;

    private int rdmaTimeout__=3000;
    private final byte[] i32buf = new byte[4];
    private final TByteArrayOutputStream writeBuffer_ = new TByteArrayOutputStream(1024);
    private Boolean isRead = false;

    /* Rdma startup IpAddr */
    private String host_;
    private int port_;
    /* rdma Conn */
    private DaRPCClientGroup<RdmaRpcRequest, RdmaRpcResponse> group_;
    private DaRPCClientEndpoint<RdmaRpcRequest,RdmaRpcResponse> endpoint_;
    private DaRPCStream<RdmaRpcRequest, RdmaRpcResponse> stream_;

    /* rdma var*/
//    private static int DEFAULT_QUEUE_SIZE = 20;
    private int maxInline_;
    private int recvQueueDepth_ = 1;
    private int sendQueueDepth_ = 1;
    private int pipelineSize_ = 1;


    /* Real transport */
    private RdmaRpcRequest req_;
    private RdmaRpcResponse resp_;
    private DaRPCFuture<RdmaRpcRequest,RdmaRpcResponse> future_;
//    private ArrayBlockingQueue<RdmaRpcResponse> freeResponses_;

    private int mode;
    private int batchSize;

    private Boolean testMode=false;

    public TRdmaClientRawTrans(String host, int port, int maxInline, int sendQueueDepth, int recvQueueDepth){
        this.host_ = host;
        this.port_ = port;
        this.maxInline_ = maxInline;
        this.sendQueueDepth_ = sendQueueDepth;
        this.recvQueueDepth_ = recvQueueDepth;
    }

    public TRdmaClientRawTrans(DaRPCClientEndpoint<RdmaRpcRequest,RdmaRpcResponse> endpoint,DaRPCStream<RdmaRpcRequest,RdmaRpcResponse> s, RdmaRpcRequest req, RdmaRpcResponse resp){
        this.endpoint_ = endpoint;
        this.req_ = req;
        this.resp_ = resp;
        this.stream_ = s;
    }


    public void init() throws Exception {
        if(!testMode){
            this.stream_ = this.endpoint_.createStream();
            return;
        }
        RdmaRpcProtocol rpcProtocol = new RdmaRpcProtocol();
        if (this.group_ == null) {
            this.group_ = DaRPCClientGroup.createClientGroup(rpcProtocol, 100, this.maxInline_, this.recvQueueDepth_, this.sendQueueDepth_);
        }
        InetSocketAddress address = new InetSocketAddress(this.host_, this.port_);
        DaRPCClientEndpoint<RdmaRpcRequest, RdmaRpcResponse> clientEp = this.group_.createEndpoint();
        clientEp.connect(address, 1000);
        this.endpoint_ = clientEp;
        this.stream_ = this.endpoint_.createStream();

        this.pipelineSize_ = Math.min(sendQueueDepth_,recvQueueDepth_);
//        this.freeResponses = new ArrayBlockingQueue<RdmaRpcResponse>(this.pipelineSize_);
//        for(int i=0;i<pipelineSize_;i++){
//            freeResponses.add(new RdmaRpcResponse());
//        }
        this.req_ = new RdmaRpcRequest();
        this.resp_ = new RdmaRpcResponse();
    }

    @Override
    public boolean isOpen() {
        if(testMode){
            return true;
        }
        return this.endpoint_ != null;
    }


    @Override
    public void open() throws TTransportException {
        if(!testMode){
            try {
                this.init();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        if (this.isOpen()) {
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
            this.endpoint_.close();
            if(this.group_!=null){
                this.group_.close();
            }
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
        got =  this.resp_.readFromParam(bytes, offset, len);
        return got;
    }

    private void readFrame() throws Exception {

        //TODO@feature switch mode
        if(testMode){
            try {
                File file = new File("./testResp.txt");
                FileInputStream fis = new FileInputStream(file);
                ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
                byte[] b = new byte[1024];
                int n;
                while ((n = fis.read(b)) != -1) {
                    bos.write(b, 0, n);
                }
                fis.close();
                bos.close();
                //TODO 记得在正式写的部分加上setLimit
                this.resp_.setLimit(bos.size());
                this.resp_.writeToParam(bos.toByteArray(),0,bos.size());
                this.resp_.setPos(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            if(this.future_==null){
                throw new Exception("Future is null! not waiting a resp");
            }
            this.future_.get(this.rdmaTimeout__, TimeUnit.MILLISECONDS);
            this.resp_ = this.future_.getReceiveMessage();
        }
        this.resp_.setLimit(4);
        this.resp_.readFromParam(this.i32buf,0,4);
        this.resp_.consumeBuffer(4);
//        System.out.println("TRdmaClientRawTrans ReadFrame resp_ pos="+this.resp_.getBufferPosition());

        int size = decodeFrameSize(this.i32buf);
        if (size < 0) {
            this.close();
            throw new TTransportException(5, "Read a negative frame size (" + size + ")!");
        } else if (size > RdmaRpcResponse.SERIALIZED_SIZE) {
            this.close();
            throw new TTransportException(5, "Frame size (" + size + ") larger than max length (" + RdmaRpcResponse.SERIALIZED_SIZE + ")!");
        } else {
            this.resp_.setLimit(size+4);
            this.req_.clear();
        }
    }

    @Override
    public void write(byte[] bytes, int offset, int len) throws TTransportException {
        writeBuffer_.write(bytes,offset,len);
    }

    @Override
    public void flush(){
//        System.out.println("TRdmaClientRawTrans Ready to flush()");
        byte[] buf = writeBuffer_.get();
        this.isRead = false;
//        this.resp_=freeResponses.poll();
        int len = writeBuffer_.len();
        writeBuffer_.reset();
        encodeFrameSize(len,this.i32buf);
        this.req_.setLimit(4+len);
        this.req_.writeToParam(i32buf,0,4);
        this.req_.writeToParam(buf,0,len);
//        System.out.println("TRdmaClientRawTrans req_ len="+this.req_.getBufferPosition());

        this.resp_.clear();
        //TODO 流控
        if(!testMode){
            // if it is not local test, send req to the HCA
            try {
//                System.out.println("TRdmaClientRawTrans future this.stream==null?"+(this.stream_ == null));

                this.future_ = this.stream_.request(this.req_, this.resp_, false);
//                System.out.println("TRdmaClientRawTrans future tend");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            System.out.println(this.req_.getBufferPosition());
            File file = new File("./test.txt");
            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            try {
                fos = new FileOutputStream(file);
                bos = new BufferedOutputStream(fos);
                bos.write(this.req_.getParam_());
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
        return this.resp_.getParam_();
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
        return resp_.getBytesRemainingInBuffer();
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
        this.resp_.consumeBuffer(len);
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
        return resp_.getBufferPosition();
    }
    public void clear(){
        this.resp_.clear();
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
        this.maxInline_ = maxInline;
    }

    public void setRecvQueueDepth(int recvQueueDepth) {
        this.recvQueueDepth_ = recvQueueDepth;
    }

    public void setSendQueueDepth(int sendQueueDepth) {
        this.sendQueueDepth_ = sendQueueDepth;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }
}
