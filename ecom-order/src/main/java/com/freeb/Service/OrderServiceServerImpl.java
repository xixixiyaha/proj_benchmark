package com.freeb.Service;

import com.freeb.Clients.OrderClients;
import com.freeb.Dao.OrderInfoStorage;
import com.freeb.Entity.OrderInfo;
import com.freeb.Entity.OrderReq;
import com.freeb.Entity.OrderResp;
import com.freeb.Enum.IdType;
import com.freeb.Orders.Orders;
import com.freeb.Utils.PackResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class OrderServiceServerImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceServerImpl.class);
    // TODO 初始化
    private OrderClients client;

    private Orders orders = new Orders();
    private OrderInfoStorage storage;

    public OrderServiceServerImpl(){
        storage = new OrderInfoStorage();
    }

    @Override
    public OrderResp GetOrderListByUserId(OrderReq orderReq){
        OrderResp orderResp = new OrderResp();

        if (!client.verifyAccount(orderReq.getUserId())){
            orderResp.setBaseResp(PackResponse.packNoAuthority());
            orderResp.setHasMore(false);
            return orderResp;
        }

        List<OrderInfo> infos = orders.getOrderListByAccountId(orderReq.getUserId(),orderReq.getStatus());
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
        if (!client.verifyAccessByAccount(orderReq.getUserId(),orderReq.getPaymentId(), IdType.PAYMENT_ID)){
            orderResp.setBaseResp(PackResponse.packNoAuthority());
            orderResp.setHasMore(false);
            return orderResp;
        }
        List<OrderInfo> infos = orders.getOrderByPaymentId(orderReq.getUserId(),orderReq.getPaymentId(),orderReq.getStatus());
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
        orderResp.setHasMore(false);
        if (!client.verifyAccessByAccount(orderReq.getUserId(),orderReq.getOrderId(), IdType.ORDER_ID)){
            orderResp.setBaseResp(PackResponse.packNoAuthority());
            return orderResp;
        }
        Double prodVal= client.GetProdPrice(orderReq.getProdId());
        Double discountsVal= client.GetDiscounts(orderReq.getProdId(),0).getDiscountVal();

        Long paymentId = client.CreatePayment(orderReq.getUserId(),prodVal,discountsVal);
        if(paymentId ==-1L){
            logger.warn("create payment failed OrderReq="+orderReq.toString());
            //TODO 升级resp类型
            orderResp.setBaseResp(PackResponse.packUnknownFailure());
            return orderResp;
        }
        Boolean re =storage.UpdatePaymentIdByOrderId(orderReq.getOrderId());
        //update order's paymentID
        List<OrderInfo> infos = storage.getOrderInfoByOrderId(orderReq.getOrderId());
        orderResp.setBaseResp(PackResponse.packSuccess());
        orderResp.setOrderInfos(infos);
        return orderResp;
    }

    @Override
    public OrderResp GetOrderByOrderId(OrderReq orderReq) {
        OrderResp orderResp = new OrderResp();
        if (!client.verifyAccessByAccount(orderReq.getUserId(),orderReq.getOrderId(), IdType.ORDER_ID)){
            orderResp.setBaseResp(PackResponse.packNoAuthority());
            orderResp.setHasMore(false);
            return orderResp;
        }
        List<OrderInfo> infos = orders.getOrderByOrderId(orderReq.getUserId(),orderReq.getOrderId(),orderReq.getStatus());
        if(infos == null){
            orderResp.setBaseResp(PackResponse.packUnknownFailure());
            return orderResp;
        }

        orderResp.setBaseResp(PackResponse.packSuccess());
        orderResp.setOrderInfos(infos);
        return orderResp;
    }


}
