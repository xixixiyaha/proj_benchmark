package com.freeb.Dao;

import java.sql.*;

import com.freeb.Entity.PaymentInfo;
import com.freeb.Utils.MarshalUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaymentInfoStorage {

    private static final Logger logger = LoggerFactory.getLogger(PaymentInfoStorage.class);

    static String PAYMENT_DB_URL;
    static String PAYMENT_USER;
    static String PAYMENT_PWD;

    public PaymentInfoStorage(){
        try(Connection conn = DriverManager.getConnection(PAYMENT_DB_URL,PAYMENT_USER, PAYMENT_PWD)){
            logger.info("DB connected!");
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
        }
    }

    public PaymentInfoStorage(String url, String name, String psw){
        PAYMENT_DB_URL =url;
        PAYMENT_USER=name;
        PAYMENT_PWD =psw;

        try(Connection conn = DriverManager.getConnection(PAYMENT_DB_URL,PAYMENT_USER, PAYMENT_PWD)){
            logger.info("DB connected!");
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
        }
    }

    static final String GET_PAYMENT_STATUS_BY_PAYMENTID = "SELECT payment_status FROM PAYMENT_INFOS WHERE payment_id = ?";
    static final String CREATE_PAYMENT="INSERT INTO PAYMENT_INFOS (payment_id，payment_status ,payment_val, discounts_val,payment_card,account_id) VALUES(?,?,?,?,?,?)";
    static final String UPDATE_PAYMENT_STATUS_BY_PAYMENTID = "UPDATE PAYMENT_INFOS SET payment_status =? ,payment_val = ?, discounts_val = ?,payment_card = ?,account_id =? WHERE payment_id = ?";
    static final String GET_PAYMENT_INFO_BY_PAYMENTID ="SELECT account_id，user_name ,user_pwd ,user_description FROM PAYMENT_INFOS WHERE payment_id = ?";

    public Integer GetPaymentStatusById(Long id){
        ResultSet rs=null;
        try(Connection conn = DriverManager.getConnection(PAYMENT_DB_URL, PAYMENT_USER, PAYMENT_PWD)){
            PreparedStatement stmt = conn.prepareStatement(GET_PAYMENT_STATUS_BY_PAYMENTID);
            stmt.setLong(1,id);
            rs = stmt.executeQuery();
            // TODO true / false

        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
            return -1;
        }
        return -1;
    }

    public Boolean CreatePaymentInfo(PaymentInfo info){
        ResultSet rs=null;
        try(Connection conn = DriverManager.getConnection(PAYMENT_DB_URL, PAYMENT_USER, PAYMENT_PWD)){
            PreparedStatement stmt = conn.prepareStatement(CREATE_PAYMENT);
            stmt.setLong(1,info.getPaymentId());
            stmt.setInt(2,info.getPaymentStatus());
            stmt.setDouble(3,info.getPaymentVal());
            stmt.setDouble(4,info.getDiscountsVal());
            stmt.setString(5,info.getPaymentCard());
            stmt.setInt(6,info.getAccountId());
            rs = stmt.executeQuery();
            // TODO true / false

        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
            return false;
        }
        return false;
    }
    public Boolean UpdatePaymentStatusById(PaymentInfo info){
        ResultSet rs=null;
        try(Connection conn = DriverManager.getConnection(PAYMENT_DB_URL, PAYMENT_USER, PAYMENT_PWD)){
            PreparedStatement stmt = conn.prepareStatement(UPDATE_PAYMENT_STATUS_BY_PAYMENTID);
            stmt.setInt(1,info.getPaymentStatus());
            stmt.setDouble(2,info.getPaymentVal());
            stmt.setDouble(3,info.getDiscountsVal());
            stmt.setString(4,info.getPaymentCard());
            stmt.setInt(5,info.getAccountId());
            rs = stmt.executeQuery();
            // TODO true / false

        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
            return false;
        }
        return false;
    }


    public PaymentInfo GetPaymentInfoById(Long id){
        ResultSet rs=null;
        try(Connection conn = DriverManager.getConnection(PAYMENT_DB_URL, PAYMENT_USER, PAYMENT_PWD)){
            PreparedStatement stmt = conn.prepareStatement(GET_PAYMENT_INFO_BY_PAYMENTID);
            stmt.setLong(1,id);
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
