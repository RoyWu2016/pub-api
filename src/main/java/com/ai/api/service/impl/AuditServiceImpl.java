package com.ai.api.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ai.api.bean.UserBean;
import com.ai.api.dao.AuditorDao;
import com.ai.api.service.AuditorService;
import com.ai.api.service.UserService;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.audit.api.ApiAuditBookingBean;

public class AuditorServiceImpl implements AuditorService {
	
	protected Logger logger = LoggerFactory.getLogger(AuditorServiceImpl.class);

	@Autowired
	private AuditorDao auditorDao;
	
	@Autowired
	@Qualifier("userService")
	private UserService userService;

	@Override
	public ApiCallResult getDraft(String userId, String draftId) {
		// TODO Auto-generated method stub
		ApiCallResult result = auditorDao.getDraft(userId, draftId);
		return result;
	}

	@Override
	public ApiCallResult createDraft(String userId, String serviceType) {
		// TODO Auto-generated method stub
		String companyId = "";
		String parentId = "";
		UserBean user = this.getUserBeanByUserId(userId);
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null)
				parentId = "";
			companyId = user.getCompany().getId();
		}
		ApiCallResult result = auditorDao.createDraft(userId, serviceType, companyId, parentId);
		return result;
	}

	@Override
	public ApiCallResult saveDraft(String userId, ApiAuditBookingBean draft) {
		// TODO Auto-generated method stub
		String companyId = "";
		String parentId = "";
		UserBean user = this.getUserBeanByUserId(userId);
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null)
				parentId = "";
			companyId = user.getCompany().getId();
		}
		ApiCallResult result = auditorDao.saveDraft(userId,companyId, parentId,draft);
		return result;
	}

	@Override
	public ApiCallResult createDraftFromPreviousOrder(String userId, String orderId, String serviceType) {
		// TODO Auto-generated method stub
		String companyId = "";
		String parentId = "";
		UserBean user = this.getUserBeanByUserId(userId);
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null)
				parentId = "";
			companyId = user.getCompany().getId();
		}
		ApiCallResult result = auditorDao.createDraftFromPreviousOrder(userId, orderId, serviceType, companyId,
				parentId);
		return result;
	}

	@Override
	public ApiCallResult createOrderByDraft(String userId, String draftId) {
		// TODO Auto-generated method stub
		String companyId = "";
		String parentId = "";
		UserBean user = this.getUserBeanByUserId(userId);
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null)
				parentId = "";
			companyId = user.getCompany().getId();
		}
		ApiCallResult result = auditorDao.createOrderByDraft(userId, draftId, companyId, parentId);
		return result;
	}

	@Override
	public ApiCallResult editOrder(String userId, String orderId) {
		// TODO Auto-generated method stub
		String companyId = "";
		String parentId = "";
		UserBean user = this.getUserBeanByUserId(userId);
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null)
				parentId = "";
			companyId = user.getCompany().getId();
		}
		ApiCallResult result = auditorDao.editOrder(userId, orderId, companyId, parentId);
		return result;
	}

	@Override
	public ApiCallResult getOrderDetail(String userId, String orderId) {
		// TODO Auto-generated method stub
		ApiCallResult result = auditorDao.getOrderDetail(userId, orderId);
		return result;
	}

	@Override
	public ApiCallResult saveOrderByDraft(String userId, String draftId, String orderId) {
		// TODO Auto-generated method stub
		String companyId = "";
		String parentId = "";
		UserBean user = this.getUserBeanByUserId(userId);
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null)
				parentId = "";
			companyId = user.getCompany().getId();
		}
		ApiCallResult result = auditorDao.saveOrderByDraft(userId, draftId, companyId, parentId);

		return result;
	}
	
	private UserBean getUserBeanByUserId(String userId) {
		UserBean user = null;
		try {
			user = userService.getCustById(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

}
