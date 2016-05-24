/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.dao.impl;

import static com.ai.api.dao.impl.sql.Get.GET_CUSTOMER_ID_BY_LOGIN;

import java.io.IOException;
import java.util.Arrays;

import com.ai.api.dao.CustomerDao;
import com.ai.api.dao.impl.db.UserDaoImpl;
import com.ai.api.exception.AIException;
import com.ai.api.service.ServiceConfig;
import com.ai.commons.HttpUtil;
import com.ai.commons.JsonUtil;
import com.ai.commons.beans.GetRequest;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.customer.ContactBean;
import com.ai.commons.beans.customer.ExtraBean;
import com.ai.commons.beans.customer.GeneralUserViewBean;
import com.ai.commons.beans.customer.OrderBookingBean;
import com.ai.commons.beans.customer.OverviewBean;
import com.ai.commons.beans.customer.ProductFamilyBean;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
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


	@Override
	public String getCustomerIdByCustomerLogin(String login) throws AIException {
		try {
			return getJdbcTemplate().queryForObject(GET_CUSTOMER_ID_BY_LOGIN, new Object[]{login},
					String.class);
		} catch (EmptyResultDataAccessException ee) {
			LOGGER.info("Customer " + login + " not found");
			return "";
		} catch (Exception e) {
			throw new AIException(UserDaoImpl.class, e.getMessage(), e);
		}
	}

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
	public OverviewBean getCompanyOverview(String customer_id) {
		return null;
	}

	@Override
	public ContactBean getCompanyContact(String comp_id) {
		return null;
	}

	@Override
	public OrderBookingBean getCompanyOrderBooking(String comp_id) {
		return null;
	}

	@Override
	public ExtraBean getCompanyExtra(String comp_id) {
		return null;
	}

	@Override
	public ProductFamilyBean getCompanyProductFamily(String comp_id) {
		return null;
	}
}
