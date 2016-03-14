/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.controller.impl;

import java.util.List;

import com.ai.api.App;
import com.ai.api.controller.User;
import com.ai.api.model.UserBean;
import com.ai.api.service.ServiceConfig;
import com.ai.api.service.UserService;
import com.ai.commons.annotation.Secured;
import com.ai.consts.CommonAuthConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

/***************************************************************************
 *<PRE>
 *  Project Name    : publicAPI
 *
 *  Package Name    : com.ai.api.controller
 *
 *  File Name       : User.java
 *
 *  Creation Date   : Mar 02, 2016
 *
 *  Author          : Allen Zhang
 *
 *  Purpose         : expose client user related resources
 *
 *
 *  History         :
 *
 *</PRE>
 ***************************************************************************/

@RestController
public class UserImpl implements User {

	@Autowired
	UserService userService;  //Service which will do all data retrieval/manipulation work

	@Value("${user.service.url}")
	private String userServiceUrl;


	@Value("${mail.password}")
	private String mailPwd;

	@Autowired
	@Qualifier("serviceConfig")
	private ServiceConfig config;

	@Autowired
	@Qualifier("app")
	private App app;


	//-------------------Retrieve All Users--------------------------------------------------------

//	@Secured({CommonAuthConstants.ROLE.READER})
	@RequestMapping(value = "/user/", method = RequestMethod.GET)
	public ResponseEntity<List<UserBean>> listAllUsers() {
		System.out.println("user: " + userServiceUrl);
		System.out.println("user url: " + config.getBaseURL());

		System.out.println("app bean url: " + app.returnURL());
		List<UserBean> users = userService.findAllUsers();
		if(users.isEmpty()){
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<>(users, HttpStatus.OK);
	}

	//-------------------Retrieve Single User--------------------------------------------------------

	@Secured({CommonAuthConstants.ROLE.ADMIN})
	@RequestMapping(value = "/user/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserBean> getUser(@PathVariable("id") long id) {
		System.out.println("Fetching User with id " + id);
		UserBean user = userService.getById(id);
		if (user == null) {
			System.out.println("User with id " + id + " not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(user, HttpStatus.OK);
	}


	//-------------------Create a User--------------------------------------------------------

	@RequestMapping(value = "/user/", method = RequestMethod.POST)
	public ResponseEntity<Void> createUser(@RequestBody UserBean user, 	UriComponentsBuilder ucBuilder) {
		System.out.println("Creating User " + user.getName());

		if (userService.isUserExist(user)) {
			System.out.println("A User with name " + user.getName() + " already exist");
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}

		userService.saveUser(user);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(ucBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri());
		return new ResponseEntity<>(headers, HttpStatus.CREATED);
	}


	//------------------- Update a User --------------------------------------------------------

	@RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
	public ResponseEntity<UserBean> updateUser(@PathVariable("id") long id, @RequestBody UserBean user) {
		System.out.println("Updating User " + id);

		UserBean currentUser = userService.getById(id);

		if (currentUser==null) {
			System.out.println("User with id " + id + " not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		currentUser.setName(user.getName());
		currentUser.setAge(user.getAge());
		currentUser.setSalary(user.getSalary());

		userService.updateUser(currentUser);
		return new ResponseEntity<>(currentUser, HttpStatus.OK);
	}

	//------------------- Delete a User --------------------------------------------------------

	@RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<UserBean> deleteUser(@PathVariable("id") long id) {
		System.out.println("Fetching & Deleting User with id " + id);

		UserBean user = userService.getById(id);
		if (user == null) {
			System.out.println("Unable to delete. User with id " + id + " not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		userService.deleteUserById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}


	//------------------- Delete All User --------------------------------------------------------

	@RequestMapping(value = "/user/", method = RequestMethod.DELETE)
	public ResponseEntity<UserBean> deleteAllUsers() {
		System.out.println("Deleting All Users");

		userService.deleteAllUsers();
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
