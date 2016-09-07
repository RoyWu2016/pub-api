/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.service;

import java.io.IOException;
import java.util.List;

import com.ai.api.exception.AIException;
import com.ai.commons.beans.legacy.order.OrderCancelBean;
import com.ai.commons.beans.order.SimpleOrderSearchBean;
import com.ai.commons.beans.psi.InspectionBookingBean;

/***************************************************************************
 *<PRE>
 *  Project Name    : api
 *
 *  Package Name    : com.ai.api.service
 *
 *  File Name       : OrderService.java
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

public interface OrderService {

	/*
	List<SimpleOrderBean> getOrdersByUserId(OrderSearchCriteriaBean criteria);
	List<SimpleOrderBean> getDraftsByUserId(OrderSearchCriteriaBean criteria);
	*/

	Boolean cancelOrder(OrderCancelBean orderCancelBean);

	InspectionBookingBean getOrderDetail(String userId, String orderId);

	InspectionBookingBean createOrderByDraft(String userId, String draftId);

    InspectionBookingBean editOrder(String userId, String orderId);

    InspectionBookingBean saveOrderByDraft(String userId, String draftId);
    
    List<SimpleOrderSearchBean> searchOrders(String userId, String serviceType,String startDate, String endDate, String keyWord, String orderStatus, String pageSize, String pageNumber)  throws IOException, AIException;
}
