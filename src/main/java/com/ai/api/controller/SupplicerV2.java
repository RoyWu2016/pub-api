package com.ai.api.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;

import com.ai.api.bean.SupplierDetailBean;
import com.ai.api.exception.AIException;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.psi.OrderFactoryBean;
import com.ai.commons.beans.psi.api.ApiOrderFactoryBean;

public interface SupplicerV2 {

	ResponseEntity<ApiCallResult> getSuppliersByUserId(String userId) throws IOException, AIException;

	ResponseEntity<ApiCallResult> getSupplierDetailInfoById(String userId, String supplierId)
			throws IOException, AIException;

	ResponseEntity<ApiCallResult> updateUserSupplierDetailInfo(String userId, String supplierId,
			SupplierDetailBean supplierDetailBean) throws IOException, AIException;

	ResponseEntity<ApiCallResult> deleteSuppliers(String userId, String supplierIds) throws IOException, AIException;

	ResponseEntity<ApiCallResult> createSupplier(String userId, SupplierDetailBean supplierDetailBean)
			throws IOException, AIException;

	ResponseEntity<ApiCallResult> updateFactoryConfirm(String orderId, String password, String inspectionDateString,
			String containReadyTime, OrderFactoryBean orderFactoryBean);

	ResponseEntity<ApiCallResult> updateAuditFactoryConfirm(String orderId, String password, String auditDate,
			String containReadyTime, ApiOrderFactoryBean orderFactoryBean);

	ResponseEntity<ApiCallResult> updateSupplierDetailInfo(String orderId, String password,
			SupplierDetailBean supplierDetailBean) throws IOException, AIException;

	ResponseEntity<ApiCallResult> getFactoryConfirm(String orderId, String password);

	ResponseEntity<ApiCallResult> getAuditFactoryConfirm(String orderId, String password);

}
