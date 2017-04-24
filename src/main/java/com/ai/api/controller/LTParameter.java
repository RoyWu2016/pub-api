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

import org.springframework.http.ResponseEntity;

import com.ai.commons.beans.ApiCallResult;

@SuppressWarnings("rawtypes")
public interface LTParameter {
	
	public ResponseEntity<ApiCallResult> searchOffice(boolean refresh);

	public ResponseEntity<ApiCallResult> searchProgram(boolean refresh, String programId);
	
	public ResponseEntity<ApiCallResult> searchTestsWithFilters(String countries, String regions, String testNames, 
			String tags, String productCategory, String office, String program);
	
	public ResponseEntity<ApiCallResult> searchTest(boolean refresh, String testId);

	public ResponseEntity<ApiCallResult> searchCategories();

	public ResponseEntity<ApiCallResult> searchTagsByCategory(String categoryId);
	
	public ResponseEntity<ApiCallResult> searchTestsByName(boolean refresh, String testName);

	public ResponseEntity<ApiCallResult> searchRegions();
	
	public ResponseEntity<ApiCallResult> searchTATs(String officeId);
}
