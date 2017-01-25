package com.ai.api.controller.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ai.aims.services.model.OrderMaster;
import com.ai.api.bean.OrderSearchBean;
import com.ai.api.config.ServiceConfig;
import com.ai.api.controller.LTOrder;
import com.ai.api.service.LTOrderService;
import com.ai.api.service.LTParameterService;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.ApiCallResult;
import com.ai.program.model.Program;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

	@Autowired
	@Qualifier("ltparameterService")
	private LTParameterService ltparameterService;

	@ApiOperation(value = "Order Add API", produces = "application/json", response = OrderMaster.class, httpMethod = "POST")
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
			callResult.setMessage("can't save LT order");
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
			@ApiParam(value="Page Number") @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNumber,
			@ApiParam(value="Page Size") @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize) {
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

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "Get Order API", produces = "application/json", response = OrderMaster.class, httpMethod = "GET")
	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/lt/order/{orderId}", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> findOrder(
			@ApiParam(value="Order ID") @PathVariable("orderId") String orderId,
			@PathVariable String userId) {
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
	@RequestMapping(value = "/user/{userId}/lt/order/{orderId}", method = RequestMethod.PUT)
	public ResponseEntity<ApiCallResult> editOrder(HttpServletRequest request, 
			@ApiParam(value="User ID") @PathVariable String userId, 
			@ApiParam(value="Order ID") @PathVariable String orderId, 
			@RequestBody OrderMaster order) {
		ApiCallResult callResult = new ApiCallResult();
		try {
			order.setId(orderId);
			callResult = ltOrderService.editOrder(userId, order);
		} catch (Exception e) {
			logger.error("get orders search error: " + ExceptionUtils.getFullStackTrace(e));
			callResult.setMessage("can't edit LT order");
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
			callResult.setMessage("can't get LT programs:");
			return new ResponseEntity<ApiCallResult>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		/*} else {
			logger.info("get lt programs from redis successfully");
			return new ResponseEntity<List<Program>>(programs, HttpStatus.OK);
		}*/
	}


}
