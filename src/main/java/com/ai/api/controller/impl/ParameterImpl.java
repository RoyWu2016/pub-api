package com.ai.api.controller.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ai.api.bean.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ai.api.controller.Parameter;
import com.ai.api.service.ParameterService;
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
}
