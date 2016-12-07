/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.lab.service.impl;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ai.aims.services.model.OfficeMaster;
import com.ai.aims.services.model.TestMaster;
import com.ai.aims.services.model.search.SearchTestCriteria;
import com.ai.api.bean.UserBean;
import com.ai.api.config.ServiceConfig;
import com.ai.api.exception.AIException;
import com.ai.api.lab.dao.LTParameterDao;
import com.ai.api.lab.service.LTParameterService;
import com.ai.api.service.UserService;
import com.ai.program.model.Program;
import com.ai.program.search.criteria.SearchProgramCriteria;

/***************************************************************************
 * <PRE>
 *  Project Name    : api
 *
 *  Package Name    : com.ai.api.lab.service.impl
 *
 *  File Name       : LTParameterServiceImpl.java
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

@Service
@Qualifier("ltparameterService")
public class LTParameterServiceImpl implements LTParameterService {

	protected Logger logger = LoggerFactory.getLogger(LTParameterServiceImpl.class);

	@Autowired
	@Qualifier("ltparameterDao")
	private LTParameterDao ltparameterDao;

	@Autowired
	@Qualifier("serviceConfig")
	private ServiceConfig config;

	@Autowired
	@Qualifier("userService")
	private UserService userService;

	@Override
	public List<OfficeMaster> searchOffice() {
		return ltparameterDao.searchOffice();
	}

	@Override
	public List<Program> searchPrograms(String userId) throws IOException, AIException {
		String companyId = "";
		String parentId = "";
		UserBean user = userService.getCustById(userId);
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null)
				parentId = "";
			companyId = user.getCompany().getId();
		}		
		SearchProgramCriteria criteria = new SearchProgramCriteria();
		criteria.setCompanyId(companyId);
		return ltparameterDao.searchPrograms(criteria);
	}

	@Override
	public List<TestMaster> searchTests(String countryName, String testName) {
		SearchTestCriteria criteria = new SearchTestCriteria();
		criteria.setCountryName(countryName);
		criteria.setName(testName);
		return ltparameterDao.searchTests(criteria);
	}

	@Override
	public Program searchProgram(String programId) {
		SearchProgramCriteria criteria = new SearchProgramCriteria();
		criteria.setProgramId(programId);
		List<Program> programs = ltparameterDao.searchPrograms(criteria);
		return !programs.isEmpty() ? programs.get(0) : null;
	}

	@Override
	public TestMaster searchTest(String testId) {
		SearchTestCriteria criteria = new SearchTestCriteria();
		criteria.setId(testId);
		List<TestMaster> tests = ltparameterDao.searchTests(criteria);
		return !tests.isEmpty() ? tests.get(0) : null;
	}

}
