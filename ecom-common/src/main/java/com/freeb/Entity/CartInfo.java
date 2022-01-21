package com.freeb.Entity;

public class CartInfo {
    Long cartId;
    Long userId;
    Long prodId;
    Long merchantId;
    Integer incartQuantity;
    Integer incartSelect;

    public CartInfo() {
    }

    public CartInfo(Long cartId) {
        this.cartId = cartId;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProdId() {
        return prodId;
    }

    public void setProdId(Long prodId) {
        this.prodId = prodId;
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
                ", accountId=" + userId +
                ", objId=" + prodId +
                ", merchantId=" + merchantId +
                ", incartQuantity=" + incartQuantity +
                ", incartSelect=" + incartSelect +
                '}';
    }
}
