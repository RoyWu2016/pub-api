package com.ai.api.service;

import com.ai.api.model.UserDemoBean;

import java.util.List;


public interface UserServiceDemo {

    UserDemoBean getById(long id);

    UserDemoBean getByName(String name);

    void saveUser(UserDemoBean user);

    void updateUser(UserDemoBean user);

    void deleteUserById(long id);

    List<UserDemoBean> findAllUsers();

    void deleteAllUsers();

    boolean isUserExist(UserDemoBean user);
}
