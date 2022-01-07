package com.freeb.Entity;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountsInfo {

    private Long userId;
    private String userName;
    private String userPwd;
    private String userDescription;
    // 懒更新 alg: 最近的浏览中 top10 category的占比
    private Map<Integer,Double> userTag;
    private Date createTime;
    private Date updateTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
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

    public Map<Integer,Double> getUserTag() {
        return userTag;
    }

    public void setUserTag(Map<Integer,Double> userTag) {
        this.userTag = userTag;
    }
}
