package com.freeb.DaRPC;

import com.ibm.darpc.DaRPCServerEndpoint;
import com.ibm.darpc.DaRPCServerEvent;
import com.ibm.darpc.DaRPCService;
import org.apache.thrift.TException;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportFactory;

import java.io.IOException;

public class TRdmaService extends RdmaRpcProtocol implements DaRPCService<RdmaRpcRequest, RdmaRpcResponse>  {

    private Boolean testMode=true;
    public static class Args extends TServer.AbstractServerArgs<Args>{

        public Args(TServerTransport transport) {
            super(transport);
        }
    }

    TProcessor processor_;
    TProtocolFactory protocolFactory_;
    TTransportFactory transFactory_;


    public TRdmaService(TProcessor processor){
        this.processor_ = processor;
    }

    public void setProtocolFactory(TProtocolFactory factory){
        this.protocolFactory_ = factory;
    }

    @Override
    public void processServerEvent(DaRPCServerEvent<RdmaRpcRequest, RdmaRpcResponse> event) throws IOException {
        // 取出 Msg => 给上层解析消费 => 获得 Request => 送走
        // protocol(transport)
        RdmaRpcRequest request = event.getReceiveMessage();
        RdmaRpcResponse response = event.getSendMessage();
        TRdmaServerRaw transport = new TRdmaServerRaw(request,response);
        TProtocol protocol = protocolFactory_.getProtocol(transport);
        try {
            processor_.process(protocol,protocol);
        } catch (TException e) {
            e.printStackTrace();
        }
        //req=>inputTrans resp=>outputTrans
        if(!testMode){
            event.triggerResponse();
        }

    }
    public void process(com.freeb.DaRPC.DaRPCServerEvent<RdmaRpcRequest, RdmaRpcResponse> event){
        RdmaRpcRequest request = event.getReceiveMessage();
        RdmaRpcResponse response = event.getSendMessage();
        TRdmaServerRaw transport = new TRdmaServerRaw(request,response);
        TProtocol protocol = protocolFactory_.getProtocol(transport);
        try {
            processor_.process(protocol,protocol);
        } catch (TException e) {
            e.printStackTrace();
        }
        //req=>inputTrans resp=>outputTrans
    }

    @Override
    public void open(DaRPCServerEndpoint<RdmaRpcRequest, RdmaRpcResponse> daRPCServerEndpoint) {

    }

    @Override
    public void close(DaRPCServerEndpoint<RdmaRpcRequest, RdmaRpcResponse> daRPCServerEndpoint) {

    }
}
