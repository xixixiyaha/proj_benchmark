package Service;

import Bean.User;
import Bean.UserFull;
import Dao.UserDao;

import java.util.List;

public class UserServiceServerImpl implements UserService {

    UserDao userDao;

    {
        try {
            userDao = new UserDao();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean createUser(UserFull userFull) {
        return false;
    }

    @Override
    public boolean existUser(Long uuid) {
        return false;
    }

    @Override
    public User getUser(Long uuid) {
        return null;
    }

    @Override
    public List<User> getUsers(List<Long> uuids) {
        return null;
    }

    @Override
    public UserFull getUserFull(Long uuid) {
        return null;
    }

    @Override
    public List<UserFull> getUserFulls(List<Long> uuids) {
        return null;
    }
}
