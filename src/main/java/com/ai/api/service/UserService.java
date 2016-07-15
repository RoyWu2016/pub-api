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

import com.ai.api.bean.*;
import com.ai.api.exception.AIException;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.legacy.customer.ClientInfoBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    UserBean getCustById(String userId) throws IOException, AIException;

    UserBean updateCompany(CompanyBean crmCompanyBean, String userId) throws IOException, AIException;

    UserBean updateContact(ContactInfoBean newContact, String userId) throws IOException, AIException;

	UserBean updateBookingPreference(BookingPreferenceBean newBookingPref, String user_id) throws IOException, AIException;

	UserBean updateBookingPreferredProductFamily(List<String> newPreferred, String user_id) throws IOException, AIException;

    ServiceCallResult updateUserPassword(String userId, HashMap<String, String> pwdMap) throws IOException, AIException;

    boolean getCompanyLogo(String userId,String companyId,HttpServletResponse httpResponse);

    boolean updateCompanyLogo(String userId,String companyId,HttpServletRequest request);

    boolean deleteCompanyLogo(String userId,String companyId);

    boolean createNewAccount(ClientInfoBean clientInfoBean) throws IOException, AIException;
}
