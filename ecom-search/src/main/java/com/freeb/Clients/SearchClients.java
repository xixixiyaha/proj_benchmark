package com.freeb.Clients;


import com.freeb.Enum.SearchOrder;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class SearchClients {


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

    public abstract ConcurrentHashMap<Integer, Integer> GetUserActiveByCategory(Long id);

    public abstract Boolean CreateActiveBehavior(Long uid, Long pid, Integer cid);

    public abstract List<Long> GetLastestAvtiveUsers(Integer userNum);

    public abstract HashSet<Long> GetUserActiveByProduct(Long uid);

    public abstract List<Long> GetProductByCategory(Integer cid, SearchOrder order,Integer prodNum);

    public abstract List<Long> GetProductBySimilarity(Integer cid, SearchOrder order,String words,Integer prodNum);
}
