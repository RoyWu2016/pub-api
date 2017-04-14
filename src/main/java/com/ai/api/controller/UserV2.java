package com.ai.api.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;

import com.ai.api.bean.ApiContactInfoBean;
import com.ai.api.bean.BookingPreferenceBean;
import com.ai.api.bean.CompanyBean;
import com.ai.api.bean.CompanyLogoBean;
import com.ai.api.exception.AIException;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.legacy.customer.ClientInfoBean;

@SuppressWarnings("rawtypes")
public interface UserV2 {

	ResponseEntity<ApiCallResult> updateUserProfileCompany(String userId, CompanyBean newComp)
			throws IOException, AIException;

	ResponseEntity<ApiCallResult> updateUserProfileContact(String userId, ApiContactInfoBean newContact)
			throws IOException, AIException;

	ResponseEntity<ApiCallResult> updateUserBookingPreference(String userId, BookingPreferenceBean newBookingPref)
			throws IOException, AIException;

	ResponseEntity<ApiCallResult> updateUserBookingPreferredProductFamily(String userId, List<String> newPreferred)
			throws IOException, AIException;

	ResponseEntity<ApiCallResult> updateUserPassword(String userId, HashMap<String, String> pwdMap)
			throws IOException, AIException;

	ResponseEntity<ApiCallResult> getCompanyLogo(String userId, String companyId);

	ResponseEntity<ApiCallResult> deleteCompanyLogo(String userId, String companyId);

	ResponseEntity<ApiCallResult> createNewAccount(ClientInfoBean clientInfoBean) throws IOException, AIException;

	ResponseEntity<ApiCallResult> updateCompanyLogo(String userId, String companyId, CompanyLogoBean logoBean);

	ResponseEntity<ApiCallResult> getUserDashboard(String userId, String startDate, String endDate)
			throws IOException, AIException;

	ResponseEntity<ApiCallResult> getEmployeeProfile(String employeeId, boolean refresh)
			throws IOException, AIException;

	ResponseEntity<ApiCallResult> getUserProfile(String userId, boolean refresh) throws IOException, AIException;

	ResponseEntity<ApiCallResult> resetPassword(String login);

	ResponseEntity<String> getQualityManual(String userId, String sessionId, String verifiedCode,
			HttpServletResponse httpResponse);

	ResponseEntity<ApiCallResult> isFirstLogin(String userId);

	ResponseEntity<ApiCallResult> swaggerLogin(String login, String pw, HttpServletResponse response);

	ResponseEntity<ApiCallResult> swaggerLogout(HttpServletResponse response);

}
