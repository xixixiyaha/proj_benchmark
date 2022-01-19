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

public class CategoryStorage {
    private static final Logger logger = LoggerFactory.getLogger(CategoryStorage.class);

    static String CATE_DB_URL;
    static String CATE_USER;
    static String CATE_PSW;

    private DruidUtil druidUtil;

    public CategoryStorage(){
        if(CATE_DB_URL==null){
            synchronized (CategoryStorage.class){
                if(CATE_DB_URL==null){
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
                    CATE_DB_URL = properties.getProperty("CATE_DB_URL");
                    CATE_USER = properties.getProperty("CATE_USER");
                    CATE_PSW = properties.getProperty("CATE_PSW");
                }
            }
        }
        druidUtil = new DruidUtil(CATE_DB_URL, CATE_USER,CATE_PSW );


        try(Connection conn =druidUtil.GetConnection()){
            logger.info("DB connected!");
        } catch (SQLException e) {
            logger.error(String.format("DB connect failure %s",e.toString()));
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public CategoryStorage(String url, String name, String pwd) throws ClassNotFoundException {
        CATE_DB_URL =url;
        CATE_USER =name;
        CATE_PSW =pwd;

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

    static final String GET_CATE_BY_ID ="SELECT category_id, category_description FROM CATEGORY_INFOS WHERE category_id = ?";
    static final String CREATE_CATE="INSERT INTO CATEGORY_INFO (category_description) VALUES(?)";

    public Boolean CreateCategoryInfo(String description){
        try(Connection conn = druidUtil.GetConnection()){
            PreparedStatement stmt = conn.prepareStatement(CREATE_CATE);
            stmt.setString(1,description);
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
