package com.ai.api.service.impl;

import com.ai.api.bean.SupplierDetailBean;
import com.ai.api.bean.legacy.FactorySearchBean;
import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.FactoryDao;
import com.ai.api.exception.AIException;
import com.ai.api.service.FactoryService;
import com.ai.commons.beans.ServiceCallResult;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2016/6/29 0029.
 */

@Service("factoryService")
public class FactoryServiceImpl implements FactoryService {

    protected Logger logger = Logger.getLogger(FactoryServiceImpl.class);

    @Autowired
    @Qualifier("serviceConfig")
    private ServiceConfig config;

    @Autowired
    @Qualifier("factoryDao")
    private FactoryDao factoryDao;

    @Override
    public List<FactorySearchBean> getSuppliersByUserId(String userId) throws IOException, AIException {
        return factoryDao.getSuppliersByUserId(userId);
    }

    @Override
    public SupplierDetailBean getUserSupplierDetailInfoById(String userId, String supplierId) throws IOException, AIException{
        return factoryDao.getUserSupplierDetailInfoById(userId, supplierId);
    }
}
