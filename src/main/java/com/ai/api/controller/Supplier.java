package com.ai.api.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.ai.api.bean.SupplierDetailBean;
import com.ai.api.exception.AIException;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.psi.OrderFactoryBean;
import com.ai.commons.beans.supplier.SupplierSearchResultBean;
import org.springframework.http.ResponseEntity;

/**
 * Created by Administrator on 2016/6/29 0029.
 */
public interface Supplier {
	ResponseEntity<List<SupplierSearchResultBean>> getUserSupplierById(String userId) throws IOException, AIException;

	ResponseEntity<SupplierDetailBean> getUserSupplierDetailInfoById(String userId, String supplierId)
			throws IOException, AIException;

	ResponseEntity<Map<String, String>> updateUserSupplierDetailInfo(String userId, String supplierId,
			SupplierDetailBean supplierDetailBean) throws IOException, AIException;

	ResponseEntity<Boolean> deleteSuppliers(String userId, String supplierIds) throws IOException, AIException;

	ResponseEntity<SupplierDetailBean> createSupplier(String userId, SupplierDetailBean supplierDetailBean)
			throws IOException, AIException;

	ResponseEntity<ApiCallResult> getSupplierConfirm(String orderId, String password);

	ResponseEntity<Boolean> updateSupplierConfirm(String orderId,String password,
                                                     String inspectionDateString,
                                                     String containReadyTime,
                                                     OrderFactoryBean orderFactoryBean);
}
