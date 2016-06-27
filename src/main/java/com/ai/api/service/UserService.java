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

import com.ai.api.bean.BookingPreferenceBean;
import com.ai.api.bean.CompanyBean;
import com.ai.api.bean.ContactInfoBean;
import com.ai.api.exception.AIException;
import com.ai.api.bean.UserBean;
import com.ai.commons.beans.ServiceCallResult;

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

    boolean updateCompany(CompanyBean crmCompanyBean, String userId) throws IOException, AIException;

    boolean updateContact(ContactInfoBean newContact, String userId) throws IOException, AIException;

	boolean updateBookingPreference(BookingPreferenceBean newBookingPref, String user_id) throws IOException, AIException;

	boolean updateBookingPreferredProductFamily(List<String> newPreferred, String user_id);

    ServiceCallResult updateUserPassword(String userId, HashMap<String, String> pwdMap) throws IOException, AIException;
    ServiceCallResult getUserSupplierById(String userId) throws IOException, AIException;
}
