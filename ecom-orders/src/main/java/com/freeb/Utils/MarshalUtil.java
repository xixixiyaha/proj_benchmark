package com.freeb.Utils;

import com.freeb.Entity.OrderInfo;
import com.freeb.Entity.OrderSearchKey;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MarshalUtil {

    public static String convertSearchKey2String(OrderSearchKey key){
        return "123";
    }

    public static List<OrderInfo> convertString2OrderList(String str){
        return null;
    }

    public static List<OrderInfo> convertRs2OrderList(ResultSet rs){

        List<OrderInfo> lst=new ArrayList<>();
        try{
            while(rs.next()){
                OrderInfo info = new OrderInfo();
                info.setOrderId(rs.getInt(1));
                info.setAccountId(rs.getInt(2));
                info.setPaymentStatus(rs.getInt(3));
                info.setMerchantId(rs.getInt(4));
                info.setMerchantName(rs.getString(5));
                info.setObjId(rs.getInt(6));
                info.setObjName(rs.getString(7));
                info.setPaymentId(rs.getInt(8));
                lst.add(info);
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

        return lst;
    }
}
