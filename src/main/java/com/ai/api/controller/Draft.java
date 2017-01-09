
package com.ai.api.controller;

import java.util.List;

import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.order.draft.DraftOrder;
import com.ai.commons.beans.order.price.OrderPriceMandayViewBean;
import com.ai.commons.beans.psi.InspectionBookingBean;
import com.ai.commons.beans.psi.InspectionProductBookingBean;
import org.springframework.http.ResponseEntity;

/***************************************************************************
 * <PRE>
 * Project Name    : Public-API
 * <p>
 * Package Name    : com.ai.api.controller
 * <p>
 * Creation Date   : 2016/8/1 17:14
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


public interface Draft {
	ResponseEntity<Boolean> deleteDrafts(String userId, String draftIds);

	ResponseEntity<InspectionBookingBean> createDraft(final String userId, final String serviceType);

	ResponseEntity<InspectionBookingBean> createDraftFromPreviousOrder(final String userId, final String orderId, final String serviceType);

	ResponseEntity<InspectionBookingBean> getDraft(final String userId, final String draftId);

	ResponseEntity<ApiCallResult> getDraftNew(final String userId, final String draftId);

	ResponseEntity<Boolean> saveDraft(String userId,String draftId,InspectionBookingBean draft);

    ResponseEntity<InspectionProductBookingBean> addProduct( String userId,String draftId);


    ResponseEntity<Boolean> deleteProduct( String userId,String draftId,String productId);
    
	ResponseEntity<OrderPriceMandayViewBean> calculatePricing(String userId, String draftId, 
			String samplingLevel,String measurementSamplingSize);
	
	 ResponseEntity<List<DraftOrder>> searchDraft(String userId, String serviceType,String startDate, String endDate, String keyWord, String pageNumber, String pageSize);

	/*
    ResponseEntity<Boolean> saveProduct(String userId,String draftId,String productId,InspectionProductBookingBean draftProduct);

	ResponseEntity<Boolean> saveProducts(String userId,String draftId,InspectionDraftProductsAndStepBean draftProductsList);
	
	ResponseEntity<Boolean> saveDraftStep(String userId,String draftId,List<DraftStepBean> draftSteps);
	*/
}

