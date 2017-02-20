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

import com.ai.api.bean.AqlAndSamplingSizeBean;
import com.ai.api.bean.BillingBean;
import com.ai.api.bean.BookingPreferenceBean;
import com.ai.api.bean.CompanyBean;
import com.ai.api.bean.CompanyLogoBean;
import com.ai.api.bean.CompanyRelationshipBean;
import com.ai.api.bean.ContactInfoBean;
import com.ai.api.bean.CustomAQLBean;
import com.ai.api.bean.CustomizedProductType;
import com.ai.api.bean.EmployeeBean;
import com.ai.api.bean.MainBean;
import com.ai.api.bean.MasterCompanyBean;
import com.ai.api.bean.MinQuantityToBeReadyBean;
import com.ai.api.bean.MultiReferenceBean;
import com.ai.api.bean.Payment;
import com.ai.api.bean.PreferencesBean;
import com.ai.api.bean.PreferredProductFamilies;
import com.ai.api.bean.ProductCategoryDtoBean;
import com.ai.api.bean.ProductFamilyDtoBean;
import com.ai.api.bean.PublicProductType;
import com.ai.api.bean.QualityManual;
import com.ai.api.bean.ReportApproverBean;
import com.ai.api.bean.ReportPreferenceBean;
import com.ai.api.bean.ReportRejectCategoryBean;
import com.ai.api.bean.ReportRejectCategoryReasonBean;
import com.ai.api.bean.SubordinateSettingsBean;
import com.ai.api.bean.SuperMasterBean;
import com.ai.api.bean.UserBean;
import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.CompanyDao;
import com.ai.api.dao.CustomerDao;
import com.ai.api.dao.FeatureDao;
import com.ai.api.dao.ParameterDao;
import com.ai.api.exception.AIException;
import com.ai.api.service.UserService;
import com.ai.api.util.AIUtil;
import com.ai.api.util.BASE64DecodedMultipartFile;
import com.ai.api.util.RedisUtil;
import com.ai.commons.StringUtils;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.customer.ApproverBean;
import com.ai.commons.beans.customer.CompanyEntireBean;
import com.ai.commons.beans.customer.ContactBean;
import com.ai.commons.beans.customer.CrmCompanyBean;
import com.ai.commons.beans.customer.CrmCompanyRelationshipBean;
import com.ai.commons.beans.customer.CrmSaleInChargeBean;
import com.ai.commons.beans.customer.CustomerFeatureBean;
import com.ai.commons.beans.customer.DashboardBean;
import com.ai.commons.beans.customer.ExtraBean;
import com.ai.commons.beans.customer.MasterBean;
import com.ai.commons.beans.customer.MultiRefBookingBean;
import com.ai.commons.beans.customer.OrderBookingBean;
import com.ai.commons.beans.customer.ProductFamilyBean;
import com.ai.commons.beans.customer.QualityManualBean;
import com.ai.commons.beans.customer.RejectCategoryBean;
import com.ai.commons.beans.customer.RejectCategoryReasonBean;
import com.ai.commons.beans.customer.RelevantCategoryInfoBean;
import com.ai.commons.beans.customer.ReportCertificateBean;
import com.ai.commons.beans.legacy.customer.ClientInfoBean;
import com.ai.commons.beans.legacy.customer.ClientInfoWithTokenBean;
import com.ai.commons.beans.user.GeneralUserBean;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

	private UserBean getUserBeanByService(String userId) {
		if (StringUtils.isBlank(userId)) {
			return null;
		}
		UserBean user = new UserBean();
		logger.info("...........start getting UserBean from user service...........");
		CompanyEntireBean companyEntireBean = companyDao.getCompanyEntireInfo(userId);
		if (companyEntireBean == null)
			return null;

		GeneralUserBean userBean = null;
		List<GeneralUserBean> generalUserBeenList = companyEntireBean.getUsers();
		if (generalUserBeenList != null && generalUserBeenList.size() > 0) {
			for (GeneralUserBean bean : generalUserBeenList) {
				if (bean.getUserId().equals(userId)) {
					userBean = bean;
					break;
				}
			}
		}

		if (userBean == null)
			return null;

		String compId = companyEntireBean.getCompanyId();

		ContactBean contactBean = companyEntireBean.getContact();
		OrderBookingBean orderBookingBean = companyEntireBean.getOrderBooking();
		ExtraBean extrabean = companyEntireBean.getExtra();
		ProductFamilyBean productFamilyBean = companyEntireBean.getProductFamily();
		QualityManualBean qualityManualBean = companyEntireBean.getQualityManual();

		List<ProductCategoryDtoBean> productCategoryDtoBeanList = paramDao.getProductCategoryList(true);
		List<ProductFamilyDtoBean> productFamilyDtoBeanList = paramDao.getProductFamilyList(true);

		MultiRefBookingBean multiRefBookingBean = companyEntireBean.getMultiRefBooking();

		CustomerFeatureBean customerFeatureBean = featureDao.getCustomerFeatureBean(compId,
				"BookOrderWithMultipleFactories");

		ReportCertificateBean reportCertificateBean = companyEntireBean.getReportCertificate();

		user.setId(userBean.getUserId());
		user.setLogin(userBean.getLogin());
		user.setStatus(userBean.getStatusText());
		user.setBusinessUnit(AIUtil.getCompanyBusinessUnit(companyEntireBean, extrabean));
		user.setRate(companyEntireBean.getRate());

		List<CrmSaleInChargeBean> sales = companyEntireBean.getSales();
		if (sales != null && sales.size() > 0) {
			for (CrmSaleInChargeBean saleBean : sales) {
				if (saleBean.getSicTypeKey() != null && saleBean.getSicTypeKey().equals("SIC")) {
					user.setSic(saleBean.getFirstName() + " " + saleBean.getLastName());
					user.setSicId(saleBean.getSaleId());
					break;
				}
			}
		}

		// ------------Set CompanyBean Properties ----------------
		CompanyBean comp = new CompanyBean();
		// super master part
		MasterBean masterBean = companyEntireBean.getMaster();
		SuperMasterBean superMaster = new SuperMasterBean();
		List<MasterCompanyBean> masterCompanies = new ArrayList<>();
		if (StringUtils.isTrue(masterBean.getIsSuperMaster())) {
			superMaster.setSuperMaster(true);
			List<ClientInfoWithTokenBean> beans = companyDao.getMasterAccountTokens(companyEntireBean.getCompanyId(),
					false, false);
			for (ClientInfoWithTokenBean each : beans) {
				MasterCompanyBean bean = new MasterCompanyBean();
				bean.setCompanyName(each.getCompanyName());
				bean.setCompanyId(each.getCompanyId());
				CompanyEntireBean child = companyDao.getCompanyEntireInfoByCompanyId(each.getCompanyId());
				if (null != child && null != child.getUsers() && null != child.getUsers().get(0)) {
					bean.setUserId(child.getUsers().get(0).getUserId());
				}
				masterCompanies.add(bean);
			}
		} else {
			superMaster.setSuperMaster(false);
		}
		superMaster.setMasterCompanies(masterCompanies);
		comp.setSuperMaster(superMaster);

		// set company type, master/subordinate/standard
		if (companyEntireBean.getDirectSubordinates() != null && companyEntireBean.getDirectSubordinates().size() > 0) {
			comp.setType("master");
			// only master account has subordinates
			List<CompanyRelationshipBean> subordinatesList = new ArrayList();
			for (CrmCompanyRelationshipBean each : companyEntireBean.getDirectSubordinates()) {
				CompanyRelationshipBean bean = new CompanyRelationshipBean();
				bean.setCompanyId(each.getSubordinateId());
				bean.setCompanyName(each.getCompanyName());
				subordinatesList.add(bean);
			}
			comp.setSubordinates(subordinatesList);
		} else if (companyEntireBean.getDirectParents() != null && companyEntireBean.getDirectParents().size() > 0) {
			comp.setType("subordinate");
			comp.setParentCompanyId(companyEntireBean.getDirectParents().get(0).getCompanyId());
			comp.setParentCompanyName(companyEntireBean.getDirectParents().get(0).getCompanyName());

			SubordinateSettingsBean extraAccess = new SubordinateSettingsBean();

			//read parent company's setting and set extra access
			MasterBean subordinateMaster = companyDao.getMasterBeanByCompanyId(companyEntireBean.getDirectParents().get(0).getCompanyId());
			if (subordinateMaster != null) {
				extraAccess.setCanSeeReportActionButtons(
						"yes".equalsIgnoreCase(subordinateMaster.getHideApproveButton()) ? false : true);
				extraAccess.setCanSeeCcOptionInBookingForm(
						"yes".equalsIgnoreCase(subordinateMaster.getHideCcFields()) ? false : true);

				ReportCertificateBean parentCertificate= companyDao.getCompanyReportCertificateInfo(companyEntireBean.getDirectParents().get(0).getCompanyId());
				if (null != parentCertificate) {
					extraAccess.setCanSeeReportsPage(
							"yes".equalsIgnoreCase(parentCertificate.getSubReportAccess()) ? false
									: true);
				}
			}

			comp.setExtraAccess(extraAccess);
		} else {
			comp.setType("standard");
		}
		comp.setId(companyEntireBean.getCompanyId());
		comp.setName(companyEntireBean.getCompanyProfile().getCompanyName());
		comp.setNameCN(companyEntireBean.getCompanyProfile().getCompanyNameCN());
		comp.setIndustry(companyEntireBean.getCompanyProfile().getIndustry());
		comp.setCountry(companyEntireBean.getCompanyProfile().getCountryRegion());
		comp.setAddress(companyEntireBean.getCompanyProfile().getAddress1());
		comp.setCity(companyEntireBean.getCompanyProfile().getCity());
		comp.setPostcode(companyEntireBean.getCompanyProfile().getPostCode());

		comp.setWebsite(companyEntireBean.getCompanyProfile().getWebsite());
		comp.setLogo(companyEntireBean.getCompanyProfile().getLogoPath());
		comp.setMainEmail(companyEntireBean.getContact().getMainEmail());

		// List<CompanyRelationshipBean> parentList = new ArrayList();
		// for(CrmCompanyRelationshipBean
		// each:companyEntireBean.getDirectParents()) {
		// CompanyRelationshipBean bean = new CompanyRelationshipBean();
		// bean.setCompanyId(each.getCompanyId());
		// bean.setCompanyName(each.getCompanyName());
		//
		// parentList.add(bean);
		// }
		// comp.setParents(parentList);

		// finalBeam.setSuperMaster(superMaster);
		// finalBeam.setMasterList(masterBean.getMasterList());
		// finalBeam.setCanCreateOrder(StringUtils.isTrue(masterBean.getCanCreateOrder()));//
		// private String canCreateOrder;
		// finalBeam.setShareFactoryLib(StringUtils.isTrue(masterBean.getShareFactoryLib()));//
		// private String shareFactoryLib;
		// finalBeam.setSeePendingOrders(StringUtils.isTrue(masterBean.getSeePendingOrders()));//
		// private String seePendingOrders;
		// finalBeam.setSeeOnlineReports(StringUtils.isTrue(masterBean.getCanCreateOrder()));//
		// private String seeOnlineReports;
		// finalBeam.setSeeAllFactories(StringUtils.isTrue(masterBean.getSeeOnlineReports()));//
		// private String seeAllFactories;
		// finalBeam.setReceiveAllMails(StringUtils.isTrue(masterBean.getReceiveAllMails()));//
		// private String receiveAllMails;
		// finalBeam.setSendAllMailsToSub(StringUtils.isTrue(masterBean.getCanCreateOrder()));//
		// private String sendAllMailsToSub;
		// finalBeam.setAllMailsToSubCcBcc(StringUtils.isTrue(masterBean.getAllMailsToSubCcBcc()));//
		// private String allMailsToSubCcBcc;
		// finalBeam.setSendReportMailsToMaster(StringUtils.isTrue(masterBean.getSendReportMailsToMaster()));//
		// private String sendReportMailsToMaster;
		// finalBeam.setSendReportMailsToSub(StringUtils.isTrue(masterBean.getSendReportMailsToSub()));//
		// private String sendReportMailsToSub;
		// finalBeam.setReportMailsToSubCcBcc(masterBean.getReportMailsToSubCcBcc());//
		// private String reportMailsToSubCcBcc;
		// finalBeam.setDisClientName(masterBean.getDisClientName());// private
		// String disClientName;
		// finalBeam.setWhoPayOrder(masterBean.getWhoPayOrder());// private
		// String whoPayOrder;
		// finalBeam.setWhoPayReorder(masterBean.getWhoPayReorder());// private
		// String whoPayReorder;
		// finalBeam.setWhoPayLt(masterBean.getWhoPayLt());// private String
		// whoPayLt;
		// finalBeam.setWhoPayAudit(masterBean.getWhoPayAudit());// private
		// String whoPayAudit;
		// finalBeam.setWhoPayReAudit(masterBean.getWhoPayReAudit());// private
		// String whoPayReAudit;
		// finalBeam.setConsolidatedInvoice(masterBean.getConsolidatedInvoice());//
		// private String consolidatedInvoice;
		// finalBeam.setSuspendOrdersBy(masterBean.getSuspendOrdersBy());//
		// private String suspendOrdersBy;
		// finalBeam.setReadMasterChecklist(StringUtils.isTrue(masterBean.getIsReadMasterChecklist()));//
		// private String isReadMasterChecklist;

		user.setCompany(comp);

		Payment payment = new Payment();
		payment.setOnlinePaymentType(
				companyEntireBean.getInvoicing().getOnlinePayStatus().toUpperCase().replaceAll(" ", "_"));
		if ("new client".equalsIgnoreCase(companyEntireBean.getInvoicing().getOnlinePayStatus().toUpperCase())) {
			payment.setCharge("USD 8");
		} else if ("new client v3"
				.equalsIgnoreCase(companyEntireBean.getInvoicing().getOnlinePayStatus().toUpperCase())) {
			payment.setCharge("5%");
		} else if ("old client".equalsIgnoreCase(companyEntireBean.getInvoicing().getOnlinePayStatus().toUpperCase())) {
			payment.setCharge("0");
		} else if ("ONLINE_PAYMENT_MANDATORY".equalsIgnoreCase(
				companyEntireBean.getInvoicing().getOnlinePayStatus().toUpperCase().replaceAll(" ", "_"))) {
			payment.setCharge("0");
		} else if ("ONLINE_PAYMENT_MANDATORY".equalsIgnoreCase(
				companyEntireBean.getInvoicing().getOnlinePayStatus().toUpperCase().replaceAll(" ", "_"))) {
			payment.setCharge("0");
		}
		user.setPayment(payment);

		// ------------Set ContactInfoBean Properties ----------------
		ContactInfoBean contactInfoBean = new ContactInfoBean();

		MainBean main = new MainBean();
		main.setSalutation(userBean.getFollowName());
		main.setFamilyName(userBean.getLastName());
		main.setGivenName(userBean.getFirstName());
		main.setPosition(contactBean.getMainPosition());
		main.setEmail(userBean.getPersonalEmail());
		main.setPhoneNumber(userBean.getLandline());
		main.setMobileNumber(userBean.getMobile());

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
			billingBean.setSameAsMainContact(true);
		} else {
			billingBean.setSameAsMainContact(false);
		}

		contactInfoBean.setBilling(billingBean);
		user.setContacts(contactInfoBean);

		// ------------Set PreferencesBean Properties ----------------

		PreferencesBean preferencesBean = new PreferencesBean();
		BookingPreferenceBean bookingbean = new BookingPreferenceBean();

		bookingbean
				.setUseQuickFormByDefault(extrabean.getIsDetailedBookingForm().equalsIgnoreCase("yes") ? true : false);

		String sendSampleToFactory = orderBookingBean.getSendSampleToFactory();
		if (sendSampleToFactory != null && sendSampleToFactory.equalsIgnoreCase("Yes")) {
			bookingbean.setShouldSendRefSampleToFactory(true);
		} else {
			bookingbean.setShouldSendRefSampleToFactory(false);
		}

		String poCompulsory = orderBookingBean.getPoCompulsory();
		if (poCompulsory != null && poCompulsory.equalsIgnoreCase("Yes")) {
			bookingbean.setIsPoMandatory(true);
		} else {
			bookingbean.setIsPoMandatory(false);
		}

		bookingbean.setProductDivisions(orderBookingBean.getAvailableDivisions());

		if (customerFeatureBean != null) {
			String featureValue = customerFeatureBean.getFeatureValue();
			if (featureValue != null && featureValue.equalsIgnoreCase("Yes")) {
				bookingbean.setBookOrdersWithMultipleFactories(true);
			} else {
				bookingbean.setBookOrdersWithMultipleFactories(false);
			}
		}

		String sendModificationMail = orderBookingBean.getSendModificationMail();
		if (sendModificationMail != null && sendModificationMail.equalsIgnoreCase("Yes")) {
			bookingbean.setSendEmailAfterModification(true);
		} else {
			bookingbean.setSendEmailAfterModification(false);
		}

		String showProdDivision = orderBookingBean.getShowProdDivision();
		if (showProdDivision != null && showProdDivision.equalsIgnoreCase("Yes")) {
			bookingbean.setShowProductDivision(true);
		} else {
			bookingbean.setShowProductDivision(false);
		}

		String showFactoryDetails = orderBookingBean.getShowFactoryDetails();
		if (showFactoryDetails != null && showFactoryDetails.equalsIgnoreCase("Yes")) {
			bookingbean.setShowFactoryDetailsToMaster(true);
		} else {
			bookingbean.setShowFactoryDetailsToMaster(false);
		}

		String requireDropTesting = orderBookingBean.getRequireDropTesting();
		if (requireDropTesting != null && requireDropTesting.equalsIgnoreCase("Yes")) {
			bookingbean.setRequireDropTesting(true);
		} else {
			bookingbean.setRequireDropTesting(false);
		}

		String allowPostpone = orderBookingBean.getAllowPostpone();
		if (allowPostpone != null && allowPostpone.equalsIgnoreCase("Yes")) {
			bookingbean.setAllowPostponementBySuppliers(true);
		} else {
			bookingbean.setAllowPostponementBySuppliers(false);
		}

		String notifyClient = orderBookingBean.getNotifyClient();
		if (notifyClient != null && notifyClient.equalsIgnoreCase("Yes")) {
			bookingbean.setSendSupplierConfirmationEmailToClientAlways(true);
		} else {
			bookingbean.setSendSupplierConfirmationEmailToClientAlways(false);
		}

		String sharePerferredTests = orderBookingBean.getSharePerferredTests();
		if (sharePerferredTests != null && sharePerferredTests.equalsIgnoreCase("Yes")) {
			bookingbean.setShareFavoriteLabTestsWithSubAccounts(true);
		} else {
			bookingbean.setShareFavoriteLabTestsWithSubAccounts(false);
		}

		String shareChecklist = orderBookingBean.getShareChecklist();
		if (shareChecklist != null && shareChecklist.equalsIgnoreCase("Yes")) {
			bookingbean.setShareChecklistWithSubAccounts(true);
		} else {
			bookingbean.setShareChecklistWithSubAccounts(false);
		}

		String turnOffAIAccess = orderBookingBean.getTurnOffAIAccess();
		if (turnOffAIAccess != null && turnOffAIAccess.equalsIgnoreCase("Yes")) {
			bookingbean.setTurnOffAiWebsiteDirectAccess(true);
		} else {
			bookingbean.setTurnOffAiWebsiteDirectAccess(false);
		}

		MultiReferenceBean multiReferenceBean = new MultiReferenceBean();
		String approveReferences = multiRefBookingBean.getApproveReferences();
		if (approveReferences != null && approveReferences.equalsIgnoreCase("Yes")) {
			multiReferenceBean.setClientCanApproveRejectIndividualProductReferences(true);
			multiReferenceBean.setAskNumberOfReferences(false);
		} else {
			multiReferenceBean.setClientCanApproveRejectIndividualProductReferences(false);
			multiReferenceBean.setAskNumberOfReferences(
					multiRefBookingBean.getAskNumberOfReferences().equalsIgnoreCase("Yes") ? true : false);
		}
		multiReferenceBean.setNumberOfRefPerProduct(multiRefBookingBean.getNumberOfRefPerProduct());
		multiReferenceBean.setNumberOfRefPerReport(multiRefBookingBean.getNumberOfRefPerReport());
		multiReferenceBean.setNumberOfRefPerMd(multiRefBookingBean.getNumberOfRefPerMd());
		multiReferenceBean.setNumberOfPcsPerRef(multiRefBookingBean.getNumberOfPcsPerRef());
		multiReferenceBean.setNumberOfReportPerMd(multiRefBookingBean.getNumberOfReportPerMd());
		multiReferenceBean.setClcNumberOfReports(multiRefBookingBean.getClcNumberOfReports());
		multiReferenceBean.setClcNumberOfContainer(multiRefBookingBean.getClcNumberOfContainer());
		multiReferenceBean.setPeoCalculation(multiRefBookingBean.getPeoCalculation());
		multiReferenceBean.setContainerRate(multiRefBookingBean.getContainerRate());
		multiReferenceBean.setShowRefResultOnline(multiRefBookingBean.getShowRefResultOnline());

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

		if (orderBookingBean.getAllowChangeAql() != null && orderBookingBean.getAllowChangeAql().equals("1")) {
			aqlAndSamplingSizeBean.setCanModify(true);
		} else {
			aqlAndSamplingSizeBean.setCanModify(false);
		}

		aqlAndSamplingSizeBean.setCustomDefaultSampleLevel(orderBookingBean.getCustomizedSampleLevel());
		aqlAndSamplingSizeBean.setMeasurementSampleLevel(orderBookingBean.getMeasurementSampleLevel());
		CustomAQLBean customAQLBean = new CustomAQLBean();

		if (orderBookingBean.getCustAqlLevel() != null && orderBookingBean.getCustAqlLevel().equalsIgnoreCase("yes")) { // .equals("yes"))
																														// {
			aqlAndSamplingSizeBean.setUseCustomAQL(true);
			customAQLBean.setCriticalDefects(orderBookingBean.getCriticalDefects());
			customAQLBean.setMajorDefects(orderBookingBean.getMajorDefects());
			customAQLBean.setMinorDefects(orderBookingBean.getMinorDefects());
			customAQLBean.setMaxMeasurementDefects(orderBookingBean.getMaxMeaDefects());
		} else {
			aqlAndSamplingSizeBean.setUseCustomAQL(false);
			if (null == orderBookingBean.getCriticalDefects()) {
				customAQLBean.setCriticalDefects("0");
			} else {
				customAQLBean.setCriticalDefects(orderBookingBean.getCriticalDefects());
			}
			customAQLBean.setMajorDefects("0");
			customAQLBean.setMinorDefects("0");
			customAQLBean.setMaxMeasurementDefects("0");
		}

		aqlAndSamplingSizeBean.setCustomAQL(customAQLBean);
		bookingbean.setAqlAndSamplingSize(aqlAndSamplingSizeBean);

		// ------------Set PreferredProductFamilies Properties ----------------

		PreferredProductFamilies preferredProductFamilies = new PreferredProductFamilies();
		// "no" - Use Client Customized Product Type
		if (productFamilyBean.getHowToChooseProType() != null
				&& "NO".equals(productFamilyBean.getHowToChooseProType().toUpperCase())) {
			preferredProductFamilies.setUseCustomizedProductType(true);
		} else {
			preferredProductFamilies.setUseCustomizedProductType(false);
		}

		int a = productFamilyBean.getRelevantCategoryInfo() != null ? productFamilyBean.getRelevantCategoryInfo().size()
				: 0;
		int b = productFamilyBean.getProductFamilyInfo() != null ? productFamilyBean.getProductFamilyInfo().size() : 0;
		List<PublicProductType> publicProductTypeList = new ArrayList<>();
		List<CustomizedProductType> customizedProductTypeList = new ArrayList<>();

		for (int i = 0; i < a; i++) {
			PublicProductType publicProductType = new PublicProductType();
			publicProductType.setProductCategoryId(productFamilyBean.getRelevantCategoryInfo().get(i).getFavCategory());
			publicProductType.setProductFamilyId(productFamilyBean.getRelevantCategoryInfo().get(i).getFavFamily());
			// set product category name
			for (ProductCategoryDtoBean productCategoryDto : productCategoryDtoBeanList) {
				if (publicProductType.getProductCategoryId() != null
						&& publicProductType.getProductCategoryId().equals(productCategoryDto.getId())) {
					publicProductType.setProductCategoryName(productCategoryDto.getName());
					break;
				}
			}

			// set product family name
			for (ProductFamilyDtoBean productFamilyDtoBean : productFamilyDtoBeanList) {
				if (publicProductType.getProductFamilyId().equals(productFamilyDtoBean.getId())) {
					publicProductType.setProductFamilyName(productFamilyDtoBean.getName());
					break;
				}
			}
			publicProductTypeList.add(publicProductType);
		}
		for (int i = 0; i < b; i++) {
			CustomizedProductType customizedProductType = new CustomizedProductType();
			BeanUtils.copyProperties(productFamilyBean.getProductFamilyInfo().get(i), customizedProductType);
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

		ReportPreferenceBean reportPreferenceBean = new ReportPreferenceBean();
		if (reportCertificateBean != null) {
			reportPreferenceBean
					.setShowICField(reportCertificateBean.getDisIcFields().equalsIgnoreCase("Yes") ? true : false);
			reportPreferenceBean
					.setAutoSendIC(reportCertificateBean.getAutoSendIc().equalsIgnoreCase("Yes") ? true : false);
			reportPreferenceBean.setAttType(reportCertificateBean.getAttType());
			reportPreferenceBean.setAllowReportApprover(
					reportCertificateBean.getAllowReportApprover().equalsIgnoreCase("Yes") ? true : false);
			reportPreferenceBean.setDisApproverName(reportCertificateBean.getDisApproverName());
			reportPreferenceBean.setMaxReportSize(reportCertificateBean.getMaxReportSize());
			reportPreferenceBean.setRejectReasonOther(reportCertificateBean.getRejectReasonOther());
			reportPreferenceBean.setRejectReasonSortBy(reportCertificateBean.getRejectReasonSortBy());
			reportPreferenceBean.setReportContactName(reportCertificateBean.getReportContactName());
			reportPreferenceBean.setWithAttachment(
					reportCertificateBean.getWithAttachment().equalsIgnoreCase("Yes") ? true : false);
			reportPreferenceBean.setSendMailToSupplier(reportCertificateBean.getSendMailToSupplier());
			reportPreferenceBean
					.setSameDayReport(reportCertificateBean.getSameDayReport().equalsIgnoreCase("Yes") ? true : false);
			reportPreferenceBean.setReportTemplate(reportCertificateBean.getReportTemplate());
			reportPreferenceBean.setCertificateType(reportCertificateBean.getIcTemplate());
			List<ApproverBean> approverBeenList = reportCertificateBean.getApprovers();
			if (approverBeenList != null) {
				List<ReportApproverBean> reportApproverBeenList = new ArrayList<ReportApproverBean>();
				for (ApproverBean approverBean : approverBeenList) {
					ReportApproverBean reportApproverBean = new ReportApproverBean();
					reportApproverBean.setApproverName(approverBean.getApproverName());
					reportApproverBean.setApproverPwd(approverBean.getApproverPwd());
					reportApproverBean.setApproverSeq(approverBean.getApproverSeq());
					reportApproverBean.setCreateTime(approverBean.getCreateTime());
					reportApproverBean.setUpdateTime(approverBean.getUpdateTime());
					reportApproverBeenList.add(reportApproverBean);
				}
				reportPreferenceBean.setApprovers(reportApproverBeenList);
			}

			List<RejectCategoryBean> rejectCategoryBeanList = reportCertificateBean.getRejectCategories();
			if (rejectCategoryBeanList != null) {
				List<ReportRejectCategoryBean> reportRejectCategoryBeanList = new ArrayList<ReportRejectCategoryBean>();
				for (RejectCategoryBean rejectCategoryBean : rejectCategoryBeanList) {
					ReportRejectCategoryBean reportRejectCategoryBean = new ReportRejectCategoryBean();
					reportRejectCategoryBean.setUpdateTime(rejectCategoryBean.getUpdateTime());
					reportRejectCategoryBean.setCreateTime(rejectCategoryBean.getCreateTime());
					reportRejectCategoryBean.setRejectCategory(rejectCategoryBean.getRejectCategory());
					reportRejectCategoryBean.setRejectCategorySeq(rejectCategoryBean.getRejectCategorySeq());
					List<RejectCategoryReasonBean> rejectCategoryReasonBeenList = rejectCategoryBean
							.getRejectCategoryReasons();
					if (rejectCategoryReasonBeenList != null) {
						List<ReportRejectCategoryReasonBean> reportRejectReasonList = new ArrayList<ReportRejectCategoryReasonBean>();
						for (RejectCategoryReasonBean rejectReason : rejectCategoryReasonBeenList) {
							ReportRejectCategoryReasonBean reportRejectReason = new ReportRejectCategoryReasonBean();
							reportRejectReason.setRejectCategorySeq(rejectReason.getRejectCategorySeq());
							reportRejectReason.setCreateTime(rejectReason.getCreateTime());
							reportRejectReason.setUpdateTime(rejectReason.getUpdateTime());
							reportRejectReason.setRejectReason(rejectReason.getRejectReason());
							reportRejectReason.setRejectReasonSeq(rejectReason.getRejectReasonSeq());
							reportRejectReasonList.add(reportRejectReason);
						}
						reportRejectCategoryBean.setRejectCategoryReasons(reportRejectReasonList);
					}
					reportRejectCategoryBeanList.add(reportRejectCategoryBean);
				}
				reportPreferenceBean.setRejectCategories(reportRejectCategoryBeanList);
			}
		}
		preferencesBean.setReport(reportPreferenceBean);

		user.setPreferences(preferencesBean);
		logger.info("...........return UserBean from user service...........");
		return user;
	}

	@Override
	public void removeUserProfileCache(String userId) throws IOException, AIException {
		logger.info("removing user profile, userId : " + userId);
		RedisUtil.hdel("userBeanCache", userId);
		logger.info("success removed!!");
	}

	@Override
	public void removeEmployeeProfileCache(String userId) {
		logger.info("removing employee profile, userId : " + userId);
		RedisUtil.hdel("employeeCache", userId);
		logger.info("success removed!!");
	}

	public UserBean updateUserBeanInCache(final String userId) {
		logger.info("ready to update user in cache ! userId:  " + userId);
		if (StringUtils.isBlank(userId)) {
			logger.error("userId is blank!");
			return null;
		}
		UserBean newUserBean = this.getUserBeanByService(userId);
		RedisUtil.hset("userBeanCache", userId, JSON.toJSONString(newUserBean), RedisUtil.HOUR * 2);
		return newUserBean;
	}

	@Override
	public UserBean getCustById(String userId) throws IOException, AIException {
		logger.info("try to get userBean from redis ...");
		if (StringUtils.isBlank(userId)) {
			logger.error("userId is blank,please check!");
			throw new AIException("userId is blank,please check!");
		}
		String jsonStr = RedisUtil.hget("userBeanCache", userId);
		UserBean user = JSON.parseObject(jsonStr, UserBean.class);
		if (user != null && StringUtils.isNotBlank(user.getId())) {
			logger.info("success get userBean from redis.");
			return user;
		} else {
			logger.info("can't find user " + userId + " in cache. Will get from customer service. ");
			user = this.getUserBeanByService(userId);
			logger.info("saving userBean to redis ...");
			RedisUtil.hset("userBeanCache", userId, JSON.toJSONString(user, SerializerFeature.WriteMapNullValue),
					RedisUtil.MINUTE * 30);
			logger.info("saving success !!!");
			return user;
		}
	}

	@Override
	public UserBean updateCompany(CompanyBean newComp, String userId) throws IOException, AIException {
		CompanyBean redisCompany = getCustById(userId).getCompany();
		CrmCompanyBean company = companyDao.getCrmCompany(redisCompany.getId());

		// fill new values
		company.setIndustry(newComp.getIndustry());
		company.setAddress1(newComp.getAddress());
		company.setCity(newComp.getCity());
		company.setPostCode(newComp.getPostcode());
		company.setCountryRegion(newComp.getCountry());
		company.setWebsite(newComp.getWebsite());

		// update
		// return companyDao.updateCrmCompany(company);
		if (companyDao.updateCrmCompany(company)) {
			logger.info("update company finished for user:  " + userId);
			return this.updateUserBeanInCache(userId);
		}
		return null;
	}

	@Override
	public UserBean updateContact(ContactInfoBean newContact, String userId) throws IOException, AIException {
		// get general user bean
		GeneralUserBean user = customerDao.getGeneralUser(userId);
		user.setFollowName(newContact.getMain().getSalutation());
		user.setFirstName(newContact.getMain().getGivenName());
		user.setLastName(newContact.getMain().getFamilyName());
		user.setPersonalEmail(newContact.getMain().getEmail());
		user.setLandline(newContact.getMain().getPhoneNumber());
		user.setMobile(newContact.getMain().getMobileNumber());

		String compId = getCustById(userId).getCompany().getId();

		// get contact bean
		ContactBean contact = companyDao.getCompanyContact(compId);
		contact.setMainPosition(newContact.getMain().getPosition());
		if (newContact.getBilling().isSameAsMainContact()) {
			contact.setAccountingGender(newContact.getMain().getSalutation());
			contact.setAccountingGivenName(newContact.getMain().getGivenName());
			contact.setAccountingName(newContact.getMain().getFamilyName());
			contact.setAccountingEmail(newContact.getMain().getEmail());
		} else {
			contact.setAccountingGender(newContact.getBilling().getSalutation());
			contact.setAccountingGivenName(newContact.getBilling().getGivenName());
			contact.setAccountingName(newContact.getBilling().getFamilyName());
			contact.setAccountingEmail(newContact.getBilling().getEmail());
		}

		// update general user and company contact
		// return customerDao.updateGeneralUser(user) &&
		// companyDao.updateCompanyContact(compId, contact);
		if (customerDao.updateGeneralUser(user) && companyDao.updateCompanyContact(compId, contact)) {
			logger.info("update contact in DB finished ! userId:  " + userId);
			return this.updateUserBeanInCache(userId);
		}
		logger.info("update contact fail !!!!!!");
		return null;
	}

	// @CachePut(value = "userBeanCache", key = "#userId")
	@Override
	public UserBean updateBookingPreference(BookingPreferenceBean newBookingPref, String userId)
			throws IOException, AIException {
		System.out.println("-----orderBookingBean-----" + newBookingPref + "---" + userId);

		// get comp id
		// GeneralUserViewBean generalUserBean =
		// customerDao.getGeneralUserViewBean(userId);
		// if (generalUserBean == null) return null;
		// String compId = generalUserBean.getCompany().getCompanyId();
		String compId = getCustById(userId).getCompany().getId();

		// get booking preference first
		OrderBookingBean booking = companyDao.getCompanyOrderBooking(compId);
		booking.setSendSampleToFactory(
				StringUtils.getYesNo(Boolean.toString(newBookingPref.isShouldSendRefSampleToFactory())));
		booking.setPoCompulsory(StringUtils.getYesNo(Boolean.toString(newBookingPref.getIsPoMandatory())));
		booking.setPsiPercentage(newBookingPref.getMinQuantityToBeReady()[0].getMinQty());
		booking.setDuproPercentage(newBookingPref.getMinQuantityToBeReady()[1].getMinQty());
		booking.setIpcPercentage(newBookingPref.getMinQuantityToBeReady()[2].getMinQty());
		booking.setClcPercentage(newBookingPref.getMinQuantityToBeReady()[3].getMinQty());
		booking.setPmPercentage(newBookingPref.getMinQuantityToBeReady()[4].getMinQty());

		booking.setAllowChangeAql(newBookingPref.getAqlAndSamplingSize().isCanModify() ? "1" : "0");
		booking.setCustomizedSampleLevel(newBookingPref.getAqlAndSamplingSize().getCustomDefaultSampleLevel());
		booking.setMeasurementSampleLevel(newBookingPref.getAqlAndSamplingSize().getMeasurementSampleLevel());
		booking.setCustAqlLevel(newBookingPref.getAqlAndSamplingSize().isUseCustomAQL() ? "Yes" : "No");
		if (!newBookingPref.getAqlAndSamplingSize().isUseCustomAQL()) {
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

		// get extra first
		ExtraBean extra = companyDao.getCompanyExtra(compId);
		extra.setIsDetailedBookingForm(newBookingPref.isUseQuickFormByDefault() ? "Yes" : "No");

		// update order booking and extra
		// return companyDao.updateCompanyExtra(compId, extra) &&
		// companyDao.updateCompanyOrderBooking(compId, booking);
		if (companyDao.updateCompanyExtra(compId, extra) && companyDao.updateCompanyOrderBooking(compId, booking)) {
			logger.info("update booking preference finished for user:  " + userId);
			return this.updateUserBeanInCache(userId);
		}
		return null;
	}

	// @CachePut(value = "userBeanCache", key = "#userId")
	@Override
	public UserBean updateBookingPreferredProductFamily(List<String> newPreferred, String userId)
			throws IOException, AIException {
		// get comp id
		// GeneralUserViewBean generalUserBean =
		// customerDao.getGeneralUserViewBean(userId);
		// String compId = generalUserBean.getCompany().getCompanyId();
		String compId = getCustById(userId).getCompany().getId();

		// get current product family
		ProductFamilyBean family = companyDao.getCompanyProductFamily(compId);
		System.out.println(family.getProductFamilyInfo());

		List<RelevantCategoryInfoBean> infos = new ArrayList<>();

		List<ProductFamilyDtoBean> productFamilyDtoBeanList = paramDao.getProductFamilyList(true);

		int index = 1;
		for (String familyID : newPreferred) {
			// get product category by product family id

			for (int i = 0; i < productFamilyDtoBeanList.size(); i++) {
				ProductFamilyDtoBean productFamilyDtoBean = productFamilyDtoBeanList.get(i);
				if (familyID.equals(productFamilyDtoBean.getId())) {
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
		// return companyDao.updateCompanyProductFamily(compId, family);
		if (companyDao.updateCompanyProductFamily(compId, family)) {
			logger.info("update booking preference finished for user:  " + userId);
			return this.updateUserBeanInCache(userId);
		}
		return null;
	}

	@Override
	public ServiceCallResult updateUserPassword(String userId, HashMap<String, String> pwdMap)
			throws IOException, AIException {
		return customerDao.updateGeneralUserPassword(userId, pwdMap);
	}

	@Override
	public boolean getCompanyLogoByFile(String userId, String companyId, HttpServletResponse httpResponse) {
		try {
			InputStream inputStream = customerDao.getCompanyLogo(companyId);
			ServletOutputStream output = httpResponse.getOutputStream();
			httpResponse.setStatus(HttpServletResponse.SC_OK);
			byte[] buffer = new byte[10240];
			if (null == inputStream)
				return false;
			for (int length = 0; (length = inputStream.read(buffer)) > 0;) {
				output.write(buffer, 0, length);
			}
			return true;
		} catch (Exception e) {
			logger.error("ERROR! from service[getCompanyLogo]" + ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

	@Override
	public String getCompanyLogo(String companyId) {
		try {
			InputStream inputStream = customerDao.getCompanyLogo(companyId);
			if (inputStream != null) {
				byte[] data = IOUtils.toByteArray(inputStream);
				return "data:image/jpg;base64," + Base64.encode(data);
			} else {
				return "";
			}
		} catch (Exception e) {
			logger.error("ERROR! from service[getBase64CompanyLogo]" + ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public boolean updateCompanyLogoByFile(String userId, String companyId, HttpServletRequest request) {
		try {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			Iterator<String> fileNames = multipartHttpServletRequest.getFileNames();
			if (fileNames == null) {
				logger.error("missing upload file!");
			}
			String fileName = fileNames.next();
			MultipartFile file = multipartHttpServletRequest.getFile(fileName);
			return customerDao.updateCompanyLogo(companyId, file);
		} catch (Exception e) {
			logger.error("ERROR!" + ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

	@Override
	public boolean updateCompanyLogo(String userId, String compId, CompanyLogoBean logoBean) {
		try {
			String encoded = logoBean.getEncodedImageStr();
			String[] encodedStrs = encoded.split(",");
			if (encodedStrs.length > 1) {
				encoded = encodedStrs[1].trim();
			}
			byte[] imageByte = Base64.decode(encoded);
			BASE64DecodedMultipartFile base64File = new BASE64DecodedMultipartFile(logoBean.getFileName(),
					logoBean.getFileOriginalName(), imageByte);
			return customerDao.updateCompanyLogo(compId, base64File);
		} catch (Exception e) {
			logger.error("ERROR!" + ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

	@Override
	public boolean deleteCompanyLogo(String userId, String companyId) {
		try {
			return customerDao.deleteCompanyLogo(companyId);
		} catch (Exception e) {
			logger.error("ERROR!" + ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

	@Override
	public boolean createNewAccount(ClientInfoBean clientInfoBean) throws IOException, AIException {
		return customerDao.createNewAccount(clientInfoBean);
	}

	@Override
	public String getLoginByUserId(String userId) {
		String jsonStr = RedisUtil.get(userId);
		String login = null;
		if (StringUtils.isNotBlank(jsonStr)) {
			login = JSON.parseObject(jsonStr).getString("login");
		}
		if (StringUtils.isBlank(login)) {
			try {
				UserBean userBean = this.getCustById(userId);
				login = userBean.getLogin();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return login;
	}

	@Override
	public EmployeeBean getEmployeeProfile(String employeeId, boolean refresh) {
		return customerDao.getEmployeeProfile(employeeId, refresh);
	}

	@Override
	public DashboardBean getUserDashboard(String userId, String startDate, String endDate)
			throws IOException, AIException {
		UserBean userBean = this.getCustById(userId);
		String parentId = "";
		String companyId = "";
		if (null != userBean) {
			parentId = userBean.getCompany().getParentCompanyId();
			if (null == parentId) {
				parentId = "";
			}
			companyId = userBean.getCompany().getId();
		}
		return customerDao.getUserDashboard(userId, parentId, companyId, startDate, endDate);
	}

	@Override
	public ServiceCallResult resetPassword(String login) {
		return customerDao.resetPassword(login);
	}

	@Override
	public boolean isMasterOfSuperMaster(String superUserId, String masterUserId) throws IOException {
		String jsonStr = RedisUtil.hget("userBeanCache", superUserId);
		UserBean user = JSON.parseObject(jsonStr, UserBean.class);
		if (user != null && StringUtils.isNotBlank(user.getId())) {
			logger.info("success get userBean from redis.");
		} else {
			logger.info("can't find user " + superUserId + " in cache. Will get from customer service. ");
			user = this.getUserBeanByService(superUserId);
			logger.info("saving userBean to redis ...");
			RedisUtil.hset("userBeanCache", superUserId, JSON.toJSONString(user, SerializerFeature.WriteMapNullValue),
					RedisUtil.MINUTE * 30);
			logger.info("saving success !!!");
		}
		if (user != null && user.getCompany().getSuperMaster().isSuperMaster()) {
			for (MasterCompanyBean master : user.getCompany().getSuperMaster().getMasterCompanies()) {
				if (master.getUserId().equalsIgnoreCase(masterUserId)) {
					return true;
				}
			}
		}
		return false;
	}
}