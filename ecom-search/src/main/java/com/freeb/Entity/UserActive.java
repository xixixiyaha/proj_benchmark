package com.freeb.Entity;

public class UserActive {

    //Notice: This is DAO record.
    private Long userActiveId;
    // 用户id AKA accountId
    private Long userId;
    // 点击物品的id
    private Long prodId;

    // 类目的id
    private Long categoryId;

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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getUserActiveId() {
        return userActiveId;
    }

    public void setUserActiveId(Long userActiveId) {
        this.userActiveId = userActiveId;
    }

//    // 该用户对该二级类目的点击量
//    private Long hits;


}
