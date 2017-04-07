package com.ai.api.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;

import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.PageBean;
import com.ai.commons.beans.psi.report.ApprovalCertificateBean;
import com.ai.commons.beans.psi.report.ClientReportSearchBean;
import com.ai.commons.beans.report.ReportsForwardingBean;

/**
 * Created by Henry Yue on 2016/7/25.
 */
public interface Report {
	ResponseEntity<ApiCallResult> getAuditReports(String userId, String startDate, String endDate, String keywords,
			Integer pageNumber, Integer pageSize);

	ResponseEntity<PageBean<ClientReportSearchBean>> getPSIReports(String userId, String startDate, String endDate,
			String keywords, Integer pageNumber, Integer pageSize);

	ResponseEntity<ApprovalCertificateBean> getApprovalCertificate(String userId, String productId, String certType);

	ResponseEntity<String> confirmApprovalCertificate(String userId, ApprovalCertificateBean cert);

	ResponseEntity<List<String>> getUserReportPdfInfo(String userId, String reportId);

	ResponseEntity<String> downloadPDF(String userId, String reportId,String sessionId, String verifiedCode, String fileName,
			HttpServletResponse httpResponse);

	ResponseEntity<Map<String, String>> exportReports(String userId, String starts, String ends,
			HttpServletResponse httpResponse);

	ResponseEntity<ApprovalCertificateBean> getReferenceApproveCertificate(String userId, String referenceId,
			String certType);

	ResponseEntity<Boolean> undoDecisionForReport(String userId, String productId);

	ResponseEntity<Boolean> undoDecisionForReference(String userId, String referenceId);

	ResponseEntity<String> forwardReports(String userId, String reportIds, ReportsForwardingBean reportsForwardingBean);

//	ResponseEntity<ApiCallResult> downloadPDFBase64(String userId, String productId, String fileName,
//			HttpServletResponse httpResponse);

	ResponseEntity<ApiCallResult> getPDFCertificate(String userId, String productId, HttpServletResponse httpResponse);

	ResponseEntity<String> forwardedAuditReports(String userId, String reportIds,
			ReportsForwardingBean reportsForwardingBean);

	ResponseEntity<ApiCallResult> exportAuditReports(String userId, String start, String end);

	ResponseEntity<ApiCallResult> getAuditReportPDFInfo(String userId,String orderId);
}
