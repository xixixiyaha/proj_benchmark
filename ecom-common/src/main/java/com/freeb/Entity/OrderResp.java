package com.freeb.Entity;

import java.util.List;

public class OrderResp {
    private List<OrderInfo> orderInfos;
    private Boolean hasMore;
    private BaseResp baseResp;

    public List<OrderInfo> getOrderInfos(){
        return orderInfos;
    }
    public void setOrderInfos(List<OrderInfo> orderInfos){
        this.orderInfos = orderInfos;
    }
    public BaseResp getBaseResp(){
        return baseResp;
    }
    public void setBaseResp(BaseResp baseResp){
        this.baseResp=baseResp;
    }
    public Boolean getHasMore(){
        return hasMore;
    }
    public void setHasMore(Boolean hasMore){
        this.hasMore = hasMore;
    }

}
