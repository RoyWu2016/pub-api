package com.ai.api.service;

import com.ai.api.exception.AIException;

import java.io.IOException;

/**
 * Created by Administrator on 2016/6/21 0021.
 */
public interface ParameterService {

    String getProductCategoryList() throws IOException, AIException;

    String getProductFamilyList() throws IOException, AIException;
}
