package com.ai.api.dao.impl;

import java.io.IOException;
import java.util.Map;

import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.DraftDao;
import com.ai.commons.HttpUtil;
import com.ai.commons.beans.ServiceCallResult;
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
 * Creation Date   : 2016/8/1 16:49
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
public class DraftDaoImpl implements DraftDao {

	private static final Logger logger = LoggerFactory.getLogger(DraftDaoImpl.class);

	@Autowired
	@Qualifier("serviceConfig")
	private ServiceConfig config;

	@Override
	public boolean deleteDrafts(Map<String, String> params) {
		String url = config.getMwServiceUrl() + "/service/draft/delete";
		try {
			ServiceCallResult result = HttpUtil.issuePostRequest(url, null, params);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {


			} else {
				logger.error("delete drafts from middleware error: " + result.getStatusCode() +
						", " + result.getResponseString());
			}

		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return false;
	}
}
