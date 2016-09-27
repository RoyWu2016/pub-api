package com.ai.api.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;

import com.ai.commons.beans.PageBean;
import com.ai.commons.beans.psi.report.ApprovalCertificateBean;
import com.ai.commons.beans.psi.report.ClientReportSearchBean;
import com.ai.commons.beans.report.ReportsForwardingBean;

/**
 * Created by Henry Yue on 2016/7/25.
 */
public interface Report {
    ResponseEntity<PageBean<ClientReportSearchBean>> getPSIReports(String userId,String startDate,String endDate,String keywords,Integer pageNumber,Integer pageSize);
    ResponseEntity<String> forwardReports(String userId,String ids,ReportsForwardingBean reportsForwardingBean);
//    ResponseEntity<String> undoDecision(String userId,String id);
    ResponseEntity<ApprovalCertificateBean> getApprovalCertificate(String userId, String productId, String certType);
    ResponseEntity<String> confirmApprovalCertificate(String userId,ApprovalCertificateBean cert);
    ResponseEntity<List<String>> getUserReportPdfInfo(String userId, String reportId);
    ResponseEntity<String> downloadPDF(String userId,String reportId,String fileName,HttpServletResponse httpResponse);
	ResponseEntity<String> exportReports(String userId,String starts,String ends,HttpServletResponse httpResponse);
	ResponseEntity<ApprovalCertificateBean> getReferenceApproveCertificate(String userId,String referenceId, String certType);
	ResponseEntity<Boolean> undoDecisionForReport(String userId,String productId);
}
