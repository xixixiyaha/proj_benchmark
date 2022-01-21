package com.freeb.Entity;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class AccountInfo {

    private Long userId;
    private String userName;
    private String userPasswd;
    private String userCard;
    private String userDescription;
    // 懒更新 alg: 最近的浏览中 top10 category的占比
    private List<Integer> userTag;
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

    public String getUserPasswd() {
        return userPasswd;
    }

    public void setUserPasswd(String userPasswd) {
        this.userPasswd = userPasswd;
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

    public List<Integer> getUserTag() {
        return userTag;
    }

    public void setUserTag(List<Integer> userTag) {
        this.userTag = userTag;
    }

    @Override
    public String toString() {
        return "AccountInfo{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", userPasswd='" + userPasswd + '\'' +
                ", userDescription='" + userDescription + '\'' +
                ", userTag=" + userTag +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

    public String getUserCard() {
        return userCard;
    }

    public void setUserCard(String userCard) {
        this.userCard = userCard;
    }
}
