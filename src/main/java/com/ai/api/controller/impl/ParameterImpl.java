package com.ai.api.controller.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.ai.aims.services.model.OfficeMaster;
import com.ai.aims.services.model.ProgramMaster;
import com.ai.api.bean.ChecklistSampleSize;
import com.ai.api.bean.ChecklistSampleSizeChildren;
import com.ai.api.bean.ChinaTimeBean;
import com.ai.api.bean.CountryBean;
import com.ai.api.bean.DropdownListOptionBean;
import com.ai.api.bean.ProductCategoryDtoBean;
import com.ai.api.bean.ProductFamilyDtoBean;
import com.ai.api.config.ServiceConfig;
import com.ai.api.controller.Parameter;
import com.ai.api.service.ParameterService;
import com.ai.api.util.AIUtil;
import com.ai.api.util.RedisUtil;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.checklist.vo.CKLDefectVO;
import com.ai.commons.beans.checklist.vo.CKLTestVO;
import com.ai.commons.beans.params.ChecklistTestSampleSizeBean;
import com.ai.commons.beans.params.ClassifiedBean;
import com.ai.commons.beans.params.GeoCountryCallingCodeBean;
import com.ai.commons.beans.params.TextileCategoryBean;
import com.ai.commons.beans.params.product.SysProductTypeBean;
import com.alibaba.fastjson.JSON;

import io.swagger.annotations.ApiOperation;

/**
 * Created by Administrator on 2016/6/21 0021.
 */

@RestController
public class ParameterImpl implements Parameter {

	protected Logger logger = LoggerFactory.getLogger(ParameterImpl.class);

	@Autowired
	ParameterService parameterService;

	@Autowired
	@Qualifier("serviceConfig")
	private ServiceConfig config;

	@Override
	@TokenSecured
	@RequestMapping(value = "/parameter/product-categories", method = RequestMethod.GET)
	public ResponseEntity<List<ProductCategoryDtoBean>> getProductCategoryList(
			@RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {

		List<ProductCategoryDtoBean> result = parameterService.getProductCategoryList(refresh);

		if (result == null) {
			logger.error("Product category list not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/parameter/product-families", method = RequestMethod.GET)
	public ResponseEntity<List<ProductFamilyDtoBean>> getProductFamilyList(
			@RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {

		List<ProductFamilyDtoBean> result = parameterService.getProductFamilyList(refresh);

		if (result == null) {
			logger.error("Product family list not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/parameter/countries", method = RequestMethod.GET)
	public ResponseEntity<List<CountryBean>> getCountryList(
			@RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {

		List<GeoCountryCallingCodeBean> result = parameterService.getCountryList(refresh);
		if (result == null) {
			logger.error("country list not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		List<CountryBean> countryBeanList = new ArrayList<CountryBean>();
		for (GeoCountryCallingCodeBean each : result) {
			CountryBean bean = new CountryBean();
			bean.setCode(each.getCallingCode());
			bean.setLabel(each.getCountry());
			bean.setValue(each.getAbbreviation());

			countryBeanList.add(bean);
		}
		return new ResponseEntity<>(countryBeanList, HttpStatus.OK);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/parameter/checklist-test-sample-size-list", method = RequestMethod.GET)
	public ResponseEntity<List<ChecklistSampleSize>> getTestSampleSizeList(
			@RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {

		Map<String, List<ChecklistTestSampleSizeBean>> resultMap = parameterService.getTestSampleSizeList(refresh);

		if (resultMap == null) {
			logger.error("TestSampleSizeList not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		List<ChecklistSampleSize> result = this.changeMap2Bean(resultMap);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/parameter/public-tests", method = RequestMethod.GET)
	public ResponseEntity<List<CKLTestVO>> getChecklistPublicTestList(
			@RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {
		logger.info("get checklistPublicTests");
		List<CKLTestVO> result = parameterService.getChecklistPublicTestList(refresh);

		if (result == null) {
			logger.error("checklistTestList not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/parameter/public-defects", method = RequestMethod.GET)
	public ResponseEntity<List<CKLDefectVO>> getChecklistPublicDefectList(
			@RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {
		logger.info("get checklistPublicDefects");
		List<CKLDefectVO> result = parameterService.getChecklistPublicDefectList(refresh);

		if (result == null) {
			logger.error("checklistTestList not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/parameter/product-types", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getProductTypeList(
			@RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<SysProductTypeBean> sysProductTypeBeanList = parameterService.getProductTypeList(refresh);
		if (sysProductTypeBeanList != null) {
			map.put("success", true);
			map.put("data", sysProductTypeBeanList);
			return new ResponseEntity<>(map, HttpStatus.OK);
		} else {
			logger.error("TestSampleSizeList not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/parameter/textile-product-categories", method = RequestMethod.GET)
	public ResponseEntity<List<DropdownListOptionBean>> getTextileProductCategories(
			@RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {
		// TODO Auto-generated method stub
		logger.info("get getTextileProductCategory");
		List<TextileCategoryBean> result = parameterService.getTextileProductCategories(refresh);
		if (result == null) {
			logger.error("getTextileProductCategory not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		List<DropdownListOptionBean> list = new ArrayList<DropdownListOptionBean>();
		for (TextileCategoryBean each : result) {
			DropdownListOptionBean bean = new DropdownListOptionBean();
			bean.setLabel(each.getTextileCategory());
			bean.setValue(each.getTextileCategory());

			list.add(bean);
		}
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	private List<ChecklistSampleSize> changeMap2Bean(Map<String, List<ChecklistTestSampleSizeBean>> map) {
		List<ChecklistSampleSize> list = new ArrayList<ChecklistSampleSize>();
		for (Map.Entry<String, List<ChecklistTestSampleSizeBean>> entry : map.entrySet()) {
			ChecklistSampleSize bean = new ChecklistSampleSize();
			bean.setLabel(entry.getKey());
			List<ChecklistSampleSizeChildren> children = new ArrayList<>();
			for (ChecklistTestSampleSizeBean b : entry.getValue()) {
				ChecklistSampleSizeChildren child = new ChecklistSampleSizeChildren();
				child.setValue(b.getValue());
				child.setLabel(b.getText());
				children.add(child);
			}
			bean.setChildren(children);
			list.add(bean);
		}
		return list;
	}

	@ApiOperation(value = "Search Office API", produces = "application/json", response = OfficeMaster.class, httpMethod = "GET")
	@TokenSecured
	@RequestMapping(value = "/parameter/lt-offices", method = RequestMethod.GET)
	public ResponseEntity<List<OfficeMaster>> searchOffice(
			@RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {
		List<OfficeMaster> proTypeList = null ;
		if(!refresh) {
			logger.info("try to search lt Office from redis ...");
			String jsonStringTextileProductCategory = RedisUtil.get("ltOfficesCache");
			proTypeList = JSON.parseArray(jsonStringTextileProductCategory, OfficeMaster.class);
		}
		if(null == proTypeList) {
			logger.info("Can not find from redis search from aims service");
			RestTemplate restTemplate = new RestTemplate();
			try {
				AIUtil.addRestTemplateMessageConverter(restTemplate);
				String url = new StringBuilder(config.getAimsServiceBaseUrl()).append("/api/office/search/all").toString();
				proTypeList = Arrays.asList(restTemplate.getForObject(url, OfficeMaster[].class));
				
				logger.info("saving searchOffice");
				RedisUtil.set("ltOfficesCache", JSON.toJSONString(proTypeList),RedisUtil.HOUR * 24);
				
				return new ResponseEntity<List<OfficeMaster>>(proTypeList, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("search office error: " + ExceptionUtils.getFullStackTrace(e));
				return new ResponseEntity<List<OfficeMaster>>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}else {
			logger.info("get lt offices from redis successfully");
			return new ResponseEntity<List<OfficeMaster>>(proTypeList, HttpStatus.OK);
		}
	}

	@ApiOperation(value = "Search Program API", produces = "application/json", response = ProgramMaster.class, httpMethod = "GET")
	@TokenSecured
	@RequestMapping(value = "/parameter/lt-programs", method = RequestMethod.GET)
	public ResponseEntity<List<ProgramMaster>> searchPrograms(
			@RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {
		List<ProgramMaster> programs = null;
		if(!refresh) {
			logger.info("try to searchPrograms from redis ...");
			String jsonStringTextileProductCategory = RedisUtil.get("ltProgramsCache");
			programs = JSON.parseArray(jsonStringTextileProductCategory, ProgramMaster.class);
		}
		if(null == programs) {
			try {
				RestTemplate restTemplate = new RestTemplate();
				AIUtil.addRestTemplateMessageConverter(restTemplate);
				String url = new StringBuilder(config.getAimsServiceBaseUrl()).append("/api/program/search/all").toString();
				programs = Arrays.asList(restTemplate.getForObject(url, ProgramMaster[].class));
				
				logger.info("saving searchPrograms");
				RedisUtil.set("ltProgramsCache", JSON.toJSONString(programs),RedisUtil.HOUR * 24);
				
				return new ResponseEntity<List<ProgramMaster>>(programs, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("search Programs error: " + ExceptionUtils.getFullStackTrace(e));
				return new ResponseEntity<List<ProgramMaster>>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}else {
			logger.info("get lt programs from redis successfully");
			return new ResponseEntity<List<ProgramMaster>>(programs, HttpStatus.OK);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/parameter/ai-offices", method = RequestMethod.GET)
	public ResponseEntity<List<DropdownListOptionBean>> getAiOffices(
			@RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {
		// TODO Auto-generated method stub
		logger.info("get getAiOffices");
		List<ClassifiedBean> result = parameterService.getAiOffices(refresh);
		if (result == null) {
			logger.error("getAiOffices not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		List<DropdownListOptionBean> list = new ArrayList<DropdownListOptionBean>();
		for (ClassifiedBean each : result) {
			DropdownListOptionBean bean = new DropdownListOptionBean();
			bean.setLabel(each.getKey());
			bean.setValue(each.getValue());

			list.add(bean);
		}
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/parameter/server-time", method = RequestMethod.GET)
	public ResponseEntity<ChinaTimeBean> getChinaTime() {
		// TODO Auto-generated method stub
		ChinaTimeBean chinaTimeBean = new ChinaTimeBean();
		Calendar cale = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH);
		int unitTimeStamp = (int) (System.currentTimeMillis() / 1000);
		chinaTimeBean.setDatetime(sdf.format(cale.getTime()));
		chinaTimeBean.setTimezone("UTC+8");
		chinaTimeBean.setUnixTimeStamp(unitTimeStamp);

		return new ResponseEntity<>(chinaTimeBean, HttpStatus.OK);
	}

}
