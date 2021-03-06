package com.ai.api.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.ai.commons.beans.psi.api.ApiOrderFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ai.api.bean.UserBean;
import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.AuditDao;
import com.ai.api.service.AuditService;
import com.ai.api.service.UserService;
import com.ai.commons.Consts;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.audit.api.ApiAuditBookingBean;
import com.ai.consts.ConstMap;

public class AuditServiceImpl implements AuditService {

	protected Logger logger = LoggerFactory.getLogger(AuditServiceImpl.class);

	@Autowired
	private AuditDao auditorDao;

	@Autowired
	@Qualifier("serviceConfig")
	private ServiceConfig config;

	@Autowired
	@Qualifier("userService")
	private UserService userService;

	@Override
	public ApiCallResult getDraft(String userId, String draftId) {
		ApiCallResult result = auditorDao.getDraft(userId, draftId);
		return result;
	}

	@Override
	public ApiCallResult createDraft(String userId, String serviceType) {
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
		String companyId = "";
		String parentId = "";
		UserBean user = this.getUserBeanByUserId(userId);
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null)
				parentId = "";
			companyId = user.getCompany().getId();
		}
		ApiCallResult result = auditorDao.saveDraft(userId, companyId, parentId, draft);
		return result;
	}

	@Override
	public ApiCallResult deleteDrafts(String userId, String draftIds) {
		String companyId = "";
		String parentId = "";
		UserBean user = this.getUserBeanByUserId(userId);
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null)
				parentId = "";
			companyId = user.getCompany().getId();
		}
		ApiCallResult result = auditorDao.deleteDrafts(userId, companyId, parentId, draftIds);
		return result;
	}

	@Override
	public ApiCallResult searchDrafts(String userId, String serviceType, String startDate, String endDate,
			String keyWord, int pageSize, int pageNo) {
		String companyId = "";
		String parentId = "";
		UserBean user = this.getUserBeanByUserId(userId);
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null)
				parentId = "";
			companyId = user.getCompany().getId();
		}
		try {
			if (!serviceType.isEmpty()) {
				serviceType = URLDecoder.decode(serviceType, "utf-8");
				serviceType = ConstMap.convertServiceType(serviceType, Consts.COMMA);
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("decoding service type: " + serviceType + "got error!");
		}
		ApiCallResult result = auditorDao.searchDrafts(userId, companyId, parentId, serviceType, startDate, endDate,
				keyWord, pageSize, pageNo);
		return result;
	}

	@Override
	public ApiCallResult searchOrders(String userId, String serviceType, String startDate, String endDate,
			String orderStatus, String keyWord, int pageSize, int pageNo) {
		String companyId = "";
		String parentId = "";
		UserBean user = this.getUserBeanByUserId(userId);
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null)
				parentId = "";
			companyId = user.getCompany().getId();
		}
		try {
			if (!serviceType.isEmpty()) {
				serviceType = URLDecoder.decode(serviceType, "utf-8");
				serviceType = ConstMap.convertServiceType(serviceType, Consts.COMMA);
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("decoding service type: " + serviceType + "got error!");
		}
		ApiCallResult result = auditorDao.searchOrders(userId, companyId, parentId, serviceType, startDate, endDate,
				orderStatus, keyWord, pageSize, pageNo);
		return result;
	}

	@Override
	public ApiCallResult createDraftFromPreviousOrder(String userId, String orderId, String serviceType) {
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
		String companyId = "";
		String parentId = "";
		UserBean user = this.getUserBeanByUserId(userId);
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null)
				parentId = "";
			companyId = user.getCompany().getId();
		}
		ApiCallResult result = auditorDao.getOrderDetail(userId, orderId, companyId, parentId);
		return result;
	}

	@Override
	public ApiCallResult saveOrderByDraft(String userId, String draftId, String orderId) {
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

	@Override
	public ApiCallResult calculatePricing(String userId, String draftId, int employeeCount) {
		String companyId = "";
		String parentId = "";
		UserBean user = this.getUserBeanByUserId(userId);
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null)
				parentId = "";
			companyId = user.getCompany().getId();
		}
		ApiCallResult result = auditorDao.calculatePricing(userId, companyId, parentId, draftId, employeeCount);

		return result;
	}

	@Override
	public ApiCallResult reAudit(String userId, String draftId, String orderId) {
		String companyId = "";
		String parentId = "";
		UserBean user = this.getUserBeanByUserId(userId);
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null)
				parentId = "";
			companyId = user.getCompany().getId();
		}
		ApiCallResult result = auditorDao.reAudit(userId, companyId, parentId, draftId, orderId);

		return result;
	}

	@Override
	public ApiCallResult cancelOrder(String userId, String orderId, String reason, String reasonOption) {
		String companyId = "";
		String parentId = "";
		UserBean user = this.getUserBeanByUserId(userId);
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null)
				parentId = "";
			companyId = user.getCompany().getId();
		}
		ApiCallResult result = auditorDao.cancelOrder(userId, companyId, parentId, orderId, reason, reasonOption);

		return result;
	}

	@Override
	public ApiCallResult supplierConfirmOrder(String orderId, String auditDate, String containReadyTime,
			ApiOrderFactoryBean orderFactoryBean) {
		return auditorDao.supplierConfirmOrder(orderId, auditDate, containReadyTime, orderFactoryBean);
	}

}
