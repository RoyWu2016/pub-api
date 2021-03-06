package com.ai.api.controller.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.ai.api.bean.ApiContactInfoBean;
import com.ai.api.bean.BookingPreferenceBean;
import com.ai.api.bean.CompanyBean;
import com.ai.api.bean.CompanyLogoBean;
import com.ai.api.bean.EmployeeBean;
import com.ai.api.bean.UserBean;
import com.ai.api.bean.consts.ConstMap;
import com.ai.api.exception.AIException;
import com.ai.api.service.UserService;
import com.ai.api.util.AIUtil;
import com.ai.api.util.RedisUtil;
import com.ai.commons.StringUtils;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.audit.api.ApiEmployeeBean;
import com.ai.commons.beans.customer.ClientRegisterBean;
import com.ai.commons.beans.customer.DashboardBean;
import com.ai.commons.beans.customer.RateBean;
import com.ai.commons.beans.legacy.customer.ClientInfoBean;
import com.ai.commons.util.BeanUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = { "User Profile V2" }, description = "User profile V2 APIs")
@SuppressWarnings("rawtypes")
public class UserV2Impl implements com.ai.api.controller.UserV2 {

	private static final Logger logger = LoggerFactory.getLogger(UserV2Impl.class);

	@Autowired
	UserService userService;

	@Value("${swagger-access-map}")
	String swaggerUser;

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/v2/{userId}/company", method = RequestMethod.PUT)
	@ApiOperation(value = "Update User Profile Company API", response = UserBean.class)
	public ResponseEntity<ApiCallResult> updateUserProfileCompany(
			@ApiParam(value = "userId", required = true) @PathVariable("userId") String userId,
			@RequestBody CompanyBean newComp) throws IOException, AIException {
		// TODO Auto-generated method stub
		ApiCallResult result = new ApiCallResult();
		UserBean cust = userService.updateCompany(newComp, userId);
		if (cust != null) {
			result.setContent(cust);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/v2/{userId}/contact-info", method = RequestMethod.PUT)
	@ApiOperation(value = "Update User Profile Contact API", response = UserBean.class)
	public ResponseEntity<ApiCallResult> updateUserProfileContact(
			@ApiParam(value = "userId", required = true) @PathVariable("userId") String userId,
			@RequestBody ApiContactInfoBean newContact) throws IOException, AIException {
		// TODO Auto-generated method stub
		ApiCallResult result = new ApiCallResult();
		UserBean cust = userService.updateContact(newContact, userId);
		if (cust != null) {
			result.setContent(cust);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/v2/{userId}/preference/booking", method = RequestMethod.PUT)
	@ApiOperation(value = "Update User Booking Preference API", response = UserBean.class)
	public ResponseEntity<ApiCallResult> updateUserBookingPreference(
			@ApiParam(value = "userId", required = true) @PathVariable("userId") String userId,
			@RequestBody BookingPreferenceBean newBookingPref) throws IOException, AIException {
		// TODO Auto-generated method stub
		ApiCallResult result = new ApiCallResult();
		UserBean cust = userService.updateBookingPreference(newBookingPref, userId);
		if (cust != null) {
			result.setContent(cust);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/v2/{userId}/preference/booking/preferred-product-families", method = RequestMethod.PUT)
	@ApiOperation(value = "Update User Booking Preference Product Family API", response = UserBean.class)
	public ResponseEntity<ApiCallResult> updateUserBookingPreferredProductFamily(
			@ApiParam(value = "userId", required = true) @PathVariable("userId") String userId,
			@RequestBody List<String> newPreferred) throws IOException, AIException {
		// TODO Auto-generated method stub
		ApiCallResult result = new ApiCallResult();
		UserBean cust = userService.updateBookingPreferredProductFamily(newPreferred, userId);
		if (cust != null) {
			result.setContent(cust);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/v2/{userId}/password", method = RequestMethod.PUT)
	@ApiOperation(value = "Update User Password API", response = ServiceCallResult.class)
	public ResponseEntity<ApiCallResult> updateUserPassword(
			@ApiParam(value = "userId", required = true) @PathVariable("userId") String userId,
			@RequestBody HashMap<String, String> pwdMap) throws IOException, AIException {
		// TODO Auto-generated method stub
		ApiCallResult result = new ApiCallResult();
		ServiceCallResult cust = userService.updateUserPassword(userId, pwdMap);
		if (cust.getStatusCode() == HttpStatus.OK.value()) {
			result.setContent(cust);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			logger.info("update user password failed! error code :" + cust.getStatusCode() + " || "
					+ cust.getResponseString());
			result.setMessage("update user password failed! error code :" + cust.getStatusCode() + " || "
					+ cust.getResponseString());
			return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/v2/{userId}/company/{companyId}/logo", method = RequestMethod.GET)
	@ApiOperation(value = "Get Company Logo API", response = Map.class, responseContainer = "Map")
	public ResponseEntity<ApiCallResult> getCompanyLogo(
			@ApiParam(value = "userId", required = true) @PathVariable("userId") String userId,
			@ApiParam(value = "companyId", required = true) @PathVariable("companyId") String companyId) {
		// TODO Auto-generated method stub
		ApiCallResult rest = new ApiCallResult();
		Map<String, String> result = new HashMap<String, String>();
		try {
			String imageStr = userService.getCompanyLogo(companyId);
			if (imageStr != null) {
				result.put("image", imageStr);
			} else {
				result.put("image", "");
			}
			rest.setContent(result);
			return new ResponseEntity<>(rest, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("", e);
			rest.setMessage("get company logo error: " + e.toString());
			return new ResponseEntity<>(rest, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/v2/company/{companyId}/logo", method = RequestMethod.DELETE)
	@ApiOperation(value = "Delete Company Logo API", response = boolean.class)
	public ResponseEntity<ApiCallResult> deleteCompanyLogo(
			@ApiParam(value = "userId", required = true) @PathVariable("userId") String userId,
			@ApiParam(value = "companyId", required = true) @PathVariable("companyId") String companyId) {
		// TODO Auto-generated method stub
		ApiCallResult rest = new ApiCallResult();
		boolean b = false;
		try {
			b = userService.deleteCompanyLogo(userId, companyId);
		} catch (Exception e) {
			logger.error("", e);
		}
		if (b) {
			rest.setContent(true);
			return new ResponseEntity<>(rest, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@RequestMapping(value = "/user/v2", method = RequestMethod.POST)
	@ApiOperation(value = "Create New Account API", response = boolean.class)
	public ResponseEntity<ApiCallResult> createNewAccount(@RequestBody ClientRegisterBean info)
			throws IOException, AIException {
		ClientInfoBean client = BeanUtil.buildClientInfoBean(info);
		String clientType = info.getClientType();
		ApiCallResult rest = new ApiCallResult();
		if (userService.createNewAccount(client, clientType)) {
			rest.setContent(true);
			return new ResponseEntity<>(rest, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/v2/{userId}/company/{companyId}/logo", method = RequestMethod.POST)
	@ApiOperation(value = "Update Company Logo API", response = boolean.class)
	public ResponseEntity<ApiCallResult> updateCompanyLogo(
			@ApiParam(value = "userId", required = true) @PathVariable("userId") String userId,
			@ApiParam(value = "companyId", required = true) @PathVariable("companyId") String companyId,
			@RequestBody CompanyLogoBean logoBean) {
		ApiCallResult rest = new ApiCallResult();
		boolean b = false;
		try {
			b = userService.updateCompanyLogo(userId, companyId, logoBean);
		} catch (Exception e) {
			logger.error("", e);
		}
		if (b) {
			rest.setContent(b);
			return new ResponseEntity<>(rest, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/v2/{userId}/dashboard", method = RequestMethod.GET)
	@ApiOperation(value = "Get User Dashboard API", response = DashboardBean.class)
	public ResponseEntity<ApiCallResult> getUserDashboard(
			@ApiParam(value = "userId", required = true) @PathVariable("userId") String userId,
			@ApiParam(value = "must be in format like 2016-12-01", required = false) @RequestParam(value = "startDate", required = false, defaultValue = "") String startDate,
			@ApiParam(value = "must be in format like 2016-12-01", required = false) @RequestParam(value = "endDate", required = false, defaultValue = "") String endDate)
			throws IOException, AIException {
		// TODO Auto-generated method stub
		if ("".equals(startDate) && "".equals(endDate)) {
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar rightNow = Calendar.getInstance();
			endDate = sf.format(rightNow.getTime());
			rightNow.add(Calendar.MONTH, -3);
			startDate = sf.format(rightNow.getTime());
		}
		ApiCallResult rest = new ApiCallResult();
		DashboardBean result = userService.getUserDashboard(userId, startDate, endDate);
		if (null == result) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			rest.setContent(result);
			return new ResponseEntity<>(rest, HttpStatus.OK);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/employee/v2/{employeeId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Employee Profile API", response = ApiEmployeeBean.class)
	public ResponseEntity<ApiCallResult> getEmployeeProfile(
			@ApiParam(value = "employeeId", required = true) @PathVariable("employeeId") String employeeId,
			@ApiParam(value = "true or false", required = false) @RequestParam(value = "refresh", defaultValue = "false") boolean refresh)
			throws IOException, AIException {
		// TODO Auto-generated method stub
		ApiCallResult rest = new ApiCallResult();
		EmployeeBean cust = userService.getEmployeeProfile(employeeId, refresh);
		ApiEmployeeBean result = null;
		if (cust != null) {
			try {
				result = ConstMap.convert2ApiEmployeeBean(cust);
				rest.setContent(result);
				return new ResponseEntity<>(rest, HttpStatus.OK);
			} catch (Exception e) {
				rest.setMessage("error!! set roles valu: " + e.toString());
				return new ResponseEntity<>(rest, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/v2/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get UserProfile API", response = UserBean.class)
	public ResponseEntity<ApiCallResult> getUserProfile(
			@ApiParam(value = "userId", required = true) @PathVariable("userId") String userId,
			@ApiParam(value = "true or false", required = false) @RequestParam(value = "refresh", defaultValue = "false") boolean refresh)
			throws IOException, AIException {
		// TODO Auto-generated method stub
		ApiCallResult<Object> rest = new ApiCallResult();
		if (userId == null) {
			rest.setMessage("User id can't be null or empty!");
			return new ResponseEntity<>(rest, HttpStatus.NOT_FOUND);
		}
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
			rest.setMessage("User with id " + userId + " not found");
			return new ResponseEntity<>(rest, HttpStatus.NOT_FOUND);
		}
		RateBean rate = cust.getRate();
		if (null != rate) {
			rate.setCountryPricingRates(null);
			rate.setLabTestRate(null);
			rate.setCreateTime(null);
			rate.setUpdateTime(null);
		}
		rest.setContent(cust);
		return new ResponseEntity<>(rest, HttpStatus.OK);
	}

	@Override
	@RequestMapping(value = "/user/v2/{login}/reset-password", method = RequestMethod.PUT)
	@ApiOperation(value = "Reset Password API", response = String.class)
	public ResponseEntity<ApiCallResult> resetPassword(
			@ApiParam(value = "login", required = true) @PathVariable("login") String login) {
		ApiCallResult callResult = new ApiCallResult();

		ServiceCallResult temp = userService.resetPassword(login);
		if (null != temp) {
			if (temp.getStatusCode() == HttpStatus.OK.value() && temp.getReasonPhase().equalsIgnoreCase("OK")) {
				callResult.setContent(temp.getResponseString());
				return new ResponseEntity<>(callResult, HttpStatus.OK);
			} else {
				logger.error("Reset password get error:" + temp.getStatusCode() + ", " + temp.getResponseString());
				callResult.setMessage(temp.getResponseString());
				return new ResponseEntity<>(callResult, HttpStatus.NOT_FOUND);
			}
		} else {
			callResult.setMessage("Get null from internal service call.");
			return new ResponseEntity<>(callResult, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@RequestMapping(value = "/user/v2/{userId}/quality-manual", method = RequestMethod.GET)
	@ApiOperation(value = "Download User Quality Manual", response = String.class)
	public ResponseEntity<String> getQualityManual(
			@ApiParam(value = "userId", required = true) @PathVariable("userId") String userId,
			@ApiParam(value = "user token sessionId", required = true) @RequestParam("sessionId") String sessionId,
			@ApiParam(value = "last 50 chars of the user token", required = true) @RequestParam("code") String verifiedCode,
			HttpServletResponse httpResponse) {
		try {
			if (AIUtil.verifiedAccess(userId, verifiedCode, sessionId)) {
				boolean b = userService.getQualityManual(userId, httpResponse);
				if (b) {
					return new ResponseEntity<>(HttpStatus.OK);
				}
			} else {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/v2/{userId}/is-first-time-log-in-aca", method = RequestMethod.GET)
	@ApiOperation(value = "Is First Login", response = boolean.class)
	public ResponseEntity<ApiCallResult> isFirstLogin(
			@ApiParam(value = "userId", required = true) @PathVariable("userId") String userId) {
		ApiCallResult apiCallResult = new ApiCallResult();
		try {
			logger.info("check from redis...");
			String existing = RedisUtil.hget("loginUserList", userId);
			if (StringUtils.isNotBlank(existing)) {
				apiCallResult.setContent(false);
				return new ResponseEntity<>(apiCallResult, HttpStatus.OK);
			}
			logger.info("check from service...");
			apiCallResult = userService.isFirstLogin(userId);
			if (null == apiCallResult.getMessage()) {
				RedisUtil.hset("loginUserList", userId, apiCallResult.getContent().toString(),
						RedisUtil.HOUR * 24 * 365 * 10);
				return new ResponseEntity<>(apiCallResult, HttpStatus.OK);
			}
			logger.error("fail from sso-service !" + apiCallResult.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			apiCallResult.setMessage(e.toString());
		}
		return new ResponseEntity<>(apiCallResult, HttpStatus.INTERNAL_SERVER_ERROR);

	}

}
