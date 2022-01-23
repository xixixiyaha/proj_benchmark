package com.freeb.thrift.OrderServer;

import com.freeb.OrderTypeConvert;
import com.freeb.Service.OrderServiceImpl;
import com.freeb.thrift.OrderClient.OrderForeignClients;
import com.freeb.thrift.OrderReq;
import com.freeb.thrift.OrderResp;
import com.freeb.thrift.OrderService;
import org.apache.thrift.TException;

public class OrderServiceServerImpl implements OrderService.Iface{

    private final com.freeb.Service.OrderService orderService = new OrderServiceImpl(new OrderForeignClients());
    @Override
    public OrderResp GetOrderListByUserId(OrderReq orderReq) throws TException {
        return OrderTypeConvert.OrderRespOri2Thr(orderService.GetOrderListByUserId(OrderTypeConvert.OrderReqThr2Ori(orderReq)));
    }

    @Override
    public OrderResp GetOrderByPaymentId(OrderReq orderReq) throws TException {
        return OrderTypeConvert.OrderRespOri2Thr(orderService.GetOrderByPaymentId(OrderTypeConvert.OrderReqThr2Ori(orderReq)));
    }

    @Override
    public OrderResp GetOrderByOrderId(OrderReq orderReq) throws TException {
        return OrderTypeConvert.OrderRespOri2Thr(orderService.GetOrderByOrderId(OrderTypeConvert.OrderReqThr2Ori(orderReq)));
    }

    @Override
    public OrderResp CreateOrderByCartInfo(OrderReq orderReq) throws TException {
        return OrderTypeConvert.OrderRespOri2Thr(orderService.CreateOrderByCartInfo(OrderTypeConvert.OrderReqThr2Ori(orderReq)));
    }
}
