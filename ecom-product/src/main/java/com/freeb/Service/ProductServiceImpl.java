package com.freeb.Service;

import com.freeb.Clients.ProductClients;
import com.freeb.Dao.CommentStorage;
import com.freeb.Dao.MerchantStorage;
import com.freeb.Dao.ProductInfoStorage;
import com.freeb.Entity.CommentInfo;
import com.freeb.Entity.MerchantInfo;
import com.freeb.Entity.ProductInfo;
import com.freeb.Enum.SearchOrder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
    private BufferedReader in = null;


    public ProductServiceImpl(ProductClients clients) {
        this.clients = clients;
        storage = new ProductInfoStorage();
        mstorage = new MerchantStorage();
        cstorage = new CommentStorage();
    }


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

    private String GetCommentBySize(Integer datasize) {
        String filepath = "./" + String.valueOf(datasize) + "KBdata.txt";
        try {
            in = new BufferedReader(new FileReader(filepath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        char[] buf = new char[datasize];
        try {
            in.read(buf);
            return String.valueOf(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CommentInfo BM5CompareTransferDataSize(Integer dataSize) {
        CommentInfo cInfo = new CommentInfo();
        cInfo.setCommentDetails(GetCommentBySize(dataSize));
        return cInfo;
    }

    @Override
    public Boolean BM6CompareMemBindWidth(Integer dataSize) {
        //Notice: 1-20000之后的pid的commentSize要特殊化； 2-现在一次取1000个 （32B* 1000 = KB）
        Long prodId = 20000L+dataSize;
        List<CommentInfo> re= cstorage.GetComments(prodId,1000);
        return true;
    }

    @Override
    public List<CommentInfo> GetComments(Long prodId, Integer comtNum) {
        return cstorage.GetComments(prodId,comtNum);
    }

    @Override
    public MerchantInfo GetMerchantInfoByProd(Long pid) {

        return mstorage.GetMerchantInfoByProd(pid);
    }

    @Override
    public ProductInfo GetProdInfo(Long pid) {
        return storage.GetProductById(pid);
    }
}
