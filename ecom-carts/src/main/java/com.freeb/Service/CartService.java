package com.freeb.Service;

import com.freeb.Entity.CartInfo;
import com.freeb.Entity.OrderResp;

import java.util.List;

public interface CartService {
    /*
    * Big Data Transfer
    * */
//    List<CartInfo> GetCartInfosByAccount(Long accountId);

    List<CartInfo> GetCartInfosByAccount(Long accountId,Integer upperLimit);

    Long BM3CompareChainNested(CartInfo info);

    Long BM3CompareChainSequence(CartInfo info);

    CartInfo GetCartInfoById(Long cartId,Long userId);


}
