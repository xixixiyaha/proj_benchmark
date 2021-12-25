package com.freeb.Dao;

import com.alibaba.druid.pool.DruidDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DruidUtil {
    DruidDataSource dataSource;

    DruidUtil(String url,String userName,String passwprd){
        dataSource = new DruidDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(userName);
        dataSource.setPassword(passwprd);

        //Default Setting
        dataSource.setInitialSize(5);  //初始连接数，默认0
        dataSource.setMaxActive(10);  //最大连接数，默认8
        dataSource.setMinIdle(5);  //最小闲置数
        dataSource.setMaxWait(2000);  //获取连接的最大等待时间，单位毫秒
//        dataSource.setPoolPreparedStatements(true); //缓存PreparedStatement，默认false
        dataSource.setMaxOpenPreparedStatements(10); //缓存PreparedStatement的最大数量，默认-1（不缓存）。大于0时会自动开启缓存PreparedStatement，所以可以省略上一句代码

    }
    public DruidDataSource GetDataSource(){
        return this.dataSource;
    }
    public Connection GetConnection() throws SQLException {

        if(dataSource==null)return null;
        return dataSource.getConnection();
    }
    public void CloseConnection(){
        if(dataSource==null)return;
        dataSource.close();
    }

}
