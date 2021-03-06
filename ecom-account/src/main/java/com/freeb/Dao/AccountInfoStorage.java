package com.freeb.Dao;

import com.freeb.Enum.IdType;
import org.json.simple.parser.JSONParser;
import com.freeb.Entity.AccountInfo;
import com.freeb.Utils.MarshalUtil;

import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AccountInfoStorage {
    private static final Logger logger = LoggerFactory.getLogger(AccountInfoStorage.class);
    private JSONParser parser = new JSONParser();
    private DruidUtil druidUtil;


    static volatile String ACCOUNT_DB_URL;
    static volatile String ACCOUNT_USER;
    static volatile String ACCOUNT_PSW;

    public AccountInfoStorage(){
        if(ACCOUNT_DB_URL==null){
            synchronized (AccountInfoStorage.class){
                if(ACCOUNT_DB_URL==null){
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
                    ACCOUNT_DB_URL = properties.getProperty("ACCOUNT_DB_URL");
                    ACCOUNT_USER = properties.getProperty("ACCOUNT_USER");
                    ACCOUNT_PSW = properties.getProperty("ACCOUNT_PSW");
                }
            }
        }
        druidUtil = new DruidUtil(ACCOUNT_DB_URL, ACCOUNT_USER,ACCOUNT_PSW );


        try(Connection conn =druidUtil.GetConnection()){
            logger.info("DB connected!");
        } catch (SQLException e) {
            logger.error(String.format("DB connect failure %s",e.toString()));
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public AccountInfoStorage(String url, String name, String psw) throws ClassNotFoundException {
        ACCOUNT_DB_URL =url;
        ACCOUNT_USER =name;
        ACCOUNT_PSW =psw;
        Class.forName("com.mysql.cj.jdbc.Driver");
        druidUtil=new DruidUtil(url,name,psw);
        try(Connection conn = druidUtil.GetConnection()){
            logger.info("DB connected!");
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
        }

    }

    static final String GET_ACCOUNT_BY_ID ="SELECT user_id, user_name, user_pwd, user_card, user_description FROM ACCOUNT_INFO WHERE user_id = ?";
    static final String GET_ACCOUNT_BY_NAME ="SELECT user_id, user_name, user_pwd, user_card, user_description FROM ACCOUNT_INFO WHERE user_name = ?";
    static final String GET_ACCOUNT_ID_BY_IDTYPE ="SELECT user_id FROM ACCOUNT_INFO WHERE ";

    static final String UPDATE_ACCOUNT_BY_ID ="UPDATE ACCOUNT_INFO SET user_name =?, user_pwd = ?, user_description = ? WHERE user_id = ?";
    static final String DELETE_ACCOUNT_BY_ID ="DELETE FROM ACCOUNT_INFO WHERE user_id = ?";
    static final String EXISTS_ACCOUNT_BY_ID ="SELECT user_id FROM ACCOUNT_INFO WHERE user_id = ?";
    static final String GET_ACCOUNT_TAG_BY_ID = "SELECT user_tag FROM ACCOUNT_INFO WHERE user_id=?";
    static final String SET_ACCOUNT_TAG_BY_ID = "UPDATE ACCOUNT_INFO SET user_tag = ? WHERE user_id=?";



    public AccountInfo GetAccountInfoByName(String name){

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
    public AccountInfo GetAccountInfoById(Long id){

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
            if (rs.next()){
                jstr = (String) rs.getObject(1);
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

    public Boolean UpdateAccountInfoById(AccountInfo info){

        try(Connection conn = druidUtil.GetConnection()){
            PreparedStatement stmt = conn.prepareStatement(UPDATE_ACCOUNT_BY_ID);
            stmt.setLong(4,info.getUserId());
            stmt.setString(1,info.getUserName());
            stmt.setString(2,info.getUserPasswd());
            stmt.setString(3,info.getUserDescription());
            return (stmt.executeUpdate()==1);
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
            return false;
        }

    }

    private static final String CREATE_USER = "INSERT INTO ACCOUNT_INFO (user_name,user_pwd,user_card,user_description) VALUES (?,?,?,?)";

    public Boolean CreateAccountInfo(AccountInfo info){
        return CreateAccountInfo(info.getUserName(),info.getUserPasswd(),info.getUserDescription());
    }

    public Boolean CreateAccountInfo(String userName,String pwd,String description){
        try(Connection conn = druidUtil.GetConnection()){
            PreparedStatement stmt = conn.prepareStatement(CREATE_USER);
            stmt.setString(1,userName);
            stmt.setString(2,pwd);
            stmt.setString(3,"TODO@low USER CARD");
            stmt.setString(4,description);
            int rs = stmt.executeUpdate();
            if(rs>0)return true;

        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
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
        try(Connection conn = druidUtil.GetConnection()){
            PreparedStatement stmt = conn.prepareStatement(EXISTS_ACCOUNT_BY_ID);
            stmt.setInt(1,id);
            rs = stmt.executeQuery();
            if(rs.getFetchSize()==1){
                return true;
            }
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            e.printStackTrace();
            return false;
        }
        return false;
    }
}
