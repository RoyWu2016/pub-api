package com.ai.api.service;

import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.audit.api.ApiAuditBookingBean;

public interface AuditService {
	
	ApiCallResult getDraft(String userId, String draftId);

	ApiCallResult createDraft(String userId, String serviceType);

	ApiCallResult saveDraft(String userId, ApiAuditBookingBean draft);

	ApiCallResult deleteDrafts(String userId, String draftIds);

    ApiCallResult searchDrafts(String userId, String serviceType,String startDate,String endDate,
                               String keyWord,int pageSize,int pageNo);

    ApiCallResult searchOrders(String userId,String serviceType, String startDate,String endDate,
                               String orderStatus,String keyWord,int pageSize,int pageNo);

	ApiCallResult createDraftFromPreviousOrder(String userId, String orderId, String serviceType);

	ApiCallResult createOrderByDraft(String userId, String draftId);

	ApiCallResult editOrder(String userId, String orderId);

	ApiCallResult getOrderDetail(String userId, String orderId);

	ApiCallResult saveOrderByDraft(String userId, String draftId, String orderId);

}
