package com.freeb.Entity;

import java.util.Date;

public class CartInfo {
    Long cartId;
    Long accountId;
    Long objId;
    Long merchantId;
    Integer incartQuantity;
    Integer incartSelect;

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getObjId() {
        return objId;
    }

    public void setObjId(Long objId) {
        this.objId = objId;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getIncartQuantity() {
        return incartQuantity;
    }

    public void setIncartQuantity(Integer incartQuantity) {
        this.incartQuantity = incartQuantity;
    }

    public Integer getIncartSelect() {
        return incartSelect;
    }

    public void setIncartSelect(Integer incartSelect) {
        this.incartSelect = incartSelect;
    }


    @Override
    public String toString() {
        return "CartInfo{" +
                "cartId=" + cartId +
                ", accountId=" + accountId +
                ", objId=" + objId +
                ", merchantId=" + merchantId +
                ", incartQuantity=" + incartQuantity +
                ", incartSelect=" + incartSelect +
                '}';
    }
}
