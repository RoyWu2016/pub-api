/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.service.impl;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ai.aims.services.model.search.SearchTagCriteria;
import com.ai.aims.services.model.search.SearchTagTestCriteria;
import com.ai.api.bean.UserBean;
import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.LTParameterDao;
import com.ai.api.exception.AIException;
import com.ai.api.service.LTParameterService;
import com.ai.api.service.UserService;
import com.ai.commons.beans.ApiCallResult;
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
	public ApiCallResult searchTestsByTag(List<String> countries, List<String> testNames, List<String> regions, String tagLevel, String productCategory) throws IOException {
		SearchTagTestCriteria criteria = new SearchTagTestCriteria();
		if(null != countries) {
			criteria.setCountries(countries);	
		}
		if(null != testNames) {
			criteria.setTestnames(testNames);	
		}	
		
		if(null != regions)
			criteria.setRegions(regions);
		
		if(!StringUtils.stripToEmpty(tagLevel).isEmpty())
			criteria.setTagLevel(tagLevel);
		
		if(!StringUtils.stripToEmpty(productCategory).isEmpty())
			criteria.setProductCategory(productCategory);
		
		return ltparameterDao.searchTestsByTag(criteria);
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
	
	@Override
	public ApiCallResult searchTestsByName(String testName) throws IOException {
		return ltparameterDao.searchTestsByName(testName);
	}

	@Override
	public ApiCallResult searchCategories() throws IOException {
		return ltparameterDao.searchCategories();
	}

	@Override
	public ApiCallResult searchTags(String categoryId) throws IOException {
		SearchTagCriteria criteria = new SearchTagCriteria();
		criteria.setCategoryId(categoryId);
		return ltparameterDao.searchTags(criteria);
	}
}
