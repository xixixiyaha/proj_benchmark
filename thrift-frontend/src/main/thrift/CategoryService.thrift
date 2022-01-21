namespace java com.freeb.com.freeb.thrift
include "DiscountService.com.freeb.thrift"
include "ProductService.com.freeb.thrift"

struct ProductPage {
    1:required ProductService.ProductInfo info,
    2:optional i64 merchantId,
    3:optional i64 merchantName,
    4:optional DiscountService.DiscountInfo discountVal,
    5:optional list<ProductService.CommentInfo> prodComments,
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

    list<CategoryPage> GetCategoryPage(1:i64 userId,2:string searchKey);

    list<ProductService.ProductInfo> BM2CompareParallelRpcEfficiency(1:i32 totalComputationLoad,2:i32 threadNum);

    list<ProductPage> BM4ComparePatternFanout(1:list<i64> pidLst);

}