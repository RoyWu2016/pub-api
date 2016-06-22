package com.ai.api.controller;

import com.ai.api.bean.SysProductCategoryBean;
import com.ai.api.bean.SysProductFamilyBean;
import com.ai.api.exception.AIException;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

/**
 * Created by Henry Yue on 2016/6/21 0021.
 */
public interface Parameter {

    ResponseEntity<SysProductCategoryBean> getProductCategoryList() throws IOException, AIException;

    ResponseEntity<SysProductFamilyBean> getProductFamilyList() throws IOException, AIException;
}
