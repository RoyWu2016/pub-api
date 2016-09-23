package com.ai.api.dao;

import com.ai.commons.beans.PageBean;
import com.ai.commons.beans.PageParamBean;
import com.ai.commons.beans.psi.report.ClientReportSearchBean;
import com.ai.commons.beans.report.ReportPdfFileInfoBean;
import com.ai.commons.beans.report.ReportSearchCriteriaBean;
import com.ai.commons.beans.report.ReportSearchResultBean;
import com.ai.commons.beans.report.ReportsForwardingBean;
import com.ai.commons.beans.report.api.ReportCertificateBean;

import java.io.InputStream;
import java.util.List;

/**
 * Created by yan on 2016/7/25.
 */
public interface ReportDao {
    List<ReportSearchResultBean> getUserReportsByCriteria(ReportSearchCriteriaBean criteria);
    PageBean<ClientReportSearchBean> getPSIReports(String userId, PageParamBean paramBean);
    boolean forwardReports(ReportsForwardingBean reportsForwardingBean);
    boolean undoDecision(String login,String reportDetailId);
    ReportCertificateBean getApprovalCertificate(String reportId, String login, String certType, String reference);
    boolean confirmApprovalCertificate(ReportCertificateBean reportCertificateBean,String login);
    List<String> getUserReportPdfInfo(String userId, String login, String reportId);
    InputStream downloadPDF(String reportId, String fileName);
    InputStream exportReports(ReportSearchCriteriaBean criteria);
}
