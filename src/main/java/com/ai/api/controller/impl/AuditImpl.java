package com.ai.api.controller.impl;

import java.util.ArrayList;
import java.util.List;

import com.ai.api.bean.consts.ConstMap;
import com.ai.api.controller.Audit;
import com.ai.api.service.AuditService;
import com.ai.api.service.OrderService;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.audit.AuditBookingBean;
import com.ai.commons.beans.audit.api.ApiAuditBookingBean;
import com.ai.commons.beans.audit.api.ApiAuditOrderBean;
import com.ai.commons.beans.order.SimpleAuditSearchBean;
import com.ai.commons.beans.order.SimpleDraftSearchBean;
import com.ai.commons.beans.order.SimpleOrderSearchBean;
import com.ai.commons.beans.psi.api.ApiOrderPriceMandayViewBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
 * File Name       : AuditorImpl.java
 * <p>
 * Creation Date   : Feb 10, 2017
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
@SuppressWarnings("rawtypes")
@Api(tags = {"Audit"}, description = "Audit booking APIs")
public class AuditImpl implements Audit {

	protected Logger logger = LoggerFactory.getLogger(AuditImpl.class);

	@Autowired
	private AuditService auditorService;

	@Autowired
	private OrderService orderService;

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/audit-draft", method = RequestMethod.POST)
	@ApiOperation(value = "Create Draft API", response = AuditBookingBean.class)
	public ResponseEntity<ApiCallResult> createDraft(
			@ApiParam(required = true)
			@PathVariable("userId") String userId,
			@ApiParam(value = "must be one of ma, ea, stra, ctpat", required = true)
			@RequestParam(value = "serviceType") String serviceType) {
		logger.info("invoke: " + "/user/" + userId + "/audit-draft?serviceType=" + serviceType);
		ApiCallResult result = auditorService.createDraft(userId, serviceType);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			logger.error("fail from psi-service!"+result.getMessage());
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/audit-draft/previous-order/{orderId}", method = RequestMethod.POST)
	@ApiOperation(value = "Create Draft From Previous Order API", response = AuditBookingBean.class)
	public ResponseEntity<ApiCallResult> createDraftFromPreviousOrder(
			@ApiParam(value = "userId", required = true)
			@PathVariable("userId") String userId,
			@ApiParam(value = "orderId", required = true)
			@PathVariable("orderId") String orderId,
			@ApiParam(value = "serviceType", required = true)
			@RequestParam("serviceType") String serviceType) {
		logger.info("invoke: " + "/user/" + userId + "/audit-draft/previous-psi-order/" + orderId + "?serviceType="
				+ serviceType);
		ApiCallResult result = auditorService.createDraftFromPreviousOrder(userId, orderId, serviceType);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			logger.error("fail from psi-service!"+result.getMessage());
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/audit-draft/{draftId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Draft API", response = AuditBookingBean.class)
	public ResponseEntity<ApiCallResult> getDraft(
			@ApiParam(value = "userId", required = true)
			@PathVariable("userId") String userId,
			@ApiParam(value = "draftId", required = true)
			@PathVariable("draftId") String draftId) {
		// TODO Auto-generated method stub
		logger.info("invoke: " + "/user/" + userId + "/audit-draft/" + draftId);
		ApiCallResult result = auditorService.getDraft(userId, draftId);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			logger.error("fail from psi-service!"+result.getMessage());
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/audit-draft/{draftId}", method = RequestMethod.PUT)
	@ApiOperation(value = "Save Draft API", response = boolean.class)
	public ResponseEntity<ApiCallResult> saveDraft(
			@ApiParam(value = "userId", required = true)
			@PathVariable("userId") String userId,
			@ApiParam(value = "draftId", required = true)
			@PathVariable("draftId") String draftId,
			@RequestBody ApiAuditBookingBean draft) {
		// TODO Auto-generated method stub
		logger.info("invoke: " + "/user/" + userId + "/audit-draft/" + draftId);
		draft.getDraft().setDraftId(draftId);
		ApiCallResult result = auditorService.saveDraft(userId, draft);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			logger.error("fail from psi-service!"+result.getMessage());
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/audit-drafts", method = RequestMethod.DELETE)
	@ApiOperation(value = "Delete Draft API", response = boolean.class)
	public ResponseEntity<ApiCallResult> deleteDrafts(
			@ApiParam(value = "userId", required = true)
			@PathVariable("userId") String userId,
			@ApiParam(value = "if multiple, separated by comma", required = true)
			@RequestParam("draftIds") String draftIds) {
		logger.info("invoke: " + "/user/" + userId + "/audit-drafts?draftIds=" + draftIds);
		ApiCallResult result = auditorService.deleteDrafts(userId, draftIds);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			logger.error("fail from psi-service!"+result.getMessage());
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/audit-drafts", method = RequestMethod.GET)
	@ApiOperation(value = "Search Drafts API", response = SimpleDraftSearchBean.class, responseContainer = "List")
	public ResponseEntity<ApiCallResult> searchDrafts(
			@ApiParam(value = "userId", required = true)
			@PathVariable("userId") String userId,
			@ApiParam(value = "should be one or more value in [ma,ea,ctpat,stra]", required = false)
			@RequestParam(value = "service-type", required = false, defaultValue = "ma,ea,ctpat,stra") String serviceType,
			@RequestParam(value = "start", required = false, defaultValue = "") String startDate,
			@RequestParam(value = "end", required = false, defaultValue = "") String endDate,
			@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
			@RequestParam(value = "page-size", required = false, defaultValue = "20") int pageSize,
			@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber) {

		ApiCallResult result = auditorService.searchDrafts(userId, serviceType, startDate, endDate, keyword, pageSize,
				pageNumber);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			logger.error("fail from psi-service!"+result.getMessage());
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/audit-orders", method = RequestMethod.GET)
	@ApiOperation(value = "Search Orders API", response = SimpleAuditSearchBean.class, responseContainer = "List")
	public ResponseEntity<ApiCallResult> searchOrders(
			@ApiParam(value = "userId", required = true)
			@PathVariable("userId") String userId,
			@ApiParam(value = "should be one or more value in [ma,ea,ctpat,stra]", required = false)
			@RequestParam(value = "service-type", required = false, defaultValue = "ma,ea,ctpat,stra") String serviceType,
			@RequestParam(value = "start", required = false, defaultValue = "") String startDate,
			@RequestParam(value = "end", required = false, defaultValue = "") String endDate,
			@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
			@ApiParam(value="status must be within: 15,17,20,22,23,25,30,40,50,60 and separated by comma")@RequestParam(value = "status", required = false, defaultValue = "") String orderStatus,
			@RequestParam(value = "page-size", required = false, defaultValue = "20") int pageSize,
			@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber) {
		if("".equals(orderStatus)){
			orderStatus = "15,17,20,22,23,25,30,40,50,60";
		}else {
			List<String> status = new ArrayList<String>();
			String[] str = orderStatus.split(",");
			for(int i=0;i<str.length;i++) {
				if(ConstMap.STATUS.contains(str[i])) {
					status.add(str[i]);
				}
			}
			if(status.size() <= 0) {
				ApiCallResult temp = new ApiCallResult();
				temp.setMessage("status must be within: 15,17,20,22,23,25,30,40,50,60");
				return new ResponseEntity<>(temp, HttpStatus.INTERNAL_SERVER_ERROR);
			}else {
				orderStatus = String.join(",", status);
			}
		}
		ApiCallResult result = auditorService.searchOrders(userId, serviceType, startDate, endDate, orderStatus,
				keyword, pageSize, pageNumber);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			logger.error("fail from psi-service!"+result.getMessage());
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/audit-order", method = RequestMethod.POST)
	@ApiOperation(value = "Create Order By Draft API", response = ApiAuditBookingBean.class)
	public ResponseEntity<ApiCallResult> createOrderByDraft(
			@ApiParam(value = "userId", required = true)
			@PathVariable("userId") String userId,
			@ApiParam(value = "draftId", required = true)
			@RequestParam("draftId") String draftId) {
		// TODO Auto-generated method stub
		logger.info("invoke: " + "/user/" + userId + "/audit-order?draftId=" + draftId);
		ApiCallResult result = auditorService.createOrderByDraft(userId, draftId);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			logger.error("fail from psi-service!"+result.getMessage());
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/audit-order/{orderId}/editing", method = RequestMethod.PUT)
	@ApiOperation(value = "Edit Order API", response = ApiAuditBookingBean.class)
	public ResponseEntity<ApiCallResult> editOrder(
			@ApiParam(value = "userId", required = true)
			@PathVariable("userId") String userId,
			@ApiParam(value = "orderId", required = true)
			@PathVariable("orderId") String orderId) {
		// TODO Auto-generated method stub
		logger.info("invoke: " + "/user/" + userId + "/audit-order/" + orderId + "/editing");
		ApiCallResult result = auditorService.editOrder(userId, orderId);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			logger.error("fail from psi-service!"+result.getMessage());
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/audit-order/{orderId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Order Detail API", response = ApiAuditOrderBean.class)
	public ResponseEntity<ApiCallResult> getOrderDetail(
			@ApiParam(value = "userId", required = true)
			@PathVariable("userId") String userId,
			@ApiParam(value = "orderId", required = true)
			@PathVariable("orderId") String orderId) {
		// TODO Auto-generated method stub
		logger.info("invoke: " + "/user/" + userId + "/audit-order/" + orderId);
		ApiCallResult result = auditorService.getOrderDetail(userId, orderId);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			logger.error("fail from psi-service!"+result.getMessage());
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/audit-order/{orderId}/audit-draft/{draftId}/saved", method = RequestMethod.PUT)
	@ApiOperation(value = "Save Order By Draft API", response = ApiAuditBookingBean.class)
	public ResponseEntity<ApiCallResult> saveOrderByDraft(
			@ApiParam(value = "userId", required = true)
			@PathVariable("userId") String userId,
			@ApiParam(value = "draftId", required = true)
			@PathVariable("draftId") String draftId,
			@ApiParam(value = "orderId", required = true)
			@PathVariable("orderId") String orderId) {
		logger.info("invoke: " + "/user/" + userId + "/audit-order/" + orderId + "/audit-draft/" + draftId + "/saved");
		ApiCallResult result = auditorService.saveOrderByDraft(userId, draftId, orderId);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			logger.error("fail from psi-service!"+result.getMessage());
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/audit-draft/{draftId}/price", method = RequestMethod.GET)
	@ApiOperation(value = "Calculate Pricing API", response = ApiOrderPriceMandayViewBean.class)
	public ResponseEntity<ApiCallResult> calculatePricing(
			@ApiParam(value = "userId", required = true)
			@PathVariable("userId") String userId,
			@ApiParam(value = "draftId", required = true)
			@PathVariable("draftId") String draftId,
			@ApiParam(value = "worker number of the factory. for StrA it can be any number, won't get calculated.<br />", required = true)
			@RequestParam(value = "employeeCount", required = true, defaultValue = "500") int employeeCount) {
		logger.info(
				"invoke: " + "/user/" + userId + "/audit-draft/" + draftId + "/price?sample-size=" + employeeCount);
		ApiCallResult result = auditorService.calculatePricing(userId, draftId, employeeCount);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			logger.error("fail from psi-service!"+result.getMessage());
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/audit-order/{orderId}/draft/{draftId}/re-audit", method = RequestMethod.POST)
	@ApiOperation(value = "ReAudit API", response = ApiAuditBookingBean.class)
	public ResponseEntity<ApiCallResult> reAudit(
			@ApiParam(value = "userId", required = true)
			@PathVariable("userId") String userId,
			@ApiParam(value = "orderId", required = true)
			@PathVariable("orderId") String orderId,
			@ApiParam(value = "draftId", required = true)
			@PathVariable("draftId") String draftId) {
		// TODO Auto-generated method stub
		logger.info("invoke: " + "/user/" + userId + "/audit-order/" + orderId + "/draftId/" + draftId + "/re-audit");
		ApiCallResult result = auditorService.reAudit(userId, draftId, orderId);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			logger.error("fail from psi-service!"+result.getMessage());
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/audit-order/{orderId}", method = RequestMethod.DELETE)
	@ApiOperation(value = "Cancel Order API", response = boolean.class)
	public ResponseEntity<ApiCallResult> cancelOrder(
			@ApiParam(value = "userId", required = true)
			@PathVariable("userId") String userId,
			@ApiParam(value = "orderId", required = true)
			@PathVariable("orderId") String orderId,
			@RequestParam(value = "reason", required = false, defaultValue = "") String reason,
			@RequestParam(value = "reasonOption", required = false, defaultValue = "") String reasonOption) {
		// TODO Auto-generated method stub
		logger.info("invoke: " + "/user/" + userId + "/audit-order/" + orderId);
		ApiCallResult result = auditorService.cancelOrder(userId, orderId, reason, reasonOption);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			logger.error("fail from psi-service!"+result.getMessage());
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/re-audit-list", method = RequestMethod.GET)
	@ApiOperation(value = "Get User's ReAudit List", response = SimpleOrderSearchBean.class, responseContainer = "List")
	public ResponseEntity<ApiCallResult<List<SimpleOrderSearchBean>>> getReInspectionList(
			@ApiParam(value = "userId", required = true)
			@PathVariable("userId") String userId,
			@ApiParam(value = "should be one or more value in [ma,ea,ctpat,stra]", required = false)
			@RequestParam(value = "service-type", required = false, defaultValue = "ma,ea,ctpat,stra") String serviceType,
			@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
			@RequestParam(value = "page-size", required = false, defaultValue = "20") int pageSize,
			@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber) {


		String orderStatus = "60";
		ApiCallResult result = auditorService.searchOrders(userId, serviceType, "", "", orderStatus,
				keyword, pageSize, pageNumber);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			logger.error("fail from psi-service!"+result.getMessage());
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
