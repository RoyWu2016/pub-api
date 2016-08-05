package com.ai.api.dao.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.ChecklistDao;
import com.ai.commons.HttpUtil;
import com.ai.commons.beans.GetRequest;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.checklist.ChecklistDetailBean;
import com.ai.commons.beans.checklist.ChecklistSearchCriteriaBean;
import com.ai.commons.beans.checklist.ChecklistSearchResultBean;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/***************************************************************************
 * <PRE>
 * Project Name    : Public-API
 * <p>
 * Package Name    : com.ai.api.dao.impl
 * <p>
 * Creation Date   : 2016/7/28 10:52
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


@Component
public class ChecklistDaoImpl implements ChecklistDao {

	private static final Logger logger = LoggerFactory.getLogger(ChecklistDaoImpl.class);

	@Autowired
	@Qualifier("serviceConfig")
	private ServiceConfig config;

	@Override
	public List<ChecklistSearchResultBean> searchChecklist(ChecklistSearchCriteriaBean criteria) {
		String url = config.getMwServiceUrl() + "/service/checklist/private";
		try {
			ServiceCallResult result = HttpUtil.issuePostRequest(url, null, criteria);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {

				return JSON.parseArray(result.getResponseString(),ChecklistSearchResultBean.class);

			} else {
				logger.error("searchChecklist from middleware error: " + result.getStatusCode() +
						", " + result.getResponseString());
			}

		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public List<ChecklistSearchResultBean> searchPublicChecklist(ChecklistSearchCriteriaBean criteria) {
		String url = config.getMwServiceUrl() + "/service/checklist/public";
		try {
			ServiceCallResult result = HttpUtil.issuePostRequest(url, null, criteria);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {

				return JSON.parseArray(result.getResponseString(),ChecklistSearchResultBean.class);

			} else {
				logger.error("searchPublicChecklist from middleware error: " + result.getStatusCode() +
						", " + result.getResponseString());
			}

		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public String createChecklist(String login,ChecklistDetailBean checklistDetailBean) {
		String url = config.getMwServiceUrl() + "/service/checklist/create";
		try {
			Map<String,Object> dataMap = new HashMap<>();
			dataMap.put("login",login);
			dataMap.put("checklistDetailBean",checklistDetailBean);
			logger.debug("create!!! Url:"+url+" login:"+login+" checklistDetailBean:"+checklistDetailBean.toString());
			ServiceCallResult result = HttpUtil.issuePostRequest(url, null, dataMap);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				return result.getResponseString();
			} else {
				logger.error("createChecklist from middleware error: " + result.getStatusCode() +
						", " + result.getResponseString());
			}

		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public String updateChecklist(String login,ChecklistDetailBean checklistDetailBean) {
		String url = config.getMwServiceUrl() + "/service/checklist/update";
		try {
			Map<String,Object> dataMap = new HashMap<>();
			dataMap.put("login",login);
			dataMap.put("checklistDetailBean",checklistDetailBean);
			logger.debug("update!!! Url:"+url+" login:"+login+" checklistDetailBean:"+checklistDetailBean.toString());
			ServiceCallResult result = HttpUtil.issuePostRequest(url, null, dataMap);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				return result.getResponseString();
			} else {
				logger.error("updateChecklist from middleware error: " + result.getStatusCode() +
						", " + result.getResponseString());
			}

		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public ChecklistDetailBean getChecklist(String login,String checklistId) {
		String url = config.getMwServiceUrl() + "/service/checklist/get/"+checklistId+"?login="+login;
		try {
			GetRequest request = GetRequest.newInstance().setUrl(url);
			logger.debug("get!!! Url:"+url+" login:"+login+" checklistId:"+checklistId);
			ServiceCallResult result = HttpUtil.issueGetRequest(request);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {

				return (ChecklistDetailBean)JSON.parse(result.getResponseString());

			} else {
				logger.error("GET Checklist from middleware error: " + result.getStatusCode() +
						", " + result.getResponseString());
			}

		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public  boolean deleteChecklist(String login,String ids) {
		String url = config.getMwServiceUrl() + "/service/checklist/delete";
		try {
			Map<String,Object> dataMap = new HashMap<>();
			dataMap.put("login",login);
			dataMap.put("ids",ids);
			logger.debug("delete!!! post Url:"+url+" login:"+login+" ids:"+ids);
			ServiceCallResult result = HttpUtil.issuePostRequest(url, null, dataMap);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
					return true;
			} else {
				logger.error("updateChecklist from middleware error: " + result.getStatusCode() +
						", " + result.getResponseString());
			}

		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return false;
	}
}
