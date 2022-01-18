namespace java com.freeb.thrift
struct CartInfo{
    1:required i64 cartId,
    2:required i64 accountId,
    3:required i64 objId,
    4:optional i64 merchantId,
    5:required i32 incartQuantity,
    6:optional i32 incartSelect,
}
service CartService{
 /*
    * Big Data Transfer
    * */

    List<CartInfo> GetCartInfosByAccount(1:i64 accountId,2:i32 upperLimit);

}