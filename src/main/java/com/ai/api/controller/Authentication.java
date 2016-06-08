/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

	String clientAccountLogin(HashMap<String, String> credentials,  HttpServletRequest request,
	                          HttpServletResponse response) throws JsonProcessingException;

	String employeeAccountLogin(HashMap<String, String> credentials,  HttpServletRequest request,
	                          HttpServletResponse response) throws JsonProcessingException;

}
