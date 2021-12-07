package com.test;

import com.freeb.Dao.ProductInfoStorage;
import com.freeb.Entity.ProductInfo;
import com.freeb.Service.Recommend;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class TestProductDao {

    private static final Logger logger = LoggerFactory.getLogger(TestProductDao.class);

    static String PROD_DB_URL = "jdbc:mysql://sh-cynosdbmysql-grp-fdsb56no.sql.tencentcdb.com:24262/FREEB";
    static String PROD_USER ="root";
    static String PROD_PWD = "1204Adzq";

    private ProductInfoStorage storage;
    @Before
    public void Init() throws ClassNotFoundException {
        storage = new ProductInfoStorage(PROD_DB_URL,PROD_USER,PROD_PWD);
    }

    @Test
    public void TestConnection(){
        assert storage.TestConnection();
    }

    private void CreateData(Long userNum,Long prodNum,Integer categoryNum){
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

    @Test
    public void TestCreateData(){
        // 插入 param=100个用户 * param=100 个物品 => 1w条 => 20MB
        // 物品 [0,1000]
        // category [0,50]
        this.CreateData(100L,100L,50);
    }

    @Test
    public void TestGetUserActiveByCategory(){
        this.CreateData(2L,50L,50);
        HashSet<Long> set = storage.GetUserActiveByProduct(1L);
        logger.info(set.toString());
    }

    @Test
    public void TestGetLastestAvtiveUsers(){

        this.CreateData(30L,10L,50);

        List<Long> lst = storage.GetLastestAvtiveUsers(30);

        logger.info(lst.toString());

        TruncateDB();

        this.CreateData(10L,10L,50);

        lst = storage.GetLastestAvtiveUsers(20);
        logger.info(lst.toString());

    }

    private void ConstructSimilarUsers(Long userStart,Long userEnd){

    }

    @Test
    public void TestUserSimilarity(){

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

    @After
    public void finale(){


    }



}
