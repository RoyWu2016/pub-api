package com.ai.api.controller.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import com.ai.api.controller.Inspection;
import com.ai.api.service.DraftService;
import com.ai.api.service.InspectionService;
import com.ai.api.service.OrderService;
import com.ai.commons.Consts;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.PageBean;
import com.ai.commons.beans.order.SimpleOrderSearchBean;
import com.ai.commons.beans.order.draft.DraftOrder;
import com.ai.commons.beans.psi.api.ApiInspectionBookingBean;
import com.ai.commons.beans.psi.api.ApiInspectionOrderBean;
import com.ai.commons.beans.psi.api.ApiOrderPriceMandayViewBean;
import com.ai.consts.ConstMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/***************************************************************************
 * <PRE>
 * Project Name    : api
 * <p>
 * Package Name    : com.ai.api.controller.impl
 * <p>
 * File Name       : InspectionImpl.java
 * <p>
 * Creation Date   : Jan 11, 2017
 * <p>
 * Author          : Roy Wu
 * <p>
 * Purpose         : TODO
 * <p>
 * <p>
 * History         : TODO
 * <p>
 * </PRE>
 ***************************************************************************/
@RestController
@Api(tags = {"Inspection"}, description = "Inspection booking APIs")
@SuppressWarnings("rawtypes")
public class InspectionImpl implements Inspection {

	protected Logger logger = LoggerFactory.getLogger(InspectionImpl.class);

	@Autowired
	private InspectionService inspectionService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private DraftService draftService;

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/inspection-draft", method = RequestMethod.POST)
	@ApiOperation(value = "Create Inspection Draft API", response = ApiInspectionBookingBean.class)
	public ResponseEntity<ApiCallResult> createDraft(
			@ApiParam(required = true)
			@PathVariable("userId") String userId,
			@ApiParam(value = "must be one of psi, clc, pm, dupro, ipc", required = true)
			@RequestParam(value = "serviceType") String serviceType) {
		logger.info("invoke: " + "/user/" + userId + "/inspection-draft?serviceType=" + serviceType);
		ApiCallResult result = inspectionService.createDraft(userId, serviceType);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@ApiOperation(value = "Create Inspection Draft From Previous Order API", response = ApiInspectionBookingBean.class)
	@RequestMapping(value = "/user/{userId}/inspection-draft/previous-order/{orderId}", method = RequestMethod.POST)
	public ResponseEntity<ApiCallResult> createDraftFromPreviousOrder(
			@ApiParam(required = true)
			@PathVariable("userId") String userId,
			@ApiParam(value = "previous order id", required = true)
			@PathVariable("orderId") String orderId,
			@ApiParam(value = "must be one of psi, clc, pm, dupro, ipc", required = true)
			@RequestParam("serviceType") String serviceType) {
		logger.info("invoke: " + "/user/" + userId + "/inspection-draft/previous-order/" + orderId + "?serviceType="
				+ serviceType);
		ApiCallResult result = inspectionService.createDraftFromPreviousOrder(userId, orderId, serviceType);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@ApiOperation(value = "Get Inspection Draft By ID API", response = ApiInspectionBookingBean.class)
	@RequestMapping(value = "/user/{userId}/inspection-draft/{draftId}", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> getDraft(
			@ApiParam(required = true)
			@PathVariable("userId") String userId,
			@ApiParam(required = true)
			@PathVariable("draftId") String draftId) {
		logger.info("invoke: " + "/user/" + userId + "/inspection-draft/" + draftId);
		ApiCallResult result = inspectionService.getDraft(userId, draftId);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@ApiOperation(value = "Save Draft By Draft Draft ID API", response = ApiInspectionBookingBean.class)
	@RequestMapping(value = "/user/{userId}/inspection-draft/{draftId}", method = RequestMethod.POST)
	public ResponseEntity<ApiCallResult> saveDraft(
			@ApiParam(required = true)
			@PathVariable("userId") String userId,
			@ApiParam(required = true)
			@PathVariable("draftId") String draftId,
			@ApiParam(required = true)
			@RequestBody ApiInspectionBookingBean draft) {
		logger.info("invoke: " + "/user/" + userId + "/inspection-draft/" + draftId);
		draft.getDraft().setDraftId(draftId);
		ApiCallResult result = inspectionService.saveDraft(userId, draft);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@ApiOperation(value = "Create Inspection Order By Draft ID API", response = ApiInspectionBookingBean.class)
	@RequestMapping(value = "/user/{userId}/inspection-order", method = RequestMethod.POST)
	public ResponseEntity<ApiCallResult> createOrderByDraft(
			@ApiParam(required = true)
			@PathVariable("userId") String userId,
			@ApiParam(required = true)
			@RequestParam("draftId") String draftId) {
		logger.info("invoke: " + "/user/" + userId + "/inspection-order?draftId=" + draftId);
		ApiCallResult result = inspectionService.createOrderByDraft(userId, draftId);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@ApiOperation(value = "Start Edit An Order By Order ID API", response = ApiInspectionBookingBean.class)
	@RequestMapping(value = "/user/{userId}/inspection-order/{orderId}/editing", method = RequestMethod.PUT)
	public ResponseEntity<ApiCallResult> editOrder(
			@ApiParam(required = true)
			@PathVariable("userId") String userId,
			@ApiParam(required = true)
			@PathVariable("orderId") String orderId) {
		logger.info("invoke: " + "/user/" + userId + "/inspection-order/" + orderId + "/editing");
		ApiCallResult result = inspectionService.editOrder(userId, orderId);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@ApiOperation(value = "Get Inspection Order By Order ID API", response = ApiInspectionOrderBean.class)
	@RequestMapping(value = "/user/{userId}/inspection-order/{orderId}", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> getOrderDetail(
			@ApiParam(required = true)
			@PathVariable("userId") String userId,
			@ApiParam(required = true)
			@PathVariable("orderId") String orderId) {
		logger.info("invoke: " + "/user/" + userId + "/inspection-order/" + orderId);
		ApiCallResult result = inspectionService.getOrderDetail(userId, orderId);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@ApiOperation(value = "Save Inspection Order By Draft ID API", response = ApiInspectionBookingBean.class)
	@RequestMapping(value = "/user/{userId}/inspection-order/{orderId}/inspection-draft/{draftId}/saved", method = RequestMethod.PUT)
	public ResponseEntity<ApiCallResult> saveOrderByDraft(
			@ApiParam(required = true)
			@PathVariable("userId") String userId,
			@ApiParam(required = true)
			@PathVariable("draftId") String draftId,
			@ApiParam(required = true)
			@PathVariable("orderId") String orderId) {
		logger.info("invoke: " + "/user/" + userId + "/inspection-order/" + orderId + "/inspection-draft/" + draftId
				+ "/saved");
		ApiCallResult result = inspectionService.saveOrderByDraft(userId, draftId, orderId);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@ApiOperation(value = "Calculate Inspection Draft Price API", response = ApiOrderPriceMandayViewBean.class)
	@RequestMapping(value = "/user/{userId}/inspection-draft/{draftId}/sampling-level/{samplingLevel}/price",
			method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> calculatePricing(
			@ApiParam(required = true)
			@PathVariable("userId") String userId,
			@ApiParam(required = true)
			@PathVariable("draftId") String draftId,
			@ApiParam(required = true)
			@PathVariable("samplingLevel") String samplingLevel,
			@ApiParam(value="must be one of 100%, III, II, I, S4, S3, S2, S1", required = false)
			@RequestParam(value = "measurementSamplingSize", defaultValue = "II") String measurementSamplingSize) {
		ApiCallResult result = inspectionService.calculatePricing(userId, draftId, samplingLevel, measurementSamplingSize);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@ApiOperation(value = "Search Inspection Order API", response = PageBean.class)
	@RequestMapping(value = "/user/{userId}/inspection-orders", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> searchOrders(
			@ApiParam(required = true)
			@PathVariable("userId") String userId,
			@ApiParam(value = "must be one of psi, clc, pm, dupro, ipc or comma delimited", required = true)
			@RequestParam(value = "service-type", defaultValue = "psi,ipc,dupro,clc,pm") String serviceType,
			@ApiParam(value = "must be in format like 2016-12-01", required = false)
			@RequestParam(value = "start", defaultValue = "") String startDate,
			@ApiParam(value = "must be in format like 2016-12-01", required = false)
			@RequestParam(value = "end", defaultValue = "") String endDate,
			@ApiParam(required = false)
			@RequestParam(value = "keyword", defaultValue = "") String keyword,
			@ApiParam(value = "must be a single status value or comma delimited <br />for open orders: 15,17,20,22,23,25,30,40,50 <br />for completed orders:60", required = false)
			@RequestParam(value = "status", defaultValue = "") String orderStatus,
			@RequestParam(value = "pageSize", required = false, defaultValue = "20") String pageSize,
			@RequestParam(value = "pageNumber", required = false, defaultValue = "1") String pageNumber) {

		ApiCallResult result = new ApiCallResult();
		try {
			try {
				if (!serviceType.isEmpty()) {
					serviceType = URLDecoder.decode(serviceType, "utf-8");
					serviceType = ConstMap.convertServiceType(serviceType, Consts.COMMA);
				}
			} catch (UnsupportedEncodingException e) {
				logger.error("decoding service type: " + serviceType + "got error!");
			}
			List<SimpleOrderSearchBean> ordersList = orderService.searchOrders(userId, serviceType, startDate, endDate, keyword, orderStatus,
					pageSize, pageNumber);
			// if not data found, just return 200 with empty list
			result.setContent(ordersList);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("get orders search error: " + ExceptionUtils.getFullStackTrace(e));
			result.setMessage("get orders search error: " + ExceptionUtils.getFullStackTrace(e));
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@Override
	@TokenSecured
	@ApiOperation(value = "Search Inspection Draft API", response = PageBean.class)
	@RequestMapping(value = "/user/{userId}/inspection-drafts", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> searchDraft(
			@ApiParam(required = true)
			@PathVariable("userId") String userId,
			@ApiParam(value = "must be one of psi, clc, pm, dupro, ipc or comma delimited", required = true)
			@RequestParam(value = "service-type", defaultValue = "psi,ipc,dupro,clc,pm") String serviceType,
			@ApiParam(value = "must be in format like 2016-12-01", required = false)
			@RequestParam(value = "start date", defaultValue = "") String startDate,
			@ApiParam(value = "must be in format like 2016-12-01", required = false)
			@RequestParam(value = "end date", defaultValue = "") String endDate,
			@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
			@RequestParam(value = "pageNumber", required = false, defaultValue = "1") String pageNumber,
			@RequestParam(value = "pageSize", required = false, defaultValue = "20") String pageSize) {

		ApiCallResult result = new ApiCallResult();
		try {
			try {
				if (!serviceType.isEmpty()) {
					serviceType = URLDecoder.decode(serviceType, "utf-8");
					serviceType = ConstMap.convertServiceType(serviceType, Consts.COMMA);
				}
			} catch (UnsupportedEncodingException e) {
				logger.error("decoding service type: " + serviceType + "got error!");
			}
			List<DraftOrder> draftList = draftService.searchDraft(userId, serviceType, startDate, endDate, keyword, pageNumber, pageSize);
			//if not data found, just return 200 with empty list
			result.setContent(draftList);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("get draft search error: " + ExceptionUtils.getFullStackTrace(e));
			result.setMessage("get orders search error: " + ExceptionUtils.getFullStackTrace(e));
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@Override
	@TokenSecured
	@ApiOperation(value = "Delete Inspection Drafts By Draft IDs API", response = Boolean.class)
	@RequestMapping(value = "/user/{userId}/inspection-drafts", method = RequestMethod.DELETE)
	public ResponseEntity<ApiCallResult> deleteDrafts(
			@ApiParam(required = true)
			@PathVariable("userId") String userId,
			@ApiParam(value = "must be comma delimited values", required = true)
			@RequestParam("draftIds") String draftIds) {
		ApiCallResult result = new ApiCallResult();
		try {
			logger.info("deleteDrafts. . .");
			boolean b = draftService.deleteDraftFromPsi(userId, draftIds);
			result.setContent(b);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("delete draft error: " + ExceptionUtils.getFullStackTrace(e));
			result.setMessage("get orders search error: " + ExceptionUtils.getFullStackTrace(e));
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@ApiOperation(value = "Update Inspection Drafts By Re-inspection Order ID API", response = ApiInspectionBookingBean.class)
	@RequestMapping(value = "/user/{userId}/order/{orderId}/inspection-draft/{draftId}/re-inspection", method = RequestMethod.POST)
	public ResponseEntity<ApiCallResult> reInspection(
			@ApiParam(required = true)
			@PathVariable("userId") String userId,
			@ApiParam(required = true)
			@PathVariable("orderId") String orderId,
			@ApiParam(required = true)
			@PathVariable("draftId") String draftId) {
		logger.info("invoke: " + "/user/" + userId + "/order/" + orderId + "/draft/" + draftId + "/re-inspection");
		ApiCallResult result = orderService.reInspection(userId, orderId, draftId);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
