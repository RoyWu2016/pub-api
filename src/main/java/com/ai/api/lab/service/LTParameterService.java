/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.lab.service;

import java.io.IOException;
import java.util.List;

import com.ai.api.exception.AIException;
import com.ai.commons.beans.ApiCallResult;

/***************************************************************************
 * <PRE>
 *  Project Name    : api
 *
 *  Package Name    : com.ai.api.lab.service
 *
 *  File Name       : LTParameterService.java
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

@SuppressWarnings("rawtypes")
public interface LTParameterService {

	public ApiCallResult searchOffice() throws IOException;
	
	public ApiCallResult searchPrograms(String userId) throws IOException, AIException;
	
	public ApiCallResult searchProgram(String programId) throws IOException;
	
	public ApiCallResult searchTestsByTag(List<String> countries, List<String> testNames, List<String> regions, String tagLevel, String productCategory) throws IOException;
	
	public ApiCallResult searchTest(String testId) throws IOException;
	
	public ApiCallResult searchTestsByName(String testName) throws IOException;
}
