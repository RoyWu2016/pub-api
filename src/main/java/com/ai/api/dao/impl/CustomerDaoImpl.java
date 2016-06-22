/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.dao.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import com.ai.api.dao.CustomerDao;
import com.ai.api.config.ServiceConfig;
import com.ai.commons.HttpUtil;
import com.ai.commons.JsonUtil;
import com.ai.commons.beans.GetRequest;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.customer.GeneralUserViewBean;
import com.ai.commons.beans.user.GeneralUserBean;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

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
	private static final Logger LOGGER = Logger.getLogger(CustomerDaoImpl.class);

	@Autowired
	@Qualifier("serviceConfig")
	private ServiceConfig config;


//	@Override
//	public String getCustomerIdByCustomerLogin(String login) throws AIException {
//		try {
//			return getJdbcTemplate().queryForObject(GET_CUSTOMER_ID_BY_LOGIN, new Object[]{login},
//					String.class);
//		} catch (EmptyResultDataAccessException ee) {
//			LOGGER.info("Customer " + login + " not found");
//			return "";
//		} catch (Exception e) {
//			throw new AIException(UserDaoImpl.class, e.getMessage(), e);
//		}
//	}

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
		} catch (IOException e) {
			LOGGER.error(Arrays.asList(e.getStackTrace()));
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
			LOGGER.error(Arrays.asList(e.getStackTrace()));
		}
		return null;
	}

	@Override
	public boolean updateGeneralUser(GeneralUserBean newUser) {
		String url = config.getCustomerServiceUrl() + "/users/" + newUser.getUserId() + "/general-user";
		try {
			ServiceCallResult result = HttpUtil.issuePostRequest(url, null, newUser);
			if (result.getStatusCode() == HttpStatus.OK.value() &&
					result.getResponseString().isEmpty() &&
					result.getReasonPhase().equalsIgnoreCase("OK")) {

				return true;
			}
		} catch (IOException e) {
			LOGGER.error(Arrays.asList(e.getStackTrace()));
		}
		return false;
	}

	@Override
	public int updateGeneralUserPassword(String userId, HashMap<String,String> pwdMap) {
		String url = config.getCustomerServiceUrl() + "/users/general-user/" + userId + "/password";
		try {
			ServiceCallResult result = HttpUtil.issuePostRequest(url, null, pwdMap);
			return result.getStatusCode();

		} catch (IOException e) {
			LOGGER.error(Arrays.asList(e.getStackTrace()));
			return HttpStatus.BAD_REQUEST.value();
		}

	}

}
