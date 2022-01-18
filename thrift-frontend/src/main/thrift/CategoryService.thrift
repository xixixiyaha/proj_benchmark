namespace java com.freeb.thrift
include "DiscountService.thrift"


struct CommentInfo{

    1:required i64 commentId;
    2:required i64 userId;
    3:optional string userName;
    4:required i64 prodId;
    5:required string commentDetails;
    6:optional list<string> commentImages;
}

struct ProductInfo {
    1:required i64 prodId;
    2:required string prodName;
    3:optional i32 categoryId;
    4:required double prodPrice;
    5:optional double prodSales;
    6:optional list<string> prodImages;
    7:optional i64 discountsId;
    8:required i64 merchantId;
    9:optional string createTime;
    10:optional string updateTime;
}

struct ProductPage {
    1:required ProductInfo info,
    2:optional i64 merchantId,
    3:optional i64 merchantName,
    4:optional DiscountService.DiscountInfo discountVal,
    5:optional list<CommentInfo> prodComments,
}

struct CategoryPage {
      1:required i64 prodId,
      2:required string prodName,
      3:optional i32 prodSales,
      4:optional string prodImage,
      5:required i64 merchantId,
      6:required string merchantName,
}

service CategoryService{
    ProductPage GetProductPage(1:i64 prodId);

    list<CategoryPage> GetCategoryPage(1:i64 userId, String searchKey);

}