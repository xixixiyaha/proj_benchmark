package com.freeb.CartClients;

import com.freeb.Clients.CartClients;
import com.freeb.Entity.OrderReq;
import com.freeb.Entity.OrderResp;
import com.freeb.Utils.LockObjectPool;
import com.freeb.CartTypeConvert;
import org.apache.thrift.TException;

import java.io.Closeable;
import java.io.IOException;

public class CartForeignClients extends CartClients implements Closeable {

    private static String account_host = "bm-account-server";
    private static String order_host = "bm-prod-server";
    private static int search_port = 8080;

    static {
        System.out.println("in CartForeignClients");
    }

    private final LockObjectPool<ThriftAccountClientImpl> accClientPool = new LockObjectPool<>(32,()->new ThriftAccountClientImpl(account_host, search_port));
    private final LockObjectPool<ThriftOrderClientImpl> orderClientPool = new LockObjectPool<>(32,()->new ThriftOrderClientImpl(order_host, search_port));


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
    public OrderResp CreateOrderByCartInfo(OrderReq orderReq) {
        ThriftOrderClientImpl client = orderClientPool.borrow();
        try {
            return CartTypeConvert.OrderRespThr2Ori(client.client.CreateOrderByCartInfo(CartTypeConvert.OrderReqOri2Thr(orderReq)));
        } catch (TException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void close() throws IOException {
        accClientPool.close();
        orderClientPool.close();
    }
}
