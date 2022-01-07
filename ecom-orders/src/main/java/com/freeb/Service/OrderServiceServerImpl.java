package com.freeb.Service;

import com.freeb.Clients.AccountsClient;
import com.freeb.Entity.OrderInfo;
import com.freeb.Entity.OrderReq;
import com.freeb.Entity.OrderResp;
import com.freeb.Enum.IdType;
import com.freeb.Orders.Orders;
import com.freeb.Utils.PackResponse;

import java.util.List;

public class OrderServiceServerImpl implements OrdersService{
    // TODO 初始化
    private static AccountsClient accountsClient;
    private Orders orders = new Orders();

    @Override
    public OrderResp GetOrderListByAccountId(OrderReq orderReq){
        OrderResp orderResp = new OrderResp();

        if (!accountsClient.verifyAccount(orderReq.getAccountId())){
            orderResp.setBaseResp(PackResponse.packNoAuthority());
            orderResp.setHasMore(false);
            return orderResp;
        }

        List<OrderInfo> infos = orders.getOrderListByAccountId(orderReq.getAccountId(),orderReq.getStatus());
        if(infos == null){
            orderResp.setBaseResp(PackResponse.packUnknownFailure());
            return orderResp;
        }

        orderResp.setBaseResp(PackResponse.packSuccess());
        orderResp.setOrderInfos(infos);
        //TODO has more
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
        List<OrderInfo> infos = orders.getOrderByPaymentId(orderReq.getAccountId(),orderReq.getPaymentId(),orderReq.getStatus());
        if(infos == null){
            orderResp.setBaseResp(PackResponse.packUnknownFailure());
            return orderResp;
        }
        orderResp.setBaseResp(PackResponse.packSuccess());
        orderResp.setOrderInfos(infos);
        return orderResp;
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
        List<OrderInfo> infos = orders.getOrderByOrderId(orderReq.getAccountId(),orderReq.getOrderId(),orderReq.getStatus());
        if(infos == null){
            orderResp.setBaseResp(PackResponse.packUnknownFailure());
            return orderResp;
        }

        orderResp.setBaseResp(PackResponse.packSuccess());
        orderResp.setOrderInfos(infos);
        return orderResp;
    }


}
