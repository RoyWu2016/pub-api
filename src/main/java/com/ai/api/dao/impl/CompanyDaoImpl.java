/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.dao.impl;

import java.io.IOException;
import java.util.Arrays;

import com.ai.api.dao.CompanyDao;
import com.ai.api.service.ServiceConfig;
import com.ai.commons.HttpUtil;
import com.ai.commons.JsonUtil;
import com.ai.commons.beans.GetRequest;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.customer.CrmCompanyBean;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/***************************************************************************
 *<PRE>
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
 *</PRE>
 ***************************************************************************/

public class CompanyDaoImpl implements CompanyDao {

	@Autowired
	@Qualifier("serviceConfig")
	private ServiceConfig config;

	private static final Logger LOGGER = Logger.getLogger(CustomerDaoImpl.class);

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
			LOGGER.error(Arrays.asList(e.getStackTrace()));
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
			LOGGER.error(Arrays.asList(e.getStackTrace()));
		}
		return false;
	}

}
