/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.lab.dao.impl;

import java.io.IOException;
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
import com.ai.aims.services.model.TagTestMap;
import com.ai.aims.services.model.TestMaster;
import com.ai.aims.services.model.search.SearchTagCriteria;
import com.ai.api.config.ServiceConfig;
import com.ai.api.lab.dao.LTParameterDao;
import com.ai.api.util.AIUtil;
import com.ai.commons.beans.ApiCallResult;
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

@SuppressWarnings({"rawtypes", "unchecked"})
@Qualifier("ltparameterDao")
@Component
public class LTParameterDaoImpl implements LTParameterDao {

	@Autowired
	@Qualifier("serviceConfig")
	private ServiceConfig config;

	@Override
	public ApiCallResult searchOffice() throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		AIUtil.addRestTemplateMessageConverter(restTemplate);
		ApiCallResult callResult = new ApiCallResult();
		String url = new StringBuilder(config.getAimsServiceBaseUrl()).append("/api/office/search/all").toString();
		List<OfficeMaster> offices = Arrays.asList(restTemplate.getForObject(url, OfficeMaster[].class));	
		callResult.setContent(offices);
		return callResult;
	}

	@Override
	public ApiCallResult searchPrograms(SearchProgramCriteria criteria) throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		AIUtil.addRestTemplateMessageConverter(restTemplate);
		ApiCallResult callResult = new ApiCallResult();

		String url = new StringBuilder(config.getProgramServiceBaseUrl()).append("/program/search").toString();
		List<Program> programs = Arrays.asList(restTemplate.getForObject(buildProgramSearchCriteria(criteria, url).build().encode().toUri(), Program[].class));	
		callResult.setContent(programs);
		return callResult;
	}
	
	@Override
	public ApiCallResult searchTests(SearchTagCriteria criteria) throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		AIUtil.addRestTemplateMessageConverter(restTemplate);
		ApiCallResult callResult = new ApiCallResult();
		String url = new StringBuilder(config.getAimsServiceBaseUrl()).append("/tag/search/tests").toString();		
		List<TagTestMap> tests = Arrays.asList(restTemplate.getForObject(buildTagSearchCriteria(criteria, url).build().encode().toUri(), TagTestMap[].class));	
		callResult.setContent(tests);
		return callResult;
	}
	
	@Override
	public ApiCallResult searchTest(String testId) throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		AIUtil.addRestTemplateMessageConverter(restTemplate);
		ApiCallResult callResult = new ApiCallResult();
		String url = new StringBuilder(config.getAimsServiceBaseUrl()).append("/testmanagement/").append(testId).toString();					
		TestMaster test = restTemplate.getForObject(url, TestMaster.class);
		callResult.setContent(test);
		return callResult;		
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
	
	private UriComponentsBuilder buildTagSearchCriteria(SearchTagCriteria criteria, String url) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
		        .queryParam("page", criteria.getPage() != 0 ? criteria.getPage() - 1 : 0)
		        .queryParam("size", criteria.getPageSize() != 0 ? criteria.getPageSize() : 1000000);
		
		if(null != criteria.getTestName() && !criteria.getTestName().isEmpty())
			builder.queryParam("testName", criteria.getTestName());
		
		if(null != criteria.getCountryName() && !criteria.getCountryName().isEmpty())
			builder.queryParam("countryName", criteria.getCountryName());
		
		if(!StringUtils.stripToEmpty(criteria.getOptional()).trim().isEmpty())
			builder.queryParam("optional", criteria.getOptional().trim());
		
		return builder;
	}

}
