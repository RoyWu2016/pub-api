package com.ai.api.service;

import com.ai.api.bean.InspectionDraftBean;

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

	InspectionDraftBean createDraft(String userId, String serviceType) throws Exception;

	InspectionDraftBean getDraft(String userId, String draftId) throws Exception;
}
