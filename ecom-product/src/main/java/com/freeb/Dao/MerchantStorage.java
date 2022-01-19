package com.freeb.Dao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class MerchantStorage {
    private static final Logger logger = LoggerFactory.getLogger(MerchantStorage.class);

    static String MERCHANT_DB_URL;
    static String MERCHANT_USER;
    static String MERCHANT_PSW;

    DruidUtil druidUtil;

    public MerchantStorage(){
        if(MERCHANT_DB_URL==null){
            synchronized (MerchantStorage.class){
                if(MERCHANT_DB_URL==null){
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
                    MERCHANT_DB_URL = properties.getProperty("MERCHANT_DB_URL");
                    MERCHANT_USER = properties.getProperty("MERCHANT_USER");
                    MERCHANT_PSW = properties.getProperty("MERCHANT_PSW");
                }
            }
        }
        druidUtil = new DruidUtil(MERCHANT_DB_URL, MERCHANT_USER,MERCHANT_PSW );


        try(Connection conn =druidUtil.GetConnection()){
            logger.info("DB connected!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public MerchantStorage(String url, String name, String psw) throws ClassNotFoundException {
        MERCHANT_DB_URL =url;
        MERCHANT_USER =name;
        MERCHANT_PSW =psw;

        Class.forName("com.mysql.cj.jdbc.Driver");
        druidUtil=new DruidUtil(url,name,psw);
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
    static final String CREATE_MERTCHANT="INSERT INTO MERCHANT_INFO (merchant_name) VALUES(?)";

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
