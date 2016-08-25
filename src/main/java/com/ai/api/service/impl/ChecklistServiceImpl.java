package com.ai.api.service.impl;


import java.util.List;

import com.ai.api.bean.ProductCategoryDtoBean;
import com.ai.api.bean.ProductFamilyDtoBean;
import com.ai.api.dao.ChecklistDao;
import com.ai.api.dao.CustomerDao;
import com.ai.api.dao.ParameterDao;
import com.ai.api.service.ChecklistService;
import com.ai.api.service.UserService;
import com.ai.commons.IDGenerator;
import com.ai.commons.beans.checklist.api.ChecklistBean;
import com.ai.commons.beans.checklist.api.ChecklistSearchCriteriaBean;
import com.ai.commons.beans.checklist.api.SimpleChecklistBean;
import com.ai.commons.beans.checklist.vo.CKLChecklistVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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

//	@Autowired
//	@Qualifier("customerDao")
//	private CustomerDao customerDao;

	@Autowired
	@Qualifier("userService")
	private UserService userService;

	@Override
	public List<SimpleChecklistBean> searchChecklist(String userID, String keyword, Integer pageNumber) {
		//ReportSearchCriteriaBean criteria = new ReportSearchCriteriaBean();
		ChecklistSearchCriteriaBean criteria = new ChecklistSearchCriteriaBean();
		criteria.setUserID(userID);
		criteria.setKeywords(keyword);
		if(pageNumber==null)
			pageNumber = 1;
		criteria.setPageNumber(pageNumber);
		criteria.setLogin(userService.getLoginByUserId(userID));

		List<SimpleChecklistBean> list = checklistDao.searchChecklist(criteria);

		List<ProductCategoryDtoBean> categoryList = paramDao.getProductCategoryList();
		List<ProductFamilyDtoBean> familyList = paramDao.getProductFamilyList();
		for (SimpleChecklistBean bean:list){
			String mwCategory = bean.getProductCategory();
			String mwFamily = bean.getProductFamily();
			bean.setProductCategory(mwFamily);
			bean.setProductFamily(mwCategory);
			for (ProductCategoryDtoBean category:categoryList){
				if (category.getName().equals(mwFamily)){
					bean.setProductCategoryId(category.getId());
					break;
				}
			}
			for (ProductFamilyDtoBean family:familyList){
				if (family.getName().equals(mwCategory)){
					bean.setProductFamilyId(family.getId());
					break;
				}
			}
		}
		return list;
	}

	@Override
	public List<SimpleChecklistBean> searchPublicChecklist(String userId, String keyword){
		ChecklistSearchCriteriaBean criteria = new ChecklistSearchCriteriaBean();
		criteria.setUserID(userId);
		criteria.setKeywords(keyword);
		criteria.setLogin(userService.getLoginByUserId(userId));
		List<SimpleChecklistBean> list = checklistDao.searchPublicChecklist(criteria);
		return list;
	}

	@Override
	public String createChecklist(String userId,CKLChecklistVO checklistVO){
		try {
			String companyId = userService.getCompanyIdByUserId(userId);
			checklistVO.setClientId(companyId);
		}catch (Exception e){
			logger.error("creating checklist ... getCompanyId error ",e);
		}
		checklistVO.setCheckListId(IDGenerator.uuid());
		return checklistDao.createChecklist(checklistVO);
	}

//    @Override
//    public String createChecklistInMW(String userId,ChecklistBean checklistBean){
//        String login = userService.getLoginByUserId(userId);//customerDao.getGeneralUser(userId).getLogin();
//        return checklistDao.createChecklistInMW(login,checklistBean);
//    }

	@Override
	public String updateChecklist(String userId,CKLChecklistVO checklist){
		String login = userService.getLoginByUserId(userId);//customerDao.getGeneralUser(userId).getLogin();
		return checklistDao.updateChecklist(checklist);
	}

	@Override
	public CKLChecklistVO getChecklist(String userId,String checklistId){
		String login = userService.getLoginByUserId(userId);//customerDao.getGeneralUser(userId).getLogin();
		return checklistDao.getChecklist(checklistId);
	}

	@Override
	public boolean deleteChecklist(String userId,String ids){
		String login = userService.getLoginByUserId(userId);//customerDao.getGeneralUser(userId).getLogin();
		return checklistDao.deleteChecklist(login,ids);
	}

	@Override
	public boolean checklistNameExist(String userId,String checklistName){
		String login = userService.getLoginByUserId(userId);
		return checklistDao.checklistNameExist(login,checklistName);
	}

	@Override
	public boolean saveFeedback(String userId,String checklistId,String feedback){
		String login = userService.getLoginByUserId(userId);
		return checklistDao.saveFeedback(login,checklistId,feedback);
	}

	@Override
	public boolean approved(String userId,String checklistId){
		String login = userService.getLoginByUserId(userId);
		return checklistDao.approved(login,checklistId);
	}
}
