package com.freeb.Entity;

public class MerchantInfo {
    Long merchantId;
    String merchantName;

    public MerchantInfo() {
    }

    public MerchantInfo(Long merchantId, String merchantName) {
        this.merchantId = merchantId;
        this.merchantName = merchantName;
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
