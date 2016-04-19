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
import com.ai.api.service.CustomerService;
import com.ai.api.service.ServiceConfig;
import com.ai.commons.HttpUtil;
import com.ai.commons.JsonUtil;
import com.ai.commons.beans.GetRequest;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.customer.CustomerBean;
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
public class CustomerServiceImpl implements CustomerService {

    //TODO: fix the injection here
//	@Value("${customer.service.url}")
//	private String customerServiceUrl;

    @Autowired
    @Qualifier("serviceConfig")
    private ServiceConfig config;

    @Autowired
    @Qualifier("customerDao")
    private CustomerDao customerDao;

    @Override
    public UserBean getByLogin(String login) throws IOException, AIException {
        //TODO: find customer by login, hard code it for now


        //String customer_id = "0273AC1639A6254248257DCE0012DC40";
        String customer_id = customerDao.getCustomerIdByCustomerLogin(login);

//		String url = customerServiceUrl + "/customer/";
        String url = config.getCustomerServiceUrl() + "/customer/" + customer_id;

        System.out.println("will get from: " + url);

        //TODO: pass session with USM
//		final UserSecurityModel usm = (UserSecurityModel) session.getAttribute(SSOConstant.ATTR_USM);
//
//		ServiceCallResult result = HttpUtil.issueGetRequest(url, new HashMap<String, String>() {
//			private static final long serialVersionUID = 2623229072511782593L; {
//				put("Authorization", "Bearer " + usm.getToken());
//			}
//		}, session);

        //do below for now
        GetRequest request = GetRequest.newInstance().setUrl(url);
        ServiceCallResult result = HttpUtil.issueGetRequest(request);

        System.out.println("Result status Code ----->" + result.getStatusCode());
        // System.out.println("Response ----- " + result.getResponseString());

        CustomerBean customer;

        UserBean user = new UserBean();
        if (result.getStatusCode() == 200 || result.getStatusCode() == 202) {
            customer = JsonUtil.mapToObject(result.getResponseString(),
                    CustomerBean.class);


            user.setUserId(customer.getCustomerId());
            user.setLogin(login);
            user.setClientId(customer.getOverview().getClientId());
            user.setSalesInCharge(customer.getOverview().getSic());
            user.setCompanyType(customer.getOverview().getCompanyType());
            user.setBusinessUnit(customer.getOverview().getBusinessUnit());

            ContactBean contactBean = new ContactBean();

            contactBean.setFamily_name(customer.getContact().getMainFamilyName());
            contactBean.setGiven_name(customer.getContact().getMainGivenName());
            contactBean.setGender(customer.getContact().getMainGender());
            contactBean.setEmail(customer.getContact().getMainEmail());
            contactBean.setPosition(customer.getContact().getMainPosition());
            contactBean.setTelephone(customer.getContact().getAccountingTelephone());
            contactBean.setMobile(customer.getContact().getMainMobile());
            contactBean.setFax(customer.getContact().getMainFax());
            user.setContact(contactBean);

            CompanyInfoBean companyInfoBean = new CompanyInfoBean();

            companyInfoBean.setIndustry(customer.getCompanyInfo().getIndustry());
            companyInfoBean.setAddress(customer.getCompanyInfo().getAddress());
            companyInfoBean.setCity(customer.getCompanyInfo().getCity());
            companyInfoBean.setPostCode(customer.getCompanyInfo().getPostCode());
            companyInfoBean.setCountry(customer.getCompanyInfo().getCountry());
            companyInfoBean.setProvince(customer.getCompanyInfo().getProvince());
            companyInfoBean.setDecisionCountry(customer.getCompanyInfo().getDecisionCountry());
            companyInfoBean.setSalesTurnover(customer.getCompanyInfo().getSalesTurnover());
            companyInfoBean.setPurchaseAmount(customer.getCompanyInfo().getPurchaseAmount());
            companyInfoBean.setCompanyId(customer.getCompanyInfo().getCompanyId());
            companyInfoBean.setCompanyType(customer.getCompanyInfo().getCompanyType());
            companyInfoBean.setCompanyName(customer.getCompanyInfo().getCompanyName());
            companyInfoBean.setCompanyCnName(customer.getCompanyInfo().getCompanyNameCn());
            companyInfoBean.setCityId(customer.getCompanyInfo().getCityId());
            companyInfoBean.setCountryId(customer.getCompanyInfo().getCountryId());
            companyInfoBean.setDecisionCountryId(customer.getCompanyInfo().getDecisionCountryId());
            companyInfoBean.setWebsite(customer.getCompanyInfo().getWebsite());
            companyInfoBean.setEmployeeNbr(customer.getCompanyInfo().getEmployeeNb());
            user.setCompanyInfo(companyInfoBean);

            ProductFamilyBean productfamilyBean = new ProductFamilyBean();

            productfamilyBean.setProductTypeSelection(customer.getProductFamily().getHowToChooseProType());
            productfamilyBean.setRememberSelectdFamily(customer.getProductFamily().getRememberSelFamily());
            productfamilyBean.setProductFamilyInfo(customer.getProductFamily().getProductFamilyInfo());

            List<RelevantCategoryInfoBean> list = new ArrayList<RelevantCategoryInfoBean>();

            List<com.ai.commons.beans.customer.RelevantCategoryInfoBean> beanls = customer.getProductFamily().getRelevantCategoryInfo();
            for (com.ai.commons.beans.customer.RelevantCategoryInfoBean rcbean : beanls) {

                RelevantCategoryInfoBean relevantcatinfoBean = new RelevantCategoryInfoBean();
                relevantcatinfoBean.setFavouriteFamily(rcbean.getFavCategory());
                relevantcatinfoBean.setFavouriteCategory(rcbean.getFavCategory());
                relevantcatinfoBean.setFavouriteSeq(rcbean.getFavSeq());
                list.add(relevantcatinfoBean);
                productfamilyBean.setRelevantCategoryInfo(list);

            }

            user.setProductFamily(productfamilyBean);

            OrderBookingBean orderBookingBean = new OrderBookingBean();

            orderBookingBean.setClcPercentage(customer.getOrderBooking().getClcPercentage());
            orderBookingBean.setPsiPercentage(customer.getOrderBooking().getPsiPercentage());
            orderBookingBean.setDuproPercentage(customer.getOrderBooking().getDuproPercentage());
            orderBookingBean.setIpcPercentage(customer.getOrderBooking().getIpcPercentage());
            orderBookingBean.setPmPercentage(customer.getOrderBooking().getPmPercentage());
            orderBookingBean.setClcPercentagePacked(customer.getOrderBooking().getClcPercentagePacked());
            orderBookingBean.setPsiPercentagePacked(customer.getOrderBooking().getPsiPercentagePacked());
            orderBookingBean.setDuproPercentagePacked(customer.getOrderBooking().getDuproPercentagePacked());
            orderBookingBean.setIpcPercentagePacked(customer.getOrderBooking().getIpcPercentagePacked());
            orderBookingBean.setPmPercentagePacked(customer.getOrderBooking().getPmPercentagePacked());
            orderBookingBean.setSendModificationMail(customer.getOrderBooking().getSendModificationMail());
            orderBookingBean.setSendSampleToFactory(customer.getOrderBooking().getSendSampleToFactory());
            orderBookingBean.setPoCompulsory(customer.getOrderBooking().getPoCompulsory());
            orderBookingBean.setRequireDropTesting(customer.getOrderBooking().getRequireDropTesting());
            orderBookingBean.setSharePerferredTests(customer.getOrderBooking().getSharePerferredTests());
            orderBookingBean.setShareChecklist(customer.getOrderBooking().getShareChecklist());
            orderBookingBean.setTurnOffAiAccess(customer.getOrderBooking().getTurnOffAIAccess());
            orderBookingBean.setFavoriteTests(customer.getOrderBooking().getFavoriteTests());
            orderBookingBean.setAllowChangeAql(customer.getOrderBooking().getAllowChangeAql());
            orderBookingBean.setCustomAqlLevel(customer.getOrderBooking().getCustAqlLevel());
            orderBookingBean.setCriticalDefects(customer.getOrderBooking().getCriticalDefects());
            orderBookingBean.setMajorDefects(customer.getOrderBooking().getMajorDefects());
            orderBookingBean.setMinorDefects(customer.getOrderBooking().getMinorDefects());
            orderBookingBean.setMaxMeaDefects(customer.getOrderBooking().getMaxMeaDefects());
            orderBookingBean.setCustomizedSampleLevel(customer.getOrderBooking().getCustomizedSampleLevel());
            orderBookingBean.setMeasurementSampleLevel(customer.getOrderBooking().getMeasurementSampleLevel());
            user.setOrderBooking(orderBookingBean);

            MultiReferenceBookingBean multirefBookingBean = new MultiReferenceBookingBean();

            multirefBookingBean.setApproveReferences(customer.getMultiRefBooking().getApproveReferences());
            multirefBookingBean.setAskNumberOfReferences(customer.getMultiRefBooking().getAskNumberOfReferences());
            multirefBookingBean.setNumberOfReferencePerProduct(customer.getMultiRefBooking().getNumberOfRefPerProduct());
            multirefBookingBean.setNumberOfReferencePerReport(customer.getMultiRefBooking().getNumberOfRefPerReport());
            multirefBookingBean.setNumberOfReferencePerManday(customer.getMultiRefBooking().getNumberOfRefPerMd());
            multirefBookingBean.setNumberOfPcsPerReference(customer.getMultiRefBooking().getNumberOfPcsPerRef());
            multirefBookingBean.setNumberOfReportPerManday(customer.getMultiRefBooking().getNumberOfReportPerMd());
            multirefBookingBean.setClcNumberOfReports(customer.getMultiRefBooking().getClcNumberOfReports());
            multirefBookingBean.setClcNumberOfContainer(customer.getMultiRefBooking().getClcNumberOfContainer());
            multirefBookingBean.setPeoCalculation(customer.getMultiRefBooking().getPeoCalculation());
            multirefBookingBean.setContainerRate(customer.getMultiRefBooking().getContainerRate());
            user.setMultiReferenceBooking(multirefBookingBean);

            ExtraBean extraBean = new ExtraBean();

            extraBean.setScreenshotNeeded(customer.getExtra().getIsScreenshotNeeded());
            extraBean.setActivated(customer.getExtra().getIsActivated());
            extraBean.setDeactivedDate(customer.getExtra().getDeactivedDate());
            extraBean.setForce_new(customer.getExtra().getForceNew());
            extraBean.setReActivedDate(customer.getExtra().getReActivedTime());
            extraBean.setAiClient(customer.getExtra().getAiClient());
            extraBean.setIsDeleted(customer.getExtra().getIsDeleted());
            extraBean.setIsChbClient(customer.getExtra().getIsChb());
            extraBean.setIsFiClient(customer.getExtra().getIsFI());
            extraBean.setReminderType(customer.getExtra().getReminderType());
            extraBean.setSubSicReceiveOrderEmail(customer.getExtra().getSubSicRecOrderEmail());
            extraBean.setSubSicReceiveReportEmail(customer.getExtra().getSubSicRecReportEmail());
            extraBean.setIsShowDashboardSurvey(customer.getExtra().getIsShowDashboardSurvey());
            extraBean.setRememberedProductFamilyKey(customer.getExtra().getRememberedProductFamilyKey());
            extraBean.setOutstandingDateLogin(customer.getExtra().getOutstandingDateLogin());
            extraBean.setOutstandingDateCompany(customer.getExtra().getOutstandingDateCompany());
            extraBean.setIsFirstLoginNp(customer.getExtra().getIsFirstLoginNP());
            extraBean.setIsDetailedBookingForm(customer.getExtra().getIsDetailedBookingForm());
            extraBean.setMaxPenalty(customer.getExtra().getMaxPenalty());
            extraBean.setMaxPenaltyUnit(customer.getExtra().getMaxPenaltyUnit());
            extraBean.setMaxAveragePenalty(customer.getExtra().getMaxAveragePenalty());
            extraBean.setMaxAveragePenaltyUnit(customer.getExtra().getMaxAveragePenaltyUnit());
            extraBean.setIsFbSaveAql(customer.getExtra().getIsFbSaveAQL());
            extraBean.setIsDisplayNewLayout(customer.getExtra().getIsDisplayNewLayout());
            extraBean.setKpiFlag(customer.getExtra().getKpiFlag());
            extraBean.setOrderViewType(customer.getExtra().getOrderViewType());
            extraBean.setSeenMobileAppPromotionBanner(customer.getExtra().getHasSeenMobAppPromoBanner());
            extraBean.setFirstTimeLog(customer.getExtra().getFirstTimeLog());
            extraBean.setRegNp(customer.getExtra().getRegNP());
            extraBean.setRefer(customer.getExtra().getRefer());
            extraBean.setUserIp(customer.getExtra().getUserip());
            extraBean.setIsTestAccount(customer.getExtra().getIsTestAccount());
            extraBean.setSaveIn(customer.getExtra().getSaveIn());
            extraBean.setNewMandayRate(customer.getExtra().getNewMDRate());
            extraBean.setDomainName(customer.getExtra().getDomainName());
            user.setExtra(extraBean);


            return user;
        }
        return null;
    }
}
