package com.ai.api.dao;

import java.util.List;

import com.ai.commons.beans.order.draft.DraftOrder;
import com.ai.commons.beans.order.price.OrderPriceMandayViewBean;
import com.ai.commons.beans.psi.InspectionBookingBean;
import com.ai.commons.beans.psi.InspectionProductBookingBean;
import com.ai.commons.beans.psi.api.ApiInspectionBookingBean;

/***************************************************************************
 * <PRE>
 * Project Name    : Public-API
 * <p>
 * Package Name    : com.ai.api.dao
 * <p>
 * Creation Date   : 2016/8/1 16:47
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


public interface DraftDao {

	boolean deleteDraftsFromPsi(String userId,String compId, String parentId, String draftIds);

    InspectionBookingBean createDraft(String userId, String compId, String parentId, String serviceTypeStrValue);

    InspectionBookingBean createDraftFromPreviousOrder(String userId, String companyId, String parentId, String orderId);

    InspectionBookingBean getDraft(String userId,String compId, String parentId, String draftId);

	ApiInspectionBookingBean getDraftNew(String userId,String compId, String parentId, String draftId);

	boolean saveDraft(String userId,String companyId,String parentId,InspectionBookingBean draft);

    InspectionProductBookingBean addProduct(String userId,String companyId,String parentId,String draftId);


    boolean deleteProduct(String userId,String companyId,String parentId,String productId);
    
    OrderPriceMandayViewBean calculatePricing(
			String userId, String companyId, 
			String parentId,String draftId,
			String samplingLevel,String measurementSamplingSize);
	
	 List<DraftOrder> searchDraft(String userId, String compId, String parentId,  String serviceType, String startDate, String endDate, String keyWord, String pageSize, String pageNumber);

	/*
	boolean saveProduct(String userId,String companyId,String parentId,InspectionProductBookingBean draftProduct);
	boolean saveDraftStep(String userId, String draftId, List<DraftStepBean> draftSteps);
	*/
}

