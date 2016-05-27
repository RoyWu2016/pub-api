/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.service;

import java.io.IOException;
import java.util.List;

import com.ai.api.bean.CompanyBean;
import com.ai.api.exception.AIException;
import com.ai.api.model.UserBean;
import com.ai.commons.beans.customer.ContactBean;
import com.ai.commons.beans.customer.GeneralUserViewBean;
import com.ai.commons.beans.customer.OrderBookingBean;

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

    UserBean getCustByLogin(String login) throws IOException, AIException;

    boolean updateCompany(CompanyBean crmCompanyBean, String userId) throws IOException, AIException;

    boolean updateContact(GeneralUserViewBean generalUserViewBean, ContactBean contactBean, String userId) throws IOException, AIException;

    boolean updateBookingPreference(OrderBookingBean orderBookingBean, String user_id) throws IOException, AIException;

	boolean updateProductCategory(List<String> categoryList, String userId);
}
