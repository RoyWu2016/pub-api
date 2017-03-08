package com.ai.api.controller.impl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.ai.aims.services.dto.order.OrderDTO;
import com.ai.aims.services.model.OrderMaster;
import com.ai.api.bean.OrderSearchBean;
import com.ai.api.bean.OrderTestBean;
import com.ai.api.config.ServiceConfig;
import com.ai.api.controller.LTOrder;
import com.ai.api.service.LTOrderService;
import com.ai.api.service.LTParameterService;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.ApiCallResult;
import com.ai.program.model.Program;

@SuppressWarnings({"rawtypes"})
@RestController
@Api(tags = {"Lab Test"}, description = "Lab Test booking APIs")
public class LTOrderImpl implements LTOrder {
	
	protected Logger logger = LoggerFactory.getLogger(LTOrderImpl.class);
	
	@Autowired
	@Qualifier("serviceConfig")
	private ServiceConfig config;
	
	@Autowired
	@Qualifier("ltorderService")
	private LTOrderService ltOrderService;

	@Autowired
	@Qualifier("ltparameterService")
	private LTParameterService ltparameterService;

	@ApiOperation(value = "Order Add API", produces = "application/json", response = OrderDTO.class, httpMethod = "POST")
	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/lt/order", method = RequestMethod.POST)
	public ResponseEntity<ApiCallResult> addOrder(HttpServletRequest request, 
			@ApiParam(value="User ID") @PathVariable String userId) {
		ApiCallResult callResult = new ApiCallResult();
		try {
			callResult = ltOrderService.saveOrder(userId, new OrderMaster());
		} catch (Exception e) {
			logger.error("create order error: " + ExceptionUtils.getFullStackTrace(e));
			callResult.setMessage("Error in creating LT order in back end service.");
			return new ResponseEntity<ApiCallResult>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ApiCallResult>(callResult, HttpStatus.OK);
	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "Get Orders API", produces = "application/json", response = OrderSearchBean.class, httpMethod = "GET", responseContainer = "List")
	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/lt/orders", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> searchLTOrders(
			@ApiParam(value="User ID") @PathVariable("userId") String userId,
			@ApiParam(value="Order Status") @RequestParam(value = "orderStatus", required = false, defaultValue = "") String orderStatus,
			@ApiParam(value="Order Clone Type", allowableValues="reorder,retest") @RequestParam(value = "cloneType", required = false, defaultValue = "") String cloneType,
			@ApiParam(value="Page Number") @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNumber,
			@ApiParam(value="Page Size") @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize) {
		ApiCallResult callResult = new ApiCallResult();
		try {
			Map<String, Object> searchParams = new HashMap<String, Object>();
			searchParams.put("userId", userId);
			searchParams.put("orderStatus", orderStatus);
			searchParams.put("cloneType", cloneType);
			List<OrderSearchBean> list = ltOrderService.searchLTOrders(searchParams, pageSize, pageNumber);
			if (null!=list && list.size()>0){
				callResult.setContent(list);
				return new ResponseEntity<ApiCallResult>(callResult, HttpStatus.OK);
			}else {
				callResult.setContent(list);
                callResult.setMessage("Got empty LT orders list.");
                return new ResponseEntity<ApiCallResult>(callResult, HttpStatus.NO_CONTENT);
            }
		} catch (Exception e) {
			logger.error("get orders search error: " + ExceptionUtils.getFullStackTrace(e));
            callResult.setMessage("Error in searching LT orders.");
            return new ResponseEntity<ApiCallResult>(callResult, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "Get Order API", produces = "application/json", response = OrderDTO.class, httpMethod = "GET")
	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/lt/order/{orderId}", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> findOrder(
			@ApiParam(value="Order ID") @PathVariable("orderId") String orderId,
			@PathVariable String userId) {
		ApiCallResult callResult = new ApiCallResult();
		try {
            OrderDTO order = ltOrderService.findOrder(orderId);
            if (null!=order) {
                callResult.setContent(order);
                return new ResponseEntity<ApiCallResult>(callResult, HttpStatus.OK);
            }else {
                callResult.setMessage("can't get LT order by orderId:" + orderId);
                return new ResponseEntity<ApiCallResult>(callResult, HttpStatus.NOT_FOUND);
            }
		} catch (Exception e) {
			logger.error("Error in getting order: " + ExceptionUtils.getFullStackTrace(e));
			callResult.setMessage("can't get LT order by orderId:" + orderId+". error occurred!");
            return new ResponseEntity<ApiCallResult>(callResult, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ApiOperation(value = "Order Edit API", produces = "application/json", response = OrderDTO.class, httpMethod = "PUT")
	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/lt/order/{orderId}", method = RequestMethod.PUT)
	public ResponseEntity<ApiCallResult> editOrder(HttpServletRequest request, 
			@ApiParam(value="User ID") @PathVariable String userId, 
			@ApiParam(value="Order ID") @PathVariable String orderId, 
			@RequestBody OrderMaster order,
			@ApiParam(value="Send Mail") @RequestParam(value = "sendMail", required = true, defaultValue = "false") Boolean sendMail) {
		ApiCallResult callResult = new ApiCallResult();
		try {
			order.setId(orderId);
			callResult = ltOrderService.editOrder(userId, order, sendMail);
		} catch (Exception e) {
			logger.error("get orders search error: " + ExceptionUtils.getFullStackTrace(e));
			callResult.setMessage("Error in saving updates to LT order.");
			return new ResponseEntity<ApiCallResult>(callResult, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ApiCallResult>(callResult, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Order Delete API", produces = "application/json", httpMethod = "DELETE")
	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/lt/orders", method = RequestMethod.DELETE)
	public ResponseEntity<ApiCallResult> deleteOrders(HttpServletRequest request,
			@ApiParam(value="User ID") @PathVariable String userId,
			@ApiParam(value="Order IDs") @RequestParam String orderIds) {
		ApiCallResult callResult = new ApiCallResult();
		try {
			callResult = ltOrderService.deleteOrders(userId, orderIds);
		} catch (Exception e) {
			logger.error("get orders search error: " + ExceptionUtils.getFullStackTrace(e));
			callResult.setMessage("Error in deleting LT order.");
			return new ResponseEntity<ApiCallResult>(callResult, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ApiCallResult>(callResult, HttpStatus.OK);
	}

	@Override
	@ApiOperation(value = "Search User's LT Program API", produces = "application/json", response = Program.class, httpMethod = "GET", responseContainer = "List")
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/lt/programs", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> searchPrograms(
//			@RequestParam(value = "refresh", defaultValue = "false") boolean refresh,
			@PathVariable String userId) {
		ApiCallResult callResult = new ApiCallResult();
		/*if (!refresh) {
			logger.info("try to searchPrograms from redis ...");
			String jsonStringTextileProductCategory = RedisUtil.get("ltProgramsCache");
			programs = JSON.parseArray(jsonStringTextileProductCategory, Program.class);
		}
		if (null == programs) {*/
		try {
			callResult = ltparameterService.searchPrograms(userId);
			logger.info("saving searchPrograms");
			//RedisUtil.set("ltProgramsCache", JSON.toJSONString(programs), RedisUtil.HOUR * 24);

			return new ResponseEntity<ApiCallResult>(callResult, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("search Programs error: " + ExceptionUtils.getFullStackTrace(e));
			callResult.setMessage("Error in getting LT programs:");
			return new ResponseEntity<ApiCallResult>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		/*} else {
			logger.info("get lt programs from redis successfully");
			return new ResponseEntity<List<Program>>(programs, HttpStatus.OK);
		}*/
	}

	@ApiOperation(value = "Get Order Test Assignments API", produces = "application/json", response = OrderTestBean.class, httpMethod = "GET")
	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/lt/order/{orderId}/tests", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> findOrderTestAssignments (
			@ApiParam(value="User ID") @PathVariable("userId") String userId,
			@ApiParam(value="Order ID") @PathVariable("orderId") String orderId) {
		ApiCallResult callResult = new ApiCallResult();
		try {
			callResult = ltOrderService.findOrderTestAssignments(orderId);
			return new ResponseEntity<ApiCallResult>(callResult, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("get order test assignments error: " + ExceptionUtils.getFullStackTrace(e));
			callResult.setMessage("Error in getting order test assignments");
			return new ResponseEntity<ApiCallResult>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ApiOperation(value = "Update Order Test Assignments API", produces = "application/json", httpMethod = "PUT")
	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/lt/order/{orderId}/tests", method = RequestMethod.PUT)
	public ResponseEntity<ApiCallResult> updateOrderTestAssignments (
			@ApiParam(value="User ID") @PathVariable("userId") String userId,
			@ApiParam(value="Order ID") @PathVariable("orderId") String orderId,
			@ApiParam(value="Test IDs") @RequestParam(value = "testIds", required = true, defaultValue = "") String testIds) {
		ApiCallResult callResult = new ApiCallResult();
		try {
			callResult = ltOrderService.updateOrderTestAssignments(userId, orderId, testIds);
			return new ResponseEntity<ApiCallResult>(callResult, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("update order test assignments error: " + ExceptionUtils.getFullStackTrace(e));
			callResult.setMessage("Error in updating order test assignments");
			return new ResponseEntity<ApiCallResult>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ApiOperation(value = "Delete Order Test Assignment API", produces = "application/json", httpMethod = "DELETE")
	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/lt/order/{orderId}/test/{testId}", method = RequestMethod.DELETE)
	public ResponseEntity<ApiCallResult> deleteOrderTestAssignment (
			@ApiParam(value="User ID") @PathVariable("userId") String userId,
			@ApiParam(value="Order ID") @PathVariable("orderId") String orderId,
			@ApiParam(value="Test Assignment ID") @PathVariable("testId") String testId) {
		ApiCallResult callResult = new ApiCallResult();
		try {
			callResult = ltOrderService.deleteOrderTestAssignment(userId, testId);
			return new ResponseEntity<ApiCallResult>(callResult, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("delete order test assignment error: " + ExceptionUtils.getFullStackTrace(e));
			callResult.setMessage("Error in deleting order test assignment");
			return new ResponseEntity<ApiCallResult>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ApiOperation(value = "Clone Order API", produces = "application/json", httpMethod = "POST")
	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/lt/order/{orderId}/clone/{cloneType}", method = RequestMethod.POST)
	public ResponseEntity<ApiCallResult> cloneOrder (
			@ApiParam(value="User ID") @PathVariable("userId") String userId,
			@ApiParam(value="Order ID") @PathVariable("orderId") String orderId,
			@ApiParam(value="Clone Type", allowableValues="reorder,retest,copy") @PathVariable("cloneType") String cloneType) {
		ApiCallResult callResult = new ApiCallResult();
		try {
			callResult = ltOrderService.cloneOrder(userId, orderId, cloneType);
			return new ResponseEntity<ApiCallResult>(callResult, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("clone order error: " + ExceptionUtils.getFullStackTrace(e));
			callResult.setMessage("Error in cloning order");
			return new ResponseEntity<ApiCallResult>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
