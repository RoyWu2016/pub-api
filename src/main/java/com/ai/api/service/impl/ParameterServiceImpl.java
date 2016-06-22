package com.ai.api.service.impl;

import com.ai.api.bean.SysProductCategoryBean;
import com.ai.api.bean.SysProductFamilyBean;
import com.ai.api.dao.ParameterDao;
import com.ai.api.exception.AIException;
import com.ai.api.service.ParameterService;
import com.ai.api.service.ServiceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2016/6/21 0021.
 */

@Service("parameterService")
public class ParameterServiceImpl implements ParameterService {

    @Autowired
    @Qualifier("serviceConfig")
    private ServiceConfig config;

    @Autowired
    @Qualifier("paramDao")
    private ParameterDao paramDao;

    @Override
    public SysProductCategoryBean getProductCategoryBeanList() throws IOException, AIException {
        return paramDao.getSysProductCategory();
    }

    @Override
    public SysProductFamilyBean getProductFamilyBeanList() throws IOException, AIException {
        return paramDao.getSysProductFamily();
    }

}
