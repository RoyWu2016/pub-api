/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.dao.impl;

import java.io.IOException;
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
import com.ai.commons.beans.params.ClassifiedBean;
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
				RedisUtil.set("productCategoryListCache", JSON.toJSONString(productCategoryList),RedisUtil.HOUR * 24);
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
					RedisUtil.set("productFamilyListCache", JSON.toJSONString(productFamilyList),RedisUtil.HOUR * 24);
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
					RedisUtil.set("countryListCache", JSON.toJSONString(countryList),RedisUtil.HOUR * 24);
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
					resultMap.put("CHECKLISTS.TEST_SAMPLE_LEVEL_BY_PIECES_NO", priceNoList);
					
					LOGGER.info("saving priceNoList CHECKLIST_TEST_SAMPLE_LEVEL_BY_PIECES_NO");
					RedisUtil.hset("testSampleSizeListCache","CHECKLIST_TEST_SAMPLE_LEVEL_BY_PIECES_NO",JSON.toJSONString(priceNoList),RedisUtil.HOUR * 24);
				} else {
					LOGGER.error("getTestSampleSizeList CHECKLIST_TEST_SAMPLE_LEVEL_BY_PIECES_NO error: " + result.getStatusCode() + ", "
							+ result.getResponseString());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LOGGER.error(ExceptionUtils.getStackTrace(e));
			}
		}else {
			resultMap.put("CHECKLISTS.TEST_SAMPLE_LEVEL_BY_PIECES_NO", priceNoList);
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
					resultMap.put("CHECKLISTS.TEST_SAMPLE_LEVEL_BY_LEVEL", sampleLevelList);
					
					LOGGER.info("saving priceNoList CHECKLIST_TEST_SAMPLE_LEVEL_BY_LEVEL");
					RedisUtil.hset("testSampleSizeListCache","CHECKLIST_TEST_SAMPLE_LEVEL_BY_LEVEL",JSON.toJSONString(sampleLevelList),RedisUtil.HOUR * 24);
				} else {
					LOGGER.error("getTestSampleSizeList CHECKLIST_TEST_SAMPLE_LEVEL_BY_LEVEL error: " + result.getStatusCode() + ", "
							+ result.getResponseString());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LOGGER.error(ExceptionUtils.getStackTrace(e));
			}
		}else {
			resultMap.put("CHECKLISTS.TEST_SAMPLE_LEVEL_BY_LEVEL", sampleLevelList);
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
					resultMap.put("CHECKLISTS.TEST_FABRIC_SAMPLE_LEVEL", fabricLevelList);
					
					LOGGER.info("saving priceNoList CHECKLIST_TEST_FABRIC_SAMPLE_LEVEL");
					RedisUtil.hset("testSampleSizeListCache","CHECKLIST_TEST_FABRIC_SAMPLE_LEVEL",JSON.toJSONString(fabricLevelList),RedisUtil.HOUR * 24);
				} else {
					LOGGER.error("getTestSampleSizeList CHECKLIST_TEST_FABRIC_SAMPLE_LEVEL error: " + result.getStatusCode() + ", "
							+ result.getResponseString());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LOGGER.error(ExceptionUtils.getStackTrace(e));
			}
		}else {
			resultMap.put("CHECKLISTS.TEST_FABRIC_SAMPLE_LEVEL", fabricLevelList);
			LOGGER.info("success getTestSampleSizeList CHECKLIST_TEST_FABRIC_SAMPLE_LEVEL from redis");
		}
		
		return resultMap;
	}

	@Override
	// @Cacheable(value="checklistPublicTestListCache", key="#root.methodName")
	public List<CKLTestVO> getChecklistPublicTestList() {
		
		List<CKLTestVO> checklistPublicTestList = null;
		LOGGER.info("try to getChecklistPublicDefectList from redis ...");
		String jsonString = RedisUtil.get("checklistPublicTestListCache");
		checklistPublicTestList = JSON.parseArray(jsonString, CKLTestVO.class);
		if (null == checklistPublicTestList) {
			String url = config.getChecklistServiceUrl() + "/ws/publicAPI/tests";
			LOGGER.info("Get! url : " + url);
			GetRequest request = GetRequest.newInstance().setUrl(url);
			try {
				ServiceCallResult result = HttpUtil.issueGetRequest(request);
				if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
					checklistPublicTestList = JSON.parseArray(result.getResponseString(),CKLTestVO.class);
					
					LOGGER.info("saving getChecklistPublicTestList");
					RedisUtil.set("checklistPublicTestListCache", JSON.toJSONString(checklistPublicTestList),RedisUtil.HOUR * 24);
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
	}

	@Override
	// @Cacheable(value="checklistPublicDefectListCache",key="#root.methodName")
	public List<CKLDefectVO> getChecklistPublicDefectList() {
		
		List<CKLDefectVO> checklistPublicDefectList = null ;
		LOGGER.info("try to getChecklistPublicDefectList from redis ...");
		String jsonString = RedisUtil.get("checklistPublicDefectListCache");
		checklistPublicDefectList = JSON.parseArray(jsonString, CKLDefectVO.class);
		if (null == checklistPublicDefectList) {
			String url = config.getChecklistServiceUrl() + "/ws/publicAPI/defects";
			LOGGER.info("Get! url : " + url);
			GetRequest request = GetRequest.newInstance().setUrl(url);
			try {
				ServiceCallResult result = HttpUtil.issueGetRequest(request);
				if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
					checklistPublicDefectList = JSON.parseArray(result.getResponseString(),CKLDefectVO.class);
					
					LOGGER.info("saving checklistPublicDefectList");
					RedisUtil.set("checklistPublicDefectListCache", JSON.toJSONString(checklistPublicDefectList),RedisUtil.HOUR * 24);
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
	}

	@Override
	// @Cacheable(value="productTypeListCache", key="#root.methodName")
	public List<SysProductTypeBean> getProductTypeList() {
		List<SysProductTypeBean> proTypeList = null ;
		LOGGER.info("try to getProductTypeList from redis ...");
		String jsonString = RedisUtil.get("productTypeListCache");
		proTypeList = JSON.parseArray(jsonString, SysProductTypeBean.class);
		if (null ==  proTypeList) {
			String url = config.getParamServiceUrl() + "/p/list-product-type";
			GetRequest request = GetRequest.newInstance().setUrl(url);
			try {
				ServiceCallResult result = HttpUtil.issueGetRequest(request);
				if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
					proTypeList = JSON.parseArray(result.getResponseString(),SysProductTypeBean.class);
					
					LOGGER.info("saving productTypeList");
					RedisUtil.set("productTypeListCache", JSON.toJSONString(proTypeList),RedisUtil.HOUR * 24);
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
	}

	@Override
	public List<ClassifiedBean> getTextileProductCategories() {
		// TODO Auto-generated method stub
		List<ClassifiedBean> proTypeList = null ;
		LOGGER.info("try to getTextileProductCategory from redis ...");
		String jsonStringTextileProductCategory = RedisUtil.hget("textileProductCategoryCache","TEXTILE_PRODUCT_CATEGORIES");
		proTypeList = JSON.parseArray(jsonStringTextileProductCategory, ClassifiedBean.class);
		if(null == proTypeList) {
			String baseUrl = config.getParamServiceUrl() + "/systemconfig/classified/list/TEXTILE_PRODUCT_CATEGORIES";
			GetRequest request = GetRequest.newInstance().setUrl(baseUrl);
			try {
				LOGGER.info("send for request: " + baseUrl);
				ServiceCallResult result = HttpUtil.issueGetRequest(request);
				if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
					JSONObject object = JSONObject.parseObject(result.getResponseString());
					Object arrayStr = object.get("content");
					proTypeList = JSON.parseArray(arrayStr + "", ClassifiedBean.class);
					
					LOGGER.info("saving getTextileProductCategory");
					RedisUtil.set("textileProductCategoryCache", JSON.toJSONString(proTypeList),RedisUtil.HOUR * 24);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			LOGGER.info("success getProductTypeList from redis");
		}
		return proTypeList;
	}

	@Override
	public List<ClassifiedBean> getAiOffices() {
		// TODO Auto-generated method stub
		List<ClassifiedBean> proTypeList = null ;
		LOGGER.info("try to getAiOffices from redis ...");
		String jsonStringTextileProductCategory = RedisUtil.hget("aiOfficesCache","AI_OFFICE");
		proTypeList = JSON.parseArray(jsonStringTextileProductCategory, ClassifiedBean.class);
		if(null == proTypeList) {
			String baseUrl = config.getParamServiceUrl() + "/systemconfig/classified/list/AI_OFFICE";
			GetRequest request = GetRequest.newInstance().setUrl(baseUrl);
			try {
				LOGGER.info("send for request: " + baseUrl);
				ServiceCallResult result = HttpUtil.issueGetRequest(request);
				if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
					JSONObject object = JSONObject.parseObject(result.getResponseString());
					Object arrayStr = object.get("content");
					proTypeList = JSON.parseArray(arrayStr + "", ClassifiedBean.class);
					
					LOGGER.info("saving getAiOffices");
					RedisUtil.set("aiOfficesCache", JSON.toJSONString(proTypeList),RedisUtil.HOUR * 24);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			LOGGER.info("success getAiOffices from redis");
		}
		return proTypeList;
	}

}
