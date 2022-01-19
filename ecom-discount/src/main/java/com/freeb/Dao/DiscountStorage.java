package com.freeb.Dao;

import com.freeb.Entity.DiscountInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.*;
import java.util.Properties;

public class DiscountStorage {
    private static final Logger logger = LoggerFactory.getLogger(DiscountStorage.class.getName());
    //TODO druid static or not & 配置属性 & Druid 内存泄漏
    DruidUtil druidUtil;
    static volatile String DSCT_DB_URL;
    static volatile String DSCT_USER;
    static volatile String DSCT_PSW;

    public DiscountStorage(){
        if(DSCT_DB_URL==null){
            synchronized (DiscountStorage.class){
                if(DSCT_DB_URL==null){
                    Properties properties = new Properties();
                    // 使用ClassLoader加载properties配置文件生成对应的输入流
                    BufferedReader in = null;
                    try {
                        in = new BufferedReader(new FileReader("./proj_benchmark.properties"));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    // 使用properties对象加载输入流
                    try {
                        assert in != null;
                        properties.load(in);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    DSCT_DB_URL = properties.getProperty("DSCT_DB_URL");
                    DSCT_USER = properties.getProperty("DSCT_USER");
                    DSCT_PSW = properties.getProperty("DSCT_PSW");
                }
            }
        }
        druidUtil = new DruidUtil(DSCT_DB_URL, DSCT_USER,DSCT_PSW );


        try(Connection conn =druidUtil.GetConnection()){
            logger.info("DB connected!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static final String CREATE_DISCOUNT = "INSERT INTO DISCOUNT_INFOS(discount_type,prod_id,discount_val) VALUES(?,?,?)";
    public Boolean CreateDiscountInfo(Integer type,Long prodId,Double price){

        try(Connection conn = druidUtil.GetConnection()){
            PreparedStatement stmt = conn.prepareStatement(CREATE_DISCOUNT);
            stmt.setInt(1,type);
            stmt.setLong(2,prodId);
            stmt.setDouble(3,price);
            int rs = stmt.executeUpdate();
            if(rs>0)return true;

        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
        }
        return false;
    }

    private static final String GET_DISCOUNTS = "SELECT discount_id,discount_type,prod_id,discount_val FROM DISCOUNT_INFOS WHERE prod_id = ? and discount_type = ?";

    public DiscountInfo GetDiscountInfo(Long prodId, Integer type){
        try(Connection conn = druidUtil.GetConnection()){

            PreparedStatement stmt = conn.prepareStatement(GET_DISCOUNTS);
            stmt.setLong(1,prodId);
            stmt.setInt(2,type);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return new DiscountInfo(rs.getLong(1),rs.getInt(2),rs.getLong(3),rs.getDouble(4));
            }

        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
        }
        logger.warn("GetDiscountInfo failed ");
        return null;
    }

//    public static void main(String[] args){
//        File directory = new File("");
//        System.out.println("path = "+directory.getAbsolutePath()); //path = D:\0_4th\proj_benchmark
//
//        Properties properties = new Properties();
//        // 使用ClassLoader加载properties配置文件生成对应的输入流
//        BufferedReader in = null;
//        try {
//            in = new BufferedReader(new FileReader("./proj_benchmark.properties"));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        // 使用properties对象加载输入流
//        try {
//            properties.load(in);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        //获取key对应的value值
////        System.out.println(properties.getProperty("DSCT_DB_URL"));
//        System.out.println("jdbc:mysql://sh-cynosdbmysql-grp-fdsb56no.sql.tencentcdb.com:24262/FREEB");
//
//        try(Connection conn = DriverManager.getConnection(properties.getProperty("DSCT_DB_URL"), properties.getProperty("DSCT_USER"), properties.getProperty("DSCT_PSW"))){
////            logger.info("DB connected!");
//            System.out.println("DB connected!");
//        }catch (SQLException e){
////            logger.error(String.format("DB connect failure %s",e.toString()));
//            // Notice here
//            e.printStackTrace();
//        }
//    }


}
