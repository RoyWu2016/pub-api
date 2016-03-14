package com.ai.api.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.ai.api.model.UserBean;
import com.ai.api.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {
	
	private static final AtomicLong counter = new AtomicLong();
	
	private static List<UserBean> users;
	
	static{
		users= populateDummyUsers();
	}

	public List<UserBean> findAllUsers() {
		return users;
	}
	
	public UserBean getById(long id) {
		for(UserBean user : users){
			if(user.getId() == id){
				return user;
			}
		}
		return null;
	}
	
	public UserBean getByName(String name) {
		for(UserBean user : users){
			if(user.getName().equalsIgnoreCase(name)){
				return user;
			}
		}
		return null;
	}
	
	public void saveUser(UserBean user) {
		user.setId(counter.incrementAndGet());
		users.add(user);
	}

	public void updateUser(UserBean user) {
		int index = users.indexOf(user);
		users.set(index, user);
	}

	public void deleteUserById(long id) {
		
		for (Iterator<UserBean> iterator = users.iterator(); iterator.hasNext(); ) {
		    UserBean user = iterator.next();
		    if (user.getId() == id) {
		        iterator.remove();
		    }
		}
	}

	public boolean isUserExist(UserBean user) {
		return getByName(user.getName())!=null;
	}

	private static List<UserBean> populateDummyUsers(){
		List<UserBean> users = new ArrayList<>();
		users.add(new UserBean(counter.incrementAndGet(),"Sam",30, 70000));
		users.add(new UserBean(counter.incrementAndGet(),"Tom",40, 50000));
		users.add(new UserBean(counter.incrementAndGet(),"Jerome",45, 30000));
		users.add(new UserBean(counter.incrementAndGet(),"Silvia",50, 40000));
		return users;
	}

	public void deleteAllUsers() {
		users.clear();
	}

}
