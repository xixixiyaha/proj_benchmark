package com.freeb.SearchClients;

import com.freeb.Clients.SearchClients;
import com.freeb.Enum.SearchOrder;
import com.freeb.Utils.LockObjectPool;
import com.freeb.SearchTypeConvert;
import org.apache.thrift.TException;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class SearchForeignClients extends SearchClients implements Closeable {
    private static String account_host = "bm-account-server";
    private static String prod_host = "bm-prod-server";

    private static int search_port = 8080;

    static {
        System.out.println("in SearchForeignClients");
    }
    // TODO@ high priority port是否可以共用


    private final LockObjectPool<ThriftAccountClientImpl> accClientPool = new LockObjectPool<>(32,()->new ThriftAccountClientImpl(account_host, search_port));
    private final LockObjectPool<ThriftProdClientImpl> prodClientPool = new LockObjectPool<>(32,()->new ThriftProdClientImpl(prod_host, search_port));

    @Override
    public List<Integer> GetAccountTag(Long id) {
        ThriftAccountClientImpl client = accClientPool.borrow();
        try {
            return client.client.GetAccountTag(id);
        } catch (TException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Boolean SetAccountTag(Long id, String jsonStr) {
        ThriftAccountClientImpl client = accClientPool.borrow();
        try {
            return client.client.SetAccountTag(id,jsonStr);
        } catch (TException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Boolean AccountExists(Long id) {
        ThriftAccountClientImpl client = accClientPool.borrow();
        try {
            return client.client.AccountExists(id);
        } catch (TException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ConcurrentHashMap<Integer, Integer> GetUserActiveByCategory(Long id) {
        ThriftProdClientImpl client = prodClientPool.borrow();
        try {
            return (ConcurrentHashMap<Integer, Integer>) client.client.GetUserActiveByCategory(id);
        } catch (TException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Boolean CreateActiveBehavior(Long uid, Long pid, Integer cid) {
        ThriftProdClientImpl client = prodClientPool.borrow();
        try {
            return client.client.CreateActiveBehavior(uid,pid,cid);
        } catch (TException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Long> GetLastestActiveUsers(Integer userNum) {
        ThriftProdClientImpl client = prodClientPool.borrow();
        try {
            return client.client.GetLastestActiveUsers(userNum);
        } catch (TException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public HashSet<Long> GetUserActiveByProduct(Long uid) {
        ThriftProdClientImpl client = prodClientPool.borrow();
        try {
            return (HashSet<Long>) client.client.GetUserActiveByProduct(uid);
        } catch (TException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Long> GetProductByCategory(Integer cid, SearchOrder order, Integer prodNum) {
        ThriftProdClientImpl client = prodClientPool.borrow();
        try {
            return client.client.GetProductByCategory(cid, SearchTypeConvert.SearchOrderOri2Thr(order),prodNum);
        } catch (TException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Long> GetProductBySimilarity(Integer cid, SearchOrder order, String words, Integer prodNum) {
        ThriftProdClientImpl client = prodClientPool.borrow();
        try {
            return client.client.GetProductBySimilarity(cid,SearchTypeConvert.SearchOrderOri2Thr(order),words,prodNum);
        } catch (TException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void close() throws IOException {
        accClientPool.close();
        prodClientPool.close();
    }
}
