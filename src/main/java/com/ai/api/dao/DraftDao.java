package com.ai.api.dao;

import java.util.Map;

import com.ai.api.bean.InspectionDraftBean;

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

	boolean deleteDrafts(Map<String,String> params);

	InspectionDraftBean createDraft(String userId, String compId, String parentId, String serviceTypeStrValue);

	InspectionDraftBean getDraft(String userId, String draftId);
}
