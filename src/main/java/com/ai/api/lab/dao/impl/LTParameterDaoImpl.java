/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.lab.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.ai.aims.constants.Status;
import com.ai.aims.services.model.OfficeMaster;
import com.ai.aims.services.model.TestMaster;
import com.ai.aims.services.model.search.SearchTestCriteria;
import com.ai.api.config.ServiceConfig;
import com.ai.api.lab.dao.LTParameterDao;
import com.ai.api.util.AIUtil;
import com.ai.program.model.Program;
import com.ai.program.search.criteria.SearchProgramCriteria;

/***************************************************************************
 * <PRE>
 *  Project Name    : api
 *
 *  Package Name    : com.ai.api.lab.dao.impl
 *
 *  File Name       : LTParameterDaoImpl.java
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

@Qualifier("ltparameterDao")
@Component
public class LTParameterDaoImpl implements LTParameterDao {

	@Autowired
	@Qualifier("serviceConfig")
	private ServiceConfig config;

	@Override
	public List<OfficeMaster> searchOffice() {
		List<OfficeMaster> offices = new ArrayList<>();
		RestTemplate restTemplate = new RestTemplate();
		AIUtil.addRestTemplateMessageConverter(restTemplate);
		
		String url = new StringBuilder(config.getAimsServiceBaseUrl()).append("/api/office/search/all").toString();
		offices = Arrays.asList(restTemplate.getForObject(url, OfficeMaster[].class));		
		return offices;
	}

	@Override
	public List<Program> searchPrograms(SearchProgramCriteria criteria) {
		List<Program> programs = new ArrayList<>();
		RestTemplate restTemplate = new RestTemplate();
		AIUtil.addRestTemplateMessageConverter(restTemplate);
		
		String url = new StringBuilder(config.getProgramServiceBaseUrl()).append("/program/search").toString();
		programs = Arrays.asList(restTemplate.getForObject(buildProgramSearchCriteria(criteria, url).build().encode().toUri(), Program[].class));		
		return programs;
	}
	
	@Override
	public List<TestMaster> searchTests(SearchTestCriteria criteria) {
		List<TestMaster> tests = new ArrayList<>();
		RestTemplate restTemplate = new RestTemplate();
		AIUtil.addRestTemplateMessageConverter(restTemplate);
		String url = new StringBuilder(config.getAimsServiceBaseUrl()).append("/api/test/search").toString();
		
		tests = Arrays.asList(restTemplate.getForObject(buildTestSearchCriteria(criteria, url).build().encode().toUri(), TestMaster[].class));
		return tests;
	}
	
	private UriComponentsBuilder buildProgramSearchCriteria(SearchProgramCriteria criteria, String url) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);	
		
		if(!StringUtils.stripToEmpty(criteria.getCompanyId()).trim().isEmpty())
			builder.queryParam("companyId", criteria.getCompanyId().trim());
		
		if(!StringUtils.stripToEmpty(criteria.getCompanyName()).trim().isEmpty())
			builder.queryParam("companyName", criteria.getCompanyName().trim());
		
		if(!StringUtils.stripToEmpty(criteria.getProgramId()).trim().isEmpty())
			builder.queryParam("programId", criteria.getProgramId().trim());
		
		if(!StringUtils.stripToEmpty(criteria.getProgramName()).trim().isEmpty())
			builder.queryParam("programName", criteria.getProgramName().trim());
		
		if(!StringUtils.stripToEmpty(criteria.getStatus()).trim().isEmpty())
			builder.queryParam("status", Status.ACTIVE.getValue().trim());
		
		return builder;
	}
	
	private UriComponentsBuilder buildTestSearchCriteria(SearchTestCriteria criteria, String url) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
		        .queryParam("page", criteria.getPage() != 0 ? criteria.getPage() - 1 : 0)
		        .queryParam("size", criteria.getSize() != 0 ? criteria.getSize() : 1000000)
		        .queryParam("direction", "desc");
		
		if(!StringUtils.stripToEmpty(criteria.getName()).trim().isEmpty())
			builder.queryParam("name", criteria.getName().trim());
		
		if(!StringUtils.stripToEmpty(criteria.getTestCategory()).trim().isEmpty())
			builder.queryParam("testCategory", criteria.getTestCategory().trim());
		
		if(!StringUtils.stripToEmpty(criteria.getCountryName()).trim().isEmpty())
			builder.queryParam("countryName", criteria.getCountryName().trim());
		
		if(!StringUtils.stripToEmpty(criteria.getOptional()).trim().isEmpty())
			builder.queryParam("optional", criteria.getOptional().trim());
		
		return builder;
	}

}
