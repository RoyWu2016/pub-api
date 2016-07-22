/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.controller.impl;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.ws.rs.PathParam;

import com.ai.api.controller.Order;
import com.ai.api.service.OrderService;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.legacy.order.OrderSearchCriteriaBean;
import com.ai.commons.beans.order.OrderSearchResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
	                                                                        @RequestParam("page") int pageNumber,
																			@RequestParam("types") String orderTypeArray,
																			@RequestParam("status") String orderStatus,
																			@RequestParam("start") String starts,
																			@RequestParam("end") String ends,
																			@RequestParam("keyword") String keywords) {

		OrderSearchCriteriaBean criteriaBean = new OrderSearchCriteriaBean();

		criteriaBean.setPageNumber(pageNumber);
		criteriaBean.setKeywords(keywords);
		if(starts.equals("") && ends.equals("")){
			Date current = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			ends = sdf.format(current);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(current);
			calendar.add(Calendar.MONTH, -1);
			starts = sdf.format(calendar.getTime());
		}
		criteriaBean.setStartDate(starts);
		criteriaBean.setEndDate(ends);
		criteriaBean.setUserID(userId);

		ArrayList<String> typeList = new ArrayList<String>();
		if (!orderTypeArray.equals("")) {
			String[] types = orderTypeArray.split(",");
			Collections.addAll(typeList, types);
		}
		criteriaBean.setServiceTypes(typeList);

		List<OrderSearchResultBean> result = null;
		if(orderStatus.equals("open")) {
			criteriaBean.setOrderStatus((short) 1);
			result = orderService.getOrdersByUserId(criteriaBean);
		} else if(orderStatus.equals("completed")){
			criteriaBean.setOrderStatus((short) 2);
			result = orderService.getOrdersByUserId(criteriaBean);
		} else if(orderStatus.equals("draft")){
			criteriaBean.setOrderStatus((short) 3);
			result = orderService.getDraftsByUserId(criteriaBean);
		}
		if(result!=null){
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}
}
