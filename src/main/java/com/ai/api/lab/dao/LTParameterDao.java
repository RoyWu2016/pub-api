/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.lab.dao;

import java.io.IOException;

import com.ai.aims.services.model.search.SearchTagCriteria;
import com.ai.commons.beans.ApiCallResult;
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
@SuppressWarnings("rawtypes")
public interface LTParameterDao {

	public ApiCallResult searchOffice() throws IOException;
	
	public ApiCallResult searchPrograms(SearchProgramCriteria criteria) throws IOException;
	
	public ApiCallResult searchTests(SearchTagCriteria criteria) throws IOException;
	
	public ApiCallResult searchTest(String testId) throws IOException;
}
