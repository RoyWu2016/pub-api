package com.ai.api.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ai.api.dao.ChecklistDao;
import com.ai.api.dao.ParameterDao;
import com.ai.api.service.ChecklistService;
import com.ai.api.service.UserService;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.checklist.vo.CKLChecklistSearchVO;
import com.ai.commons.beans.checklist.vo.CKLChecklistVO;
import com.ai.commons.beans.checklist.vo.CKLDefectVO;
import com.ai.commons.beans.checklist.vo.CKLTestVO;

/***************************************************************************
 * <PRE>
 * Project Name    : Public-API
 * <p>
 * Package Name    : com.ai.api.service.impl
 * <p>
 * Creation Date   : 2016/7/28 10:49
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

@Service
public class ChecklistServiceImpl implements ChecklistService {
	protected Logger logger = LoggerFactory.getLogger(ChecklistServiceImpl.class);

	@Autowired
	private ChecklistDao checklistDao;

	@Autowired
	@Qualifier("paramDao")
	private ParameterDao paramDao;

	// @Autowired
	// @Qualifier("customerDao")
	// private CustomerDao customerDao;

	@Autowired
	@Qualifier("userService")
	private UserService userService;

	@Override
	public List<CKLChecklistSearchVO> searchPrivateChecklist(String userId, String keyword, int pageNumber) {
		if (0 == pageNumber)
			pageNumber = 1;
		List<CKLChecklistSearchVO> list = checklistDao.searchPrivateChecklist(userId, keyword, pageNumber);

		logger.info("warning ...");
		// List<ProductCategoryDtoBean> categoryList =
		// paramDao.getProductCategoryList();
		// List<ProductFamilyDtoBean> familyList =
		// paramDao.getProductFamilyList();
		// for (CKLChecklistSearchVO bean:list){
		// //pls do pay attention to this !
		// String mwCategory = bean.getProductCategory();
		// String mwFamily = bean.getProductFamily();
		// bean.setProductCategory(mwFamily);
		// bean.setProductFamily(mwCategory);
		// for (ProductCategoryDtoBean category:categoryList){
		// if (category.getName().equals(mwFamily)){
		// bean.setProductCategoryId(category.getId());
		// break;
		// }
		// }
		// for (ProductFamilyDtoBean family:familyList){
		// if (family.getName().equals(mwCategory)){
		// bean.setProductFamilyId(family.getId());
		// break;
		// }
		// }
		// }
		logger.info("warning end!");
		return list;
	}

	@Override
	public List<CKLChecklistSearchVO> searchPublicChecklist(String userId, String keyword, int pageNumber) {
		if (0 == pageNumber)
			pageNumber = 1;
		List<CKLChecklistSearchVO> list = checklistDao.searchPublicChecklist(userId, keyword, pageNumber);
		return list;
	}

	@Override
	public String createChecklist(String userId, CKLChecklistVO checklistVO) {
		return checklistDao.createChecklist(userId, checklistVO);
	}

	@Override
	public String updateChecklist(String userId, String checklistId, CKLChecklistVO checklist) {
		// String login =
		// userService.getLoginByUserId(userId);//customerDao.getGeneralUser(userId).getLogin();
		return checklistDao.updateChecklist(userId, checklistId, checklist);
	}

	@Override
	public CKLChecklistVO getChecklist(String userId, String checklistId) {
		// String login =
		// userService.getLoginByUserId(userId);//customerDao.getGeneralUser(userId).getLogin();
		return checklistDao.getChecklist(userId, checklistId);
	}

	@Override
	public boolean deleteChecklist(String userId, String ids) {
		// String login =
		// userService.getLoginByUserId(userId);//customerDao.getGeneralUser(userId).getLogin();
		return checklistDao.deleteChecklist(userId, ids);
	}

	@Override
	public boolean checklistNameExist(String userId, String checklistName) {
		// String login = userService.getLoginByUserId(userId);
		return checklistDao.checklistNameExist(userId, checklistName);
	}

	@Override
	public boolean saveFeedback(String userId, String checklistId, String feedback) {
		// String login = userService.getLoginByUserId(userId);
		return checklistDao.saveFeedback(userId, checklistId, feedback);
	}

	@Override
	public boolean approved(String userId, String checklistId) {
		// String login = userService.getLoginByUserId(userId);
		return checklistDao.approved(userId, checklistId);
	}

	@Override
	public ApiCallResult calculateChecklistSampleSize(Integer productQty, String sampleLevel, String unit,
			String criticalDefects, String majorDefects, String minorDefects, Integer piecesNumberPerSet) {
		// TODO Auto-generated method stub
		return checklistDao.calculateChecklistSampleSize(productQty, sampleLevel, unit, criticalDefects, majorDefects,
				minorDefects, piecesNumberPerSet);
	}

	@Override
	public ApiCallResult createTest(String userId, String testId) {
		// TODO Auto-generated method stub
		return checklistDao.createTest(userId,testId);
	}

	@Override
	public ApiCallResult createDefect(String userId,  String defectId) {
		// TODO Auto-generated method stub
		return checklistDao.createDefect(userId, defectId);
	}
}
