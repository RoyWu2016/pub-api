package com.ai.api.dao;

import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.psi.api.ApiInspectionBookingBean;

public interface InspectionDao {

	ApiCallResult getDraft(String userId, String draftId);

	ApiCallResult createDraft(String userId, String serviceType, String companyId, String parentId);

	ApiCallResult saveDraft(String userId,String companyId, String parentId, ApiInspectionBookingBean draft);

	ApiCallResult createDraftFromPreviousOrder(String userId, String orderId, String serviceType, String companyId,
			String parentId);

	ApiCallResult createOrderByDraft(String userId, String draftId, String companyId, String parentId);

	ApiCallResult editOrder(String userId, String orderId, String companyId, String parentId);

	ApiCallResult getOrderDetail(String userId, String orderId);

	ApiCallResult saveOrderByDraft(String userId, String draftId, String companyId, String parentId);

}
