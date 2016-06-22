package com.ai.api.controller.impl;

import com.ai.api.controller.Parameter;
import com.ai.api.exception.AIException;
import com.ai.api.service.ParameterService;
import com.ai.commons.annotation.TokenSecured;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Created by Administrator on 2016/6/21 0021.
 */

@RestController
public class ParameterImpl implements Parameter {

    @Autowired
    ParameterService parameterService;

    @Override
    @TokenSecured
    @RequestMapping(value = "/parameter/product-category-list", method = RequestMethod.GET)
    public ResponseEntity<String> getProductCategoryList() throws IOException, AIException {

        String result = null;

        try{
            result = parameterService.getProductCategoryList();
        } catch (Exception e){
            e.printStackTrace();
        }

        if(result==null){
            System.out.println("Product category list not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    @TokenSecured
    @RequestMapping(value = "/parameter/product-family-list", method = RequestMethod.GET)
    public ResponseEntity<String> getProductFamilyList() throws IOException, AIException {

        String result = null;

        try{
            result = parameterService.getProductFamilyList();
        } catch (Exception e){
            e.printStackTrace();
        }

        if(result==null){
            System.out.println("Product family list not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
