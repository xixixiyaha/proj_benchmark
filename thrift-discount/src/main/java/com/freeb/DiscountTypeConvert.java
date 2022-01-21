package com.freeb.thrift;

public class DiscountTypeConvert {

    public static com.freeb.thrift.DiscountInfo DiscountInfoOri2Thr(com.freeb.Entity.DiscountInfo info){
        com.freeb.thrift.DiscountInfo nr = new com.freeb.thrift.DiscountInfo();
        nr.setDiscountId(info.getDiscountId());
        nr.setProdId(info.getProdId());
        nr.setDiscountId(info.getDiscountType());
        nr.setDiscountVal(info.getDiscountVal());
        return nr;
    }
}
