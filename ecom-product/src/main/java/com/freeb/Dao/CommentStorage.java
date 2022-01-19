package com.freeb.Dao;

import com.freeb.Entity.CommentInfo;
import com.freeb.Utils.MarshalUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class CommentStorage {
    private static final Logger logger = LoggerFactory.getLogger(CommentStorage.class);

    static String CMT_DB_URL;
    static String CMT_USER;
    static String CMT_PSW;

    private DruidUtil druidUtil;
    public CommentStorage(){
        if(CMT_DB_URL==null){
            synchronized (CommentStorage.class){
                if(CMT_DB_URL==null){
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
                    CMT_DB_URL = properties.getProperty("CMT_DB_URL");
                    CMT_USER = properties.getProperty("CMT_USER");
                    CMT_PSW = properties.getProperty("CMT_PSW");
                }
            }
        }
        druidUtil = new DruidUtil(CMT_DB_URL, CMT_USER,CMT_PSW );


        try(Connection conn =druidUtil.GetConnection()){
            logger.info("DB connected!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public CommentStorage(String url, String name, String pwd) throws ClassNotFoundException {
        CMT_DB_URL =url;
        CMT_USER =name;
        CMT_PSW =pwd;

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

    public static final String CREATE_COMMENT = "INSERT INTO COMMENT_INFO(user_id,prod_id,comment_detail,comment_media) VALUES(?,?,?,?)";
    public Boolean CreateComments(Long userId,Long prodId,String commentDetail,String commentMedia){
        try(Connection conn = druidUtil.GetConnection()){
            PreparedStatement stmt = conn.prepareStatement(CREATE_COMMENT);
            stmt.setLong(1,userId);
            stmt.setLong(2,prodId);
            stmt.setString(3,commentDetail);
            stmt.setString(4,commentMedia);
            int rs = stmt.executeUpdate();
            if(rs>0)return true;

        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
        }
        return false;
    }

    public static final String GET_COMMENTS = "SELECT comment_id, user_id, prod_id, comment_detail, comment_media FROM COMMENT_INFO WHERE prod_id = ? ORDER BY UPDATE_TIME DESC";

    public List<CommentInfo> GetComments(Long prodId, Integer comtNum){

        try(Connection conn = druidUtil.GetConnection()){
            PreparedStatement stmt = conn.prepareStatement(CREATE_COMMENT);
            stmt.setLong(1,prodId);
            stmt.setInt(2,comtNum);
            ResultSet rs = stmt.executeQuery();
            return MarshalUtil.convertRs2CommentLists(rs);
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
        }
        return null;
    }


}
