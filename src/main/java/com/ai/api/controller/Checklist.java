package com.ai.api.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.checklist.api.ChecklistBean;
import com.ai.commons.beans.checklist.api.SimpleChecklistBean;
import com.ai.commons.beans.checklist.vo.CKLChecklistSearchVO;
import com.ai.commons.beans.checklist.vo.CKLChecklistVO;
import com.ai.commons.beans.checklist.vo.CKLDefectVO;
import com.ai.commons.beans.checklist.vo.CKLTestVO;
import com.alibaba.fastjson.JSONArray;

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
	ResponseEntity<JSONArray> searchPrivateChecklist(String userId, String keyword, String pageNumber);

	ResponseEntity<List<CKLChecklistSearchVO>> searchPublicChecklist(String userId, String keyword, String pageNumber);

	ResponseEntity<ApiCallResult<CKLChecklistVO>> createChecklist(String userId, CKLChecklistVO checklistVO);

	ResponseEntity<ApiCallResult<CKLChecklistVO>> updateChecklist(String userId, String checklistId, CKLChecklistVO checklist);

	ResponseEntity<CKLChecklistVO> getChecklist(String userId, String checklistId);

	ResponseEntity deleteChecklist(String userId, String checklistIds);

	ResponseEntity checklistNameExist(String userId, String checklistName);

	ResponseEntity saveFeedback(String userId, String checklistId, String feedback);

	ResponseEntity approved(String userId, String checklistId);

	ResponseEntity<ApiCallResult> calculateChecklistSampleSize(String userId, Integer productQty, String sampleLevel,
			String unit, String criticalDefects, String majorDefects, String minorDefects, Integer piecesNumberPerSet);

	ResponseEntity<ApiCallResult> createTest(String userId, String testId);

	ResponseEntity<ApiCallResult> createDefect(String userId, String defectId);

	ResponseEntity<ApiCallResult> importChecklist(String userId, String checklistId);
}
