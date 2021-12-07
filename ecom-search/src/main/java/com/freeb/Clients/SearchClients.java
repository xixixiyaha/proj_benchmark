package com.freeb.Clients;

import com.freeb.Dao.ProductInfoStorage;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SearchClients {


    private ProductInfoStorage storage ;
    public SearchClients(){
        storage = new ProductInfoStorage();
    }
    public SearchClients(ProductInfoStorage storage){
        this.storage = storage;
    }


    public Boolean AccountExists(Long id){
        return true;
    }
    public List<Integer> GetAccountTag(Long id) {
        // todo
        return null;
    }


    public Boolean SetUserTags(Long userId,HashMap<Integer,Double> tags){
        //todo
        return null;
    }


}
