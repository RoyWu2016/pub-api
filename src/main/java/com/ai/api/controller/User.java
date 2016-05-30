/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.controller;

import java.io.IOException;

import com.ai.api.bean.CompanyBean;
import com.ai.api.exception.AIException;
import com.ai.api.model.UserBean;
import com.ai.api.model.UserProfileBookingPreference;
import com.ai.api.model.UserProfileContactRequest;
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

	ResponseEntity<UserBean> getUserProfileByLogin(String login) throws IOException, AIException;

	ResponseEntity<Boolean> updateUserProfileCompany(String userId, CompanyBean newComp) throws IOException, AIException;

	ResponseEntity<Void> updateUserProfileContact(String USER_ID,
	                                              UserProfileContactRequest userProfileContactRequest) throws IOException, AIException;

	ResponseEntity<Void> updateUserProfileContact(String USER_ID,
	                                              UserProfileBookingPreference userProfileBookingPreference) throws IOException, AIException;
}
