/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.lab.service;

import java.io.IOException;
import java.util.List;

import com.ai.aims.services.model.OfficeMaster;
import com.ai.aims.services.model.TestMaster;
import com.ai.api.exception.AIException;
import com.ai.program.model.Program;

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

public interface LTParameterService {

	public List<OfficeMaster> searchOffice();
	
	public List<Program> searchPrograms(String userId) throws IOException, AIException;
	
	public Program searchProgram(String programId);
	
	public List<TestMaster> searchTests(String countryName, String testName);
	
	public TestMaster searchTest(String testId);
}
