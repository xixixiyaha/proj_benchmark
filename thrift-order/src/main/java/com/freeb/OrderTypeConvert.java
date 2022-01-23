package com.freeb;

import com.freeb.Entity.OrderInfo;
import com.freeb.Entity.OrderReq;
import com.freeb.Entity.OrderResp;
import com.freeb.thrift.*;

import java.util.ArrayList;
import java.util.List;

public class OrderTypeConvert {

    public static com.freeb.Entity.OrderReq OrderReqThr2Ori(com.freeb.thrift.OrderReq req){
        OrderReq nre = new OrderReq();
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

    public static com.freeb.thrift.OrderReq OrderReqOri2Thr(com.freeb.Entity.OrderReq req){
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

    public static com.freeb.thrift.OrderInfo OrderInfoOri2Thr(com.freeb.Entity.OrderInfo info){
        com.freeb.thrift.OrderInfo oInfo = new com.freeb.thrift.OrderInfo();
        oInfo.setOrderId(info.getOrderId());
        oInfo.setUserId(info.getUserId());
        oInfo.setPaymentStatus(info.getPaymentStatus());
        oInfo.setMerchantId(info.getMerchantId());
        oInfo.setMerchantName(info.getMerchantName());
        oInfo.setProdId(info.getProdId());
        oInfo.setProdName(info.getProdName());
        oInfo.setPaymentId(info.getPaymentId());
        oInfo.setCartId(info.getCartId());
        return oInfo;
    }

    public static com.freeb.thrift.BaseResp BaseRespOri2Thr(com.freeb.Entity.BaseResp resp){
        com.freeb.thrift.BaseResp bResp = new com.freeb.thrift.BaseResp();
        bResp.Msg=resp.getMsg();
        bResp.Status=com.freeb.thrift.RespCode.findByValue(resp.getStatus().ordinal());
        return bResp;
    }

    public static com.freeb.thrift.OrderResp OrderRespOri2Thr(com.freeb.Entity.OrderResp resp){
        com.freeb.thrift.OrderResp nr = new com.freeb.thrift.OrderResp();
        nr.setBaseResp(BaseRespOri2Thr(resp.getBaseResp()));
        nr.setHasMore(resp.getHasMore());
        List<com.freeb.thrift.OrderInfo> lst = new ArrayList<>();
        for(OrderInfo info:resp.getOrderInfos()){
            lst.add(OrderInfoOri2Thr(info));
        }
        nr.setOrderInfos(lst);
        return nr;
    }

    public static com.freeb.Entity.AccountInfo ConvertAccountInfoThr2Ori(com.freeb.thrift.AccountInfo info){
        com.freeb.Entity.AccountInfo re = new com.freeb.Entity.AccountInfo();
        re.setUserId(info.getUserId());
        re.setUserName(info.getUserName());
        re.setUserPasswd(info.getUserPasswd());
        re.setUserDescription(info.getUserDescription());
        re.setUserTag(info.getUserTag());
        return re;
    }

    public static com.freeb.Entity.CartInfo ConvertCartInfoThr2Ori(com.freeb.thrift.CartInfo info){
        return new com.freeb.Entity.CartInfo(info.getCartId(),info.getUserId(),info.getObjId(),info.getMerchantId(),info.getIncartQuantity(),info.getIncartSelect());
    }

    public static com.freeb.Entity.DiscountInfo ConvertDiscountInfoThr2Ori(com.freeb.thrift.DiscountInfo info){
        com.freeb.Entity.DiscountInfo dInfo = new com.freeb.Entity.DiscountInfo();
        dInfo.setDiscountId(info.getDiscountId());
        dInfo.setDiscountType(info.getDiscountType());
        dInfo.setDiscountVal(info.getDiscountVal());
        dInfo.setProdId(info.getProdId());
        return dInfo;
    }

    public static com.freeb.Entity.MerchantInfo ConvertMerchantInfoThr2Ori(com.freeb.thrift.MerchantInfo info){
        com.freeb.Entity.MerchantInfo mInfo = new com.freeb.Entity.MerchantInfo();
        mInfo.setMerchantId(info.getMerchantId());
        mInfo.setMerchantName(info.getMerchantName());
        return mInfo;
    }
    public static com.freeb.Entity.ProductInfo ConvertProductInfoThr2Ori(com.freeb.thrift.ProductInfo info){
        com.freeb.Entity.ProductInfo pinfo = new com.freeb.Entity.ProductInfo();
        pinfo.setProdId(info.getProdId());
        pinfo.setProdName(info.getProdName());
        pinfo.setCategoryId(info.getCategoryId());
        pinfo.setProdSales(info.getProdSales());
        pinfo.setProdRemain(info.getProdRemain());
        pinfo.setProdImages(info.getProdImages());
        pinfo.setDiscountsId(info.getDiscountsId());
        pinfo.setMerchantId(info.getMerchantId());
        return pinfo;
    }
    public static com.freeb.Entity.PaymentInfo ConvertPaymentInfoThr2Ori(com.freeb.thrift.PaymentInfo info){
        com.freeb.Entity.PaymentInfo pInfo = new com.freeb.Entity.PaymentInfo();

        pInfo.setPaymentId(info.getPaymentId());
        pInfo.setPaymentStatus(info.getPaymentStatus());
        pInfo.setPaymentVal(info.getPaymentVal());
        pInfo.setDiscountsVal(info.getDiscountsVal());
        pInfo.setUserId(info.getUserId());
        pInfo.setDiscountsVal(info.getDiscountsVal());
        pInfo.setPaymentCard(info.getPaymentCard());
        //Notice OmitTime
        return pInfo;
    }
    public static com.freeb.thrift.PaymentInfo ConvertPaymentInfoOri2Thr(com.freeb.Entity.PaymentInfo info){
        com.freeb.thrift.PaymentInfo pInfo = new com.freeb.thrift.PaymentInfo();

        pInfo.setPaymentId(info.getPaymentId());
        pInfo.setPaymentStatus(info.getPaymentStatus());
        pInfo.setPaymentVal(info.getPaymentVal());
        pInfo.setDiscountsVal(info.getDiscountsVal());
        pInfo.setUserId(info.getUserId());
        pInfo.setDiscountsVal(info.getDiscountsVal());
        pInfo.setPaymentCard(info.getPaymentCard());
        //Notice OmitTime
        return pInfo;
    }
    public static com.freeb.thrift.IdType ConvertIdTypeOri2Thr(com.freeb.Enum.IdType type){
        switch (type){
            case PAYMENT_ID:
                return IdType.PAYMENT_ID;
            case ORDER_ID:
                return IdType.ORDER_ID;
            case MERCHANT_ID:
                return IdType.MERCHANT_ID;
            case OBJ_ID:
                return IdType.OBJ_ID;
            case SHIPPING_ID:
                return IdType.SHIPPING_ID;
            case ACCOUNT_ID:
                return IdType.ACCOUNT_ID;
            default:
                return IdType.ORDER_ID;
        }
    }

    public static com.freeb.Enum.PaymentStatus ConvertPaymentStatusThr2Ori(com.freeb.thrift.PaymentStatus status){
        return com.freeb.Enum.PaymentStatus.values()[status.ordinal()];
    }


}
