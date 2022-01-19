package com.freeb.Dao;

import com.freeb.Entity.DiscountInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.*;

public class DiscountStorage {
    private static final Logger logger = LoggerFactory.getLogger(DiscountStorage.class.getName());
    //TODO druid属性
    DruidUtil druidUtil;
    static String DSCT_DB_URL;
    static String DSCT_USER;
    static String DSCT_PSW;

    public DiscountStorage(){

        try(Connection conn = DriverManager.getConnection(DSCT_DB_URL, DSCT_USER, DSCT_PSW)){
            logger.info("DB connected!");
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
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

    public static void main(String[] args){
        File directory = new File("");
        System.out.println("path = "+directory.getAbsolutePath()); //path = D:\0_4th\proj_benchmark
    }


}
