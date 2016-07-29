package com.ai.api.controller;

import java.io.IOException;
import java.util.List;

import com.ai.api.bean.SupplierDetailBean;
import com.ai.api.bean.legacy.FactorySearchBean;
import com.ai.api.exception.AIException;
import com.ai.commons.beans.supplier.SupplierSearchResultBean;
import org.springframework.http.ResponseEntity;

/**
 * Created by Administrator on 2016/6/29 0029.
 */
public interface Supplier {
    ResponseEntity<List<SupplierSearchResultBean>> getUserSupplierById(String userId) throws IOException, AIException;
    ResponseEntity<SupplierDetailBean> getUserSupplierDetailInfoById(String userId, String supplierId) throws IOException, AIException;
    ResponseEntity<Boolean> updateUserSupplierDetailInfo(String userId, String supplierId, SupplierDetailBean supplierDetailBean)
            throws IOException, AIException;
    ResponseEntity<Boolean> deleteSupplier(String userId,String supplierId)
            throws IOException, AIException;
}
