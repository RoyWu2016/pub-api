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
import com.ai.api.bean.ContactInfoBean;
import com.ai.api.bean.EmployeeBean;
import com.ai.api.bean.UserBean;
import com.ai.api.exception.AIException;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.legacy.customer.ClientInfoBean;
import com.ai.commons.beans.payment.GlobalPaymentInfoBean;
import com.ai.commons.beans.payment.PaymentSearchCriteriaBean;
import com.ai.commons.beans.payment.PaymentSearchResultBean;
import com.ai.commons.beans.payment.api.PaymentActionLogBean;

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

	void  removeUserProfileCache(String userId) throws IOException, AIException;

    UserBean getCustById(String userId) throws IOException, AIException;

    UserBean updateCompany(CompanyBean crmCompanyBean, String userId) throws IOException, AIException;

    UserBean updateContact(ContactInfoBean newContact, String userId) throws IOException, AIException;

	UserBean updateBookingPreference(BookingPreferenceBean newBookingPref, String user_id) throws IOException, AIException;

	UserBean updateBookingPreferredProductFamily(List<String> newPreferred, String user_id) throws IOException, AIException;

    ServiceCallResult updateUserPassword(String userId, HashMap<String, String> pwdMap) throws IOException, AIException;

    boolean getCompanyLogoByFile(String userId,String companyId,HttpServletResponse httpResponse);

    boolean updateCompanyLogoByFile(String userId, String companyId, HttpServletRequest request);

    boolean deleteCompanyLogo(String userId,String companyId);

    boolean createNewAccount(ClientInfoBean clientInfoBean) throws IOException, AIException;

	List<PaymentSearchResultBean> searchPaymentList(PaymentSearchCriteriaBean criteria) throws IOException, AIException;

    String getCompanyLogo(String companyId);

    boolean updateCompanyLogo(final String userId, final String compId, CompanyLogoBean logoBean);

    String createProformaInvoice(String userId, String orders);

    boolean reissueProFormaInvoice(String userId, String orders);

    List<GlobalPaymentInfoBean> generateGlobalPayment(String userId, String orders);

    boolean logPaymentAction(String userId, PaymentActionLogBean logBean);

    String getLoginByUserId(String userId);

    EmployeeBean getEmployeeProfile(String employeeId);

//    String getCompanyIdByUserId(String userId)throws IOException, AIException;
}
