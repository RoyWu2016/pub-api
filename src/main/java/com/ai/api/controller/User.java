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

import org.springframework.http.ResponseEntity;

import com.ai.api.bean.BookingPreferenceBean;
import com.ai.api.bean.CompanyBean;
import com.ai.api.bean.CompanyLogoBean;
import com.ai.api.bean.ContactInfoBean;
import com.ai.api.bean.UserBean;
import com.ai.api.exception.AIException;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.customer.DashboardBean;
import com.ai.commons.beans.legacy.customer.ClientInfoBean;
import com.alibaba.fastjson.JSONObject;

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

	ResponseEntity<UserBean> getUserProfile(String login) throws IOException, AIException;

	ResponseEntity<UserBean> updateUserProfileCompany(String userId, CompanyBean newComp)
			throws IOException, AIException;

	ResponseEntity<UserBean> updateUserProfileContact(String userId, ContactInfoBean newContact)
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

	ResponseEntity<Boolean> createNewAccount(ClientInfoBean clientInfoBean) throws IOException, AIException;

	ResponseEntity<String> updateCompanyLogo(String userId, String companyId, CompanyLogoBean logoBean);

	ResponseEntity<JSONObject> getEmployeeProfile(String employeeId) throws IOException, AIException;

	ResponseEntity<JSONObject> isACAUser(String userId) throws IOException, AIException;

	ResponseEntity<DashboardBean> getUserDashboard(String userId, String startDate, String endDate)
			throws IOException, AIException;
}
