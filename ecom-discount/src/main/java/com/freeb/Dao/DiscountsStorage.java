package com.freeb.Dao;

import com.freeb.Entity.DiscountInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DiscountsStorage {
    private static final Logger logger = LoggerFactory.getLogger(DiscountsStorage.class.getName());

    DruidUtil druidUtil;

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


}
