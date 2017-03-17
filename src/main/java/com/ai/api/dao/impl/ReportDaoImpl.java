package com.ai.api.dao.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ai.commons.beans.*;
import com.ai.commons.beans.audit.AuditReportsSearchBean;

import com.ai.commons.beans.fileservice.ApiFileMetaBean;
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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.ReportDao;
import com.ai.commons.HttpUtil;
import com.ai.commons.JsonUtil;
import com.ai.commons.beans.psi.report.ApprovalCertificateBean;
import com.ai.commons.beans.psi.report.ClientReportSearchBean;
import com.ai.commons.beans.report.ReportsForwardingBean;
import com.ai.commons.beans.sync.LotusSyncBean;
import com.alibaba.fastjson.JSON;

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
				ApprovalCertificateBean bean = (ApprovalCertificateBean) JSON.parseObject(result.getResponseString(),
						ApprovalCertificateBean.class);
				String name = bean.getApprover().trim();
				bean.setApprover(name);
				return bean;
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
	public ApiCallResult getAuditReports(String userId, String companyId, String parentId, PageParamBean criteria) {
		ApiCallResult finalResult = new ApiCallResult();
		try {
			logger.info("getAuditReports json before encoding: " + JsonUtil.mapToJson(criteria));
			String param = URLEncoder.encode(JsonUtil.mapToJson(criteria), "utf-8");
			logger.info("getAuditReports json after encoding: " + param);
			StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
			url.append("/audit/report/api/report-list");
			url.append("?userId=" + userId);
			url.append("&companyId=" + companyId);
			url.append("&parentId=" + parentId);
			url.append("&param=" + param);
			ServiceCallResult result = HttpUtil.issueGetRequest(url.toString(), null);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				finalResult.setContent(JSON.parseObject(result.getResponseString(), PageBean.class));
			} else {
				logger.error("getAuditReports from psi-service error: " + result.getStatusCode() + " || "
						+ result.getResponseString());
				finalResult.setMessage("getAuditReports from psi-service error: " + result.getStatusCode() + " || "
						+ result.getResponseString());
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			finalResult.setMessage("Exception: " + e.toString());
		}
		return finalResult;

	}

	@Override
	public PageBean<ClientReportSearchBean> getPSIReports(String userId, String companyId, String parentId,
			PageParamBean criteria) {
		// TODO Auto-generated method stub
		try {
			logger.info("getPSIReports json before encoding: " + JsonUtil.mapToJson(criteria));
			String param = URLEncoder.encode(JsonUtil.mapToJson(criteria), "utf-8");
			logger.info("getPSIReports json after encoding: " + param);
			StringBuilder url = new StringBuilder(config.getPsiServiceUrl() + "/report/api/report-list");
			url.append("?userId=" + userId).append("&companyId=" + companyId).append("&parentId=" + parentId)
					.append("&param=" + param);
			logger.info("requesting !!! Url:" + url.toString());
			ServiceCallResult result = HttpUtil.issueGetRequest(url.toString(), null);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				return JSON.parseObject(result.getResponseString(), PageBean.class);
			} else {
				logger.error("searchClientReports from psi-service error: " + result.getStatusCode() + ", "
						+ result.getResponseString());
			}
		} catch (Exception e) {
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
	public boolean clientForwardReport(ReportsForwardingBean reportsForwardingBean, String companyId, String parentId,
			String userId, String reportIds) {
		StringBuilder url = new StringBuilder(config.getPsiServiceUrl() + "/report/api/forward-reports");
		try {
			reportsForwardingBean.setMessage(URLEncoder.encode(reportsForwardingBean.getMessage().trim(), "UTF-8"));
		} catch (Exception e) {
			reportsForwardingBean.getMessage().trim().replace(" ", "%20");
		}
		url.append("?productIds=").append(reportIds).append("&to=").append(reportsForwardingBean.getTo()).append("&cc=")
				.append(reportsForwardingBean.getCc()).append("&bcc=").append(reportsForwardingBean.getBcc())
				.append("&message=").append(reportsForwardingBean.getMessage()).append("&userId=").append(userId)
				.append("&companyId=").append(companyId).append("&parentId=").append(parentId);
		try {
			logger.info("requesting url: " + url.toString());
			ServiceCallResult result = HttpUtil.issuePostRequest(url.toString(), null, new HashMap<>());
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				return true;
			} else {
				logger.error("forward reports from psi-service error: " + result.getStatusCode() + ", "
						+ result.getResponseString());
				return false;
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return false;
		}
	}

	@Override
	public List<LotusSyncBean> listAllSyncObjByOracleId(String productId, String reportDetail) {
		// TODO Auto-generated method stub
		StringBuilder url = new StringBuilder(config.getPsiServiceUrl() + "/lotus-sync/list-by-oracle-id/" + productId);
		url.append("?syncObj=" + reportDetail);
		logger.info("requesting: " + url.toString());
		GetRequest request = GetRequest.newInstance().setUrl(url.toString());
		try {
			ServiceCallResult result = HttpUtil.issueGetRequest(request);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				return JSON.parseObject(result.getResponseString(), List.class);
			} else {
				logger.error("listAllSyncObjByOracleId from psi-service error: " + result.getStatusCode() + ", "
						+ result.getResponseString());
				return null;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getPDFCertificate(String lotusId) {
		// TODO Auto-generated method stub
		// StringBuilder url = new StringBuilder(config.getMwServiceUrl() +
		// "/report/getPDFCertificate?reportId=" + lotusId);
		// logger.info("requesting: " + url.toString());
		// InputStream inputStream = null;
		// try {
		// HttpClient httpclient = HttpClients.createDefault();
		// HttpGet httpget = new HttpGet(url.toString());
		// HttpResponse response = httpclient.execute(httpget);
		// HttpEntity entity = response.getEntity();
		// inputStream = entity.getContent();
		// return inputStream;
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		StringBuilder url = new StringBuilder(config.getLotusApiUrl() + lotusId);
		logger.info("requesting: " + url.toString());
		GetRequest request = GetRequest.newInstance().setUrl(url.toString());
		try {
			ServiceCallResult result = HttpUtil.issueGetRequest(request);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				return result.getResponseString();
			} else {
				logger.error("listAllSyncObjByOracleId from psi-service error: " + result.getStatusCode() + ", "
						+ result.getResponseString());
				return null;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean forwardedAuditReports(ReportsForwardingBean reportsForwardingBean, String companyId, String parentId,
			String userId, String reportIds) {
		// TODO Auto-generated method stub
		StringBuilder url = new StringBuilder(config.getPsiServiceUrl() + "/audit/report/api/forward-reports");
		try {
			reportsForwardingBean.setMessage(URLEncoder.encode(reportsForwardingBean.getMessage().trim(), "UTF-8"));
		} catch (Exception e) {
			reportsForwardingBean.getMessage().trim().replace(" ", "%20");
		}
		url.append("?orderIds=").append(reportIds).append("&to=").append(reportsForwardingBean.getTo()).append("&cc=")
				.append(reportsForwardingBean.getCc()).append("&bcc=").append(reportsForwardingBean.getBcc())
				.append("&message=").append(reportsForwardingBean.getMessage()).append("&userId=").append(userId)
				.append("&companyId=").append(companyId).append("&parentId=").append(parentId);
		try {
			logger.info("requesting url: " + url.toString());
			ServiceCallResult result = HttpUtil.issuePostRequest(url.toString(), null, new HashMap<>());
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				return true;
			} else {
				logger.error("forward reports from psi-service error: " + result.getStatusCode() + ", "
						+ result.getResponseString());
				return false;
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return false;
		}
	}

	@Override
	public PageBean<AuditReportsSearchBean> exportAuditReport(String userId, String companyId, String parentId,
			PageParamBean criteriaBean) {
		// TODO Auto-generated method stub
		try {
			String parmStr = URLEncoder.encode(JsonUtil.mapToJson(criteriaBean), "utf-8");
			StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
			url.append("/audit/report/api/report-list");
			url.append("?userId=" + userId);
			url.append("&companyId=" + companyId);
			url.append("&parentId=" + parentId);
			url.append("&param=" + parmStr);
			ServiceCallResult result = HttpUtil.issueGetRequest(url.toString(), null);
			return JsonUtil.mapToObject(result.getResponseString(), PageBean.class);
		} catch (IOException e) {
			logger.error("Error exportAuditReport!" + ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

//	@Override
//	public ApiCallResult getAuditReportPDFInfo(String userId, String companyId, String parentId,String orderId){
//		StringBuilder url = new StringBuilder(config.getPsiServiceUrl())
//				.append("/audit/report/api/list-all-final-report")
//				.append("?userId=").append(userId)
//				.append("&companyId=").append(companyId)
//				.append("&parentId=").append(parentId)
//				.append("&orderId=").append(orderId);
//		ApiCallResult finalResult = new ApiCallResult();
//		try {
//			ServiceCallResult result = HttpUtil.issueGetRequest(url.toString(),null);
//			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
//				List<FileMetaBean> fileMetaBeanList = JSON.parseArray(result.getResponseString(),FileMetaBean.class);
//				List<ApiFileMetaBean> returnList = new ArrayList<>();
//				if (null!=fileMetaBeanList&&fileMetaBeanList.size()>0){
//					for (FileMetaBean f:fileMetaBeanList){
//						returnList.add(new ApiFileMetaBean(f));
//					}
//				}
//				finalResult.setContent(returnList);
//			} else {
//				logger.info("getAuditReportPDFInfo failed from psi-service!!!");
//				finalResult.setMessage("getAuditReportPDFInfo failed from psi-service!!! code["
//						+ result.getStatusCode() + "] msg:" + result.getResponseString());
//			}
//		} catch (Exception e) {
//			logger.error("error Exception!", e);
//			finalResult.setMessage("error Exception!" + e);
//		}
//		return finalResult;
//	}
}
