package com.freeb.DaRPC.jVerbVersion;

import com.freeb.DaRPC.RawVersion.RdmaRpcRequest;
import com.freeb.DaRPC.RawVersion.RdmaRpcResponse;
import com.freeb.DaRPC.TRdma;
import com.ibm.darpc.*;
import com.ibm.disni.verbs.SVCPostSend;
import org.apache.thrift.TByteArrayOutputStream;
import org.apache.thrift.transport.TMemoryInputTransport;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TRdmaClientJVerbTrans extends TTransport {
    /*
    * Todo: 复用req&resp 以及 清空 req的时机
    *
    * */
    Boolean isRead = false;
    private static final Logger logger = LoggerFactory.getLogger(TRdmaClientJVerbTrans.class.getName());
    private int rdmaTimeout_,connTimeout_;

    /* Rdma startup IpAddr */
    private String host_;
    private int port_;

    private TRdmaClientEndpoint endpoint_;

    /* 传输内部 buf*/
    private final byte[] i32buf = new byte[4];
    private final TByteArrayOutputStream writeBuffer_ = new TByteArrayOutputStream(1024);
    private final TMemoryInputTransport readBuffer_ = new TMemoryInputTransport(new byte[0]);
    private AtomicInteger ticket;

    public TRdmaClientJVerbTrans(TRdmaClientEndpoint ep,String host,int port,int timeout){
        this.endpoint_ = ep;
        this.host_ = host;
        this.port_ = port;
        this.rdmaTimeout_ = timeout;
        this.connTimeout_ = timeout;
        ticket = new AtomicInteger(0);
    }

    @Override
    public boolean isOpen() {
        return this.endpoint_.isConnected();
    }

    @Override
    public void open() throws TTransportException {
        if(isOpen()){
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
        logger.info("endpoint connect success!");
    }

    private void init() throws Exception {
        InetSocketAddress address = new InetSocketAddress(this.host_, this.port_);
        this.endpoint_.connect(address,connTimeout_);
    }

    @Override
    public void close() {
        try {
            this.endpoint_.close();
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

        ByteBuffer recvContent = this.endpoint_.eventTake(ticket.get());
        // TODO@ high 看一下 recv 何时free()/buffer清空
        recvContent.get(this.i32buf,0,4);
        int size = decodeFrameSize(this.i32buf);
        if (size < 0) {
            this.close();
            throw new TTransportException(5, "Read a negative frame size (" + size + ")!");
        } else if (size > RdmaRpcResponse.SERIALIZED_SIZE) {
            this.close();
            throw new TTransportException(5, "Frame size (" + size + ") larger than max length (" + RdmaRpcResponse.SERIALIZED_SIZE + ")!");
        } else {
            byte[] buff = new byte[size];
            recvContent.get(buff,0,size);
            this.readBuffer_.reset(buff);
            endpoint_.freeRecv(ticket.getAndIncrement());
            // TODO@ high 看一下 recv 何时free()/buffer清空
        }

    }

    @Override
    public void write(byte[] bytes, int offset, int len) throws TTransportException {
          writeBuffer_.write(bytes,offset,len);
    }


    @Override
    public void flush(){
        this.isRead=false;
        ByteBuffer buf = null;

        while (true){
            try {
                if (!((buf = this.endpoint_.getSendWQ(ticket.get()))==null)) break;
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
        buf.putInt(this.ticket.get());
        buf.put(this.i32buf,0,4);
        buf.put(writeBuffer_.get(),0,len);
        writeBuffer_.reset();
        try {
            this.endpoint_.request(ticket.get());
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

    public void setRdmaTimeout_(int rdmaTimeout) {
        this.rdmaTimeout_ = rdmaTimeout;
    }

    public void setConnectTimeout_(int connectTimeout) {
        this.connTimeout_ = connectTimeout;
    }


}
