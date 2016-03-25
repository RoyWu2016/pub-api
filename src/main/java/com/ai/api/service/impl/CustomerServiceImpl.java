/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.service.impl;

import java.util.ArrayList;
import com.ai.api.bean.*;
import com.ai.api.bean.CompanyInfoBean;
import com.ai.api.bean.ContactBean;
import com.ai.api.bean.ExtraBean;
import com.ai.api.bean.OrderBookingBean;
import com.ai.api.bean.ProductFamilyBean;
import com.ai.api.bean.RelevantCategoryInfoBean;
import com.ai.api.dao.CustomerDao;
import com.ai.api.exception.AIException;
import com.ai.api.model.UserBean;
import com.ai.api.service.CustomerService;
import com.ai.api.service.ServiceConfig;
import com.ai.commons.HttpUtil;
import com.ai.commons.JsonUtil;
import com.ai.commons.beans.GetRequest;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.customer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
        System.out.println("Response ----- " + result.getResponseString());

        CustomerBean customer;

        UserBean user = new UserBean();
        if (result.getStatusCode() == 200 || result.getStatusCode() == 202) {
            customer = JsonUtil.mapToObject(result.getResponseString(),
                    CustomerBean.class);


            user.setUser_id(customer.getCustomerId());
            user.setLogin(login);
            user.setClient_id(customer.getOverview().getSic());
            user.setSales_in_charge(customer.getOverview().getSic());
            user.setCompany_type(customer.getOverview().getCompanyType());
            user.setBusiness_unit(customer.getOverview().getBusinessUnit());

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
            companyInfoBean.setPost_code(customer.getCompanyInfo().getPostCode());
            companyInfoBean.setCountry(customer.getCompanyInfo().getCountry());
            companyInfoBean.setProvince(customer.getCompanyInfo().getProvince());
            companyInfoBean.setDecision_country(customer.getCompanyInfo().getDecisionCountry());
            companyInfoBean.setSales_turnover(customer.getCompanyInfo().getSalesTurnover());
            companyInfoBean.setPurchase_amount(customer.getCompanyInfo().getPurchaseAmount());
            companyInfoBean.setCompany_id(customer.getCompanyInfo().getCompanyId());
            companyInfoBean.setCompany_type(customer.getCompanyInfo().getCompanyType());
            companyInfoBean.setCompany_name(customer.getCompanyInfo().getCompanyName());
            companyInfoBean.setCompany_cn_name(customer.getCompanyInfo().getCompanyNameCn());
            companyInfoBean.setCity_id(customer.getCompanyInfo().getCityId());
            companyInfoBean.setCountry_id(customer.getCompanyInfo().getCountryId());
            companyInfoBean.setDecision_country_id(customer.getCompanyInfo().getDecisionCountryId());
            companyInfoBean.setWebsite(customer.getCompanyInfo().getWebsite());
            companyInfoBean.setEmployee_nb(customer.getCompanyInfo().getEmployeeNb());
            user.setCompany_info(companyInfoBean);

            ProductFamilyBean productfamilyBean = new ProductFamilyBean();

            productfamilyBean.setProduct_type_selection(customer.getProductFamily().getHowToChooseProType());
            productfamilyBean.setRemember_selectd_family(customer.getProductFamily().getRememberSelFamily());
            productfamilyBean.setProduct_family_info(customer.getProductFamily().getProductFamilyInfo());

            List<RelevantCategoryInfoBean> list = new ArrayList<RelevantCategoryInfoBean>();

            List<com.ai.commons.beans.customer.RelevantCategoryInfoBean> beanls = customer.getProductFamily().getRelevantCategoryInfo();
            for (com.ai.commons.beans.customer.RelevantCategoryInfoBean rcbean : beanls) {

                RelevantCategoryInfoBean relevantcatinfoBean = new RelevantCategoryInfoBean();
                relevantcatinfoBean.setFavourite_family(rcbean.getFavCategory());
                relevantcatinfoBean.setFavourite_category(rcbean.getFavCategory());
                relevantcatinfoBean.setFavourite_seq(rcbean.getFavSeq());
                list.add(relevantcatinfoBean);
                productfamilyBean.setRelevant_category_info(list);

            }

            user.setProduct_family(productfamilyBean);

            OrderBookingBean orderBookingBean = new OrderBookingBean();

            orderBookingBean.setClc_percentage(customer.getOrderBooking().getClcPercentage());
            orderBookingBean.setPsi_percentage(customer.getOrderBooking().getPsiPercentage());
            orderBookingBean.setDupro_percentage(customer.getOrderBooking().getDuproPercentage());
            orderBookingBean.setIpc_percentage(customer.getOrderBooking().getIpcPercentage());
            orderBookingBean.setPm_percentage(customer.getOrderBooking().getPmPercentage());
            orderBookingBean.setClc_percentage_packed(customer.getOrderBooking().getClcPercentagePacked());
            orderBookingBean.setPsi_percentage_packed(customer.getOrderBooking().getPsiPercentagePacked());
            orderBookingBean.setDupro_percentage_packed(customer.getOrderBooking().getDuproPercentagePacked());
            orderBookingBean.setIpc_percentage_packed(customer.getOrderBooking().getIpcPercentagePacked());
            orderBookingBean.setPm_percentage_packed(customer.getOrderBooking().getPmPercentagePacked());
            orderBookingBean.setSend_modification_mail(customer.getOrderBooking().getSendModificationMail());
            orderBookingBean.setSend_sample_to_factory(customer.getOrderBooking().getSendSampleToFactory());
            orderBookingBean.setPo_compulsory(customer.getOrderBooking().getPoCompulsory());
            orderBookingBean.setRequire_drop_testing(customer.getOrderBooking().getRequireDropTesting());
            orderBookingBean.setShare_perferred_tests(customer.getOrderBooking().getSharePerferredTests());
            orderBookingBean.setShare_checklist(customer.getOrderBooking().getShareChecklist());
            orderBookingBean.setTurn_off_ai_access(customer.getOrderBooking().getTurnOffAIAccess());
            orderBookingBean.setFavorite_tests(customer.getOrderBooking().getFavoriteTests());
            orderBookingBean.setAllow_change_aql(customer.getOrderBooking().getAllowChangeAql());
            orderBookingBean.setCustom_aql_level(customer.getOrderBooking().getCustAqlLevel());
            orderBookingBean.setCritical_defects(customer.getOrderBooking().getCriticalDefects());
            orderBookingBean.setMajor_defects(customer.getOrderBooking().getMajorDefects());
            orderBookingBean.setMinor_defects(customer.getOrderBooking().getMinorDefects());
            orderBookingBean.setMax_mea_defects(customer.getOrderBooking().getMaxMeaDefects());
            orderBookingBean.setCustomized_sample_level(customer.getOrderBooking().getCustomizedSampleLevel());
            orderBookingBean.setMeasurement_sample_level(customer.getOrderBooking().getMeasurementSampleLevel());
            user.setOrder_booking(orderBookingBean);

            MultiReferenceBookingBean multirefBookingBean = new MultiReferenceBookingBean();

            multirefBookingBean.setApprove_references(customer.getMultiRefBooking().getApproveReferences());
            multirefBookingBean.setAsk_number_of_references(customer.getMultiRefBooking().getAskNumberOfReferences());
            multirefBookingBean.setNumber_of_reference_per_product(customer.getMultiRefBooking().getNumberOfRefPerProduct());
            multirefBookingBean.setNumber_of_reference_per_report(customer.getMultiRefBooking().getNumberOfRefPerReport());
            multirefBookingBean.setNumber_of_reference_per_manday(customer.getMultiRefBooking().getNumberOfRefPerMd());
            multirefBookingBean.setNumber_of_pcs_per_reference(customer.getMultiRefBooking().getNumberOfPcsPerRef());
            multirefBookingBean.setNumber_of_report_per_manday(customer.getMultiRefBooking().getNumberOfReportPerMd());
            multirefBookingBean.setClc_number_of_reports(customer.getMultiRefBooking().getClcNumberOfReports());
            multirefBookingBean.setClc_number_of_container(customer.getMultiRefBooking().getClcNumberOfContainer());
            multirefBookingBean.setPeo_calculation(customer.getMultiRefBooking().getPeoCalculation());
            multirefBookingBean.setContainer_rate(customer.getMultiRefBooking().getContainerRate());
            user.setMulti_reference_booking(multirefBookingBean);

            ExtraBean extraBean = new ExtraBean();

            extraBean.setScreenshot_needed(customer.getExtra().getIsScreenshotNeeded());
            extraBean.setActivated(customer.getExtra().getIsActivated());
            extraBean.setDeactived_date(customer.getExtra().getDeactivedDate());
            extraBean.setForce_new(customer.getExtra().getForceNew());
            extraBean.setRe_actived_date(customer.getExtra().getReActivedTime());
            extraBean.setAi_client(customer.getExtra().getAiClient());
            extraBean.setIs_deleted(customer.getExtra().getIsDeleted());
            extraBean.setIs_chb_client(customer.getExtra().getIsChb());
            extraBean.setIs_fi_client(customer.getExtra().getIsFI());
            extraBean.setReminder_type(customer.getExtra().getReminderType());
            extraBean.setSub_sic_receive_order_email(customer.getExtra().getSubSicRecOrderEmail());
            extraBean.setSub_sic_receive_report_email(customer.getExtra().getSubSicRecReportEmail());
            extraBean.setIs_show_dashboard_survey(customer.getExtra().getIsShowDashboardSurvey());
            extraBean.setRemembered_product_family_key(customer.getExtra().getRememberedProductFamilyKey());
            extraBean.setOutstanding_date_login(customer.getExtra().getOutstandingDateLogin());
            extraBean.setOutstanding_date_company(customer.getExtra().getOutstandingDateCompany());
            extraBean.setIs_first_login_np(customer.getExtra().getIsFirstLoginNP());
            extraBean.setIs_detailed_booking_form(customer.getExtra().getIsDetailedBookingForm());
            extraBean.setMax_penalty(customer.getExtra().getMaxPenalty());
            extraBean.setMax_penalty_unit(customer.getExtra().getMaxPenaltyUnit());
            extraBean.setMax_average_penalty(customer.getExtra().getMaxAveragePenalty());
            extraBean.setMax_average_penalty_unit(customer.getExtra().getMaxAveragePenaltyUnit());
            extraBean.setIs_fb_save_aql(customer.getExtra().getIsFbSaveAQL());
            extraBean.setIs_display_new_layout(customer.getExtra().getIsDisplayNewLayout());
            extraBean.setKpi_flag(customer.getExtra().getKpiFlag());
            extraBean.setOrder_view_type(customer.getExtra().getOrderViewType());
            extraBean.setSeen_mobile_app_promotion_banner(customer.getExtra().getHasSeenMobAppPromoBanner());
            extraBean.setFirst_time_log(customer.getExtra().getFirstTimeLog());
            extraBean.setReg_np(customer.getExtra().getRegNP());
            extraBean.setRefer(customer.getExtra().getRefer());
            extraBean.setUser_ip(customer.getExtra().getUserip());
            extraBean.setIs_test_account(customer.getExtra().getIsTestAccount());
            extraBean.setSave_in(customer.getExtra().getSaveIn());
            extraBean.setNew_manday_rate(customer.getExtra().getNewMDRate());
            extraBean.setDomain_name(customer.getExtra().getDomainName());
            user.setExtra(extraBean);


            return user;
        }
        return null;
    }
}
