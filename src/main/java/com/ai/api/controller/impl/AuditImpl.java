package com.ai.api.controller.impl;

import com.ai.api.controller.Audit;
import com.ai.api.service.AuditService;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.audit.api.ApiAuditBookingBean;
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
 *  Project Name    : api
 *
 *  Package Name    : com.ai.api.controller.impl
 *
 *  File Name       : AuditorImpl.java
 *
 *  Creation Date   : Feb 10, 2017
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
public class AuditImpl implements Audit {

	protected Logger logger = LoggerFactory.getLogger(AuditImpl.class);

	@Autowired
	private AuditService auditorService;

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/audit-draft", method = RequestMethod.POST)
	public ResponseEntity<ApiCallResult> createDraft(@PathVariable("userId") String userId,
			@RequestParam(value = "serviceType", required = true) String serviceType) {
		// TODO Auto-generated method stub
		logger.info("invoke: " + "/user/" + userId + "/audit-draft?serviceType=" + serviceType);
		ApiCallResult result = auditorService.createDraft(userId, serviceType);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/audit-draft/previous-order/{orderId}", method = RequestMethod.POST)
	public ResponseEntity<ApiCallResult> createDraftFromPreviousOrder(@PathVariable("userId") String userId,
			@PathVariable("orderId") String orderId, @RequestParam("serviceType") String serviceType) {
		// TODO Auto-generated method stub
		logger.info("invoke: " + "/user/" + userId + "/audit-draft/previous-psi-order/" + orderId + "?serviceType="
				+ serviceType);
		ApiCallResult result = auditorService.createDraftFromPreviousOrder(userId, orderId, serviceType);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/audit-draft/{draftId}", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> getDraft(@PathVariable("userId") String userId,
			@PathVariable("draftId") String draftId) {
		// TODO Auto-generated method stub
		logger.info("invoke: " + "/user/" + userId + "/audit-draft/" + draftId);
		ApiCallResult result = auditorService.getDraft(userId, draftId);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/audit-draft/{draftId}", method = RequestMethod.PUT)
	public ResponseEntity<ApiCallResult> saveDraft(@PathVariable("userId") String userId,
			@PathVariable("draftId") String draftId, @RequestBody ApiAuditBookingBean draft) {
		// TODO Auto-generated method stub
		logger.info("invoke: " + "/user/" + userId + "/audit-draft/" + draftId);
		draft.getDraft().setDraftId(draftId);
		ApiCallResult result = auditorService.saveDraft(userId, draft);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/audit-drafts", method = RequestMethod.DELETE)
	public ResponseEntity<ApiCallResult> deleteDrafts(@PathVariable("userId") String userId,
			@RequestParam("draftIds") String draftIds) {
		logger.info("invoke: " + "/user/" + userId + "/audit-drafts?draftIds=" + draftIds);
		ApiCallResult result = auditorService.deleteDrafts(userId, draftIds);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/audit-drafts", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> searchDrafts(@PathVariable("userId") String userId,
			@RequestParam(value = "service-type", required = false, defaultValue = "") String serviceType,
			@RequestParam(value = "start", required = false, defaultValue = "") String startDate,
			@RequestParam(value = "end", required = false, defaultValue = "") String endDate,
			@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
			@RequestParam(value = "page-size", required = false, defaultValue = "20") int pageSize,
			@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber) {
		logger.info("invoke: " + "/user/" + userId + "/audit-drafts?serviceType=" + serviceType + "&startDate="
				+ startDate + "&endDate=" + endDate + "&keyword=" + keyword + "&pageSize=" + pageSize + "&pageNumber="
				+ pageNumber);
		ApiCallResult result = auditorService.searchDrafts(userId, serviceType, startDate, endDate, keyword, pageSize,
				pageNumber);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/audit-orders", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> searchOrders(@PathVariable("userId") String userId,
			@RequestParam(value = "service-type", required = false, defaultValue = "") String serviceType,
			@RequestParam(value = "start", required = false, defaultValue = "") String startDate,
			@RequestParam(value = "end", required = false, defaultValue = "") String endDate,
			@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
			@RequestParam(value = "status", required = false, defaultValue = "") String orderStatus,
			@RequestParam(value = "page-size", required = false, defaultValue = "20") int pageSize,
			@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber) {
		logger.info("invoke: " + "/user/" + userId + "/audit-orders?serviceType=" + serviceType + "&startDate="
				+ startDate + "&endDate=" + endDate + "&keyword=" + keyword + "&pageSize=" + pageSize + "&pageNumber="
				+ pageNumber);
		ApiCallResult result = auditorService.searchOrders(userId, serviceType, startDate, endDate, orderStatus,
				keyword, pageSize, pageNumber);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/audit-order", method = RequestMethod.POST)
	public ResponseEntity<ApiCallResult> createOrderByDraft(@PathVariable("userId") String userId,
			@RequestParam("draftId") String draftId) {
		// TODO Auto-generated method stub
		logger.info("invoke: " + "/user/" + userId + "/audit-order?draftId=" + draftId);
		ApiCallResult result = auditorService.createOrderByDraft(userId, draftId);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/audit-order/{orderId}/editing", method = RequestMethod.PUT)
	public ResponseEntity<ApiCallResult> editOrder(@PathVariable("userId") String userId,
			@PathVariable("orderId") String orderId) {
		// TODO Auto-generated method stub
		logger.info("invoke: " + "/user/" + userId + "/audit-order/" + orderId + "/editing");
		ApiCallResult result = auditorService.editOrder(userId, orderId);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/audit-order/{orderId}", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> getOrderDetail(@PathVariable("userId") String userId,
			@PathVariable("orderId") String orderId) {
		// TODO Auto-generated method stub
		logger.info("invoke: " + "/user/" + userId + "/audit-order/" + orderId);
		ApiCallResult result = auditorService.getOrderDetail(userId, orderId);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/audit-order/{orderId}/audit-draft/{draftId}/saved", method = RequestMethod.PUT)
	public ResponseEntity<ApiCallResult> saveOrderByDraft(@PathVariable("userId") String userId,
			@PathVariable("draftId") String draftId, @PathVariable("orderId") String orderId) {
		// TODO Auto-generated method stub
		logger.info("invoke: " + "/user/" + userId + "/audit-order/" + orderId + "/audit-draft/" + draftId + "/saved");
		ApiCallResult result = auditorService.saveOrderByDraft(userId, draftId, orderId);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/audit-draft/{draftId}/price", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> calculatePricing(@PathVariable("userId") String userId,
			@PathVariable("draftId") String draftId,
			@RequestParam(value = "employeeCount", required = false) String employeeCount) {
		// TODO Auto-generated method stub
		logger.info(
				"invoke: " + "/user/" + userId + "/audit-draft/" + draftId + "/price?employee-count=" + employeeCount);
		ApiCallResult result = auditorService.calculatePricing(userId, draftId, employeeCount);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/audit-order/{orderId}/draft/{draftId}/re-audit", method = RequestMethod.POST)
	public ResponseEntity<ApiCallResult> reAudit(@PathVariable("userId") String userId,
			@PathVariable("orderId") String orderId, @PathVariable("draftId") String draftId) {
		// TODO Auto-generated method stub
		logger.info("invoke: " + "/user/" + userId + "/audit-order/" + orderId + "/draftId/" + draftId + "/re-audit");
		ApiCallResult result = auditorService.reAudit(userId, draftId, orderId);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/audit-order/{orderId}", method = RequestMethod.DELETE)
	public ResponseEntity<ApiCallResult> cancelOrder(@PathVariable("userId") String userId,
			@PathVariable("orderId") String orderId,
			@RequestParam(value = "reason", required = false, defaultValue = "") String reason,
			@RequestParam(value = "reasonOption", required = false, defaultValue = "") String reasonOption) {
		// TODO Auto-generated method stub
		logger.info("invoke: " + "/user/" + userId + "/audit-order/" + orderId);
		ApiCallResult result = auditorService.cancelOrder(userId, orderId,reason,reasonOption);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
