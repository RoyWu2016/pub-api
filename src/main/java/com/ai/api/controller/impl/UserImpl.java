/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.controller.impl;

import com.ai.api.controller.User;
import com.ai.api.exception.AIException;
import com.ai.api.model.UserBean;
import com.ai.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/***************************************************************************
 * <PRE>
 * Project Name    : api
 * <p>
 * Package Name    : com.ai.api.controller.impl
 * <p>
 * File Name       : UserImpl.java
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

@RestController
public class UserImpl implements User {

    @Autowired
    UserService userService;  //Service which will do all data retrieval/manipulation work

    @Override
    @RequestMapping(value = "/user/{login}/profile", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserBean> getUserProfileByLogin(@PathVariable("login") String login) throws IOException, AIException {
        System.out.println("login: " + login);
        UserBean cust = null;

        try {
            cust = userService.getCustByLogin(login);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AIException e) {
            e.printStackTrace();
        }
        if (cust == null) {
            System.out.println("User with login " + login + " not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(cust, HttpStatus.OK);
    }


}
