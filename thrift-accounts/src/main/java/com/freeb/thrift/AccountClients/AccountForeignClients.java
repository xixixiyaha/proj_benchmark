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

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

public class AccountForeignClients extends AccountClients implements Closeable {
    private static String search_host = "bm-search-server";
    private static int search_port = 8080;
    static {
        System.out.println("in AccountForeignClients");
    }
//    private final LockObjectPool<ThriftSearchClientImpl> clientPool = new LockObjectPool<>(32,()->new ThriftSearchClientImpl(search_host, search_port));
    private DaRPCClientGroup<RdmaRpcRequest, RdmaRpcResponse> group_;
    private final LockObjectPool<ThriftSearchClientImpl> clientPool_;

    public AccountForeignClients(String hostt,int portt,int timeout,int maxInline, int sendQueueDepth,int recvQueueDepth,int poolSize){
        System.out.println("AccountForeignClients@ == IN ==");
        System.out.println("host = "+hostt+" port = "+ portt+" maxInline = "+maxInline+" send/recvDepth = "+sendQueueDepth);


        try {
            group_ = DaRPCClientGroup.createClientGroup(new RdmaRpcProtocol(), timeout, maxInline, recvQueueDepth, sendQueueDepth);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("AccountForeignClients@ create Group Done");

//        this.clientPool = new LockObjectPool<>(poolSize, () -> {
//            try {
//                return new ThriftSearchClientImpl(hostt, portt, group_.createEndpoint());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            System.out.println("AccountForeignClients@ create Endpoint Failed");
//            return null;
//        });
        DaRPCClientEndpoint<RdmaRpcRequest,RdmaRpcResponse> endpoint1 = null;
        DaRPCClientEndpoint<RdmaRpcRequest,RdmaRpcResponse> endpoint2 = null;
        DaRPCStream<RdmaRpcRequest,RdmaRpcResponse> s1=null;
        DaRPCStream<RdmaRpcRequest,RdmaRpcResponse> s2=null;
        try {
            endpoint1 = group_.createEndpoint();
            endpoint2 = group_.createEndpoint();
            InetSocketAddress address = new InetSocketAddress(hostt, portt);
            endpoint1.connect(address,1000);
            s1 = endpoint1.createStream();
            endpoint2.connect(address,1000);
            s2 =endpoint2.createStream();
        } catch (Exception e) {
            e.printStackTrace();
        }


        clientPool_ = new LockObjectPool<ThriftSearchClientImpl>(poolSize);
        for(int p = 0;p<poolSize;p++){
            if(p<poolSize/2){
                this.clientPool_.setObject(p,new ThriftSearchClientImpl(endpoint1,s1));
            }else {
                this.clientPool_.setObject(p,new ThriftSearchClientImpl(endpoint2,s2));
            }
        }
    }

    public AccountForeignClients(String hostt,int portt){
        clientPool_ = new LockObjectPool<>(16,()->new ThriftSearchClientImpl(hostt, portt));
    }

    public AccountForeignClients(){
        clientPool_ = new LockObjectPool<>(16,()->new ThriftSearchClientImpl(search_host, search_port));
    }


    @Override
    public void close() throws IOException {
        clientPool_.close();
    }

    @Override
    protected AccountClients getClient() {
        return this;
    }

    @Override
    public List<Long> IdealResEfficiencyTest(Integer totalComputationLoad, Integer threadNum) {
        ThriftSearchClientImpl client = clientPool_.borrow();
//        System.out.println("IdealResEfficiencyTest/AccountForeignClients");
        try{
            List<Long> re = client.client.IdealResEfficiencyTest(totalComputationLoad,threadNum);
//            List<Long> re2 = client.client.IdealResEfficiencyTest(totalComputationLoad,threadNum);
//            re.addAll(re2);
//            System.out.println(re);
            return re;
        } catch (TException e) {
            e.printStackTrace();
        }finally {
            clientPool_.release(client);
        }
        return null;
    }

//    public static void main(String[] args){
//        AccountForeignClients clients = new AccountForeignClients();
//        List<Long> re = clients.IdealResEfficiencyTest(100,1);
//        System.out.println(re);
//    }
}
