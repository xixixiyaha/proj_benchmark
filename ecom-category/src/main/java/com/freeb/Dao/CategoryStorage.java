package com.freeb.Dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CategoryStorage {
    private static final Logger logger = LoggerFactory.getLogger(CategoryStorage.class);

    static String CATE_DB_URL;
    static String CATE_USER;
    static String CATE_PWD;

    private DruidUtil druidUtil;

    public CategoryStorage(String url, String name, String pwd) throws ClassNotFoundException {
        CATE_DB_URL =url;
        CATE_USER =name;
        CATE_PWD =pwd;

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
