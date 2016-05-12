/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.controller.impl;

import com.ai.api.App;
import com.ai.api.controller.UserDemo;
import com.ai.api.exception.AIException;
import com.ai.api.model.UserDemoBean;
import com.ai.api.model.UserProfileBookingPreference;
import com.ai.api.model.UserProfileContactRequest;
import com.ai.api.model.UserProfileRequest;
import com.ai.api.service.ServiceConfig;
import com.ai.api.service.UserServiceDemo;
import com.ai.commons.annotation.Secured;
import com.ai.commons.beans.customer.*;
import com.ai.commons.beans.user.GeneralUserBean;
import com.ai.consts.CommonAuthConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;

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

@RestController
public class UserDemoImpl implements UserDemo {

    @Autowired
    UserServiceDemo userDemoService;  //Service which will do all data retrieval/manipulation work

//	@Value("${user.service.url}")
//	private String userServiceUrl;
//
//
//	@Value("${mail.password}")
//	private String mailPwd;

    @Autowired
    @Qualifier("serviceConfig")
    private ServiceConfig config;

    @Autowired
    @Qualifier("app")
    private App app;


    //-------------------Retrieve All Users--------------------------------------------------------

    //	@Secured({CommonAuthConstants.ROLE.READER})
    @RequestMapping(value = "/userdemo/", method = RequestMethod.GET)
    public ResponseEntity<List<UserDemoBean>> listAllUsers() {
//		System.out.println("user: " + userServiceUrl);
        System.out.println("user url: " + config.getBaseURL());

        System.out.println("app bean url: " + app.returnURL());
        List<UserDemoBean> users = userDemoService.findAllUsers();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    //-------------------Retrieve Single User--------------------------------------------------------

    @Secured({CommonAuthConstants.ROLE.ADMIN})
    @RequestMapping(value = "/userdemo/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
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
    //------------------- User Profile Company Update --------------------------------------------------------

    @RequestMapping(value = "/user/{USER_ID}/profile/company", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateUserProfileCompany(@PathVariable("USER_ID") String USER_ID, @RequestBody UserProfileRequest userProfileRequest) throws IOException, AIException {
        System.out.println("Creating User Choice " + USER_ID);

        GeneralUserViewBean generalUserViewBean = new GeneralUserViewBean();

        CrmCompanyBean crmCompanyBean = new CrmCompanyBean();

        crmCompanyBean.setCompanyNameCN(userProfileRequest.getNameCN());
        crmCompanyBean.setIndustry(userProfileRequest.getIndustry());
        crmCompanyBean.setCountryRegion(userProfileRequest.getCountry());
        crmCompanyBean.setAddress1(userProfileRequest.getAddress());
        crmCompanyBean.setCity(userProfileRequest.getCity());
        crmCompanyBean.setPostCode(userProfileRequest.getPostcode());


        generalUserViewBean.setCompany(crmCompanyBean);

        userDemoService.getProfileUpdate(generalUserViewBean, USER_ID);

        return null;
    }

    //------------------- User Profile Contact Update --------------------------------------------------------

    @RequestMapping(value = "/user/{USER_ID}/profile/contactInfo", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateUserProfileContact(@PathVariable("USER_ID") String USER_ID, @RequestBody UserProfileContactRequest userProfileContactRequest) throws IOException, AIException {
        System.out.println("Creating User Choice " + USER_ID);

        GeneralUserViewBean generalUserViewBean = new GeneralUserViewBean();
        GeneralUserBean generalUserBean = new GeneralUserBean();
        ContactBean contactBean = new ContactBean();

        generalUserBean.setFollowName(userProfileContactRequest.getMainSalutation());
        generalUserBean.setLastName(userProfileContactRequest.getMainFamilyName());
        generalUserBean.setFirstName(userProfileContactRequest.getMainGivenName());
        contactBean.setMainPosition(userProfileContactRequest.getMainPosition());
        generalUserBean.setPersonalEmail(userProfileContactRequest.getMainEmail());
        generalUserBean.setLandline(userProfileContactRequest.getMainPhoneNumber());
        generalUserBean.setMobile(userProfileContactRequest.getMainMobileNumber());

        contactBean.setAccountingGender(userProfileContactRequest.getBillingSalutation());
        contactBean.setAccountingName(userProfileContactRequest.getBillingFamilyName());
        contactBean.setAccountingGivenName(userProfileContactRequest.getBillingGivenName());
        contactBean.setAccountingEmail(userProfileContactRequest.getBillingEmail());
        //contactBean.set(userProfileContactRequest.getBillingEmail());
        generalUserViewBean.setUser(generalUserBean);

        userDemoService.getProfileContactUpdate(generalUserViewBean, contactBean, USER_ID);

        return null;
    }


    //------------------- User Profile Booking Preference Update --------------------------------------------------------

    @RequestMapping(value = "/user/{USER_ID}/profile/preference/booking", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateUserProfileContact(@PathVariable("USER_ID") String USER_ID, @RequestBody UserProfileBookingPreference userProfileBookingPreference) throws IOException, AIException {
        System.out.println("Creating User Choice " + USER_ID);

        OrderBookingBean orderBookingBean = new OrderBookingBean();

        orderBookingBean.setSendSampleToFactory(userProfileBookingPreference.getShouldSendRefSampleToFactory());
        orderBookingBean.setPoCompulsory(userProfileBookingPreference.getIsPoMandatory());
        orderBookingBean.setPsiPercentage(userProfileBookingPreference.getMinQuantityToBeReadyPsiPercentage());
        orderBookingBean.setDuproPercentage(userProfileBookingPreference.getMinQuantityToBeReadyDuproPercentage());
        orderBookingBean.setIpcPercentage(userProfileBookingPreference.getMinQuantityToBeReadyIpcPercentage());
        orderBookingBean.setClcPercentage(userProfileBookingPreference.getMinQuantityToBeReadyClcPercentage());
        orderBookingBean.setPmPercentage(userProfileBookingPreference.getMinQuantityToBeReadyPmPercentage());
        orderBookingBean.setAllowChangeAql(userProfileBookingPreference.getAleCanModify());
        orderBookingBean.setCustomizedSampleLevel(userProfileBookingPreference.getAqlCustomDefaultSampleLevel());
        orderBookingBean.setCustAqlLevel(userProfileBookingPreference.getUseCustomAQL());
        orderBookingBean.setCriticalDefects(userProfileBookingPreference.getCustomAQLCriticalDefects());
        orderBookingBean.setMajorDefects(userProfileBookingPreference.getCustomAQLMajorDefects());
        orderBookingBean.setMinorDefects(userProfileBookingPreference.getCustoMAQLMinorDefects());
        orderBookingBean.setMaxMeaDefects(userProfileBookingPreference.getCustoMAQLMaxMeasurementDefects());

        userDemoService.getProfileBookingPreferenceUpdate(orderBookingBean, USER_ID);

        return null;
    }
}
