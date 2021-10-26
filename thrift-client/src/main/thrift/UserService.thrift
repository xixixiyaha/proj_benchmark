namespace java benchmark.rpc.thrift
 
struct User {
	1: i64 uuid,
	2: i32 apartId,
	3: i32 age,
	4: string name,
	5: string profile,
}

struct UserFull{
    1: i64 uuid,
    2: string name,
    3: i32 sex,
    4: string email,
    5: string mobile,
    6: string profile,
    7: i32 status,
    8: list<i64> permissions,
}

//struct UserPage {
//  	1: i32 pageNo,
//	2: i32 total,
//	3: list<User> result,
//}
 
service UserService {
  
	bool createUser(1: UserFull userFull),
  
	User getUser(1: i64 uuid),
}