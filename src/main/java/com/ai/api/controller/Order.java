/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.controller;

import java.util.List;

import com.ai.commons.beans.legacy.order.OrderCancelBean;
import com.ai.commons.beans.order.OrderSearchResultBean;
import org.springframework.http.ResponseEntity;

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

	ResponseEntity<List<OrderSearchResultBean>> getOrderListByUserId(String userId,
	                                                                 Integer pageNumber,
	                                                                 String orderTypeArray,
	                                                                 String orderStatus,
	                                                                 String starts,
	                                                                 String ends,
	                                                                 String keyword);
	ResponseEntity<Boolean> cancelOrder(String userId, String orderId, OrderCancelBean orderCancelBean);
}
