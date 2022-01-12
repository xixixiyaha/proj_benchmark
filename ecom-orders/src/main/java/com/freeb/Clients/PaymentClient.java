package com.freeb.Clients;

import com.freeb.Entity.PaymentInfo;
import com.freeb.Enum.PaymentStatus;

public class PaymentClient {

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
    public PaymentInfo GetPaymentInfoById(Long uid,Long paymentId){
        return null;
    }
}
