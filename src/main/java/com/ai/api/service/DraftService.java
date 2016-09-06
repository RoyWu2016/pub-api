package com.ai.api.service;

import com.ai.commons.beans.psi.InspectionBookingBean;
import com.ai.commons.beans.psi.InspectionProductBookingBean;

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
	boolean deleteDraft(String userId,String ids) throws Exception;

	boolean deleteDraftFromPsi(String userId, String draftIds) throws Exception;

    InspectionBookingBean createDraft(String userId, String serviceType) throws Exception;

    InspectionBookingBean createDraftFromPreviousOrder(String userId, String orderId) throws Exception;

    InspectionBookingBean getDraft(String userId, String draftId) throws Exception;

	boolean saveDraft(String userId,InspectionBookingBean draft) throws Exception;

    boolean addProduct(String userId,String draftId) throws Exception;

    boolean saveProduct(String userId,InspectionProductBookingBean draftProduct) throws Exception;

    boolean deleteProduct(String userId,String productId) throws Exception;
    
	InspectionBookingBean calculatePricing(String userId, String draftId,
			String samplingLevel,String measurementSamplingSize) throws Exception;
}
