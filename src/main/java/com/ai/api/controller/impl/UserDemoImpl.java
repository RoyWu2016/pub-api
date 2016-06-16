/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.controller.impl;

import java.util.List;

import com.ai.api.controller.UserDemo;
import com.ai.api.model.UserDemoBean;
import com.ai.api.service.UserServiceDemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;

/***************************************************************************
 * <PRE>
 * Project Name    : publicAPI
 * <p>
 * Package Name    : com.ai.api.controller
 * <p>
 * File Name       : User.java
 * <p>
 * Creation Date   : Mar 02, 2016
 * <p>
 * Author          : Allen Zhang
 * <p>
 * Purpose         : expose client user related resources
 * <p>
 * <p>
 * History         :
 * <p>
 * </PRE>
 ***************************************************************************/

@ApiIgnore
@RestController
public class UserDemoImpl implements UserDemo {

    @Autowired
    UserServiceDemo userDemoService;  //Service which will do all data retrieval/manipulation work

    //-------------------Retrieve All Users--------------------------------------------------------

    //	@Secured({CommonAuthConstants.ROLE.READER})
    @RequestMapping(value = "/userdemo/", method = RequestMethod.GET)
    public ResponseEntity<List<UserDemoBean>> listAllUsers() {
        List<UserDemoBean> users = userDemoService.findAllUsers();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    //-------------------Retrieve Single User--------------------------------------------------------

//    @Secured({CommonAuthConstants.ROLE.ADMIN})
//    @ClientAccountTokenCheck
	//, produces = MediaType.APPLICATION_JSON_VALUE
    @RequestMapping(value = "/userdemo/{id}", method = RequestMethod.GET)
    public ResponseEntity<UserDemoBean> getUser(@PathVariable("id") long id) {

        System.out.println("Fetching User with id " + id);
        UserDemoBean user = userDemoService.getById(id);
        if (user == null) {
            System.out.println("User with id " + id + " not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    //-------------------Create a User--------------------------------------------------------

    @RequestMapping(value = "/userdemo/", method = RequestMethod.POST)
    public ResponseEntity<Void> createUser(@RequestBody UserDemoBean user, UriComponentsBuilder ucBuilder) {
        System.out.println("Creating User " + user.getName());

        if (userDemoService.isUserExist(user)) {
            System.out.println("A User with name " + user.getName() + " already exist");
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        userDemoService.saveUser(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }


    //------------------- Update a User --------------------------------------------------------

    @RequestMapping(value = "/userdemo/{id}", method = RequestMethod.PUT)
    public ResponseEntity<UserDemoBean> updateUser(@PathVariable("id") long id, @RequestBody UserDemoBean user) {
        System.out.println("Updating User " + id);

        UserDemoBean currentUser = userDemoService.getById(id);

        if (currentUser == null) {
            System.out.println("User with id " + id + " not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        currentUser.setName(user.getName());
        currentUser.setAge(user.getAge());
        currentUser.setSalary(user.getSalary());

        userDemoService.updateUser(currentUser);
        return new ResponseEntity<>(currentUser, HttpStatus.OK);
    }

    //------------------- Delete a User --------------------------------------------------------

    @RequestMapping(value = "/userdemo/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<UserDemoBean> deleteUser(@PathVariable("id") long id) {
        System.out.println("Fetching & Deleting User with id " + id);

        UserDemoBean user = userDemoService.getById(id);
        if (user == null) {
            System.out.println("Unable to delete. User with id " + id + " not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        userDemoService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    //------------------- Delete All User --------------------------------------------------------

    @RequestMapping(value = "/userdemo/", method = RequestMethod.DELETE)
    public ResponseEntity<UserDemoBean> deleteAllUsers() {
        System.out.println("Deleting All Users");

        userDemoService.deleteAllUsers();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
