/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.controller.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.ai.api.bean.*;
import com.ai.api.controller.User;
import com.ai.api.exception.AIException;
import com.ai.api.service.UserService;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.ServiceCallResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/***************************************************************************
 * <PRE>
 * Project Name    : api
 * <p/>
 * Package Name    : com.ai.api.controller.impl
 * <p/>
 * File Name       : UserImpl.java
 * <p/>
 * Creation Date   : Mar 16, 2016
 * <p/>
 * Author          : Allen Zhang
 * <p/>
 * Purpose         : TODO
 * <p/>
 * <p/>
 * History         : TODO
 * <p/>
 * </PRE>
 ***************************************************************************/

@RestController
public class UserImpl implements User {

	@Autowired
	UserService userService;  //Service which will do all data retrieval/manipulation work

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/profile", method = RequestMethod.GET)
	public ResponseEntity<UserBean> getUserProfileByLogin(@PathVariable("userId") String userId)
			throws IOException, AIException {
		UserBean cust = null;

		try {
			cust = userService.getCustById(userId);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (cust == null) {
			System.out.println("User with id " + userId + " not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(cust, HttpStatus.OK);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/profile/company", method = RequestMethod.PUT)
	public ResponseEntity<UserBean> updateUserProfileCompany(@PathVariable("userId") String userId,
	                                                        @RequestBody CompanyBean newComp)
			throws IOException, AIException {
		System.out.println("updating company for user: " + userId);
		if (userService.updateCompany(newComp, userId)) {
			UserBean cust = userService.getCustById(userId);
			return new ResponseEntity<>(cust, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/profile/contactInfo", method = RequestMethod.PUT)
	public ResponseEntity<UserBean> updateUserProfileContact(@PathVariable("userId") String userId,
	                                                        @RequestBody ContactInfoBean newContact)
			throws IOException, AIException {
		System.out.println("updating User contact " + userId);
		UserBean cust = userService.updateContact(newContact, userId);
		if (cust!=null) {
			return new ResponseEntity<>(cust, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}


	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/profile/preference/booking", method = RequestMethod.PUT)
	public ResponseEntity<UserBean> updateUserBookingPreference(@PathVariable("userId") String userId,
	                                                           @RequestBody BookingPreferenceBean newBookingPref)
			throws IOException, AIException {
		System.out.println("Updating User booking preference: " + userId);

		if (userService.updateBookingPreference(newBookingPref, userId)) {
			UserBean cust = userService.getCustById(userId);
			return new ResponseEntity<>(cust, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/profile/preference/booking/preferredProductFamilies", method = RequestMethod.PUT)
	public ResponseEntity<UserBean> updateUserBookingPreferredProductFamily(@PathVariable("userId") String userId,
	                                                           @RequestBody List<String> newPreferred)
			throws IOException, AIException {
		System.out.println("Updating User preferred product family: " + userId);

		if (userService.updateBookingPreferredProductFamily(newPreferred, userId)) {
			UserBean cust = userService.getCustById(userId);
			return new ResponseEntity<>(cust, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/profile/password", method = RequestMethod.PUT)
	public ResponseEntity<ServiceCallResult> updateUserPassword(@PathVariable("userId") String USER_ID,
													  @RequestBody HashMap<String, String> pwdMap)
			throws IOException, AIException {
		System.out.println("Updating User password: " + USER_ID);

		ServiceCallResult result = userService.updateUserPassword(USER_ID, pwdMap);

		if(result.getStatusCode() == HttpStatus.OK.value()){
			return new ResponseEntity<>(result, HttpStatus.OK);
		}else{
			return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
		}

	}

}
