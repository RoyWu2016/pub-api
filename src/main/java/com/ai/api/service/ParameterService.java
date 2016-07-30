package com.ai.api.service;

import java.util.List;

import com.ai.api.bean.ProductCategoryDtoBean;
import com.ai.api.bean.ProductFamilyDtoBean;

/**
 * Created by Administrator on 2016/6/21 0021.
 */
public interface ParameterService {

    List<ProductCategoryDtoBean> getProductCategoryList();

    List<ProductFamilyDtoBean> getProductFamilyList();

	List<String> getCountryList();
}
