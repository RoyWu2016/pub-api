package com.ai.api.service;

import com.ai.commons.beans.report.ReportPdfFileInfoBean;
import com.ai.commons.beans.report.ReportSearchCriteriaBean;
import com.ai.commons.beans.report.ReportSearchResultBean;
import com.ai.commons.beans.report.ReportsForwardingBean;

import java.util.List;

/**
 * Created by yan on 2016/7/25.
 */
public interface ReportService {
    List<ReportSearchResultBean> getUserReportsByCriteria(ReportSearchCriteriaBean criteria);
    boolean forwardReports(ReportsForwardingBean reportsForwardingBean);
    boolean undoDecision(String userId,String reportDetailId);
    List<ReportPdfFileInfoBean> getUserReportPdfInfo(String userId, String reportId);
}
