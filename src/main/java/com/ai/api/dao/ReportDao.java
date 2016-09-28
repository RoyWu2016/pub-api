package com.ai.api.dao;

import java.io.InputStream;
import java.util.List;

import com.ai.commons.beans.PageBean;
import com.ai.commons.beans.PageParamBean;
import com.ai.commons.beans.psi.report.ApprovalCertificateBean;
import com.ai.commons.beans.psi.report.ClientReportSearchBean;
import com.ai.commons.beans.report.ReportSearchCriteriaBean;
import com.ai.commons.beans.report.ReportsForwardingBean;

/**
 * Created by yan on 2016/7/25.
 */
public interface ReportDao {
    PageBean<ClientReportSearchBean> getPSIReports(String userId, PageParamBean paramBean);
    ApprovalCertificateBean getApprovalCertificate(String userId, String companyId, String parentId,String productId, String certType);
    boolean confirmApprovalCertificate(String userId, String companyId, String parentId,ApprovalCertificateBean cert);
    List<String> getUserReportPdfInfo(String userId, String login, String reportId);
    InputStream downloadPDF(String reportId, String fileName);
    InputStream exportReports(ReportSearchCriteriaBean criteria);
	ApprovalCertificateBean getReferenceApproveCertificate(String userId,String referenceId,String companyId,String parentId,String certType);
	boolean undoDecisionForReport(String userId, String productId, String companyId, String parentId);
	boolean undoDecisionForReference(String userId, String referenceId, String companyId, String parentId);
	boolean clientForwardReport(ReportsForwardingBean reportsForwardingBean, String companyId, String parentId);
}
