/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.dao.impl;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.ai.aims.constants.Status;
import com.ai.aims.services.dto.TestFilterDTO;
import com.ai.aims.services.dto.TurnAroundTimeDTO;
import com.ai.aims.services.dto.office.OfficeDTO;
import com.ai.aims.services.dto.packagemgmt.PackageDTO;
import com.ai.aims.services.model.OfficeMaster;
import com.ai.aims.services.model.ProductCategory;
import com.ai.aims.services.model.Region;
import com.ai.aims.services.model.Tag;
import com.ai.aims.services.model.TestMaster;
import com.ai.aims.services.model.search.SearchPackageTestCriteria;
import com.ai.aims.services.model.search.SearchProgramTestCriteria;
import com.ai.aims.services.model.search.SearchTagCriteria;
import com.ai.aims.services.model.search.SearchTagResponse;
import com.ai.aims.services.model.search.SearchTagTestCriteria;
import com.ai.api.bean.OfficeSearchBean;
import com.ai.api.bean.ProductCategoryDtoBean;
import com.ai.api.bean.RegionSearchBean;
import com.ai.api.bean.TagSearchBean;
import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.LTParameterDao;
import com.ai.api.util.AIUtil;
import com.ai.commons.beans.ApiCallResult;
import com.ai.program.dto.ProgramDTO;
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
	public ApiCallResult searchPackages(String programID) throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		AIUtil.addRestTemplateMessageConverter(restTemplate);

		String url = new StringBuilder(config.getAimsServiceBaseUrl()).append("/api/package/search/").toString();
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url.toString())
				.queryParam("program", programID)
				.queryParam("requestor", "external");
		PackageDTO[] packages = restTemplate.getForObject(builder.build().encode().toUri(), PackageDTO[].class);
		
		ApiCallResult callResult = new ApiCallResult();
		callResult.setContent(packages);
		return callResult;
	}

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
		List<ProgramDTO> programs = Arrays.asList(restTemplate.getForObject(buildProgramSearchCriteria(criteria, url).build().encode().toUri(), ProgramDTO[].class));	
		callResult.setContent(programs);
		return callResult;
	}

	@Override
	public ApiCallResult searchProgramTestLocations(String programId) throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		AIUtil.addRestTemplateMessageConverter(restTemplate);
		ApiCallResult callResult = new ApiCallResult();
		
		String url = new StringBuilder(config.getProgramServiceBaseUrl()).append("/program/").append(programId).append("/testlocations").toString();
		URI uri = UriComponentsBuilder.fromHttpUrl(url).queryParam("requestor", "external").build().encode().toUri();
		List<OfficeDTO> testLocations = Arrays.asList(restTemplate.getForObject(uri, OfficeDTO[].class));
		callResult.setContent(testLocations);
		return callResult;
	}
	
	@Override
	public ApiCallResult searchTagTests(SearchTagTestCriteria criteria) throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		AIUtil.addRestTemplateMessageConverter(restTemplate);
		ApiCallResult callResult = new ApiCallResult();
		String url = new StringBuilder(config.getAimsServiceBaseUrl()).append("/tag/search/tests").toString();		
		List<TestFilterDTO> tests = Arrays.asList(restTemplate.getForObject(buildTagTestSearchCriteria(criteria, url).build().encode().toUri(), TestFilterDTO[].class));
		callResult.setContent(tests);
		return callResult;
	}
	
	@Override
	public ApiCallResult searchPackageTests(SearchPackageTestCriteria criteria) throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		AIUtil.addRestTemplateMessageConverter(restTemplate);
		ApiCallResult callResult = new ApiCallResult();
		String url = new StringBuilder(config.getAimsServiceBaseUrl()).append("/api/package/tests").toString();		
		List<TestFilterDTO> tests = Arrays.asList(restTemplate.getForObject(buildPackageTestSearchCriteria(criteria, url).build().encode().toUri(), TestFilterDTO[].class));
		callResult.setContent(tests);
		return callResult;
	}
	
	@Override
	public ApiCallResult searchProgramTests(SearchProgramTestCriteria criteria) throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		AIUtil.addRestTemplateMessageConverter(restTemplate);
		ApiCallResult callResult = new ApiCallResult();
		String url = new StringBuilder(config.getAimsServiceBaseUrl()).append("/api/program/tests").toString();
		List<TestFilterDTO> tests = Arrays.asList(restTemplate.getForObject(buildProgramTestSearchCriteria(criteria, url).build().encode().toUri(), TestFilterDTO[].class));
		callResult.setContent(tests);
		return callResult;
	}

	@Override
	public ApiCallResult updateProgramTests(String userId, String programId, String tests, Boolean isFavorite) throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		AIUtil.addRestTemplateMessageConverter(restTemplate);
		String url = new StringBuilder(config.getProgramServiceBaseUrl())
			.append("/program/").append(programId)
			.append("/user/").append(userId).append("/tests").toString();
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
				.queryParam("requestor", "external")
				.queryParam("isFavorite", isFavorite);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tests", tests);
		restTemplate.put(builder.build().encode().toUri(), params);
		SearchProgramTestCriteria criteria = new SearchProgramTestCriteria();
		criteria.setIsFavorite(isFavorite);
		criteria.setProgramId(programId);
		return searchProgramTests(criteria);
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
	public ApiCallResult searchTATs(String officeId, String programId, String testIds) throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		AIUtil.addRestTemplateMessageConverter(restTemplate);
		ApiCallResult callResult = new ApiCallResult();
		String url = new StringBuilder(config.getAimsServiceBaseUrl()).append("/tats/office/").append(officeId).toString();
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
		        .queryParam("programId", programId)
		        .queryParam("testIds", testIds)
		        .queryParam("requestor", "external");
		List<TurnAroundTimeDTO> tats = Arrays.asList(restTemplate.getForObject(builder.build().encode().toUri(), TurnAroundTimeDTO[].class));
		callResult.setContent(tats);
		return callResult;
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
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
				.queryParam("requestor", "external");	
		
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
	
	private UriComponentsBuilder buildPackageTestSearchCriteria(SearchPackageTestCriteria criteria, String url) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
				.queryParam("requestor", "external");
		
		if(null != criteria.getCountries())
			builder.queryParam("countries", String.join(",", criteria.getCountries()));
		
		if(null != criteria.getRegions())
			builder.queryParam("regions", String.join(",", criteria.getRegions()));

		if(null != criteria.getPackageId() && !criteria.getPackageId().isEmpty())
			builder.queryParam("packageId", criteria.getPackageId());

		if(null != criteria.getOfficeId() && !criteria.getOfficeId().isEmpty())
			builder.queryParam("officeId", criteria.getOfficeId());

		if(null != criteria.getProgramId() && !criteria.getProgramId().isEmpty())
			builder.queryParam("programId", criteria.getProgramId());

		if(null != criteria.getTestName() && !criteria.getTestName().isEmpty())
			builder.queryParam("testName", criteria.getTestName());
		
		return builder;
	}
	
	private UriComponentsBuilder buildProgramTestSearchCriteria(SearchProgramTestCriteria criteria, String url) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
				.queryParam("requestor", "external");
		
		if(null != criteria.getCountries())
			builder.queryParam("countries", String.join(",", criteria.getCountries()));
		
		if(null != criteria.getRegions())
			builder.queryParam("regions", String.join(",", criteria.getRegions()));

		if(null != criteria.getOfficeId() && !criteria.getOfficeId().isEmpty())
			builder.queryParam("officeId", criteria.getOfficeId());

		if(null != criteria.getProgramId() && !criteria.getProgramId().isEmpty())
			builder.queryParam("programId", criteria.getProgramId());

		if(null != criteria.getTestName() && !criteria.getTestName().isEmpty())
			builder.queryParam("testName", criteria.getTestName());

		if(null != criteria.getIsFavorite())
			builder.queryParam("isFavorite", criteria.getIsFavorite());
		
		return builder;
	}
}
