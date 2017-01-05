package com.ai.api.controller;

import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.psi.InspResultForm;
import org.springframework.http.ResponseEntity;

/**
 * Project Name    : Public-API
 * Package Name    : com.ai.api.controller
 * Creation Date   : 2017/1/4 15:20
 * Author          : Jianxiong.Cai
 * Purpose         : TODO
 * History         : TODO
 */
public interface InspectorResultController {
    ResponseEntity<ApiCallResult> saveResult(String productId, InspResultForm aiResultForm,String username);
    ResponseEntity<ApiCallResult> updateResult(String productId, InspResultForm aiResultForm,String username);
    ResponseEntity<ApiCallResult> saveUserData(String productId, InspResultForm aiResultForm,String username);
    ResponseEntity<ApiCallResult> searchByOrderId(String orderId, String reportType);
    ResponseEntity<ApiCallResult> searchByProductId(String productId, String reportType);
    ResponseEntity<ApiCallResult> searchByProductId(String productId);
    ResponseEntity<ApiCallResult> searchBySourceId(String sourceId, String reportType);
    ResponseEntity<ApiCallResult> searchBySourceType(String sourceId, String sourceType, String reportType);
    ResponseEntity<ApiCallResult> getAllIpSupervisorData(String productId);
}
