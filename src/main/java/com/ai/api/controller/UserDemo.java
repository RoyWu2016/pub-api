/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.controller;

import com.ai.api.model.UserDemoBean;
import org.springframework.http.ResponseEntity;

/***************************************************************************
 *<PRE>
 *  Project Name    : publicAPI
 *
 *  Package Name    : com.ai.api.controller.impl
 *
 *  File Name       : User.java
 *
 *  Creation Date   : Mar 02, 2016
 *
 *  Author          : Allen Zhang
 *
 *  Purpose         : Interface for client related resource request
 *
 *
 *  History         : TODO
 *
 *</PRE>
 ***************************************************************************/

public interface UserDemo {
	ResponseEntity<UserDemoBean> getUser(long id);
}
