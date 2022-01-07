package com.freeb.Dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class OrderInfoStorage {

    private static final Logger logger = LoggerFactory.getLogger(OrderInfoStorage.class);

    static String ORDER_CACHE_URL ;
    static String ORDER_USER;
    static String ORDER_PSW;

    public OrderInfoStorage(){
        try(Connection conn = DriverManager.getConnection(ORDER_CACHE_URL,ORDER_USER,ORDER_PSW)){
            logger.info("DB connected!");
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
        }
    }

    public OrderInfoStorage(String url, String name, String psw){
        ORDER_CACHE_URL=url;
        ORDER_USER=name;
        ORDER_PSW=psw;

        try(Connection conn = DriverManager.getConnection(ORDER_CACHE_URL,ORDER_USER,ORDER_PSW)){
            logger.info("DB connected!");
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
        }
    }

    static final String ORDER_FULL_QUERY_BY_ORDERID ="SELECT order_id, account_id, payment_status, merchant_id,merchant_name,obj_id,obj_name,payment_id FROM ORDER_INFOS WHERE order_id = ?";
    static final String ORDER_FULL_QUERY_BY_ACCOUNTID ="SELECT order_id, account_id, payment_status, merchant_id,merchant_name,obj_id,obj_name,payment_id FROM ORDER_INFOS WHERE account_id = ?";
    static final String ORDER_FULL_QUERY_BY_MERCHANTID ="SELECT order_id, account_id, payment_status, merchant_id,merchant_name,obj_id,obj_name,payment_id FROM ORDER_INFOS WHERE merchant_id = ?";
    static final String ORDER_FULL_QUERY_BY_OBJID ="SELECT order_id, account_id, payment_status, merchant_id,merchant_name,obj_id,obj_name,payment_id FROM ORDER_INFOS WHERE obj_id = ?";
    static final String ORDER_FULL_QUERY_BY_PAYMENTID="SELECT order_id, account_id, payment_status, merchant_id,merchant_name,obj_id,obj_name,payment_id FROM ORDER_INFOS WHERE payment_id = ?";

    public ResultSet getOrderInfoByOrderId(Integer orderId) {
        ResultSet rs=null;
        try(Connection conn = DriverManager.getConnection(ORDER_CACHE_URL,ORDER_USER,ORDER_PSW)){
            PreparedStatement stmt = conn.prepareStatement(ORDER_FULL_QUERY_BY_ORDERID);
            stmt.setInt(1,orderId);
            rs = stmt.executeQuery();
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
            return null;
        }
        return rs;
    }
    public ResultSet getOrderInfoByAccountId(Integer orderId) {
        ResultSet rs=null;
        try(Connection conn = DriverManager.getConnection(ORDER_CACHE_URL,ORDER_USER,ORDER_PSW)){
            PreparedStatement stmt = conn.prepareStatement(ORDER_FULL_QUERY_BY_ACCOUNTID);
            stmt.setInt(1,orderId);
            rs = stmt.executeQuery();
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
            return null;
        }
        return rs;
    }
    public ResultSet getOrderInfoByMerchantId(Integer orderId) {
        ResultSet rs=null;
        try(Connection conn = DriverManager.getConnection(ORDER_CACHE_URL,ORDER_USER,ORDER_PSW)){
            PreparedStatement stmt = conn.prepareStatement(ORDER_FULL_QUERY_BY_MERCHANTID);
            stmt.setInt(1,orderId);
            rs = stmt.executeQuery();
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
            return null;
        }
        return rs;
    }
    public ResultSet getOrderInfoByObjId(Integer orderId) {
        ResultSet rs=null;
        try(Connection conn = DriverManager.getConnection(ORDER_CACHE_URL,ORDER_USER,ORDER_PSW)){
            PreparedStatement stmt = conn.prepareStatement(ORDER_FULL_QUERY_BY_OBJID);
            stmt.setInt(1,orderId);
            rs = stmt.executeQuery();
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
            return null;
        }
        return rs;
    }

    public ResultSet getOrderInfoByPaymentId(Integer paymentId) {
        ResultSet rs=null;
        try(Connection conn = DriverManager.getConnection(ORDER_CACHE_URL,ORDER_USER,ORDER_PSW)){
            PreparedStatement stmt = conn.prepareStatement(ORDER_FULL_QUERY_BY_PAYMENTID);
            stmt.setInt(1,paymentId);
            rs = stmt.executeQuery();
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
            return null;
        }
        return rs;
    }
}
