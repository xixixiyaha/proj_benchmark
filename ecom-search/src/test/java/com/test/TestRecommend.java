package com.test;
import com.freeb.Clients.SearchClients;
import com.freeb.Dao.ProductInfoStorage;
import com.freeb.Entity.ProductInfo;
import com.freeb.Service.Recommend;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class TestRecommend {

    private static final Logger logger = LoggerFactory.getLogger(TestRecommend.class);

    static String PROD_DB_URL = "jdbc:mysql://sh-cynosdbmysql-grp-fdsb56no.sql.tencentcdb.com:24262/FREEB";
    static String PROD_USER ="root";
    static String PROD_PWD = "1204Adzq";

    private SearchClients clients;
    private ProductInfoStorage storage;
    private Recommend rcmdService;

    public void CreateData(Long userNum,Long prodNum,Integer categoryNum){
        HashMap<Long,Integer> obj2Category = new HashMap<>();

        Random rand =new Random(25);
        Long pid;
        Integer cid;
        for(Long uid=0L;uid<userNum;uid++){
            for(int i=0;i<prodNum;i++){
                pid = (long)rand.nextInt(500);
                if( obj2Category.containsKey(pid)){
                    cid = obj2Category.get(pid);
                }else {
                    cid=rand.nextInt(categoryNum);
                    obj2Category.put(pid,cid);
                }
                Boolean re = storage.CreateActiveBehavior(uid,pid,cid);
                if(!re){
                    logger.error("insert failed round"+uid+" "+i);
                }
            }
        }
    }

    @Before
    public void initi() throws ClassNotFoundException {
        storage = new ProductInfoStorage(PROD_DB_URL,PROD_USER,PROD_PWD);
        rcmdService = new Recommend(storage);
    }

    @Test
    public void TestGetUserTags(){
        CreateData(2L,200L,15);
        Map<Integer,Double> tags1 = rcmdService.GetUserTags(0L);
        logger.info(tags1.toString());
        Map<Integer,Double> tags2 = rcmdService.GetUserTags(1L);
        logger.info(tags2.toString());
    }

    @Test
    public void TestAssembleUserClicks(){
        long startTime = System.nanoTime();
        ConcurrentHashMap<Long, HashSet<Long>> rcmdProd = rcmdService.AssembleUserClicks();
        long endTime = System.nanoTime();

        long duration = (long) ((endTime - startTime)/1e6);  //divide by 1000000 to get milliseconds.

        logger.debug("duration is "+duration);
        logger.debug(rcmdProd.toString());

    }


}
