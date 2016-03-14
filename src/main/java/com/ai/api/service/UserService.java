package com.ai.api.service;

import java.util.List;

import com.ai.api.model.UserBean;


public interface UserService {
	
	UserBean getById(long id);
	
	UserBean getByName(String name);
	
	void saveUser(UserBean user);
	
	void updateUser(UserBean user);
	
	void deleteUserById(long id);

	List<UserBean> findAllUsers();
	
	void deleteAllUsers();
	
	boolean isUserExist(UserBean user);
	
}
