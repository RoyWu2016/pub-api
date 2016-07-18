package com.ai.api.controller.impl;

import java.util.List;

import com.ai.api.bean.ProductCategoryDtoBean;
import com.ai.api.bean.ProductFamilyDtoBean;
import com.ai.api.controller.Parameter;
import com.ai.api.service.ParameterService;
import com.ai.commons.annotation.TokenSecured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    @RequestMapping(value = "/parameter/productCategories", method = RequestMethod.GET)
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
    @RequestMapping(value = "/parameter/productFamilies", method = RequestMethod.GET)
    public ResponseEntity<List<ProductFamilyDtoBean>> getProductFamilyList() {

        List<ProductFamilyDtoBean> result = parameterService.getProductFamilyList();

        if(result==null){
	        logger.error("Product family list not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
