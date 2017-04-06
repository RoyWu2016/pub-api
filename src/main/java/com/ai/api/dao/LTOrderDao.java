/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.dao;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.ai.aims.services.dto.order.OrderDTO;
import com.ai.aims.services.model.OrderAttachment;
import com.ai.aims.services.model.OrderMaster;
import com.ai.api.bean.OrderSearchBean;
import com.ai.commons.beans.ApiCallResult;

/***************************************************************************
 * <PRE>
 *  Project Name    : api
 *
 *  Package Name    : com.ai.api.lab.dao
 *
 *  File Name       : LTOrderDao.java
 *
 *  Creation Date   : Dec 6, 2016
 *
 *  Author          : Aashish Thakran
 *
 *  Purpose         : TODO
 *
 *
 *  History         : TODO
 *
 * </PRE>
 ***************************************************************************/
@SuppressWarnings("rawtypes")
public interface LTOrderDao {
	
	public List<OrderSearchBean> searchLTOrders(Map<String, Object> searchParams, Integer pageNumber, Integer pageSize, String direction) throws IOException;
	
	public OrderDTO findOrder(String orderId) throws IOException;

	public ApiCallResult saveOrder(String userId, OrderMaster order) throws IOException;
	
	public ApiCallResult editOrder(String userId, OrderMaster order) throws IOException;

	public ApiCallResult editOrderStatus(String userId, OrderMaster order) throws IOException;

	public ApiCallResult editOrderTestAssignmentStatus(String orderId, String testAssignmentId, String userId, String status) throws IOException;

	public OrderAttachment getOrderAttachment(String attachmentId) throws IOException;

	public ApiCallResult deleteOrders(String userId, String orderIds) throws IOException;

	public ApiCallResult findOrderTestAssignments(String orderId) throws IOException;

	public ApiCallResult updateOrderTestAssignments(String userId, String orderId, String testIds) throws IOException;

	public ApiCallResult deleteOrderTestAssignment(String userId, String testId) throws IOException;

	public ApiCallResult cloneOrder(String userId, String orderId, String cloneType) throws IOException;

	public Long countTotalOrders(Map<String, Object> searchParams, Integer pageNumber, Integer pageSize, String direction) throws IOException;
}
