/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.controller.impl;

import java.util.List;

import javax.ws.rs.PathParam;

import com.ai.api.controller.Order;
import com.ai.api.service.OrderService;
import com.ai.commons.beans.legacy.order.OrderSearchCriteriaBean;
import com.ai.commons.beans.order.OrderSearchResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/***************************************************************************
 *<PRE>
 *  Project Name    : api
 *
 *  Package Name    : com.ai.api.controller.impl
 *
 *  File Name       : OrderImpl.java
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

@RestController
public class OrderImpl implements Order {

	protected Logger logger = LoggerFactory.getLogger(OrderImpl.class);

	@Autowired
	OrderService orderService;

	@Override
	@RequestMapping(value = "/user/{userId}/orders", method = RequestMethod.GET)
	public ResponseEntity<List<OrderSearchResultBean>> getOrderListByUserId(@PathVariable("userId") String userId,
	                                                                        @PathParam("page") int pageNumber,
	                                                                        @PathParam("types") String orderTypeArray,
	                                                                        @PathParam("status") String orderStatus,
	                                                                        @PathParam("start") String starts,
	                                                                        @PathParam("end") String ends,
	                                                                        @PathParam("keyword") String keyword) {

		OrderSearchCriteriaBean criteriaBean = new OrderSearchCriteriaBean();

		//fill criteria to criteriaBean
		List<OrderSearchResultBean> result = orderService.getOrdersByUserId(criteriaBean);

		if(result!=null){
			return new ResponseEntity<>(result, HttpStatus.OK);
		}else{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}
