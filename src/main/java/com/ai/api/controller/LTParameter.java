package com.ai.api.controller;

/***************************************************************************
 * <PRE>
 *  Project Name    : api
 *
 *  Package Name    : com.ai.api.lab.controller
 *
 *  File Name       : LTParameter.java
 *
 *  Creation Date   : Dec 6, 2016
 *
 *  Author          : Aashish Thakran
 *
 *  Purpose         : TODO
 *
 *
 *  History         : TODO
 *
 * </PRE>
 ***************************************************************************/

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ai.commons.beans.ApiCallResult;

@SuppressWarnings("rawtypes")
public interface LTParameter {
	
	public ResponseEntity<ApiCallResult> searchOffice(boolean refresh);
	
	public ResponseEntity<ApiCallResult> searchPrograms(boolean refresh, String userId);
	
	public ResponseEntity<ApiCallResult> searchProgram(boolean refresh, String programId);
	
	public ResponseEntity<ApiCallResult> searchTestsByTag(boolean refresh, List<String> countries, List<String> testNames, List<String> regions, String tagLevel, String productCategory);
	
	public ResponseEntity<ApiCallResult> searchTest(boolean refresh, String testId);
	
	public ResponseEntity<ApiCallResult> searchTestsByName(boolean refresh, String testName);
}