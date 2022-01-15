package com.freeb.Entity;

import java.util.List;

public class ProductPage {
    ProductInfo info;
    Long merchantId;
    Long merchantName;
    DiscountInfo discountVal;
    List<CommentInfo> prodComments;



    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Long getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(Long merchantName) {
        this.merchantName = merchantName;
    }

    public List<CommentInfo> getProdComments() {
        return prodComments;
    }

    public void setProdComments(List<CommentInfo> prodComments) {
        this.prodComments = prodComments;
    }

    public ProductInfo getInfo() {
        return info;
    }

    public void setInfo(ProductInfo info) {
        this.info = info;
    }

    public DiscountInfo getDiscountVal() {
        return discountVal;
    }

    public void setDiscountVal(DiscountInfo discountVal) {
        this.discountVal = discountVal;
    }
}
