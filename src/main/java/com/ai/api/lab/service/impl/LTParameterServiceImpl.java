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

import com.ai.aims.services.model.search.SearchTagCriteria;
import com.ai.api.bean.UserBean;
import com.ai.api.config.ServiceConfig;
import com.ai.api.exception.AIException;
import com.ai.api.lab.dao.LTParameterDao;
import com.ai.api.lab.service.LTParameterService;
import com.ai.api.service.UserService;
import com.ai.commons.beans.ApiCallResult;
import com.ai.program.search.criteria.SearchProgramCriteria;
import com.google.common.collect.Lists;

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

@SuppressWarnings("rawtypes")
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
	public ApiCallResult searchOffice() throws IOException {
		return ltparameterDao.searchOffice();
	}

	@Override
	public ApiCallResult searchPrograms(String userId) throws IOException, AIException {
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
	public ApiCallResult searchTests(List<String> countryName, List<String> testName) throws IOException {
		SearchTagCriteria criteria = new SearchTagCriteria();
		if(null != countryName && !countryName.isEmpty()) {
			criteria.setCountryName(Lists.newArrayList(countryName));	
		}
		if(null != testName && !testName.isEmpty()) {
			criteria.setTestName(Lists.newArrayList(testName));	
		}		
		return ltparameterDao.searchTests(criteria);
	}

	@Override
	public ApiCallResult searchProgram(String programId) throws IOException {
		SearchProgramCriteria criteria = new SearchProgramCriteria();
		criteria.setProgramId(programId);
		return ltparameterDao.searchPrograms(criteria);
	}

	@Override
	public ApiCallResult searchTest(String testId) throws IOException {
		return ltparameterDao.searchTest(testId);
	}

}
