package com.freeb.Service;

import com.freeb.Clients.ProductClients;
import com.freeb.Dao.CommentStorage;
import com.freeb.Dao.MerchantStorage;
import com.freeb.Dao.ProductInfoStorage;
import com.freeb.Entity.CommentInfo;
import com.freeb.Entity.MerchantInfo;
import com.freeb.Entity.ProductInfo;
import com.freeb.Enum.SearchOrder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ProductServiceImpl implements ProductService {

    private ProductInfoStorage storage;
    private MerchantStorage mstorage;
    private CommentStorage cstorage;
    private int updateEveryClicks;
    private int curClicks;
    private ProductClients clients;


    @Override
    public HashMap<Integer, Integer> GetUserActiveByCategory(Long id) {
        return storage.GetUserActiveByCategory(id);
    }

    @Override
    public Boolean CreateActiveBehavior(Long uid, Long pid, Integer cid) {
        return storage.CreateActiveBehavior(uid,pid,cid);
    }

    @Override
    public List<Long> GetLastestActiveUsers(Integer userNum) {
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

    @Override
    public ProductInfo IncProductSales(Long pid, Integer purchaseNum) {
        Integer num = storage.GetProductNum(pid);
        if(num<purchaseNum){
            return null;
        }
        Boolean status = storage.IncProductSales(pid, purchaseNum);
        if(!status){
            return null;
        }
        return storage.GetProductById(pid);
    }

    @Override
    public MerchantInfo GetMerchantInfoById(Long mid) {
        return mstorage.GetMerchantInfoById(mid);
    }

    @Override
    public Boolean BM4ComparePatternTrigger(List<Long> uidLst,List<Long> pidLst,List<Integer> cidLst,Integer compLoad) {

        assert uidLst.size()==pidLst.size()&&pidLst.size()==cidLst.size();

        int len = uidLst.size();
        for(int pos=0;pos<len;pos++){
            CreateActiveBehavior(uidLst.get(pos),pidLst.get(pos),cidLst.get(pos));
            curClicks +=1;
            if(curClicks>updateEveryClicks){
                synchronized (this){
                    curClicks = 0;
                    List<Long> compUidLst = storage.GetLastestAvtiveUsers(compLoad);
                    clients.OfflineUserTagComputation(compUidLst);
                }
            }
        }

        return true;
    }

    @Override
    public CommentInfo BM5CompareTransferDataSize(Integer dataSize) {
        //TODO@ high priority
        return null;
    }

    @Override
    public Boolean BM6CompareMemBindWidth(Integer dataSize) {
        //TODO@ high priority
        return null;
    }

    @Override
    public List<CommentInfo> GetComments(Long prodId, Integer comtNum) {
        //TODO@ high priority
        return null;
    }
}
