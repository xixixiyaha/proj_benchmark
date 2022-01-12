package com.freeb.Dao;

import java.sql.*;

import com.freeb.Entity.PaymentInfo;
import com.freeb.Utils.MarshalUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaymentInfoStorage {

    private static final Logger logger = LoggerFactory.getLogger(PaymentInfoStorage.class);
    private     DruidUtil druidUtil;


    public PaymentInfoStorage(String url, String name, String psw) throws ClassNotFoundException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        druidUtil=new DruidUtil(url,name,psw);
        try(Connection conn = druidUtil.GetConnection()){
            logger.info("DB connected!");
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
        }
    }

    static final String GET_PAYMENT_STATUS_BY_PAYMENTID = "SELECT payment_status FROM PAYMENT_INFO WHERE payment_id = ?";
    static final String CREATE_PAYMENT="INSERT INTO PAYMENT_INFO (payment_status ,payment_val, discounts_val,payment_card,user_id) VALUES(?,?,?,?,?)";
    static final String UPDATE_PAYMENT_STATUS_BY_PAYMENTID = "UPDATE PAYMENT_INFO SET payment_status =? WHERE payment_id = ?";
    static final String GET_PAYMENT_INFO_BY_PAYMENTID ="SELECT payment_id,payment_status,payment_val,discounts_val,payment_card,user_id FROM PAYMENT_INFO WHERE payment_id = ?";
    static final String GET_USERID_BY_PAYMENTID = "SELECT user_id FROM PAYMENT_INFO WHERE payment_id=?";

    public Long GetUserIdByPaymentId(Long pid){
        ResultSet rs=null;
        try(Connection conn = druidUtil.GetConnection()){
            PreparedStatement stmt = conn.prepareStatement(GET_USERID_BY_PAYMENTID);
            stmt.setLong(1,pid);
            rs = stmt.executeQuery();
            while(rs.next()){
                return rs.getLong(1);
            }
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
            return null;
        }
        return null;
    }



    public Integer GetPaymentStatusById(Long pid){
        ResultSet rs=null;
        try(Connection conn = druidUtil.GetConnection()){
            PreparedStatement stmt = conn.prepareStatement(GET_PAYMENT_STATUS_BY_PAYMENTID);
            stmt.setLong(1,pid);
            rs = stmt.executeQuery();
            while(rs.next()){
                return rs.getInt(1);
            }
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
            return -1;
        }
        return -1;
    }

    public Long CreatePaymentInfo(PaymentInfo info){
        ResultSet rs=null;
        try(Connection conn = druidUtil.GetConnection()){
            // 查询同时返回主键ID https://www.cnblogs.com/prodigal-son/p/13388276.html
            PreparedStatement stmt = conn.prepareStatement(CREATE_PAYMENT,Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1,info.getPaymentStatus());
            stmt.setDouble(2,info.getPaymentVal());
            stmt.setDouble(3,info.getDiscountsVal());
            stmt.setString(4,info.getPaymentCard());
            stmt.setLong(5,info.getUserId());
            stmt.executeUpdate();
            rs=stmt.getGeneratedKeys();
            if(rs.next()){
                return rs.getLong(1);
            }
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
            return -1L;
        }
        return -1L;
    }
    public Boolean UpdatePaymentStatusById(PaymentInfo info){

        try(Connection conn = druidUtil.GetConnection()){
            PreparedStatement stmt = conn.prepareStatement(UPDATE_PAYMENT_STATUS_BY_PAYMENTID);
            stmt.setInt(1,info.getPaymentStatus());
            stmt.setLong(2,info.getPaymentId());
            return (1==stmt.executeUpdate());
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
            return false;
        }
    }


    public PaymentInfo GetPaymentInfoById(Long pid){
        ResultSet rs=null;
        try(Connection conn = druidUtil.GetConnection()){
            PreparedStatement stmt = conn.prepareStatement(GET_PAYMENT_INFO_BY_PAYMENTID);
            stmt.setLong(1,pid);
            rs = stmt.executeQuery();
            return MarshalUtil.convertRs2Payment(rs);
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
            return null;
        }
    }

}
