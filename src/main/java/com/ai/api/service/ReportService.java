package com.ai.api.service;

import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.PageBean;
import com.ai.commons.beans.PageParamBean;
import com.ai.commons.beans.psi.report.ApprovalCertificateBean;
import com.ai.commons.beans.psi.report.ClientReportSearchBean;
import com.ai.commons.beans.report.ReportsForwardingBean;
import com.ai.commons.beans.sync.LotusSyncBean;

/**
 * Created by yan on 2016/7/25.
 */
public interface ReportService {
	ApiCallResult getAuditReports(String useId, PageParamBean paramBean);

	PageBean<ClientReportSearchBean> getPSIReports(String useId, PageParamBean paramBean);

	ApprovalCertificateBean getApprovalCertificate(String userId, String productId, String certType);

	boolean confirmApprovalCertificate(String userId, ApprovalCertificateBean cert);

	List<String> getUserReportPdfInfo(String userId, String reportId);

	boolean downloadPDF(String reportId, String fileName, HttpServletResponse httpResponse);

	InputStream exportReports(String userId, PageParamBean criteriaBean, String inspectionPeriod);

	ApprovalCertificateBean getReferenceApproveCertificate(String userId, String referenceId, String certType);

	boolean undoDecisionForReport(String userId, String productId);

	boolean undoDecisionForReference(String userId, String referenceId);

	boolean clientForwardReport(ReportsForwardingBean reportsForwardingBean, String userId, String reportIds);

	InputStream downloadPDFBase64(String reportId, String fileName, HttpServletResponse httpResponse);

	List<LotusSyncBean> listAllSyncObjByOracleId(String productId, String reportDetail);

	String getPDFCertificate(String lotusId);
}
