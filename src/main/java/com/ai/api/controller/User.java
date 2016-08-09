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

import com.ai.api.bean.*;
import com.ai.api.exception.AIException;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.legacy.customer.ClientInfoBean;
import com.ai.commons.beans.payment.PaymentSearchResultBean;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

	ResponseEntity<UserBean> getUserProfileByLogin(String login) throws IOException, AIException;

	ResponseEntity<UserBean> updateUserProfileCompany(String userId, CompanyBean newComp) throws IOException, AIException;

	ResponseEntity<UserBean> updateUserProfileContact(String userId,
	                                                 ContactInfoBean newContact) throws IOException, AIException;

	ResponseEntity<UserBean> updateUserBookingPreference(String userId,
	                                                    BookingPreferenceBean newBookingPref) throws IOException, AIException;

	ResponseEntity<UserBean> updateUserBookingPreferredProductFamily(String userId,List<String> newPreferred)
			throws IOException, AIException;

	ResponseEntity<ServiceCallResult> updateUserPassword(String userId, HashMap<String, String> pwdMap) throws IOException, AIException;

	ResponseEntity<String> getCompanyLogo(String userId, String companyId,HttpServletResponse httpResponse);

    ResponseEntity<String> updateCompanyLogo(String userId, String companyId,HttpServletRequest request);

	ResponseEntity<String> deleteCompanyLogo(String userId, String companyId);

	ResponseEntity<Boolean> createNewAccount(ClientInfoBean clientInfoBean) throws IOException, AIException;

	ResponseEntity<String> updateBase64CompanyLogo(String userId, String companyId, CompanyLogoBean logoBean);
}
