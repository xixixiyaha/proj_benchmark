package com.freeb.Service;

import com.freeb.Clients.SearchClients;
import com.freeb.Entity.ProductInfo;
import com.freeb.Enum.SearchOrder;
import com.freeb.Enum.SearchType;

import com.freeb.Utils.MapUtil;
import org.json.simple.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class SearchServiceImpl implements SearchService {

    private static final Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

    private SearchClients clients ;
    private Recommend rcmd;
    private Boolean forceSearch = true;

    public SearchServiceImpl(SearchClients c){

        this.clients = c;
        this.rcmd = new Recommend(this.clients);
//        System.out.println("DBG@ init SearchServiceImpl");
    }


    @Override
    public List<Long> GetRecommendByProdName(Long userId, String words, SearchType type, SearchOrder order) {

        List<Long> lst = new ArrayList<>();
        if(!clients.AccountExists(userId))return lst;
//        List<Integer> tags = clients.GetAccountTag(userId);

        if(!forceSearch){
            //TODO@ low priority buffer
            logger.info("GetRecommendByProdName: need add buffer");
        }else {
            switch (type){
                case OBJ_NAME:
                    return rcmd.GetRecommendProductsByProdName(userId,order,words);
                case MERCHANT_NAME:

                    break;
                default:
                    break;
            }
        }
        logger.warn("unsupported type");
        return null;
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
            Integer a;
            Integer b;
            long temp = 0L;
            for(int time = 0; time<this.loopTime; time++){
                a = seed.nextInt(100)+50;
                b= seed.nextInt(100)+50;
                if((time&3)==0){
                    temp += a+b;
                }else if((time&3)==1){
                    temp += a-b;
                }else if((time&3)==2){
                    temp += a*b;
                }else {
                    temp += a/b;
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
        //TODO@high
//        long begintime = System.currentTimeMillis();
        List<Long> results = new ArrayList<>();
        results.add((long) (totalComputationLoad+threadNum));
//        Integer loopPerThread = totalComputationLoad/threadNum;
//        List<IdealComputationThread> threads = new ArrayList<>();
//        for(int i=0;i<threadNum;i++){
//            IdealComputationThread thread = new IdealComputationThread(loopPerThread);
//            thread.start();
////            System.out.println("DBG@ threads len = "+threads.size());
//            threads.add(thread);
////            System.out.println("DBG@ put "+i+" thread");
//        }
//        for(IdealComputationThread thread:threads){
//            try {
//                thread.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        for(int i = 0; i<threadNum; i++){
//            results.add(threads.get(i).getComputationRe());
//        }
//        long endtime = System.currentTimeMillis();
//        long costtime = (endtime-begintime);
//        logger.info("beginTime = "+begintime);
//        logger.info("endTime = "+endtime);
//        logger.info("costTime = "+costtime);
        return results;
    }

    @Override
    public Boolean BM2CompareParallelEfficiency(Integer totalComputationLoad, Integer threadNum, Integer type) {
        switch (type){
            case 1:
                /*先RPC fetch数据 再计算相似度 */
                return BM2CompRpcEfficiency(totalComputationLoad,threadNum);
            default:
                /* computation heavy 复用*/
                IdealResEfficiencyTest(totalComputationLoad,threadNum);
                return true;
        }
    }
    private List<Integer> SortMapByVal(Map<Integer,Double> map,Integer topN){
        List<Map.Entry<Integer,Double>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());
        Collections.reverse(list);
        List<Integer> result = new ArrayList<>();
        for(int i=0;i<topN;i++){
            result.add(list.get(i).getKey());
        }
        return result;
    }
    private String ConvertList2JsonArray(List<Integer> cidLst){
        JSONArray jarr = new JSONArray();
        //TODO --check 这个警告是什么
        jarr.addAll(cidLst);
        return jarr.toJSONString();
    }

    @Override
    public Boolean OfflineUserTagComputation(List<Long> uidLst) {
        //直接从数据库拿数据计算 不走client

        // 1. 取得 Product sql 连接
        this.rcmd.SetStorage();
        this.rcmd.SetOffline(true);
        // 2. 获取要计算的 List<userId> => 参数 uidLst
        Map<Long,String> usersTag = new HashMap<>();
        // 3.  for(uid:List<uid>) Recommend 模块通过本地SQL连接获取计算数据
        for(Long uid:uidLst){
            usersTag.put(uid,ConvertList2JsonArray(SortMapByVal(rcmd.GetUserTags(uid),10)));
        }
        // 4. 对于得到的数据 调用RPC更新Account的UserTag
        for(Map.Entry<Long,String> entry:usersTag.entrySet()){
            clients.SetAccountTag(entry.getKey(),entry.getValue());
        }

        return true;
    }



    public Boolean BM2CompRpcEfficiency(Integer totalUserNum,Integer threadNum){
        long begintime = System.currentTimeMillis();

        Boolean re =rcmd.CompRpcEfficiency(totalUserNum,threadNum);

        long endtime = System.currentTimeMillis();
        long costtime = (endtime-begintime);
        logger.info("beginTime = "+begintime);
        logger.info("endTime = "+endtime);
        logger.info("costTime = "+costtime);
        return true;
    }
}
