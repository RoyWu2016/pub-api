package com.ai.api.service;

import com.ai.commons.beans.report.ReportPdfFileInfoBean;
import com.ai.commons.beans.report.ReportSearchCriteriaBean;
import com.ai.commons.beans.report.ReportSearchResultBean;
import com.ai.commons.beans.report.ReportsForwardingBean;
import com.ai.commons.beans.report.api.ReportCertificateBean;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by yan on 2016/7/25.
 */
public interface ReportService {
    List<ReportSearchResultBean> getUserReportsByCriteria(ReportSearchCriteriaBean criteria);
    boolean forwardReports(ReportsForwardingBean reportsForwardingBean);
    boolean undoDecision(String userId,String reportDetailId);
    ReportCertificateBean getApprovalCertificate(String reportId, String userId, String certType, String reference);
    boolean confirmApprovalCertificate(String userId,ReportCertificateBean reportCertificateBean);
    List<String> getUserReportPdfInfo(String userId, String reportId);
    boolean downloadPDF(String reportId,String fileName,HttpServletResponse httpResponse);
    boolean exportReports(ReportSearchCriteriaBean criteria,HttpServletResponse httpResponse);
}
