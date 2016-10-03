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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.ai.aims.services.model.OrderMaster;
import com.ai.aims.services.model.TagTestMap;
import com.ai.api.bean.OrderSearchBean;
import com.ai.api.config.ServiceConfig;
import com.ai.api.controller.LTOrder;
import com.ai.api.service.LTOrderService;
import com.ai.api.service.UserService;
import com.ai.api.util.AIUtil;
import com.ai.commons.annotation.TokenSecured;

/***************************************************************************
 *<PRE>
 *  Project Name    : api
 *
 *  Package Name    : com.ai.api.controller.impl
 *
 *  File Name       : LTOrderImpl.java
 *
 *  Creation Date   : Sep 3, 2016
 *
 *  Author          : Aashish Thakran
 *
 *  Purpose         : TODO
 *
 *
 *  History         : TODO
 *
 *</PRE>
 ***************************************************************************/

@RestController
public class LTOrderImpl implements LTOrder {

	protected Logger logger = LoggerFactory.getLogger(LTOrderImpl.class);

	@Autowired
	UserService userService;

	@Autowired
	LTOrderService ltOrderService;

	@Autowired
	@Qualifier("serviceConfig")
	private ServiceConfig config;

	@ApiOperation(value = "LT Order Add API",		
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
			ordersList = ltOrderService.searchLTOrders(userId, serviceType, orderStatus, pageSize.toString(), pageNumber.toString());
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
