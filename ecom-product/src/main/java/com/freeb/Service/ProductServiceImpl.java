package com.freeb.Service;

import com.freeb.Dao.ProductInfoStorage;
import com.freeb.Entity.ProductInfo;
import com.freeb.Enum.SearchOrder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ProductServiceImpl implements ProductService {

    ProductInfoStorage storage;



    @Override
    public ConcurrentHashMap<Integer, Integer> GetUserActiveByCategory(Long id) {
        return storage.GetUserActiveByCategory(id);
    }

    @Override
    public Boolean CreateActiveBehavior(Long uid, Long pid, Integer cid) {
        return storage.CreateActiveBehavior(uid,pid,cid);
    }

    @Override
    public List<Long> GetLastestAvtiveUsers(Integer userNum) {
        return storage.GetLastestAvtiveUsers(userNum);
    }

    @Override
    public HashSet<Long> GetUserActiveByProduct(Long uid) {
        return storage.GetUserActiveByProduct(uid);
    }

    @Override
    public List<Long> GetProductByCategory(Integer cid, SearchOrder order, Integer prodNum) {
        List<ProductInfo> infos = storage.GetProductByCategory(cid,order,prodNum);
        List<Long> re = new ArrayList<>();
        for(ProductInfo info:infos){
            re.add(info.getProdId());
        }
        return re;
    }

    @Override
    public List<Long> GetProductBySimilarity(Integer cid, SearchOrder order, String words, Integer prodNum) {
        List<ProductInfo> infos = storage.GetProductBySimilarity(cid,order,words,prodNum);
        List<Long> re = new ArrayList<>();
        for(ProductInfo info:infos){
            re.add(info.getProdId());
        }
        return re;
    }
}
