package com.ai.api.controller.impl;

import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ai.aims.services.model.OfficeMaster;
import com.ai.aims.services.model.ProgramMaster;
import com.ai.api.bean.*;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.ai.api.config.ServiceConfig;
import com.ai.api.controller.Parameter;
import com.ai.api.service.ParameterService;
import com.ai.api.util.AIUtil;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.checklist.vo.CKLDefectVO;
import com.ai.commons.beans.checklist.vo.CKLTestVO;
import com.ai.commons.beans.params.ChecklistTestSampleSizeBean;
import com.ai.commons.beans.params.ClassifiedBean;
import com.ai.commons.beans.params.product.SysProductTypeBean;

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
    public ResponseEntity<List<ProductCategoryDtoBean>> getProductCategoryList() {

        List<ProductCategoryDtoBean> result = parameterService.getProductCategoryList();

        if(result==null){
	        logger.error("Product category list not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    @TokenSecured
    @RequestMapping(value = "/parameter/product-families", method = RequestMethod.GET)
    public ResponseEntity<List<ProductFamilyDtoBean>> getProductFamilyList() {

        List<ProductFamilyDtoBean> result = parameterService.getProductFamilyList();

        if(result==null){
	        logger.error("Product family list not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

	@Override
	@TokenSecured
	@RequestMapping(value = "/parameter/countries", method = RequestMethod.GET)
	public ResponseEntity<List<String>> getCountryList() {

		List<String> result = parameterService.getCountryList();

		if(result==null){
			logger.error("country list not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/parameter/checklist-test-sample-size-list", method = RequestMethod.GET)
	public ResponseEntity<List<ChecklistSampleSize>> getTestSampleSizeList() {

		Map<String,List<ChecklistTestSampleSizeBean>> resultMap = parameterService.getTestSampleSizeList();

		if(resultMap==null){
			logger.error("TestSampleSizeList not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		List<ChecklistSampleSize> result =this.changeMap2Bean(resultMap);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/parameter/public-tests", method = RequestMethod.GET)
	public ResponseEntity<List<CKLTestVO>> getChecklistPublicTestList() {
		logger.info("get checklistPublicTests");
		List<CKLTestVO> result = parameterService.getChecklistPublicTestList();

		if(result==null){
			logger.error("checklistTestList not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/parameter/public-defects", method = RequestMethod.GET)
	public ResponseEntity<List<CKLDefectVO>> getChecklistPublicDefectList() {
		logger.info("get checklistPublicDefects");
		List<CKLDefectVO> result = parameterService.getChecklistPublicDefectList();

		if(result==null){
			logger.error("checklistTestList not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/parameter/product-types", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getProductTypeList() {
		Map<String, Object> map = new HashMap<String, Object>();
		List<SysProductTypeBean> sysProductTypeBeanList = parameterService.getProductTypeList();
		if(sysProductTypeBeanList!=null){
			map.put("success",true);
			map.put("data",sysProductTypeBeanList);
			return new ResponseEntity<>(map, HttpStatus.OK);
		}else {
			logger.error("TestSampleSizeList not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/parameter/textile-product-categories", method = RequestMethod.GET)
	public ResponseEntity<List<TextileProductCategoryBean>> getTextileProductCategories() {
		// TODO Auto-generated method stub
		logger.info("get getTextileProductCategory");
		List<ClassifiedBean> result = parameterService.getTextileProductCategories();
		if(result==null){
			logger.error("getTextileProductCategory not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		List<TextileProductCategoryBean> list = new ArrayList<TextileProductCategoryBean>();
		for(ClassifiedBean each : result) {
			TextileProductCategoryBean bean = new TextileProductCategoryBean();
			bean.setLabel(each.getKey());
			bean.setValue(each.getValue());
			
			list.add(bean);
		}
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	private List<ChecklistSampleSize> changeMap2Bean(Map<String,List<ChecklistTestSampleSizeBean>> map){
	    List<ChecklistSampleSize> list = new ArrayList<ChecklistSampleSize>();
        for (Map.Entry<String, List<ChecklistTestSampleSizeBean>> entry:map.entrySet()){
            ChecklistSampleSize bean = new ChecklistSampleSize();
            bean.setLabel(entry.getKey());
            List<ChecklistSampleSizeChildren> children = new ArrayList<>();
            for (ChecklistTestSampleSizeBean b:entry.getValue()){
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
	
	@ApiOperation(value = "Search Office API",		
	        produces = "application/json",
		    response = OfficeMaster.class,
		    httpMethod = "GET")
	@TokenSecured
	@RequestMapping(value = "/parameter/lt-offices", method = RequestMethod.GET)
	public ResponseEntity<List<OfficeMaster>> searchOffice() {
		RestTemplate restTemplate = new RestTemplate();
		List<OfficeMaster> offices = new ArrayList<OfficeMaster>();
		try {
			AIUtil.addRestTemplateMessageConverter(restTemplate);
			String url = new StringBuilder(config.getAimsServiceBaseUrl()).append("/api/office/search/all").toString();
			offices = Arrays.asList(restTemplate.getForObject(url, OfficeMaster[].class));
		} catch (Exception e) {
			logger.error("search office error: " + ExceptionUtils.getFullStackTrace(e));
			return new ResponseEntity<List<OfficeMaster>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<OfficeMaster>>(offices, HttpStatus.OK);
	}	
	
	@ApiOperation(value = "Search Program API",		
	        produces = "application/json",
		    response = ProgramMaster.class,
		    httpMethod = "GET")
	@TokenSecured
	@RequestMapping(value = "/parameter/lt-programs", method = RequestMethod.GET)
	public ResponseEntity<List<ProgramMaster>> searchPrograms() {
		RestTemplate restTemplate = new RestTemplate();
		List<ProgramMaster> programs = new ArrayList<ProgramMaster>();
		try {
			AIUtil.addRestTemplateMessageConverter(restTemplate);
			String url = new StringBuilder(config.getAimsServiceBaseUrl()).append("/api/program/search/all").toString();
			programs = Arrays.asList(restTemplate.getForObject(url, ProgramMaster[].class));
		} catch (Exception e) {
			logger.error("search office error: " + ExceptionUtils.getFullStackTrace(e));
			return new ResponseEntity<List<ProgramMaster>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<ProgramMaster>>(programs, HttpStatus.OK);
	}
}
