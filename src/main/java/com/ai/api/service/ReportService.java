package com.ai.api.service;

import com.ai.commons.beans.report.ReportSearchCriteriaBean;
import com.ai.commons.beans.report.ReportSearchResultBean;

import java.util.List;

/**
 * Created by yan on 2016/7/25.
 */
public interface ReportService {
    List<ReportSearchResultBean> getUserReportsByCriteria(ReportSearchCriteriaBean criteria);
}
