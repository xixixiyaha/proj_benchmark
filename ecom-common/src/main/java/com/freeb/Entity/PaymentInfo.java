package com.freeb.Entity;

import java.util.Date;

public class PaymentInfo {
    private Long paymentId;
    private Integer paymentStatus;
    private Double paymentVal;
    private Double discountsVal;
    private String paymentCard;
    private Integer accountId;
    private Date createTime;
    private Date updateTime;

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public Integer getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(Integer paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Double getPaymentVal() {
        return paymentVal;
    }

    public void setPaymentVal(Double paymentVal) {
        this.paymentVal = paymentVal;
    }

    public Double getDiscountsVal() {
        return discountsVal;
    }

    public void setDiscountsVal(Double discountsVal) {
        this.discountsVal = discountsVal;
    }

    public String getPaymentCard() {
        return paymentCard;
    }

    public void setPaymentCard(String paymentCard) {
        this.paymentCard = paymentCard;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
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
}
