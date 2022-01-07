namespace java benchmark.rpc.thrift
 
service SearchService {
  
	list<i64> IdealResEfficiencyTest(1: i32 totalComputationLoad,2:i32 threadName),

}