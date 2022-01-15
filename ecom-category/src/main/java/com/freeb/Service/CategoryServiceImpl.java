package com.freeb.Service;

import com.freeb.Clients.CategoryClients;
import com.freeb.Dao.CategoryStorage;
import com.freeb.Entity.*;
import com.freeb.Enum.SearchOrder;
import com.freeb.Enum.SearchType;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

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
        @Override
        public void run() {
            info = clients.GetDiscounts(pid,type);
        }

        public DiscountInfo GetDiscount(){
            return info;
        }
    }

    @Override
    public ProductPage GetProductPage(Long prodId) {
        ProductPage page = new ProductPage();
        // 开线程
        Thread t1= new Thread()
        // t1 => prodInfo
        // t2 => prodComments
        // t3 => discounts

        return null;
    }

    @Override
    public List<CategoryPage> GetCategoryPage(Long userId, String searchKey) {
        List<Long> prodIds = clients.GetRecommendProdId(userId,searchKey, SearchType.OBJ_NAME, SearchOrder.SIMILARITY);
        // t1 => prodName, prodSales, prodNames, merchantId
        // t2 => follow t1, merchantName
        return null;
    }
}
