package com.ai.api.dao;

import java.util.List;

import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.checklist.vo.CKLChecklistSearchVO;
import com.ai.commons.beans.checklist.vo.CKLChecklistVO;
import com.ai.commons.beans.checklist.vo.CKLDefectVO;
import com.ai.commons.beans.checklist.vo.CKLTestVO;

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
	List<CKLChecklistSearchVO> searchPrivateChecklist(String userId, String keyword, int pageNumber);

	List<CKLChecklistSearchVO> searchPublicChecklist(String userId, String keyword, int pageNumber);

	ApiCallResult<CKLChecklistVO> createChecklist(String userId, CKLChecklistVO checklistVO);

	ApiCallResult<CKLChecklistVO> updateChecklist(String userId, String checklistId, CKLChecklistVO checklist);

	CKLChecklistVO getChecklist(String userId, String checklistId);

	boolean deleteChecklist(String userId, String ids);

	boolean checklistNameExist(String userId, String checklistName);

	boolean saveFeedback(String userId, String checklistId, String feedback);

	boolean approved(String userId, String checklistId);

	ApiCallResult calculateChecklistSampleSize(Integer productQty, String sampleLevel, String unit,
			String criticalDefects, String majorDefects, String minorDefects, Integer piecesNumberPerSet);

	ApiCallResult createTest(String userId, String testId);

	ApiCallResult createDefect(String userId, String defectId);

	ApiCallResult importChecklist(String userId, String checklistId);
}
