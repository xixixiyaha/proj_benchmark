package com.freeb.Entity;

public class CategoryPage {
    Long prodId;
    String prodName;
    Integer prodSales;
    String prodImage;
    Long merchantId;
    String merchantName;

    public CategoryPage(Long prodId, String prodName, Integer prodSales, String prodImage, Long merchantId, String merchantName) {
        this.prodId = prodId;
        this.prodName = prodName;
        this.prodSales = prodSales;
        this.prodImage = prodImage;
        this.merchantId = merchantId;
        this.merchantName = merchantName;
    }

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

    public Integer getProdSales() {
        return prodSales;
    }

    public void setProdSales(Integer prodSales) {
        this.prodSales = prodSales;
    }

    public String getProdImage() {
        return prodImage;
    }

    public void setProdImage(String prodImage) {
        this.prodImage = prodImage;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }
}
