package com.ai.api.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;

import com.ai.aims.services.model.OrderMaster;
import com.ai.commons.beans.ApiCallResult;

@SuppressWarnings("rawtypes")
public interface LTOrder {

	public ResponseEntity<ApiCallResult> addOrder(HttpServletRequest request, String userId);
	
	public ResponseEntity<ApiCallResult> editOrder(HttpServletRequest request, String userId, String orderId, OrderMaster order);
	
	public ResponseEntity<ApiCallResult> searchLTOrders(String userId, String orderStatus, Integer pageNumber, Integer pageSize);
	
	public ResponseEntity<ApiCallResult> findOrder(String orderId, String userId);

	public ResponseEntity<ApiCallResult> searchPrograms(String userId);

}
