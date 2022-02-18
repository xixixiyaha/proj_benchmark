package com.freeb.DaRPC;

import com.ibm.darpc.DaRPCServerEndpoint;
import com.ibm.darpc.DaRPCServerGroup;
import com.ibm.darpc.DaRPCService;
import com.ibm.disni.RdmaServerEndpoint;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.server.ServerContext;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TNonblockingServerTransport;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Selector;

public class TServerRdmaRaw extends TServer {
    DaRPCServerGroup<RdmaRpcRequest,RdmaRpcResponse> group_;
    RdmaServerEndpoint<DaRPCServerEndpoint<RdmaRpcRequest, RdmaRpcResponse>> endpoint_;
    long[] clusterAffinities;
    int maxinline;
    boolean polling;
    int recvQueue,sendQueue,wqSize;

    private String host;

    protected TServerRdmaRaw(AbstractServerArgs args, DaRPCService<RdmaRpcRequest, RdmaRpcResponse> rpcService) throws Exception {
        super(args);

        group_= DaRPCServerGroup.createServerGroup(rpcService, clusterAffinities, -1, maxinline, polling, recvQueue, sendQueue, wqSize, 32);

        try {
            this.endpoint_ = group_.createServerEndpoint();
            InetSocketAddress address = new InetSocketAddress(host, 1919);
            this.endpoint_.bind(address, 100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void serve() {
		// rdma has no "listen()"
        while (this.stopped_){
            DaRPCServerEndpoint<RdmaRpcRequest, RdmaRpcResponse> client = null;
            TProcessor processor = null;
            TTransport inputTransport = null;
            TTransport outputTransport = null;
            TProtocol inputProtocol = null;
            TProtocol outputProtocol = null;
            ServerContext connectionContext = null;

            try {
                client = this.endpoint_.accept();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
