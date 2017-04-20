/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.ai.aims.constants.Status;
import com.ai.aims.services.dto.TestFilterDTO;
import com.ai.aims.services.model.OfficeMaster;
import com.ai.aims.services.model.ProductCategory;
import com.ai.aims.services.model.Region;
import com.ai.aims.services.model.Tag;
import com.ai.aims.services.model.TestMaster;
import com.ai.aims.services.model.TurnAroundTime;
import com.ai.aims.services.model.search.SearchTagCriteria;
import com.ai.aims.services.model.search.SearchTagResponse;
import com.ai.aims.services.model.search.SearchTagTestCriteria;
import com.ai.api.bean.OfficeSearchBean;
import com.ai.api.bean.ProductCategoryDtoBean;
import com.ai.api.bean.RegionSearchBean;
import com.ai.api.bean.TagSearchBean;
import com.ai.api.bean.TatSearchBean;
import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.LTParameterDao;
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
		List<OfficeSearchBean> officeResult = new ArrayList<OfficeSearchBean>(offices.size());
		for (OfficeMaster office : offices) {
			OfficeSearchBean officeBean = new OfficeSearchBean();
			officeBean.setId(office.getId());
			officeBean.setName(office.getName());
			officeBean.setAddress(office.getAddress());
			officeBean.setCode(office.getCode());
			officeBean.setTimeZone(office.getTimezone());
			officeBean.setZipCode(office.getZipCode());
			officeBean.setStatus(office.getStatus());
			officeBean.setRemarks(office.getRemarks());
			officeBean.setInvoiceRemarks(office.getInvoiceRemarks());
			officeBean.setQuotationRemarks(office.getQuotationRemarks());
			officeBean.setBankName(office.getBankName());
			officeResult.add(officeBean);
		}
		callResult.setContent(officeResult);
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
	public ApiCallResult searchTests(SearchTagTestCriteria criteria) throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		AIUtil.addRestTemplateMessageConverter(restTemplate);
		ApiCallResult callResult = new ApiCallResult();
		String url = new StringBuilder(config.getAimsServiceBaseUrl()).append("/tag/search/tests").toString();		
		List<TestFilterDTO> tests = Arrays.asList(restTemplate.getForObject(buildTagTestSearchCriteria(criteria, url).build().encode().toUri(), TestFilterDTO[].class));
		callResult.setContent(tests);
		return callResult;
	}
	
	@Override
	public ApiCallResult searchTestsByName(String testName) throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		AIUtil.addRestTemplateMessageConverter(restTemplate);
		ApiCallResult callResult = new ApiCallResult();
		String url = new StringBuilder(config.getAimsServiceBaseUrl()).append("/api/test/autocomplete?name=").append(testName).toString();		
		List<TestMaster> tests = Arrays.asList(restTemplate.getForObject(url, TestMaster[].class));	
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
	
	@Override
	public ApiCallResult searchCategories() throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		AIUtil.addRestTemplateMessageConverter(restTemplate);
		ApiCallResult callResult = new ApiCallResult();
		String url = new StringBuilder(config.getAimsServiceBaseUrl()).append("/api/product/category/search/all").toString();
		List<ProductCategory> categories = Arrays.asList(restTemplate.getForObject(url, ProductCategory[].class, new HashMap()));
		List<ProductCategoryDtoBean> categoryData = new ArrayList<ProductCategoryDtoBean>();
		for (ProductCategory category : categories) {
			ProductCategoryDtoBean categoryObj = new ProductCategoryDtoBean();
			categoryObj.setId(category.getId());
			categoryObj.setName(category.getProductCategory());
			categoryData.add(categoryObj);
		}
		callResult.setContent(categoryData);
		return callResult;
	}
	
	@Override
	public ApiCallResult searchTags(SearchTagCriteria criteria) throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		AIUtil.addRestTemplateMessageConverter(restTemplate);
		ApiCallResult callResult = new ApiCallResult();
		String url = new StringBuilder(config.getAimsServiceBaseUrl()).append("/tag/search/filter").toString();
		SearchTagResponse response = restTemplate.getForObject(buildTagSearchCriteria(criteria, url).build().encode().toUri(), SearchTagResponse.class);
		List<Tag> tags = null != response.getData() ? response.getData() : new ArrayList<Tag>(0);
		List<TagSearchBean> tagData = new ArrayList<TagSearchBean>();
		for (Tag tag : tags) {
			TagSearchBean tagObj = new TagSearchBean();
			tagObj.setId(tag.getId());
			tagObj.setName(tag.getName());
			tagObj.setDescription(tag.getDescription());
			tagObj.setTagLevel(tag.getTagLevel());
			tagData.add(tagObj);
		}
		callResult.setContent(tagData);
		return callResult;
	}
	
	@Override
	public ApiCallResult searchRegions() throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		AIUtil.addRestTemplateMessageConverter(restTemplate);
		ApiCallResult callResult = new ApiCallResult();
		String url = new StringBuilder(config.getAimsServiceBaseUrl()).append("/api/region/search").toString();
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
		        .queryParam("page", 0)
		        .queryParam("size", 1000000);
		List<Region> regions = Arrays.asList(restTemplate.getForObject(builder.build().toUri(), Region[].class));
		List<RegionSearchBean> regionResult = new ArrayList<RegionSearchBean>(regions.size());
		for (Region region : regions) {
			RegionSearchBean regionBean = new RegionSearchBean();
			regionBean.setId(region.getId());
			regionBean.setName(region.getName());
			regionBean.setStatus(region.getStatus());
			regionResult.add(regionBean);
		}
		callResult.setContent(regionResult);
		return callResult;
	}

	@Override
	public ApiCallResult searchTATs(String officeId) throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		AIUtil.addRestTemplateMessageConverter(restTemplate);
		ApiCallResult callResult = new ApiCallResult();
		String url = new StringBuilder(config.getAimsServiceBaseUrl()).append("/tats/office/").append(officeId).toString();
		List<TurnAroundTime> tats = Arrays.asList(restTemplate.getForObject(url, TurnAroundTime[].class));
		List<TatSearchBean> tatResult = new ArrayList<TatSearchBean>(tats.size());
		tats.stream().forEach(t -> tatResult.add(getTatDetails(t)));
		callResult.setContent(tatResult);
		return callResult;
	}
	
	private TatSearchBean getTatDetails(TurnAroundTime tat) {
		TatSearchBean tatSearchBean = new TatSearchBean();
		BeanUtils.copyProperties(tat, tatSearchBean, "office", "isExpService");
		tatSearchBean.setOffice(null != tat.getOffice() ? tat.getOffice().getName() : null);
		tatSearchBean.setIsExpService("1".equals(tat.getIsExpService()) ? true : false);
		return tatSearchBean;
	}

	private UriComponentsBuilder buildTagSearchCriteria(SearchTagCriteria criteria, String url) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
		        .queryParam("page", criteria.getPage() != 0 ? criteria.getPage() - 1 : 0)
		        .queryParam("pageSize", criteria.getPageSize() != 0 ? criteria.getPageSize() : 1000000);
		
		if (!StringUtils.stripToEmpty(criteria.getCategoryId()).trim().isEmpty())
			builder.queryParam("categoryId", criteria.getCategoryId().trim());
		return builder;
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
	
	private UriComponentsBuilder buildTagTestSearchCriteria(SearchTagTestCriteria criteria, String url) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
		        .queryParam("page", criteria.getPage() != 0 ? criteria.getPage() - 1 : 0)
		        .queryParam("size", criteria.getPageSize() != 0 ? criteria.getPageSize() : 1000000);
		
		if(null != criteria.getTagIds() && !criteria.getTagIds().isEmpty())
			builder.queryParam("tagIds", criteria.getTagIds());
		
		if(null != criteria.getProductCategory() && !criteria.getProductCategory().isEmpty())
			builder.queryParam("productCategory", criteria.getProductCategory());
		
		if(null != criteria.getOffice() && !criteria.getOffice().isEmpty())
			builder.queryParam("office", criteria.getOffice());
		
		if(null != criteria.getProgram() && !criteria.getProgram().isEmpty())
			builder.queryParam("program", criteria.getProgram());
		
		if(null != criteria.getTestnames())
			builder.queryParam("testnames", String.join(",", criteria.getTestnames()));
		
		if(null != criteria.getCountries())
			builder.queryParam("countries", String.join(",", criteria.getCountries()));
		
		if(null != criteria.getRegions())
			builder.queryParam("regions", String.join(",", criteria.getRegions()));
		
		return builder;
	}

}
