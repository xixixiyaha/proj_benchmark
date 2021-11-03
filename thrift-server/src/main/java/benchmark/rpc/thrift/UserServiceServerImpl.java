package benchmark.rpc.thrift;


import org.apache.thrift.TException;


public class UserServiceServerImpl implements UserService.Iface {

    private final Service.UserService userService = new Service.UserServiceServerImpl();
    private int createNum = 0;
    private int getNum = 0;
    @Override
    public boolean createUser(UserFull userFull) throws TException {
        Bean.UserFull user = TypeConvertor.convertUserFullThrift2Bean(userFull);
        System.out.println("<createUser> received"+createNum++);
        return userService.createUser(user);
    }

    @Override
    public User getUser(long uuid) throws TException {

        Bean.User user = userService.getUser(uuid);
        System.out.println("<getUser> received"+getNum++);
        return TypeConvertor.convertUserBean2Thrift(user);
    }
}
