/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.dao;

import java.util.List;

import com.ai.api.bean.OrderSearchBean;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.PageBean;
import com.ai.commons.beans.order.SimpleOrderSearchBean;
import com.ai.commons.beans.psi.InspectionBookingBean;
import com.ai.commons.beans.psi.ProductBean;

/***************************************************************************
 * <PRE>
 *  Project Name    : api
 *
 *  Package Name    : com.ai.api.dao
 *
 *  File Name       : OrderDao.java
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
public interface OrderDao {

	/*
	 * List<SimpleOrderBean> getOrdersByUserId(OrderSearchCriteriaBean
	 * criteria); List<SimpleOrderBean>
	 * getDraftsByUserId(OrderSearchCriteriaBean criteria);
	 */

	Boolean cancelOrder(String userId, String orderId, String reason, String reason_options);

	InspectionBookingBean getOrderDetail(String userId, String orderId);

	InspectionBookingBean createOrderByDraft(String userId, String draftId, String companyId, String parentId);

	InspectionBookingBean editOrder(String userId, String orderId, String companyId, String parentId);

	InspectionBookingBean saveOrderByDraft(String userId, String draftId, String companyId, String parentId);

	PageBean<SimpleOrderSearchBean> searchOrders(String userId, String compId, String parentId, String serviceType,
			String startDate, String endDate, String keyWord, String orderStatus, String pageSize, String pageNumber);

	public List<OrderSearchBean> searchLTOrders(String compId, String orderStatus, String pageSize, String pageNumber,
			String direction);

	ApiCallResult getOrderActionEdit(String orderId);

	ApiCallResult getOrderActionCancel(String orderId);

	ApiCallResult getOrderPrice(String userId, String compId, String parentId, String orderId);

	InspectionBookingBean getInspectionOrder(String string, String orderId);

	ApiCallResult reInspection(String userId, String companyId, String parentId, String orderId, String draftId);

	List<ProductBean> listProducts(String orderId);
}
