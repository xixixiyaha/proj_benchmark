package com.freeb;

public class DiscountTypeConvert {

    public static DiscountInfo DiscountInfoOri2Thr(com.freeb.Entity.DiscountInfo info){
        DiscountInfo nr = new DiscountInfo();
        nr.setDiscountId(info.getDiscountId());
        nr.setProdId(info.getProdId());
        nr.setDiscountId(info.getDiscountType());
        nr.setDiscountVal(info.getDiscountVal());
        return nr;
    }
}
