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
 * <PRE>
 * Project Name    : publicAPI
 * <p>
 * Package Name    : com.ai.api.controller.impl
 * <p>
 * File Name       : User.java
 * <p>
 * Creation Date   : Mar 02, 2016
 * <p>
 * Author          : Allen Zhang
 * <p>
 * Purpose         : Demo class for initial use
 * <p>
 * <p>
 * History         : TODO
 * <p>
 * </PRE>
 ***************************************************************************/

public interface UserDemo {
    ResponseEntity<UserDemoBean> getUser(long id);
}
