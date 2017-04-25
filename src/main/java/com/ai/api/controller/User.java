/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.ai.api.bean.ApiContactInfoBean;
import com.ai.api.bean.BookingPreferenceBean;
import com.ai.api.bean.CompanyBean;
import com.ai.api.bean.CompanyLogoBean;
import com.ai.api.bean.UserBean;
import com.ai.api.exception.AIException;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.audit.api.ApiEmployeeBean;
import com.ai.commons.beans.customer.DashboardBean;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.ResponseEntity;

/***************************************************************************
 * <PRE>
 * Project Name    : api
 * <p/>
 * Package Name    : com.ai.api.controller.impl
 * <p/>
 * File Name       : User.java
 * <p/>
 * Creation Date   : Mar 16, 2016
 * <p/>
 * Author          : Allen Zhang
 * <p/>
 * Purpose         : Interface for client related resource request
 * <p/>
 * <p/>
 * History         : TODO
 * <p/>
 * </PRE>
 ***************************************************************************/

public interface User {

	ResponseEntity<UserBean> updateUserProfileCompany(String userId, CompanyBean newComp)
			throws IOException, AIException;

	ResponseEntity<UserBean> updateUserProfileContact(String userId, ApiContactInfoBean newContact)
			throws IOException, AIException;

	ResponseEntity<UserBean> updateUserBookingPreference(String userId, BookingPreferenceBean newBookingPref)
			throws IOException, AIException;

	ResponseEntity<UserBean> updateUserBookingPreferredProductFamily(String userId, List<String> newPreferred)
			throws IOException, AIException;

	ResponseEntity<ServiceCallResult> updateUserPassword(String userId, HashMap<String, String> pwdMap)
			throws IOException, AIException;

	// ResponseEntity<String> getCompanyLogoByFile(String userId, String
	// companyId,HttpServletResponse httpResponse);

	ResponseEntity<Map<String, String>> getCompanyLogo(String userId, String companyId);

	// ResponseEntity<String> updateCompanyLogoByFile(String userId, String
	// companyId, HttpServletRequest request);

	ResponseEntity<String> deleteCompanyLogo(String userId, String companyId);

	ResponseEntity<String> updateCompanyLogo(String userId, String companyId, CompanyLogoBean logoBean);

	ResponseEntity<DashboardBean> getUserDashboard(String userId, String startDate, String endDate)
			throws IOException, AIException;

	ResponseEntity<ApiEmployeeBean> getEmployeeProfile(String employeeId, boolean refresh) throws IOException, AIException;

	ResponseEntity<JSONObject> getUserProfile(String userId, boolean refresh) throws IOException, AIException;

	ResponseEntity<ApiCallResult> resetPassword(String login);

	ResponseEntity<ApiCallResult> isFirstLogin(String userId);

	ResponseEntity<String> getQualityManual(String userId, String sessionId, String verifiedCode,
			HttpServletResponse httpResponse);

	ResponseEntity<ApiCallResult> resetPW(String employeeEmail);

	ResponseEntity<ApiCallResult> swaggerLogin(String login, String pw, HttpServletResponse response);

	ResponseEntity<ApiCallResult> swaggerLogout(HttpServletResponse response);

}
