package com.ai.api.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;

import com.ai.commons.beans.ApiCallResult;

@SuppressWarnings("rawtypes")
public interface LTReport {
	
	public ResponseEntity<ApiCallResult> findReports(String userId, Integer pageNumber, Integer pageSize);

	public ResponseEntity<ApiCallResult> findReport(String userId, String reportId);
	
	public ResponseEntity<ApiCallResult> editReportStatus(String userId, String reportId, String status);
	
	public ResponseEntity<ApiCallResult> editReportTestStatus(String userId, String reportId, String testId, String status);

	ResponseEntity downloadPDF(String userId, String attachmentId, HttpServletResponse response);
}
