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

import com.ai.api.bean.SysProductCategoryBean;
import com.ai.api.bean.SysProductFamilyBean;
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
	public SysProductCategoryBean getSysProductCategory() {
		String SysProductCategoryURL = config.getParamServiceUrl() + "/p/list-product-category";
		GetRequest request7 = GetRequest.newInstance().setUrl(SysProductCategoryURL);
		List<String> id = new ArrayList<>();
		List<String> name = new ArrayList<>();
		SysProductCategoryBean sysProductCategoryBean = new SysProductCategoryBean();

		try {
			ServiceCallResult result = HttpUtil.issueGetRequest(request7);
			JSONArray jsonArray = new JSONArray(result.getResponseString());
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.getJSONObject(i);
				name.add(obj.getString("name"));
				id.add(obj.getString("id"));
			}
			sysProductCategoryBean.setId(id);
			sysProductCategoryBean.setName(name);

			return sysProductCategoryBean;
		} catch (IOException e) {
			LOGGER.error(Arrays.asList(e.getStackTrace()));
		}
		return null;
	}

	@Override
	public SysProductFamilyBean getSysProductFamily() {
		String SysProductFamilyBeanURL = config.getParamServiceUrl() +"/p/list-product-family";
		GetRequest request7 = GetRequest.newInstance().setUrl(SysProductFamilyBeanURL);
		List<String> familycatid = new ArrayList<>();
		List<String> familyid = new ArrayList<>();
		List<String> familyname = new ArrayList<>();
		SysProductFamilyBean sysProductFamilyBean = new SysProductFamilyBean();

		try {
			ServiceCallResult result = HttpUtil.issueGetRequest(request7);
			JSONArray jsonArray = new JSONArray(result.getResponseString());
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.getJSONObject(i);
				familycatid.add(obj.getString("categoryId"));
				familyname.add(obj.getString("name"));
				familyid.add(obj.getString("id"));
			}

			sysProductFamilyBean.setCategoryId(familycatid);
			sysProductFamilyBean.setId(familyid);
			sysProductFamilyBean.setName(familyname);
			return sysProductFamilyBean;

		} catch (IOException e) {
			LOGGER.error(Arrays.asList(e.getStackTrace()));
		}
		return null;
	}
}
