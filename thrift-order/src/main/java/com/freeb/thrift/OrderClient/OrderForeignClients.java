package com.freeb.thrift.OrderClient;

import com.freeb.Clients.OrderClients;
import com.freeb.Entity.*;
import com.freeb.Enum.IdType;
import com.freeb.Enum.PaymentStatus;
import com.freeb.OrderTypeConvert;
import com.freeb.Utils.LockObjectPool;
import org.apache.thrift.TException;

import java.io.Closeable;
import java.io.IOException;

public class OrderForeignClients extends OrderClients implements Closeable {

    private static String account_host = "bm-account-server";
    private static String cart_host = "bm-cart-server";
    private static String discount_host = "bm-discount-server";
    private static String payment_host = "bm-payment-server";
    private static String prod_host = "bm-product-server";
    private static int search_port = 8080;
    static {
        System.out.println("in AccountForeignClients");
    }
    private final LockObjectPool<ThriftAccountClientImpl> accClientPool = new LockObjectPool<>(32,()->new ThriftAccountClientImpl(account_host, search_port));
    private final LockObjectPool<ThriftCartClientImpl> cartClientPool = new LockObjectPool<>(32,()->new ThriftCartClientImpl(cart_host, search_port));

    private final LockObjectPool<ThriftDiscountClientImpl> discClientPool = new LockObjectPool<>(32,()->new ThriftDiscountClientImpl(discount_host, search_port));
    private final LockObjectPool<ThriftPaymentClientImpl> paymentClientPool = new LockObjectPool<>(32,()->new ThriftPaymentClientImpl(payment_host, search_port));
    private final LockObjectPool<ThriftProdClientImpl> prodClientPool = new LockObjectPool<>(32,()->new ThriftProdClientImpl(prod_host, search_port));


    @Override
    public Boolean AccountExists(long accountId) {
        ThriftAccountClientImpl client = accClientPool.borrow();
        try {
            return  client.client.AccountExists(accountId);
        } catch (TException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Boolean VerifyAccessByAccount(long accountId, long targetId, IdType idType) {
        ThriftAccountClientImpl client = accClientPool.borrow();
        try {
            return  client.client.VerifyAccessByAccount(accountId,targetId,OrderTypeConvert.ConvertIdTypeOri2Thr(idType));
        } catch (TException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public AccountInfo GetAccountInfo(Long id) {
        ThriftAccountClientImpl client = accClientPool.borrow();
        try {
            return  OrderTypeConvert.ConvertAccountInfoThr2Ori(client.client.GetAccountInfo(id));
        } catch (TException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public PaymentStatus CheckPaymentStatusById(Long uid, Long paymentId) {
        ThriftPaymentClientImpl client = paymentClientPool.borrow();
        try {
            return  OrderTypeConvert.ConvertPaymentStatusThr2Ori(client.client.CheckPaymentStatusById(uid,paymentId));
        } catch (TException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Long CreatePayment(PaymentInfo info) {
        ThriftPaymentClientImpl client = paymentClientPool.borrow();
        try {
            return  client.client.CreatePayment( OrderTypeConvert.ConvertPaymentInfoOri2Thr(info));
        } catch (TException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Boolean CancelPayment(Long uid, Long paymentId) {
        ThriftPaymentClientImpl client = paymentClientPool.borrow();
        try {
            return  client.client.CancelPayment(uid,paymentId);
        } catch (TException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public PaymentInfo GetPaymentInfoById(Long uid, Long paymentId) {
        ThriftPaymentClientImpl client = paymentClientPool.borrow();
        try {
            return  OrderTypeConvert.ConvertPaymentInfoThr2Ori(client.client.GetPaymentInfoById(uid,paymentId));
        } catch (TException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ProductInfo IncProductSales(Long pid, Integer purchaseNum) {
        ThriftProdClientImpl client = prodClientPool.borrow();
        try {
            return  OrderTypeConvert.ConvertProductInfoThr2Ori(client.client.IncProductSales(pid,purchaseNum));
        } catch (TException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public MerchantInfo GetMerchantInfoById(Long mid) {
        ThriftProdClientImpl client = prodClientPool.borrow();
        try {
            return  OrderTypeConvert.ConvertMerchantInfoThr2Ori(client.client.GetMerchantInfoById(mid));
        } catch (TException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public DiscountInfo GetProdDiscounts(Long prodId, Integer type){
        ThriftDiscountClientImpl client = discClientPool.borrow();
        try {
            return OrderTypeConvert.ConvertDiscountInfoThr2Ori(client.client.GetProdDiscounts(prodId,type));
        } catch (TException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CartInfo GetCartInfoById(Long cartId, Long userId) {
        ThriftCartClientImpl cartClient = cartClientPool.borrow();
        try {
            return OrderTypeConvert.ConvertCartInfoThr2Ori(cartClient.client.GetCartInfoById(cartId,userId));
        } catch (TException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void close() throws IOException {
        accClientPool.close();
        cartClientPool.close();
        discClientPool.close();
        paymentClientPool.close();
        prodClientPool.close();
    }
}
