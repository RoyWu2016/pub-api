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

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ai.api.config.ServiceConfig;
import com.ai.api.controller.Order;
import com.ai.api.service.APIFileService;
import com.ai.api.service.OrderService;
import com.ai.api.service.UserService;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.fileservice.FileMetaBean;
import com.ai.commons.beans.order.SimpleOrderSearchBean;
import com.ai.commons.beans.psi.InspectionBookingBean;
import com.ai.commons.beans.psi.ProductBean;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

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

	@Autowired
	private APIFileService myFileService;

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
		if (userId == null || userId.isEmpty() || orderId == null || orderId.isEmpty()) {
			logger.error("userId:" + userId + ", orderId:" + orderId + " can't be null!");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		ApiCallResult callResult = new ApiCallResult();
		try {
			logger.info("getOrderDetail ...");
			logger.info("userId :" + userId);
			logger.info("orderId:" + orderId);
			InspectionBookingBean orderBean = orderService.getOrderDetail(userId, orderId);
			if (orderBean != null) {
				JSONObject jsonObject = (JSONObject) JSON.toJSON(orderBean);
				try {
					callResult = orderService.getOrderPrice(userId, orderId);
					jsonObject.put("orderPrice", callResult.getContent());

				} catch (Exception e) {
					logger.error("error occurred! getOrderPrice failed", e);
					jsonObject.put("orderPrice", null);
				}
				try {
					callResult = orderService.getOrderActionEdit(orderId);
					jsonObject.put("editable", callResult.getContent());

					callResult = orderService.getOrderActionCancel(orderId);
					jsonObject.put("cancelable", callResult.getContent());
				} catch (Exception e) {
					logger.error("error occurred! getOrderAction failed", e);
					jsonObject.put("editable", null);
					jsonObject.put("cancelable", null);
				}
				callResult.setContent(jsonObject);
				return new ResponseEntity<>(callResult, HttpStatus.OK);
			} else {
				callResult.setMessage("can't get order by orderId:" + orderId);
				return new ResponseEntity<>(callResult, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			logger.error("error in getOrderDetail", e);
			e.printStackTrace();
			callResult.setMessage("convert bean to json failed!" + e.toString());
		}
		return new ResponseEntity<>(callResult, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/psi-order", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Object>> createOrderByDraft(@PathVariable("userId") String userId,
			@RequestParam(value = "draftId", required = true) String draftId) {

		if (userId == null || userId.isEmpty() || draftId == null || draftId.isEmpty()) {
			logger.error("userId:" + userId + ", draftId:" + draftId + " can't be null!");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

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
		if (userId == null || userId.isEmpty() || orderId == null || orderId.isEmpty()) {
			logger.error("userId:" + userId + ", orderId:" + orderId + " can't be null!");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

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
	@RequestMapping(value = "/user/{userId}/re-inspection-list", method = RequestMethod.GET)
	public ResponseEntity<List<SimpleOrderSearchBean>> getReInspectionList(@PathVariable("userId") String userId,
			@RequestParam(value = "service-type", required = false, defaultValue = "") String serviceType,
			@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
			@RequestParam(value = "page-size", required = false, defaultValue = "20") String pageSize,
			@RequestParam(value = "page", required = false, defaultValue = "1") String pageNumber) {

		List<SimpleOrderSearchBean> ordersList = new ArrayList<SimpleOrderSearchBean>();
		String orderStatus = "60";
		try {
			ordersList = orderService.searchOrders(userId, serviceType, "", "", keyword, orderStatus, pageSize,
					pageNumber);
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

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/order/{orderId}/draft/{draftId}/re-inspection", method = RequestMethod.POST)
	public ResponseEntity<ApiCallResult> reInspection(@PathVariable("userId") String userId,
			@PathVariable("orderId") String orderId, @PathVariable("draftId") String draftId) {
		logger.info("invoke: " + "/user/" + userId + "/order/" + orderId + "/draft/" + draftId + "/re-inspection");
		ApiCallResult result = orderService.reInspection(userId, orderId, draftId);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/order/{orderId}/files", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> getFilesByOrderID(@PathVariable("userId") String userId,
			@PathVariable("orderId") String orderId) throws IOException {
		// TODO Auto-generated method stub
		logger.info("invoke: " + "/user/" + userId + "/order/" + orderId + "/files");
		ApiCallResult result = new ApiCallResult();
		Map<String, String> prodMap = new HashMap<String, String>();
		List<ProductBean> products = orderService.listProducts(orderId);
		if (null != products) {
			JSONArray jArray = (JSONArray) JSON.parseArray(JSON.toJSONString(products));
			StringBuilder srcIds = new StringBuilder();
			for (int i = 0; i < jArray.size(); i++) {
				JSONObject each = (JSONObject) jArray.get(i);
				srcIds.append(each.getString("productId"));
				prodMap.put(each.getString("productId"), each.getString("prodName"));
				if (i != jArray.size() - 1) {
					srcIds.append(";");
				}
			}
			Map<String, List<FileMetaBean>> content = myFileService.getFileService()
					.getFileInfoBySrcIds(srcIds.toString());
			if (null != content) {
				JSONObject jsonObj = JSON.parseObject(JSON.toJSONString(content));
				for (Map.Entry<String, String> entry : prodMap.entrySet()) {
					logger.info(entry.getKey() + "--->" + entry.getValue());
					JSONArray fileArray = jsonObj.getJSONArray(entry.getKey());
					JSONArray tempArray = new JSONArray();
					if (null != fileArray) {
						for (int j = 0; j < fileArray.size(); j++) {
							JSONObject each = (JSONObject) fileArray.get(j);
							if ("ORDER_ATT".equals(each.getString("fileType"))) {
								tempArray.add(each);
								each.put("prodName", entry.getValue());
							}
						}
					}
					jsonObj.replace(entry.getKey(), tempArray);
				}
				result.setContent(jsonObj);
				return new ResponseEntity<>(result, HttpStatus.OK);
			} else {
				result.setContent(null);
				return new ResponseEntity<>(result, HttpStatus.OK);
			}
		} else {
			result.setMessage("No products in this order: " + orderId);
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
	}

}
