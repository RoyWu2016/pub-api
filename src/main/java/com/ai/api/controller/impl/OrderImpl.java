/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.controller.impl;

import io.swagger.annotations.ApiOperation;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.ai.aims.services.model.OrderMaster;
import com.ai.aims.services.model.TagTestMap;
import com.ai.api.bean.OrderSearchBean;
import com.ai.api.config.ServiceConfig;
import com.ai.api.controller.Order;
import com.ai.api.service.OrderService;
import com.ai.api.service.UserService;
import com.ai.api.util.AIUtil;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.order.SimpleOrderSearchBean;
import com.ai.commons.beans.psi.InspectionBookingBean;

/***************************************************************************
 * <PRE>
 * Project Name    : api
 * <p>
 * Package Name    : com.ai.api.controller.impl
 * <p>
 * File Name       : OrderImpl.java
 * <p>
 * Creation Date   : Jul 13, 2016
 * <p>
 * Author          : Allen Zhang
 * <p>
 * Purpose         : TODO
 * <p>
 * <p>
 * History         : TODO
 * <p>
 * </PRE>
 ***************************************************************************/

@RestController
public class OrderImpl implements Order {

	protected Logger logger = LoggerFactory.getLogger(OrderImpl.class);

	@Autowired
	UserService userService;

	@Autowired
	OrderService orderService;

	@Autowired
	@Qualifier("serviceConfig")
	private ServiceConfig config;
	
	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/psi-order/{orderId}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> cancelOrder(@PathVariable("userId") String userId,
	                                           @PathVariable("orderId") String orderId,
	                                           @RequestParam("reason") String reason,
	                                           @RequestParam("reason_options") String reason_options
	                                          ) {
		try {
			logger.info("cancelOrder ...");
			logger.info("userId :"+userId);
			logger.info("orderId:"+orderId);
			
		
				Boolean result = orderService.cancelOrder(userId, orderId, reason, reason_options);
				if (result) {
					return new ResponseEntity<>(result, HttpStatus.OK);
				}

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/psi-order/{orderId}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getOrderDetail(@PathVariable("userId") String userId,
															  @PathVariable("orderId") String orderId) {

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			logger.info("getOrderDetail ...");
			logger.info("userId :"+userId);
			logger.info("orderId:"+orderId);
			InspectionBookingBean orderBean = orderService.getOrderDetail(userId, orderId);
			if (orderBean != null) {
				map.put("success", true);
				map.put("data", orderBean);
				return new ResponseEntity<>(map, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			logger.error("error in getOrderDetail",e);
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/psi-order", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> createOrderByDraft(@PathVariable("userId") String userId,
	                                                             @RequestParam(value = "draftId", required = true) String draftId
															  ) {

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			logger.info("createOrderByDraft ...");
			logger.info("userId :"+userId);
			logger.info("draftId :"+draftId);
			InspectionBookingBean orderBean = orderService.createOrderByDraft(userId, draftId);
			if (orderBean != null) {
				map.put("success", true);
				map.put("data", orderBean);
				return new ResponseEntity<>(map, HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("error in createOrderByDraft",e);
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/psi-order/{orderId}/editing", method = RequestMethod.PUT)
	public ResponseEntity<Map<String, Object>> editOrder(@PathVariable("userId") String userId,
																  @PathVariable("orderId") String orderId) {

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			logger.info("editOrder ...");
			logger.info("userId :"+userId);
			logger.info("orderId:"+orderId);
			InspectionBookingBean orderBean = orderService.editOrder(userId, orderId);
			if (orderBean != null) {
				map.put("success", true);
				map.put("data", orderBean);
				return new ResponseEntity<>(map, HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("error in editOrder",e);
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/psi-order/{orderId}/draft/{draftId}/saved", method = RequestMethod.PUT)
	public ResponseEntity<Map<String, Object>> saveOrderByDraft(@PathVariable("userId") String userId,
																@PathVariable("draftId") String draftId,
																@PathVariable("orderId") String orderId) {

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			logger.info("saveOrderByDraft ...");
			logger.info("userId :"+userId);
			logger.info("draftId :"+draftId);
			logger.info("orderId:"+orderId);
			InspectionBookingBean orderBean = orderService.saveOrderByDraft(userId, draftId);
			if (orderBean != null) {
				map.put("success", true);
				map.put("data", orderBean);
				return new ResponseEntity<>(map, HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("error in saveOrderByDraft",e);
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
    @TokenSecured
    @RequestMapping(value = "/user/{userId}/psi-orders", method = RequestMethod.GET)
	public ResponseEntity<List<SimpleOrderSearchBean>> searchOrders(@PathVariable("userId")String userId,
													   @RequestParam(value = "service-type", required = false , defaultValue="") String serviceType,
													   @RequestParam(value = "start", required = false, defaultValue="") String startDate,
													   @RequestParam(value = "end", required = false , defaultValue="") String endDate,
													   @RequestParam(value = "keyword", required = false , defaultValue="") String keyword,
													   @RequestParam(value = "status", required = false, defaultValue="") String orderStatus,
													   @RequestParam(value = "page-size", required = false , defaultValue="20") String pageSize,
													   @RequestParam(value = "page", required = false , defaultValue="1") String pageNumber) {
		
		List<SimpleOrderSearchBean> ordersList = new ArrayList<SimpleOrderSearchBean>();
		try {
			ordersList = orderService.searchOrders(userId, serviceType,
					startDate, endDate, keyword, orderStatus,pageSize, pageNumber);
			//if not data found, just return 200 with empty list
			return new ResponseEntity<>(ordersList, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("get orders search error: " + ExceptionUtils.getFullStackTrace(e));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@ApiOperation(value = "Order Add API",		
	        produces = "application/json",
		    response = OrderMaster.class,
		    httpMethod = "POST")
	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/lt-orders", method = RequestMethod.POST)
	public ResponseEntity<OrderMaster> addOrder(HttpServletRequest request, @PathVariable String userId, @RequestBody OrderMaster orderMaster) {
		RestTemplate restTemplate = new RestTemplate();
		OrderMaster orderMasterObj = null;
		try {
			AIUtil.addRestTemplateMessageConverter(restTemplate);
			String url = new StringBuilder(config.getAimsServiceBaseUrl()).append("/api/ordermanagement/order/").append(userId).toString();
			orderMaster.setOrderStatus("Draft");
	        orderMasterObj = restTemplate.postForObject(url, orderMaster, OrderMaster.class, request);
		} catch (Exception e) {
			logger.error("create order error: " + ExceptionUtils.getFullStackTrace(e));
			return new ResponseEntity<OrderMaster>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<OrderMaster>(orderMasterObj, HttpStatus.OK);
	}
	
	@Override
    @TokenSecured
    @RequestMapping(value = "/user/{userId}/lt-orders/list", method = RequestMethod.GET)
	public ResponseEntity<List<OrderSearchBean>> searchLTOrders(@PathVariable("userId")String userId, 
			@RequestParam(value = "serviceType", required = false , defaultValue="") String serviceType, 
			@RequestParam(value = "orderStatus", required = false , defaultValue="") String orderStatus, 
			@RequestParam(value = "pageNo", required = false , defaultValue="1") Integer pageNumber, 
			@RequestParam(value = "pageSize", required = false , defaultValue="20") Integer pageSize) {
		List<OrderSearchBean> ordersList = new ArrayList<OrderSearchBean>();
		try {
			ordersList = orderService.searchLTOrders(userId, serviceType, orderStatus, pageSize.toString(), pageNumber.toString());
			//if not data found, just return 200 with empty list
			return new ResponseEntity<List<OrderSearchBean>>(ordersList, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("get orders search error: " + ExceptionUtils.getFullStackTrace(e));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Override
    @TokenSecured
    @RequestMapping(value = "/user/{userId}/lt-tests/list", method = RequestMethod.GET)
	public ResponseEntity<List<TagTestMap>> searchLTTests(@PathVariable("userId") String userId, 
			@RequestParam(value = "countryName", required = false ) List<String> countryName, 
			@RequestParam(value = "productCategory", required = false) List<String> productCategory, 
			@RequestParam(value = "keywords", required = false) List<String> keywords, 
			@RequestParam(value = "pageNo", required = false , defaultValue="1") Integer pageNumber, 
			@RequestParam(value = "pageSize", required = false , defaultValue="20") Integer pageSize) {
		RestTemplate restTemplate = new RestTemplate();
		List<TagTestMap> tests = new ArrayList<TagTestMap>();
		try {
			AIUtil.addRestTemplateMessageConverter(restTemplate);
			String url = new StringBuilder(config.getAimsServiceBaseUrl()).append("/tag/search/tests").toString();
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
			        .queryParam("page", pageNumber - 1)
			        .queryParam("size", pageSize)
			        .queryParam("direction", "desc" );	
			
			if(null != countryName && !countryName.isEmpty()) {
				builder.queryParam("countryName", countryName);
			}
			if(null != productCategory && !productCategory.isEmpty()) {
				builder.queryParam("productCategory", productCategory);
			}
			if(null != keywords && !keywords.isEmpty()) {
				builder.queryParam("testName", keywords);
			}
			
			tests = Arrays.asList(restTemplate.getForObject(builder.build().encode().toUri(), TagTestMap[].class));
		} catch (Exception e) {
			logger.error("error fetching tests: " + ExceptionUtils.getFullStackTrace(e));
			return new ResponseEntity<List<TagTestMap>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<TagTestMap>>(tests, HttpStatus.OK);		
	}
}
