package com.ai.api.controller;

import com.ai.commons.beans.ApiCallResult;
import org.springframework.http.ResponseEntity;

/**
 * Project Name    : Public-API
 * Package Name    : com.ai.api.controller
 * Creation Date   : 2017/1/4 10:37
 * Author          : Jianxiong.Cai
 * Purpose         : TODO
 * History         : TODO
 */
public interface InspectorController {
    ResponseEntity<ApiCallResult> getAllSummaryDetails(String orderId);
    ResponseEntity<ApiCallResult> getProductDetails(String productId);
    ResponseEntity<ApiCallResult> getIpGuideline(String productId);
    ResponseEntity<ApiCallResult> getAllReportsByInspectorId(String inspectorId);
    ResponseEntity<ApiCallResult> getReportDetail(String orderId,String productId,String orderNumber);
    ResponseEntity<ApiCallResult> getAllReports(String inspectorId);
    ResponseEntity<ApiCallResult> getTestDetails(String productId);
    ResponseEntity<ApiCallResult> getDefectsDetails(String productId);
    ResponseEntity<ApiCallResult> getAllProductsForProtocol();
    ResponseEntity<ApiCallResult> getAllReportsForSupervisor();
}
