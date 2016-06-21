/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.dao.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ai.api.dao.SSOUserServiceDao;
import com.ai.api.service.ServiceConfig;
import com.ai.commons.Consts;
import com.ai.commons.HttpUtil;
import com.ai.commons.beans.ServiceCallResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/***************************************************************************
 *<PRE>
 *  Project Name    : api
 *
 *  Package Name    : com.ai.api.dao.impl
 *
 *  File Name       : SSOUserServiceDaoImpl.java
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
 *</PRE>
 ***************************************************************************/

public class SSOUserServiceDaoImpl implements SSOUserServiceDao {

	private static final Logger LOGGER = Logger.getLogger(SSOUserServiceDaoImpl.class);
	private final ObjectMapper mapper = new ObjectMapper();

	@Autowired
	@Qualifier("serviceConfig")
	private ServiceConfig config;

	@Override
	public ServiceCallResult userLogin(final String username, final String password,
	                                   final String userType, final String accessToken){

		String ssoUserServiceUrl = config.getSsoUserServiceUrl() + "/auth/public-api-token";
		Map<String, String> obj = new HashMap<>();
		obj.put("username", username);
		obj.put("password", password);
		obj.put("userType", userType);
		obj.put(Consts.Http.PUBLIC_API_ACCESS_TOKEN_HEADER, accessToken);
		try {
			ServiceCallResult result = HttpUtil.issuePostRequest(ssoUserServiceUrl, null, obj);
			return mapper.readValue(result.getResponseString(), ServiceCallResult.class);

		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}

		return null;
	}

	@Override
	public ServiceCallResult refreshAPIToken(Map<String, String> data, HttpServletRequest request,
	                                         HttpServletResponse response) {
		String ssoUserServiceUrl = config.getSsoUserServiceUrl() + "/auth/refresh-public-api-token";
		Map<String, String> headers = new HashMap<>();
		headers.put("authorization", request.getHeader("authorization"));
		headers.put("ai-api-access-token", request.getHeader("ai-api-access-token"));
		headers.put("ai-api-refresh-key", request.getHeader("ai-api-refresh-key"));

		try {
			ServiceCallResult result = HttpUtil.issuePostRequest(ssoUserServiceUrl, headers, data);
			return mapper.readValue(result.getResponseString(), ServiceCallResult.class);
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public ServiceCallResult removeAPIToken(HttpServletRequest request, HttpServletResponse response) {
		String ssoUserServiceUrl = config.getSsoUserServiceUrl() + "/auth/remove-public-api-token";
		Map<String, String> headers = new HashMap<>();
		headers.put("authorization", request.getHeader("authorization"));
		headers.put("ai-api-access-token", request.getHeader("ai-api-access-token"));
		headers.put("ai-api-refresh-key", request.getHeader("ai-api-refresh-key"));
		try {
			ServiceCallResult result = HttpUtil.issuePostRequest(ssoUserServiceUrl, headers, "");
			return mapper.readValue(result.getResponseString(), ServiceCallResult.class);
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

//
//	@Override
//	public ServiceCallResult employeeAccountLogin(final String username, final String password,
//	                                              final String tokenCategory) {
//		String ssoUserServiceUrl = config.getSsoUserServiceUrl() + "/auth/employee-account-token";
//		Map<String, String> obj = new HashMap<>();
//		obj.put("username", username);
//		obj.put("password", password);
//		try {
//			ServiceCallResult result = HttpUtil.issuePostRequest(ssoUserServiceUrl, null, obj);
//			return mapper.readValue(result.getResponseString(), ServiceCallResult.class);
//		} catch (IOException e) {
//			LOGGER.error(Arrays.asList(e.getStackTrace()));
//		}
//		return null;
//	}

}
