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
import com.ai.api.model.UserProfileBookingPreference;
import com.ai.api.model.UserProfileCompanyRequest;
import com.ai.api.model.UserProfileContactRequest;
import com.ai.api.service.UserService;
import com.ai.commons.beans.customer.ContactBean;
import com.ai.commons.beans.customer.CrmCompanyBean;
import com.ai.commons.beans.customer.GeneralUserViewBean;
import com.ai.commons.beans.customer.OrderBookingBean;
import com.ai.commons.beans.user.GeneralUserBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
//import org.apache.http.impl.client.HttpClientBuilder;
//import javax.ws.rs.core.HttpHeaders;

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


    //------------------- User Profile Company Update --------------------------------------------------------


    @RequestMapping(value = "/user/{USER_ID}/profile/company", method = RequestMethod.POST)
    public ResponseEntity<Void> updateUserProfileCompany(@PathVariable("USER_ID") String USER_ID, @RequestBody UserProfileCompanyRequest userProfileCompanyRequest) throws IOException, AIException {
        System.out.println("Creating User Choice " + USER_ID);

        CrmCompanyBean crmCompanyBean = new CrmCompanyBean();

        crmCompanyBean.setCompanyNameCN(userProfileCompanyRequest.getNameCN());
        crmCompanyBean.setIndustry(userProfileCompanyRequest.getIndustry());
        crmCompanyBean.setCountryRegion(userProfileCompanyRequest.getCountry());
        crmCompanyBean.setAddress1(userProfileCompanyRequest.getAddress());
        crmCompanyBean.setCity(userProfileCompanyRequest.getCity());
        crmCompanyBean.setPostCode(userProfileCompanyRequest.getPostcode());

        userService.getProfileUpdate(crmCompanyBean, USER_ID);

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

        userService.getProfileContactUpdate(generalUserViewBean, contactBean, USER_ID);


        return null;
    }


    //------------------- User Profile Booking Preference Update --------------------------------------------------------


    @RequestMapping(value = "/user/{USER_ID}/profile/preference/booking/", method = RequestMethod.PUT)
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

        userService.getProfileBookingPreferenceUpdate(orderBookingBean, USER_ID);

        return null;
    }

}
