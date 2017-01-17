/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.dao;

import java.io.IOException;
import java.util.List;

import com.ai.aims.services.model.OrderAttachment;
import com.ai.aims.services.model.OrderMaster;
import com.ai.aims.services.model.OrderTestAssignment;
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
	
	public List<OrderSearchBean> searchLTOrders(String compId, String orderStatus, Integer pageNumber, Integer pageSize, String direction) throws IOException;
	
	public OrderMaster findOrder(String orderId) throws IOException;
	
	public ApiCallResult saveOrder(String userId, OrderMaster order) throws IOException;
	
	public ApiCallResult editOrder(String userId, OrderMaster order) throws IOException;

	public ApiCallResult editOrderStatus(String userId, OrderMaster order) throws IOException;

	public ApiCallResult editOrderTestAssignmentStatus(String userId, OrderTestAssignment testAssignment) throws IOException;

	public OrderAttachment getOrderAttachment(String attachmentId) throws IOException;
}