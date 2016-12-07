package com.ai.api.controller.impl;

/***************************************************************************
 * <PRE>
 *  Project Name    : api
 *
 *  Package Name    : com.ai.api.lab.controller.impl
 *
 *  File Name       : LTOrderImpl.java
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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ai.aims.services.model.OrderMaster;
import com.ai.api.bean.OrderSearchBean;
import com.ai.api.config.ServiceConfig;
import com.ai.api.controller.LTOrder;
import com.ai.api.lab.service.LTOrderService;
import com.ai.commons.annotation.TokenSecured;

import io.swagger.annotations.ApiOperation;

@RestController
public class LTOrderImpl implements LTOrder {
	
	protected Logger logger = LoggerFactory.getLogger(LTOrderImpl.class);
	
	@Autowired
	@Qualifier("serviceConfig")
	private ServiceConfig config;
	
	@Autowired
	@Qualifier("ltorderService")
	private LTOrderService ltOrderService;
	
	@ApiOperation(value = "Order Add API", produces = "application/json", response = OrderMaster.class, httpMethod = "POST")
	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/lt-orders", method = RequestMethod.POST)
	public ResponseEntity<OrderMaster> addOrder(HttpServletRequest request, @PathVariable String userId, @RequestBody OrderMaster order) {
		try {
			order = ltOrderService.saveOrder(userId, order);
		} catch (Exception e) {
			logger.error("create order error: " + ExceptionUtils.getFullStackTrace(e));
			return new ResponseEntity<OrderMaster>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<OrderMaster>(order, HttpStatus.OK);
	}

	@ApiOperation(value = "Get Orders API", produces = "application/json", response = OrderMaster.class, httpMethod = "GET", responseContainer = "List")
	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/lt-orders/list", method = RequestMethod.GET)
	public ResponseEntity<List<OrderSearchBean>> searchLTOrders(
			@PathVariable("userId") String userId,
			@RequestParam(value = "orderStatus", required = false, defaultValue = "") String orderStatus,
			@RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNumber,
			@RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize) {
		List<OrderSearchBean> ordersList = new ArrayList<OrderSearchBean>();
		try {
			ordersList = ltOrderService.searchLTOrders(userId, orderStatus, pageSize, pageNumber);
			return new ResponseEntity<List<OrderSearchBean>>(ordersList, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("get orders search error: " + ExceptionUtils.getFullStackTrace(e));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ApiOperation(value = "Get Order API", produces = "application/json", response = OrderMaster.class, httpMethod = "GET")
	@Override
	@TokenSecured
	@RequestMapping(value = "/lt-orders/list/{orderId}", method = RequestMethod.GET)
	public ResponseEntity<OrderMaster> findOrder(@PathVariable("orderId") String orderId) {
		OrderMaster order = null;
		try {
			order = ltOrderService.findOrder(orderId);
		} catch (Exception e) {
			logger.error("get orders search error: " + ExceptionUtils.getFullStackTrace(e));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<OrderMaster>(order, HttpStatus.OK);
	}

	@ApiOperation(value = "Order Edit API", produces = "application/json", response = OrderMaster.class, httpMethod = "PUT")
	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/lt-orders", method = RequestMethod.PUT)
	public ResponseEntity<OrderMaster> editOrder(HttpServletRequest request, @PathVariable String userId, @RequestBody OrderMaster order) {
		try {
			order = ltOrderService.editOrder(userId, order);
		} catch (Exception e) {
			logger.error("get orders search error: " + ExceptionUtils.getFullStackTrace(e));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<OrderMaster>(order, HttpStatus.OK);
	}
}
