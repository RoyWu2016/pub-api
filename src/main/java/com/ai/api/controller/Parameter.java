package com.ai.api.controller;

import com.ai.api.exception.AIException;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

/**
 * Created by Henry Yue on 2016/6/21 0021.
 */
public interface Parameter {

    ResponseEntity<String> getProductCategoryList() throws IOException, AIException;

    ResponseEntity<String> getProductFamilyList() throws IOException, AIException;
}
