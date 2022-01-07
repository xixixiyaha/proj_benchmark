package Dao;

import Bean.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    // TODO 理解connection: 是一个数据库实例对应一个connection,并发由数据库控制; 还是一个connection对应一个并发
    Connection userConn = SqlConnection.getConnection();
    String getUserAll = "select * from user where uuid = ?";
    String addUserAll = "insert into user(uuid,apart_id,age,name,profile) values(?,?,?,?,?)";
    String updateUserAll = "update user set apart_id = ? and age = ? and name = ? and profile = ? where uuid = ?";
    String delUserAll = "delete from user where uuid = ?";

    public UserDao() throws Exception {
    }

    public User GetUser(int uuid) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            ps = userConn.prepareStatement(getUserAll);
            // first param is 1
            ps.setObject(1,uuid);
            rs = ps.executeQuery();

            assert rs.getFetchSize()<=1;
            User user;
            while (rs.next()){
                user = new User((long)rs.getObject(0),(int)rs.getObject(1),(int)rs.getObject(2),(String) rs.getObject(3),(String) rs.getObject(4));
                return user;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> GetUsers(List<Integer> uuids){
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();
        try{
            ps = userConn.prepareStatement(getUserAll);
            // first param is 1
            for (Integer uuid:uuids){
                ps.setObject(1,uuid);
                rs = ps.executeQuery();

                assert rs.getFetchSize()<=1;
                User user;
                while (rs.next()){
                    user = new User(rs.getLong("uuid"),(int)rs.getObject(1),(int)rs.getObject(2),(String) rs.getObject(3),(String) rs.getObject(4));
                    users.add(user);
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public Integer AddUser(User user) throws SQLException {
        PreparedStatement ps = userConn.prepareStatement(addUserAll);
        ps.setLong(1,user.getUuid());
        ps.setInt(2,user.getApartId());
        ps.setInt(3,user.getAge());
        ps.setString(4,user.getName());
        ps.setString(5,user.getProfile());

        ps.execute();
        return 1;
    }

    public Integer AddUsers(List<User> users) throws SQLException {
        PreparedStatement ps = userConn.prepareStatement(addUserAll);
        for(User user:users){
            ps.setLong(1,user.getUuid());
            ps.setInt(2,user.getApartId());
            ps.setInt(3,user.getAge());
            ps.setString(4,user.getName());
            ps.setString(5,user.getProfile());

            ps.execute();
        }
        return 1;
    }

    public Integer UpdateUser(User user) throws SQLException {
        PreparedStatement ps = userConn.prepareStatement(updateUserAll);
        ps.setInt(1,user.getApartId());
        ps.setInt(2,user.getAge());
        ps.setString(3,user.getName());
        ps.setString(4,user.getProfile());

        ps.setLong(5,user.getUuid());

        ps.execute();
        return 1;
    }

    public Integer UpdateUsers(List<User> users) throws SQLException {
        PreparedStatement ps = userConn.prepareStatement(addUserAll);
        for(User user:users){
            ps.setInt(1,user.getApartId());
            ps.setInt(2,user.getAge());
            ps.setString(3,user.getName());
            ps.setString(4,user.getProfile());

            ps.setLong(5,user.getUuid());

            ps.execute();

        }
        return 1;
    }

    public Integer DelUser(Integer uuid){
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            ps = userConn.prepareStatement(delUserAll);
            // first param is 1
            ps.setObject(1,uuid);
            rs = ps.executeQuery();
            // TODO 校验结果

            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public Integer delUsers(List<Integer> uuids) throws SQLException {
        PreparedStatement ps = userConn.prepareStatement(delUserAll);
        for(Integer uuid:uuids){
            ps.setInt(1,uuid);
            ps.execute();


            // TODO 校验结果
        }
        return 1;
    }
}
