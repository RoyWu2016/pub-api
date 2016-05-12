package com.ai.api.service;

import com.ai.api.exception.AIException;
import com.ai.api.model.UserDemoBean;
import com.ai.commons.beans.customer.ContactBean;
import com.ai.commons.beans.customer.GeneralUserViewBean;
import com.ai.commons.beans.customer.OrderBookingBean;

import java.io.IOException;
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

    void getProfileUpdate(GeneralUserViewBean generalUserViewBean, String user_id) throws IOException, AIException;

    void getProfileContactUpdate(GeneralUserViewBean generalUserViewBean, ContactBean contactBean, String user_id) throws IOException, AIException;

    void getProfileBookingPreferenceUpdate(OrderBookingBean orderBookingBean, String user_id) throws IOException, AIException;
}
