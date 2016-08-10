package com.ai.api.controller;

import com.ai.commons.beans.report.ReportPdfFileInfoBean;
import com.ai.commons.beans.report.ReportSearchResultBean;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Created by Henry Yue on 2016/7/25.
 */
public interface Report {
    ResponseEntity<List<ReportSearchResultBean>> getUserReportsByCriteria(String userId,String orderTypeArray,Integer pageNumber,String archived,String starts,String ends,String keywords);
    ResponseEntity<List<ReportPdfFileInfoBean>> getUserReportPdfInfo(String userId, String reportId);
}
