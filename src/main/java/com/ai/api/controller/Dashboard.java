package com.ai.api.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;

import com.ai.api.exception.AIException;
import com.ai.commons.beans.ApiCallResult;

public interface Dashboard {

	ResponseEntity<ApiCallResult> getDashboardOverView(String userId, String startDate, String endDate)
			throws IOException, AIException;

}
