package com.ai.api.controller;

import java.util.List;

import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.audit.api.ApiAuditBookingBean;
import com.ai.commons.beans.order.SimpleOrderSearchBean;
import org.springframework.http.ResponseEntity;

/***************************************************************************
 * <PRE>
 *  Project Name    : api
 *
 *  Package Name    : com.ai.api.controller
 *
 *  File Name       : Auditor.java
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

public interface Audit {

	ResponseEntity<ApiCallResult> createDraft(final String userId, final String serviceType);

	ResponseEntity<ApiCallResult> createDraftFromPreviousOrder(final String userId, final String orderId,
			final String serviceType);

	ResponseEntity<ApiCallResult> getDraft(final String userId, final String draftId);

	ResponseEntity<ApiCallResult> saveDraft(String userId, String draftId, ApiAuditBookingBean draft);

	ResponseEntity<ApiCallResult> deleteDrafts(String userId, String draftIds);

	ResponseEntity<ApiCallResult> searchDrafts(String userId, String serviceType, String startDate, String endDate,
			String keyWord, int pageSize, int pageNo);

	ResponseEntity<ApiCallResult> searchOrders(String userId, String serviceType, String startDate, String endDate,
			String orderStatus, String keyWord, int pageSize, int pageNo);

	ResponseEntity<ApiCallResult> createOrderByDraft(String userId, String draftId);

	ResponseEntity<ApiCallResult> editOrder(String userId, String orderId);

	ResponseEntity<ApiCallResult> getOrderDetail(String userId, String orderId);

	ResponseEntity<ApiCallResult> saveOrderByDraft(String userId, String draftId, String orderId);

	ResponseEntity<ApiCallResult> calculatePricing(String userId, String draftId, String employeeCount);

	ResponseEntity<ApiCallResult> reAudit(String userId, String draftId, String orderId);

	ResponseEntity<ApiCallResult> cancelOrder(String userId, String reason, String orderId, String reasonOption);

	ResponseEntity<ApiCallResult<List<SimpleOrderSearchBean>>> getReInspectionList(String userId, String serviceType,
			String keyword, String pageSize, String pageNumber);

	ResponseEntity<ApiCallResult> getAuditReportPDFInfo(String userId,String orderId);
}
