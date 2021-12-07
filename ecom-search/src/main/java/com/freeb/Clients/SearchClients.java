package com.freeb.Clients;

import com.freeb.Dao.ProductInfoStorage;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SearchClients {

    private ProductInfoStorage storage = new ProductInfoStorage();

    public Boolean AccountExists(Long id){
        return true;
    }
    public List<Integer> GetAccountTag(Long id) {
        // todo
        return null;
    }

    public Map<Integer,Double> GetUserTags(Long id){
        // TODO & NOTICE : This is local version -- should in account
        ConcurrentHashMap<Integer, Integer> categoryTags = storage.GetUserActiveByCategory(id);
        Integer sumClicks = 0;
        Map<Integer,Double> tags = new HashMap<>();
        Set<Integer> valSet = (Set<Integer>) categoryTags.values();
        for (Integer integer : valSet) {
            sumClicks += integer;
        }
        for(Map.Entry<Integer,Integer> entry:categoryTags.entrySet()){
            tags.put(entry.getKey(),(entry.getValue()*1.0)/sumClicks);
        }
        return tags;
    }
    public Boolean SetUserTags(Long userId,HashMap<Integer,Double> tags){
        //todo
        return null;
    }


}
