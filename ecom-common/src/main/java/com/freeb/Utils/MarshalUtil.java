package com.freeb.Utils;

import com.freeb.Entity.AccountsInfo;
import com.freeb.Entity.CartInfo;
import com.freeb.Entity.OrderInfo;
import com.freeb.Entity.PaymentInfo;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MarshalUtil {


    public static AccountsInfo convertRs2Account(ResultSet rs){

        AccountsInfo info=new AccountsInfo();
        try{
            while(rs.next()){
                info.setUserId(rs.getLong(1));
                info.setUserName(rs.getString(2));
                info.setUserPwd(rs.getString(3));
                info.setUserDescription(rs.getString(4));
                break;
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

        return info;
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
    public static PaymentInfo convertRs2Payment(ResultSet rs){
        PaymentInfo info=new PaymentInfo();
        try{
            while(rs.next()){
                info.setPaymentId(rs.getLong(1));
                info.setPaymentStatus(rs.getInt(2));
                info.setPaymentVal(rs.getDouble(3));
                info.setDiscountsVal(rs.getDouble(4));
                info.setPaymentCard(rs.getString(5));
                info.setAccountId(rs.getInt(6));
                break;
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

        return info;
    }

    public static List<CartInfo> convertRs2CartList(ResultSet rs){

        List<CartInfo> lst=new ArrayList<>();
        try{
            while(rs.next()){
                CartInfo info = new CartInfo();
                info.setCartId(rs.getLong(1));
                info.setAccountId(rs.getLong(2));
                info.setObjId(rs.getLong(3));
                info.setMerchantId(rs.getLong(4));
                info.setIncartQuantity(rs.getInt(5));
                info.setIncartSelect(rs.getInt(6));
                lst.add(info);
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

        return lst;
    }


}
