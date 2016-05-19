/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.service.impl;

import com.ai.api.bean.*;
import com.ai.api.dao.CustomerDao;
import com.ai.api.exception.AIException;
import com.ai.api.model.UserBean;
import com.ai.api.service.ServiceConfig;
import com.ai.api.service.UserService;
import com.ai.commons.HttpUtil;
import com.ai.commons.JsonUtil;
import com.ai.commons.beans.GetRequest;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.customer.ContactBean;
import com.ai.commons.beans.customer.ExtraBean;
import com.ai.commons.beans.customer.*;
import com.ai.commons.beans.customer.OrderBookingBean;
import com.ai.commons.beans.customer.ProductFamilyBean;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    String comp_id;

    //***********************25-04 by KK****************
    @Override
    public UserBean getCustByLogin(String login) throws IOException, AIException {

        String customer_id = customerDao.getCustomerIdByCustomerLogin(login);
        System.out.println("---**----customer_id--**---" + customer_id);

        //-------------------5-10  by KK  ----------

        String generalUVBeanURL = config.getCustomerServiceUrl() + "/users/" + customer_id;
        GetRequest request = GetRequest.newInstance().setUrl(generalUVBeanURL);
        ServiceCallResult result = HttpUtil.issueGetRequest(request);
        GeneralUserViewBean generalUserBean;
        generalUserBean = JsonUtil.mapToObject(result.getResponseString(), GeneralUserViewBean.class);


        UserBean user = new UserBean();

        if (result.getStatusCode() == 200 || result.getStatusCode() == 202) {
            comp_id = generalUserBean.getCompany().getCompanyId();

            // ------------OverViewBean URL ----------------

            String overviewBeanURL = config.getCustomerServiceUrl() + "/customer/" + comp_id + "/overview";
            GetRequest request1 = GetRequest.newInstance().setUrl(overviewBeanURL);
            ServiceCallResult result1 = HttpUtil.issueGetRequest(request1);
            OverviewBean overviewBean;
            overviewBean = JsonUtil.mapToObject(result1.getResponseString(), OverviewBean.class);


            // ------------ContactBean URL ----------------


            String contactBeanURL = config.getCustomerServiceUrl() + "/customer/" + comp_id + "/contact";
            GetRequest request2 = GetRequest.newInstance().setUrl(contactBeanURL);
            ServiceCallResult result2 = HttpUtil.issueGetRequest(request2);
            com.ai.commons.beans.customer.ContactBean contactBean = new ContactBean();
            contactBean = JsonUtil.mapToObject(result2.getResponseString(), ContactBean.class);


            // ------------OrderBookingBean URL ----------------


            String orderBBeanURL = config.getCustomerServiceUrl() + "/customer/" + comp_id + "/order-booking";
            GetRequest request3 = GetRequest.newInstance().setUrl(orderBBeanURL);
            ServiceCallResult result3 = HttpUtil.issueGetRequest(request3);
            OrderBookingBean orderBookingBean = new OrderBookingBean();
            orderBookingBean = JsonUtil.mapToObject(result3.getResponseString(), OrderBookingBean.class);


            // ------------ExtraBean URL ----------------


            String extraURL = config.getCustomerServiceUrl() + "/customer/" + comp_id + "/extra";
            GetRequest request4 = GetRequest.newInstance().setUrl(extraURL);
            ServiceCallResult result4 = HttpUtil.issueGetRequest(request4);
            com.ai.commons.beans.customer.ExtraBean extrabean = new ExtraBean();
            extrabean = JsonUtil.mapToObject(result4.getResponseString(), ExtraBean.class);


            // ------------ProductFamilyBean URL ----------------


            String productFamilyURL = config.getCustomerServiceUrl() + "/customer/" + comp_id + "/product-family";

            GetRequest request5 = GetRequest.newInstance().setUrl(productFamilyURL);
            ServiceCallResult result5 = HttpUtil.issueGetRequest(request5);
            com.ai.commons.beans.customer.ProductFamilyBean productFamilyBean = new ProductFamilyBean();
            productFamilyBean = JsonUtil.mapToObject(result5.getResponseString(), ProductFamilyBean.class);


            // ------------QualityMmanualBean URL ----------------

            String qualitymanualURL = config.getCustomerServiceUrl() + "/customer/" + comp_id + "/quality-manual";

            GetRequest request6 = GetRequest.newInstance().setUrl(qualitymanualURL);
            ServiceCallResult result6 = HttpUtil.issueGetRequest(request6);
            com.ai.commons.beans.customer.QualityManualBean qualityManualBean = new QualityManualBean();
            qualityManualBean = JsonUtil.mapToObject(result6.getResponseString(), QualityManualBean.class);


            // ------------SysProductCategoryBean URL ----------------

            //String SysProductCategoryURL = config.getCustomerServiceUrl()+"/param-service/p/list-product-category";
            String SysProductCategoryURL = "http://192.168.0.31:8090/param-service/p/list-product-category";

            GetRequest request7 = GetRequest.newInstance().setUrl(SysProductCategoryURL);
            ServiceCallResult result7 = HttpUtil.issueGetRequest(request7);

            JSONArray jsonArray = new JSONArray(result7.getResponseString().toString());
            List<String> id = new ArrayList<String>();
            List<String> name = new ArrayList<String>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                name.add(obj.getString("name"));
                id.add(obj.getString("id"));
            }
            SysProductCategoryBean sysProductCategoryBean = new SysProductCategoryBean();
            sysProductCategoryBean.setId(id);
            sysProductCategoryBean.setName(name);


            //----------------SysProductFamilyBean URL ----------------

            //String SysProductFamilyBeanURL = config.getCustomerServiceUrl()+"/param-service/p/list-product-family";
            String SysProductFamilyBeanURL = "http://192.168.0.31:8090/param-service/p/list-product-family";

            GetRequest request8 = GetRequest.newInstance().setUrl(SysProductFamilyBeanURL);
            ServiceCallResult result8 = HttpUtil.issueGetRequest(request8);

            JSONArray jsonArray1 = new JSONArray(result8.getResponseString().toString());
            List<String> familycatid = new ArrayList<String>();
            List<String> familyid = new ArrayList<String>();
            List<String> familyname = new ArrayList<String>();
            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject obj = jsonArray1.getJSONObject(i);
                familycatid.add(obj.getString("categoryId"));
                familyname.add(obj.getString("name"));
                familyid.add(obj.getString("id"));
            }
            SysProductFamilyBean sysProductFamilyBean = new SysProductFamilyBean();
            sysProductFamilyBean.setCategoryId(familycatid);
            sysProductFamilyBean.setId(familyid);
            sysProductFamilyBean.setName(familyname);



            // ------------Set USerBean Properties ----------------


            user.setId(generalUserBean.getUser().getUserId());
            user.setLogin(generalUserBean.getUser().getLogin());
            user.setSic(overviewBean.getSic());
            user.setStatus(generalUserBean.getUser().getStatusText());


            String status = extrabean.getIsChb();
            String status1 = extrabean.getIsFI();
            String status2 = generalUserBean.getSettingBean().getBusinessUnitText();

            if (status.equalsIgnoreCase("Yes")) {
                user.setBusinessUnit("CHB");
            } else if (status1.equalsIgnoreCase("Yes")) {
                user.setBusinessUnit("AFI");
            } else if (status2.equals("AG")) {
                user.setBusinessUnit("AG");
            } else {
                user.setBusinessUnit("AI");
            }

            // ------------Set CompanyBean Properties ----------------

            CompanyBean comp = new CompanyBean();

            comp.setName(generalUserBean.getCompany().getCompanyName());
            comp.setNameCN(generalUserBean.getCompany().getCompanyNameCN());
            comp.setIndustry(generalUserBean.getCompany().getIndustry());
            comp.setAddress(generalUserBean.getCompany().getAddress1() + " " + generalUserBean.getCompany().getAddress2() + " " + generalUserBean.getCompany().getAddress3());
            comp.setCity(generalUserBean.getCompany().getCity());
            comp.setPostcode(generalUserBean.getCompany().getPostCode());

            user.setCompany(comp);

            // ------------Set ContactInfoBean Properties ----------------
            ContactInfoBean contactInfoBean = new ContactInfoBean();

            MainBean main = new MainBean();

            main.setSalutation(generalUserBean.getUser().getFollowName());
            main.setFamilyName(generalUserBean.getUser().getLastName());
            main.setGivenName(generalUserBean.getUser().getFirstName());
            main.setPosition(contactBean.getMainPosition());
            main.setEmail(generalUserBean.getUser().getPersonalEmail());
            main.setPhoneNumber(generalUserBean.getUser().getLandline());
            main.setMobileNumber(generalUserBean.getUser().getMobile());

            contactInfoBean.setMain(main);

            // ------------Set BillingBean Properties ----------------

            BillingBean billingBean = new BillingBean();

            billingBean.setSalutation(contactBean.getAccountingGender());
            billingBean.setFamilyName(contactBean.getAccountingName());
            billingBean.setGivenName(contactBean.getAccountingGivenName());
            billingBean.setEmail(contactBean.getAccountingEmail());

            String mainSalutation = String.valueOf(main.getSalutation());
            String billSalutation = String.valueOf(billingBean.getSalutation());
            String mainFamilyName = String.valueOf(main.getFamilyName());
            String billFamilyName = String.valueOf(billingBean.getFamilyName());
            String mainGivenName = String.valueOf(main.getGivenName());
            String billGivenName = String.valueOf(billingBean.getGivenName());
            String mainEmail = String.valueOf(main.getEmail());
            String billEmail = String.valueOf(billingBean.getEmail());

            if (mainSalutation.equals(billSalutation) && mainFamilyName.equals(billFamilyName) && mainGivenName.equals(billGivenName) && mainEmail.equals(billEmail)) {
                billingBean.setIsSameAsMainContact("true");
            } else {
            billingBean.setIsSameAsMainContact("false");
            }

            contactInfoBean.setBilling(billingBean);
            user.setContactInfo(contactInfoBean);

            // ------------Set PreferencesBean Properties ----------------

            PreferencesBean preferencesBean = new PreferencesBean();
            BookingBean bookingbean = new BookingBean();

            bookingbean.setUseQuickFormByDefault(extrabean.getIsDetailedBookingForm());
            bookingbean.setShouldSendRefSampleToFactory(orderBookingBean.getSendSampleToFactory());
            bookingbean.setIsPoMandatory(orderBookingBean.getPoCompulsory());


            MinQuantityToBeReadyBean[] minQuantityToBeReadyBean = new MinQuantityToBeReadyBean[5];
            MinQuantityToBeReadyBean minQuantityToBeReadyBean1 = new MinQuantityToBeReadyBean();
            minQuantityToBeReadyBean1.setServiceType("PSI");
            minQuantityToBeReadyBean1.setMinQty(orderBookingBean.getPsiPercentage());

            MinQuantityToBeReadyBean minQuantityToBeReadyBean2 = new MinQuantityToBeReadyBean();
            minQuantityToBeReadyBean2.setServiceType("DUPRO");
            minQuantityToBeReadyBean2.setMinQty(orderBookingBean.getDuproPercentage());

            MinQuantityToBeReadyBean minQuantityToBeReadyBean3 = new MinQuantityToBeReadyBean();
            minQuantityToBeReadyBean3.setServiceType("IPC");
            minQuantityToBeReadyBean3.setMinQty(orderBookingBean.getIpcPercentage());

            MinQuantityToBeReadyBean minQuantityToBeReadyBean4 = new MinQuantityToBeReadyBean();
            minQuantityToBeReadyBean4.setServiceType("CLC");
            minQuantityToBeReadyBean4.setMinQty(orderBookingBean.getClcPercentage());

            MinQuantityToBeReadyBean minQuantityToBeReadyBean5 = new MinQuantityToBeReadyBean();
            minQuantityToBeReadyBean5.setServiceType("PM");
            minQuantityToBeReadyBean5.setMinQty(orderBookingBean.getPmPercentage());

            minQuantityToBeReadyBean[0] = minQuantityToBeReadyBean1;
            minQuantityToBeReadyBean[1] = minQuantityToBeReadyBean2;
            minQuantityToBeReadyBean[2] = minQuantityToBeReadyBean3;
            minQuantityToBeReadyBean[3] = minQuantityToBeReadyBean4;
            minQuantityToBeReadyBean[4] = minQuantityToBeReadyBean5;

            bookingbean.setMinQuantityToBeReady(minQuantityToBeReadyBean);

            // ------------Set AqlAndSamplingSizeBean Properties ----------------

            AqlAndSamplingSizeBean aqlAndSamplingSizeBean = new AqlAndSamplingSizeBean();

            String allowaql = orderBookingBean.getAllowChangeAql();

            if (allowaql.equals("1")) {
                aqlAndSamplingSizeBean.setCanModify("true");
            } else {
                aqlAndSamplingSizeBean.setCanModify("false");
            }

            aqlAndSamplingSizeBean.setCustomDefaultSampleLevel(orderBookingBean.getCustomizedSampleLevel());
            CustomAQLBean customAQLBean = new CustomAQLBean();

            String usecustomeaql = orderBookingBean.getCustAqlLevel();

            if (usecustomeaql.equals("yes")) {
                aqlAndSamplingSizeBean.setUseCustomAQL("true");
                customAQLBean.setCriticalDefects(orderBookingBean.getCriticalDefects());
                customAQLBean.setMajorDefects(orderBookingBean.getMajorDefects());
                customAQLBean.setMinorDefects(orderBookingBean.getMinorDefects());
                customAQLBean.setMaxMeasurementDefects(orderBookingBean.getMaxMeaDefects());
            } else {
                aqlAndSamplingSizeBean.setUseCustomAQL("false");
                customAQLBean.setCriticalDefects(orderBookingBean.getCriticalDefects());
                customAQLBean.setMajorDefects("0");
                customAQLBean.setMinorDefects("0");
                customAQLBean.setMaxMeasurementDefects("0");
            }

            aqlAndSamplingSizeBean.setCustomAQL(customAQLBean);
            bookingbean.setAqlAndSamplingSize(aqlAndSamplingSizeBean);

            // ------------Set PreferredProductFamilies Properties ----------------

            PreferredProductFamilies preferredProductFamilies = new PreferredProductFamilies();

            int a = productFamilyBean.getRelevantCategoryInfo().size();
            PreferredProductFamilies[] preferredProductFamiliesarray = new PreferredProductFamilies[a];

            System.out.println("Product Family Data Size : " + a);

            for (int i = 0; i < a; i++) {
                preferredProductFamilies.setProductCategoryId(productFamilyBean.getRelevantCategoryInfo().get(i).getFavCategory());
                preferredProductFamilies.setProductFamilyId(productFamilyBean.getRelevantCategoryInfo().get(i).getFavFamily());
                String catid = productFamilyBean.getRelevantCategoryInfo().get(i).getFavCategory();
                for (int j = 0; j < id.size(); j++) {

                    String catname = sysProductCategoryBean.getId().get(j);
                    if (catid.equals(catname)) {
                        preferredProductFamilies.setProductCategoryName(sysProductCategoryBean.getName().get(j));
                    }
                }

                for (int k = 0; k < familycatid.size(); k++) {

                    String famcatid = sysProductFamilyBean.getCategoryId().get(k);
                    if (catid.equals(famcatid)) {
                        preferredProductFamilies.setProductFamilyName(sysProductFamilyBean.getName().get(k));
                    }
                }
                preferredProductFamiliesarray[i] = preferredProductFamilies;
            }

            bookingbean.setPreferredProductFamilies(preferredProductFamiliesarray);

            // ------------Set QualityManual Properties ----------------

            QualityManual qualityManual = new QualityManual();
            qualityManual.setDocType("QUALITY_MANUAL");
            qualityManual.setFilename(qualityManualBean.getQmFileName());
            qualityManual.setPublishDate(qualityManualBean.getQmReleaseDate());
            qualityManual.setUrl(config.getCustomerServiceUrl() + "/customer/" + comp_id + "/quality-manual-file");

            bookingbean.setQualityManual(qualityManual);
            preferencesBean.setBooking(bookingbean);
            user.setPreferencesBean(preferencesBean);

            return user;
        }
        return null;
    }

    //-------------------By kk ---------------------------
    public void getProfileUpdate(GeneralUserViewBean generalUserViewBean, String user_id) throws IOException, AIException {

        System.out.println("--Simpl---NameCN------" + generalUserViewBean.getCompany().getCompanyNameCN());
        System.out.println("-----Industry------" + generalUserViewBean.getCompany().getIndustry());
        System.out.println("-----CountryRegion------" + generalUserViewBean.getCompany().getCountryRegion());
        System.out.println("-----Address1------" + generalUserViewBean.getCompany().getAddress1());
        System.out.println("-----City------" + generalUserViewBean.getCompany().getCity());
        System.out.println("-----PostCode------" + generalUserViewBean.getCompany().getPostCode());
        System.out.println("-----ID-----" + user_id);
//        UserDaoImpl userDao =  new UserDaoImpl();
//        userDao.updateProfileCompany(generalUserViewBean, user_id);
        System.out.println("-----generalUserViewBean-----1" + generalUserViewBean + "---" + user_id + " " + customerDao);
        customerDao.updateProfileCompany(generalUserViewBean, user_id);
    }

    public void getProfileContactUpdate(GeneralUserViewBean generalUserViewBean, ContactBean contactBean, String user_id) throws IOException, AIException {
        customerDao.updateProfileContact(generalUserViewBean, contactBean, user_id);
    }

    public void getProfileBookingPreferenceUpdate(OrderBookingBean orderBookingBean, String user_id) throws IOException, AIException {
        System.out.println("-----orderBookingBean-----" + orderBookingBean + "---" + user_id);
        customerDao.updateBookingPreference(orderBookingBean, user_id);
    }

}