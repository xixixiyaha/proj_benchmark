package com.freeb.Service;

import com.freeb.Clients.CategoryClients;
import com.freeb.Dao.CategoryStorage;
import com.freeb.Entity.*;
import com.freeb.Enum.SearchOrder;
import com.freeb.Enum.SearchType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

public class CategoryServiceImpl implements CategoryService {
    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    private ThreadPoolExecutor executor;
    private CategoryStorage cStorage;
    private CategoryClients clients;
    //Notice 对比线程数量
    public CategoryServiceImpl(CategoryClients c,Integer num) throws ClassNotFoundException {
        clients = c;
        cStorage = new CategoryStorage();
        //TODO 配置参数
        executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(num);
    }

    class GetProductInfo implements Callable<ProductInfo> {
        private Long pid;
        GetProductInfo(Long id){
            pid=id;
        }

        @Override
        public ProductInfo call() throws Exception {
            return clients.GetProductInfo(pid);
        }
    }
    class GetProductInfoLst implements Callable<List<ProductInfo>>{
        private List<ProductInfo> info;
        private Long startPid,endPid;
        GetProductInfoLst(Long sid,Long eid){
            startPid=sid;
            endPid=eid;
        }

        @Override
        public List<ProductInfo> call() throws Exception {
            for (long pid = startPid; pid <= endPid; pid++) {
                info.add(clients.GetProductInfo(pid));
            }
            return info;
        }

    }

    class GetProdComments implements Callable<List<CommentInfo>>{
        private Long pid;
        private Integer num;
        GetProdComments(Long id,Integer commentNum){
            pid=id;
            num = commentNum;
        }
        @Override
        public List<CommentInfo> call() {
            return clients.GetComments(pid, num);
        }

    }

    class GetProdDiscount implements Callable<DiscountInfo>{

        private Long pid;
        private Integer type;
        GetProdDiscount(Long id,Integer t){
            pid = id;
            type = t;
        }
        @Override
        public DiscountInfo call() {
            return clients.GetDiscounts(pid, type);
        }

    }

    class GetMerchantInfo implements Callable<MerchantInfo>{
        private Long pid;
        GetMerchantInfo(Long id){
            pid = id;
        }

        @Override
        public MerchantInfo call() {
            MerchantInfo info = clients.GetMerchantInfoByProd(pid);
            return info;
        }
    }

    @Override
    public ProductPage GetProductPage(Long prodId) {
        ProductPage page = new ProductPage();
        // 开线程
        // t1 => prodInfo
        GetProductInfo t1 = new GetProductInfo(prodId);
        Future<ProductInfo> f1 =executor.submit(t1);
        // t2 => prodComments
        GetProdComments t2 = new GetProdComments(prodId,100);
        Future<List<CommentInfo>> f2=executor.submit(t2);
        // t3 => discounts
        GetProdDiscount t3 = new GetProdDiscount(prodId,0);
        Future<DiscountInfo> f3 =executor.submit(t3);
        try {
            ProductInfo info = f1.get();
            page.setInfo(info);
            page.setProdComments(f2.get());
            page.setDiscountVal(f3.get().getDiscountVal());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<CategoryPage> GetCategoryPage(Long userId, String searchKey) {
        // 1. get product ids
        List<Long> prodIds = clients.GetRecommendByProdName(userId,searchKey, SearchType.OBJ_NAME, SearchOrder.SIMILARITY);

        List<GetProductInfo> tasks = new ArrayList<>();
        List<GetMerchantInfo> tasks2 = new ArrayList<>();
        // 2. t2 => prod -> merchantName
        //    t1 => prodName, prodSales, prodNames, merchantId
        for(Long prodId:prodIds){

            GetProductInfo t1 = new GetProductInfo(prodId);
            executor.submit(t1);
            GetMerchantInfo t2 = new GetMerchantInfo(prodId);
            executor.submit(t1);
            tasks.add(t1);
            tasks2.add(t2);
        }
        //TODO@ runtime: CHECK if the function can work
        try {
            List<Future<ProductInfo>> f1= executor.invokeAll(tasks);
            List<Future<MerchantInfo>> f2=executor.invokeAll(tasks2);

            List<CategoryPage> re = new ArrayList<>();
            int pos = 0;
            for(Long pid:prodIds){
                ProductInfo pInfo = f1.get(pos).get();
                MerchantInfo mInfo = f2.get(pos).get();
                if(mInfo.getMerchantId().equals(pInfo.getMerchantId())){
                    re.add(new CategoryPage(pid,pInfo.getProdName(),pInfo.getProdSales(),pInfo.getProdImages().get(0),pInfo.getMerchantId(),mInfo.getMerchantName()));
                }else {
                    logger.warn("product Info doesnt match ");
                    re.add(new CategoryPage(pid,pInfo.getProdName(),pInfo.getProdSales(),pInfo.getProdImages().get(0),null,null));
                }
                pos+=1;
                // Notice@ Notice ImagesList == null ?
            }
            return re;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ProductInfo> BM2CompareParallelRpcEfficiency(Integer totalComputationLoad, Integer threadNum) {
        int loopPerThread = totalComputationLoad/threadNum;
            List<GetProductInfoLst> tasks = new ArrayList<>();
        for(int tid = 0; tid<threadNum; tid++){

            GetProductInfoLst t1 = new GetProductInfoLst((long) (tid * loopPerThread + 1),(long) (tid+1)*loopPerThread);
            executor.submit(t1);
            tasks.add(t1);
        }
        try {
            List<Future<List<ProductInfo>>> f1 = executor.invokeAll(tasks);
            List<ProductInfo> re = new ArrayList<>();
            for(Future<List<ProductInfo>> f:f1){
                re.addAll(f.get());
            }
            return re;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<ProductPage> BM4ComparePatternFanout(List<Long> pidLst){
        List<ProductPage> re = new ArrayList<>();
        for(long pid:pidLst) {
            re.add(GetProductPage(pid));
        }
        return re;
    }
}
