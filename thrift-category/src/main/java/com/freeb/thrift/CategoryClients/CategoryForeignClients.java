package com.freeb.thrift.CategoryClients;

import com.freeb.Clients.CategoryClients;
import com.freeb.Entity.CommentInfo;
import com.freeb.Entity.DiscountInfo;
import com.freeb.Entity.MerchantInfo;
import com.freeb.Entity.ProductInfo;
import com.freeb.Enum.SearchOrder;
import com.freeb.Enum.SearchType;
import com.freeb.Utils.LockObjectPool;
import com.freeb.thrift.CategoryTypeConvert;
import org.apache.thrift.TException;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

public class CategoryForeignClients extends CategoryClients implements Closeable {
    private static String discount_host = "bm-discount-server";
    private static String prod_host = "bm-product-server";
    private static String search_host = "bm-search-server";
    private static int search_port = 8080;

    static {
        System.out.println("in CategoryForeignClients");
    }

    private final LockObjectPool<ThriftDiscountClientImpl> discClientPool = new LockObjectPool<>(32,()->new ThriftDiscountClientImpl(discount_host, search_port));

    private final LockObjectPool<ThriftProdClientImpl> prodClientPool = new LockObjectPool<>(32,()->new ThriftProdClientImpl(prod_host, search_port));
    private final LockObjectPool<ThriftSearchClientImpl> searchClientPool = new LockObjectPool<>(32,()->new ThriftSearchClientImpl(search_host, search_port));



    @Override
    public List<Long> GetRecommendByProdName(Long userId, String words, SearchType type, SearchOrder order) {
        ThriftSearchClientImpl client = searchClientPool.borrow();
        try {
            return  client.client.GetRecommendByProdName(userId,words,CategoryTypeConvert.ConvertSearchTypeOri2Thr(type),CategoryTypeConvert.ConvertSearchOrderOri2Thr(order));
        } catch (TException e) {
            e.printStackTrace();
        }        return null;
    }

    @Override
    public ProductInfo GetProductInfo(Long id) {
        ThriftProdClientImpl client = prodClientPool.borrow();
        try {
            return CategoryTypeConvert.ConvertProductInfoThr2Ori(client.client.GetProdInfo(id));
        } catch (TException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<CommentInfo> GetComments(Long prodId, Integer comtNum) {
        ThriftProdClientImpl client = prodClientPool.borrow();
        try {
            return  CategoryTypeConvert.ConvertCommentInfoLstThr2Ori(client.client.GetComments(prodId,comtNum));
        } catch (TException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public DiscountInfo GetDiscounts(Long prodId, Integer type) {
        ThriftDiscountClientImpl client = discClientPool.borrow();
        try {
            return CategoryTypeConvert.ConvertDiscountInfoThr2Ori(client.client.GetDiscounts(prodId,type));
        } catch (TException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public MerchantInfo GetMerchantInfoByProd(Long pid) {
        ThriftProdClientImpl client = prodClientPool.borrow();
        try {
            return CategoryTypeConvert.ConvertMerchantInfoThr2Ori(client.client.GetMerchantInfoByProd(pid));
        } catch (TException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void close() throws IOException {
        discClientPool.close();
        prodClientPool.close();
        searchClientPool.close();
    }
}
