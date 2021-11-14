package com.freeb.Orders;


import com.freeb.Dao.OrderInfoCache;
import com.freeb.Dao.OrderInfoStorage;
import com.freeb.Entity.OrderInfo;
import com.freeb.Entity.OrderSearchKey;
import com.freeb.Enum.SearchType;
import com.freeb.Utils.MarshalUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Orders {
    private static final Logger logger = LoggerFactory.getLogger(Orders.class);

    //TODO 我觉得这个声明方式不对
    OrderInfoCache cache = new OrderInfoCache();
    OrderInfoStorage storage = new OrderInfoStorage();

    public List<OrderInfo> getOrderListByAccountId(Integer accountId,Integer paymentStatus){
        OrderSearchKey key = new OrderSearchKey();
        key.setAccountId(accountId);
        key.setStatus(paymentStatus);
        String skey = MarshalUtil.convertSearchKey2String(key);

        return getOrderList(skey,accountId,SearchType.ACCOUNT_ID);
    }
    public List<OrderInfo> getOrderByOrderId(int accountId, int orderId, int status) {
        OrderSearchKey key = new OrderSearchKey();
        key.setAccountId(accountId);
        key.setObjId(orderId);
        String skey = MarshalUtil.convertSearchKey2String(key);

        List<OrderInfo> info =  getOrderList(skey,orderId,SearchType.ORDER_ID);
        if(info==null||info.size()>1){
            return null;
        }
        return info;
    }
    public List<OrderInfo> getOrderByPaymentId(int accountId, int paymentId, int status) {
        OrderSearchKey key = new OrderSearchKey();
        key.setAccountId(accountId);
        key.setObjId(paymentId);
        String skey = MarshalUtil.convertSearchKey2String(key);

        List<OrderInfo> info =  getOrderList(skey,paymentId,SearchType.PAYMENT_ID);
        if(info==null||info.size()>1){
            return null;
        }
        return info;
    }
    public List<OrderInfo> getOrderListByObjId(Integer accountId, Integer objId, Integer paymentStatus){
        OrderSearchKey key = new OrderSearchKey();
        key.setAccountId(accountId);
        key.setObjId(objId);
        key.setStatus(paymentStatus);
        String skey = MarshalUtil.convertSearchKey2String(key);

        return getOrderList(skey,objId,SearchType.OBJ_ID);
    }

    public List<OrderInfo> getOrderListByObjName(Integer accountId, String objName, Integer paymentStatus){
        OrderSearchKey key = new OrderSearchKey();
        key.setAccountId(accountId);
        key.setObjName(objName);
        key.setStatus(paymentStatus);
        String skey = MarshalUtil.convertSearchKey2String(key);
        return getOrderList(skey,objName,SearchType.OBJ_NAME);
    }

    public List<OrderInfo> getOrderListByMerchantId(Integer accountId, Integer merchantId, Integer paymentStatus){
        OrderSearchKey key = new OrderSearchKey();
        key.setAccountId(accountId);
        key.setMerchantId(merchantId);
        key.setStatus(paymentStatus);
        String skey = MarshalUtil.convertSearchKey2String(key);

        return getOrderList(skey,merchantId,SearchType.MERCHANT_ID);
    }

    public List<OrderInfo> getOrderListByMerchantName(Integer accountId, String merchantName, Integer paymentStatus){
        OrderSearchKey key = new OrderSearchKey();
        key.setAccountId(accountId);
        key.setMerchantName(merchantName);
        key.setStatus(paymentStatus);
        String skey = MarshalUtil.convertSearchKey2String(key);
        return getOrderList(skey,merchantName,SearchType.MERCHANT_NAME);
    }

    public List<OrderInfo> getOrderList(String key, Integer searchId, SearchType type){
        String str = OrderInfoCache.getOrderListString(key);
        if(str!=null){
            return MarshalUtil.convertString2OrderList(key);
        }
        switch (type){
            case ORDER_ID:
                return MarshalUtil.convertRs2OrderList(storage.getOrderInfoByOrderId(searchId));
            case OBJ_ID:
                return MarshalUtil.convertRs2OrderList(storage.getOrderInfoByObjId(searchId));
            case ACCOUNT_ID:
                return MarshalUtil.convertRs2OrderList(storage.getOrderInfoByAccountId(searchId));
            case MERCHANT_ID:
                return MarshalUtil.convertRs2OrderList(storage.getOrderInfoByMerchantId(searchId));
            case PAYMENT_ID:
                return MarshalUtil.convertRs2OrderList(storage.getOrderInfoByPaymentId(searchId));
            default:
                logger.warn("unsupported type");
                return null;
        }
    }

    public List<OrderInfo> getOrderList(String key,String searchName,SearchType type){
        String str = OrderInfoCache.getOrderListString(key);
        if(str!=null){
            return MarshalUtil.convertString2OrderList(key);
        }
        switch (type){
            case OBJ_NAME:
                //TODO
                return null;
            case MERCHANT_NAME:
                //TODO
                return null;
            default:
                logger.warn("unsupported type");
                return null;
        }
    }


}
