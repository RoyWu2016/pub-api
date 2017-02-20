package com.ai.api.controller.impl;

import com.ai.api.service.DraftService;
import com.ai.api.service.OrderService;
import com.ai.commons.beans.order.SimpleOrderSearchBean;
import com.ai.commons.beans.order.draft.DraftOrder;
import com.ai.commons.beans.psi.api.ApiInspectionBookingBean;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ai.api.controller.Inspection;
import com.ai.api.service.InspectionService;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.ApiCallResult;

import java.util.ArrayList;
import java.util.List;

/***************************************************************************
 * <PRE>
 *  Project Name    : api
 *
 *  Package Name    : com.ai.api.controller.impl
 *
 *  File Name       : InspectionImpl.java
 *
 *  Creation Date   : Jan 11, 2017
 *
 *  Author          : Roy Wu
 *
 *  Purpose         : TODO
 *
 *
 *  History         : TODO
 *
 * </PRE>
 ***************************************************************************/
@RestController
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
	public ResponseEntity<ApiCallResult> createDraft(@PathVariable("userId") String userId,
			@RequestParam(value = "serviceType", required = true) String serviceType) {
		// TODO Auto-generated method stub
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
	@RequestMapping(value = "/user/{userId}/inspection-draft/previous-order/{orderId}", method = RequestMethod.POST)
	public ResponseEntity<ApiCallResult> createDraftFromPreviousOrder(@PathVariable("userId") String userId,
			@PathVariable("orderId") String orderId, @RequestParam("serviceType") String serviceType) {
		// TODO Auto-generated method stub
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
	@RequestMapping(value = "/user/{userId}/inspection-draft/{draftId}", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> getDraft(@PathVariable("userId") String userId,
			@PathVariable("draftId") String draftId) {
		// TODO Auto-generated method stub
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
	@RequestMapping(value = "/user/{userId}/inspection-draft/{draftId}", method = RequestMethod.POST)
	public ResponseEntity<ApiCallResult> saveDraft(@PathVariable("userId") String userId,
												   @PathVariable("draftId") String draftId,
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
	@RequestMapping(value = "/user/{userId}/inspection-order", method = RequestMethod.POST)
	public ResponseEntity<ApiCallResult> createOrderByDraft(@PathVariable("userId") String userId,
			@RequestParam("draftId") String draftId) {
		// TODO Auto-generated method stub
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
	@RequestMapping(value = "/user/{userId}/inspection-order/{orderId}/editing", method = RequestMethod.PUT)
	public ResponseEntity<ApiCallResult> editOrder(@PathVariable("userId") String userId,
			@PathVariable("orderId") String orderId) {
		// TODO Auto-generated method stub
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
	@RequestMapping(value = "/user/{userId}/inspection-order/{orderId}", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> getOrderDetail(@PathVariable("userId") String userId,
			@PathVariable("orderId") String orderId) {
		// TODO Auto-generated method stub
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
	@RequestMapping(value = "/user/{userId}/inspection-order/{orderId}/inspection-draft/{draftId}/saved", method = RequestMethod.PUT)
	public ResponseEntity<ApiCallResult> saveOrderByDraft(@PathVariable("userId") String userId,
			@PathVariable("draftId") String draftId, @PathVariable("orderId") String orderId) {
		// TODO Auto-generated method stub
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
	@RequestMapping(value = "/user/{userId}/inspection-draft/{draftId}/sampling-level/{samplingLevel}/price",
			method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> calculatePricing(
			@PathVariable("userId") String userId,
			@PathVariable("draftId") String draftId,
			@PathVariable("samplingLevel") String samplingLevel,
			@RequestParam(value = "measurementSamplingSize", required = false, defaultValue = "") String measurementSamplingSize) {
		ApiCallResult result = inspectionService.calculatePricing(userId, draftId, samplingLevel, measurementSamplingSize);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/inspection-orders", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> searchOrders(@PathVariable("userId") String userId,
																	@RequestParam(value = "service-type", required = false, defaultValue = "") String serviceType,
																	@RequestParam(value = "start", required = false, defaultValue = "") String startDate,
																	@RequestParam(value = "end", required = false, defaultValue = "") String endDate,
																	@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
																	@RequestParam(value = "status", required = false, defaultValue = "") String orderStatus,
																	@RequestParam(value = "page-size", required = false, defaultValue = "20") String pageSize,
																	@RequestParam(value = "page", required = false, defaultValue = "1") String pageNumber) {

		ApiCallResult result = new ApiCallResult();
		try {
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
    @RequestMapping(value = "/user/{userId}/inspection-drafts", method = RequestMethod.GET)
    public ResponseEntity<ApiCallResult> searchDraft(@PathVariable("userId") String userId,
                                                        @RequestParam(value = "service-type", required = false, defaultValue = "") String serviceType,
                                                        @RequestParam(value = "start", required = false, defaultValue = "") String startDate,
                                                        @RequestParam(value = "end", required = false, defaultValue = "") String endDate,
                                                        @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                                        @RequestParam(value = "page", required = false, defaultValue = "1") String pageNumber,
                                                        @RequestParam(value = "page-size", required = false, defaultValue = "20") String pageSize) {

        ApiCallResult result = new ApiCallResult();
        try {
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
    @RequestMapping(value = "/user/{userId}/inspection-drafts", method = RequestMethod.DELETE)
    public ResponseEntity<ApiCallResult> deleteDrafts(@PathVariable("userId") String userId, @RequestParam("draftIds") String draftIds) {
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

}
