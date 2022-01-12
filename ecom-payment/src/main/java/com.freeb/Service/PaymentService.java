package com.freeb.Service;

import com.freeb.Entity.PaymentInfo;
import com.freeb.Enum.PaymentStatus;

public interface PaymentService {

    /*
    * Normal Function. @Long Call Chain
    * */
    public PaymentStatus CheckPaymentStatusById(Long uid,Long paymentId);

    /*
    * Normal Callback. High Concurrency.
    * */
    public Long CreatePayment(PaymentInfo info);

    /*
    * Nested. Long waiting time.
    * */
    public Boolean CancelPayment(Long uid,Long paymentId);

    /*
    * Big data structure.
    * */
    public PaymentInfo GetPaymentInfoById(Long uid,Long paymentId);

}
