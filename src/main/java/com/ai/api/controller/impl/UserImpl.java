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
import java.util.Map;

import com.ai.api.bean.BookingPreferenceBean;
import com.ai.api.bean.CompanyBean;
import com.ai.api.bean.CompanyLogoBean;
import com.ai.api.bean.ContactInfoBean;
import com.ai.api.bean.UserBean;
import com.ai.api.controller.User;
import com.ai.api.exception.AIException;
import com.ai.api.service.UserService;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.legacy.customer.ClientInfoBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * <p>
 * Package Name    : com.ai.api.controller.impl
 * <p>
 * File Name       : UserImpl.java
 * <p>
 * Creation Date   : Mar 16, 2016
 * <p>
 * Author          : Allen Zhang
 * <p>
 * Purpose         : TODO
 * <p>
 * <p>
 * History         : TODO
 * <p>
 * </PRE>
 ***************************************************************************/

@RestController
public class UserImpl implements User {
	private static final Logger logger = LoggerFactory.getLogger(UserImpl.class);

	@Autowired
	UserService userService;  //Service which will do all data retrieval/manipulation work

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
	public ResponseEntity<UserBean> getUserProfileByLogin(@PathVariable("userId") String userId)
			throws IOException, AIException {
		logger.info("......start getting user profile.......");
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

		logger.info("......finish getting user profile.......");
		return new ResponseEntity<>(cust, HttpStatus.OK);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/company", method = RequestMethod.PUT)
	public ResponseEntity<UserBean> updateUserProfileCompany(@PathVariable("userId") String userId,
	                                                         @RequestBody CompanyBean newComp)
			throws IOException, AIException {
		System.out.println("updating company for user: " + userId);
		UserBean cust = userService.updateCompany(newComp, userId);
		if (cust != null) {
			return new ResponseEntity<>(cust, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/contact-info", method = RequestMethod.PUT)
	public ResponseEntity<UserBean> updateUserProfileContact(@PathVariable("userId") String userId,
	                                                         @RequestBody ContactInfoBean newContact)
			throws IOException, AIException {
		System.out.println("updating User contact " + userId);
		UserBean cust = userService.updateContact(newContact, userId);
		if (cust != null) {
			return new ResponseEntity<>(cust, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/preference/booking", method = RequestMethod.PUT)
	public ResponseEntity<UserBean> updateUserBookingPreference(@PathVariable("userId") String userId,
	                                                            @RequestBody BookingPreferenceBean newBookingPref)
			throws IOException, AIException {
		System.out.println("Updating User booking preference: " + userId);

		UserBean cust = userService.updateBookingPreference(newBookingPref, userId);
		if (cust != null) {
			return new ResponseEntity<>(cust, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/preference/booking/preferred-product-families", method = RequestMethod.PUT)
	public ResponseEntity<UserBean> updateUserBookingPreferredProductFamily(@PathVariable("userId") String userId,
	                                                                        @RequestBody List<String> newPreferred)
			throws IOException, AIException {
		System.out.println("Updating User preferred product family: " + userId);

		UserBean cust = userService.updateBookingPreferredProductFamily(newPreferred, userId);
		if (cust != null) {
			return new ResponseEntity<>(cust, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/password", method = RequestMethod.PUT)
	public ResponseEntity<ServiceCallResult> updateUserPassword(@PathVariable("userId") String userId,
	                                                            @RequestBody HashMap<String, String> pwdMap)
			throws IOException, AIException {
		System.out.println("Updating User password: " + userId);

		ServiceCallResult result = userService.updateUserPassword(userId, pwdMap);

		if (result.getStatusCode() == HttpStatus.OK.value()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
		}

	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/company/{companyId}/logo", method = RequestMethod.GET)
	public ResponseEntity<Map<String, String>> getCompanyLogo(@PathVariable("userId") String userId,
	                                             @PathVariable("companyId") String companyId) {
		logger.info("get companyLogo----userId[" + userId + "]companyId[" + companyId + "]");
		Map<String, String> result = new HashMap<String, String>();
		try {
			String imageStr = userService.getCompanyLogo(companyId);
			if (imageStr != null) {
				result.put("image",imageStr);
			} else {
				result.put("image","");
			}
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

//    @Override
//    @TokenSecured
//    @RequestMapping(value = "/user/{userId}/company/{companyId}/file_logo", method = RequestMethod.POST)
//    public ResponseEntity<String> updateCompanyLogoByFile(@PathVariable("userId") String userId, @PathVariable("companyId") String companyId, HttpServletRequest request) {
//		logger.info("update companyLogo----userId["+userId+"]companyId["+companyId+"]");
//		boolean b = false;
//        try {
//            b = userService.updateCompanyLogoByFile(userId, companyId, request);
//        }catch (Exception e){
//            logger.error("",e);
//        }
//        if (b){
//            return new ResponseEntity<>(HttpStatus.OK);
//        }else {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/company/{companyId}/logo", method = RequestMethod.POST)
	public ResponseEntity<String> updateCompanyLogo(@PathVariable("userId") String userId,
	                                                @PathVariable("companyId") String companyId,
	                                                @RequestBody CompanyLogoBean logoBean) {
		logger.info("update companyLogo----userId[" + userId + "]companyId[" + companyId + "]");
		boolean b = false;
		try {
			b = userService.updateCompanyLogo(userId, companyId, logoBean);
		} catch (Exception e) {
			logger.error("", e);
		}
		if (b) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/company/{companyId}/logo", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteCompanyLogo(@PathVariable("userId") String userId, @PathVariable("companyId") String companyId) {
		logger.info("delete companyLogo----userId[" + userId + "]companyId[" + companyId + "]");
		boolean b = false;
		try {
			b = userService.deleteCompanyLogo(userId, companyId);
		} catch (Exception e) {
			logger.error("", e);
		}
		if (b) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@RequestMapping(value = "/user", method = RequestMethod.PUT)
	public ResponseEntity<Boolean> createNewAccount(@RequestBody ClientInfoBean clientInfoBean) throws IOException, AIException {
		logger.info("creating a new account . . . . . ");
		if (userService.createNewAccount(clientInfoBean)) {
			return new ResponseEntity<>(true, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
