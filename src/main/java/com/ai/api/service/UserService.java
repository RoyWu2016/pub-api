/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.service;

import com.ai.api.exception.AIException;
import com.ai.api.model.UserBean;
import com.ai.commons.beans.customer.ContactBean;
import com.ai.commons.beans.customer.GeneralUserViewBean;
import com.ai.commons.beans.customer.OrderBookingBean;

import java.io.IOException;

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

    void getProfileUpdate(GeneralUserViewBean generalUserViewBean, String user_id) throws IOException, AIException;

    void getProfileContactUpdate(GeneralUserViewBean generalUserViewBean, ContactBean contactBean, String user_id) throws IOException, AIException;

    void getProfileBookingPreferenceUpdate(OrderBookingBean orderBookingBean, String user_id) throws IOException, AIException;


}
