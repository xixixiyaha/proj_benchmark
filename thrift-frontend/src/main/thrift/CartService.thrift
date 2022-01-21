namespace java com.freeb.thrift

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

    list<CartInfo> GetCartInfosByAccount(1:i64 userId,2:i32 upperLimit);

    i64 BM3CompareChainNested(1:CartInfo info);

    i64 BM3CompareChainSequence(2:CartInfo info);

}