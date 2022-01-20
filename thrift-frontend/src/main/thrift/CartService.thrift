namespace java com.freeb.thrift
include "OrderService.thrift"

struct CartInfo{
    1:required i64 cartId,
    2:required i64 userId,
    3:optional i64 objId,
    4:optional i64 merchantId,
    5:optional i32 incartQuantity,
    6:optional i32 incartSelect,
}
service CartService{
 /*
    * Big Data Transfer
    * */

    List<CartInfo> GetCartInfosByAccount(1:i64 userId,2:i32 upperLimit);

     OrderService.OrderResp BM3CompareChainNested(CartInfo info);

     OrderService.OrderResp BM3CompareChainSequence(CartInfo info);

}