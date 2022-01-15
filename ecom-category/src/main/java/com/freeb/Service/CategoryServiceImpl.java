package com.freeb.Service;

import com.freeb.Clients.CategoryClients;
import com.freeb.Dao.CategoryStorage;
import com.freeb.Entity.*;
import com.freeb.Enum.SearchOrder;
import com.freeb.Enum.SearchType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CategoryServiceImpl implements CategoryService {

    private ThreadPoolExecutor executor;
    private CategoryStorage cStorage;
    private CategoryClients clients;
    //Notice 对比线程数量
    public CategoryServiceImpl(String url,String name,String psw,Integer num) throws ClassNotFoundException {

        cStorage = new CategoryStorage(url,name,psw);
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(num);
    }

    class GetProductInfo implements Runnable{
        private ProductInfo info;
        private Long pid;
        GetProductInfo(Long id){
            pid=id;
        }
        @Override
        public void run() {
            info = clients.GetProductInfo(pid);
        }
        public ProductInfo GetInfo(){
            return info;
        }
    }

    class GetProdComments implements Runnable{
        private List<CommentInfo> infos;
        private Long pid;
        private Integer num;
        GetProdComments(Long id,Integer commentNum){
            pid=id;
            num = commentNum;
        }
        @Override
        public void run() {
            infos = clients.GetComments(pid,num);
        }

        public List<CommentInfo> GetComments(){
            return infos;
        }
    }

    class GetProdDiscount implements Runnable{

        private DiscountInfo info;
        private Long pid;
        private Integer type;
        GetProdDiscount(Long id,Integer t){
            pid = id;
            type = t;
        }
        @Override
        public void run() {
            info = clients.GetDiscounts(pid,type);
        }

        public DiscountInfo GetDiscount(){
            return info;
        }
    }

    class GetMerchantNames implements Runnable{
        private List<String> names;
        private List<Long> pids;
        GetMerchantNames(List<Long> ids){
            pids = ids;
        }

        @Override
        public void run() {
            names = clients.GetMerchantNames(pids);
        }
        public List<String> GetNames(){
            return names;
        }
    }

    @Override
    public ProductPage GetProductPage(Long prodId) {
        ProductPage page = new ProductPage();
        // 开线程
        // t1 => prodInfo
        GetProductInfo t1 = new GetProductInfo(prodId);
        executor.submit(t1);
        // t2 => prodComments
        GetProdComments t2 = new GetProdComments(prodId,100);
        executor.submit(t2);
        // t3 => discounts
        GetProdDiscount t3 = new GetProdDiscount(prodId,0);
        executor.submit(t3);
        try {
            executor.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ProductInfo info = t1.GetInfo();
        page.setInfo(info);
        page.setProdComments(t2.GetComments());
        page.setDiscountVal(t3.GetDiscount());
        return page;
    }

    @Override
    public List<CategoryPage> GetCategoryPage(Long userId, String searchKey) {
        List<Long> prodIds = clients.GetRecommendProdId(userId,searchKey, SearchType.OBJ_NAME, SearchOrder.SIMILARITY);

        List<GetProductInfo> tasks = new ArrayList<>();
        List<GetMerchantNames> task1s = new ArrayList<>();

        // t2 => follow t1, merchantName
        GetMerchantNames t2 = new GetMerchantNames(prodIds);
        executor.submit(t2);
        // t1 => prodName, prodSales, prodNames, merchantId
        // t1 => prodInfo
        for(Long prodId:prodIds){

            GetProductInfo t1 = new GetProductInfo(prodId);
            executor.submit(t1);
            tasks.add(t1);
        }
        try {
            executor.awaitTermination(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }
}
