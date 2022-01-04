package com.test;

import com.freeb.Dao.ProductInfoStorage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class TestProductDao {

    private static final Logger logger = LoggerFactory.getLogger(TestProductDao.class);

    static String PROD_DB_URL = "jdbc:mysql://sh-cynosdbmysql-grp-fdsb56no.sql.tencentcdb.com:24262/FREEB";
    static String PROD_USER ="root";
    static String PROD_PWD = "1204Adzq";

    private ProductInfoStorage storage;

    private HashMap<Integer,Integer> prod2Category;
    @Before
    public void Init() throws ClassNotFoundException {
        storage = new ProductInfoStorage(PROD_DB_URL,PROD_USER,PROD_PWD);
        prod2Category = new HashMap<>();
    }

    @Test
    public void TestConnection(){
        assert storage.TestConn();
    }

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

    private void loadProd2Category(String filepath) throws IOException {
        File file = new File(filepath);
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while (reader.ready()){
                String line = reader.readLine();
                String[] splitline = line.split(",");
                int dim = splitline.length;
                assert dim==2;
                this.prod2Category.put(Integer.valueOf(splitline[0]),Integer.valueOf(splitline[1]));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void writeProd2Category(String filepath){
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filepath)));
            for(Map.Entry<Integer,Integer> entry:this.prod2Category.entrySet()){
                writer.write(entry.getKey());
                writer.write(",");
                writer.write(entry.getValue());
                writer.newLine();
            }
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void createProduct(int prodNum){
        Random rand =new Random(25);
        Integer prodSales,categoryId;
        Integer upperProdNum = prodNum/1000;
        Integer cnter =0;
        Double prodPrice;
        String prodName = "pdname:";

        for(int merchantId=0;merchantId<1000;merchantId++){
            for(int i=0;i<upperProdNum;i++){
                prodPrice = rand.nextInt(1000)*1.0 / 10.0;
                prodSales = rand.nextInt(10000);
                categoryId = rand.nextInt(50);
                 Boolean re = storage.CreateProductInfo(prodName+cnter,categoryId,prodPrice,prodSales,(long)0,(long)merchantId);
                 if(!re){
                     logger.error("insert failed round"+merchantId+" "+i);                                                                                     
                 }
                 this.prod2Category.put(cnter,categoryId);
                 cnter++;
            }
        }
        writeProd2Category(".//prod2category.csv");
    }

    @Test
    public void CreateProductInfo(){
        createProduct(100000);
    }

    @After
    public void finale(){
//        TruncateDB();

    }



}
