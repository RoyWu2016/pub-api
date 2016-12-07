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

import com.ai.aims.services.model.OfficeMaster;
import com.ai.aims.services.model.TestMaster;
import com.ai.program.model.Program;

public interface LTParameter {

	public ResponseEntity<List<OfficeMaster>> searchOffice(boolean refresh);
	
	public ResponseEntity<List<Program>> searchPrograms(boolean refresh, String userId);
	
	public ResponseEntity<Program> searchProgram(boolean refresh, String programId);
	
	public ResponseEntity<List<TestMaster>> searchTests(boolean refresh, String countryName, String testName);
	
	public ResponseEntity<TestMaster> searchTest(boolean refresh, String testId);
}
