package com.freeb.Dao;



import com.freeb.Entity.OrderInfo;
import com.freeb.Entity.OrderSearchKey;
import com.freeb.Utils.MarshalUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class OrderInfoCache {

    private static final Logger logger = LoggerFactory.getLogger(OrderInfoCache.class);

    static String ORDER_CACHE_URL ;
    static String ORDER_USER;
    static String ORDER_PSW;

    void OrderInfoCache(String url,String name, String psw){
        ORDER_CACHE_URL=url;
        ORDER_USER=name;
        ORDER_PSW=psw;

        try(Connection conn = DriverManager.getConnection(ORDER_CACHE_URL,ORDER_USER,ORDER_PSW)){
            logger.info("redis connected!");
        }catch (SQLException e){
            logger.error(String.format("redis connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
        }
    }

    List<OrderInfo> getOrderListByObjId(Integer accountId, Integer objId, Integer paymentStatus){
        OrderSearchKey key = new OrderSearchKey();
        key.setAccountId(accountId);
        key.setObjId(objId);
        key.setStatus(paymentStatus);
        String skey = MarshalUtil.convertSearchKey2String(key);

        return null;
    }

    List<OrderInfo> getOrderListByObjName(Integer accountId, String objName, Integer paymentStatus){
        OrderSearchKey key = new OrderSearchKey();
        key.setAccountId(accountId);
        key.setObjName(objName);
        key.setStatus(paymentStatus);
        String skey = MarshalUtil.convertSearchKey2String(key);
        return null;
    }

    List<OrderInfo> getOrderListByMerchantId(Integer accountId, Integer merchantId, Integer paymentStatus){
        OrderSearchKey key = new OrderSearchKey();
        key.setAccountId(accountId);
        key.setMerchantId(merchantId);
        key.setStatus(paymentStatus);
        String skey = MarshalUtil.convertSearchKey2String(key);

        return null;
    }

    List<OrderInfo> getOrderListByMerchantName(Integer accountId, String merchantName, Integer paymentStatus){
        OrderSearchKey key = new OrderSearchKey();
        key.setAccountId(accountId);
        key.setMerchantName(merchantName);
        key.setStatus(paymentStatus);
        String skey = MarshalUtil.convertSearchKey2String(key);
        return null;
    }

    public static void main(String[] args){
        logger.debug("123 debug");
        logger.info("123 info");logger.error("123 error");
    }

}

