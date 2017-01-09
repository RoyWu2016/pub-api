package com.ai.api.controller.impl;

import javax.servlet.http.HttpServletRequest;

import com.ai.api.bean.OrderSearchBean;
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
import com.ai.api.config.ServiceConfig;
import com.ai.api.controller.LTOrder;
import com.ai.api.lab.service.LTOrderService;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.ApiCallResult;

import io.swagger.annotations.ApiOperation;

import java.util.List;

@SuppressWarnings({"rawtypes"})
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
	public ResponseEntity<ApiCallResult> addOrder(HttpServletRequest request, @PathVariable String userId, @RequestBody OrderMaster order) {
		ApiCallResult callResult = new ApiCallResult();
		try {
			callResult = ltOrderService.saveOrder(userId, order);
		} catch (Exception e) {
			logger.error("create order error: " + ExceptionUtils.getFullStackTrace(e));
			callResult.setMessage("can't save LT order");
			return new ResponseEntity<ApiCallResult>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ApiCallResult>(callResult, HttpStatus.OK);
	}

	@ApiOperation(value = "Get Orders API", produces = "application/json", response = OrderMaster.class, httpMethod = "GET", responseContainer = "List")
	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/lt-orders/list", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> searchLTOrders(
			@PathVariable("userId") String userId,
			@RequestParam(value = "orderStatus", required = false, defaultValue = "") String orderStatus,
			@RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNumber,
			@RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize) {
		ApiCallResult callResult = new ApiCallResult();
		try {
			List<OrderSearchBean> list = ltOrderService.searchLTOrders(userId, orderStatus, pageSize, pageNumber);
			if (null!=list && list.size()>0){
				callResult.setContent(list);
				return new ResponseEntity<ApiCallResult>(callResult, HttpStatus.OK);
			}else {
				callResult.setContent(list);
                callResult.setMessage("get empty LT orders list.");
                return new ResponseEntity<ApiCallResult>(callResult, HttpStatus.NO_CONTENT);
            }
		} catch (Exception e) {
			logger.error("get orders search error: " + ExceptionUtils.getFullStackTrace(e));
            callResult.setMessage("can't get LT orders.error occurred!");
            return new ResponseEntity<ApiCallResult>(callResult, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ApiOperation(value = "Get Order API", produces = "application/json", response = OrderMaster.class, httpMethod = "GET")
	@Override
	@TokenSecured
	@RequestMapping(value = "/lt-orders/list/{orderId}", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> findOrder(@PathVariable("orderId") String orderId) {
		ApiCallResult callResult = new ApiCallResult();
		try {
            OrderMaster order = ltOrderService.findOrder(orderId);
            if (null!=order) {
                callResult.setContent(order);
                return new ResponseEntity<ApiCallResult>(callResult, HttpStatus.OK);
            }else {
                callResult.setMessage("can't get LT order by orderId:" + orderId);
                return new ResponseEntity<ApiCallResult>(callResult, HttpStatus.NOT_FOUND);
            }
		} catch (Exception e) {
			logger.error("get orders search error: " + ExceptionUtils.getFullStackTrace(e));
			callResult.setMessage("can't get LT order by orderId:" + orderId+". error occurred!");
            return new ResponseEntity<ApiCallResult>(callResult, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ApiOperation(value = "Order Edit API", produces = "application/json", response = OrderMaster.class, httpMethod = "PUT")
	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/lt-orders", method = RequestMethod.PUT)
	public ResponseEntity<ApiCallResult> editOrder(HttpServletRequest request, @PathVariable String userId, @RequestBody OrderMaster order) {
		ApiCallResult callResult = new ApiCallResult();
		try {
			callResult = ltOrderService.editOrder(userId, order);
		} catch (Exception e) {
			logger.error("get orders search error: " + ExceptionUtils.getFullStackTrace(e));
			callResult.setMessage("can't edit LT order");
			return new ResponseEntity<ApiCallResult>(callResult, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ApiCallResult>(callResult, HttpStatus.OK);
	}
}
