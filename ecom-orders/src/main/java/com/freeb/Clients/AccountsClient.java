package com.freeb.Clients;

import com.freeb.Enum.IdType;

public class AccountsClient {



    public Boolean verifyAccount(Integer accountId){
        return true;
    }

    public Boolean verifyAccessByAccount(Integer accountId,Integer targetId, IdType idType){
        switch (idType){
            case PAYMENT_ID:
                return true;
            case SHIPPING_ID:
                return true;
            default:
                return false;
        }
    }

}
