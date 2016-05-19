package com.ai.api;

import com.ai.api.exception.AIException;
import com.ai.api.service.UserService;
import com.ai.commons.beans.customer.ContactBean;
import com.ai.commons.beans.customer.CrmCompanyBean;
import com.ai.commons.beans.customer.GeneralUserViewBean;
import com.ai.commons.beans.customer.OrderBookingBean;
import com.ai.commons.beans.user.GeneralUserBean;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

/**
 * Created by KK on 5/17/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring-controller.xml"})
public class UserServiceImplTest {

    GeneralUserViewBean generalUserViewBean;
    CrmCompanyBean crm;
    GeneralUserBean generalUserBean;
    ContactBean contactBean;
    OrderBookingBean orderBookingBean;

    //@Autowired
    UserService userService;


    @Before
    public void setup() {
        generalUserViewBean = new GeneralUserViewBean();
        crm = new CrmCompanyBean();
        crm.setCompanyNameCN("Prakash Mehta");
        crm.setIndustry("LUE infoservices");
        crm.setCountryRegion("India");
        crm.setAddress1("Rajendra Nagar");
        crm.setCity("Patna 1");
        crm.setPostCode("803201");
        generalUserViewBean.setCompany(crm);

        generalUserBean = new GeneralUserBean();
        generalUserBean.setFollowName("Er.");
        generalUserBean.setLastName("Chandra");
        generalUserBean.setFirstName("Prakash");
        generalUserBean.setPersonalEmail("prakash.mehta003@gmail.com");
        generalUserBean.setLandline("0612-440920");
        generalUserBean.setMobile("9481394832");
        generalUserViewBean.setUser(generalUserBean);

        contactBean = new ContactBean();
        contactBean.setMainPosition("Senior Java Developer");
        contactBean.setAccountingGender("Er.");
        contactBean.setAccountingName("Chandra");
        contactBean.setAccountingGivenName("Mehta");

        orderBookingBean = new OrderBookingBean();
        orderBookingBean.setSendSampleToFactory("no");
        orderBookingBean.setPoCompulsory("no");
        orderBookingBean.setPsiPercentage(0);
        orderBookingBean.setDuproPercentage(0);
        orderBookingBean.setIpcPercentage(0);
        orderBookingBean.setClcPercentage(0);
        orderBookingBean.setPmPercentage(0);
        orderBookingBean.setAllowChangeAql("no");
        orderBookingBean.setCustomizedSampleLevel("no");
        orderBookingBean.setCustAqlLevel("II");
        orderBookingBean.setCriticalDefects("NOT=ALLOWED");
        orderBookingBean.setMajorDefects("300");
        orderBookingBean.setMinorDefects("20");
        orderBookingBean.setMaxMeaDefects("NOT=ALLOWED");


    }

    @Test
    public void getProfileUpdate_test() throws IOException, AIException {
        // UserServiceImpl userService = new UserServiceImpl();
        userService.getProfileUpdate(generalUserViewBean, "002F7C45A47FC2E3C1256F81006893B");


    }

    @Test
    public void getProfileContactUpdate_test() throws IOException, AIException {
        // UserServiceImpl userService = new UserServiceImpl();
        userService.getProfileContactUpdate(generalUserViewBean, contactBean, "002F7C45A47FC2E3C1256F81006893B");
    }

    @Test
    public void getProfileBookingPreferenceUpdate_test() throws IOException, AIException {
        //UserServiceImpl userService = new UserServiceImpl();
        userService.getProfileBookingPreferenceUpdate(orderBookingBean, "002F7C45A47FC2E3C1256F81006893B");
    }

    @Test
    public void testAdd() {
        double result = 3 + 3;
        assert (result == 6);
    }


}