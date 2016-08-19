package com.ai.api.dao;

import java.util.List;

import com.ai.commons.beans.checklist.api.ChecklistBean;
import com.ai.commons.beans.checklist.api.ChecklistSearchCriteriaBean;
import com.ai.commons.beans.checklist.api.SimpleChecklistBean;

/***************************************************************************
 * <PRE>
 * Project Name    : Public-API
 * <p>
 * Package Name    : com.ai.api.dao
 * <p>
 * Creation Date   : 2016/7/28 10:51
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


public interface ChecklistDao {
	List<SimpleChecklistBean> searchChecklist(ChecklistSearchCriteriaBean criteria);
	List<SimpleChecklistBean> searchPublicChecklist(ChecklistSearchCriteriaBean criteria);
	String createChecklist(String login,ChecklistBean ChecklistBean);
	String updateChecklist(String login,ChecklistBean ChecklistBean);
	ChecklistBean getChecklist(String login,String checklistId);
	boolean deleteChecklist(String login,String ids);
	boolean checklistNameExist(String login,String checklistName);
    boolean saveFeedback(String login,String checklistId,String feedback);
}
