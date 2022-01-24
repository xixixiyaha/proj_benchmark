package com.test;
import com.freeb.Clients.SearchClients;
import com.freeb.Entity.ProductInfo;
import com.freeb.Service.Recommend;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TestRecommend {

    private static final Logger logger = LoggerFactory.getLogger(TestRecommend.class);

    static String PROD_DB_URL = "jdbc:mysql://sh-cynosdbmysql-grp-fdsb56no.sql.tencentcdb.com:24262/FREEB";
    static String PROD_USER ="root";
    static String PROD_PWD = "1204Adzq";

    private SearchClients clients;
    private Recommend rcmdService;

    public void CreateData(Long userNum,Long prodNum,Integer categoryNum,Long startUserNum){
        HashMap<Long,Integer> obj2Category = new HashMap<>();

        Random rand =new Random(25);
        Long pid;
        Integer cid;
        for(Long uid=startUserNum;uid<userNum+startUserNum;uid++){
            for(int i=0;i<prodNum;i++){
                pid = (long)rand.nextInt(500);
                if( obj2Category.containsKey(pid)){
                    cid = obj2Category.get(pid);
                }else {
                    cid=rand.nextInt(categoryNum);
                    obj2Category.put(pid,cid);
                }
                Boolean re = clients.CreateActiveBehavior(uid,pid,cid);
                if(!re){
                    logger.error("insert failed round"+uid+" "+i);
                }
            }
        }
    }

    private void TruncateDB(){
        try(Connection conn = DriverManager.getConnection(PROD_DB_URL, PROD_USER, PROD_PWD)){
            PreparedStatement stmt = conn.prepareStatement("truncate table USER_ACTIVE_INFOS");
            int re = stmt.executeUpdate();
            if(re<0){
                logger.warn("truncate table USER_ACTIVES FAILED !");
            }
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
        }
    }

    @Before
    public void initi() throws ClassNotFoundException {
        rcmdService = new Recommend(clients);
    }

    @Test
    public void TestGetUserTags(){
        TruncateDB();
        CreateData(2L,200L,15,0L);
        Map<Integer,Double> tags1 = rcmdService.GetUserTags(0L);
        logger.info(tags1.toString());
        Map<Integer,Double> tags2 = rcmdService.GetUserTags(1L);
        logger.info(tags2.toString());
    }

    @Test
    public void TestAssembleUserBehavior(){
        TruncateDB();
        CreateData(1500L,200L,15,0L);
        long startTime = System.nanoTime();
        HashMap<Long, HashMap<Integer, Integer>> activeMap = rcmdService.AssembleUserBehavior();
        long endTime = System.nanoTime();
        long duration = (long) ((endTime - startTime)/1e6);  //divide by 1000000 to get milliseconds.


        logger.debug("duration is "+duration);
        logger.debug(activeMap.toString());
    }

    @Test
    public void TestAssembleUserClicks(){
        TruncateDB();
        CreateData(1000L,51L,15,0L);
        long startTime = System.nanoTime();
        ConcurrentHashMap<Long, HashSet<Long>> rcmdProd = rcmdService.AssembleUserClicks();
        long endTime = System.nanoTime();

        long duration = (long) ((endTime - startTime)/1e6);  //divide by 1000000 to get milliseconds.

        logger.debug("duration is "+duration);
        logger.debug(rcmdProd.toString());

    }

    private void CreateSimilarUser(Long userNum,List<Integer> similarity,Long prodNum,int categoryNum,Long startUserNum){
        Random rand = new Random(7);
        HashMap<Long,Integer> obj2Category = new HashMap<>();
        List<Long> prodLists = new ArrayList<>();
        Long pid;
        Integer cid;
        for(int i=0;i<prodNum;i++){
            pid = (long)rand.nextInt(500);
            prodLists.add(pid);
            if( obj2Category.containsKey(pid)){
                cid = obj2Category.get(pid);
            }else {
                cid=rand.nextInt(categoryNum);
                obj2Category.put(pid,cid);
            }
            Boolean re = clients.CreateActiveBehavior((long) startUserNum,pid,cid);
            if(!re){
                logger.error("insert failed round"+startUserNum+" "+i);
            }
        }
        int cnter;
        for(Integer idx = 0;idx<similarity.size();idx++){
            for(long uid = (long) userNum*idx; uid<userNum*(idx+1); uid++){
                for(int i=0;i<prodNum;i++){
                    cnter = rand.nextInt(100);
                    if(cnter<similarity.get(idx)){
                        pid = prodLists.get(i);
                        cid = obj2Category.get(pid);
                    }else{
                        pid = (long)rand.nextInt(500);
                        if( obj2Category.containsKey(pid)){
                            cid = obj2Category.get(pid);
                        }else {
                            cid=rand.nextInt(categoryNum);
                            obj2Category.put(pid,cid);
                        }
                    }
                    Boolean re = clients.CreateActiveBehavior((uid+1L+startUserNum),pid,cid);
                    if(!re){
                        logger.error("insert failed round"+uid+" "+i);
                    }
                }
            }
        }


    }

    @Test
    public void TestCalcSimilarityBetweenUsers(){
//        TruncateDB();
        Long targetUser = 1000L;
        List<Integer> similarity = List.of(85,75,60,50,40,30);
        long startTime = System.nanoTime();
        CreateSimilarUser(10L,similarity,60L,15,targetUser);
        long createTime = System.nanoTime();
        HashMap<Long,Double> similarities =  rcmdService.calcSimilarityBetweenUsers(targetUser,rcmdService.AssembleUserClicks());
        long calcTime = System.nanoTime();

        long dbDuration = (long) ((createTime - startTime)/1e6);
        logger.debug("dbDuration is "+dbDuration);
        long calcDurition = (long) ((calcTime - startTime)/1e6);
        logger.debug("calcDurition is "+calcDurition);
        logger.info(similarities.toString());

        startTime = System.nanoTime();
        List<Map.Entry<Long,Double>> similarityList = rcmdService.GetTopNSimilarityUserSimilarity(similarities,15);
        List<Integer> rcmdCateList = rcmdService.GetRecommendCategory(targetUser,similarityList,15,10);
        calcTime = System.nanoTime();
        calcDurition = (long) ((calcTime - startTime)/1e6);
        logger.debug("calcDurition is "+calcDurition);
        logger.info("simiList = "+similarityList.toString());
        logger.info("rcmdCateList = "+rcmdCateList.toString());

    }

}
