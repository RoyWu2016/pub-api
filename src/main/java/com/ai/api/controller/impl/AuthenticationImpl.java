/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.controller.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ai.api.bean.LoginBean;
import com.ai.api.controller.Authentication;
import com.ai.api.dao.SSOUserServiceDao;
import com.ai.api.service.AuthenticationService;
import com.ai.api.service.UserService;
import com.ai.commons.Consts;
import com.ai.commons.HttpUtil;
import com.ai.commons.StringUtils;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.checklist.vo.CKLDefectVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/***************************************************************************
 * <PRE>
 *  Project Name    : api
 *
 *  Package Name    : com.ai.api.controller.impl
 *
 *  File Name       : AuthenticationImpl.java
 *
 *  Creation Date   : Jun 07, 2016
 *
 *  Author          : Allen Zhang
 *
 *  Purpose         : TODO
 *
 *
 *  History         : TODO
 *
 * </PRE>
 ***************************************************************************/

@RestController
@Api(tags = { "Authentication" }, description = "Authentication tokens APIs")
public class AuthenticationImpl implements Authentication {
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationImpl.class);

	@Autowired
	SSOUserServiceDao ssoUserServiceDao; // Service which will do all data
											// retrieval/manipulation work

	@Autowired
	UserService userService;
	@Autowired
	AuthenticationService authenticationService;

	@Override
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/auth/token")
	@ApiOperation(value = "User Login Then Get Api Token", response = String.class)
	public String userLogin(@ApiParam(required = true) @RequestBody LoginBean loginBean, HttpServletRequest request,
			HttpServletResponse response) throws JsonProcessingException {
		logger.info("user login ......");
		// user can be client or employee
		String account = loginBean.getAccount();
		// password should be in MD5 format
		String password = loginBean.getPassword();
		String userType = loginBean.getUserType();
		logger.info("account:" + account);
		logger.info("userType:" + userType);
		ServiceCallResult result = new ServiceCallResult();
		ObjectMapper mapper = new ObjectMapper();

		if ((StringUtils.isBlank(account)) || (StringUtils.isBlank(password))) {
			result.setStatusCode(HttpServletResponse.SC_FORBIDDEN);
			result.setResponseString("");// username and password can not be
											// null
			result.setReasonPhase("username/password empty");
			return mapper.writeValueAsString(result);
		}

		if (!Consts.Http.USER_TYPES.contains(userType)) {
			result.setStatusCode(HttpServletResponse.SC_FORBIDDEN);
			result.setResponseString("");
			result.setReasonPhase("wrong user type: " + userType);
			return mapper.writeValueAsString(result);
		}

		// check api access token
		boolean validateResult = HttpUtil.validatePublicAPICallToken(request);
		logger.info("validate token result :" + validateResult);
		if (!validateResult) {
			result.setStatusCode(HttpServletResponse.SC_FORBIDDEN);
			result.setResponseString("");
			result.setReasonPhase("AI API call token not present or invalid for login.");
			return mapper.writeValueAsString(result);
		}
		logger.info("start to get user from user-service --> verify pw --> generate tokenSession");
		result = authenticationService.userLogin(account, password, userType, request);
		logger.info("user login result: " + result.getResponseString());
		return mapper.writeValueAsString(result);
	}

	@Override
	@ResponseBody
	@RequestMapping(method = RequestMethod.PUT, value = "/auth/refresh-token")
	@ApiOperation(value = "Refresh Api Token", response = String.class)
	public String refreshAPIToken(HttpServletRequest request, HttpServletResponse response)
			throws JsonProcessingException {

		logger.info("refresh token ...........");
		ObjectMapper mapper = new ObjectMapper();
		ServiceCallResult result = ssoUserServiceDao.refreshAPIToken(request, response);
		logger.info("refresh token result: " + result.getResponseString());
		return mapper.writeValueAsString(result);
	}

	@Override
	@ResponseBody
	@RequestMapping(method = RequestMethod.PUT, value = "/auth/remove-token")
	@ApiOperation(value = "Remove Api Token", response = String.class)
	public String removeAPIToken(HttpServletRequest request, HttpServletResponse response)
			throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		logger.info("remove token ...........");
		ServiceCallResult result = authenticationService.removeAPIToken(request, response);
		logger.info("remove token result: " + result.getResponseString());
		return mapper.writeValueAsString(result);
	}

	@Override
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/auth/verify-token")
	@ApiOperation(value = "Verify Public Api Token", response = String.class)
	public String verifyPublicAPIToken(HttpServletRequest request, HttpServletResponse response)
			throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		logger.info("verify token ...........");
		ServiceCallResult result = ssoUserServiceDao.verifyAPIToken(request, response);
		logger.info("verify token result: " + result.getResponseString());
		return mapper.writeValueAsString(result);
	}

}
