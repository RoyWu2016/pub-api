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
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import com.ai.api.bean.EmployeeBean;
import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.CustomerDao;
import com.ai.api.util.RedisUtil;
import com.ai.commons.HttpUtil;
import com.ai.commons.JsonUtil;
import com.ai.commons.StringUtils;
import com.ai.commons.beans.GetRequest;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.customer.ContactBean;
import com.ai.commons.beans.customer.DashboardBean;
import com.ai.commons.beans.customer.GeneralUserViewBean;
import com.ai.commons.beans.legacy.customer.ClientInfoBean;
import com.ai.commons.beans.user.GeneralUserBean;
import com.alibaba.fastjson.JSON;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

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

public class CustomerDaoImpl implements CustomerDao {
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
		System.out.println("xx: " + JSON.toJSONString(newUser));
		try {
			ServiceCallResult result = HttpUtil.issuePostRequest(url, null, newUser);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getResponseString().isEmpty()
					&& result.getReasonPhase().equalsIgnoreCase("OK")) {
				return true;
			} else {
				LOGGER.info("update user fail! error from customerService :" + result.getResponseString() + " || code:"
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
			LOGGER.info("get companyLogo doGet----url:[" + url + "]");
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
			LOGGER.error("ERROR from-Dao[getCompanyLogo]", e);
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
			LOGGER.info("update companyLogo doPost----url:[" + url + "]");
			response = httpClient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == 200) {
				b = true;
			}
			String responseJson = EntityUtils.toString(response.getEntity(), "UTF-8");
			LOGGER.info("update logo responseJson:" + responseJson);
		} catch (Exception e) {
			LOGGER.error("ERROR", e);
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
			LOGGER.info("delete companyLogo doDelete----url:[" + url + "]");
			response = httpClient.execute(httpDelete);
			if (response.getStatusLine().getStatusCode() == 200) {
				b = true;
			}
			String responseJson = EntityUtils.toString(response.getEntity(), "UTF-8");
			LOGGER.info("delete logo responseJson:" + responseJson);

		} catch (Exception e) {
			LOGGER.error("ERROR", e);
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
	public EmployeeBean getEmployeeProfile(String employeeId, boolean refresh) {
		// TODO Auto-generated method stub
		EmployeeBean generalUserBean = null;
		if (!refresh) {
			LOGGER.info("try to getEmployeeProfile from redis ...");
			// String jsonString = RedisUtil.get("employeeCache");
			String jsonString = RedisUtil.hget("employeeCache", employeeId);
			if (null != jsonString) {
				generalUserBean = JSON.parseObject(jsonString).toJavaObject(EmployeeBean.class);
			}
		}
		StringBuilder sb = new StringBuilder(config.getSsoUserServiceUrl() + "/user/" + employeeId);
		sb.append("?deep=true");
		GetRequest request = GetRequest.newInstance().setUrl(sb.toString());
		try {
			if (null == generalUserBean) {
				LOGGER.info("requesting url: " + sb.toString());
				ServiceCallResult result = HttpUtil.issueGetRequest(request);
				if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
					if (StringUtils.isBlank(result.getResponseString())) {
						LOGGER.error("getEmployeeProfile from user-service response 200  but EmployeeBean is null");
						return null;
					}
					generalUserBean = JsonUtil.mapToObject(result.getResponseString(), EmployeeBean.class);
					LOGGER.info("saving employee into redis employee id: " + employeeId);
					RedisUtil.hset("employeeCache", employeeId, JSON.toJSONString(generalUserBean), RedisUtil.HOUR * 2);
					// RedisUtil.set("employeeCache",
					// JSON.toJSONString(generalUserBean),RedisUtil.HOUR * 2);
					return generalUserBean;
				} else {
					LOGGER.error("getEmployeeProfile from user-service error: " + result.getStatusCode() + ", "
							+ result.getResponseString());
					return null;
				}
			} else {
				LOGGER.info("get employee from redis successfully employee id: " + employeeId);
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
		url.append("?userId=" + userId).append("&companyId=" + companyId).append("&parentId=" + parentId)
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

	@Override
	public ServiceCallResult resetPassword(String login) {
		// TODO Auto-generated method stub
		try {
			String loginCode = URLEncoder.encode(login, "UTF-8");
			StringBuilder url = new StringBuilder(
					config.getCustomerServiceUrl() + "/customer-legacy/get-lost-login-password-new");
			url.append("?login=" + loginCode).append("&email=" + "");
			LOGGER.info("requesting url: " + url.toString());
			GetRequest request = GetRequest.newInstance().setUrl(url.toString());
			ServiceCallResult result = HttpUtil.issueGetRequest(request);

			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean checkIfUserNameExist(String userName) {
		// TODO Auto-generated method stub
		StringBuilder url = new StringBuilder(config.getCustomerServiceUrl() + "/customer-legacy/is-login-exist?login=");
		try {
//			String login = URLEncoder.encode(userName, "UTF-8");
			url.append(userName);
			LOGGER.info("requseting: " + url.toString());
			GetRequest request = GetRequest.newInstance().setUrl(url.toString());
			ServiceCallResult result = HttpUtil.issueGetRequest(request);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				LOGGER.info("Check if ACA Accessible login:" + userName + " || result:" + result.getResponseString());
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
	public ContactBean getCustomerContact(String customerId) {
		String url = new StringBuilder(config.getCustomerServiceUrl()).append("/customer/{customerId}/contact").toString();
		url = UriComponentsBuilder.fromUriString(url).buildAndExpand(customerId).toString();
		GetRequest request = GetRequest.newInstance().setUrl(url);
		try {
			ServiceCallResult result = HttpUtil.issueGetRequest(request);
			ContactBean contact = JsonUtil.mapToObject(result.getResponseString(), ContactBean.class);
			return contact;
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
			return new ContactBean();
		}
		
	}
}
