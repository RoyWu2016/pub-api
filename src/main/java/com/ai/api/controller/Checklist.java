package com.ai.api.controller;

import java.util.List;

import com.ai.commons.beans.checklist.api.ChecklistBean;
import com.ai.commons.beans.checklist.api.SimpleChecklistBean;
import org.springframework.http.ResponseEntity;

/***************************************************************************
 * <PRE>
 * Project Name    : Public-API
 * <p>
 * Package Name    : com.ai.api.controller
 * <p>
 * Creation Date   : 2016/7/28 10:07
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


public interface Checklist {
	ResponseEntity<List<SimpleChecklistBean>> searchChecklist(String userId,String keyword, Integer pageNumber);
	ResponseEntity<List<SimpleChecklistBean>> searchPublicChecklist(String userId, String keyword);
	ResponseEntity<String> createChecklist(String userId, ChecklistBean ChecklistBean);
	ResponseEntity<String> updateChecklist(String userId,String checklistId, ChecklistBean ChecklistBean);
	ResponseEntity<ChecklistBean> getChecklist(String userId,String checklistId);
	ResponseEntity deleteChecklist(String userId,String checklistIds);
	ResponseEntity checklistNameExist(String userId,String checklistName);
	ResponseEntity saveFeedback(String userId,String checklistId,String feedback);
    ResponseEntity approved(String userId,String checklistId);
}
