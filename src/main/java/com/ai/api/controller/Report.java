package com.ai.api.controller;

import com.ai.commons.beans.report.ReportSearchResultBean;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Created by yan on 2016/7/25.
 */
public interface Report {
    ResponseEntity<List<ReportSearchResultBean>> getUserReportsByCriteria(String userId,Integer pageNumber,String archived,String starts,String ends,String keywords);
}
