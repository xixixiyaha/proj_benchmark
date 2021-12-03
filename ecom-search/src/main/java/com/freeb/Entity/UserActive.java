package com.freeb.Entity;

public class UserActive {

    //Notice: This is DAO record.

    // 用户id AKA accountId
    private Long userId;

    // 点击物品的id
    private Long objId;

    // 二级类目的id
    private Long categoryId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getObjId() {
        return objId;
    }

    public void setObjId(Long objId) {
        this.objId = objId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

//    // 该用户对该二级类目的点击量
//    private Long hits;


}
