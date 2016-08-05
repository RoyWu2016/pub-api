package com.ai.api.service.impl;

import java.io.IOException;
import java.util.List;

import com.ai.api.bean.SupplierDetailBean;
import com.ai.api.bean.legacy.FactorySearchBean;
import com.ai.api.dao.FactoryDao;
import com.ai.api.exception.AIException;
import com.ai.api.service.FactoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2016/6/29 0029.
 */

@Service("factoryService")
public class FactoryServiceImpl implements FactoryService {

    protected Logger logger = LoggerFactory.getLogger(FactoryServiceImpl.class);

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

    @Override
    public boolean updateSupplierDetailInfo(SupplierDetailBean supplierDetailBean) throws IOException, AIException{
        return factoryDao.updateSupplierDetailInfo(supplierDetailBean);
    }

    @Override
    public boolean deleteSuppliers(String supplierIds) throws IOException, AIException {
        return factoryDao.deleteSuppliers(supplierIds);
    }


}
