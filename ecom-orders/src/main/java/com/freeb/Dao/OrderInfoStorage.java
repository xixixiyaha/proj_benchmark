package com.freeb.Dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OrderInfoStorage {

    private static final Logger logger = LoggerFactory.getLogger(OrderInfoStorage.class);

    static String ORDER_CACHE_URL ;
    static String ORDER_USER;
    static String ORDER_PSW;

    OrderInfoStorage(String url, String name, String psw){
        ORDER_CACHE_URL=url;
        ORDER_USER=name;
        ORDER_PSW=psw;

        try(Connection conn = DriverManager.getConnection(ORDER_CACHE_URL,ORDER_USER,ORDER_PSW)){
            logger.info("DB connected!");
        }catch (SQLException e){
            logger.error(String.format("DB connect failure %s",e.toString()));
            // Notice here
            e.printStackTrace();
        }
    }
}
