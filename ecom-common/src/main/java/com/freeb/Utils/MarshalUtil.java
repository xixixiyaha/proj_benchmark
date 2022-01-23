package com.freeb.Utils;

import com.freeb.Entity.*;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MarshalUtil {


    public static AccountInfo convertRs2Account(ResultSet rs){
        //Notice 现在的marshal和accountDao配套 不含user Tags
        AccountInfo info=new AccountInfo();
        try{
            if(rs.next()){
                info.setUserId(rs.getLong(1));
                info.setUserName(rs.getString(2));
                info.setUserPasswd(rs.getString(3));
                info.setUserCard(rs.getString(4));
                info.setUserDescription(rs.getString(5));

            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

        return info;
    }

//    public static List<OrderInfo> convertString2OrderList(String str){
//        return null;
//    }

    public static List<OrderInfo> convertRs2OrderList(ResultSet rs){

        List<OrderInfo> lst=new ArrayList<>();
        try{
            while(rs.next()){
                OrderInfo info = new OrderInfo();
                info.setOrderId(rs.getInt(1));
                info.setUserId(rs.getInt(2));
                info.setPaymentStatus(rs.getInt(3));
                info.setMerchantId(rs.getInt(4));
                info.setMerchantName(rs.getString(5));
                info.setProdId(rs.getInt(6));
                info.setProdName(rs.getString(7));
                info.setPaymentId(rs.getInt(8));
                info.setCartId(rs.getLong(9));
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
            if(rs.next()){
                info.setPaymentId(rs.getLong(1));
                info.setPaymentStatus(rs.getInt(2));
                info.setPaymentVal(rs.getDouble(3));
                info.setDiscountsVal(rs.getDouble(4));
                info.setPaymentCard(rs.getString(5));
                info.setUserId(rs.getLong(6));

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
                info.setUserId(rs.getLong(2));
                info.setProdId(rs.getLong(3));
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

    public static List<ProductInfo> convertRs2ProdList(ResultSet rs){ //done
        JSONParser parser = new JSONParser();
        List<ProductInfo> lst=new ArrayList<>();
        try{
            while(rs.next()){
                ProductInfo info = new ProductInfo();
                info.setProdId(rs.getLong(1));
                info.setProdName(rs.getString(2));
                info.setCategoryId(rs.getInt(3));
                info.setProdPrice(rs.getDouble(4));
                info.setProdSales(rs.getInt(5));
                info.setProdRemain(rs.getInt(6));
                //Notice DB only stores the URLs.
                List<String> imgs = new ArrayList<>();
                JSONArray jarr = (JSONArray) parser.parse(rs.getString(7));
                for (Object jsonValue : jarr) {
                    imgs.add( (String)jsonValue);
                }
                info.setProdImages(imgs);
                info.setDiscountsId(rs.getLong(8));
                info.setMerchantId(rs.getLong(9));
                lst.add(info);
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        return lst;
    }


    public static List<CommentInfo> convertRs2CommentList(ResultSet rs){
        JSONParser parser = new JSONParser();
        List<CommentInfo> lst=new ArrayList<>();
        try{
            while(rs.next()){
                CommentInfo info = new CommentInfo();
                info.setCommentId(rs.getLong(1));
                info.setUserId(rs.getLong(2));
                //TODO DB only stores the path. Tags should be read from .txt/.csv/.json/ ...
//                info.setProdTag(rs.get(3));
                info.setProdId(rs.getLong(3));
                info.setCommentDetails(rs.getString(4));
                List<String> imageUrls = new ArrayList<>();
                JSONArray jarr = (JSONArray) parser.parse(rs.getString(5));
                for (Object jsonValue : jarr) {
                    imageUrls.add( (String)jsonValue);
                }
                info.setCommentImages(imageUrls);
                lst.add(info);
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        return lst;
    }

}
