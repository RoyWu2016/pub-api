package com.ai.api.controller;

import org.springframework.http.ResponseEntity;

import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.audit.api.ApiAuditBookingBean;

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

	ResponseEntity<ApiCallResult> createOrderByDraft(String userId, String draftId);

	ResponseEntity<ApiCallResult> editOrder(String userId, String orderId);

	ResponseEntity<ApiCallResult> getOrderDetail(String userId, String orderId);

	ResponseEntity<ApiCallResult> saveOrderByDraft(String userId, String draftId, String orderId);

}
