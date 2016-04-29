/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.service.impl;

import com.ai.api.bean.BillingBean;
import com.ai.api.bean.CompanyBean;
import com.ai.api.bean.ContactInfoBean;
import com.ai.api.bean.MainBean;
import com.ai.api.dao.CustomerDao;
import com.ai.api.exception.AIException;
import com.ai.api.model.UserBean;
import com.ai.api.service.ServiceConfig;
import com.ai.api.service.UserService;
import com.ai.commons.HttpUtil;
import com.ai.commons.JsonUtil;
import com.ai.commons.beans.GetRequest;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.customer.CustomerBean;

import com.ai.commons.beans.customer.GeneralUserViewBean;
import com.ai.commons.beans.customer.ContactBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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

@Service("customerService")
public class UserServiceImpl implements UserService {

    //TODO: fix the injection here

    @Autowired
    @Qualifier("serviceConfig")
    private ServiceConfig config;

    @Autowired
    @Qualifier("customerDao")
    private CustomerDao customerDao;

    //***********************25-04 by KK****************
    @Override
    public UserBean getCustByLogin(String login) throws IOException, AIException {

        String customer_id = customerDao.getCustomerIdByCustomerLogin(login);

        String url = config.getCustomerServiceUrl() + "/customer/" + customer_id;

        String url1 = config.getCustomerServiceUrl() + "/users/" + customer_id;

        //String url1 = "http://192.168.0.31:8093/customer-service/users/2E85EB892D5BF11AE050A8C00600456F";

        System.out.println("will get from url--: " + url);


        //do below for now
        GetRequest request = GetRequest.newInstance().setUrl(url);
        GetRequest request1 = GetRequest.newInstance().setUrl(url1);
        ServiceCallResult result = HttpUtil.issueGetRequest(request);
        ServiceCallResult result1 = HttpUtil.issueGetRequest(request1);



        CustomerBean customer;
        GeneralUserViewBean generalUserBean;
        ContactBean contactBean;

        UserBean cust = new UserBean();
        if (result.getStatusCode() == 200 || result.getStatusCode() == 202) {
            customer = JsonUtil.mapToObject(result.getResponseString(), CustomerBean.class);

            //System.out.println("-result--------->"+result.getResponseString());


            generalUserBean = JsonUtil.mapToObject(result1.getResponseString(), GeneralUserViewBean.class);
            contactBean = JsonUtil.mapToObject(result.getResponseString(), ContactBean.class);

            //System.out.println("-result1--------->"+result1.getResponseString());

            cust.setId(generalUserBean.getUser().getUserId());
            cust.setLogin(generalUserBean.getUser().getLogin());
            cust.setSic(customer.getOverview().getSic());
            cust.setStatus(generalUserBean.getUser().getStatusText());


            String status = customer.getExtra().getIsChb();
            String status1 = customer.getExtra().getIsFI();
            String status2 = generalUserBean.getSettingBean().getBusinessUnitText();

            if (status.equalsIgnoreCase("Yes")) {
                cust.setBusinessUnit("CHB");
            } else if (status1.equalsIgnoreCase("Yes")) {
                cust.setBusinessUnit("AFI");
            } else if (status2.equals("AG")) {
                cust.setBusinessUnit("AG");
            } else {
                cust.setBusinessUnit("AI");
            }


            CompanyBean comp = new CompanyBean();

            comp.setName(generalUserBean.getCompany().getCompanyName());
            comp.setNameCN(generalUserBean.getCompany().getCompanyNameCN());
            comp.setIndustry(generalUserBean.getCompany().getIndustry());
            comp.setCountry(generalUserBean.getCompany().getCountryRegion());
            comp.setAddress(generalUserBean.getCompany().getAddress1() + " " + generalUserBean.getCompany().getAddress2() + " " + generalUserBean.getCompany().getAddress3());
            comp.setCity(generalUserBean.getCompany().getCity());
            comp.setPostcode(generalUserBean.getCompany().getPostCode());

            cust.setCompany(comp);


            ContactInfoBean contactInfoBean = new ContactInfoBean();

            MainBean main = new MainBean();

            main.setSalutation(generalUserBean.getUser().getFollowName());
            main.setFamilyName(generalUserBean.getUser().getLastName());
            main.setGivenName(generalUserBean.getUser().getFirstName());
            main.setPosition(customer.getContact().getMainPosition());
            main.setEmail(generalUserBean.getUser().getPersonalEmail());
            main.setPhoneNumber(generalUserBean.getUser().getLandline());
            main.setMobileNumber(generalUserBean.getUser().getMobile());

            contactInfoBean.setMain(main);

            BillingBean billingBean = new BillingBean();

            billingBean.setSalutation(customer.getContact().getAccountingGender());
            billingBean.setFamilyName(customer.getContact().getAccountingName());
            billingBean.setGivenName(customer.getContact().getAccountingGivenName());
            billingBean.setEmail(customer.getContact().getAccountingEmail());
            billingBean.setIsSameAsMainContact("false");

            contactInfoBean.setBilling(billingBean);
            cust.setContactInfo(contactInfoBean);

            return cust;
        }
        return null;
    }

}
