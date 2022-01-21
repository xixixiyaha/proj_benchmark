package com.freeb.thrift;



import com.freeb.Entity.CartInfo;
import com.freeb.Entity.OrderInfo;
import com.freeb.Entity.OrderReq;
import com.freeb.Entity.OrderResp;

import java.util.ArrayList;
import java.util.List;

public class CartTypeConvert {

    public static com.freeb.Entity.CartInfo CartInfoThr2Ori(com.freeb.thrift.CartInfo info) {
        return new CartInfo(info.getCartId(),info.getUserId(),info.getObjId(),info.getMerchantId(),info.getIncartQuantity(),info.getIncartSelect());

    }

    public static com.freeb.thrift.CartInfo CartInfoOri2Thr(com.freeb.Entity.CartInfo info) {
        com.freeb.thrift.CartInfo re = new com.freeb.thrift.CartInfo();
        re.setCartId(info.getCartId());
        re.setUserId(info.getUserId());
        re.setObjId(info.getProdId());
        re.setMerchantId(info.getMerchantId());
        re.setIncartQuantity(info.getIncartQuantity());
        re.setIncartSelect(info.getIncartSelect());
        return re;
    }


    public static com.freeb.thrift.OrderReq OrderReqOri2Thr(OrderReq req){
        com.freeb.thrift.OrderReq nre = new com.freeb.thrift.OrderReq();
        nre.setUserId(req.getUserId());
        nre.setStatus(req.getStatus());
        nre.setMerchantId(req.getMerchantId());
        nre.setMerchantName(req.getMerchantName());
        nre.setProdId(req.getProdId());
        nre.setProdName(req.getProdName());
        nre.setOrderId(req.getOrderId());
        nre.setPaymentId(req.getPaymentId());
        nre.setCartId(req.getCartId());
        return nre;
    }
    public static OrderInfo OrderInfoThr2Ori(com.freeb.thrift.OrderInfo info){
        return new OrderInfo(info.getOrderId(),info.getUserId(),info.getPaymentStatus(),info.getMerchantId(),info.getMerchantName(),info.getProdId(),info.getProdName(),info.getPaymentId(),info.getCartId());
    }
    public static OrderResp OrderRespThr2Ori(com.freeb.thrift.OrderResp resp){
        OrderResp nr = new OrderResp();
        if(resp.getBaseResp().Status != RespCode.SUCCESS){
            return null;
        }
        nr.setHasMore(resp.hasMore);
        List<OrderInfo> lst = new ArrayList<>();
        for(com.freeb.thrift.OrderInfo info:resp.getOrderInfos()){
            lst.add(OrderInfoThr2Ori(info));
        }
        nr.setOrderInfos(lst);
        return nr;
    }
}
