/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.controller.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ai.api.bean.BookingPreferenceBean;
import com.ai.api.bean.CompanyBean;
import com.ai.api.bean.CompanyLogoBean;
import com.ai.api.bean.ContactInfoBean;
import com.ai.api.bean.EmployeeBean;
import com.ai.api.bean.UserBean;
import com.ai.api.controller.User;
import com.ai.api.exception.AIException;
import com.ai.api.service.UserService;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.customer.DashboardBean;
import com.ai.commons.beans.legacy.customer.ClientInfoBean;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

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
	UserService userService; // Service which will do all data
								// retrieval/manipulation work

	// @Autowired
	// ApiCallResult callResult;

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
	public ResponseEntity<JSONObject> getUserProfile(@PathVariable("userId") String userId,
			@RequestParam(value = "refresh", defaultValue = "false") boolean refresh) throws IOException, AIException {

		if (userId == null) {
			logger.error("User id can't be null or empty!");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		logger.info("......start getting user profile.......");
		UserBean cust = null;

		try {
			if (!refresh) {
				cust = userService.getCustById(userId);
			} else {
				userService.removeUserProfileCache(userId);
				cust = userService.getCustById(userId);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (cust == null) {
			logger.info("User with id " + userId + " not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		logger.info("......finish getting user profile.......");
		JSONObject result = JSON.parseObject(JSON.toJSONString(cust));
		JSONObject rateObj = result.getJSONObject("rate");
		rateObj.remove("countryPricingRates");
		rateObj.remove("labTestRate");
		rateObj.remove("createTime");
		rateObj.remove("createTime");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/company", method = RequestMethod.PUT)
	public ResponseEntity<UserBean> updateUserProfileCompany(@PathVariable("userId") String userId,
			@RequestBody CompanyBean newComp) throws IOException, AIException {
		logger.info("updating company for user: " + userId);
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
			@RequestBody ContactInfoBean newContact) throws IOException, AIException {
		logger.info("updating User contact " + userId);
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
			@RequestBody BookingPreferenceBean newBookingPref) throws IOException, AIException {
		logger.info("Updating User booking preference: " + userId);

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
			@RequestBody List<String> newPreferred) throws IOException, AIException {
		logger.info("Updating User preferred product family: " + userId);

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
			@RequestBody HashMap<String, String> pwdMap) throws IOException, AIException {
		logger.info("Updating User password! userId: " + userId);

		ServiceCallResult result = userService.updateUserPassword(userId, pwdMap);

		if (result.getStatusCode() == HttpStatus.OK.value()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			logger.info("update user password failed! error code :" + result.getStatusCode() + " || "
					+ result.getResponseString());
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
				result.put("image", imageStr);
			} else {
				result.put("image", "");
			}
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/company/{companyId}/logo", method = RequestMethod.POST)
	public ResponseEntity<String> updateCompanyLogo(@PathVariable("userId") String userId,
			@PathVariable("companyId") String companyId, @RequestBody CompanyLogoBean logoBean) {
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
	public ResponseEntity<String> deleteCompanyLogo(@PathVariable("userId") String userId,
			@PathVariable("companyId") String companyId) {
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
	public ResponseEntity<Boolean> createNewAccount(@RequestBody ClientInfoBean clientInfoBean)
			throws IOException, AIException {
		logger.info("creating a new account . . . . . ");
		if (userService.createNewAccount(clientInfoBean)) {
			return new ResponseEntity<>(true, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/employee/{employeeId}", method = RequestMethod.GET)
	public ResponseEntity<JSONObject> getEmployeeProfile(@PathVariable("employeeId") String employeeId,
			@RequestParam(value = "refresh", defaultValue = "false") boolean refresh) throws IOException, AIException {
		// TODO Auto-generated method stub
		logger.info("getEmployeeProfile employeeId: " + employeeId);
		EmployeeBean cust = userService.getEmployeeProfile(employeeId, refresh);
		if (cust != null) {
			JSONObject object = JSON.parseObject(JSON.toJSONString(cust));
			object.remove("joinDate");
			object.remove("password");
			object.remove("createTime");
			object.remove("updateTime");
			object.remove("criteriaStatusList");
			object.remove("criteriaDepartmentIdList");
			object.remove("criteriaGroupIdList");
			object.remove("criteriaRoleIdList");
			object.remove("reportTos");
			object.remove("modifiedBy");
			object.remove("modifiedDate");
			try {
				object.remove("groups");
				JSONArray roles = object.getJSONArray("roles");
				Map<String,List<String>> result = new HashMap<String,List<String>>();
				for (int i = 0; i < roles.size(); i++) {
					JSONObject role = roles.getJSONObject(i);
					result.put(role.getString("moduleName"), new ArrayList<String>());
					result.get(role.getString("moduleName")).add(role.getString("displayName"));
				}
				object.put("roles",result);
			} catch (Exception e) {
                logger.error("error!! set roles value",e);
			}
			return new ResponseEntity<>(object, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/dashboard", method = RequestMethod.GET)
	public ResponseEntity<DashboardBean> getUserDashboard(@PathVariable("userId") String userId,
			@RequestParam(value = "startDate", required = false, defaultValue = "") String startDate,
			@RequestParam(value = "endDate", required = false, defaultValue = "") String endDate)
			throws IOException, AIException {

		if ("".equals(startDate) && "".equals(endDate)) {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar rightNow = Calendar.getInstance();
			endDate = sf.format(rightNow.getTime());
			rightNow.add(Calendar.MONTH, -3);
			startDate = sf.format(rightNow.getTime());
		}

		DashboardBean result = userService.getUserDashboard(userId, startDate, endDate);
		if (null == result) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
	}

	@Override
	// @TokenSecured
	@RequestMapping(value = "/user/{login}/reset-password", method = RequestMethod.PUT)
	public ResponseEntity<ApiCallResult> resetPassword(@PathVariable("login") String login) {
		logger.info("invoking: " + "/user/" + login + "/reset-password");
		ApiCallResult callResult = new ApiCallResult();

		ServiceCallResult temp = userService.resetPassword(login);
		if (null != temp) {
			if (temp.getStatusCode() == HttpStatus.OK.value() && temp.getReasonPhase().equalsIgnoreCase("OK")) {
				callResult.setContent(temp.getResponseString());
				return new ResponseEntity<>(callResult, HttpStatus.OK);
			} else {
				logger.error("Reset password get error:" + temp.getStatusCode() + ", " + temp.getResponseString());
				callResult.setMessage(temp.getResponseString());
				return new ResponseEntity<>(callResult, HttpStatus.OK);
			}
		} else {
			callResult.setMessage("Get null from internal service call.");
			return new ResponseEntity<>(callResult, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
