package com.ai.api.controller;

import java.util.List;

import com.ai.commons.beans.checklist.api.ChecklistBean;
import com.ai.commons.beans.checklist.api.SimpleChecklistBean;
import com.ai.commons.beans.checklist.vo.CKLChecklistSearchVO;
import com.ai.commons.beans.checklist.vo.CKLChecklistVO;
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
	ResponseEntity<List<CKLChecklistSearchVO>> searchPrivateChecklist(String userId, String keyword,int pageNumber);
	ResponseEntity<List<CKLChecklistSearchVO>> searchPublicChecklist(String userId, String keyword,int pageNumber);
	ResponseEntity<String> createChecklist(String userId, CKLChecklistVO checklistVO);
	ResponseEntity<String> updateChecklist(String userId,String checklistId,CKLChecklistVO checklist);
    ResponseEntity<CKLChecklistVO> getChecklist(String userId,String checklistId);
	ResponseEntity deleteChecklist(String userId,String checklistIds);
	ResponseEntity checklistNameExist(String userId,String checklistName);
	ResponseEntity saveFeedback(String userId,String checklistId,String feedback);
    ResponseEntity approved(String userId,String checklistId);
}
