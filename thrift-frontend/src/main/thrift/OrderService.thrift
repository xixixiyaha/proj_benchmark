namespace java com.freeb.thrift

struct OrderResp{
//TODO
}

struct OrderReq{
  1:required i64 userId;
  2:optional i32 status;
  3:optional i64 merchantId;
  4:optional string merchantName;
  5:optional i64 prodId;
  6:optional i64 prodName;
  7: requireInfo;
    private long orderId;
    private long paymentId;
}

service OrderSearvice{
    OrderResp GetOrderListByUserId(OrderReq orderReq);

    OrderResp GetOrderByPaymentId(OrderReq orderReq);

    OrderResp CreatePaymentByOrderId(OrderReq orderReq);

    OrderResp GetOrderByOrderId(OrderReq orderReq);
}




