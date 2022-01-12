package com.freeb.Dao;

import com.freeb.Entity.OrderInfo;
import com.freeb.Utils.MarshalUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class OrderInfoStorage {

    private static final Logger logger = LoggerFactory.getLogger(OrderInfoStorage.class);
    private DruidUtil druidUtil;
    static String ORDER_DB_URL ="jdbc:mysql://sh-cynosdbmysql-grp-fdsb56no.sql.tencentcdb.com:24262/FREEB";
    static String ORDER_USER="root";
    static String ORDER_PSW= "1204Adzq";


    public OrderInfoStorage(){
        druidUtil = new DruidUtil(ORDER_DB_URL,ORDER_USER,ORDER_PSW);
        try(Connection conn = druidUtil.GetConnection()){
            logger.info("DB connected!");

        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
        }

    }

    public OrderInfoStorage(String url, String name, String psw){
        ORDER_DB_URL =url;
        ORDER_USER=name;
        ORDER_PSW=psw;
        druidUtil = new DruidUtil(ORDER_DB_URL,ORDER_USER,ORDER_PSW);
        try(Connection conn = druidUtil.GetConnection()){
            logger.info("DB connected!");
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
        }
    }

    static final String ORDER_FULL_QUERY_BY_ORDERID ="SELECT order_id, user_id, payment_status, merchant_id,merchant_name,prod_id,prod_name,payment_id FROM ORDER_INFO WHERE order_id = ?";
    static final String ORDER_FULL_QUERY_BY_ACCOUNTID ="SELECT order_id, user_id, payment_status, merchant_id,merchant_name,prod_id,prod_name,payment_id FROM ORDER_INFO WHERE user_id = ?";
    static final String ORDER_FULL_QUERY_BY_MERCHANTID ="SELECT order_id, user_id, payment_status, merchant_id,merchant_name,prod_id,prod_name,payment_id FROM ORDER_INFO WHERE merchant_id = ?";
    static final String ORDER_FULL_QUERY_BY_OBJID ="SELECT order_id, user_id, payment_status, merchant_id,merchant_name,prod_id,prod_name,payment_id FROM ORDER_INFO WHERE prod_id = ?";
    static final String ORDER_FULL_QUERY_BY_PAYMENTID="SELECT order_id, user_id, payment_status, merchant_id,merchant_name,prod_id,prod_name,payment_id FROM ORDER_INFO WHERE payment_id = ?";
    static final String UPDATE_PAYMENTID_BY_ORDERID = "UPDATE ORDER_INFO SET payment_id = ? WHERE order_id=?";
    public OrderInfo getOrderInfoByOrderId(long orderId) {
        ResultSet rs=null;
        try(Connection conn = druidUtil.GetConnection()){
            PreparedStatement stmt = conn.prepareStatement(ORDER_FULL_QUERY_BY_ORDERID);
            stmt.setLong(1,orderId);
            rs = stmt.executeQuery();
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
            return null;
        }
        return MarshalUtil.convertRs2OrderInfo(rs);
    }
    public OrderInfo getOrderInfoByAccountId(long orderId) {
        ResultSet rs=null;
        try(Connection conn = druidUtil.GetConnection()){
            PreparedStatement stmt = conn.prepareStatement(ORDER_FULL_QUERY_BY_ACCOUNTID);
            stmt.setLong(1,orderId);
            rs = stmt.executeQuery();
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
            return null;
        }
        return MarshalUtil.convertRs2OrderInfo(rs);
    }
    public OrderInfo getOrderInfoByMerchantId(long orderId) {
        ResultSet rs=null;
        try(Connection conn = druidUtil.GetConnection()){
            PreparedStatement stmt = conn.prepareStatement(ORDER_FULL_QUERY_BY_MERCHANTID);
            stmt.setLong(1,orderId);
            rs = stmt.executeQuery();
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
            return null;
        }
        return MarshalUtil.convertRs2OrderInfo(rs);
    }
    public OrderInfo getOrderInfoByObjId(long orderId) {
        ResultSet rs=null;
        try(Connection conn = druidUtil.GetConnection()){
            PreparedStatement stmt = conn.prepareStatement(ORDER_FULL_QUERY_BY_OBJID);
            stmt.setLong(1,orderId);
            rs = stmt.executeQuery();
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
            return null;
        }
        return MarshalUtil.convertRs2OrderInfo(rs);
    }

    public OrderInfo getOrderInfoByPaymentId(long paymentId) {
        ResultSet rs=null;
        try(Connection conn = druidUtil.GetConnection()){
            PreparedStatement stmt = conn.prepareStatement(ORDER_FULL_QUERY_BY_PAYMENTID);
            stmt.setLong(1,paymentId);
            rs = stmt.executeQuery();
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
            return null;
        }
        return MarshalUtil.convertRs2OrderInfo(rs);
    }

    public Boolean UpdatePaymentIdByOrderId(Long orderId) {

        try(Connection conn = druidUtil.GetConnection()){
            PreparedStatement stmt = conn.prepareStatement(UPDATE_PAYMENTID_BY_ORDERID);
            stmt.setLong(1,orderId);
            return (1== stmt.executeUpdate());
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
            return false;
        }
    }
}
