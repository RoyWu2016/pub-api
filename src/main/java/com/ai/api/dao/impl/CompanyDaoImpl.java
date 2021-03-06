/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.dao.impl;

import java.io.IOException;
import java.util.List;

import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.CompanyDao;
import com.ai.commons.HttpUtil;
import com.ai.commons.JsonUtil;
import com.ai.commons.beans.GetRequest;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.customer.*;
import com.ai.commons.beans.legacy.customer.ClientInfoWithTokenBean;
import com.fasterxml.jackson.core.type.TypeReference;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/***************************************************************************
 * <PRE>
 *  Project Name    : api
 *
 *  Package Name    : com.ai.api.dao.impl
 *
 *  File Name       : CompanyDaoImpl.java
 *
 *  Creation Date   : May 25, 2016
 *
 *  Author          : Allen Zhang
 *
 *  Purpose         : TODO
 *
 *
 *  History         : TODO
 *
 * </PRE>
 ***************************************************************************/

public class CompanyDaoImpl implements CompanyDao {

	@Autowired
	@Qualifier("serviceConfig")
	private ServiceConfig config;

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerDaoImpl.class);

	@Override
	public CrmCompanyBean getCrmCompany(String compId) {
		String url = config.getCustomerServiceUrl() + "/customer/" + compId + "/crm-company";
		GetRequest request = GetRequest.newInstance().setUrl(url);
		ServiceCallResult result;
		CrmCompanyBean company;
		try {
			result = HttpUtil.issueGetRequest(request);
			company = JsonUtil.mapToObject(result.getResponseString(), CrmCompanyBean.class);
			return company;
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public boolean updateCrmCompany(CrmCompanyBean company) {
		String url = config.getCustomerServiceUrl() + "/customer/" + company.getCompanyId() + "/crm-company";
		try {
			ServiceCallResult result = HttpUtil.issuePostRequest(url, null, company);
			if (result.getResponseString().equalsIgnoreCase("true")) {
				return true;
			}
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

	@Override
	public OverviewBean getCompanyOverview(String compId) {
		String overviewBeanURL = config.getCustomerServiceUrl() + "/customer/" + compId + "/overview";
		GetRequest request = GetRequest.newInstance().setUrl(overviewBeanURL);
		ServiceCallResult result;
		OverviewBean overviewBean;
		try {
			result = HttpUtil.issueGetRequest(request);
			overviewBean = JsonUtil.mapToObject(result.getResponseString(), OverviewBean.class);
			return overviewBean;
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public ContactBean getCompanyContact(String compId) {

		String contactBeanURL = config.getCustomerServiceUrl() + "/customer/" + compId + "/contact";
		GetRequest request = GetRequest.newInstance().setUrl(contactBeanURL);
		ServiceCallResult result;
		ContactBean contactBean;
		try {
			result = HttpUtil.issueGetRequest(request);
			contactBean = JsonUtil.mapToObject(result.getResponseString(), ContactBean.class);
			return contactBean;
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public boolean updateCompanyContact(String compId, ContactBean newContact) {
		String contactBeanURL = config.getCustomerServiceUrl() + "/customer/" + compId + "/contact";
		try {
			ServiceCallResult result = HttpUtil.issuePostRequest(contactBeanURL, null, newContact);
			if (result.getResponseString().equalsIgnoreCase("true")) {
				return true;
			} else {
				LOGGER.info("update CompanyContact fail! error from customerService :" + result.getResponseString()
						+ " || code:" + result.getStatusCode());
			}
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

	@Override
	public MultiRefBookingBean getCompanyMultiRefBooking(String compId) {
		String orderBBeanURL = config.getCustomerServiceUrl() + "/customer/" + compId + "/multi-ref-booking";
		GetRequest request = GetRequest.newInstance().setUrl(orderBBeanURL);
		try {
			ServiceCallResult result = HttpUtil.issueGetRequest(request);
			MultiRefBookingBean multiRefBookingBean = JsonUtil.mapToObject(result.getResponseString(),
					MultiRefBookingBean.class);
			return multiRefBookingBean;
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public OrderBookingBean getCompanyOrderBooking(String compId) {
		String orderBBeanURL = config.getCustomerServiceUrl() + "/customer/" + compId + "/order-booking";
		GetRequest request = GetRequest.newInstance().setUrl(orderBBeanURL);
		ServiceCallResult result;
		OrderBookingBean orderBookingBean;
		try {
			result = HttpUtil.issueGetRequest(request);
			orderBookingBean = JsonUtil.mapToObject(result.getResponseString(), OrderBookingBean.class);
			return orderBookingBean;
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public ExtraBean getCompanyExtra(String compId) {
		String extraURL = config.getCustomerServiceUrl() + "/customer/" + compId + "/extra";
		GetRequest request = GetRequest.newInstance().setUrl(extraURL);
		ServiceCallResult result;
		ExtraBean extraBean;
		try {
			result = HttpUtil.issueGetRequest(request);
			extraBean = JsonUtil.mapToObject(result.getResponseString(), ExtraBean.class);
			return extraBean;
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}

		return null;
	}

	@Override
	public QualityManualBean getCompanyQualityManual(String compId) {
		String qualitymanualURL = config.getCustomerServiceUrl() + "/customer/" + compId + "/quality-manual";
		GetRequest request = GetRequest.newInstance().setUrl(qualitymanualURL);
		ServiceCallResult result;
		QualityManualBean qualityManualBean;
		try {
			result = HttpUtil.issueGetRequest(request);
			qualityManualBean = JsonUtil.mapToObject(result.getResponseString(), QualityManualBean.class);
			return qualityManualBean;
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public boolean updateCompanyExtra(String compId, ExtraBean extra) {
		String extraURL = config.getCustomerServiceUrl() + "/customer/" + compId + "/extra";

		try {
			ServiceCallResult result = HttpUtil.issuePostRequest(extraURL, null, extra);
			if (result.getResponseString().equalsIgnoreCase("true")) {
				return true;
			}
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

	@Override
	public boolean updateCompanyOrderBooking(String compId, OrderBookingBean booking) {
		String orderBBeanURL = config.getCustomerServiceUrl() + "/customer/" + compId + "/order-booking";
		try {
			ServiceCallResult result = HttpUtil.issuePostRequest(orderBBeanURL, null, booking);
			if (result.getResponseString().equalsIgnoreCase("true")) {
				return true;
			}
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

	@Override
	public ProductFamilyBean getCompanyProductFamily(String compId) {
		String productFamilyURL = config.getCustomerServiceUrl() + "/customer/" + compId + "/product-family";
		GetRequest request = GetRequest.newInstance().setUrl(productFamilyURL);
		ServiceCallResult result;
		ProductFamilyBean productFamilyBean;
		try {
			result = HttpUtil.issueGetRequest(request);
			productFamilyBean = JsonUtil.mapToObject(result.getResponseString(), ProductFamilyBean.class);
			return productFamilyBean;
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public boolean updateCompanyProductFamily(String compId, ProductFamilyBean prodFamily) {
		String productFamilyURL = config.getCustomerServiceUrl() + "/customer/" + compId + "/product-family";
		try {
			ServiceCallResult result = HttpUtil.issuePostRequest(productFamilyURL, null, prodFamily);
			if (result.getResponseString().equalsIgnoreCase("true")) {
				return true;
			}
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

	@Override
	public ReportCertificateBean getCompanyReportCertificateInfo(String companyId) {
		try {
			String url = config.getCustomerServiceUrl() + "/customer/" + companyId + "/report-certificate";
			GetRequest request = GetRequest.newInstance().setUrl(url);
			ServiceCallResult result = HttpUtil.issueGetRequest(request);
			return JsonUtil.mapToObject(result.getResponseString(), ReportCertificateBean.class);
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public CompanyEntireBean getCompanyEntireInfo(String userId) {
		try {
			String url = config.getCustomerServiceUrl() + "/customer/get-companyentire-by-user/" + userId;
			GetRequest request = GetRequest.newInstance().setUrl(url);
			ServiceCallResult result = HttpUtil.issueGetRequest(request);
			return JsonUtil.mapToObject(result.getResponseString(), CompanyEntireBean.class);
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public List<ClientInfoWithTokenBean> getMasterAccountTokens(String companyId, boolean isMobile,
			boolean isClientFactory) {
		try {
			String url = config.getCustomerServiceUrl() + "/customer-legacy/master-account-tokens/" + companyId
					+ "/is-mobile/" + isMobile + "/is-client-factory/" + isClientFactory;
			GetRequest request = GetRequest.newInstance().setUrl(url);
			ServiceCallResult result = HttpUtil.issueGetRequest(request);
			return JsonUtil.mapToObject(result.getResponseString(), new TypeReference<List<ClientInfoWithTokenBean>>() {
			});
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public CompanyEntireBean getCompanyEntireInfoByCompanyId(String companyId) {
		try {
			String url = config.getCustomerServiceUrl() + "/customer/get-companyentire-by-company/" + companyId;
			GetRequest request = GetRequest.newInstance().setUrl(url);
			ServiceCallResult result = HttpUtil.issueGetRequest(request);
			return JsonUtil.mapToObject(result.getResponseString(), CompanyEntireBean.class);
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public MasterBean getMasterBeanByCompanyId(String companyId) {
		try {
			String url = config.getCustomerServiceUrl() + "/customer/" + companyId + "/master" ;
			GetRequest request = GetRequest.newInstance().setUrl(url);
			ServiceCallResult result = HttpUtil.issueGetRequest(request);
			return JsonUtil.mapToObject(result.getResponseString(), MasterBean.class);
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
}
