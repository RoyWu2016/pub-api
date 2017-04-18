package com.ai.api.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;

import com.ai.api.exception.AIException;
import com.ai.commons.beans.ApiCallResult;

public interface SupplicerV2 {

	ResponseEntity<ApiCallResult> getSuppliersByUserId(String userId) throws IOException, AIException;

}
