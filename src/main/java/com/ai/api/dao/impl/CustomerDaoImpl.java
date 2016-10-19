/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.dao.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.web.multipart.MultipartFile;

import com.ai.api.bean.EmployeeBean;
import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.CustomerDao;
import com.ai.api.util.RedisUtil;
import com.ai.commons.HttpUtil;
import com.ai.commons.JsonUtil;
import com.ai.commons.StringUtils;
import com.ai.commons.beans.GetRequest;
import com.ai.commons.beans.PageBean;
import com.ai.commons.beans.PageParamBean;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.customer.DashboardBean;
import com.ai.commons.beans.customer.GeneralUserViewBean;
import com.ai.commons.beans.legacy.customer.ClientInfoBean;
import com.ai.commons.beans.payment.GlobalPaymentInfoBean;
import com.ai.commons.beans.payment.PaymentSearchResultBean;
import com.ai.commons.beans.payment.api.PaymentActionLogBean;
import com.ai.commons.beans.psi.InspectionProductBookingBean;
import com.ai.commons.beans.psi.report.ClientReportSearchBean;
import com.ai.commons.beans.user.GeneralUserBean;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;

/***************************************************************************
 * <PRE>
 * Project Name    : api
 * <p/>
 * Package Name    : com.ai.api.dao.impl
 * <p/>
 * File Name       : CustomerDaoImpl.java
 * <p/>
 * Creation Date   : May 24, 2016
 * <p/>
 * Author          : Allen Zhang
 * <p/>
 * Purpose         : all call to customer service should be done here,
 * we don't call database directly, we call services instead
 * <p/>
 * <p/>
 * History         : TODO
 * <p/>
 * </PRE>
 ***************************************************************************/

public class CustomerDaoImpl extends JdbcDaoSupport implements CustomerDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerDaoImpl.class);

	@Autowired
	@Qualifier("serviceConfig")
	private ServiceConfig config;

	@Override
	public GeneralUserViewBean getGeneralUserViewBean(String userId) {

		String generalUVBeanURL = config.getCustomerServiceUrl() + "/users/" + userId;
		GetRequest request = GetRequest.newInstance().setUrl(generalUVBeanURL);
		ServiceCallResult result;
		GeneralUserViewBean generalUserBean;
		try {
			result = HttpUtil.issueGetRequest(request);
			generalUserBean = JsonUtil.mapToObject(result.getResponseString(), GeneralUserViewBean.class);
			return generalUserBean;
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public GeneralUserBean getGeneralUser(String userId) {

		String generalUVBeanURL = config.getCustomerServiceUrl() + "/users/general-user/" + userId;
		GetRequest request = GetRequest.newInstance().setUrl(generalUVBeanURL);
		ServiceCallResult result;
		GeneralUserBean generalUserBean;
		try {
			result = HttpUtil.issueGetRequest(request);
			generalUserBean = JsonUtil.mapToObject(result.getResponseString(), GeneralUserBean.class);
			return generalUserBean;
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public boolean updateGeneralUser(GeneralUserBean newUser) {
		String url = config.getCustomerServiceUrl() + "/users/" + newUser.getUserId() + "/general-user";
		try {
			ServiceCallResult result = HttpUtil.issuePostRequest(url, null, newUser);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getResponseString().isEmpty()
					&& result.getReasonPhase().equalsIgnoreCase("OK")) {
				return true;
			} else {
				logger.info("update user fail! error from customerService :" + result.getResponseString() + " || code:"
						+ result.getStatusCode());
			}
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

	@Override
	public ServiceCallResult updateGeneralUserPassword(String userId, HashMap<String, String> pwdMap) {
		String url = config.getCustomerServiceUrl() + "/users/general-user/" + userId + "/password";

		try {
			return HttpUtil.issuePostRequest(url, null, pwdMap);

		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
			ServiceCallResult result = new ServiceCallResult();
			result.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			result.setReasonPhase("error when updating user password.");
			result.setResponseString("error when updating user password.");
			return result;
		}

	}

	@Override
	public InputStream getCompanyLogo(String companyId) {
		String url = config.getCustomerServiceUrl() + "/customer/" + companyId + "/logo";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		InputStream inputStream = null;
		try {
			HttpGet httpGet = new HttpGet(url);
			RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(60000)
					.setSocketTimeout(60000).setConnectTimeout(60000).build();
			httpGet.setConfig(requestConfig);
			logger.info("get companyLogo doGet----url:[" + url + "]");
			response = httpClient.execute(httpGet);
			if (null == response)
				return null;
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				inputStream = entity.getContent();
				// EntityUtils.consume(entity);
			}
			// String responseJson = EntityUtils.toString(response.getEntity(),
			// "UTF-8");
			// logger.info("get logo responseJson:"+responseJson);
		} catch (Exception e) {
			logger.error("ERROR from-Dao[getCompanyLogo]", e);
		} /*
			 * finally { try { if (response != null) { response.close(); } if
			 * (httpClient != null) { httpClient.close(); } }catch (Exception
			 * e){
			 * 
			 * } }
			 */
		return inputStream;
	}

	@Override
	public boolean updateCompanyLogo(String companyId, MultipartFile file) {
		String url = config.getCustomerServiceUrl() + "/customer/" + companyId + "/logo-stream/file-name/"
				+ file.getOriginalFilename() + "/file-size/" + file.getSize();
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		boolean b = false;
		try {
			HttpPost httpPost = new HttpPost(url);
			RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(60000)
					.setConnectTimeout(60000).setSocketTimeout(60000).build();
			EntityBuilder entity = EntityBuilder.create();
			entity.setStream(file.getInputStream());

			httpPost.setEntity(entity.build());
			httpPost.setConfig(requestConfig);
			logger.info("update companyLogo doPost----url:[" + url + "]");
			response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == 200) {
				b = true;
			}
			String responseJson = EntityUtils.toString(response.getEntity(), "UTF-8");
			logger.info("update logo responseJson:" + responseJson);
		} catch (Exception e) {
			logger.error("ERROR", e);
		} /*
			 * finally { try { if (response != null) { response.close(); } if
			 * (httpClient != null) { httpClient.close(); } }catch (Exception
			 * e){
			 * 
			 * } }
			 */
		return b;
	}

	@Override
	public boolean deleteCompanyLogo(String companyId) {
		String url = config.getCustomerServiceUrl() + "/customer/" + companyId + "/logo";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		boolean b = false;
		try {
			HttpDelete httpDelete = new HttpDelete(url);
			RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(60000)
					.setSocketTimeout(60000).setConnectTimeout(60000).build();
			httpDelete.setConfig(requestConfig);
			logger.info("delete companyLogo doDelete----url:[" + url + "]");
			response = httpClient.execute(httpDelete);
			if (response.getStatusLine().getStatusCode() == 200) {
				b = true;
			}
			String responseJson = EntityUtils.toString(response.getEntity(), "UTF-8");
			logger.info("delete logo responseJson:" + responseJson);

		} catch (Exception e) {
			logger.error("ERROR", e);
		} /*
			 * finally { try { if (response != null) { response.close(); } if
			 * (httpClient != null) { httpClient.close(); } }catch (Exception
			 * e){
			 * 
			 * } }
			 */
		return b;
	}

	@Override
	public boolean createNewAccount(ClientInfoBean clientInfoBean) {
		String url = config.getCustomerServiceUrl() + "/customer-legacy/create-new-client-account?clientType=AFI";
		try {
			// String jsonStr = JsonUtil.mapToJson(clientInfoBean);
			// url = url + "?clientInfo="+jsonStr;
			ServiceCallResult result = HttpUtil.issuePostRequest(url, null, clientInfoBean);
			// ServiceCallResult result = HttpUtil.issuePostRequest(url, null,
			// jsonStr);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				return true;
			}
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

	@Override
	public PageBean<PaymentSearchResultBean> searchPaymentList(PageParamBean criteria, String userId, String parentId,
			String companyId, String paid) {
		try {
			StringBuilder url = new StringBuilder(config.getPsiServiceUrl() + "/payment/api/search");
			LOGGER.info("searchPaymentList json before encoding: " + JsonUtil.mapToJson(criteria));
			String param = URLEncoder.encode(JsonUtil.mapToJson(criteria), "utf-8");
			LOGGER.info("searchPaymentList json after encoding: " + param);
			url.append("?userId=" + userId).append("&companyId=" + companyId).append("&parentId=" + parentId)
					.append("&isPaid=" + paid).append("&param=" + param);
			LOGGER.info("Requesting url: " + url.toString());
			ServiceCallResult result = HttpUtil.issueGetRequest(url.toString(), null);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				return JSON.parseObject(result.getResponseString(), PageBean.class);
			} else {
				logger.error("searchPaymentList from psi error: " + result.getStatusCode() + ", "
						+ result.getResponseString());
			}
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public String createProformaInvoice(String userId, String login, String orders) {
		try {
			String url = config.getMwServiceUrl() + "/service/payment/proformaInvoice";
			Map<String, String> dataMap = new HashMap<String, String>();
			dataMap.put("userId", userId);
			dataMap.put("login", login);
			dataMap.put("orders", orders);
			ServiceCallResult result = HttpUtil.issuePostRequest(url, null, dataMap);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				return result.getResponseString();
			} else {
				logger.error("Generate Proforma Invoice For Given Orders from middleware error: "
						+ result.getStatusCode() + ", " + result.getResponseString());
			}
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public boolean reissueProFormaInvoice(String userId, String login, String orders) {
		try {
			String url = config.getMwServiceUrl() + "/service/payment/reissueProformaInvoice";
			Map<String, String> dataMap = new HashMap<String, String>();
			dataMap.put("userId", userId);
			dataMap.put("login", login);
			dataMap.put("orders", orders);
			ServiceCallResult result = HttpUtil.issuePutRequest(url, null, dataMap);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				return true;
			} else {
				logger.error("Reissue Proforma Invoice For Given Orders from middleware error: "
						+ result.getStatusCode() + ", " + result.getResponseString());
			}
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

	@Override
	public List<GlobalPaymentInfoBean> generateGlobalPayment(String userId, String parentId, String orders) {
		StringBuilder url = new StringBuilder(config.getPsiServiceUrl() + "/payment/api/global-payment-list");
		url.append("?parentId=" + parentId);
		logger.info("requesting url: " + url.toString());
		String[] ids = orders.split(",");
		List<String> oderIds = new ArrayList<String>();
		oderIds = Arrays.asList(ids);
		try {
			ServiceCallResult result = HttpUtil.issuePostRequest(url.toString(), null, oderIds);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				return JsonUtil.mapToObject(result.getResponseString(),
						new TypeReference<List<GlobalPaymentInfoBean>>() {
						});
			} else {
				logger.error("save draft error from psi service : " + result.getStatusCode() + ", "
						+ result.getResponseString());
			}
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public boolean logPaymentAction(String userId, PaymentActionLogBean logBean) {
		try {
			String url = config.getMwServiceUrl() + "/service/payment/log";
			ServiceCallResult result = HttpUtil.issuePostRequest(url, null, logBean);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				return true;
			} else {
				logger.error("Log Payment Action from middleware error: " + result.getStatusCode() + ", "
						+ result.getResponseString());
			}
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

	@Override
	public EmployeeBean getEmployeeProfile(String employeeId,boolean refresh) {
		// TODO Auto-generated method stub
		EmployeeBean generalUserBean = null;
		if(!refresh) {
			LOGGER.info("try to getEmployeeProfile from redis ...");
			// String jsonString = RedisUtil.get("employeeCache");
			String jsonString = RedisUtil.hget("employeeCache", employeeId);
			if (null != jsonString) {
				generalUserBean = JSON.parseObject(jsonString).toJavaObject(EmployeeBean.class);
			}
		}
		StringBuilder sb = new StringBuilder("https://202.66.128.138:8491/user-service/user/" + employeeId);
		GetRequest request = GetRequest.newInstance().setUrl(sb.toString());
		try {
			if (null == generalUserBean) {
				logger.info("requesting url: " + sb.toString());
				ServiceCallResult result = HttpUtil.issueGetRequest(request);
				if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
					if (StringUtils.isBlank(result.getResponseString())) {
						logger.error("getEmployeeProfile from user-service response 200  but EmployeeBean is null");
						return null;
					}
					generalUserBean = JsonUtil.mapToObject(result.getResponseString(), EmployeeBean.class);
					logger.info("saving employee into redis employee id: " + employeeId);
					RedisUtil.hset("employeeCache", employeeId, JSON.toJSONString(generalUserBean), RedisUtil.HOUR * 2);
					// RedisUtil.set("employeeCache",
					// JSON.toJSONString(generalUserBean),RedisUtil.HOUR * 2);
					return generalUserBean;
				} else {
					logger.error("getEmployeeProfile from user-service error: " + result.getStatusCode() + ", "
							+ result.getResponseString());
					return null;
				}
			} else {
				logger.info("get employee from redis successfully employee id: " + employeeId);
				return generalUserBean;
			}
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public boolean isACAUser(String login) {
		StringBuilder url = new StringBuilder(config.getCustomerServiceUrl()).append("/customer-legacy/is-access-aca");
		try {
			if (StringUtils.isNotBlank(login)) {
				url = url.append("?login=").append(login);
			}
			GetRequest request = GetRequest.newInstance().setUrl(url.toString());
			ServiceCallResult result = HttpUtil.issueGetRequest(request);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				LOGGER.info("Check if ACA Accessible login:" + login + " || result:" + result.getResponseString());
				if (result.getResponseString().equalsIgnoreCase("true")) {
					return true;
				} else {
					return false;
				}
			}
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

	@Override
	public DashboardBean getUserDashboard(String userId, String parentId, String companyId, String startDate,
			String endDate) {
		// TODO Auto-generated method stub
		StringBuilder url = new StringBuilder(config.getPsiServiceUrl() + "/dashBoard/statistics");
		url.append("?userId=" + userId).append("&companyId=" + parentId).append("&parentId=" + companyId)
				.append("&startDate=" + startDate).append("&endDate=" + endDate);
		LOGGER.info("requesting url: " + url.toString());
		GetRequest request = GetRequest.newInstance().setUrl(url.toString());
		try {
			ServiceCallResult result = HttpUtil.issueGetRequest(request);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				return JsonUtil.mapToObject(result.getResponseString(), DashboardBean.class);
			} else {
				LOGGER.error("getUserDashboard from psi-service error:" + result.getStatusCode() + ", "
						+ result.getResponseString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
