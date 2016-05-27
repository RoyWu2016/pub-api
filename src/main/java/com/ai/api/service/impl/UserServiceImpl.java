/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.service.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.ai.api.bean.AqlAndSamplingSizeBean;
import com.ai.api.bean.BillingBean;
import com.ai.api.bean.BookingBean;
import com.ai.api.bean.CompanyBean;
import com.ai.api.bean.ContactInfoBean;
import com.ai.api.bean.CustomAQLBean;
import com.ai.api.bean.MainBean;
import com.ai.api.bean.MinQuantityToBeReadyBean;
import com.ai.api.bean.PreferencesBean;
import com.ai.api.bean.PreferredProductFamilies;
import com.ai.api.bean.QualityManual;
import com.ai.api.bean.SysProductCategoryBean;
import com.ai.api.bean.SysProductFamilyBean;
import com.ai.api.dao.CompanyDao;
import com.ai.api.dao.CustomerDao;
import com.ai.api.dao.ParameterDao;
import com.ai.api.exception.AIException;
import com.ai.api.model.UserBean;
import com.ai.api.service.ServiceConfig;
import com.ai.api.service.UserService;
import com.ai.api.util.AIUtil;
import com.ai.commons.HttpUtil;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.customer.ContactBean;
import com.ai.commons.beans.customer.CrmCompanyBean;
import com.ai.commons.beans.customer.ExtraBean;
import com.ai.commons.beans.customer.GeneralUserViewBean;
import com.ai.commons.beans.customer.OrderBookingBean;
import com.ai.commons.beans.customer.OverviewBean;
import com.ai.commons.beans.customer.ProductFamilyBean;
import com.ai.commons.beans.customer.QualityManualBean;
import org.apache.log4j.Logger;
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

@Service("userService")
public class UserServiceImpl implements UserService {
    protected Logger logger = Logger.getLogger(UserServiceImpl.class);

    @Autowired
    @Qualifier("serviceConfig")
    private ServiceConfig config;

    @Autowired
    @Qualifier("customerDao")
    private CustomerDao customerDao;

	@Autowired
	@Qualifier("paramDao")
	private ParameterDao paramDao;

	@Autowired
	@Qualifier("companyDao")
	private CompanyDao companyDao;


    @Override
    public UserBean getCustByLogin(String login) throws IOException, AIException {

        String customerId = customerDao.getCustomerIdByCustomerLogin(login);
        System.out.println("---**----customerId--**---" + customerId);

	    UserBean user = new UserBean();

	    //get all needed beans
	    GeneralUserViewBean generalUserBean = customerDao.getGeneralUserViewBean(customerId);
	    String compId = generalUserBean.getCompany().getCompanyId();
		OverviewBean overviewBean = customerDao.getCompanyOverview(compId);
		ContactBean contactBean = customerDao.getCompanyContact(compId);
		OrderBookingBean orderBookingBean = customerDao.getCompanyOrderBooking(compId);
		ExtraBean extrabean = customerDao.getCompanyExtra(compId);

		ProductFamilyBean productFamilyBean = customerDao.getCompanyProductFamily(compId);
		QualityManualBean qualityManualBean = customerDao.getCompanyQualityManual(compId);
	    SysProductCategoryBean sysProductCategoryBean = paramDao.getSysProductCategory();
	    SysProductFamilyBean sysProductFamilyBean = paramDao.getSysProductFamily();

	    //1st level fields
	    user.setId(generalUserBean.getUser().getUserId());
		user.setSic(overviewBean.getSic());
	    user.setLogin(generalUserBean.getUser().getLogin());
	    user.setStatus(generalUserBean.getUser().getStatusText());
	    user.setBusinessUnit(AIUtil.getUserBusinessUnit(generalUserBean, extrabean));

		// ------------Set CompanyBean Properties ----------------
		CompanyBean comp = new CompanyBean();
	    comp.setId(generalUserBean.getCompany().getCompanyId());
		comp.setName(generalUserBean.getCompany().getCompanyName());
		comp.setNameCN(generalUserBean.getCompany().getCompanyNameCN());
		comp.setIndustry(generalUserBean.getCompany().getIndustry());
	    comp.setCountry(generalUserBean.getCompany().getCountryRegion());
		comp.setAddress(generalUserBean.getCompany().getAddress1());
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

		if (mainSalutation.equals(billSalutation) && mainFamilyName.equals(billFamilyName)
				&& mainGivenName.equals(billGivenName) && mainEmail.equals(billEmail)) {
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
			for (int j = 0; j < sysProductCategoryBean.getId().size(); j++) {

				String catname = sysProductCategoryBean.getId().get(j);
				if (catid.equals(catname)) {
					preferredProductFamilies.setProductCategoryName(sysProductCategoryBean.getName().get(j));
				}
			}

			for (int k = 0; k < sysProductFamilyBean.getId().size(); k++) {

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
		qualityManual.setUrl(config.getCustomerServiceUrl() + "/customer/" + compId + "/quality-manual-file");

		bookingbean.setQualityManual(qualityManual);
		preferencesBean.setBooking(bookingbean);
		user.setPreferencesBean(preferencesBean);

		return user;
    }

    public boolean updateCompany(CompanyBean newComp, String userID) throws IOException, AIException {
	    //call customer service to get latest crmCompanyBean first
	    GeneralUserViewBean generalUserBean = customerDao.getGeneralUserViewBean(userID);
	    String compId = generalUserBean.getCompany().getCompanyId();

	    CrmCompanyBean company = companyDao.getCrmCompany(compId);

	    //fill new values
	    company.setIndustry(newComp.getIndustry());
	    company.setAddress1(newComp.getAddress());
	    company.setCity(newComp.getCity());
	    company.setPostCode(newComp.getPostcode());
	    company.setCountryRegion(newComp.getCountry());

	    //update
	    return companyDao.updateCrmCompany(company);
    }

    public boolean updateContact(GeneralUserViewBean generalUserViewBean, ContactBean contactBean, String user_id) throws IOException, AIException {
        // customerDao.updateProfileContact(generalUserViewBean, contactBean, user_id);
        String url = "http://192.168.0.31:8093/customer-service/customer/" + user_id + "/contact";
        try {
            ServiceCallResult result = HttpUtil.issuePostRequest(url, null,
                    generalUserViewBean);
            if (result.getStatusCode() == 200 || result.getStatusCode() == 202) {
                return true;
            }
        } catch (IOException e) {
            logger.error(Arrays.asList(e.getStackTrace()));
        }
        return false;
    }

    public boolean updateBookingPreference(OrderBookingBean orderBookingBean, String user_id) throws IOException, AIException {
        System.out.println("-----orderBookingBean-----" + orderBookingBean + "---" + user_id);
        // customerDao.updateBookingPreference(orderBookingBean, user_id);
        String url = "http://192.168.0.31:8093/customer-service/customer/" + user_id + "/order-booking";
        try {
            ServiceCallResult result = HttpUtil.issuePostRequest(url, null,
                    orderBookingBean);
            if (result.getStatusCode() == 200 || result.getStatusCode() == 202) {
                return true;
            }
        } catch (IOException e) {
            logger.error(Arrays.asList(e.getStackTrace()));
        }
        return false;
    }

	@Override
	public boolean updateProductCategory(List<String> categoryList, String userId) {
		return false;
	}
}