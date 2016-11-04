/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.controller.impl;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
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

import com.ai.aims.services.model.OrderMaster;
import com.ai.api.bean.OrderSearchBean;
import com.ai.api.config.ServiceConfig;
import com.ai.api.controller.Order;
import com.ai.api.service.OrderService;
import com.ai.api.service.UserService;
import com.ai.api.util.AIUtil;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.order.SimpleOrderSearchBean;
import com.ai.commons.beans.psi.InspectionBookingBean;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import io.swagger.annotations.ApiOperation;

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
@SuppressWarnings("rawtypes")
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
			@PathVariable("orderId") String orderId, @RequestParam("reason") String reason,
			@RequestParam(value = "reason_options", required = false) String reason_options) {
		try {
			logger.info("cancelOrder ...");
			logger.info("userId :" + userId);
			logger.info("orderId:" + orderId);

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
	public ResponseEntity<ApiCallResult> getOrderDetail(@PathVariable("userId") String userId,
														@PathVariable("orderId") String orderId) {
		ApiCallResult callResult = new ApiCallResult();
		try {
			logger.info("getOrderDetail ...");
			logger.info("userId :" + userId);
			logger.info("orderId:" + orderId);
			InspectionBookingBean orderBean = orderService.getOrderDetail(userId, orderId);
			if (orderBean != null) {
                JSONObject jsonObject = (JSONObject)JSON.toJSON(orderBean);
			    try {
                    callResult = orderService.getOrderPrice(userId, orderId);
                    jsonObject.put("orderPrice",callResult.getContent());
                    
                }catch (Exception e){
                    logger.error("error occurred! getOrderPrice failed",e);
                    jsonObject.put("orderPrice",null);
                }
			    try{
                    callResult = orderService.getOrderActionEdit(orderId);
                    jsonObject.put("editable",callResult.getContent());
                    
                    callResult = orderService.getOrderActionCancel(orderId);
                    jsonObject.put("cancelable",callResult.getContent());
			    }catch (Exception e) {
			    	 logger.error("error occurred! getOrderAction failed",e);
			    	 jsonObject.put("editable",null);
			    	 jsonObject.put("cancelable",null);
			    }
                callResult.setContent(jsonObject);
				return new ResponseEntity<>(callResult, HttpStatus.OK);
			} else {
			    callResult.setMessage("can't get order by orderId:"+orderId);
				return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			logger.error("error in getOrderDetail", e);
			e.printStackTrace();
            callResult.setMessage("convert bean to json failed!"+e.toString());
		}
		return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/psi-order", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> createOrderByDraft(@PathVariable("userId") String userId,
			@RequestParam(value = "draftId", required = true) String draftId) {

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			logger.info("createOrderByDraft ...");
			logger.info("userId :" + userId);
			logger.info("draftId :" + draftId);
			InspectionBookingBean orderBean = orderService.createOrderByDraft(userId, draftId);
			if (orderBean != null) {
				map.put("success", true);
				map.put("data", orderBean);
				return new ResponseEntity<>(map, HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("error in createOrderByDraft", e);
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
			logger.info("userId :" + userId);
			logger.info("orderId:" + orderId);
			InspectionBookingBean orderBean = orderService.editOrder(userId, orderId);
			if (orderBean != null) {
				map.put("success", true);
				map.put("data", orderBean);
				return new ResponseEntity<>(map, HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("error in editOrder", e);
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/psi-order/{orderId}/draft/{draftId}/saved", method = RequestMethod.PUT)
	public ResponseEntity<Map<String, Object>> saveOrderByDraft(@PathVariable("userId") String userId,
			@PathVariable("draftId") String draftId, @PathVariable("orderId") String orderId) {

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			logger.info("saveOrderByDraft ...");
			logger.info("userId :" + userId);
			logger.info("draftId :" + draftId);
			logger.info("orderId:" + orderId);
			InspectionBookingBean orderBean = orderService.saveOrderByDraft(userId, draftId);
			if (orderBean != null) {
				map.put("success", true);
				map.put("data", orderBean);
				return new ResponseEntity<>(map, HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("error in saveOrderByDraft", e);
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/psi-orders", method = RequestMethod.GET)
	public ResponseEntity<List<SimpleOrderSearchBean>> searchOrders(@PathVariable("userId") String userId,
			@RequestParam(value = "service-type", required = false, defaultValue = "") String serviceType,
			@RequestParam(value = "start", required = false, defaultValue = "") String startDate,
			@RequestParam(value = "end", required = false, defaultValue = "") String endDate,
			@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
			@RequestParam(value = "status", required = false, defaultValue = "") String orderStatus,
			@RequestParam(value = "page-size", required = false, defaultValue = "20") String pageSize,
			@RequestParam(value = "page", required = false, defaultValue = "1") String pageNumber) {

		List<SimpleOrderSearchBean> ordersList = new ArrayList<SimpleOrderSearchBean>();
		try {
			ordersList = orderService.searchOrders(userId, serviceType, startDate, endDate, keyword, orderStatus,
					pageSize, pageNumber);
			// if not data found, just return 200 with empty list
			return new ResponseEntity<>(ordersList, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("get orders search error: " + ExceptionUtils.getFullStackTrace(e));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/psi-orders-export", method = RequestMethod.GET)
	public ResponseEntity<Map<String, String>> exportOrders(@PathVariable("userId") String userId,
			@RequestParam(value = "service-type", required = false, defaultValue = "") String serviceType,
			@RequestParam(value = "start", required = false, defaultValue = "") String start,
			@RequestParam(value = "end", required = false, defaultValue = "") String end,
			@RequestParam(value = "status", required = false, defaultValue = "") String orderStatus) {

		Map<String, String> result = new HashMap<String, String>();
		String inspectionPeriod = null;
		if (!("".equals(start) && "".equals(end))) {
			inspectionPeriod = start + " - " + end;
		} else {
			// get last 3 month
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar rightNow = Calendar.getInstance();
			String endStr = sf.format(rightNow.getTime());
			rightNow.add(Calendar.MONTH, -3);
			String startStr = sf.format(rightNow.getTime());

			inspectionPeriod = startStr + " - " + endStr;
		}
		try {
			InputStream inpurtStream = orderService.exportOrders(userId, serviceType, start, end, orderStatus,
					inspectionPeriod);
			String fileStr = null;
			if (inpurtStream != null) {
				try {
					byte[] data = IOUtils.toByteArray(inpurtStream);
					fileStr = Base64.encode(data);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			result.put("xlsx_base64", fileStr);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("export orders search error: " + ExceptionUtils.getFullStackTrace(e));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ApiOperation(value = "Order Add API", produces = "application/json", response = OrderMaster.class, httpMethod = "POST")
	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/lt-orders", method = RequestMethod.POST)
	public ResponseEntity<OrderMaster> addOrder(HttpServletRequest request, @PathVariable String userId,
			@RequestBody OrderMaster orderMaster) {
		RestTemplate restTemplate = new RestTemplate();
		OrderMaster orderMasterObj = null;
		try {
			AIUtil.addRestTemplateMessageConverter(restTemplate);
			String url = new StringBuilder(config.getAimsServiceBaseUrl()).append("/api/ordermanagement/order/")
					.append(userId).toString();
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
	public ResponseEntity<List<OrderSearchBean>> searchLTOrders(@PathVariable("userId") String userId,
			@RequestParam(value = "serviceType", required = false, defaultValue = "") String serviceType,
			@RequestParam(value = "orderStatus", required = false, defaultValue = "") String orderStatus,
			@RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNumber,
			@RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize) {
		List<OrderSearchBean> ordersList = new ArrayList<OrderSearchBean>();
		try {
			ordersList = orderService.searchLTOrders(userId, serviceType, orderStatus, pageSize.toString(),
					pageNumber.toString());
			// if not data found, just return 200 with empty list
			return new ResponseEntity<List<OrderSearchBean>>(ordersList, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("get orders search error: " + ExceptionUtils.getFullStackTrace(e));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

//	@Override
//	@TokenSecured
//	@RequestMapping(value = "/user/{userId}/order/{orderId}/editable-cancelable", method = RequestMethod.GET)
//	public ResponseEntity<Map<String,ApiCallResult>> getOrderAction(@PathVariable("userId") String userId,
//			@PathVariable("orderId") String orderId) {
//		logger.info("invoke: " + "/user/" + userId + "/order/" + orderId + "/editable-cancelable");
//		Map<String,ApiCallResult> result = new HashMap<>();
//		ApiCallResult editReslut = orderService.getOrderActionEdit(orderId);
//		ApiCallResult cancelReslut = orderService.getOrderActionCancel(orderId);
//		result.put("editable", editReslut);
//		result.put("cancelable", cancelReslut);
//		if(null == editReslut.getMessage() && null == cancelReslut.getMessage()) {
//			return new ResponseEntity<>(result,HttpStatus.OK);
//		}else {
//			return new ResponseEntity<>(result,HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//
//	}
}
