package com.freeb.thrift.AccountClients;

import com.freeb.Clients.AccountClients;
import com.freeb.DaRPC.RawVersion.RdmaRpcProtocol;
import com.freeb.DaRPC.RawVersion.RdmaRpcRequest;
import com.freeb.DaRPC.RawVersion.RdmaRpcResponse;
import com.freeb.Utils.LockObjectPool;
import com.ibm.darpc.DaRPCClientEndpoint;
import com.ibm.darpc.DaRPCClientGroup;
import com.ibm.darpc.DaRPCStream;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

public class AccountForeignClients extends AccountClients implements Closeable {
    private static String search_host = "bm-search-server";
    private static int search_port = 8080;

    private DaRPCClientGroup<RdmaRpcRequest, RdmaRpcResponse> group_;
    private final LockObjectPool<ThriftSearchClientImpl> clientPool_;
    private static final Logger logger = LoggerFactory.getLogger(AccountForeignClients.class.getName());

    public AccountForeignClients(String hostt,int portt,int timeout,int maxInline, int sendQueueDepth,int recvQueueDepth,int poolSize,int endpointSize){
       logger.info("host = "+hostt+" port = "+ portt+" maxInline = "+maxInline+" send/recvDepth = "+sendQueueDepth);
        try {
            group_ = DaRPCClientGroup.createClientGroup(new RdmaRpcProtocol(), timeout, maxInline, recvQueueDepth, sendQueueDepth);
            logger.info("AccountForeignClients@ create Group Done");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(poolSize%endpointSize!=0){
            logger.warn("poolSize%endpointSize is not 0, poolSize={} endpointSize={}",poolSize,endpointSize);
        }
        clientPool_ = new LockObjectPool<ThriftSearchClientImpl>(poolSize);
//        DaRPCEndpoint<?,?>[] rpcConnections = new DaRPCEndpoint[endpointSize];
        int transPerEndpoint = (int) Math.ceil(poolSize/endpointSize);
        int poolNum = 0;
        try {
            assert group_ != null;
            InetSocketAddress address = new InetSocketAddress(hostt, portt);
            for (int i = 0; i < endpointSize; i++){
                DaRPCClientEndpoint<RdmaRpcRequest, RdmaRpcResponse> clientEp = group_.createEndpoint();
                clientEp.connect(address, 1000);
//                rpcConnections[i] = clientEp;
                DaRPCStream<RdmaRpcRequest,RdmaRpcResponse> stream = clientEp.createStream();
                for(int j = 0;(poolNum<poolSize)&&(j<transPerEndpoint);j++){
                    clientPool_.setObject(poolNum,new ThriftSearchClientImpl(clientEp,stream));
                    poolNum +=1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AccountForeignClients(String hostt,int portt){
        clientPool_ = new LockObjectPool<>(16,()->new ThriftSearchClientImpl(hostt, portt));
    }

    public AccountForeignClients(){
        // This is used when Account serve as Server
        clientPool_ = new LockObjectPool<>(16,()->new ThriftSearchClientImpl(search_host, search_port));
    }


    @Override
    public void close() throws IOException {
        // It is ok to close an endpoint for multiple times => later ones will do nothing and just return
        clientPool_.close();
    }

    @Override
    protected AccountClients getClient() {
        return this;
    }

    @Override
    public List<Long> IdealResEfficiencyTest(Integer totalComputationLoad, Integer threadNum) {
        ThriftSearchClientImpl client = clientPool_.borrow();
        try{
            List<Long> re = client.client_.IdealResEfficiencyTest(totalComputationLoad,threadNum);
            logger.debug("IdealResEfficiencyTest result = "+re);
            return re;
        } catch (TException e) {
            e.printStackTrace();
        }finally {
            clientPool_.release(client);
        }
        return null;
    }

}
