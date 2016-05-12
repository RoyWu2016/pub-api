package com.ai.api.service.impl;

import com.ai.api.bean.UserChoiceBean;
import com.ai.api.dao.CustomerDao;
import com.ai.api.exception.AIException;
import com.ai.api.model.UserDemoBean;
import com.ai.api.service.UserServiceDemo;
import com.ai.commons.beans.customer.ContactBean;
import com.ai.commons.beans.customer.GeneralUserViewBean;
import com.ai.commons.beans.customer.OrderBookingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service("userDemoService")
@Transactional
public class UserServiceDemoImpl implements UserServiceDemo {

    @Autowired
    @Qualifier("customerDao")
    private CustomerDao customerDao;


    private static final AtomicLong counter = new AtomicLong();

    private static List<UserDemoBean> users;

    private static List<UserChoiceBean> usersChoice;

    static {
        users = populateDummyUsers();
    }

    private static List<UserDemoBean> populateDummyUsers() {
        List<UserDemoBean> users = new ArrayList<>();
        users.add(new UserDemoBean(counter.incrementAndGet(), "Sam", 30, 70000));
        users.add(new UserDemoBean(counter.incrementAndGet(), "Tom", 40, 50000));
        users.add(new UserDemoBean(counter.incrementAndGet(), "Jerome", 45, 30000));
        users.add(new UserDemoBean(counter.incrementAndGet(), "Silvia", 50, 40000));
        return users;
    }

    public List<UserDemoBean> findAllUsers() {
        return users;
    }

    public UserDemoBean getById(long id) {
        for (UserDemoBean user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    public UserDemoBean getByName(String name) {
        for (UserDemoBean user : users) {
            if (user.getName().equalsIgnoreCase(name)) {
                return user;
            }
        }
        return null;
    }

    public void saveUser(UserDemoBean user) {
        user.setId(counter.incrementAndGet());
        users.add(user);
    }

    public void updateUser(UserDemoBean user) {
        int index = users.indexOf(user);
        users.set(index, user);
    }

    public void deleteUserById(long id) {

        for (Iterator<UserDemoBean> iterator = users.iterator(); iterator.hasNext(); ) {
            UserDemoBean user = iterator.next();
            if (user.getId() == id) {
                iterator.remove();
            }
        }
    }

    public boolean isUserExist(UserDemoBean user) {
        return getByName(user.getName()) != null;
    }

    public void deleteAllUsers() {
        users.clear();
    }

    //-------------------By kk ---------------------------
    public void getProfileUpdate(GeneralUserViewBean generalUserViewBean, String user_id) throws AIException {
        customerDao.updateProfileCompany(generalUserViewBean, user_id);
    }

    public void getProfileContactUpdate(GeneralUserViewBean generalUserViewBean, ContactBean contactBean, String user_id) throws IOException, AIException {
        customerDao.updateProfileContact(generalUserViewBean, contactBean, user_id);
    }

    public void getProfileBookingPreferenceUpdate(OrderBookingBean orderBookingBean, String user_id) throws IOException, AIException {
        customerDao.updateBookingPreference(orderBookingBean, user_id);
    }
}
