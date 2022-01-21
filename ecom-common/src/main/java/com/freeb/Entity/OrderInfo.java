package com.freeb.Entity;

public class OrderInfo {
    /**
     * orderId : 1234
     * accountId : 123
     * paymentStatus : 1
     * merchantId : 1234
     * merchantName : name
     * objId : 1234
     * objName : name
     * paymentId : 12345
     */

    private long orderId;
    private long userId;
    private int paymentStatus;
    private long merchantId;
    private String merchantName;
    private long prodId;
    private String prodName;
    private long paymentId;
    private long cartId;

    public OrderInfo() {
    }

    public OrderInfo(long orderId, long userId, int paymentStatus, long merchantId, String merchantName, long prodId, String prodName, long paymentId, long cartId) {
        this.orderId = orderId;
        this.userId = userId;
        this.paymentStatus = paymentStatus;
        this.merchantId = merchantId;
        this.merchantName = merchantName;
        this.prodId = prodId;
        this.prodName = prodName;
        this.paymentId = paymentId;
        this.cartId = cartId;
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

    public int getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(int paymentStatus) {
        this.paymentStatus = paymentStatus;
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

    public long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(long paymentId) {
        this.paymentId = paymentId;
    }


    public long getCartId() {
        return cartId;
    }

    public void setCartInfo(long cartId) {
        this.cartId = cartId;
    }

    @Override
    public String toString() {
        return "OrderInfo{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", paymentStatus=" + paymentStatus +
                ", merchantId=" + merchantId +
                ", merchantName='" + merchantName + '\'' +
                ", prodId=" + prodId +
                ", prodName='" + prodName + '\'' +
                ", paymentId=" + paymentId +
                ", cartId=" + cartId +
                '}';
    }
}
