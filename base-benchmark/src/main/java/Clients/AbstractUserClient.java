package Clients;

import Bean.User;
import Bean.UserFull;
import Generater.BeanGenerator;
import Service.UserService;
import Service.UserServiceServerImpl;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractUserClient {

    private final AtomicInteger counter = new AtomicInteger(0);
    private final UserService _serviceUserService = new UserServiceServerImpl();


    protected abstract UserService getUserService();

    public boolean createUser() throws Exception {
        int id = counter.getAndIncrement();
        UserFull userFull = BeanGenerator.genUserFull(1).get(0); //_serviceUserService.getUserFull((long) id);
        return getUserService().createUser(userFull);
    }

    public User getUser() throws Exception {
        int id = counter.getAndIncrement();
        return getUserService().getUser((long)id);
    }


}
