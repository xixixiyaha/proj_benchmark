package com.freeb.Service;

import com.freeb.Clients.CategoryClients;
import com.freeb.Dao.CategoryStorage;
import com.freeb.Entity.*;
import com.freeb.Enum.SearchOrder;
import com.freeb.Enum.SearchType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CategoryServiceImpl implements CategoryService {
    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    //TODO excutor
    private ThreadPoolExecutor executor;
    private CategoryStorage cStorage;
    private CategoryClients clients;
    //Notice 对比线程数量
    public CategoryServiceImpl(CategoryClients c,Integer num) throws ClassNotFoundException {
        clients = c;
        cStorage = new CategoryStorage();
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
    class GetProductInfoLst implements Runnable{
        private List<ProductInfo> info;
        private Long startPid,endPid;
        GetProductInfoLst(Long sid,Long eid){
            startPid=sid;
            endPid=eid;
        }
        @Override
        public void run() {
            for (long pid = startPid; pid <= endPid; pid++) {
                info.add(clients.GetProductInfo(pid));
            }
        }
        public List<ProductInfo> GetInfoLst(){
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

    class GetMerchantInfo implements Runnable{
        private MerchantInfo info;
        private Long pid;
        GetMerchantInfo(Long id){
            pid = id;
        }

        @Override
        public void run() {
            info = clients.GetMerchantInfoByProd(pid);
        }
        public MerchantInfo GetInfo(){
            return info;
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
        page.setDiscountVal(t3.GetDiscount().getDiscountVal());
        return page;
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
        //TODO CHECK if the function is that meaning
        try {
            executor.awaitTermination(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<CategoryPage> re = new ArrayList<>();
        int pos = 0;
        for(Long pid:prodIds){
            ProductInfo pInfo = tasks.get(pos).GetInfo();
            MerchantInfo mInfo = tasks2.get(pos).GetInfo();
            if(mInfo.getMerchantId().equals(pInfo.getMerchantId())){
                re.add(new CategoryPage(pid,pInfo.getProdName(),pInfo.getProdSales(),pInfo.getProdImages().get(0),pInfo.getMerchantId(),mInfo.getMerchantName()));
            }else {
                logger.warn("product Info doesnt match ");
                re.add(new CategoryPage(pid,pInfo.getProdName(),pInfo.getProdSales(),pInfo.getProdImages().get(0),null,null));
            }
            // Notice ImagesList == null ?
        }
        return re;
    }

    //TODO@ high-priority executor
    @Override
    public List<ProductInfo> BM2CompareParallelRpcEfficiency(Integer totalComputationLoad, Integer threadNum) {
        int loopPerThread = totalComputationLoad/threadNum;
            List<GetProductInfoLst> tasks = new ArrayList<>();

        for(int tid = 0; tid<threadNum; tid++){

            GetProductInfoLst t1 = new GetProductInfoLst((long) (tid * loopPerThread + 1),(long) (tid+1)*loopPerThread);
            executor.submit(t1);
            tasks.add(t1);
        }
        //TODO CHECK if the function is that meaning
        try {
            executor.awaitTermination(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<ProductInfo> re = new ArrayList<>();
        for(GetProductInfoLst task:tasks){
            re.addAll(task.GetInfoLst());
        }
        return re;
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
