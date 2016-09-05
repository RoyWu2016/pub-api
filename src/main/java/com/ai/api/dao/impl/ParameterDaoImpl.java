/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ai.api.bean.ProductCategoryDtoBean;
import com.ai.api.bean.ProductFamilyDtoBean;
import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.ParameterDao;
import com.ai.api.util.RedisUtil;
import com.ai.commons.HttpUtil;
import com.ai.commons.beans.GetRequest;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.checklist.vo.CKLDefectVO;
import com.ai.commons.beans.checklist.vo.CKLTestVO;
import com.ai.commons.beans.params.ChecklistTestSampleSizeBean;
import com.ai.commons.beans.params.product.SysProductTypeBean;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;

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
	private static final Logger LOGGER = LoggerFactory.getLogger(ParameterDaoImpl.class);

	@Autowired
	@Qualifier("serviceConfig")
	private ServiceConfig config;

	@Override
//	@Cacheable(value="productCategoryListCache", key="#root.methodName")
	public List<ProductCategoryDtoBean> getProductCategoryList(){

        List<ProductCategoryDtoBean> productCategoryList = new ArrayList<>();
        try {
            LOGGER.info("try to getProductCategoryList from redis ...");
            String jsonString = RedisUtil.get("productCategoryListCache");
            productCategoryList = JSON.parseArray(jsonString, ProductCategoryDtoBean.class);
            if (productCategoryList.size()>0){
                LOGGER.info("success getProductCategoryList from redis");
                return productCategoryList;
            }
        }catch (Exception e){
            LOGGER.error("error getting productCategoryList from redis",e);
        }

		String SysProductCategoryURL = config.getParamServiceUrl() + "/p/list-product-category";
		GetRequest request7 = GetRequest.newInstance().setUrl(SysProductCategoryURL);

		try{
			ServiceCallResult result = HttpUtil.issueGetRequest(request7);
            productCategoryList = JSON.parseArray(result.getResponseString(),ProductCategoryDtoBean.class);
//			JSONArray jsonArray =new JSONArray(result.getResponseString());
//			for (int i = 0; i < jsonArray.length(); i++) {
//				JSONObject obj = jsonArray.getJSONObject(i);
//				ProductCategoryDtoBean productCategory = new ProductCategoryDtoBean(obj);
//				produttCategoryList.add(productCategory);
//			}

		}catch(IOException e){
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		try {
			LOGGER.info("saving productCategoryListCache");
            RedisUtil.set("productCategoryListCache",JSON.toJSONString(productCategoryList));
		}catch (Exception e){
            LOGGER.error("error saving productCategoryListCache ",e);
        }
		return productCategoryList;
	}

	@Override
//	@Cacheable(value="productFamilyListCache", key="#root.methodName")
	public List<ProductFamilyDtoBean> getProductFamilyList(){
        List<ProductFamilyDtoBean> productFamilyList = new ArrayList<>();
        try {
            LOGGER.info("try to getProductFamilyList from redis ...");
            String jsonString = RedisUtil.get("productFamilyListCache");
            productFamilyList = JSON.parseArray(jsonString, ProductFamilyDtoBean.class);
            if (productFamilyList.size()>0){
                LOGGER.info("success getProductFamilyList from redis");
                return productFamilyList;
            }
        }catch (Exception e){
            LOGGER.error("error getting productFamilyList from redis",e);
        }

		String SysProductFamilyBeanURL = config.getParamServiceUrl() +"/p/list-product-family";
		GetRequest request7 = GetRequest.newInstance().setUrl(SysProductFamilyBeanURL);
		try{
			ServiceCallResult result = HttpUtil.issueGetRequest(request7);
			productFamilyList = JSON.parseArray(result.getResponseString(),ProductFamilyDtoBean.class);
//			JSONArray jsonArray = new JSONArray(result.getResponseString());
//			for (int i = 0; i < jsonArray.length(); i++) {
//				JSONObject obj = jsonArray.getJSONObject(i);
//				ProductFamilyDtoBean productFamily = new ProductFamilyDtoBean(obj);
//				productFamilyList.add(productFamily);
//			}

		}catch(IOException e){
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
        try {
            LOGGER.info("saving productFamilyListCache");
            RedisUtil.set("productFamilyListCache",JSON.toJSONString(productFamilyList));
        }catch (Exception e){
            LOGGER.error("error saving productFamilyListCache ",e);
        }
		return productFamilyList;
	}

	@Override
//	@Cacheable(value="countryListCache", key="#root.methodName")
	public List<String> getCountryList(){
        List<String> countryList = new ArrayList<>();
        String SysProductFamilyBeanURL = config.getParamServiceUrl() +"/p/list-country";
		GetRequest request = GetRequest.newInstance().setUrl(SysProductFamilyBeanURL);
		try{
			ServiceCallResult result = HttpUtil.issueGetRequest(request);
			countryList = JSON.parseArray(result.getResponseString(),String.class);

		}catch(IOException e){
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return countryList;
	}

	@Override
	public Map<String,List<ChecklistTestSampleSizeBean>> getTestSampleSizeList(){
		String baseUrl = config.getParamServiceUrl() +"/systemconfig/classified/list/";
		Map<String,List<ChecklistTestSampleSizeBean>> resultMap = new HashMap<>();
		try{
			String url = baseUrl + "CHECKLIST_TEST_SAMPLE_LEVEL_BY_PIECES_NO";
			GetRequest request = GetRequest.newInstance().setUrl(url);
			ServiceCallResult result = HttpUtil.issueGetRequest(request);
			JSONObject object = JSONObject.parseObject(result.getResponseString());
			Object  arrayStr = object.get("content");
			List<ChecklistTestSampleSizeBean> list = JSON.parseArray(arrayStr+"",ChecklistTestSampleSizeBean.class);
			resultMap.put("CHECKLIST_TEST_SAMPLE_LEVEL_BY_PIECES_NO",list);

			url = baseUrl + "CHECKLIST_TEST_SAMPLE_LEVEL_BY_LEVEL";
			request = GetRequest.newInstance().setUrl(url);
			result = HttpUtil.issueGetRequest(request);
			object = JSONObject.parseObject(result.getResponseString());
			arrayStr =  object.get("content");
			list = JSON.parseArray(arrayStr+"",ChecklistTestSampleSizeBean.class);
			resultMap.put("CHECKLIST_TEST_SAMPLE_LEVEL_BY_LEVEL",list);

			url = baseUrl + "CHECKLIST_TEST_FABRIC_SAMPLE_LEVEL";
			request = GetRequest.newInstance().setUrl(url);
			result = HttpUtil.issueGetRequest(request);
			object = JSONObject.parseObject(result.getResponseString());
			arrayStr = object.get("content");
			list = JSON.parseArray(arrayStr+"",ChecklistTestSampleSizeBean.class);
			resultMap.put("CHECKLIST_TEST_FABRIC_SAMPLE_LEVEL",list);

		}catch(IOException e){
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return resultMap;
	}

	@Override
	public List<CKLTestVO> getChecklistPublicTestList(){
        String url = config.getChecklistServiceUrl() + "/ws/publicAPI/tests";
        try {
        	LOGGER.info("Get! url : "+url);
            GetRequest request = GetRequest.newInstance().setUrl(url);
            ServiceCallResult result = HttpUtil.issueGetRequest(request);
            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
                return JSON.parseArray(result.getResponseString(), CKLTestVO.class);
            } else {
                LOGGER.error("getChecklistPublicTestList from checklist-service error: " +
                        result.getStatusCode() +", " +
                        result.getResponseString());
            }
        } catch (IOException e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
	}

    @Override
    public List<CKLDefectVO> getChecklistPublicDefectList(){
        String url = config.getChecklistServiceUrl() + "/ws/publicAPI/defects";
        try {
			LOGGER.info("Get! url : "+url);
            GetRequest request = GetRequest.newInstance().setUrl(url);
            ServiceCallResult result = HttpUtil.issueGetRequest(request);
            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
                return JSON.parseArray(result.getResponseString(), CKLDefectVO.class);
            } else {
                LOGGER.error("getChecklistPublicDefectList from checklist-service error: " +
                        result.getStatusCode() +", " +
                        result.getResponseString());
            }
        } catch (IOException e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

	@Override
	public List<SysProductTypeBean> getProductTypeList(){
		try{
			String url = config.getParamServiceUrl() +"/p/list-product-type";
			GetRequest request = GetRequest.newInstance().setUrl(url);
			ServiceCallResult result = HttpUtil.issueGetRequest(request);
			List<SysProductTypeBean> productTypeList = JSON.parseArray(result.getResponseString(),SysProductTypeBean.class);
			return productTypeList;
		}catch(IOException e){
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

}
