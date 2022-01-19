package com.freeb.Dao;

import com.freeb.Entity.CartInfo;
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

public class CartInfoStorage {
    private static final Logger logger = LoggerFactory.getLogger(CartInfoStorage.class);
    private DruidUtil druidUtil;
    static String CART_DB_URL;
    static String CART_USER;
    static String CART_PSW;

    public CartInfoStorage(){
        if(CART_DB_URL==null){
            synchronized (CartInfo.class){
                if(CART_DB_URL==null){
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
                    CART_DB_URL = properties.getProperty("CART_DB_URL");
                    CART_USER = properties.getProperty("CART_USER");
                    CART_PSW = properties.getProperty("CART_PSW");
                }
            }
        }
        druidUtil = new DruidUtil(CART_DB_URL, CART_USER,CART_PSW );


        try(Connection conn =druidUtil.GetConnection()){
            logger.info("DB connected!");
        } catch (SQLException e) {
            logger.error(String.format("DB connect failure %s",e.toString()));
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public CartInfoStorage(String url, String name, String psw){
        CART_DB_URL =url;
        CART_USER =name;
        CART_PSW =psw;

        druidUtil = new DruidUtil(CART_DB_URL, CART_USER,CART_PSW );


        try(Connection conn =druidUtil.GetConnection()){
            logger.info("DB connected!");
        } catch (SQLException e) {
            logger.error(String.format("DB connect failure %s",e.toString()));
            e.printStackTrace();
            System.exit(-1);
        }
    }

    static final String GET_CART_BY_ACCOUNT ="SELECT cart_id, user_id,prod_id,merchant_id,incart_quantity,incart_select FROM CART_INFOS WHERE user_id = ?";
    static final String GET_LIMIT_CART_BY_ACCOUNT ="SELECT cart_id, user_id,prod_id,merchant_id,incart_quantity,incart_select FROM CART_INFOS WHERE user_id = ? ORDER BY update_time DESC LIMIT ?";

    static final String GET_OBJ_QUANTITY_BY_ACCOUNT_OBJ ="SELECT cart_id,incart_quantity FROM CART_INFOS WHERE user_id = ? AND obj_id = ?";
    static final String UPDATE_OBJ_QUANTITY_BY_ID ="UPDATE CART_INFOS SET incart_quantity = ? WHERE cart_id=?";
    static final String UPDATE_OBJ_SELECT_BY_ID ="UPDATE CART_INFOS SET incart_select = ? WHERE cart_id=?";
    static final String UPDATE_OBJ_SELECT_BY_ACCOUNT_OBJ ="UPDATE CART_INFOS SET incart_select = ? WHERE  user_id = ? AND obj_id = ?";

    static final String DELETE_CART_OBJ_BY_ID ="DELETE FROM CART_INFOS WHERE cart_id = ?";
    static final String DELETE_CART_BY_ACCOUNT ="DELETE FROM CART_INFOS WHERE user_id = ?";
    static final String ADD_CART_OBJ="INSERT INTO CART_INFOS (user_id,obj_id,merchant_id,incart_quantity,incart_select) VALUES(?,?,?,?,?)";



    public List<CartInfo> GetCartInfosByAccount(Long aId){

        ResultSet rs=null;
        try(Connection conn = druidUtil.GetConnection()){
            PreparedStatement stmt = conn.prepareStatement(GET_CART_BY_ACCOUNT);
            stmt.setLong(1,aId);
            rs = stmt.executeQuery();
            return MarshalUtil.convertRs2CartList(rs);
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
            return null;
        }
    }

    public List<CartInfo> GetCartInfosByAccount(Long aId,Integer limit){

        ResultSet rs=null;
        try(Connection conn = druidUtil.GetConnection()){
            PreparedStatement stmt = conn.prepareStatement(GET_LIMIT_CART_BY_ACCOUNT);
            stmt.setLong(1,aId);
            stmt.setInt(2,limit);
            rs = stmt.executeQuery();
            return MarshalUtil.convertRs2CartList(rs);
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
            return null;
        }
    }


}
