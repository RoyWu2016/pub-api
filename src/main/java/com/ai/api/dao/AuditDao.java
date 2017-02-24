package com.ai.api.dao;

import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.audit.api.ApiAuditBookingBean;

public interface AuditDao {

	ApiCallResult getDraft(String userId, String draftId);

	ApiCallResult createDraft(String userId, String serviceType, String companyId, String parentId);

	ApiCallResult saveDraft(String userId, String companyId, String parentId, ApiAuditBookingBean draft);

	ApiCallResult deleteDrafts(String userId, String companyId, String parentId, String draftIds);

	ApiCallResult searchDrafts(String userId, String companyId, String parentId, String serviceType, String startDate,
			String endDate, String keyWord, int pageSize, int pageNo);

	ApiCallResult searchOrders(String userId, String companyId, String parentId, String serviceType, String startDate,
			String endDate, String orderStatus, String keyWord, int pageSize, int pageNo);

	ApiCallResult createDraftFromPreviousOrder(String userId, String orderId, String serviceType, String companyId,
			String parentId);

	ApiCallResult createOrderByDraft(String userId, String draftId, String companyId, String parentId);

	ApiCallResult editOrder(String userId, String orderId, String companyId, String parentId);

	ApiCallResult getOrderDetail(String userId, String orderId, String companyId, String parentId);

	ApiCallResult saveOrderByDraft(String userId, String draftId, String companyId, String parentId);

	ApiCallResult calculatePricing(String userId, String companyId, String parentId, String draftId,
			String employeeCount);

	ApiCallResult reAudit(String userId, String companyId, String parentId, String draftId, String orderId);

	ApiCallResult cancelOrder(String userId, String companyId, String parentId, String orderId, String reason,
			String reasonOption);

}
