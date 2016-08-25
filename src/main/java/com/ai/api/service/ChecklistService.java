package com.ai.api.service;

import java.util.List;

import com.ai.commons.beans.checklist.api.ChecklistBean;
import com.ai.commons.beans.checklist.api.SimpleChecklistBean;
import com.ai.commons.beans.checklist.vo.CKLChecklistVO;

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
	List<SimpleChecklistBean> searchChecklist(String userID,String keyword, Integer pageNumber);
	List<SimpleChecklistBean> searchPublicChecklist(String userId, String keyword);
	String createChecklist(String userId,CKLChecklistVO checklistVO);
//	String createChecklistInMW(String userId,ChecklistBean checklistBean);
	String updateChecklist(String userId,CKLChecklistVO checklist);
	ChecklistBean getChecklist(String userId,String checklistId);
	boolean deleteChecklist(String userId,String ids);
	boolean checklistNameExist(String userId,String checklistName);
    boolean saveFeedback(String userId,String checklistId,String feedback);
    boolean approved(String userId,String checklistId);
}
