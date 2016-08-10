package com.ai.api.controller;

import com.ai.commons.beans.report.ReportSearchResultBean;
import com.ai.commons.beans.report.ReportsForwardingBean;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by yan on 2016/7/25.
 */
public interface Report {
    ResponseEntity<List<ReportSearchResultBean>> getUserReportsByCriteria(String userId,String orderTypeArray,Integer pageNumber,String archived,String starts,String ends,String keywords);
    ResponseEntity<String> forwardReports(String userId,String ids,ReportsForwardingBean reportsForwardingBean);
    ResponseEntity<String> undoDecision(String userId,String id);
}
