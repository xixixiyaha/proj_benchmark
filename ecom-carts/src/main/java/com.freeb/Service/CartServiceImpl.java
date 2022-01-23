package com.freeb.Service;

import com.freeb.Clients.CartClients;
import com.freeb.Dao.CartInfoStorage;
import com.freeb.Entity.CartInfo;
import com.freeb.Entity.OrderReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CartServiceImpl implements CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    private CartClients clients;
    private CartInfoStorage storage;

    public CartServiceImpl(CartClients c){
        this.clients = c;
        storage = new CartInfoStorage();
    }

//    @Override
//    public List<CartInfo> GetCartInfosByAccount(Long accountId) {
//        if(!clients.AccountExists(accountId))return null;
//        return storage.GetCartInfosByAccount(accountId);
//    }

    @Override
    public List<CartInfo> GetCartInfosByAccount(Long accountId, Integer upperLimit) {
        if(!clients.AccountExists(accountId))return null;
        return storage.GetCartInfosByAccount(accountId,upperLimit);
    }

    @Override
    public Long BM3CompareChainNested(CartInfo info) {
        logger.error("Method Not Implemented");

        OrderReq req = new OrderReq();
        req.setUserId(info.getUserId());
        req.setCartId(info.getCartId());
        return clients.CreateOrderByCartInfo(req).getOrderInfos().get(0).getOrderId();
    }

    @Override
    public Long BM3CompareChainSequence(CartInfo info) {
        logger.error("Method Not Implemented");
        return null;
    }

    @Override
    public CartInfo GetCartInfoById(Long cartId, Long userId) {
        if(!clients.AccountExists(userId)){
            return null;
        }
        return storage.GetCartInfoById(cartId);
    }
}
