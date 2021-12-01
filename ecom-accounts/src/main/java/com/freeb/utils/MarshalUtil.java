package com.freeb.utils;

import com.freeb.Entity.AccountsInfo;

import java.sql.ResultSet;

public class MarshalUtil {

    public static AccountsInfo convertRs2Account(ResultSet rs){

        AccountsInfo info=new AccountsInfo();
        try{
            while(rs.next()){
                info.setAccountId(rs.getInt(1));
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
}
