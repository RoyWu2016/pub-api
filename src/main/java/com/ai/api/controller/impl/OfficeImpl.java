/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.controller.impl;

import io.swagger.annotations.ApiOperation;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.ai.aims.services.model.OfficeMaster;
import com.ai.api.config.ServiceConfig;
import com.ai.api.controller.Office;
import com.ai.api.util.AIUtil;
import com.ai.commons.annotation.TokenSecured;

/***************************************************************************
 * <PRE>
 * Project Name    : api
 * <p>
 * Package Name    : com.ai.api.controller.impl
 * <p>
 * File Name       : OfficeImpl.java
 * <p>
 * Creation Date   : Sep 20, 2016
 * <p>
 * Author          : Aashish Thakran
 * <p>
 * Purpose         : TODO
 * <p>
 * <p>
 * History         : TODO
 * <p>
 * </PRE>
 ***************************************************************************/

@RestController
public class OfficeImpl implements Office {

	protected Logger logger = LoggerFactory.getLogger(OfficeImpl.class);

	@Autowired
	@Qualifier("serviceConfig")
	private ServiceConfig config;

	@ApiOperation(value = "Search Office API",		
	        produces = "application/json",
		    response = OfficeMaster.class,
		    httpMethod = "GET")
	@TokenSecured
	@RequestMapping(value = "/offices/list", method = RequestMethod.GET)
	public ResponseEntity<List<OfficeMaster>> searchOffice() {
		RestTemplate restTemplate = new RestTemplate();
		List<OfficeMaster> offices = new ArrayList<OfficeMaster>();
		try {
			AIUtil.addRestTemplateMessageConverter(restTemplate);
			String url = new StringBuilder(config.getAimsServiceBaseUrl()).append("/api/office/search/all").toString();
			offices = Arrays.asList(restTemplate.getForObject(url, OfficeMaster[].class));
		} catch (Exception e) {
			logger.error("search office error: " + ExceptionUtils.getFullStackTrace(e));
			return new ResponseEntity<List<OfficeMaster>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<List<OfficeMaster>>(offices, HttpStatus.OK);
	}
	

}
