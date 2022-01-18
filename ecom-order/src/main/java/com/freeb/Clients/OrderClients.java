package com.freeb.Clients;

import com.freeb.Entity.DiscountInfo;
import com.freeb.Entity.PaymentInfo;
import com.freeb.Enum.IdType;
import com.freeb.Enum.PaymentStatus;

public class OrderClients {

    /*==== Accounts ====*/

    public Boolean verifyAccount(long accountId){
        return true;
    }

    public Boolean verifyAccessByAccount(long accountId, long targetId, IdType idType){
        switch (idType){
            case PAYMENT_ID:
                return true;
            case SHIPPING_ID:
                return true;
            default:
                return false;
        }
    }

    /*==== Products ====*/

    public PaymentStatus CheckPaymentStatusById(Long uid, Long paymentId){
        return PaymentStatus.PAYMENT_NO_RECORD;
    }

    /*
     * Normal Callback. High Concurrency.
     * */
    public Long CreatePayment(long uid,double prodVal,double discountsVal){
        return -1L;
    }

    /*
     * Nested. Long waiting time.
     * */
    public Boolean CancelPayment(Long uid,Long paymentId){
        return false;
    }

    /*
     * Big data structure.
     * */
    public PaymentInfo GetPaymentInfoById(Long uid, Long paymentId){
        return null;
    }


    /*==== Discounts ====*/
    public Double GetProdPrice(long prodId) {
        return null;
    }

    public DiscountInfo GetDiscounts(Long prodId, Integer type){
        return null;
    }
}
