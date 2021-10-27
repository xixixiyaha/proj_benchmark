package benchmark.rpc.thrift;


import org.apache.thrift.TException;


public class UserServiceServerImpl implements UserService.Iface {

    private final Service.UserService userService = new Service.UserServiceServerImpl();

    @Override
    public boolean createUser(UserFull userFull) throws TException {
        Bean.UserFull user = TypeConvertor.convertUserFullThrift2Bean(userFull);
        return userService.createUser(user);
    }

    @Override
    public User getUser(long uuid) throws TException {

        Bean.User user = userService.getUser(uuid);
        return TypeConvertor.convertUserBean2Thrift(user);
    }
}
