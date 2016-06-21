/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.dao;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

	ServiceCallResult userLogin(String username, String password, String userType, String accessToken);

//	ServiceCallResult employeeAccountLogin(String username, String password, String tokenCategory);

	ServiceCallResult refreshAPIToken(Map<String, String> data, HttpServletRequest request, HttpServletResponse response);

	ServiceCallResult removeAPIToken(HttpServletRequest request, HttpServletResponse response);
}
