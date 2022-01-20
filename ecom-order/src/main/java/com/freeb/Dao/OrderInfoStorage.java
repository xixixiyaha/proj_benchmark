package com.freeb.Dao;

import com.freeb.Entity.OrderInfo;
import com.freeb.Service.OrderService;
import com.freeb.Utils.MarshalUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.Properties;

public class OrderInfoStorage {

    private static final Logger logger = LoggerFactory.getLogger(OrderInfoStorage.class);
    private DruidUtil druidUtil;
    static String ORDER_DB_URL;
    static String ORDER_USER;
    static String ORDER_PSW;


    public OrderInfoStorage(){
        if(ORDER_DB_URL==null){
            synchronized (OrderInfoStorage.class){
                if(ORDER_DB_URL==null){
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
                    ORDER_DB_URL = properties.getProperty("ORDER_DB_URL");
                    ORDER_USER = properties.getProperty("ORDER_USER");
                    ORDER_PSW = properties.getProperty("ORDER_PSW");
                }
            }
        }
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

    static final String CREATE_ORDER_INFO = "INSERT INTO ORDER_INFO(user_id,merchant_id,merchant_name,prod_id,prod_name,cart_id) VALUES(?,?,?,?,?,?)";
    public List<OrderInfo> CreateOrderInfoByCartInfo(long user_id,long merchant_id,String merchant_name,long prod_id,String prod_name,long cart_id) {
        ResultSet rs=null;
        //TODO 获取结果OrderInfo
        try(Connection conn = druidUtil.GetConnection()){
            PreparedStatement stmt = conn.prepareStatement(CREATE_ORDER_INFO);
            stmt.setLong(1,user_id);
            stmt.setLong(2,merchant_id);
            stmt.setString(3,merchant_name);
            stmt.setLong(4,prod_id);
            stmt.setString(5,prod_name);
            stmt.setLong(6,cart_id);
            rs = stmt.executeQuery();
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
            return null;
        }
        return MarshalUtil.convertRs2OrderList(rs);
    }

    static final String ORDER_FULL_QUERY_BY_ORDERID ="SELECT order_id, user_id, payment_status, merchant_id,merchant_name,prod_id,prod_name,payment_id FROM ORDER_INFO WHERE order_id = ?";
    static final String ORDER_FULL_QUERY_BY_ACCOUNTID ="SELECT order_id, user_id, payment_status, merchant_id,merchant_name,prod_id,prod_name,payment_id FROM ORDER_INFO WHERE user_id = ?";
    static final String ORDER_FULL_QUERY_BY_MERCHANTID ="SELECT order_id, user_id, payment_status, merchant_id,merchant_name,prod_id,prod_name,payment_id FROM ORDER_INFO WHERE merchant_id = ?";
    static final String ORDER_FULL_QUERY_BY_OBJID ="SELECT order_id, user_id, payment_status, merchant_id,merchant_name,prod_id,prod_name,payment_id FROM ORDER_INFO WHERE prod_id = ?";
    static final String ORDER_FULL_QUERY_BY_PAYMENTID="SELECT order_id, user_id, payment_status, merchant_id,merchant_name,prod_id,prod_name,payment_id FROM ORDER_INFO WHERE payment_id = ?";
    static final String UPDATE_PAYMENTID_BY_ORDERID = "UPDATE ORDER_INFO SET payment_id = ? WHERE order_id=?";

    public List<OrderInfo> getOrderInfoByOrderId(long orderId) {
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
        return MarshalUtil.convertRs2OrderList(rs);
    }
    public List<OrderInfo> getOrderInfoByAccountId(long orderId) {
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
        return MarshalUtil.convertRs2OrderList(rs);
    }
    public List<OrderInfo> getOrderInfoByMerchantId(long orderId) {
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
        return MarshalUtil.convertRs2OrderList(rs);
    }
    public List<OrderInfo> getOrderInfoByObjId(long orderId) {
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
        return MarshalUtil.convertRs2OrderList(rs);
    }

    public List<OrderInfo> getOrderInfoByPaymentId(long paymentId) {
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
        return MarshalUtil.convertRs2OrderList(rs);
    }

    public Boolean UpdatePaymentIdByOrderId(Long orderId,Long paymentId) {

        try(Connection conn = druidUtil.GetConnection()){
            PreparedStatement stmt = conn.prepareStatement(UPDATE_PAYMENTID_BY_ORDERID);
            stmt.setLong(1,paymentId);
            stmt.setLong(2,orderId);
            return (1== stmt.executeUpdate());
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
            return false;
        }
    }
}
