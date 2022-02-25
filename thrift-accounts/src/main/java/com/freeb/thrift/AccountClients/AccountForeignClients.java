package com.freeb.thrift.AccountClients;

import com.freeb.Clients.AccountClients;
import com.freeb.DaRPC.RdmaRpcProtocol;
import com.freeb.DaRPC.RdmaRpcRequest;
import com.freeb.DaRPC.RdmaRpcResponse;
import com.freeb.Utils.LockObjectPool;
import com.ibm.darpc.DaRPCClientGroup;
import org.apache.commons.cli.*;
import org.apache.thrift.TException;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.stream.StreamSupport;

public class AccountForeignClients extends AccountClients implements Closeable {
    private static String search_host = "bm-search-server";
    private static int search_port = 8080;
    static {
        System.out.println("in AccountForeignClients");
    }
//    private final LockObjectPool<ThriftSearchClientImpl> clientPool = new LockObjectPool<>(32,()->new ThriftSearchClientImpl(search_host, search_port));
    private DaRPCClientGroup<RdmaRpcRequest, RdmaRpcResponse> group_;
    private final LockObjectPool<ThriftSearchClientImpl> clientPool ;

    public AccountForeignClients(String hostt,int portt,int timeout,int maxInline, int sendQueueDepth,int recvQueueDepth) throws Exception {
        System.out.println("AccountForeignClients@ == IN ==");
        System.out.println("host = "+hostt+" port = "+ portt+" maxInline = "+maxInline+" send/recvDepth = "+sendQueueDepth);


        group_ = DaRPCClientGroup.createClientGroup(new RdmaRpcProtocol(), 3000, maxInline, recvQueueDepth, sendQueueDepth);
        System.out.println("AccountForeignClients@ create Group Done");

        this.clientPool = new LockObjectPool<>(2, () -> {
            try {
                return new ThriftSearchClientImpl(hostt, portt, group_.createEndpoint());
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("AccountForeignClients@ create Endpoint Failed");
            return null;
        });
    }

    public AccountForeignClients(){
        clientPool = new LockObjectPool<>(32,()->new ThriftSearchClientImpl(search_host, search_port));

    }

    @Override
    public void close() throws IOException {
        clientPool.close();
    }

    @Override
    protected AccountClients getClient() {
        return this;
    }

    @Override
    public List<Long> IdealResEfficiencyTest(Integer totalComputationLoad, Integer threadNum) {
        ThriftSearchClientImpl client = clientPool.borrow();
        System.out.println("IdealResEfficiencyTest/AccountForeignClients");
        try{
            List<Long> re = client.client.IdealResEfficiencyTest(totalComputationLoad,threadNum);
            List<Long> re2 = client.client.IdealResEfficiencyTest(totalComputationLoad,threadNum);
            re.addAll(re2);
            return re;
        } catch (TException e) {
            e.printStackTrace();
        }finally {
            clientPool.release(client);
        }
        return null;
    }

    public static void main(String[] args){
        AccountForeignClients clients = new AccountForeignClients();
        List<Long> re = clients.IdealResEfficiencyTest(100,1);
        System.out.println(re);
    }
}
