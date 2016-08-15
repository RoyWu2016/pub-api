package com.ai.api.controller;

import com.ai.commons.beans.report.ReportPdfFileInfoBean;
import com.ai.commons.beans.report.ReportSearchResultBean;
import com.ai.commons.beans.report.ReportsForwardingBean;
import com.ai.commons.beans.report.api.ReportCertificateBean;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Henry Yue on 2016/7/25.
 */
public interface Report {
    ResponseEntity<List<ReportSearchResultBean>> getUserReportsByCriteria(String userId,String orderTypeArray,Integer pageNumber,String archived,String starts,String ends,String keywords);
    ResponseEntity<String> forwardReports(String userId,String ids,ReportsForwardingBean reportsForwardingBean);
    ResponseEntity<String> undoDecision(String userId,String id);
    ResponseEntity<ReportCertificateBean> getApprovalCertificate(String userId, String reportId, String certType, String reference);
    ResponseEntity<String> confirmApprovalCertificate(String userId,String reportId,ReportCertificateBean reportCertificateBean);
    ResponseEntity<List<String>> getUserReportPdfInfo(String userId, String reportId);
    ResponseEntity<String> downloadPDF(String userId,String reportId,String fileName,HttpServletResponse httpResponse);
	ResponseEntity<String> exportReports(String userId,String starts,String ends,HttpServletResponse httpResponse);
}
