package com.freeb.thrift.CartServer;

import com.freeb.Service.CartServiceImpl;
import com.freeb.thrift.CartClients.CartForeignClients;
import org.apache.thrift.TException;
import com.freeb.thrift.CartTypeConvert;

import java.util.ArrayList;
import java.util.List;

public class CartServiceServerImpl implements CartService.Iface{

    private final com.freeb.Service.CartService cartService = new CartServiceImpl(new CartForeignClients());


    @Override
    public List<CartInfo> GetCartInfosByAccount(long userId, int upperLimit) throws TException {
        List<com.freeb.Entity.CartInfo> lst = cartService.GetCartInfosByAccount(userId,upperLimit);
        List<CartInfo> re=new ArrayList<>();
        for(com.freeb.Entity.CartInfo info:lst){
            re.add(CartTypeConvert.CartInfoOri2Thr(info));
        }
        return re;
    }

    @Override
    public long BM3CompareChainNested(CartInfo info) throws TException {
        return cartService.BM3CompareChainNested(CartTypeConvert.CartInfoThr2Ori(info));
    }

    @Override
    public long BM3CompareChainSequence(CartInfo info) throws TException {
        return cartService.BM3CompareChainSequence(CartTypeConvert.CartInfoThr2Ori(info));
    }

    @Override
    public CartInfo GetCartInfoById(long cartId, long userId) throws TException {

        return CartTypeConvert.CartInfoOri2Thr(cartService.GetCartInfoById(cartId,userId));
    }
}
