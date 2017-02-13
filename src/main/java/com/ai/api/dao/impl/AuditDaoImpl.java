package com.ai.api.dao.impl;

import java.io.IOException;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.AuditDao;
import com.ai.commons.HttpUtil;
import com.ai.commons.JsonUtil;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.audit.api.ApiAuditBookingBean;

public class AuditDaoImpl implements AuditDao {
	
	private static final Logger logger = LoggerFactory.getLogger(AuditDaoImpl.class);

	@Autowired
	@Qualifier("serviceConfig")
	private ServiceConfig config;

	@Override
	public ApiCallResult getDraft(String userId, String draftId) {
		// TODO Auto-generated method stub
		StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
		ApiCallResult fianlRe = new ApiCallResult();
		url.append("/inspection-draft/api/getDraft/").append(userId).append("/").append(draftId);
		try {
			ServiceCallResult result = HttpUtil.issueGetRequest(url.toString(), null);
			fianlRe = JsonUtil.mapToObject(result.getResponseString(), ApiCallResult.class);
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			fianlRe.setMessage("Exception: " + e.toString());
		}
		return fianlRe;
	}

	@Override
	public ApiCallResult createDraft(String userId, String serviceType, String companyId, String parentId) {
		// TODO Auto-generated method stub
		StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
		ApiCallResult fianlRe = new ApiCallResult();
		url.append("/inspection-draft/api/createDraft");
		url.append("?userId=" + userId);
		url.append("&serviceType=" + serviceType);
		url.append("&companyId=" + companyId);
		url.append("&parentId=" + parentId);
		try {
			ServiceCallResult result = HttpUtil.issuePostRequest(url.toString(), null, userId);
			fianlRe = JsonUtil.mapToObject(result.getResponseString(), ApiCallResult.class);
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			fianlRe.setMessage("Exception: " + e.toString());
		}
		return fianlRe;
	}

	@Override
	public ApiCallResult saveDraft(String userId, String companyId, String parentId, ApiAuditBookingBean draft) {
		// TODO Auto-generated method stub
		StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
		ApiCallResult fianlRe = new ApiCallResult();
		url.append("/inspection-draft/api/saveDraft");
		url.append("?userId=" + userId);
		url.append("&companyId=" + companyId);
		url.append("&parentId=" + parentId);
		try {
			ServiceCallResult result = HttpUtil.issuePostRequest(url.toString(), null, draft);
			fianlRe = JsonUtil.mapToObject(result.getResponseString(), ApiCallResult.class);
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			fianlRe.setMessage("Exception: " + e.toString());
		}
		return fianlRe;
	}

	@Override
	public ApiCallResult createDraftFromPreviousOrder(String userId, String orderId, String serviceType,
			String companyId, String parentId) {
		// TODO Auto-generated method stub
		StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
		ApiCallResult fianlRe = new ApiCallResult();
		url.append("/inspection-draft/api/createDraftFromPreviousOrder");
		url.append("?userId=" + userId);
		url.append("&serviceType=" + serviceType);
		url.append("&orderId=" + orderId);
		url.append("&companyId=" + companyId);
		url.append("&parentId=" + parentId);
		try {
			ServiceCallResult result = HttpUtil.issuePostRequest(url.toString(), null, userId);
			fianlRe = JsonUtil.mapToObject(result.getResponseString(), ApiCallResult.class);
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			fianlRe.setMessage("Exception: " + e.toString());
		}
		return fianlRe;
	}

	@Override
	public ApiCallResult createOrderByDraft(String userId, String draftId, String companyId, String parentId) {
		// TODO Auto-generated method stub
		StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
		ApiCallResult fianlRe = new ApiCallResult();
		url.append("/inspection-draft/api/createOrderByDraft");
		url.append("?userId=" + userId);
		url.append("&draftId=" + draftId);
		url.append("&companyId=" + companyId);
		url.append("&parentId=" + parentId);
		try {
			ServiceCallResult result = HttpUtil.issuePostRequest(url.toString(), null, draftId);
			fianlRe = JsonUtil.mapToObject(result.getResponseString(), ApiCallResult.class);
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			fianlRe.setMessage("Exception: " + e.toString());
		}
		return fianlRe;
	}

	@Override
	public ApiCallResult editOrder(String userId, String orderId, String companyId, String parentId) {
		// TODO Auto-generated method stub
		StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
		ApiCallResult fianlRe = new ApiCallResult();
		url.append("/inspection-draft/api/editOrder");
		url.append("?userId=" + userId);
		url.append("&orderId=" + orderId);
		url.append("&companyId=" + companyId);
		url.append("&parentId=" + parentId);
		try {
			ServiceCallResult result = HttpUtil.issueGetRequest(url.toString(), null);
			fianlRe = JsonUtil.mapToObject(result.getResponseString(), ApiCallResult.class);
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			fianlRe.setMessage("Exception: " + e.toString());
		}
		return fianlRe;
	}

	@Override
	public ApiCallResult getOrderDetail(String userId, String orderId) {
		// TODO Auto-generated method stub
		StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
		ApiCallResult fianlRe = new ApiCallResult();
		url.append("/inspection-draft/api/getOrder/").append(userId).append("/").append(orderId);
		try {
			ServiceCallResult result = HttpUtil.issueGetRequest(url.toString(), null);
			fianlRe = JsonUtil.mapToObject(result.getResponseString(), ApiCallResult.class);
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			fianlRe.setMessage("Exception: " + e.toString());
		}
		return fianlRe;
	}

	@Override
	public ApiCallResult saveOrderByDraft(String userId, String draftId, String companyId, String parentId) {
		// TODO Auto-generated method stub
		StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
		ApiCallResult fianlRe = new ApiCallResult();
		url.append("/inspection-draft/api/saveOrderByDraft");
		url.append("?userId=" + userId);
		url.append("&draftId=" + draftId);
		url.append("&companyId=" + companyId);
		url.append("&parentId=" + parentId);
		try {
			ServiceCallResult result = HttpUtil.issueGetRequest(url.toString(), null);
			fianlRe = JsonUtil.mapToObject(result.getResponseString(), ApiCallResult.class);
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			fianlRe.setMessage("Exception: " + e.toString());
		}
		return fianlRe;
	}

}
