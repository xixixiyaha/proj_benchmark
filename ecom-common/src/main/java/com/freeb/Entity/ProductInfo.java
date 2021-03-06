package com.freeb.Entity;

import java.util.Date;
import java.util.List;

public class ProductInfo {
    Long prodId;
    String prodName;
    Integer categoryId;
    Double prodPrice;
    Integer prodSales;
    Integer prodRemain;
    List<String> prodImages;
    Long discountsId;
    Long merchantId;
    Date createTime;
    Date updateTime;

    public Long getProdId() {
        return prodId;
    }

    public void setProdId(Long prodId) {
        this.prodId = prodId;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Double getProdPrice() {
        return prodPrice;
    }

    public void setProdPrice(Double prodPrice) {
        this.prodPrice = prodPrice;
    }

    public Integer getProdSales() {
        return prodSales;
    }

    public void setProdSales(Integer prodSales) {
        this.prodSales = prodSales;
    }

    public Long getDiscountsId() {
        return discountsId;
    }

    public void setDiscountsId(Long discountsId) {
        this.discountsId = discountsId;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<String> getProdImages() {
        return prodImages;
    }

    public void setProdImages(List<String> prodImages) {
        this.prodImages = prodImages;
    }

    public Integer getProdRemain() {
        return prodRemain;
    }

    public void setProdRemain(Integer prodRemain) {
        this.prodRemain = prodRemain;
    }
}
