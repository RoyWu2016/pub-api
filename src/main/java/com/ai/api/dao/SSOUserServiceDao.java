/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.dao;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.ServiceCallResult;

/***************************************************************************
 *<PRE>
 *  Project Name    : api
 *
 *  Package Name    : com.ai.api.dao
 *
 *  File Name       : SSOUserServiceDao.java
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

public interface SSOUserServiceDao {

	ServiceCallResult refreshAPIToken(HttpServletRequest request, HttpServletResponse response);

	ServiceCallResult verifyAPIToken(HttpServletRequest request, HttpServletResponse response);

	ServiceCallResult checkAccessHeader(String headerValue);

	String getToken(String authorizationHeader, HttpServletResponse response) throws IOException;

	ApiCallResult isFirstLogin(String login);

	ApiCallResult resetPW(String employeeEmail) throws Exception;
}
