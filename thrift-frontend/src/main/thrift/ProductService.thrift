namespace java com.freeb.thrift

enum SearchOrder{
     PRICE_DESC = 0,
     PRICE_ASC = 1,
     SIMILARITY = 2,
     SALES = 3,
     UPDATE_TIME = 4
}
struct CommentInfo{

    1:required i64 commentId,
    2:required i64 userId,
    3:optional string userName,
    4:required i64 prodId,
    5:required string commentDetails,
    6:optional list<string> commentImages,
}
struct ProductInfo {
    1:required i64 prodId,
    2:required string prodName,
    3:optional i32 categoryId,
    4:required double prodPrice,
    5:optional double prodSales,
    6:optional i32 prodRemain,
    7:optional list<string> prodImages,
    8:optional i64 discountsId,
    9:required i64 merchantId,
    10:optional string createTime,
    11:optional string updateTime,
}

struct MerchantInfo{
    1:required i64 merchantId,
    2:optional string merchantName,
}

service ProductService{
    map<i32, i32> GetUserActiveByCategory(1:i64 id);

    bool CreateActiveBehavior(1:i64 uid, 2:i64 pid, 3:i32 cid);

    list<i64> GetLastestActiveUsers(1:i32 userNum);

    set<i64> GetUserActiveByProduct(1:i64 uid);

    list<i64> GetProductByCategory(1:i32 cid, 2:SearchOrder order, 3:i32 prodNum);

    list<i64> GetProductBySimilarity(1:i32 cid, 2: SearchOrder order,3:string words,4:i32 prodNum);

    ProductInfo IncProductSales(1:i64 pid,2:i32 perchaseNum);

    MerchantInfo GetMerchantInfoById(1:i64 mid);

    bool BM4ComparePatternTrigger(1:list<i64> uidLst,2:list<i64> pidLst,3:list<i32> cidLst,4:i32 compLoad);

    CommentInfo BM5CompareTransferDataSize(1:i32 dataSize);

    bool BM6CompareMemBindWidth(1:i32 dataSize);

    list<CommentInfo> GetComments(1:i64 prodId,2:i32 comtNum);





}