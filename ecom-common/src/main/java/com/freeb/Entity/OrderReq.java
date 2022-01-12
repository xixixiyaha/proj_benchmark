package com.freeb.Entity;

public class OrderReq {
    /**
     * accountId : 1234
     * status : 1
     * merchantId : 123
     * merchantName : Merchant Name
     * objId : 123
     * objName : obj Name
     * requireInfo : 3
     * orderId: 123
     */

    private long userId;
    private int status;
    private long merchantId;
    private String merchantName;
    private long prodId;
    private String prodName;
    private int requireInfo;
    private long orderId;
    private long paymentId;

    public long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(long paymentId) {
        this.paymentId = paymentId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(long merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public long getProdId() {
        return prodId;
    }

    public void setProdId(long prodId) {
        this.prodId = prodId;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public int getRequireInfo() {
        return requireInfo;
    }

    public void setRequireInfo(int requireInfo) {
        this.requireInfo = requireInfo;
    }
}
