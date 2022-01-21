namespace java com.freeb.thrift
include "CartService.thrift"


enum RespCode {
    SUCCESS=0,
    DENY_NO_AUTHORITY=1,
    FAILURE_UNKNOWN=2
}

struct BaseResp {
    1: required RespCode Status;
    2: optional string Msg;
}
struct OrderInfo{
    1:required i64 orderId,
    2:required i64 userId,
    3:optional i32 paymentStatus,
    4:optional i64 merchantId,
    5:optional string merchantName,
    6:required i64 prodId,
    7:required string prodName,
    8:optional i64 paymentId,
    9:optional CartService.CartInfo cartId,
}
struct OrderResp{
    1: optional list<OrderInfo> orderInfos;
    2: optional bool hasMore;
    3: required BaseResp baseResp;
}

struct OrderReq{
  1:required i64 userId,
  2:optional i32 status,
  3:optional i64 merchantId,
  4:optional string merchantName,
  5:optional i64 prodId,
  6:optional i64 prodName,
  7:optional i64 orderId,
  8:optional i64 paymentId,
  9:optional CartService.CartInfo cartId,
}

service OrderSearvice{
    OrderResp GetOrderListByUserId(OrderReq orderReq);

    OrderResp GetOrderByPaymentId(OrderReq orderReq);

//    OrderResp CreatePaymentByOrderId(OrderReq orderReq);

    OrderResp GetOrderByOrderId(OrderReq orderReq);

    OrderResp CreateOrderByCartInfo(OrderReq orderReq);

}




