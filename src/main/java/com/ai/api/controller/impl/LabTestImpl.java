/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.controller.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.ai.aims.services.model.TagTestMap;
import com.ai.api.config.ServiceConfig;
import com.ai.api.controller.LabTest;
import com.ai.api.util.AIUtil;
import com.ai.commons.annotation.TokenSecured;

/***************************************************************************
 *<PRE>
 *  Project Name    : api
 *
 *  Package Name    : com.ai.api.controller.impl
 *
 *  File Name       : LabTestImpl.java
 *
 *  Creation Date   : Sep 3, 2016
 *
 *  Author          : Aashish Thakran
 *
 *  Purpose         : TODO
 *
 *
 *  History         : TODO
 *
 *</PRE>
 ***************************************************************************/

@RestController
public class LabTestImpl implements LabTest {

	protected Logger logger = LoggerFactory.getLogger(LabTestImpl.class);

	@Autowired
	@Qualifier("serviceConfig")
	private ServiceConfig config;

	@Override
    @TokenSecured
    @RequestMapping(value = "/user/{userId}/lt-tests/list", method = RequestMethod.GET)
	public ResponseEntity<List<TagTestMap>> searchLTTests(@PathVariable("userId") String userId, 
			@RequestParam(value = "countryName", required = false ) List<String> countryName, 
			@RequestParam(value = "productCategory", required = false) List<String> productCategory, 
			@RequestParam(value = "keywords", required = false) List<String> keywords, 
			@RequestParam(value = "pageNo", required = false , defaultValue="1") Integer pageNumber, 
			@RequestParam(value = "pageSize", required = false , defaultValue="20") Integer pageSize) {
		RestTemplate restTemplate = new RestTemplate();
		List<TagTestMap> tests = new ArrayList<TagTestMap>();
		try {
			AIUtil.addRestTemplateMessageConverter(restTemplate);
			String url = new StringBuilder(config.getAimsServiceBaseUrl()).append("/tag/search/tests").toString();
			
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
			        .queryParam("page", pageNumber - 1)
			        .queryParam("size", pageSize)
			        .queryParam("direction", "desc" );	
			
			if(null != countryName && !countryName.isEmpty()) {
				builder.queryParam("countryName", countryName);
			}
			if(null != productCategory && !productCategory.isEmpty()) {
				builder.queryParam("productCategory", productCategory);
			}
			if(null != keywords && !keywords.isEmpty()) {
				builder.queryParam("testName", keywords);
			}
			
			tests = Arrays.asList(restTemplate.getForObject(builder.build().encode().toUri(), TagTestMap[].class));
		} catch (Exception e) {
			logger.error("error fetching tests: " + ExceptionUtils.getFullStackTrace(e));
			return new ResponseEntity<List<TagTestMap>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<TagTestMap>>(tests, HttpStatus.OK);		
	}
}
