package com.ai.api.controller;

import java.util.List;

import com.ai.api.bean.ProductCategoryDtoBean;
import com.ai.api.bean.ProductFamilyDtoBean;
import org.springframework.http.ResponseEntity;

/**
 * Created by Henry Yue on 2016/6/21 0021.
 */
public interface Parameter {

    ResponseEntity<List<ProductCategoryDtoBean>> getProductCategoryList() ;

    ResponseEntity<List<ProductFamilyDtoBean>> getProductFamilyList();

	ResponseEntity<List<String>> getCountryList();
}
