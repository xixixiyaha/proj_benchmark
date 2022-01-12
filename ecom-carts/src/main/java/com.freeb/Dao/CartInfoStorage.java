package com.freeb.Dao;

import com.freeb.Entity.CartInfo;
import com.freeb.Utils.MarshalUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.ShortLookupTable;
import java.sql.*;
import java.util.List;

public class CartInfoStorage {
    private static final Logger logger = LoggerFactory.getLogger(CartInfoStorage.class);

    static String CART_DB_URL;
    static String CART_USER;
    static String CART_PWD;

    public CartInfoStorage(){
        try(Connection conn = DriverManager.getConnection(CART_DB_URL, CART_USER, CART_PWD)){
            logger.info("DB connected!");
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
        }
    }

    public CartInfoStorage(String url, String name, String psw){
        CART_DB_URL =url;
        CART_USER =name;
        CART_PWD =psw;

        try(Connection conn = DriverManager.getConnection(CART_DB_URL, CART_USER, CART_PWD)){
            logger.info("DB connected!");
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
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
        try(Connection conn = DriverManager.getConnection(CART_DB_URL, CART_USER, CART_PWD)){
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
        try(Connection conn = DriverManager.getConnection(CART_DB_URL, CART_USER, CART_PWD)){
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
