package com.freeb.Service;

import Bean.User;
import Bean.UserFull;

public interface UserService {

    public boolean createUser(UserFull userFull);

    public User getUser(Long uuid);

//    public boolean existUser(Long uuid);

//    public List<User> getUsers(List<Long> uuids);
//
//    public UserFull getUserFull(Long uuid);
//
//    public List<UserFull> getUserFulls(List<Long> uuids);

}
