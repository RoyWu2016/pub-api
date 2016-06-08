/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.controller.impl;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ai.api.controller.Authentication;
import com.ai.api.dao.SSOUserServiceDao;
import com.ai.commons.HttpUtil;
import com.ai.commons.beans.ServiceCallResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/***************************************************************************
 *<PRE>
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
 *</PRE>
 ***************************************************************************/

@RestController
public class AuthenticationImpl implements Authentication {


	@Autowired
	SSOUserServiceDao ssoUserServiceDao;  //Service which will do all data retrieval/manipulation work

	@Override
	@RequestMapping(method = RequestMethod.POST, value = "/auth/client-account-token")
	@ResponseBody
	public String clientAccountLogin(
									@RequestBody HashMap<String, String> credentials,
	                                 HttpServletRequest request, HttpServletResponse response)
			throws JsonProcessingException {

		String username = credentials.get("username");
		String password = credentials.get("password");

		ServiceCallResult result = new ServiceCallResult();
		ObjectMapper mapper = new ObjectMapper();

		if ((username != null && username.isEmpty() ) || (password != null && password.isEmpty())) {
			result.setStatusCode(HttpServletResponse.SC_FORBIDDEN);
			result.setResponseString("");
			result.setReasonPhase("username/password empty.");
			return mapper.writeValueAsString(result);

		}

		//check token category
		boolean validateResult = HttpUtil.validatePublicAPICallToken(request);
		if (!validateResult) {
			result.setStatusCode(HttpServletResponse.SC_FORBIDDEN);
			result.setResponseString("");
			result.setReasonPhase("AI API call token not present or invalid for login.");
			return mapper.writeValueAsString(result);
		}

		result = ssoUserServiceDao.clientAccountLogin(username, password, HttpUtil.getPublicAPICallToken(request));
		return mapper.writeValueAsString(result);
	}

	@Override
	@RequestMapping(method = RequestMethod.POST, value = "/auth/employee-account-token")
	@ResponseBody
	public String employeeAccountLogin(@RequestBody HashMap<String, String> credentials,
	                                   HttpServletRequest request, HttpServletResponse response)
			throws JsonProcessingException {
		String username = credentials.get("username");
		String password = credentials.get("password");

		ServiceCallResult result = new ServiceCallResult();
		ObjectMapper mapper = new ObjectMapper();

		if ((username != null && username.isEmpty() ) || (password != null && password.isEmpty())) {
			result.setStatusCode(HttpServletResponse.SC_FORBIDDEN);
			result.setResponseString("");
			result.setReasonPhase("username/password empty.");
			return mapper.writeValueAsString(result);

		}
//		boolean validateResult = HttpUtil.validatePublicAPICallToken(request);
//		if (!validateResult) {
//			result.setStatusCode(HttpServletResponse.SC_FORBIDDEN);
//			result.setResponseString("");
//			result.setReasonPhase("AI API call token not present or invalid for login.");
//			return mapper.writeValueAsString(result);
//		}

		result = ssoUserServiceDao.employeeAccountLogin(username, password, HttpUtil.getPublicAPICallToken(request));
		return mapper.writeValueAsString(result);
	}
}
