package com.ai.api.service.impl;


import java.util.List;

import com.ai.api.bean.ProductCategoryDtoBean;
import com.ai.api.bean.ProductFamilyDtoBean;
import com.ai.api.dao.ChecklistDao;
import com.ai.api.dao.CustomerDao;
import com.ai.api.dao.ParameterDao;
import com.ai.api.service.ChecklistService;
import com.ai.commons.beans.checklist.api.ChecklistDetailBean;
import com.ai.commons.beans.checklist.ChecklistSearchCriteriaBean;
import com.ai.commons.beans.checklist.ChecklistSearchResultBean;
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

	@Autowired
	private ChecklistDao checklistDao;

	@Autowired
	@Qualifier("paramDao")
	private ParameterDao paramDao;

	@Autowired
	@Qualifier("customerDao")
	private CustomerDao customerDao;

	@Override
	public List<ChecklistSearchResultBean> searchChecklist(String userID, String keyword, Integer pageNumber) {
		//ReportSearchCriteriaBean criteria = new ReportSearchCriteriaBean();
		ChecklistSearchCriteriaBean criteria = new ChecklistSearchCriteriaBean();
		criteria.setUserID(userID);
		criteria.setKeywords(keyword);
		if(pageNumber==null)
			pageNumber = 1;
		criteria.setPageNumber(pageNumber);
		criteria.setLogin(customerDao.getGeneralUser(userID).getLogin());

		List<ChecklistSearchResultBean> list = checklistDao.searchChecklist(criteria);

		List<ProductCategoryDtoBean> categoryList = paramDao.getProductCategoryList();
		List<ProductFamilyDtoBean> familyList = paramDao.getProductFamilyList();
		for (ChecklistSearchResultBean bean:list){
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
	public List<ChecklistSearchResultBean> searchPublicChecklist(String userId, String keyword){
		ChecklistSearchCriteriaBean criteria = new ChecklistSearchCriteriaBean();
		criteria.setUserID(userId);
		criteria.setKeywords(keyword);
		criteria.setLogin(customerDao.getGeneralUser(userId).getLogin());
		List<ChecklistSearchResultBean> list = checklistDao.searchPublicChecklist(criteria);
		return list;
	}

	@Override
	public String createChecklist(String userId,ChecklistDetailBean checklistDetailBean){
		String login = customerDao.getGeneralUser(userId).getLogin();
		return checklistDao.createChecklist(login,checklistDetailBean);
	}

	@Override
	public String updateChecklist(String userId,ChecklistDetailBean checklistDetailBean){
		String login = customerDao.getGeneralUser(userId).getLogin();
		return checklistDao.updateChecklist(login,checklistDetailBean);
	}

	@Override
	public ChecklistDetailBean getChecklist(String userId,String checklistId){
		String login = customerDao.getGeneralUser(userId).getLogin();
		return checklistDao.getChecklist(login,checklistId);
	}

	@Override
	public  boolean deleteChecklist(String userId,String ids){
		String login = customerDao.getGeneralUser(userId).getLogin();
		return checklistDao.deleteChecklist(login,ids);
	}
}
