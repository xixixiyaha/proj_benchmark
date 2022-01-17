package com.freeb.Service;

import com.freeb.Clients.CartClients;
import com.freeb.Dao.CartInfoStorage;
import com.freeb.Entity.CartInfo;

import java.util.List;

public class CartServiceImpl implements CartService {
    // TODO 虚类
    private CartClients clients;
    private CartInfoStorage storage = new CartInfoStorage();

    @Override
    public List<CartInfo> GetCartInfosByAccount(Long accountId) {
        if(!clients.AccountExists(accountId))return null;
        return storage.GetCartInfosByAccount(accountId);
    }

    @Override
    public List<CartInfo> GetCartInfosByAccount(Long accountId, Integer upperLimit) {
        if(!clients.AccountExists(accountId))return null;
        return storage.GetCartInfosByAccount(accountId,upperLimit);
    }
}
