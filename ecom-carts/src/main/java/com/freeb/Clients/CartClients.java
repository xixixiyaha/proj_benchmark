package com.freeb.Clients;

import com.freeb.Entity.OrderReq;
import com.freeb.Entity.OrderResp;

public abstract class CartClients {
    public abstract Boolean AccountExists(Long id);

    public abstract OrderResp CreateOrderByCartInfo(OrderReq orderReq);
}
