/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.dao.impl;

import java.io.IOException;
import java.util.Arrays;

import com.ai.api.dao.ParameterDao;
import com.ai.api.service.ServiceConfig;
import com.ai.commons.HttpUtil;
import com.ai.commons.beans.GetRequest;
import com.ai.commons.beans.ServiceCallResult;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/***************************************************************************
 *<PRE>
 *  Project Name    : api
 *
 *  Package Name    : com.ai.api.dao.impl
 *
 *  File Name       : ParameterDaoImpl.java
 *
 *  Creation Date   : May 24, 2016
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

public class ParameterDaoImpl implements ParameterDao {
	private static final Logger LOGGER = Logger.getLogger(ParameterDaoImpl.class);

	@Autowired
	@Qualifier("serviceConfig")
	private ServiceConfig config;

	@Override
	public String getProductCategoryList(){

		String SysProductCategoryURL = config.getParamServiceUrl() + "/p/list-product-category";
		GetRequest request7 = GetRequest.newInstance().setUrl(SysProductCategoryURL);

		try{
			ServiceCallResult result = HttpUtil.issueGetRequest(request7);
			return result.getResponseString();
		}catch(IOException e){
			LOGGER.error(Arrays.asList(e.getStackTrace()));
		}
		return null;
	}

	@Override
	public String getProductFamilyList(){

		String SysProductFamilyBeanURL = config.getParamServiceUrl() +"/p/list-product-family";
		GetRequest request7 = GetRequest.newInstance().setUrl(SysProductFamilyBeanURL);

		try{
			ServiceCallResult result = HttpUtil.issueGetRequest(request7);
			return result.getResponseString();
		}catch(IOException e){
			LOGGER.error(Arrays.asList(e.getStackTrace()));
		}
		return null;
	}

}
