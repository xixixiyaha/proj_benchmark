package com.freeb.Dao;

import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import com.freeb.Entity.AccountsInfo;
import com.freeb.Utils.MarshalUtil;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountInfoStorage {
    private static final Logger logger = LoggerFactory.getLogger(AccountInfoStorage.class);
    private JSONParser parser = new JSONParser();

    static String ACCOUNT_DB_URL;
    static String ACCOUNT_USER;
    static String ACCOUNT_PWD;

    public AccountInfoStorage(){
        try(Connection conn = DriverManager.getConnection(ACCOUNT_DB_URL, ACCOUNT_USER, ACCOUNT_PWD)){
            logger.info("DB connected!");
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
        }
    }

    public AccountInfoStorage(String url, String name, String psw){
        ACCOUNT_DB_URL =url;
        ACCOUNT_USER =name;
        ACCOUNT_PWD =psw;

        try(Connection conn = DriverManager.getConnection(ACCOUNT_DB_URL, ACCOUNT_USER, ACCOUNT_PWD)){
            logger.info("DB connected!");
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
        }
    }

    static final String GET_ACCOUNT_BY_ID ="SELECT user_id, user_name, user_pwd, user_description FROM ACCOUNT_INFO WHERE user_id = ?";
    static final String GET_ACCOUNT_BY_NAME ="SELECT user_id, user_name, user_pwd, user_description FROM ACCOUNT_INFO WHERE user_name = ?";

    static final String UPDATE_ACCOUNT_BY_ID ="UPDATE ACCOUNT_INFO SET user_name =?, user_pwd = ?, user_description = ? WHERE user_id = ?";
    static final String DELETE_ACCOUNT_BY_ID ="DELETE FROM ACCOUNT_INFO WHERE user_id = ?";
    static final String CREATE_ACCOUNT="INSERT INTO ACCOUNT_INFO (user_id, user_name, user_pwd, user_description) VALUES(?,?,?,?)";
    static final String EXISTS_ACCOUNT_BY_ID ="SELECT user_id FROM ACCOUNT_INFO WHERE user_id = ?";
    static final String GET_ACCOUNT_TAG_BY_INFO = "SELECT user_tag FROM ACCOUNT_INFO WHERE user_id=?";
    static final String SET_ACCOUNT_TAG_BY_INFO = "UPDATE ACCOUNT_INFO SET user_tag = ? WHERE user_id=?";

    public AccountsInfo GetAccountInfoByName(String name){

        ResultSet rs=null;
        try(Connection conn = DriverManager.getConnection(ACCOUNT_DB_URL, ACCOUNT_USER, ACCOUNT_PWD)){
            PreparedStatement stmt = conn.prepareStatement(GET_ACCOUNT_BY_NAME);
            stmt.setString(1,name);
            rs = stmt.executeQuery();
            return MarshalUtil.convertRs2Account(rs);
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
            return null;
        }
    }
    public AccountsInfo GetAccountInfoById(Long id){

        ResultSet rs=null;
        try(Connection conn = DriverManager.getConnection(ACCOUNT_DB_URL, ACCOUNT_USER, ACCOUNT_PWD)){
            PreparedStatement stmt = conn.prepareStatement(GET_ACCOUNT_BY_ID);
            stmt.setLong(1,id);
            rs = stmt.executeQuery();
            return MarshalUtil.convertRs2Account(rs);
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
            return null;
        }
    }

    public List<Integer> GetAccountTagById(Long id){
        ResultSet rs=null;
        try(Connection conn = DriverManager.getConnection(ACCOUNT_DB_URL, ACCOUNT_USER, ACCOUNT_PWD)){
            PreparedStatement stmt = conn.prepareStatement(GET_ACCOUNT_TAG_BY_INFO);
            stmt.setLong(1,id);
            rs = stmt.executeQuery();
            JSONObject jobj = (JSONObject) parser.parse(rs.getString(1));
            JSONArray jarr = (JSONArray) jobj.get("tag");
            int len = jarr.size();
            List<Integer> re = new ArrayList<>();
            for (Object jsonValue : jarr) {
                re.add((Integer) jsonValue);
            }
            return re;
        }catch (SQLException | ParseException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
            return null;
        }
    }
    public Boolean SetAccountTagById(Long id,String jsonStr){
        try(Connection conn = DriverManager.getConnection(ACCOUNT_DB_URL, ACCOUNT_USER, ACCOUNT_PWD)){
            PreparedStatement stmt = conn.prepareStatement(SET_ACCOUNT_TAG_BY_INFO);
            stmt.setString(1,jsonStr);
            stmt.setLong(2,id);
            return stmt.execute();
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
            return false;
        }
    }

    public Boolean UpdateAccountInfoById(AccountsInfo info){

        ResultSet rs=null;
        try(Connection conn = DriverManager.getConnection(ACCOUNT_DB_URL, ACCOUNT_USER, ACCOUNT_PWD)){
            PreparedStatement stmt = conn.prepareStatement(UPDATE_ACCOUNT_BY_ID);
            stmt.setLong(4,info.getUserId());
            stmt.setString(1,info.getUserName());
            stmt.setString(2,info.getUserPasswd());
            stmt.setString(3,info.getUserDescription());
            rs = stmt.executeQuery();
            // TODO true / false

        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
            return false;
        }
        return false;

    }


    public Boolean DeleteAccountInfoById(Integer id){
        ResultSet rs=null;
        try(Connection conn = DriverManager.getConnection(ACCOUNT_DB_URL, ACCOUNT_USER, ACCOUNT_PWD)){
            PreparedStatement stmt = conn.prepareStatement(DELETE_ACCOUNT_BY_ID);
            stmt.setInt(1,id);
            rs = stmt.executeQuery();
            // TODO true / false
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
            return false;
        }

        return false;
    }


    public Boolean CreateAccountInfo(AccountsInfo info){
        ResultSet rs=null;
        try(Connection conn = DriverManager.getConnection(ACCOUNT_DB_URL, ACCOUNT_USER, ACCOUNT_PWD)){
            PreparedStatement stmt = conn.prepareStatement(CREATE_ACCOUNT);
            stmt.setLong(1,info.getUserId());
            stmt.setString(2,info.getUserName());
            stmt.setString(3,info.getUserPasswd());
            stmt.setString(4,info.getUserDescription());
            rs = stmt.executeQuery();
            // TODO true / false

        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public Boolean ExistsAccountInfo(Integer id){
        ResultSet rs=null;
        try(Connection conn = DriverManager.getConnection(ACCOUNT_DB_URL, ACCOUNT_USER, ACCOUNT_PWD)){
            PreparedStatement stmt = conn.prepareStatement(EXISTS_ACCOUNT_BY_ID);
            stmt.setInt(1,id);
            rs = stmt.executeQuery();
            if(rs.getFetchSize()==1){
                return true;
            }
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
            return false;
        }
        return false;
    }



}
