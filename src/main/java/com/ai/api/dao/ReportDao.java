package com.ai.api.dao;

import com.ai.commons.beans.report.ReportPdfFileInfoBean;
import com.ai.commons.beans.report.ReportSearchCriteriaBean;
import com.ai.commons.beans.report.ReportSearchResultBean;

import java.util.List;

/**
 * Created by yan on 2016/7/25.
 */
public interface ReportDao {
    List<ReportSearchResultBean> getUserReportsByCriteria(ReportSearchCriteriaBean criteria);
    List<ReportPdfFileInfoBean> getUserReportPdfInfo(String userId, String reportId);
}
