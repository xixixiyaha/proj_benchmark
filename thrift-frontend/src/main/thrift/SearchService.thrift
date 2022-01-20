namespace java com.freeb.thrift

enum SearchType{
     ACCOUNT_ID = 0,
     ORDER_ID = 1,
     PAYMENT_ID = 2,
     SHIPPING_ID = 3,
     MERCHANT_ID = 4,
     OBJ_ID = 5,
     MERCHANT_NAME = 6,
     OBJ_NAME = 7
}
enum SearchOrder{
     PRICE_DESC = 0,
     PRICE_ASC = 1,
     SIMILARITY = 2,
     SALES = 3,
     UPDATE_TIME = 4
}

service SearchService {

    list<i64> GetRecommendByProdName(1:i64 userId, String words, SearchType type, SearchOrder order)

	list<i64> IdealResEfficiencyTest(1: i32 totalComputationLoad,2:i32 threadName),

	bool BM2CompareParallelEfficiency(1:i32 totalComputationLoad,2:i32 threadNum,3:i32 type);

    bool OfflineUserTagComputation(1:list<i64> uidLst);

}