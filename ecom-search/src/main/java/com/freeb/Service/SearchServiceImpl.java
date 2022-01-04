package com.freeb.Service;

import com.freeb.Clients.SearchClients;
import com.freeb.Entity.ProductInfo;
import com.freeb.Enum.SearchOrder;
import com.freeb.Enum.SearchType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SearchServiceImpl implements SearchService {

    private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

    private SearchClients clients = new SearchClients();
    private Recommend rcmd = new Recommend(clients);
    private Boolean forceSearch = true;

    @Override
    public List<ProductInfo> GetRecommendByProdName(Long userId, String words, SearchType type, SearchOrder order) {

        List<ProductInfo> lst = new ArrayList<>();
        if(!clients.AccountExists(userId))return lst;
//        List<Integer> tags = clients.GetAccountTag(userId);

        if(!forceSearch){
            //TODO buffer
            logger.info("TODO@ GetRecommendByProdName: need add buffer");
        }else {
            switch (type){
                case OBJ_NAME:
                    return rcmd.GetRecommendProductsByProdName(userId,order,words);
                case MERCHANT_NAME:
                    //todo
                    break;
                default:
                    break;
            }
        }
        logger.warn("unsupported type");
        return null;
    }


    @Override
    public Boolean CreateUserClick(Long userId, Long prodId, Integer categoryId) {
        logger.info("CreateUserClick userId=%d"+userId);
        if(!clients.AccountExists(userId)){
            return false;
        }
        return rcmd.CreateUserClickBehavior(userId,prodId,categoryId);
    }

    class IdealComputationThread extends Thread{
        private Integer loopTime;
        private Long computationRe;

        IdealComputationThread(Integer loopt){
            this.loopTime = loopt;
        }

        @Override
        public void run() {
            Random seed = new Random(7L);
            Integer a = seed.nextInt(100)+50;
            Integer b= seed.nextInt(100)+50;
            long temp = 0L;
            for(int time = 0; time<this.loopTime; time++){
                if((time&3)==0){
                    temp = a+b;
                }else if((time&3)==1){
                    temp = a-b;
                }else if((time&3)==2){
                    temp = a*b;
                }else {
                    temp = a/b;
                }
            }
            this.computationRe=temp;
        }

        public Long getComputationRe(){
            return this.computationRe;
        }
    }

    @Override
    public List<Long> IdealResEfficiencyTest(Integer totalComputationLoad, Integer threadNum) {
        long bengintime = System.currentTimeMillis();
        List<Long> results = new ArrayList<>(threadNum);
        Integer loopPerThread = totalComputationLoad/threadNum;
        List<IdealComputationThread> threads = new ArrayList<>(threadNum);
        for(int i=0;i<threadNum;i++){
            IdealComputationThread thread = new IdealComputationThread(loopPerThread);
            thread.start();
            threads.set(i,thread);
        }
        for(IdealComputationThread thread:threads){
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for(Integer i=0;i<threadNum;i++){
            results.set(i,threads.get(i).getComputationRe());
        }
        long endtime = System.currentTimeMillis();
        long costtime = (endtime-bengintime)/1000;
        logger.info("costTime = "+costtime);
        return results;
    }
}
