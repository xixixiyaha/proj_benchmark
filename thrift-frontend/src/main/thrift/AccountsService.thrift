namespace java benchmark.rpc.thrift

enum IdType{
    ACCOUNT_ID = 0,
    ORDER_ID = 1,
    PAYMENT_ID = 2,
    SHIPPING_ID = 3,
    MERCHANT_ID = 4,
    OBJ_ID = 5
}

struct AccountsInfo{
    1: optional i64 userId,
    2: required string userName,
    3: required string userPasswd,
    4: optional string userDescription,
    5: optional map<i32,double> userTag,
}
service AccountsService{


    bool AccountExists(1: i64 id),

    bool VerifyAccessByAccount(1: i64 accountId, 2: i64 targetId,3:IdType idType),

    list<i32> GetAccountTag(1:i64 id),

    map<i32,double> GetUserTags(1:i64 id),

    bool SetUserTags(1:i64 id,2:map<i32,double> tags),

    bool ChangeAccountPwd(1:AccountsInfo info,2:string passwd),


    bool CreateAccount(1:AccountsInfo info),

    AccountsInfo GetAccountInfo(1:i64 id),

    string CompareResEfficiencyBM1(1:string remoteFilePath, 2:i32 testType),

}