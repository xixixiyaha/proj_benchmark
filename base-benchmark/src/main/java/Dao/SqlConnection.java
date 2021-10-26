package Dao;

import java.sql.*;

public class SqlConnection {

    // TODO 从命令行读取数据库连接信息
    private static final String URL = "jdbc:mysql://ip:port/dbName";
    private static final String NAME = "root";
    private static final String PASSWORD = "root";

    private static final String driver = "com.mysql.jdbc.Driver";



    public void TestSqlConnection(){
        //加载驱动
        try{
            Class.forName(driver);
        } catch (ClassNotFoundException e){
            System.out.println("未能成功加载驱动程序,请检查是否导入驱动程序!");
            e.printStackTrace();
        }
        Connection testConn = null;
        try{
            testConn = DriverManager.getConnection(URL, NAME, PASSWORD);
            System.out.println("获取数据库链接成功");
        }catch (SQLException e){
            System.out.println("获取数据库连接失败");
            e.printStackTrace();
        }

        //数据库打开后要关闭
        if(testConn != null){
            try {
                testConn.close();
            }catch (SQLException e){
                e.printStackTrace();
                testConn = null;
            }
        }
    }

    static {
        try {
            Class.forName(driver);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static Connection conn = null;

    public static Connection getConnection() throws Exception{
        if(conn == null){
            synchronized (SqlConnection.class){
                if (conn==null){
                    conn = DriverManager.getConnection(URL,NAME,PASSWORD);
                }
            }
        }
        return conn;
    }

    public void closeConnection(ResultSet rs, Statement statement, Connection con) {
          try {
            if (rs != null) {
                rs.close();
            }
          } catch (SQLException e) {
              e.printStackTrace();
          } finally {
              try {
                  if (statement != null) {
                      statement.close();
                  }
              } catch (Exception e) {
                  e.printStackTrace();
              } finally {
                  try {
                      if (con != null) {
                          con.close();
                      }
                  } catch (SQLException e) {
                      e.printStackTrace();
                  }
              }
          }
    }
}
