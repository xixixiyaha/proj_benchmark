package benchmark.rpc.thrift;


import Bean.User;
import Bean.UserFull;
import CommuteUtils.LockObjectPool;

import java.io.Closeable;
import java.io.IOException;


public class UserServiceClientImpl implements Service.UserService, Closeable {

    // TODO

    private static String host = "{benchmark-server}";
    private static int port = 8080;
    private final LockObjectPool<ThriftUserServiceClient> clientPool = new LockObjectPool<>(32,()->new ThriftUserServiceClient(host,port));

    @Override
    public boolean createUser(UserFull userFull) {
        benchmark.rpc.thrift.UserFull tuserfull = TypeConvertor.convertUserFullBean2Thrift(userFull);
        ThriftUserServiceClient thriftUserServiceClient = clientPool.borrow();

        try {
            return thriftUserServiceClient.client.createUser(tuserfull);
        }catch (Throwable e){
            throw new RuntimeException(e);
        }finally {
            clientPool.release(thriftUserServiceClient);
        }
    }

    @Override
    public User getUser(Long uuid) {

        ThriftUserServiceClient thriftUserServiceClient = clientPool.borrow();
        Bean.User user;
        try {
            benchmark.rpc.thrift.User tuser = thriftUserServiceClient.client.getUser(uuid);
            return TypeConvertor.convertUserThrift2Bean(tuser);
        }catch (Throwable e){
            throw new RuntimeException(e);
        }finally {
            clientPool.release(thriftUserServiceClient);
        }
    }

    @Override
    public void close() throws IOException {
        clientPool.close();
    }

    public static void main(String[] args) throws IOException{
        try(UserServiceClientImpl userService= new UserServiceClientImpl()){
            Bean.UserFull testUserFull = new Bean.UserFull((long)5,"Anna",1,"abc@gmail","12345678910","http://url",0);

            System.out.println(userService.createUser(new Bean.UserFull()));
            System.out.println(userService.getUser((long) 5));
        }
    }
}
