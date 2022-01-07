package com.freeb.Entity;

public class UserSimilarity {

    // 用户id
    private Long userId;

    // 进行比较的用户id
    private Long userRefId;

    // userId与userRefId之间的相似度
    private Double similarity;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserRefId() {
        return userRefId;
    }

    public void setUserRefId(Long userRefId) {
        this.userRefId = userRefId;
    }

    public Double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(Double similarity) {
        this.similarity = similarity;
    }
}
