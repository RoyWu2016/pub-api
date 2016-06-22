package com.ai.api.controller;

import com.ai.api.bean.ProductCategoryDtoBean;
import com.ai.api.bean.ProductFamilyDtoBean;
import com.ai.api.exception.AIException;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

/**
 * Created by Henry Yue on 2016/6/21 0021.
 */
public interface Parameter {

    ResponseEntity<List<ProductCategoryDtoBean>> getProductCategoryList() throws IOException, AIException;

    ResponseEntity<List<ProductFamilyDtoBean>> getProductFamilyList() throws IOException, AIException;
}
