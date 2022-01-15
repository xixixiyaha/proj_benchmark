package com.freeb.Dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MerchantStorage {
    private static final Logger logger = LoggerFactory.getLogger(MerchantStorage.class);

    static String MERCHANT_DB_URL;
    static String MERCHANT_USER;
    static String ERCHANT_PWD;

    DruidUtil druidUtil;

    public MerchantStorage(String url, String name, String pwd) throws ClassNotFoundException {
        MERCHANT_DB_URL =url;
        MERCHANT_USER =name;
        ERCHANT_PWD =pwd;

        Class.forName("com.mysql.cj.jdbc.Driver");
        druidUtil=new DruidUtil(url,name,pwd);
    }

    public Boolean TestConn(){
        try (Connection conn = druidUtil.GetConnection()) {
            return true;
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
            return false;
        }
    }

    static final String GET_MERCHANT_BY_ID ="SELECT merchant_id, merchant_name FROM MERCHANT_INFOS WHERE merchant_id = ?";
    static final String CREATE_MERTCHANT="INSERT INTO MERCHANT_INFOS (merchant_name) VALUES(?)";

    public Boolean CreateMerchantInfo(String mName){
        try(Connection conn = druidUtil.GetConnection()){
            PreparedStatement stmt = conn.prepareStatement(CREATE_MERTCHANT);
            stmt.setString(1,mName);
            int rs = stmt.executeUpdate();
            if(rs>0)return true;

        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
        }
        return false;
    }


}
