namespace java com.freeb.thrift

enum PaymentStatus{
    PAYMENT_NO_RECORD=0,
    PAYMENT_START=1,
    PAYMENT_CANCELLED=2,
    PAYMENT_FINISH=3,
    PAYMENT_ERR=4,
    PAYMENT_REFUND_START=5,
    PAYMENT_REFUND_CALLED=6,
    PAYMENT_REFUND_FINISH=7,
    PAYMENT_REFUND_ERR=8,
}

struct PaymentInfo{
    1:required i64 paymentId,
    2:optional i32 paymentStatus,
    3:required double paymentVal,
    4:optional double discountsVal,
    5:optional string paymentCard,
    6:required i64 userId,
    7:optional string createTime,
    8:optional string updateTime,
}

service PaymentService{
 /*
    * Normal Function. @Long Call Chain
    * */
    PaymentStatus CheckPaymentStatusById(1:i64 uid,2:i64 paymentId);

    /*
    * Normal Callback. High Concurrency.
    * */
    i64 CreatePayment(PaymentInfo info);

    /*
    * Nested. Long waiting time.
    * */
    bool CancelPayment(1:i64 uid,2:i64 paymentId);

    /*
    * Big data structure.
    * */
    PaymentInfo GetPaymentInfoById(1:i64 uid,2:i64 paymentId);
}