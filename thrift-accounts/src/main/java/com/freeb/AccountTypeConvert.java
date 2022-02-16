package com.freeb;

import com.freeb.Entity.AccountInfo;
import com.freeb.Enum.IdType;

public class AccountTypeConvert {

    public static com.freeb.Enum.IdType IdTypeThr2Ori(com.freeb.thrift.IdType idType) {
        switch (idType) {
            case PAYMENT_ID:
                return IdType.PAYMENT_ID;
            case ORDER_ID:
                return IdType.ORDER_ID;
            case MERCHANT_ID:
                return IdType.MERCHANT_ID;
            case ACCOUNT_ID:
                return IdType.ACCOUNT_ID;
            case OBJ_ID:
                return IdType.OBJ_ID;
            case SHIPPING_ID:
                return IdType.SHIPPING_ID;
            default:
                return IdType.ACCOUNT_ID;
        }
    }

    public static com.freeb.thrift.IdType IdTypeOri2thr(com.freeb.Enum.IdType idType) {
        switch (idType) {
            case ACCOUNT_ID:
                return com.freeb.thrift.IdType.ACCOUNT_ID;
            case PAYMENT_ID:
                return com.freeb.thrift.IdType.PAYMENT_ID;
            case ORDER_ID:
                return com.freeb.thrift.IdType.ORDER_ID;
            case MERCHANT_ID:
                return com.freeb.thrift.IdType.MERCHANT_ID;
            case OBJ_ID:
                return com.freeb.thrift.IdType.OBJ_ID;
            case SHIPPING_ID:
                return com.freeb.thrift.IdType.SHIPPING_ID;
            default:
                return com.freeb.thrift.IdType.ACCOUNT_ID;
        }
    }

    public static AccountInfo AccountsInfoThr2Ori(com.freeb.thrift.AccountInfo info) {
        AccountInfo re = new AccountInfo();
        re.setUserId(info.getUserId());
        re.setUserName(info.getUserName());
        re.setUserPasswd(info.getUserPasswd());
        re.setUserDescription(info.getUserDescription());
        re.setUserTag(info.getUserTag());
        return re;
    }

    public static com.freeb.thrift.AccountInfo AccountsInfoOri2Thr(AccountInfo info) {
        com.freeb.thrift.AccountInfo re = new com.freeb.thrift.AccountInfo();
        re.setUserId(info.getUserId());
        re.setUserName(info.getUserName());
        re.setUserPasswd(info.getUserPasswd());
        re.setUserDescription(info.getUserDescription());
        re.setUserTag(info.getUserTag());
        return re;
    }


}