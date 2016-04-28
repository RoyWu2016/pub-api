/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.service.impl;

import java.io.IOException;

import com.ai.api.bean.AqlAndSamplingSizeBean;
import com.ai.api.bean.BillingBean;
import com.ai.api.bean.BookingBean;
import com.ai.api.bean.CompanyBean;
import com.ai.api.bean.ContactInfoBean;
import com.ai.api.bean.CustomAQLBean;
import com.ai.api.bean.MainBean;
import com.ai.api.bean.MinQuantityToBeReadyBean;
import com.ai.api.bean.PreferencesBean;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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

//    @Override
//    public UserBean getByLogin(String login) throws IOException, AIException {
//        //TODO: find customer by login, hard code it for now
//
//        String customer_id = customerDao.getCustomerIdByCustomerLogin(login);
//        String url = config.getCustomerServiceUrl() + "/customer/" + customer_id;
//
//        System.out.println("will get from: " + url);
//
//        //TODO: pass session with USM
//
//        GetRequest request = GetRequest.newInstance().setUrl(url);
//        ServiceCallResult result = HttpUtil.issueGetRequest(request);
//
//        CustomerBean customer;
//
//        UserBean user = new UserBean();
//        if (result.getStatusCode() == 200 || result.getStatusCode() == 202) {
//            customer = JsonUtil.mapToObject(result.getResponseString(),
//                    CustomerBean.class);
//
//
//            user.setUserId(customer.getCustomerId());
//            user.setLogin(login);
//            user.setClientId(customer.getOverview().getClientId());
//            user.setSalesInCharge(customer.getOverview().getSic());
//            user.setCompanyType(customer.getOverview().getCompanyType());
//            user.setBusinessUnit(customer.getOverview().getBusinessUnit());
//
//            ContactBean contactBean = new ContactBean();
//
//            contactBean.setFamily_name(customer.getContact().getMainFamilyName());
//            contactBean.setGiven_name(customer.getContact().getMainGivenName());
//            contactBean.setGender(customer.getContact().getMainGender());
//            contactBean.setEmail(customer.getContact().getMainEmail());
//            contactBean.setPosition(customer.getContact().getMainPosition());
//            contactBean.setTelephone(customer.getContact().getAccountingTelephone());
//            contactBean.setMobile(customer.getContact().getMainMobile());
//            contactBean.setFax(customer.getContact().getMainFax());
//            user.setContact(contactBean);
//
//            CompanyInfoBean companyInfoBean = new CompanyInfoBean();
//
//            companyInfoBean.setIndustry(customer.getCompanyInfo().getIndustry());
//            companyInfoBean.setAddress(customer.getCompanyInfo().getAddress());
//            companyInfoBean.setCity(customer.getCompanyInfo().getCity());
//            companyInfoBean.setPostCode(customer.getCompanyInfo().getPostCode());
//            companyInfoBean.setCountry(customer.getCompanyInfo().getCountry());
//            companyInfoBean.setProvince(customer.getCompanyInfo().getProvince());
//            companyInfoBean.setDecisionCountry(customer.getCompanyInfo().getDecisionCountry());
//            companyInfoBean.setSalesTurnover(customer.getCompanyInfo().getSalesTurnover());
//            companyInfoBean.setPurchaseAmount(customer.getCompanyInfo().getPurchaseAmount());
//            companyInfoBean.setCompanyId(customer.getCompanyInfo().getCompanyId());
//            companyInfoBean.setCompanyType(customer.getCompanyInfo().getCompanyType());
//            companyInfoBean.setCompanyName(customer.getCompanyInfo().getCompanyName());
//            companyInfoBean.setCompanyCnName(customer.getCompanyInfo().getCompanyNameCn());
//            companyInfoBean.setCityId(customer.getCompanyInfo().getCityId());
//            companyInfoBean.setCountryId(customer.getCompanyInfo().getCountryId());
//            companyInfoBean.setDecisionCountryId(customer.getCompanyInfo().getDecisionCountryId());
//            companyInfoBean.setWebsite(customer.getCompanyInfo().getWebsite());
//            companyInfoBean.setEmployeeNbr(customer.getCompanyInfo().getEmployeeNb());
//            user.setCompanyInfo(companyInfoBean);
//
//            ProductFamilyBean productfamilyBean = new ProductFamilyBean();
//
//            productfamilyBean.setProductTypeSelection(customer.getProductFamily().getHowToChooseProType());
//            productfamilyBean.setRememberSelectdFamily(customer.getProductFamily().getRememberSelFamily());
//            productfamilyBean.setProductFamilyInfo(customer.getProductFamily().getProductFamilyInfo());
//
//            List<RelevantCategoryInfoBean> list = new ArrayList<RelevantCategoryInfoBean>();
//
//            List<com.ai.commons.beans.customer.RelevantCategoryInfoBean> beanls = customer.getProductFamily().getRelevantCategoryInfo();
//            for (com.ai.commons.beans.customer.RelevantCategoryInfoBean rcbean : beanls) {
//
//                RelevantCategoryInfoBean relevantcatinfoBean = new RelevantCategoryInfoBean();
//                relevantcatinfoBean.setFavouriteFamily(rcbean.getFavCategory());
//                relevantcatinfoBean.setFavouriteCategory(rcbean.getFavCategory());
//                relevantcatinfoBean.setFavouriteSeq(rcbean.getFavSeq());
//                list.add(relevantcatinfoBean);
//                productfamilyBean.setRelevantCategoryInfo(list);
//
//            }
//
//            user.setProductFamily(productfamilyBean);
//
//            OrderBookingBean orderBookingBean = new OrderBookingBean();
//
//            orderBookingBean.setClcPercentage(customer.getOrderBooking().getClcPercentage());
//            orderBookingBean.setPsiPercentage(customer.getOrderBooking().getPsiPercentage());
//            orderBookingBean.setDuproPercentage(customer.getOrderBooking().getDuproPercentage());
//            orderBookingBean.setIpcPercentage(customer.getOrderBooking().getIpcPercentage());
//            orderBookingBean.setPmPercentage(customer.getOrderBooking().getPmPercentage());
//            orderBookingBean.setClcPercentagePacked(customer.getOrderBooking().getClcPercentagePacked());
//            orderBookingBean.setPsiPercentagePacked(customer.getOrderBooking().getPsiPercentagePacked());
//            orderBookingBean.setDuproPercentagePacked(customer.getOrderBooking().getDuproPercentagePacked());
//            orderBookingBean.setIpcPercentagePacked(customer.getOrderBooking().getIpcPercentagePacked());
//            orderBookingBean.setPmPercentagePacked(customer.getOrderBooking().getPmPercentagePacked());
//            orderBookingBean.setSendModificationMail(customer.getOrderBooking().getSendModificationMail());
//            orderBookingBean.setSendSampleToFactory(customer.getOrderBooking().getSendSampleToFactory());
//            orderBookingBean.setPoCompulsory(customer.getOrderBooking().getPoCompulsory());
//            orderBookingBean.setRequireDropTesting(customer.getOrderBooking().getRequireDropTesting());
//            orderBookingBean.setSharePerferredTests(customer.getOrderBooking().getSharePerferredTests());
//            orderBookingBean.setShareChecklist(customer.getOrderBooking().getShareChecklist());
//            orderBookingBean.setTurnOffAiAccess(customer.getOrderBooking().getTurnOffAIAccess());
//            orderBookingBean.setFavoriteTests(customer.getOrderBooking().getFavoriteTests());
//            orderBookingBean.setAllowChangeAql(customer.getOrderBooking().getAllowChangeAql());
//            orderBookingBean.setCustomAqlLevel(customer.getOrderBooking().getCustAqlLevel());
//            orderBookingBean.setCriticalDefects(customer.getOrderBooking().getCriticalDefects());
//            orderBookingBean.setMajorDefects(customer.getOrderBooking().getMajorDefects());
//            orderBookingBean.setMinorDefects(customer.getOrderBooking().getMinorDefects());
//            orderBookingBean.setMaxMeaDefects(customer.getOrderBooking().getMaxMeaDefects());
//            orderBookingBean.setCustomizedSampleLevel(customer.getOrderBooking().getCustomizedSampleLevel());
//            orderBookingBean.setMeasurementSampleLevel(customer.getOrderBooking().getMeasurementSampleLevel());
//            user.setOrderBooking(orderBookingBean);
//
//            MultiReferenceBookingBean multirefBookingBean = new MultiReferenceBookingBean();
//
//            multirefBookingBean.setApproveReferences(customer.getMultiRefBooking().getApproveReferences());
//            multirefBookingBean.setAskNumberOfReferences(customer.getMultiRefBooking().getAskNumberOfReferences());
//            multirefBookingBean.setNumberOfReferencePerProduct(customer.getMultiRefBooking().getNumberOfRefPerProduct());
//            multirefBookingBean.setNumberOfReferencePerReport(customer.getMultiRefBooking().getNumberOfRefPerReport());
//            multirefBookingBean.setNumberOfReferencePerManday(customer.getMultiRefBooking().getNumberOfRefPerMd());
//            multirefBookingBean.setNumberOfPcsPerReference(customer.getMultiRefBooking().getNumberOfPcsPerRef());
//            multirefBookingBean.setNumberOfReportPerManday(customer.getMultiRefBooking().getNumberOfReportPerMd());
//            multirefBookingBean.setClcNumberOfReports(customer.getMultiRefBooking().getClcNumberOfReports());
//            multirefBookingBean.setClcNumberOfContainer(customer.getMultiRefBooking().getClcNumberOfContainer());
//            multirefBookingBean.setPeoCalculation(customer.getMultiRefBooking().getPeoCalculation());
//            multirefBookingBean.setContainerRate(customer.getMultiRefBooking().getContainerRate());
//            user.setMultiReferenceBooking(multirefBookingBean);
//
//            ExtraBean extraBean = new ExtraBean();
//
//            extraBean.setScreenshotNeeded(customer.getExtra().getIsScreenshotNeeded());
//            extraBean.setActivated(customer.getExtra().getIsActivated());
//            extraBean.setDeactivedDate(customer.getExtra().getDeactivedDate());
//            extraBean.setForce_new(customer.getExtra().getForceNew());
//            extraBean.setReActivedDate(customer.getExtra().getReActivedTime());
//            extraBean.setAiClient(customer.getExtra().getAiClient());
//            extraBean.setIsDeleted(customer.getExtra().getIsDeleted());
//            extraBean.setIsChbClient(customer.getExtra().getIsChb());
//            extraBean.setIsFiClient(customer.getExtra().getIsFI());
//            extraBean.setReminderType(customer.getExtra().getReminderType());
//            extraBean.setSubSicReceiveOrderEmail(customer.getExtra().getSubSicRecOrderEmail());
//            extraBean.setSubSicReceiveReportEmail(customer.getExtra().getSubSicRecReportEmail());
//            extraBean.setIsShowDashboardSurvey(customer.getExtra().getIsShowDashboardSurvey());
//            extraBean.setRememberedProductFamilyKey(customer.getExtra().getRememberedProductFamilyKey());
//            extraBean.setOutstandingDateLogin(customer.getExtra().getOutstandingDateLogin());
//            extraBean.setOutstandingDateCompany(customer.getExtra().getOutstandingDateCompany());
//            extraBean.setIsFirstLoginNp(customer.getExtra().getIsFirstLoginNP());
//            extraBean.setIsDetailedBookingForm(customer.getExtra().getIsDetailedBookingForm());
//            extraBean.setMaxPenalty(customer.getExtra().getMaxPenalty());
//            extraBean.setMaxPenaltyUnit(customer.getExtra().getMaxPenaltyUnit());
//            extraBean.setMaxAveragePenalty(customer.getExtra().getMaxAveragePenalty());
//            extraBean.setMaxAveragePenaltyUnit(customer.getExtra().getMaxAveragePenaltyUnit());
//            extraBean.setIsFbSaveAql(customer.getExtra().getIsFbSaveAQL());
//            extraBean.setIsDisplayNewLayout(customer.getExtra().getIsDisplayNewLayout());
//            extraBean.setKpiFlag(customer.getExtra().getKpiFlag());
//            extraBean.setOrderViewType(customer.getExtra().getOrderViewType());
//            extraBean.setSeenMobileAppPromotionBanner(customer.getExtra().getHasSeenMobAppPromoBanner());
//            extraBean.setFirstTimeLog(customer.getExtra().getFirstTimeLog());
//            extraBean.setRegNp(customer.getExtra().getRegNP());
//            extraBean.setRefer(customer.getExtra().getRefer());
//            extraBean.setUserIp(customer.getExtra().getUserip());
//            extraBean.setIsTestAccount(customer.getExtra().getIsTestAccount());
//            extraBean.setSaveIn(customer.getExtra().getSaveIn());
//            extraBean.setNewMandayRate(customer.getExtra().getNewMDRate());
//            extraBean.setDomainName(customer.getExtra().getDomainName());
//            user.setExtra(extraBean);
//
//            return user;
//        }
//        return null;
//    }

    //***********************25-04 by KK****************

    @Override
    public UserBean getCustByLogin(String login) throws IOException, AIException {

        String customer_id = customerDao.getCustomerIdByCustomerLogin(login);

        String url = config.getCustomerServiceUrl() + "/customer/" + customer_id;

        System.out.println("will get from: " + url);


        //do below for now
        GetRequest request = GetRequest.newInstance().setUrl(url);
        ServiceCallResult result = HttpUtil.issueGetRequest(request);

        CustomerBean customer;

        UserBean cust = new UserBean();
        if (result.getStatusCode() == 200 || result.getStatusCode() == 202) {
            customer = JsonUtil.mapToObject(result.getResponseString(),
                    CustomerBean.class);


            cust.setUserId(customer.getCustomerId());
            cust.setSic(customer.getOverview().getSic());

            PreferencesBean preferencesBean = new PreferencesBean();

            BookingBean bookingbean = new BookingBean();

            bookingbean.setShouldSendRefSampleToFactory(customer.getOrderBooking().getSendSampleToFactory());
            bookingbean.setIsPoMandatory(customer.getOrderBooking().getPoCompulsory());
            bookingbean.setUseQuickFormByDefault(null);

            AqlAndSamplingSizeBean aqlbean = new AqlAndSamplingSizeBean();

            aqlbean.setCanModify(null);
            aqlbean.setCustomDefaultSampleLevel(null);
            aqlbean.setUseCustomAQL(null);

            CustomAQLBean custaqlbean = new CustomAQLBean();

            custaqlbean.setCriticalDefects(null);
            custaqlbean.setMajorDefects(null);
            custaqlbean.setMaxMeasurementDefects(null);
            custaqlbean.setMinorDefects(null);

            aqlbean.setCustomAQL(custaqlbean);

            bookingbean.setAqlAndSamplingSize(aqlbean);

            MinQuantityToBeReadyBean minquantbean = new MinQuantityToBeReadyBean();

            minquantbean.setServiceType(null);
            minquantbean.setMinQty(null);

            bookingbean.setMinQuantityToBeReady(minquantbean);

            preferencesBean.setBooking(bookingbean);

            cust.setPreferences(preferencesBean);


            CompanyBean companybean = new CompanyBean();
            companybean.setAddress(customer.getCompanyInfo().getAddress());
            companybean.setCity(customer.getCompanyInfo().getCity());
            companybean.setIndustry(customer.getCompanyInfo().getIndustry());
            companybean.setPostcode(customer.getCompanyInfo().getPostCode());
            companybean.setName(customer.getCompanyInfo().getCompanyName());
            companybean.setCountry(customer.getCompanyInfo().getCountry());

            cust.setCompany(companybean);

            ContactInfoBean contactinfobean = new ContactInfoBean();

            BillingBean billingbean = new BillingBean();
            billingbean.setIsSameAsMainContact(null);

            contactinfobean.setBilling(billingbean);

            MainBean mainbean = new MainBean();

            mainbean.setPosition(null);
            mainbean.setEmail(null);
            mainbean.setFamilyName(null);
            mainbean.setGivenName(null);
            mainbean.setPhoneNumber(null);
            mainbean.setSalutation(null);
            contactinfobean.setMain(mainbean);

            cust.setContactInfo(contactinfobean);

            return cust;
        }
        return null;
    }

}
