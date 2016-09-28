package com.ai.api.dao.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.ReportDao;
import com.ai.api.util.FTPUtil;
import com.ai.commons.HttpUtil;
import com.ai.commons.beans.GetRequest;
import com.ai.commons.beans.PageBean;
import com.ai.commons.beans.PageParamBean;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.psi.report.ApprovalCertificateBean;
import com.ai.commons.beans.psi.report.ClientReportSearchBean;
import com.ai.commons.beans.report.ReportSearchCriteriaBean;
import com.ai.commons.beans.report.ReportsForwardingBean;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by yan on 2016/7/25.
 */
@Component
public class ReportDaoImpl implements ReportDao {

	private static final Logger logger = LoggerFactory.getLogger(ReportDaoImpl.class);

	@Autowired
	@Qualifier("serviceConfig")
	private ServiceConfig config;

	@Override
	public PageBean<ClientReportSearchBean> getPSIReports(String userId, PageParamBean paramBean) {
		String url = config.getPsiServiceUrl() + "/report/api/report-list";
		String paramStr = JSON.toJSONString(paramBean);
		try {
			url = url + "?userId=" + userId + "&companyId=companyIdNull&parentId=parentIdNull&param="
					+ URLEncoder.encode(paramStr, "UTF-8");
			GetRequest request = GetRequest.newInstance().setUrl(url);
			logger.info("get!!! Url:" + url);
			ServiceCallResult result = HttpUtil.issueGetRequest(request);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				JSONObject jsonObject = JSON.parseObject(result.getResponseString());
				String reportStr = jsonObject.getString("pageItems");
				List<ClientReportSearchBean> reportSearchBeanList = JSON.parseArray(reportStr,
						ClientReportSearchBean.class);
				PageBean<ClientReportSearchBean> pageBean = JSON.parseObject(result.getResponseString(),
						PageBean.class);
				pageBean.setPageItems(reportSearchBeanList);
				return pageBean;
			} else {
				logger.error("get psi-reports from psi-service error: " + result.getStatusCode() + ", "
						+ result.getResponseString());
			}
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public ApprovalCertificateBean getApprovalCertificate(String userId, String companyId, String parentId,
			String productId, String certType) {
		StringBuilder url = new StringBuilder(
				config.getPsiServiceUrl() + "/report/api/approval-certificate/report/" + productId);
		try {
			url.append("?approveOrReject=" + certType);
			url.append("&userId=" + userId);
			url.append("&companyId=" + companyId);
			url.append("&parentId=" + parentId);
			logger.info("post !!! Url:" + url);
			ServiceCallResult result = HttpUtil.issuePostRequest(url.toString(), null, certType);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				return JSON.parseObject(result.getResponseString(), ApprovalCertificateBean.class);
			} else {
				logger.error("getApprovalCertificate from psi-service error: " + result.getStatusCode() + ", "
						+ result.getResponseString());
				return null;
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return null;
		}
	}

	@Override
	public boolean confirmApprovalCertificate(String userId, String companyId, String parentId,
			ApprovalCertificateBean cert) {
		StringBuilder url = new StringBuilder(config.getPsiServiceUrl() + "/report/api/confirm-approve-reject");
		try {
			url.append("?userId=" + userId);
			url.append("&companyId=" + companyId);
			url.append("&parentId=" + parentId);
			logger.info("post !!! Url:" + url);
			ServiceCallResult result = HttpUtil.issuePostRequest(url.toString(), null, cert);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				return true;
			} else {
				logger.error("confirmApprovalCertificate from psi-service error: " + result.getStatusCode() + ", "
						+ result.getResponseString());
				return false;
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return false;
		}
	}

	@Override
	public List<String> getUserReportPdfInfo(String userId, String login, String reportId) {
		String url = config.getReportServiceUrl() + "/list-pdf-names/" + reportId;
		try {
			GetRequest request = GetRequest.newInstance().setUrl(url);
			ServiceCallResult result = HttpUtil.issueGetRequest(request);
			List<String> pdfList = JSON.parseArray(result.getResponseString(), String.class);
			return pdfList;
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public InputStream downloadPDF(String reportId, String fileName) {
		InputStream inputStream = null;
		try {
			String url = config.getReportServiceUrl() + "/attachment/download-pdf/" + reportId + "?fileName="
					+ URLEncoder.encode(fileName, "UTF-8");
			HttpClient httpclient = HttpClients.createDefault();
			HttpGet httpget = new HttpGet(url);
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			inputStream = entity.getContent();
		} catch (Exception e) {
			logger.error("ERROR!!! downloadPDF", e);
		}
		return inputStream;

	}

	@Override
	public InputStream exportReports(ReportSearchCriteriaBean criteria) {
		String url = config.getMwServiceUrl() + "/service/report/export";
		// String url = "http://127.0.0.1:8888/service/report/export";
		try {
			logger.info("post url:" + url);
			logger.info(criteria.toString());
			ServiceCallResult result = HttpUtil.issuePostRequest(url, null, criteria);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				logger.info("request OK!");
				String remotePath = "/CACHE/";
				String fileName = result.getResponseString();
				// String host = config.getMwFTPHost();
				String host = "";
				int port = 21;
				// String username = config.getMwFTPUsername();
				// String password = config.getMwFTPPassword();
				String username = "";
				String password = "";
				String tempPath = "/tmp/";
				logger.info(remotePath);
				logger.info(fileName);
				logger.info(host + ":" + port);
				logger.info(username + " || " + password);
				logger.info(tempPath);
				boolean b = FTPUtil.downloadFile(host, port, username, password, remotePath, fileName, tempPath);
				if (b) {
					logger.info("success download File to /tmp ");
					File tempFile = new File(tempPath + fileName);
					InputStream inputStream = new FileInputStream(tempFile);
					return inputStream;
				} else {
					logger.error("ERROR! fail to download file from FTP server. ");
					return null;
				}
			} else {
				logger.error("get reports from middleware error: " + result.getStatusCode() + ", "
						+ result.getResponseString());
			}
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;

	}

	@Override
	public ApprovalCertificateBean getReferenceApproveCertificate(String userId, String referenceId, String companyId,
			String parentId, String certType) {
		// TODO Auto-generated method stub
		StringBuilder url = new StringBuilder(
				config.getPsiServiceUrl() + "/report/api/approval-certificate/reference/" + referenceId);
		try {
			url.append("?approveOrReject=" + certType);
			url.append("&userId=" + userId);
			url.append("&companyId=" + companyId);
			url.append("&parentId=" + parentId);
			logger.info("post !!! Url:" + url);
			ServiceCallResult result = HttpUtil.issuePostRequest(url.toString(), null, certType);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				return JSON.parseObject(result.getResponseString(), ApprovalCertificateBean.class);
			} else {
				logger.error("getReferenceApproveCertificate from psi-service error: " + result.getStatusCode() + ", "
						+ result.getResponseString());
				return null;
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public boolean undoDecisionForReport(String userId, String productId, String companyId, String parentId) {
		// TODO Auto-generated method stub
		StringBuilder url = new StringBuilder(
				config.getPsiServiceUrl() + "/report/api/undo-decision/report/" + productId);
		try {
			url.append("?userId=" + userId);
			url.append("&companyId=" + companyId);
			url.append("&parentId=" + parentId);
			logger.info("post !!! Url:" + url);
			ServiceCallResult result = HttpUtil.issuePostRequest(url.toString(), null, new HashMap<>());
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				return true;
			} else {
				logger.error("getReferenceApproveCertificate from psi-service error: " + result.getStatusCode() + ", "
						+ result.getResponseString());
				return false;
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

	@Override
	public boolean undoDecisionForReference(String userId, String referenceId, String companyId, String parentId) {
		// TODO Auto-generated method stub
		StringBuilder url = new StringBuilder(
				config.getPsiServiceUrl() + "/report/api/undo-decision/reference/" + referenceId);
		try {
			url.append("?userId=" + userId);
			url.append("&companyId=" + companyId);
			url.append("&parentId=" + parentId);
			logger.info("post !!! Url:" + url);
			ServiceCallResult result = HttpUtil.issuePostRequest(url.toString(), null, new HashMap<>());
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				return true;
			} else {
				logger.error("getReferenceApproveCertificate from psi-service error: " + result.getStatusCode() + ", "
						+ result.getResponseString());
				return false;
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

	@Override
	public boolean clientForwardReport(ReportsForwardingBean reportsForwardingBean, String companyId, String parentId,String userId) {
		// TODO Auto-generated method stub
		StringBuilder url = new StringBuilder(config.getPsiServiceUrl() + "/report/api/forward-reports");
		url.append("?productIds=" + reportsForwardingBean.getIds())
			.append("&to=" + reportsForwardingBean.getTo())
			.append("&cc=" + reportsForwardingBean.getCc())
			.append("&bcc=" + reportsForwardingBean.getBcc())
			.append("&message=" + reportsForwardingBean.getMessage())
			.append("&userId=" + userId)
			.append("&companyId=" + companyId)
			.append("&parentId=" + parentId);
		try {
			logger.info("requesting url: " + url.toString());
			ServiceCallResult result = HttpUtil.issuePostRequest(url.toString(), null, new HashMap<>());
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				return true;
			} else {
				logger.error("forward reports from middleware error: " + result.getStatusCode() + ", "+ result.getResponseString());
				return false;
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return false;
		}
	}
}
