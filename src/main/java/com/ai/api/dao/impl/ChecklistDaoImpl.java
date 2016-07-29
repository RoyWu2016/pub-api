package com.ai.api.dao.impl;

import java.io.IOException;
import java.util.List;

import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.ChecklistDao;
import com.ai.commons.HttpUtil;
import com.ai.commons.JsonUtil;
import com.ai.commons.StringUtils;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.checklist.ChecklistSearchCriteriaBean;
import com.ai.commons.beans.checklist.ChecklistSearchResultBean;
import com.ai.commons.beans.order.OrderSearchResultBean;
import com.ai.commons.beans.report.ReportSearchCriteriaBean;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
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
}
