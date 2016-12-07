package com.ai.api.controller;

/***************************************************************************
 * <PRE>
 *  Project Name    : api
 *
 *  Package Name    : com.ai.api.lab.controller
 *
 *  File Name       : LTOrder.java
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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;

import com.ai.aims.services.model.OrderMaster;
import com.ai.api.bean.OrderSearchBean;

public interface LTOrder {

	public ResponseEntity<OrderMaster> addOrder(HttpServletRequest request, String userId, OrderMaster order);
	
	public ResponseEntity<OrderMaster> editOrder(HttpServletRequest request, String userId, OrderMaster order);
	
	public ResponseEntity<List<OrderSearchBean>> searchLTOrders(String userId, String orderStatus, Integer pageNumber, Integer pageSize);
	
	public ResponseEntity<OrderMaster> findOrder(String orderId);
}
