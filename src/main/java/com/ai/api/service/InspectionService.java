package com.ai.api.service;

import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.psi.api.ApiInspectionBookingBean;

public interface InspectionService {

	ApiCallResult getDraft(String userId, String draftId);

	ApiCallResult createDraft(String userId, String serviceType);

	ApiCallResult saveDraft(String userId, ApiInspectionBookingBean draft);

	ApiCallResult createDraftFromPreviousOrder(String userId, String orderId, String serviceType);

	ApiCallResult createOrderByDraft(String userId, String draftId);

	ApiCallResult editOrder(String userId, String orderId);

	ApiCallResult getOrderDetail(String userId, String orderId);

	ApiCallResult saveOrderByDraft(String userId, String draftId, String orderId);

}
