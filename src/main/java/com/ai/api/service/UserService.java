/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ai.api.bean.BookingPreferenceBean;
import com.ai.api.bean.CompanyBean;
import com.ai.api.bean.CompanyLogoBean;
import com.ai.api.bean.ApiContactInfoBean;
import com.ai.api.bean.EmployeeBean;
import com.ai.api.bean.UserBean;
import com.ai.api.exception.AIException;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.customer.DashboardBean;
import com.ai.commons.beans.legacy.customer.ClientInfoBean;

/***************************************************************************
 * <PRE>
 * Project Name    : api
 * <p>
 * Package Name    : com.ai.api.service
 * <p>
 * File Name       : CustomerService.java
 * <p>
 * Creation Date   : Mar 16, 2016
 * <p>
 * Author          : Allen Zhang
 * <p>
 * Purpose         : TODO
 * <p>
 * <p>
 * History         : TODO
 * <p>
 * </PRE>
 ***************************************************************************/

public interface UserService {

	void removeUserProfileCache(String userId) throws IOException, AIException;

	UserBean getCustById(String userId) throws IOException, AIException;

	UserBean updateCompany(CompanyBean crmCompanyBean, String userId) throws IOException, AIException;

	UserBean updateContact(ApiContactInfoBean newContact, String userId) throws IOException, AIException;

	UserBean updateBookingPreference(BookingPreferenceBean newBookingPref, String user_id)
			throws IOException, AIException;

	UserBean updateBookingPreferredProductFamily(List<String> newPreferred, String user_id)
			throws IOException, AIException;

	ServiceCallResult updateUserPassword(String userId, HashMap<String, String> pwdMap) throws IOException, AIException;

	boolean getCompanyLogoByFile(String userId, String companyId, HttpServletResponse httpResponse);

	boolean updateCompanyLogoByFile(String userId, String companyId, HttpServletRequest request);

	boolean deleteCompanyLogo(String userId, String companyId);

	boolean createNewAccount(ClientInfoBean clientInfoBean) throws IOException, AIException;

	String getCompanyLogo(String companyId);

	boolean updateCompanyLogo(final String userId, final String compId, CompanyLogoBean logoBean);

	String getLoginByUserId(String userId);

	EmployeeBean getEmployeeProfile(String employeeId, boolean refresh);

	DashboardBean getUserDashboard(String userId, String startDate, String endDate) throws IOException, AIException;

	void removeEmployeeProfileCache(String userId);

	ServiceCallResult resetPassword(String login);

	boolean isMasterOfSuperMaster(String superUserId, String masterUserId) throws IOException;

	boolean getQualityManual(String userId,HttpServletResponse httpResponse) throws IOException, AIException;

	ApiCallResult isFirstLogin(String userId) throws Exception;

	ApiCallResult resetPW(String employeeEmail) throws Exception;
}
