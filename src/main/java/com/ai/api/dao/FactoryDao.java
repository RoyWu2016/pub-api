package com.ai.api.dao;

import java.io.IOException;
import java.util.List;

import com.ai.api.bean.SupplierDetailBean;
import com.ai.api.bean.legacy.FactorySearchBean;
import com.ai.api.exception.AIException;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.psi.OrderFactoryBean;

/**
 * Created by Administrator on 2016/6/29 0029.
 */
public interface FactoryDao {

	List<FactorySearchBean> getSuppliersByUserId(String userId) throws IOException, AIException;

	SupplierDetailBean getUserSupplierDetailInfoById(String userId, String supplierId) throws IOException, AIException;

	boolean updateSupplierDetailInfo(SupplierDetailBean supplierDetailBean) throws IOException, AIException;

	boolean deleteSuppliers(String supplierIds) throws IOException, AIException;

	String createSupplier(SupplierDetailBean supplierDetailBean) throws IOException, AIException;

	ApiCallResult supplierConfirmOrder(String orderId, String inspectionDateString, String containReadyTime,
			OrderFactoryBean orderFactoryBean);

	OrderFactoryBean getOrderFactory(String supplierId);
}
