package com.freeb.Dao;

import com.freeb.Entity.CommentInfo;
import com.freeb.Utils.MarshalUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CommentStorage {
    private static final Logger logger = LoggerFactory.getLogger(CategoryStorage.class);

    static String CMT_DB_URL;
    static String CMT_USER;
    static String CMT_PSW;

    private DruidUtil druidUtil;

    public CommentStorage(String url, String name, String pwd) throws ClassNotFoundException {
        CMT_DB_URL =url;
        CMT_DB_URL =name;
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
