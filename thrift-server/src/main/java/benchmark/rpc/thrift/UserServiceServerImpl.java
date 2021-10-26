package thrift;


import CommuteUtils.LockObjectPool;
import org.apache.thrift.TException;


import java.io.Closeable;
import java.io.IOException;


public class UserServiceServerImpl implements UserService.Iface {

    private final Service.UserService userService = new Service.UserServiceServerImpl();

    @Override
    public boolean createUser(UserFull userFull) throws TException {
        Bean.User user = TypeConvertor.
        return false;
    }

    @Override
    public User getUser(long uuid) throws TException {
        return null;
    }
}
