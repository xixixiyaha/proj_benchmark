namespace java com.freeb.thrift

enum SearchOrder{
     PRICE_DESC = 0,
     PRICE_ASC = 1,
     SIMILARITY = 2,
     SALES = 3,
     UPDATE_TIME = 4
}

service ProductService{
    map<i32, i32> GetUserActiveByCategory(1:i64 id);

    bool CreateActiveBehavior(1:i64 uid, 2:i64 pid, 3:i32 cid);

    list<i64> GetLastestAvtiveUsers(1:i32 userNum);

    set<i64> GetUserActiveByProduct(1:i64 uid);

    list<i64> GetProductByCategory(1:i32 cid, 2:SearchOrder order, 3:i32 prodNum);

    list<i64> GetProductBySimilarity(1:i32 cid, 2: SearchOrder order,3:string words,4:i32 prodNum);
}