package com.freeb.Orders;


import com.freeb.Entity.OrderInfo;
import com.freeb.Entity.OrderSearchKey;
import com.freeb.Utils.MarshalUtil;

import java.util.List;

public class Orders {
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

}
