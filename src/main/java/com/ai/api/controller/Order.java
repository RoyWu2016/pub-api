/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.PageBean;
import com.ai.commons.beans.order.SimpleOrderSearchBean;

/***************************************************************************
 * <PRE>
 *  Project Name    : api
 *
 *  Package Name    : com.ai.api.controller.impl
 *
 *  File Name       : Order.java
 *
 *  Creation Date   : Jul 13, 2016
 *
 *  Author          : Allen Zhang
 *
 *  Purpose         : TODO
 *
 *
 *  History         : TODO
 *
 * </PRE>
 ***************************************************************************/

@SuppressWarnings("rawtypes")
public interface Order {

	/*
	 * ResponseEntity<List<SimpleOrderBean>> getOrderListByUserId(String userId,
	 * Integer pageNumber, String orderTypeArray, String orderStatus, String
	 * starts, String ends, String keyword);
	 */
	ResponseEntity<Boolean> cancelOrder(String userId, String orderId, String reason, String reason_options);

	ResponseEntity<ApiCallResult> getOrderDetail(String userId, String orderId);

	ResponseEntity<Map<String, Object>> createOrderByDraft(String userId, String draftId);

	ResponseEntity<Map<String, Object>> editOrder(String userId, String orderId);

	ResponseEntity<Map<String, Object>> saveOrderByDraft(String userId, String draftId, String orderId);

	ResponseEntity<ApiCallResult> searchOrders(String userId, String serviceType, String startDate,
			String endDate, String keyWord, String orderStatus, String pageNumber, String pageSize);

	ResponseEntity<Map<String, String>> exportOrders(String userId, String serviceType, String startDate,
			String endDate, String orderStatus);

	ResponseEntity<ApiCallResult> reInspection(String userId, String orderId, String draftId);

	ResponseEntity<ApiCallResult> getFilesByOrderID(String userId, String orderId) throws IOException;

	ResponseEntity<ApiCallResult> getReInspectionList(String userId, String serviceType, String keyword,
			 String pageSize, String pageNumber);

//	ResponseEntity<Map<String, ApiCallResult>> getOrderAction(String userId, String orderId);

}
