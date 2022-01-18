namespace java com.freeb.thrift
struct DiscountInfo{
//TODO
}

service DiscountService{
       DiscountInfo GetDiscounts(1:i64 prodId.2:i32 discountType);
}