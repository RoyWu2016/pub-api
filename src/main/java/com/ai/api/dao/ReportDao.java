package com.ai.api.dao;

import com.ai.commons.beans.report.ReportSearchCriteriaBean;
import com.ai.commons.beans.report.ReportSearchResultBean;
import com.ai.commons.beans.report.ReportsForwardingBean;

import java.util.List;

/**
 * Created by yan on 2016/7/25.
 */
public interface ReportDao {
    List<ReportSearchResultBean> getUserReportsByCriteria(ReportSearchCriteriaBean criteria);
    boolean forwardReports(ReportsForwardingBean reportsForwardingBean);
    boolean undoDecision(String login,String reportDetailId);
}
