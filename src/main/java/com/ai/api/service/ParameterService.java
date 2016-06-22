package com.ai.api.service;

import com.ai.api.bean.SysProductCategoryBean;
import com.ai.api.bean.SysProductFamilyBean;
import com.ai.api.exception.AIException;

import java.io.IOException;

/**
 * Created by Administrator on 2016/6/21 0021.
 */
public interface ParameterService {

    SysProductCategoryBean getProductCategoryBeanList() throws IOException, AIException;

    SysProductFamilyBean getProductFamilyBeanList() throws IOException, AIException;
}
