/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.service;

import java.io.IOException;
import java.util.List;

import com.ai.aims.services.model.OrderMaster;
import com.ai.api.bean.OrderSearchBean;
import com.ai.api.exception.AIException;
import com.ai.commons.beans.ApiCallResult;

/***************************************************************************
 * <PRE>
 *  Project Name    : api
 *
 *  Package Name    : com.ai.api.lab.service
 *
 *  File Name       : LTOrderService.java
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
public interface LTOrderService {

	public List<OrderSearchBean> searchLTOrders(String userId, String orderStatus, Integer pageNumber, Integer pageSize) throws IOException, AIException;
	
	public OrderMaster findOrder(String orderId) throws IOException;
	
	public ApiCallResult saveOrder(String userId, OrderMaster order) throws IOException;
	
	public ApiCallResult editOrder(String userId, OrderMaster order) throws IOException;
}
