/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ai.api.bean.*;
import com.ai.api.bean.UserBean;
import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.CompanyDao;
import com.ai.api.dao.CustomerDao;
import com.ai.api.dao.FeatureDao;
import com.ai.api.dao.ParameterDao;
import com.ai.api.exception.AIException;
import com.ai.api.service.UserService;
import com.ai.api.util.AIUtil;
import com.ai.commons.StringUtils;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.customer.*;
import com.ai.commons.beans.legacy.customer.ClientInfoBean;
import com.ai.commons.beans.payment.PaymentSearchCriteriaBean;
import com.ai.commons.beans.payment.PaymentSearchResultBean;
import com.ai.commons.beans.user.GeneralUserBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

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

@Service
public class UserServiceImpl implements UserService {
    protected Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

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

	@Autowired
	@Qualifier("featureDao")
	private FeatureDao featureDao;

	@Cacheable("userBeanCache")
    @Override
    public UserBean getCustById(String userId) throws IOException, AIException {
		logger.info("...........start getting UserBean from user service...........");
	    UserBean user = new UserBean();

	    //get all needed beans
	    GeneralUserViewBean generalUserBean = customerDao.getGeneralUserViewBean(userId);
	    if (generalUserBean == null) return null;

	    String compId = generalUserBean.getCompany().getCompanyId();
		OverviewBean overviewBean = companyDao.getCompanyOverview(compId);
		ContactBean contactBean = companyDao.getCompanyContact(compId);
		OrderBookingBean orderBookingBean = companyDao.getCompanyOrderBooking(compId);
		ExtraBean extrabean = companyDao.getCompanyExtra(compId);

		ProductFamilyBean productFamilyBean = companyDao.getCompanyProductFamily(compId);
		QualityManualBean qualityManualBean = companyDao.getCompanyQualityManual(compId);

		List<ProductCategoryDtoBean> productCategoryDtoBeanList = paramDao.getProductCategoryList();

		List<ProductFamilyDtoBean> productFamilyDtoBeanList = paramDao.getProductFamilyList();

		MultiRefBookingBean multiRefBookingBean = companyDao.getCompanyMultiRefBooking(compId);

		CustomerFeatureBean customerFeatureBean = featureDao.getCustomerFeatureBean(compId,"BookOrderWithMultipleFactories");

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

		comp.setWebsite(generalUserBean.getCompany().getWebsite());
		comp.setLogo(generalUserBean.getCompany().getLogoPath());

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
		BookingPreferenceBean bookingbean = new BookingPreferenceBean();

		bookingbean.setUseQuickFormByDefault(extrabean.getIsDetailedBookingForm());

		String sendSampleToFactory = orderBookingBean.getSendSampleToFactory();
		if(sendSampleToFactory!=null && sendSampleToFactory.equalsIgnoreCase("Yes")) {
			bookingbean.setShouldSendRefSampleToFactory(true);
		} else {
			bookingbean.setShouldSendRefSampleToFactory(false);
		}

		String poCompulsory = orderBookingBean.getPoCompulsory();
		if(poCompulsory!=null && poCompulsory.equalsIgnoreCase("Yes")){
			bookingbean.setIsPoMandatory(true);
		} else {
			bookingbean.setIsPoMandatory(false);
		}

		bookingbean.setProductDivisions(orderBookingBean.getAvailableDivisions());

		if(customerFeatureBean!=null) {
			String featureValue = customerFeatureBean.getFeatureValue();
			if (featureValue != null && featureValue.equalsIgnoreCase("Yes")) {
				bookingbean.setBookOrdersWithMultipleFactories(true);
			} else {
				bookingbean.setBookOrdersWithMultipleFactories(false);
			}
		}

		String sendModificationMail = orderBookingBean.getSendModificationMail();
		if(sendModificationMail!=null && sendModificationMail.equalsIgnoreCase("Yes")){
			bookingbean.setSendEmailAfterModification(true);
		} else {
			bookingbean.setSendEmailAfterModification(false);
		}

		String showProdDivision = orderBookingBean.getShowProdDivision();
		if(showProdDivision!=null && showProdDivision.equalsIgnoreCase("Yes")){
			bookingbean.setShowProductDivision(true);
		} else {
			bookingbean.setShowProductDivision(false);
		}

		String showFactoryDetails = orderBookingBean.getShowFactoryDetails();
		if(showFactoryDetails!=null && showFactoryDetails.equalsIgnoreCase("Yes")){
			bookingbean.setShowFactoryDetailsToMaster(true);
		} else {
			bookingbean.setShowFactoryDetailsToMaster(false);
		}

		String requireDropTesting = orderBookingBean.getRequireDropTesting();
		if(requireDropTesting!=null && requireDropTesting.equalsIgnoreCase("Yes")){
			bookingbean.setRequireDropTesting(true);
		} else {
			bookingbean.setRequireDropTesting(false);
		}

		String allowPostpone = orderBookingBean.getAllowPostpone();
		if(allowPostpone!=null && allowPostpone.equalsIgnoreCase("Yes")){
			bookingbean.setAllowPostponementBySuppliers(true);
		} else {
			bookingbean.setAllowPostponementBySuppliers(false);
		}

		String notifyClient = orderBookingBean.getNotifyClient();
		if(notifyClient!=null && notifyClient.equalsIgnoreCase("Yes")){
			bookingbean.setSendSupplierConfirmationEmailToClientAlways(true);
		} else {
			bookingbean.setSendSupplierConfirmationEmailToClientAlways(false);
		}

		String sharePerferredTests = orderBookingBean.getSharePerferredTests();
		if(sharePerferredTests!=null && sharePerferredTests.equalsIgnoreCase("Yes")){
			bookingbean.setShareFavoriteLabTestsWithSubAccounts(true);
		} else {
			bookingbean.setShareFavoriteLabTestsWithSubAccounts(false);
		}

		String shareChecklist = orderBookingBean.getShareChecklist();
		if(shareChecklist!=null && shareChecklist.equalsIgnoreCase("Yes")){
			bookingbean.setShareChecklistWithSubAccounts(true);
		} else {
			bookingbean.setShareChecklistWithSubAccounts(false);
		}

		String turnOffAIAccess = orderBookingBean.getTurnOffAIAccess();
		if(turnOffAIAccess!=null && turnOffAIAccess.equalsIgnoreCase("Yes")){
			bookingbean.setTurnOffAiWebsiteDirectAccess(true);
		} else {
			bookingbean.setTurnOffAiWebsiteDirectAccess(false);
		}

		MultiReferenceBean multiReferenceBean = new MultiReferenceBean();
		String approveReferences = multiRefBookingBean.getApproveReferences();
		if(approveReferences!=null && approveReferences.equalsIgnoreCase("Yes")){
			multiReferenceBean.setClientCanApproveRejectIndividualProductReferences(true);
		} else {
			multiReferenceBean.setClientCanApproveRejectIndividualProductReferences(false);
		}
		bookingbean.setMultiReference(multiReferenceBean);

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
		//"no" - Use Client Customized Product Type
		if ("no".toUpperCase().equals(productFamilyBean.getHowToChooseProType().toUpperCase())) {
			preferredProductFamilies.setUseCustomizedProductType(true);
		}else {
			preferredProductFamilies.setUseCustomizedProductType(false);
		}

		int a = productFamilyBean.getRelevantCategoryInfo().size();
		int b = productFamilyBean.getProductFamilyInfo().size();
		List<PublicProductType> publicProductTypeList = new ArrayList<>();
		List<CustomizedProductType> customizedProductTypeList = new ArrayList<>();

		for (int i = 0; i < a; i++) {
			PublicProductType publicProductType = new PublicProductType();
			publicProductType.setProductCategoryId(productFamilyBean.getRelevantCategoryInfo().get(i).getFavCategory());
			publicProductType.setProductFamilyId(productFamilyBean.getRelevantCategoryInfo().get(i).getFavFamily());
			//set product category name
			for (ProductCategoryDtoBean productCategoryDto : productCategoryDtoBeanList) {
				if (publicProductType.getProductCategoryId().equals(productCategoryDto.getId())) {
					publicProductType.setProductCategoryName(productCategoryDto.getName());
					break;
				}
			}

			//set product family name
			for (ProductFamilyDtoBean productFamilyDtoBean : productFamilyDtoBeanList) {
				if (publicProductType.getProductFamilyId().equals(productFamilyDtoBean.getId())) {
					publicProductType.setProductFamilyName(productFamilyDtoBean.getName());
					break;
				}
			}
			publicProductTypeList.add(publicProductType);
		}
		for (int i=0;i<b;i++){
			CustomizedProductType customizedProductType = new CustomizedProductType();
			BeanUtils.copyProperties(productFamilyBean.getProductFamilyInfo().get(i),customizedProductType);
			customizedProductTypeList.add(customizedProductType);
		}
		preferredProductFamilies.setPublicProductTypeList(publicProductTypeList);
		preferredProductFamilies.setCustomizedProductTypeList(customizedProductTypeList);
		bookingbean.setPreferredProductFamilies(preferredProductFamilies);

		// ------------Set QualityManual Properties ----------------

		QualityManual qualityManual = new QualityManual();
		qualityManual.setDocType("QUALITY_MANUAL");
		qualityManual.setFilename(qualityManualBean.getQmFileName());
		qualityManual.setPublishDate(qualityManualBean.getQmReleaseDate());
		qualityManual.setUrl(config.getCustomerServiceUrl() + "/customer/" + compId + "/quality-manual-file");

		bookingbean.setQualityManual(qualityManual);
		preferencesBean.setBooking(bookingbean);
		user.setPreferencesBean(preferencesBean);

		logger.info("...........return UserBean from user service...........");
		return user;
    }

	@CachePut(value = "userBeanCache", key = "#userId")
	@Override
    public UserBean updateCompany(CompanyBean newComp, String userId) throws IOException, AIException {
	    //call customer service to get latest crmCompanyBean first
	    GeneralUserViewBean generalUserBean = customerDao.getGeneralUserViewBean(userId);
	    String compId = generalUserBean.getCompany().getCompanyId();

	    CrmCompanyBean company = companyDao.getCrmCompany(compId);

	    //fill new values
	    company.setIndustry(newComp.getIndustry());
	    company.setAddress1(newComp.getAddress());
	    company.setCity(newComp.getCity());
	    company.setPostCode(newComp.getPostcode());
	    company.setCountryRegion(newComp.getCountry());

		company.setWebsite(newComp.getWebsite());

	    //update
		//return companyDao.updateCrmCompany(company);

		if(companyDao.updateCrmCompany(company)){
			return this.getCustById(userId);
		}
	    return null;
    }

	@CachePut(value = "userBeanCache", key = "#userId")
	@Override
    public UserBean updateContact(ContactInfoBean newContact, String userId) throws IOException, AIException {
	    //get general user bean
	    GeneralUserBean user = customerDao.getGeneralUser(userId);
	    user.setFollowName(newContact.getMain().getSalutation());
	    user.setFirstName(newContact.getMain().getGivenName());
	    user.setLastName(newContact.getMain().getFamilyName());
	    user.setPersonalEmail(newContact.getMain().getEmail());
	    user.setLandline(newContact.getMain().getPhoneNumber());
	    user.setMobile(newContact.getMain().getMobileNumber());

	    //get comp id
	    GeneralUserViewBean generalUserBean = customerDao.getGeneralUserViewBean(userId);
	    String compId = generalUserBean.getCompany().getCompanyId();

	    //get contact bean
	    ContactBean contact = companyDao.getCompanyContact(compId);
	    contact.setMainPosition(newContact.getMain().getPosition());
	    if (newContact.getBilling().getIsSameAsMainContact().equalsIgnoreCase("true")) {
		    contact.setAccountingGender(newContact.getMain().getSalutation());
		    contact.setAccountingGivenName(newContact.getMain().getGivenName());
		    contact.setAccountingName(newContact.getMain().getFamilyName());
		    contact.setAccountingEmail(newContact.getMain().getEmail());
	    }else {
		    contact.setAccountingGender(newContact.getBilling().getSalutation());
		    contact.setAccountingGivenName(newContact.getBilling().getGivenName());
		    contact.setAccountingName(newContact.getBilling().getFamilyName());
		    contact.setAccountingEmail(newContact.getBilling().getEmail());
	    }

	    //update general user and company contact
	    //return customerDao.updateGeneralUser(user) && companyDao.updateCompanyContact(compId, contact);
		if(customerDao.updateGeneralUser(user)&&companyDao.updateCompanyContact(compId, contact))
			return this.getCustById(userId);
		return null;
    }

	@CachePut(value = "userBeanCache", key = "#userId")
	@Override
    public UserBean updateBookingPreference(BookingPreferenceBean newBookingPref, String userId) throws IOException, AIException {
        System.out.println("-----orderBookingBean-----" + newBookingPref + "---" + userId);

		//get comp id
		GeneralUserViewBean generalUserBean = customerDao.getGeneralUserViewBean(userId);
		if (generalUserBean == null) return null;

		String compId = generalUserBean.getCompany().getCompanyId();

		//get booking preference first
		OrderBookingBean booking = companyDao.getCompanyOrderBooking(compId);
		booking.setSendSampleToFactory(StringUtils.getYesNo(Boolean.toString(newBookingPref.isShouldSendRefSampleToFactory())));
		booking.setPoCompulsory(StringUtils.getYesNo(Boolean.toString(newBookingPref.getIsPoMandatory())));
		booking.setPsiPercentage(newBookingPref.getMinQuantityToBeReady()[0].getMinQty());
		booking.setDuproPercentage(newBookingPref.getMinQuantityToBeReady()[1].getMinQty());
		booking.setIpcPercentage(newBookingPref.getMinQuantityToBeReady()[2].getMinQty());
		booking.setClcPercentage(newBookingPref.getMinQuantityToBeReady()[3].getMinQty());
		booking.setPmPercentage(newBookingPref.getMinQuantityToBeReady()[4].getMinQty());

		booking.setAllowChangeAql(StringUtils.getOneZero(newBookingPref.getAqlAndSamplingSize().getCanModify()));
		booking.setCustomizedSampleLevel(newBookingPref.getAqlAndSamplingSize().getCustomDefaultSampleLevel());
		booking.setCustAqlLevel(StringUtils.getYesNo(newBookingPref.getAqlAndSamplingSize().getUseCustomAQL()));
		if (newBookingPref.getAqlAndSamplingSize().getUseCustomAQL().equalsIgnoreCase("false")) {
			booking.setCriticalDefects("");
			booking.setMajorDefects("");
			booking.setMinorDefects("");
			booking.setMaxMeaDefects("");
		} else {
			booking.setCriticalDefects(newBookingPref.getAqlAndSamplingSize().getCustomAQL().getCriticalDefects());
			booking.setMajorDefects(newBookingPref.getAqlAndSamplingSize().getCustomAQL().getMajorDefects());
			booking.setMinorDefects(newBookingPref.getAqlAndSamplingSize().getCustomAQL().getMinorDefects());
			booking.setMaxMeaDefects(newBookingPref.getAqlAndSamplingSize().getCustomAQL().getMaxMeasurementDefects());
		}

		//get extra first
		ExtraBean extra= companyDao.getCompanyExtra(compId);
		extra.setIsDetailedBookingForm(StringUtils.getYesNo(newBookingPref.getUseQuickFormByDefault()));

		//update order booking and extra
		//return companyDao.updateCompanyExtra(compId, extra) && companyDao.updateCompanyOrderBooking(compId, booking);
		if(companyDao.updateCompanyExtra(compId, extra) && companyDao.updateCompanyOrderBooking(compId, booking)){
			return this.getCustById(userId);
		}
		return null;
    }

	@CachePut(value = "userBeanCache", key = "#userId")
	@Override
	public UserBean updateBookingPreferredProductFamily(List<String> newPreferred, String userId) throws IOException, AIException {
		//get comp id
		GeneralUserViewBean generalUserBean = customerDao.getGeneralUserViewBean(userId);
		String compId = generalUserBean.getCompany().getCompanyId();

		//get current product family
		ProductFamilyBean family = companyDao.getCompanyProductFamily(compId);
		System.out.println(family.getProductFamilyInfo());

		List<RelevantCategoryInfoBean> infos = new ArrayList<>();

		List<ProductFamilyDtoBean> productFamilyDtoBeanList = paramDao.getProductFamilyList();

		int index = 1;
		for (String familyID : newPreferred) {
			//get product category by product family id

			for(int i=0;i<productFamilyDtoBeanList.size();i++){
				ProductFamilyDtoBean productFamilyDtoBean = productFamilyDtoBeanList.get(i);
				if(familyID.equals(productFamilyDtoBean.getId())){
					String categoryID = productFamilyDtoBean.getCategoryId();
					RelevantCategoryInfoBean newInfo = new RelevantCategoryInfoBean();
					newInfo.setFavFamily(familyID);
					newInfo.setFavCategory(categoryID);
					newInfo.setFavSeq(index);
					infos.add(newInfo);
					index++;
					break;
				}
			}
		}
		family.setRelevantCategoryInfo(infos);
		//return companyDao.updateCompanyProductFamily(compId, family);
		if(companyDao.updateCompanyProductFamily(compId, family)){
			return this.getCustById(userId);
		}
		return null;
	}

	@Override
	public ServiceCallResult updateUserPassword(String userId, HashMap<String, String> pwdMap) throws IOException, AIException {
		return customerDao.updateGeneralUserPassword(userId, pwdMap);
	}

	@Override
	public boolean getCompanyLogo(String userId, String companyId,HttpServletResponse httpResponse) {
        try {
            InputStream inputStream = customerDao.getCompanyLogo(companyId);
            ServletOutputStream output = httpResponse.getOutputStream();
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            byte[] buffer = new byte[10240];
            if(null == inputStream)return false;
            for (int length = 0; (length = inputStream.read(buffer)) > 0;) {
                output.write(buffer, 0, length);
            }
            return true;
        }catch (Exception e){
            logger.error("ERROR! from service[getCompanyLogo]",e);
        }
		return false;
	}

    @Override
    public boolean updateCompanyLogo(String userId, String companyId, HttpServletRequest request) {
        try{
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            Iterator<String> fileNames = multipartHttpServletRequest.getFileNames();
            if (fileNames == null) {
                logger.error("missing upload file!");
            }
			String fileName = fileNames.next();
            MultipartFile file = multipartHttpServletRequest.getFile(fileName);
            return customerDao.updateCompanyLogo(companyId,file);
        }catch (Exception e){
            logger.error("ERROR!",e);
        }
        return false;
    }

	@Override
	public boolean deleteCompanyLogo(String userId, String companyId) {
		try {
			return customerDao.deleteCompanyLogo(companyId);
		}catch (Exception e){
			logger.error("ERROR!",e);
		}
		return false;
	}

	@Override
	public boolean createNewAccount(ClientInfoBean clientInfoBean) throws IOException, AIException {
		return customerDao.createNewAccount(clientInfoBean);
	}

	@Override
	public List<PaymentSearchResultBean> searchPaymentList(PaymentSearchCriteriaBean criteria) throws IOException, AIException {
		if(criteria.getLogin()==null){
			String login = customerDao.getGeneralUser(criteria.getUserID()).getLogin();
			criteria.setLogin(login);
		}
		return customerDao.searchPaymentList(criteria);
	}


}