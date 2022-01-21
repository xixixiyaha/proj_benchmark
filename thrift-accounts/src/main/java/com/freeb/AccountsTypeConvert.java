package com.freeb;

import com.freeb.Entity.AccountInfo;
import com.freeb.Enum.IdType;

public class AccountsTypeConvert {

    public static com.freeb.Enum.IdType IdTypeThr2Ori(com.freeb.AccountServer.IdType idType) {
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

    public static com.freeb.AccountServer.IdType IdTypeOri2thr(com.freeb.Enum.IdType idType) {
        switch (idType) {
            case ACCOUNT_ID:
                return com.freeb.AccountServer.IdType.ACCOUNT_ID;
            case PAYMENT_ID:
                return com.freeb.AccountServer.IdType.PAYMENT_ID;
            case ORDER_ID:
                return com.freeb.AccountServer.IdType.ORDER_ID;
            case MERCHANT_ID:
                return com.freeb.AccountServer.IdType.MERCHANT_ID;
            case OBJ_ID:
                return com.freeb.AccountServer.IdType.OBJ_ID;
            case SHIPPING_ID:
                return com.freeb.AccountServer.IdType.SHIPPING_ID;
            default:
                return com.freeb.AccountServer.IdType.ACCOUNT_ID;
        }
    }

    public static AccountInfo AccountsInfoThr2Ori(com.freeb.AccountServer.AccountInfo info) {
        AccountInfo re = new AccountInfo();
        re.setUserId(info.getUserId());
        re.setUserName(info.getUserName());
        re.setUserPasswd(info.getUserPasswd());
        re.setUserDescription(info.getUserDescription());
        re.setUserTag(info.getUserTag());
        return re;
    }

    public static com.freeb.AccountServer.AccountInfo AccountsInfoOri2Thr(AccountInfo info) {
        com.freeb.AccountServer.AccountInfo re = new com.freeb.AccountServer.AccountInfo();
        re.setUserId(info.getUserId());
        re.setUserName(info.getUserName());
        re.setUserPasswd(info.getUserPasswd());
        re.setUserDescription(info.getUserDescription());
        re.setUserTag(info.getUserTag());
        return re;
    }


}