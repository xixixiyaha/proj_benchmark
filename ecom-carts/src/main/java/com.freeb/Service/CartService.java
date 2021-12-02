package com.freeb.Service;

import com.freeb.Entity.CartInfo;

import java.util.List;

public interface CartService {
    /*
    * Big Data Transfer
    * */
    List<CartInfo> GetCartInfosByAccount(Long accountId);

    List<CartInfo> GetCartInfosByAccount(Long accountId,Integer upperLimit);


}
