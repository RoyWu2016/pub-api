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

import com.ai.api.bean.BookingPreferenceBean;
import com.ai.api.bean.CompanyBean;
import com.ai.api.bean.ContactInfoBean;
import com.ai.api.exception.AIException;
import com.ai.api.model.UserBean;
import com.ai.commons.annotation.ClientAccountTokenCheck;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

	ResponseEntity<Boolean> updateUserProfileContact(String USER_ID,
	                                                 ContactInfoBean newContact) throws IOException, AIException;

	ResponseEntity<Boolean> updateUserBookingPreference(String USER_ID,
	                                                    BookingPreferenceBean newBookingPref) throws IOException, AIException;

	@ClientAccountTokenCheck
	@RequestMapping(value = "/user/{userId}/profile/preference/booking/preferredProductFamilies", method = RequestMethod.PUT)
	ResponseEntity<Boolean> updateUserBookingPreferredProductFamily(@PathVariable("userId") String USER_ID,
	                                                                @RequestBody List<String> newPreferred)
			throws IOException, AIException;

	ResponseEntity<Boolean> updateUserPassword(String USER_ID, HashMap<String, String> pwdMap) throws IOException, AIException;

}
