package com.freeb.Clients;

import com.freeb.Entity.CommentInfo;
import com.freeb.Entity.DiscountInfo;
import com.freeb.Entity.ProductInfo;
import com.freeb.Enum.SearchOrder;
import com.freeb.Enum.SearchType;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public abstract class CategoryClients {

    public abstract List<Long> GetRecommendProdId(Long userId, String words, SearchType type, SearchOrder order);

    /*
     * 插入数据
     * */
    public abstract Boolean CreateUserClick(Long userId,Long prodId,Integer categoryId);

    public abstract List<Long> IdealResEfficiencyTest(Integer totalComputationLoad, Integer threadNum);

    public abstract ConcurrentHashMap<Integer, Integer> GetUserActiveByCategory(Long id);

    public abstract Boolean CreateActiveBehavior(Long uid, Long pid, Integer cid);

    public abstract List<Long> GetLastestAvtiveUsers(Integer userNum);

    public abstract HashSet<Long> GetUserActiveByProduct(Long uid);

    public abstract List<Long> GetProductByCategory(Integer cid, SearchOrder order,Integer prodNum);

    public abstract List<Long> GetProductBySimilarity(Integer cid, SearchOrder order,String words,Integer prodNum);

    public abstract ProductInfo GetProductInfo(Long id);

    public abstract List<CommentInfo> GetComments(Long prodId, Integer comtNum);

    public abstract DiscountInfo GetDiscounts(Long prodId,Integer type);
}
