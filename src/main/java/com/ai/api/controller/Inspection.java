package com.ai.api.controller;

import com.ai.commons.beans.psi.api.ApiInspectionBookingBean;
import org.springframework.http.ResponseEntity;

import com.ai.commons.beans.ApiCallResult;

/***************************************************************************
 * <PRE>
 *  Project Name    : api
 *
 *  Package Name    : com.ai.api.controller
 *
 *  File Name       : Inspection.java
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

public interface Inspection {

	ResponseEntity<ApiCallResult> createDraft(final String userId, final String serviceType);

	ResponseEntity<ApiCallResult> createDraftFromPreviousOrder(final String userId, final String orderId,
			final String serviceType);

	ResponseEntity<ApiCallResult> getDraft(final String userId, final String draftId);

	ResponseEntity<ApiCallResult> saveDraft(String userId,String draftId,ApiInspectionBookingBean draft);

	ResponseEntity<ApiCallResult> createOrderByDraft(String userId, String draftId);

	ResponseEntity<ApiCallResult> editOrder(String userId, String orderId);

	ResponseEntity<ApiCallResult> getOrderDetail(String userId, String orderId);

	ResponseEntity<ApiCallResult> saveOrderByDraft(String userId, String draftId, String orderId);

}
