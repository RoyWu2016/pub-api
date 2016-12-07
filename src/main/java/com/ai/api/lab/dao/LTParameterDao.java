/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.lab.dao;

import java.util.List;

import com.ai.aims.services.model.OfficeMaster;
import com.ai.aims.services.model.TestMaster;
import com.ai.aims.services.model.search.SearchTestCriteria;
import com.ai.program.model.Program;
import com.ai.program.search.criteria.SearchProgramCriteria;

/***************************************************************************
 * <PRE>
 *  Project Name    : api
 *
 *  Package Name    : com.ai.api.lab.dao
 *
 *  File Name       : LTParameterDao.java
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

public interface LTParameterDao {

	public List<OfficeMaster> searchOffice();
	
	public List<Program> searchPrograms(SearchProgramCriteria criteria);
	
	public List<TestMaster> searchTests(SearchTestCriteria criteria);
}
