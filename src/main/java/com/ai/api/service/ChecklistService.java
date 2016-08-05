package com.ai.api.service;

import java.util.List;

import com.ai.commons.beans.checklist.ChecklistDetailBean;
import com.ai.commons.beans.checklist.ChecklistSearchResultBean;

/***************************************************************************
 * <PRE>
 * Project Name    : Public-API
 * <p>
 * Package Name    : com.ai.api.service
 * <p>
 * Creation Date   : 2016/7/28 10:48
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


public interface ChecklistService {
	List<ChecklistSearchResultBean> searchChecklist(String userID,String keyword, Integer pageNumber);
	List<ChecklistSearchResultBean> searchPublicChecklist(String userId, String keyword);
	String createChecklist(String userId,ChecklistDetailBean checklistDetailBean);
	String updateChecklist(String userId,ChecklistDetailBean checklistDetailBean);
	ChecklistDetailBean getChecklist(String userId,String checklistId);
	boolean deleteChecklist(String userId,String ids);
}
