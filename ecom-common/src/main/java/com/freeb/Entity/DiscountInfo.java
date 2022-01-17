package com.freeb.Entity;

public class DiscountInfo {
    Long discountId;
    Integer discountType;
    Long prodId;
    Double discountVal;

    public DiscountInfo(Long discountId, Integer discountType, Long prodId, Double discountVal) {
        this.discountId = discountId;
        this.discountType = discountType;
        this.prodId = prodId;
        this.discountVal = discountVal;
    }

    public Long getDiscountId() {
        return discountId;
    }

    public void setDiscountId(Long discountId) {
        this.discountId = discountId;
    }

    public Integer getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    public Long getProdId() {
        return prodId;
    }

    public void setProdId(Long prodId) {
        this.prodId = prodId;
    }

    public Double getDiscountVal() {
        return discountVal;
    }

    public void setDiscountVal(Double discountVal) {
        this.discountVal = discountVal;
    }
}
