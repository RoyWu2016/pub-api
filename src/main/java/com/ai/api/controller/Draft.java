package com.ai.api.controller;

import com.ai.api.bean.InspectionDraftBean;
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
	ResponseEntity<Boolean> deleteDraftFrom(String userId, String draftIds);

	ResponseEntity<InspectionDraftBean> createDraft(final String userId, final String serviceType);

	ResponseEntity<InspectionDraftBean> getDraft(final String userId, final String draftId);

	ResponseEntity<Boolean> saveDraft(String userId,String draftId,InspectionDraftBean inspectionDraftBean);
}
