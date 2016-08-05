package com.ai.api.dao;

import com.ai.api.bean.SupplierDetailBean;
import com.ai.api.bean.legacy.FactorySearchBean;
import com.ai.api.exception.AIException;
import com.ai.commons.beans.ServiceCallResult;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2016/6/29 0029.
 */
public interface FactoryDao {

    List<FactorySearchBean> getSuppliersByUserId(String userId) throws IOException, AIException;

    SupplierDetailBean getUserSupplierDetailInfoById(String userId, String supplierId) throws IOException, AIException;

    boolean updateSupplierDetailInfo(SupplierDetailBean supplierDetailBean) throws IOException, AIException;
    boolean deleteSuppliers(String supplierIds) throws IOException, AIException;
}
