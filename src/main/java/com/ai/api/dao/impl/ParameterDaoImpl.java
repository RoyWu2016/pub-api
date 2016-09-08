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

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;

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
import com.alibaba.fastjson.JSONObject;

/***************************************************************************
 * <PRE>
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
 * </PRE>
 ***************************************************************************/

public class ParameterDaoImpl implements ParameterDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(ParameterDaoImpl.class);

	@Autowired
	@Qualifier("serviceConfig")
	private ServiceConfig config;

	@Override
	public List<ProductCategoryDtoBean> getProductCategoryList() {
		// get it from redis
		List<ProductCategoryDtoBean> productCategoryList;
		LOGGER.info("try to getProductCategoryList from redis ...");
		String jsonString = RedisUtil.get("productCategoryListCache");
		productCategoryList = JSON.parseArray(jsonString, ProductCategoryDtoBean.class);
		if (productCategoryList == null) {
			// get it from param-service
			String SysProductCategoryURL = config.getParamServiceUrl() + "/p/list-product-category";
			GetRequest request7 = GetRequest.newInstance().setUrl(SysProductCategoryURL);
			try {
				ServiceCallResult result = HttpUtil.issueGetRequest(request7);
				productCategoryList = JSON.parseArray(result.getResponseString(), ProductCategoryDtoBean.class);

				LOGGER.info("get from param-service succeed, saving productCategoryListCache");
				RedisUtil.set("productCategoryListCache", JSON.toJSONString(productCategoryList));
			} catch (IOException e) {
				LOGGER.error(ExceptionUtils.getStackTrace(e));
			}
		} else {
			LOGGER.info("success getProductCategoryList from redis");
		}
		return productCategoryList;
	}

	@Override
	// @Cacheable(value="productFamilyListCache", key="#root.methodName")
	public List<ProductFamilyDtoBean> getProductFamilyList() {
		List<ProductFamilyDtoBean> productFamilyList = null;
		LOGGER.info("try to getProductFamilyList from redis ...");
		String jsonString = RedisUtil.get("productFamilyListCache");
		productFamilyList = JSON.parseArray(jsonString, ProductFamilyDtoBean.class);
		if (null == productFamilyList) {
			String SysProductFamilyBeanURL = config.getParamServiceUrl() + "/p/list-product-family";
			GetRequest request7 = GetRequest.newInstance().setUrl(SysProductFamilyBeanURL);
			try {
				ServiceCallResult result = HttpUtil.issueGetRequest(request7);
				if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
					productFamilyList = JSON.parseArray(result.getResponseString(), ProductFamilyDtoBean.class);
					
					LOGGER.info("saving productFamilyListCache");
					RedisUtil.set("productFamilyListCache", JSON.toJSONString(productFamilyList));
				} else {
					LOGGER.error("getProductFamilyList error: " + result.getStatusCode() + ", "
							+ result.getResponseString());
				}
			} catch (IOException e) {
				LOGGER.error(ExceptionUtils.getStackTrace(e));
			}

		} else {
			LOGGER.info("success getProductFamilyList from redis");
		}

		// try {
		// String jsonString = RedisUtil.get("productFamilyListCache");
		// productFamilyList = JSON.parseArray(jsonString,
		// ProductFamilyDtoBean.class);
		// if (productFamilyList.size()>0){
		// LOGGER.info("success getProductFamilyList from redis");
		// return productFamilyList;
		// }
		// }catch (Exception e){
		// LOGGER.error("error getting productFamilyList from redis",e);
		// }
		// String SysProductFamilyBeanURL = config.getParamServiceUrl()
		// +"/p/list-product-family";
		// GetRequest request7 =
		// GetRequest.newInstance().setUrl(SysProductFamilyBeanURL);
		// try{
		// ServiceCallResult result = HttpUtil.issueGetRequest(request7);
		// productFamilyList =
		// JSON.parseArray(result.getResponseString(),ProductFamilyDtoBean.class);
		// }catch(IOException e){
		// LOGGER.error(ExceptionUtils.getStackTrace(e));
		// }
		// try {
		// LOGGER.info("saving productFamilyListCache");
		// RedisUtil.set("productFamilyListCache",JSON.toJSONString(productFamilyList));
		// }catch (Exception e){
		// LOGGER.error("error saving productFamilyListCache ",e);
		// }

		return productFamilyList;
	}

	@Override
	// @Cacheable(value="countryListCache", key="#root.methodName")
	public List<String> getCountryList() {
		List<String> countryList = null;
		LOGGER.info("try to getCountryList from redis ...");
		String jsonString = RedisUtil.get("countryListCache");
		countryList = JSON.parseArray(jsonString, String.class);
		if (null == countryList) {
			String SysProductFamilyBeanURL = config.getParamServiceUrl() + "/p/list-country";
			GetRequest request7 = GetRequest.newInstance().setUrl(SysProductFamilyBeanURL);
			try {
				ServiceCallResult result = HttpUtil.issueGetRequest(request7);
				if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
					countryList = JSON.parseArray(result.getResponseString(), String.class);
					
					LOGGER.info("saving getCountryList");
					RedisUtil.set("countryListCache", JSON.toJSONString(countryList));
				} else {
					LOGGER.error("getCountryList error: " + result.getStatusCode() + ", "
							+ result.getResponseString());
				}
			} catch (IOException e) {
				LOGGER.error(ExceptionUtils.getStackTrace(e));
			}

		} else {
			LOGGER.info("success getCountryList from redis");
		}
		return countryList;

		// List<String> countryList = new ArrayList<>();
		// String SysProductFamilyBeanURL = config.getParamServiceUrl()
		// +"/p/list-country";
		// GetRequest request =
		// GetRequest.newInstance().setUrl(SysProductFamilyBeanURL);
		// try{
		// ServiceCallResult result = HttpUtil.issueGetRequest(request);
		// countryList =
		// JSON.parseArray(result.getResponseString(),String.class);
		//
		// }catch(IOException e){
		// LOGGER.error(ExceptionUtils.getStackTrace(e));
		// }
		// return countryList;
	}

	@Override
	// @Cacheable(value="testSampleSizeListCache", key="#root.methodName")
	public Map<String, List<ChecklistTestSampleSizeBean>> getTestSampleSizeList() {
		String baseUrl = config.getParamServiceUrl() + "/systemconfig/classified/list/";
		Map<String, List<ChecklistTestSampleSizeBean>> resultMap = new HashMap<>();
		
		LOGGER.info("try to getTestSampleSizeList from redis ...");
		String jsonStringPriceNo = RedisUtil.hget("testSampleSizeListCache","CHECKLIST_TEST_SAMPLE_LEVEL_BY_PIECES_NO");
		String jsonStringSampleLevel = RedisUtil.hget("testSampleSizeListCache","CHECKLIST_TEST_SAMPLE_LEVEL_BY_LEVEL");
		String jsonStringFabricLevel = RedisUtil.hget("testSampleSizeListCache","CHECKLIST_TEST_FABRIC_SAMPLE_LEVEL");
		
		List<ChecklistTestSampleSizeBean> priceNoList = JSON.parseArray(jsonStringPriceNo, ChecklistTestSampleSizeBean.class);
		List<ChecklistTestSampleSizeBean> sampleLevelList = JSON.parseArray(jsonStringSampleLevel, ChecklistTestSampleSizeBean.class);
		List<ChecklistTestSampleSizeBean> fabricLevelList = JSON.parseArray(jsonStringFabricLevel, ChecklistTestSampleSizeBean.class);
		
		if(null == priceNoList) {
			String url = baseUrl + "CHECKLIST_TEST_SAMPLE_LEVEL_BY_PIECES_NO";
			LOGGER.info("get CHECKLIST_TEST_SAMPLE_LEVEL_BY_PIECES_NO url: " + url);
			GetRequest request = GetRequest.newInstance().setUrl(url);
			try {
				ServiceCallResult result = HttpUtil.issueGetRequest(request);
				if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
					JSONObject object = JSONObject.parseObject(result.getResponseString());
					Object arrayStr = object.get("content");
					priceNoList = JSON.parseArray(arrayStr + "", ChecklistTestSampleSizeBean.class);
					resultMap.put("CHECKLIST_TEST_SAMPLE_LEVEL_BY_PIECES_NO", priceNoList);
					
					LOGGER.info("saving priceNoList CHECKLIST_TEST_SAMPLE_LEVEL_BY_PIECES_NO");
					RedisUtil.hset("testSampleSizeListCache","CHECKLIST_TEST_SAMPLE_LEVEL_BY_PIECES_NO",JSON.toJSONString(priceNoList));
				} else {
					LOGGER.error("getTestSampleSizeList CHECKLIST_TEST_SAMPLE_LEVEL_BY_PIECES_NO error: " + result.getStatusCode() + ", "
							+ result.getResponseString());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LOGGER.error(ExceptionUtils.getStackTrace(e));
			}
		}else {
			LOGGER.info("success getTestSampleSizeList CHECKLIST_TEST_SAMPLE_LEVEL_BY_PIECES_NO from redis");
		}
		
		if(null == sampleLevelList) {
			String url = baseUrl + "CHECKLIST_TEST_SAMPLE_LEVEL_BY_LEVEL";
			LOGGER.info("get CHECKLIST_TEST_SAMPLE_LEVEL_BY_LEVEL url: " + url);
			GetRequest request = GetRequest.newInstance().setUrl(url);
			try {
				ServiceCallResult result = HttpUtil.issueGetRequest(request);
				if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
					JSONObject object = JSONObject.parseObject(result.getResponseString());
					Object arrayStr = object.get("content");
					sampleLevelList = JSON.parseArray(arrayStr + "", ChecklistTestSampleSizeBean.class);
					resultMap.put("CHECKLIST_TEST_SAMPLE_LEVEL_BY_LEVEL", sampleLevelList);
					
					LOGGER.info("saving priceNoList CHECKLIST_TEST_SAMPLE_LEVEL_BY_LEVEL");
					RedisUtil.hset("testSampleSizeListCache","CHECKLIST_TEST_SAMPLE_LEVEL_BY_LEVEL",JSON.toJSONString(sampleLevelList));
				} else {
					LOGGER.error("getTestSampleSizeList CHECKLIST_TEST_SAMPLE_LEVEL_BY_LEVEL error: " + result.getStatusCode() + ", "
							+ result.getResponseString());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LOGGER.error(ExceptionUtils.getStackTrace(e));
			}
		}else {
			LOGGER.info("success getTestSampleSizeList CHECKLIST_TEST_SAMPLE_LEVEL_BY_LEVEL from redis");
		}
		
		if(null == fabricLevelList) {
			String url = baseUrl + "CHECKLIST_TEST_FABRIC_SAMPLE_LEVEL";
			LOGGER.info("get CHECKLIST_TEST_FABRIC_SAMPLE_LEVEL url: " + url);
			GetRequest request = GetRequest.newInstance().setUrl(url);
			try {
				ServiceCallResult result = HttpUtil.issueGetRequest(request);
				if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
					JSONObject object = JSONObject.parseObject(result.getResponseString());
					Object arrayStr = object.get("content");
					fabricLevelList = JSON.parseArray(arrayStr + "", ChecklistTestSampleSizeBean.class);
					resultMap.put("CHECKLIST_TEST_FABRIC_SAMPLE_LEVEL", fabricLevelList);
					
					LOGGER.info("saving priceNoList CHECKLIST_TEST_FABRIC_SAMPLE_LEVEL");
					RedisUtil.hset("testSampleSizeListCache","CHECKLIST_TEST_FABRIC_SAMPLE_LEVEL",JSON.toJSONString(fabricLevelList));
				} else {
					LOGGER.error("getTestSampleSizeList CHECKLIST_TEST_FABRIC_SAMPLE_LEVEL error: " + result.getStatusCode() + ", "
							+ result.getResponseString());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LOGGER.error(ExceptionUtils.getStackTrace(e));
			}
		}else {
			LOGGER.info("success getTestSampleSizeList CHECKLIST_TEST_FABRIC_SAMPLE_LEVEL from redis");
		}
		
		return resultMap;
		
		
		
//		String baseUrl = config.getParamServiceUrl() + "/systemconfig/classified/list/";
//		Map<String, List<ChecklistTestSampleSizeBean>> resultMap = new HashMap<>();
//		
//		try {
//			String url = baseUrl + "CHECKLIST_TEST_SAMPLE_LEVEL_BY_PIECES_NO";
//			GetRequest request = GetRequest.newInstance().setUrl(url);
//			ServiceCallResult result = HttpUtil.issueGetRequest(request);
//			JSONObject object = JSONObject.parseObject(result.getResponseString());
//			Object arrayStr = object.get("content");
//			List<ChecklistTestSampleSizeBean> list = JSON.parseArray(arrayStr + "", ChecklistTestSampleSizeBean.class);
//			resultMap.put("CHECKLIST_TEST_SAMPLE_LEVEL_BY_PIECES_NO", list);
//
//			url = baseUrl + "CHECKLIST_TEST_SAMPLE_LEVEL_BY_LEVEL";
//			request = GetRequest.newInstance().setUrl(url);
//			result = HttpUtil.issueGetRequest(request);
//			object = JSONObject.parseObject(result.getResponseString());
//			arrayStr = object.get("content");
//			list = JSON.parseArray(arrayStr + "", ChecklistTestSampleSizeBean.class);
//			resultMap.put("CHECKLIST_TEST_SAMPLE_LEVEL_BY_LEVEL", list);
//
//			url = baseUrl + "CHECKLIST_TEST_FABRIC_SAMPLE_LEVEL";
//			request = GetRequest.newInstance().setUrl(url);
//			result = HttpUtil.issueGetRequest(request);
//			object = JSONObject.parseObject(result.getResponseString());
//			arrayStr = object.get("content");
//			list = JSON.parseArray(arrayStr + "", ChecklistTestSampleSizeBean.class);
//			resultMap.put("CHECKLIST_TEST_FABRIC_SAMPLE_LEVEL", list);
//
//		} catch (IOException e) {
//			LOGGER.error(ExceptionUtils.getStackTrace(e));
//		}
//		return resultMap;
	}

	@Override
	// @Cacheable(value="checklistPublicTestListCache", key="#root.methodName")
	public List<CKLTestVO> getChecklistPublicTestList() {
		
		List<CKLTestVO> checklistPublicTestList = null ;
		LOGGER.info("try to getChecklistPublicDefectList from redis ...");
		String jsonString = RedisUtil.get("checklistPublicTestListCache");
		checklistPublicTestList = JSON.parseArray(jsonString, CKLTestVO.class);
		if (checklistPublicTestList.size()<=0) {
			String url = config.getChecklistServiceUrl() + "/ws/publicAPI/tests";
			LOGGER.info("Get! url : " + url);
			GetRequest request = GetRequest.newInstance().setUrl(url);
			try {
				ServiceCallResult result = HttpUtil.issueGetRequest(request);
				if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
					checklistPublicTestList = JSON.parseArray(result.getResponseString(),CKLTestVO.class);
					
					LOGGER.info("saving getChecklistPublicTestList");
					RedisUtil.set("checklistPublicTestListCache", JSON.toJSONString(checklistPublicTestList));
				} else {
					LOGGER.error("getChecklistPublicTestList from checklist-service error: " + result.getStatusCode() + ", "
							+ result.getResponseString());
				}
			} catch (IOException e) {
				LOGGER.error(ExceptionUtils.getStackTrace(e));
			}

		} else {
			LOGGER.info("success getChecklistPublicTestList from redis");
		}
		return checklistPublicTestList;
		
//		String url = config.getChecklistServiceUrl() + "/ws/publicAPI/tests";
//		try {
//			LOGGER.info("Get! url : " + url);
//			GetRequest request = GetRequest.newInstance().setUrl(url);
//			ServiceCallResult result = HttpUtil.issueGetRequest(request);
//			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
//				return JSON.parseArray(result.getResponseString(), CKLTestVO.class);
//			} else {
//				LOGGER.error("getChecklistPublicTestList from checklist-service error: " + result.getStatusCode() + ", "
//						+ result.getResponseString());
//			}
//		} catch (IOException e) {
//			LOGGER.error(ExceptionUtils.getStackTrace(e));
//		}
//		return null;
	}

	@Override
	// @Cacheable(value="checklistPublicDefectListCache",key="#root.methodName")
	public List<CKLDefectVO> getChecklistPublicDefectList() {
		
		List<CKLDefectVO> checklistPublicDefectList = null ;
		LOGGER.info("try to getChecklistPublicDefectList from redis ...");
		String jsonString = RedisUtil.get("checklistPublicDefectListCache");
		checklistPublicDefectList = JSON.parseArray(jsonString, CKLDefectVO.class);
		if (checklistPublicDefectList.size()<=0) {
			String url = config.getChecklistServiceUrl() + "/ws/publicAPI/defects";
			LOGGER.info("Get! url : " + url);
			GetRequest request = GetRequest.newInstance().setUrl(url);
			try {
				ServiceCallResult result = HttpUtil.issueGetRequest(request);
				if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
					checklistPublicDefectList = JSON.parseArray(result.getResponseString(),CKLDefectVO.class);
					
					LOGGER.info("saving checklistPublicDefectList");
					RedisUtil.set("checklistPublicDefectListCache", JSON.toJSONString(checklistPublicDefectList));
				} else {
					LOGGER.error("getChecklistPublicDefectList from checklist-service error: " + result.getStatusCode()
							+ ", " + result.getResponseString());
				}
			} catch (IOException e) {
				LOGGER.error(ExceptionUtils.getStackTrace(e));
			}

		} else {
			LOGGER.info("success getChecklistPublicDefectList from redis");
		}
		return checklistPublicDefectList;
		
//		String url = config.getChecklistServiceUrl() + "/ws/publicAPI/defects";
//		try {
//			LOGGER.info("Get! url : " + url);
//			GetRequest request = GetRequest.newInstance().setUrl(url);
//			ServiceCallResult result = HttpUtil.issueGetRequest(request);
//			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
//				return JSON.parseArray(result.getResponseString(), CKLDefectVO.class);
//			} else {
//				LOGGER.error("getChecklistPublicDefectList from checklist-service error: " + result.getStatusCode()
//						+ ", " + result.getResponseString());
//			}
//		} catch (IOException e) {
//			LOGGER.error(ExceptionUtils.getStackTrace(e));
//		}
//		return null;
	}

	@Override
	// @Cacheable(value="productTypeListCache", key="#root.methodName")
	public List<SysProductTypeBean> getProductTypeList() {
		List<SysProductTypeBean> proTypeList = null ;
		LOGGER.info("try to getProductTypeList from redis ...");
		String jsonString = RedisUtil.get("productTypeListCache");
		proTypeList = JSON.parseArray(jsonString, SysProductTypeBean.class);
		if (proTypeList.size() <=0 ) {
			String url = config.getParamServiceUrl() + "/p/list-product-type";
			GetRequest request = GetRequest.newInstance().setUrl(url);
			try {
				ServiceCallResult result = HttpUtil.issueGetRequest(request);
				if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
					proTypeList = JSON.parseArray(result.getResponseString(),SysProductTypeBean.class);
					
					LOGGER.info("saving productTypeList");
					RedisUtil.set("productTypeListCache", JSON.toJSONString(proTypeList));
				} else {
					LOGGER.error("getProductTypeList error: " + result.getStatusCode()
							+ ", " + result.getResponseString());
				}
			} catch (IOException e) {
				LOGGER.error(ExceptionUtils.getStackTrace(e));
			}

		} else {
			LOGGER.info("success getProductTypeList from redis");
		}
		return proTypeList;

		// try {
		// String url = config.getParamServiceUrl() + "/p/list-product-type";
		// GetRequest request = GetRequest.newInstance().setUrl(url);
		// ServiceCallResult result = HttpUtil.issueGetRequest(request);
		// List<SysProductTypeBean> productTypeList =
		// JSON.parseArray(result.getResponseString(),SysProductTypeBean.class);
		// return productTypeList;
		// } catch (IOException e) {
		// LOGGER.error(ExceptionUtils.getStackTrace(e));
		// }
		// return null;
	}

}
