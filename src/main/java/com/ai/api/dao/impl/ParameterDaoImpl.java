/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ai.api.bean.ProductCategoryDtoBean;
import com.ai.api.bean.ProductFamilyDtoBean;
import com.ai.api.dao.ParameterDao;
import com.ai.api.config.ServiceConfig;
import com.ai.commons.HttpUtil;
import com.ai.commons.beans.GetRequest;
import com.ai.commons.beans.ServiceCallResult;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
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
	public List<ProductCategoryDtoBean> getProductCategoryList(){

		String SysProductCategoryURL = config.getParamServiceUrl() + "/p/list-product-category";
		GetRequest request7 = GetRequest.newInstance().setUrl(SysProductCategoryURL);

		List<ProductCategoryDtoBean> prodcutCategoryList = new ArrayList<>();

		try{
			ServiceCallResult result = HttpUtil.issueGetRequest(request7);
			JSONArray jsonArray = new JSONArray(result.getResponseString());
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.getJSONObject(i);
				ProductCategoryDtoBean productCategory = new ProductCategoryDtoBean(obj);
				prodcutCategoryList.add(productCategory);
			}

		}catch(IOException e){
			LOGGER.error(Arrays.asList(e.getStackTrace()));
		}
		return prodcutCategoryList;
	}

	@Override
	public List<ProductFamilyDtoBean> getProductFamilyList(){

		String SysProductFamilyBeanURL = config.getParamServiceUrl() +"/p/list-product-family";
		GetRequest request7 = GetRequest.newInstance().setUrl(SysProductFamilyBeanURL);

		List<ProductFamilyDtoBean> productFamilyList = new ArrayList<>();

		try{
			ServiceCallResult result = HttpUtil.issueGetRequest(request7);
			JSONArray jsonArray = new JSONArray(result.getResponseString());
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.getJSONObject(i);
				ProductFamilyDtoBean productFamily = new ProductFamilyDtoBean(obj);
				productFamilyList.add(productFamily);
			}

		}catch(IOException e){
			LOGGER.error(Arrays.asList(e.getStackTrace()));
		}
		return productFamilyList;
	}

}
