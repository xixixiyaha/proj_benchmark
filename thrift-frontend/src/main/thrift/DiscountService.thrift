namespace java com.freeb.thrift

struct DiscountInfo{
    1:required i64 discountId,
    2:optional i32 discountType,
    3:required i64 prodId,
    4:required double discountVal,
}

service DiscountService{
       DiscountInfo GetProdDiscounts(1:i64 prodId, 2:i32 discountType);
}