package com.freeb.Dao;

import com.freeb.Entity.AccountsInfo;
import com.freeb.utils.MarshalUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class AccountsInfoStorage {
    private static final Logger logger = LoggerFactory.getLogger(AccountsInfoStorage.class);

    static String ACCOUNT_DB_URL;
    static String ACCOUNT_USER;
    static String ACCOUNT_PWD;

    public AccountsInfoStorage(){
        try(Connection conn = DriverManager.getConnection(ACCOUNT_DB_URL, ACCOUNT_USER, ACCOUNT_PWD)){
            logger.info("DB connected!");
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
        }
    }

    public AccountsInfoStorage(String url, String name, String psw){
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

    static final String GET_ACCOUNT_BY_ID ="SELECT account_id, user_name, user_pwd, user_description FROM ACCOUNT_INFOS WHERE account_id = ?";
    static final String UPDATE_ACCOUNT_BY_ID ="UPDATE ACCOUNT_INFOS SET user_name =?, user_pwd = ?, user_description = ? WHERE account_id = ?";
    static final String DELETE_ACCOUNT_BY_ID ="DELETE FROM ACCOUNT_INFOS WHERE account_id = ?";
    static final String CREATE_ACCOUNT="INSERT INTO ACCOUNT_INFOS (account_id, user_name, user_pwd, user_description) VALUES(?,?,?,?)";
    static final String EXISTS_ACCOUNT_BY_ID ="SELECT account_id FROM ACCOUNT_INFOS WHERE account_id = ?";


    public AccountsInfo GetAccountInfoById(Integer id){

        ResultSet rs=null;
        try(Connection conn = DriverManager.getConnection(ACCOUNT_DB_URL, ACCOUNT_USER, ACCOUNT_PWD)){
            PreparedStatement stmt = conn.prepareStatement(GET_ACCOUNT_BY_ID);
            stmt.setInt(1,id);
            rs = stmt.executeQuery();
            // TODO true / false
            return MarshalUtil.convertRs2Account(rs);
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
            return null;
        }
    }
    public Boolean UpdateAccountInfoById(AccountsInfo info){

        ResultSet rs=null;
        try(Connection conn = DriverManager.getConnection(ACCOUNT_DB_URL, ACCOUNT_USER, ACCOUNT_PWD)){
            PreparedStatement stmt = conn.prepareStatement(UPDATE_ACCOUNT_BY_ID);
            stmt.setInt(4,info.getAccountId());
            stmt.setString(1,info.getUserName());
            stmt.setString(2,info.getUserPwd());
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
            stmt.setInt(1,info.getAccountId());
            stmt.setString(2,info.getUserName());
            stmt.setString(3,info.getUserPwd());
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
