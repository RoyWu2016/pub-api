package com.ai.api.dao;

import java.util.List;

import com.ai.commons.beans.checklist.api.ChecklistBean;
import com.ai.commons.beans.checklist.api.ChecklistSearchCriteriaBean;
import com.ai.commons.beans.checklist.api.SimpleChecklistBean;
import com.ai.commons.beans.checklist.vo.CKLChecklistVO;

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
	String createChecklist(CKLChecklistVO checklistVO);
//	String createChecklistInMW(String login,ChecklistBean ChecklistBean);
    String updateChecklist(CKLChecklistVO checklist);
	CKLChecklistVO getChecklist(String checklistId);
	boolean deleteChecklist(String login,String ids);
	boolean checklistNameExist(String login,String checklistName);
    boolean saveFeedback(String login,String checklistId,String feedback);
    boolean approved(String login,String checklistId);
}
