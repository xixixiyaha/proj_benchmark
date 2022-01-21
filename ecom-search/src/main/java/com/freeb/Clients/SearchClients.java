package com.freeb.Clients;


import com.freeb.Enum.SearchOrder;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class SearchClients {


    public abstract List<Integer> GetAccountTag(Long id);
    public abstract Boolean SetAccountTag(Long id, String jsonStr);

    public abstract Boolean AccountExists(Long id);

    public abstract ConcurrentHashMap<Integer, Integer> GetUserActiveByCategory(Long id);

    public abstract Boolean CreateActiveBehavior(Long uid, Long pid, Integer cid);

    public abstract List<Long> GetLastestActiveUsers(Integer userNum);

    public abstract HashSet<Long> GetUserActiveByProduct(Long uid);

    public abstract List<Long> GetProductByCategory(Integer cid, SearchOrder order,Integer prodNum);

    public abstract List<Long> GetProductBySimilarity(Integer cid, SearchOrder order,String words,Integer prodNum);
}
