package com.ai.api.service;

import java.util.List;

import com.ai.api.model.UserDemoBean;


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
