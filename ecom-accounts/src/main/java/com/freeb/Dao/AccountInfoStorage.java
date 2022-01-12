package com.freeb.Dao;

import org.json.simple.parser.JSONParser;
import com.freeb.Entity.AccountsInfo;
import com.freeb.Utils.MarshalUtil;

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
    private DruidUtil druidUtil;


    static String ACCOUNT_DB_URL;
    static String ACCOUNT_USER;
    static String ACCOUNT_PWD;

    public AccountInfoStorage(){
        logger.error("TODO@ unsupported initialization method / AccountInfoStorage");
        try(Connection conn = DriverManager.getConnection(ACCOUNT_DB_URL, ACCOUNT_USER, ACCOUNT_PWD)){
            logger.info("DB connected!");
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
        }
    }

    public AccountInfoStorage(String url, String name, String psw) throws ClassNotFoundException {
        ACCOUNT_DB_URL =url;
        ACCOUNT_USER =name;
        ACCOUNT_PWD =psw;

        try(Connection conn = druidUtil.GetConnection()){
            logger.info("DB connected!");
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
        }
        Class.forName("com.mysql.cj.jdbc.Driver");
        druidUtil=new DruidUtil(url,name,psw);
    }

    static final String GET_ACCOUNT_BY_ID ="SELECT user_id, user_name, user_pwd, user_description FROM ACCOUNT_INFO WHERE user_id = ?";
    static final String GET_ACCOUNT_BY_NAME ="SELECT user_id, user_name, user_pwd, user_description FROM ACCOUNT_INFO WHERE user_name = ?";

    static final String UPDATE_ACCOUNT_BY_ID ="UPDATE ACCOUNT_INFO SET user_name =?, user_pwd = ?, user_description = ? WHERE user_id = ?";
    static final String DELETE_ACCOUNT_BY_ID ="DELETE FROM ACCOUNT_INFO WHERE user_id = ?";
    static final String EXISTS_ACCOUNT_BY_ID ="SELECT user_id FROM ACCOUNT_INFO WHERE user_id = ?";
    static final String GET_ACCOUNT_TAG_BY_ID = "SELECT user_tag FROM ACCOUNT_INFO WHERE user_id=?";
    static final String SET_ACCOUNT_TAG_BY_ID = "UPDATE ACCOUNT_INFO SET user_tag = ? WHERE user_id=?";

    public AccountsInfo GetAccountInfoByName(String name){

        ResultSet rs=null;
        try(Connection conn = druidUtil.GetConnection()){
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
        try(Connection conn = druidUtil.GetConnection()){
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
        ResultSet rs;
        try(Connection conn = druidUtil.GetConnection()){
            PreparedStatement stmt = conn.prepareStatement(GET_ACCOUNT_TAG_BY_ID);
            stmt.setLong(1,id);
            rs = stmt.executeQuery();
            String jstr="";
            while (rs.next()){
                jstr = (String) rs.getObject(1);
                break;
            }
            if(jstr==null){
                logger.warn("unfilled user_tag uid = "+id);
                return null;
            }
            JSONArray jarr = (JSONArray) parser.parse(jstr);
            List<Integer> re = new ArrayList<>();
            for (Object jsonValue : jarr) {
                re.add( ((Long)jsonValue).intValue());
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
        try(Connection conn = druidUtil.GetConnection()){
            PreparedStatement stmt = conn.prepareStatement(SET_ACCOUNT_TAG_BY_ID);
            stmt.setString(1,jsonStr);
            stmt.setLong(2,id);
            return (!stmt.execute());
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();

        }
        return false;
    }

    public Boolean UpdateAccountInfoById(AccountsInfo info){

        try(Connection conn = druidUtil.GetConnection()){
            PreparedStatement stmt = conn.prepareStatement(UPDATE_ACCOUNT_BY_ID);
            stmt.setLong(4,info.getUserId());
            stmt.setString(1,info.getUserName());
            stmt.setString(2,info.getUserPasswd());
            stmt.setString(3,info.getUserDescription());
            return (stmt.executeUpdate()==1);
            // TODO true / false

        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
            return false;
        }

    }

    private static final String CREATE_USER = "INSERT INTO ACCOUNT_INFO (user_name,user_pwd,user_description) VALUES (?,?,?)";

    public Boolean CreateAccountInfo(AccountsInfo info){
        return CreateAccountInfo(info.getUserName(),info.getUserPasswd(),info.getUserDescription());
    }

    public Boolean CreateAccountInfo(String userName,String pwd,String description){
        try(Connection conn = druidUtil.GetConnection()){
            PreparedStatement stmt = conn.prepareStatement(CREATE_USER);
            stmt.setString(1,userName);
            stmt.setString(2,pwd);
            stmt.setString(3,description);
            int rs = stmt.executeUpdate();
            if(rs>0)return true;

        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
        }
        return false;
    }




    public Boolean DeleteAccountInfoById(Long id){
        try(Connection conn = druidUtil.GetConnection()){
            PreparedStatement stmt = conn.prepareStatement(DELETE_ACCOUNT_BY_ID);
            stmt.setLong(1,id);
            return (stmt.executeUpdate()==1);
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
            return false;
        }

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
