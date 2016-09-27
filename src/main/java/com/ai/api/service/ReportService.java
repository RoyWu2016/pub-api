package com.ai.api.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.ai.commons.beans.PageBean;
import com.ai.commons.beans.PageParamBean;
import com.ai.commons.beans.psi.report.ApprovalCertificateBean;
import com.ai.commons.beans.psi.report.ClientReportSearchBean;
import com.ai.commons.beans.report.ReportSearchCriteriaBean;
import com.ai.commons.beans.report.ReportsForwardingBean;

/**
 * Created by yan on 2016/7/25.
 */
public interface ReportService {
    PageBean<ClientReportSearchBean> getPSIReports(String useId, PageParamBean paramBean);
    boolean forwardReports(ReportsForwardingBean reportsForwardingBean);
    boolean undoDecision(String userId,String reportDetailId);
    ApprovalCertificateBean getApprovalCertificate(String userId, String productId, String certType);
    boolean confirmApprovalCertificate(String userId,ApprovalCertificateBean cert);
    List<String> getUserReportPdfInfo(String userId, String reportId);
    boolean downloadPDF(String reportId,String fileName,HttpServletResponse httpResponse);
    boolean exportReports(ReportSearchCriteriaBean criteria,HttpServletResponse httpResponse);
	ApprovalCertificateBean getReferenceApproveCertificate(String userId, String referenceId,String certType);
}
