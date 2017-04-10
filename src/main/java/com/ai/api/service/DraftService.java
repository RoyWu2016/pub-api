package com.ai.api.service;

import java.io.IOException;
import java.util.List;

import com.ai.api.exception.AIException;
import com.ai.commons.beans.PageBean;
import com.ai.commons.beans.order.SimpleDraftSearchBean;
import com.ai.commons.beans.order.draft.DraftOrder;
import com.ai.commons.beans.order.price.OrderPriceMandayViewBean;
import com.ai.commons.beans.psi.InspectionBookingBean;
import com.ai.commons.beans.psi.InspectionProductBookingBean;
import com.ai.commons.beans.psi.api.ApiInspectionBookingBean;

/***************************************************************************
 * <PRE>
 * Project Name    : Public-API
 * <p>
 * Package Name    : com.ai.api.service
 * <p>
 * Creation Date   : 2016/8/1 17:01
 * <p>
 * Author          : Jianxiong Cai
 * <p>
 * Purpose         : TODO
 * <p>
 * <p>
 * History         : TODO
 * <p>
 * </PRE>
 ***************************************************************************/

public interface DraftService {

	boolean deleteDraftFromPsi(String userId, String draftIds) throws Exception;

	InspectionBookingBean createDraft(String userId, String serviceType) throws Exception;

	InspectionBookingBean createDraftFromPreviousOrder(String userId, String orderId, String serviceType)
			throws Exception;

	InspectionBookingBean getDraft(String userId, String draftId) throws Exception;

	boolean saveDraft(String userId, InspectionBookingBean draft) throws Exception;

	InspectionProductBookingBean addProduct(String userId, String draftId) throws Exception;

	boolean deleteProduct(String userId, String productId) throws Exception;

	OrderPriceMandayViewBean calculatePricing(String userId, String draftId, String samplingLevel,
			String measurementSamplingSize) throws Exception;

	PageBean<SimpleDraftSearchBean> searchDraft(String userId, String serviceType, String startDate, String endDate, String keyWord,
			String pageNumber, String pageSize) throws IOException, AIException;

	/*
	 * boolean saveProduct(String userId,InspectionProductBookingBean
	 * draftProduct) throws Exception; boolean saveDraftStep(String
	 * userId,String draftId,List<DraftStepBean> draftSteps) throws Exception;
	 */
}
