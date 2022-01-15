package com.freeb.Service;

import com.freeb.Enum.SearchOrder;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface ProductService {
    public ConcurrentHashMap<Integer, Integer> GetUserActiveByCategory(Long id);

    public Boolean CreateActiveBehavior(Long uid, Long pid, Integer cid);

    public List<Long> GetLastestAvtiveUsers(Integer userNum);

    public HashSet<Long> GetUserActiveByProduct(Long uid);

    public List<Long> GetProductByCategory(Integer cid, SearchOrder order, Integer prodNum);

    public List<Long> GetProductBySimilarity(Integer cid, SearchOrder order,String words,Integer prodNum);
}
