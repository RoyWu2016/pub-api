/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.ai.api.bean.OrderSearchBean;
import com.ai.api.exception.AIException;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.PageBean;
import com.ai.commons.beans.order.SimpleOrderSearchBean;
import com.ai.commons.beans.psi.InspectionBookingBean;
import com.ai.commons.beans.psi.ProductBean;

/***************************************************************************
 * <PRE>
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
 * </PRE>
 ***************************************************************************/

@SuppressWarnings("rawtypes")
public interface OrderService {

	/*
	 * List<SimpleOrderBean> getOrdersByUserId(OrderSearchCriteriaBean
	 * criteria); List<SimpleOrderBean>
	 * getDraftsByUserId(OrderSearchCriteriaBean criteria);
	 */

	Boolean cancelOrder(String userId, String OrderId, String reason, String reason_options);

	InspectionBookingBean getOrderDetail(String userId, String orderId);

	InspectionBookingBean createOrderByDraft(String userId, String draftId);

	InspectionBookingBean editOrder(String userId, String orderId);

	InspectionBookingBean saveOrderByDraft(String userId, String draftId);

	PageBean<SimpleOrderSearchBean> searchOrders(String userId, String serviceType, String startDate, String endDate,
			String keyWord, String orderStatus, String pageSize, String pageNumber) throws IOException, AIException;

	public List<OrderSearchBean> searchLTOrders(String userId, String orderStatus, String pageSize,
			String pageNumber) throws IOException, AIException;

	InputStream exportOrders(String userId, String serviceType, String startDate, String endDate, String keyword,
			String inspectionPeriod) throws IOException, AIException;

	ApiCallResult getOrderActionCancel(String orderId);

	ApiCallResult getOrderActionEdit(String orderId);

	ApiCallResult getOrderPrice(String userId, String orderId);

	InspectionBookingBean getInspectionOrder(String string, String orderId);

	ApiCallResult reInspection(String userId, String orderId, String draftId);

	List<ProductBean> listProducts(String orderId);
}
