/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import com.ai.aims.services.model.OrderMaster;
import com.ai.commons.beans.order.SimpleOrderSearchBean;

/***************************************************************************
 *<PRE>
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
 *</PRE>
 ***************************************************************************/

public interface Order {

	/*
	ResponseEntity<List<SimpleOrderBean>> getOrderListByUserId(String userId,
	                                                                 Integer pageNumber,
	                                                                 String orderTypeArray,
	                                                                 String orderStatus,
	                                                                 String starts,
	                                                                 String ends,
	                                                                 String keyword);  */
	ResponseEntity<Boolean> cancelOrder(String userId, String orderId, String reason, String reason_options);

	ResponseEntity<Map<String, Object>> getOrderDetail(String userId,String orderId);

	ResponseEntity<Map<String, Object>> createOrderByDraft(String userId,String draftId);

    ResponseEntity<Map<String, Object>> editOrder(String userId,String orderId);

    ResponseEntity<Map<String, Object>> saveOrderByDraft(String userId,String draftId,String orderId);
    
    ResponseEntity<List<SimpleOrderSearchBean>> searchOrders(String userId, String serviceType,String startDate, String endDate, String keyWord, String orderStatus, String pageNumber, String pageSize);

	public ResponseEntity<OrderMaster> addOrder(HttpServletRequest request, @RequestBody OrderMaster orderMaster);

}
