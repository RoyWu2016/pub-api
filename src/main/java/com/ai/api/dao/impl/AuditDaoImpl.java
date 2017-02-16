package com.ai.api.dao.impl;

import java.io.IOException;

import com.ai.api.bean.consts.ConstMap;
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
		StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
		ApiCallResult finalResult = new ApiCallResult();
		url.append("/api/audit/getDraft/").append(userId).append("/").append(draftId);
		try {
			ServiceCallResult result = HttpUtil.issueGetRequest(url.toString(), null);
			finalResult = JsonUtil.mapToObject(result.getResponseString(), ApiCallResult.class);
		} catch (IOException e) {
			logger.error("Error getDraft!"+ExceptionUtils.getStackTrace(e));
			finalResult.setMessage("Exception: " + e.toString());
		}
		return finalResult;
	}

	@Override
	public ApiCallResult createDraft(String userId, String serviceType, String companyId, String parentId) {
		String subServiceType = null;
        String type = ConstMap.serviceTypeMap.get(serviceType.toLowerCase());
        serviceType = type;
        if (type.indexOf(",")!=-1){
            serviceType = type.split(",")[0];
            subServiceType = type.split(",")[1];
        }
		StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
		ApiCallResult finalResult = new ApiCallResult();
		url.append("/api/audit/createDraft");
		url.append("?userId=" + userId);
		url.append("&serviceType=" + serviceType);
		url.append("&companyId=" + companyId);
		url.append("&parentId=" + parentId).append("&subServiceType="+subServiceType);
		try {
			ServiceCallResult result = HttpUtil.issuePostRequest(url.toString(), null, userId);
			finalResult = JsonUtil.mapToObject(result.getResponseString(), ApiCallResult.class);
		} catch (IOException e) {
			logger.error("Error createDraft!"+ExceptionUtils.getStackTrace(e));
			finalResult.setMessage("Exception: " + e.toString());
		}
		return finalResult;
	}

	@Override
	public ApiCallResult saveDraft(String userId, String companyId, String parentId, ApiAuditBookingBean draft) {
		StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
		ApiCallResult finalResult = new ApiCallResult();
		url.append("/api/audit/updateDraft");
		url.append("?userId=" + userId);
		url.append("&companyId=" + companyId);
		url.append("&parentId=" + parentId);
		try {
			ServiceCallResult result = HttpUtil.issuePostRequest(url.toString(), null, draft);
			finalResult = JsonUtil.mapToObject(result.getResponseString(), ApiCallResult.class);
		} catch (IOException e) {
			logger.error("Error saveDraft!"+ExceptionUtils.getStackTrace(e));
			finalResult.setMessage("Exception: " + e.toString());
		}
		return finalResult;
	}

	@Override
	public ApiCallResult createDraftFromPreviousOrder(String userId, String orderId, String serviceType,
			String companyId, String parentId) {
        String type = ConstMap.serviceTypeMap.get(serviceType.toLowerCase());
        serviceType = type;
        if (type.indexOf(",")!=-1){
            serviceType = type.split(",")[0];
        }
		StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
		ApiCallResult finalResult = new ApiCallResult();
		url.append("/api/audit/createDraftFromPreviousOrder");
		url.append("?userId=" + userId);
		url.append("&serviceType=" + serviceType);
		url.append("&orderId=" + orderId);
		url.append("&companyId=" + companyId);
		url.append("&parentId=" + parentId);
		try {
			ServiceCallResult result = HttpUtil.issuePostRequest(url.toString(), null, userId);
			finalResult = JsonUtil.mapToObject(result.getResponseString(), ApiCallResult.class);
		} catch (IOException e) {
			logger.error("Error createDraftFromPreviousOrder!"+ExceptionUtils.getStackTrace(e));
			finalResult.setMessage("Exception: " + e.toString());
		}
		return finalResult;
	}

	@Override
	public ApiCallResult createOrderByDraft(String userId, String draftId, String companyId, String parentId) {
		StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
		ApiCallResult finalResult = new ApiCallResult();
		url.append("/api/audit/createOrder");
		url.append("?userId=" + userId);
		url.append("&draftId=" + draftId);
		url.append("&companyId=" + companyId);
		url.append("&parentId=" + parentId);
		try {
			ServiceCallResult result = HttpUtil.issuePostRequest(url.toString(), null, draftId);
			finalResult = JsonUtil.mapToObject(result.getResponseString(), ApiCallResult.class);
		} catch (IOException e) {
			logger.error("Error createOrderByDraft!"+ExceptionUtils.getStackTrace(e));
			finalResult.setMessage("Exception: " + e.toString());
		}
		return finalResult;
	}

	@Override
	public ApiCallResult editOrder(String userId, String orderId, String companyId, String parentId) {
		StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
		ApiCallResult finalResult = new ApiCallResult();
		url.append("/api/audit/editOrder");
		url.append("?userId=" + userId);
		url.append("&orderId=" + orderId);
		url.append("&companyId=" + companyId);
		url.append("&parentId=" + parentId);
		try {
			ServiceCallResult result = HttpUtil.issueGetRequest(url.toString(), null);
			finalResult = JsonUtil.mapToObject(result.getResponseString(), ApiCallResult.class);
		} catch (IOException e) {
			logger.error("Error editOrder!"+ExceptionUtils.getStackTrace(e));
			finalResult.setMessage("Exception: " + e.toString());
		}
		return finalResult;
	}

	@Override
	public ApiCallResult getOrderDetail(String userId, String orderId) {
		StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
		ApiCallResult finalResult = new ApiCallResult();
		url.append("/api/audit/getOrder?userId=").append(userId).append("&orderId=").append(orderId);
		try {
			ServiceCallResult result = HttpUtil.issueGetRequest(url.toString(), null);
			finalResult = JsonUtil.mapToObject(result.getResponseString(), ApiCallResult.class);
		} catch (IOException e) {
			logger.error("Error getOrderDetail!"+ExceptionUtils.getStackTrace(e));
			finalResult.setMessage("Exception: " + e.toString());
		}
		return finalResult;
	}

	@Override
	public ApiCallResult saveOrderByDraft(String userId, String draftId, String companyId, String parentId) {
		StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
		ApiCallResult finalResult = new ApiCallResult();
		url.append("/api/audit/reAudit");
		url.append("?userId=" + userId);
		url.append("&draftId=" + draftId);
		url.append("&companyId=" + companyId);
		url.append("&parentId=" + parentId);
		try {
			ServiceCallResult result = HttpUtil.issueGetRequest(url.toString(), null);
			finalResult = JsonUtil.mapToObject(result.getResponseString(), ApiCallResult.class);
		} catch (IOException e) {
			logger.error("Error saveOrderByDraft!"+ExceptionUtils.getStackTrace(e));
			finalResult.setMessage("Exception: " + e.toString());
		}
		return finalResult;
	}

}
