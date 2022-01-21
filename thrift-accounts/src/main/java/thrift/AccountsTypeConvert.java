package thrift;

import com.freeb.Entity.AccountInfo;
import com.freeb.Enum.IdType;

public class AccountsTypeConvert {

    public static com.freeb.Enum.IdType IdTypeThr2Ori(thrift.AccountServer.IdType idType) {
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

    public static thrift.AccountServer.IdType IdTypeOri2thr(com.freeb.Enum.IdType idType) {
        switch (idType) {
            case ACCOUNT_ID:
                return thrift.AccountServer.IdType.ACCOUNT_ID;
            case PAYMENT_ID:
                return thrift.AccountServer.IdType.PAYMENT_ID;
            case ORDER_ID:
                return thrift.AccountServer.IdType.ORDER_ID;
            case MERCHANT_ID:
                return thrift.AccountServer.IdType.MERCHANT_ID;
            case OBJ_ID:
                return thrift.AccountServer.IdType.OBJ_ID;
            case SHIPPING_ID:
                return thrift.AccountServer.IdType.SHIPPING_ID;
            default:
                return thrift.AccountServer.IdType.ACCOUNT_ID;
        }
    }

    public static AccountInfo AccountsInfoThr2Ori(thrift.AccountServer.AccountInfo info) {
        AccountInfo re = new AccountInfo();
        re.setUserId(info.getUserId());
        re.setUserName(info.getUserName());
        re.setUserPasswd(info.getUserPasswd());
        re.setUserDescription(info.getUserDescription());
        re.setUserTag(info.getUserTag());
        return re;
    }

    public static thrift.AccountServer.AccountInfo AccountsInfoOri2Thr(AccountInfo info) {
        thrift.AccountServer.AccountInfo re = new thrift.AccountServer.AccountInfo();
        re.setUserId(info.getUserId());
        re.setUserName(info.getUserName());
        re.setUserPasswd(info.getUserPasswd());
        re.setUserDescription(info.getUserDescription());
        re.setUserTag(info.getUserTag());
        return re;
    }


}