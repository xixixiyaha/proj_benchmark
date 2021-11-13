package com.freeb.Service;

import com.freeb.Clients.AccountsClient;
import com.freeb.Entity.OrderReq;
import com.freeb.Entity.OrderResp;
import com.freeb.Enum.IdType;
import com.freeb.Utils.PackResponse;

public class OrderServiceServerImpl implements OrdersService{
    // TODO 初始化
    private static AccountsClient accountsClient;

    @Override
    public OrderResp GetOrderListByAccountId(OrderReq orderReq){
        OrderResp orderResp = new OrderResp();

        if (!accountsClient.verifyAccount(orderReq.getAccountId())){
            orderResp.setBaseResp(PackResponse.packNoAuthority());
            orderResp.setHasMore(false);
            return orderResp;
        }

        //TODO
        return orderResp;
    }

    @Override
    public OrderResp GetOrderByPaymentId(OrderReq orderReq) {
        OrderResp orderResp = new OrderResp();
        if (!accountsClient.verifyAccessByAccount(orderReq.getAccountId(),orderReq.getPaymentId(), IdType.PAYMENT_ID)){
            orderResp.setBaseResp(PackResponse.packNoAuthority());
            orderResp.setHasMore(false);
            return orderResp;
        }
        //TODO
        return null;
    }

    @Override
    public OrderResp CreatePaymentByOrderId(OrderReq orderReq) {
        OrderResp orderResp = new OrderResp();
        if (!accountsClient.verifyAccessByAccount(orderReq.getAccountId(),orderReq.getOrderId(), IdType.ORDER_ID)){
            orderResp.setBaseResp(PackResponse.packNoAuthority());
            orderResp.setHasMore(false);
            return orderResp;
        }
        //TODO
        return null;
    }

    @Override
    public OrderResp GetOrderByOrderId(OrderReq orderReq) {
        OrderResp orderResp = new OrderResp();
        if (!accountsClient.verifyAccessByAccount(orderReq.getAccountId(),orderReq.getOrderId(), IdType.ORDER_ID)){
            orderResp.setBaseResp(PackResponse.packNoAuthority());
            orderResp.setHasMore(false);
            return orderResp;
        }
        //TODO
        return null;
    }


}
