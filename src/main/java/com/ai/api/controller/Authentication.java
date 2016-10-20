/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ai.api.bean.LoginBean;
import com.fasterxml.jackson.core.JsonProcessingException;

/***************************************************************************
 *<PRE>
 *  Project Name    : api
 *
 *  Package Name    : com.ai.api.controller
 *
 *  File Name       : Authentication.java
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

public interface Authentication {

	String userLogin(LoginBean loginBean, HttpServletRequest request,
	                 HttpServletResponse response) throws JsonProcessingException;

	String refreshAPIToken(HttpServletRequest request, HttpServletResponse response)
			throws JsonProcessingException;

	String removeAPIToken(HttpServletRequest request, HttpServletResponse response)
			throws JsonProcessingException;

	String verifyPublicAPIToken(HttpServletRequest request, HttpServletResponse response)
			throws JsonProcessingException;
}
