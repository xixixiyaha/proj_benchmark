package thrift;

import com.freeb.Enum.IdType;
import thrift.accounts.AccountsInfo;

public class AccountsTypeConvert {

    public static com.freeb.Enum.IdType IdTypeThr2Ori(thrift.accounts.IdType idType) {
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

    public static thrift.accounts.IdType IdTypeOri2thr(com.freeb.Enum.IdType idType) {
        switch (idType) {
            case ACCOUNT_ID:
                return thrift.accounts.IdType.ACCOUNT_ID;
            case PAYMENT_ID:
                return thrift.accounts.IdType.PAYMENT_ID;
            case ORDER_ID:
                return thrift.accounts.IdType.ORDER_ID;
            case MERCHANT_ID:
                return thrift.accounts.IdType.MERCHANT_ID;
            case OBJ_ID:
                return thrift.accounts.IdType.OBJ_ID;
            case SHIPPING_ID:
                return thrift.accounts.IdType.SHIPPING_ID;
            default:
                return thrift.accounts.IdType.ACCOUNT_ID;
        }
    }

    public static com.freeb.Entity.AccountsInfo AccountsInfoThr2Ori(thrift.accounts.AccountsInfo info) {
        com.freeb.Entity.AccountsInfo re = new com.freeb.Entity.AccountsInfo();
        re.setUserId(info.getUserId());
        re.setUserName(info.getUserName());
        re.setUserPasswd(info.getUserPasswd());
        re.setUserDescription(info.getUserDescription());
        re.setUserTag(info.getUserTag());
        return re;
    }

    public static thrift.accounts.AccountsInfo AccountsInfoOri2Thr(com.freeb.Entity.AccountsInfo info) {
        AccountsInfo re = new AccountsInfo();
        re.setUserId(info.getUserId());
        re.setUserName(info.getUserName());
        re.setUserPasswd(info.getUserPasswd());
        re.setUserDescription(info.getUserDescription());
        re.setUserTag(info.getUserTag());
        return re;
    }


}