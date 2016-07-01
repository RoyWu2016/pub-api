package com.ai.api.dao.impl;

import java.io.InputStream;

import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.FileDao;
import com.ai.commons.HttpUtil;
import com.ai.commons.JsonUtil;
import com.ai.commons.beans.GetRequest;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.fileservice.FileMetaBean;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/***************************************************************************
 * <PRE>
 * Project Name    : api
 * <p>
 * Package Name    : com.ai.api.dao.impl
 * <p>
 * Creation Date   : 2016/6/30 12:49
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
public class FileDaoImpl implements FileDao {

	private static final Logger logger = LoggerFactory.getLogger(FileDaoImpl.class);

	@Autowired
	@Qualifier("serviceConfig")
	private ServiceConfig config;

	@Override
	public FileMetaBean getFileDetailInfo(String fileId) {
		String url = config.getFileServiceUrl() + "/getFileInfoById?id=" + fileId;
		GetRequest request = GetRequest.newInstance().setUrl(url);
		ServiceCallResult result;
		FileMetaBean fileMetaBean;
		try {
			result = HttpUtil.issueGetRequest(request);
			fileMetaBean = JsonUtil.mapToObject(result.getResponseString(), FileMetaBean.class);
			return fileMetaBean;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public InputStream downloadFile(String fileId) {
		String url = config.getFileServiceUrl() + "/getFile?id=" + fileId;
		InputStream instream = null;
		try {
			HttpClient httpclient = HttpClients.createDefault();
			HttpGet httpget = new HttpGet(url);
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			instream = entity.getContent();
		} catch (Exception e) {
			logger.error("", e);
		}
		return instream;
	}
}
