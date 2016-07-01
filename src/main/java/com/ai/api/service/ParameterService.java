package com.ai.api.service;

import com.ai.api.bean.ProductCategoryDtoBean;
import com.ai.api.bean.ProductFamilyDtoBean;
import com.ai.api.exception.AIException;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2016/6/21 0021.
 */
public interface ParameterService {

    List<ProductCategoryDtoBean> getProductCategoryList() throws IOException, AIException;

    List<ProductFamilyDtoBean> getProductFamilyList() throws IOException, AIException;
}
