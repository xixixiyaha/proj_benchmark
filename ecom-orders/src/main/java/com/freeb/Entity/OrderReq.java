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

    private int accountId;
    private int status;
    private int merchantId;
    private String merchantName;
    private int objId;
    private String objName;
    private int requireInfo;
    private int orderId;
    private int paymentId;

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public int getObjId() {
        return objId;
    }

    public void setObjId(int objId) {
        this.objId = objId;
    }

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    public int getRequireInfo() {
        return requireInfo;
    }

    public void setRequireInfo(int requireInfo) {
        this.requireInfo = requireInfo;
    }
}
