package com.freeb.Service;

import com.freeb.Entity.OrderReq;
import com.freeb.Entity.OrderResp;

public interface OrdersService {

    OrderResp GetOrderListByUserId(OrderReq orderReq);

    OrderResp GetOrderByPaymentId(OrderReq orderReq);

    OrderResp CreatePaymentByOrderId(OrderReq orderReq);

    OrderResp GetOrderByOrderId(OrderReq orderReq);




}
