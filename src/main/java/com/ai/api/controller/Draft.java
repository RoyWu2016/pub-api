package com.ai.api.controller;

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

	ResponseEntity<InspectionBookingBean> createDraftFromPreviousOrder(final String userId, final String orderId);

	ResponseEntity<InspectionBookingBean> getDraft(final String userId, final String draftId);

	ResponseEntity<Boolean> saveDraft(String userId,String draftId,InspectionBookingBean draft);

    ResponseEntity<Boolean> addProduct( String userId,String draftId);

    ResponseEntity<Boolean> saveProduct(String userId,String draftId,String productId,InspectionProductBookingBean draftProduct);

    ResponseEntity<Boolean> deleteProduct( String userId,String draftId,String productId);
    
	ResponseEntity<InspectionBookingBean> calculatePricing(String userId, String draftId, 
			String samplingLevel,String measurementSamplingSize);
}
