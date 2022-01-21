package com.freeb.Service;

import com.freeb.Entity.CommentInfo;
import com.freeb.Entity.MerchantInfo;
import com.freeb.Entity.ProductInfo;
import com.freeb.Enum.SearchOrder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface ProductService {
    public HashMap<Integer, Integer> GetUserActiveByCategory(Long id);

    public Boolean CreateActiveBehavior(Long uid, Long pid, Integer cid);

    public List<Long> GetLastestActiveUsers(Integer userNum);

    public HashSet<Long> GetUserActiveByProduct(Long uid);

    public List<Long> GetProductByCategory(Integer cid, SearchOrder order, Integer prodNum);

    public List<Long> GetProductBySimilarity(Integer cid, SearchOrder order,String words,Integer prodNum);

    public ProductInfo IncProductSales(Long pid,Integer purchaseNum);

    public MerchantInfo GetMerchantInfoById(Long mid);

    Boolean BM4ComparePatternTrigger(List<Long> uidLst,List<Long> pidLst,List<Integer> cidLst,Integer compLoad);

    CommentInfo BM5CompareTransferDataSize(Integer dataSize);

    Boolean BM6CompareMemBindWidth(Integer dataSize);

    List<CommentInfo> GetComments(Long prodId, Integer comtNum);

    MerchantInfo GetMerchantInfoByProd(Long pid);

    ProductInfo GetProdInfo(Long pid);

}
