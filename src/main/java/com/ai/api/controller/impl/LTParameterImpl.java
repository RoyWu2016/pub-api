package com.ai.api.controller.impl;

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

import com.ai.aims.services.model.OfficeMaster;
import com.ai.aims.services.model.TagTestMap;
import com.ai.aims.services.model.TestMaster;
import com.ai.api.config.ServiceConfig;
import com.ai.api.controller.LTParameter;
import com.ai.api.service.LTParameterService;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.ApiCallResult;
import com.ai.program.model.Program;

/***************************************************************************
 * <PRE>
 *  Project Name    : api
 *
 *  Package Name    : com.ai.api.lab.controller.impl
 *
 *  File Name       : LTParameterImpl.java
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


import io.swagger.annotations.ApiOperation;

@SuppressWarnings({"rawtypes"})
@RestController
public class LTParameterImpl implements LTParameter {

	protected Logger logger = LoggerFactory.getLogger(LTParameterImpl.class);
	
	@Autowired
	@Qualifier("serviceConfig")
	private ServiceConfig config;
	
	@Autowired
	@Qualifier("ltparameterService")
	private LTParameterService ltparameterService;
	
	@Override
	@ApiOperation(value = "Search Office API", produces = "application/json", response = OfficeMaster.class, httpMethod = "GET")
	@TokenSecured
	@RequestMapping(value = "/parameter/lt-offices", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> searchOffice(
			@RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {
		ApiCallResult callResult = new ApiCallResult();
		/*if (!refresh) {
			logger.info("try to search lt Office from redis ...");
			String jsonStringTextileProductCategory = RedisUtil.get("ltOfficesCache");
			proTypeList = JSON.parseArray(jsonStringTextileProductCategory, OfficeMaster.class);
		}
		if (null == proTypeList) {*/
			logger.info("Can not find from redis search from aims service");
			try {
				callResult = ltparameterService.searchOffice();
				logger.info("saving searchOffice");
				//RedisUtil.set("ltOfficesCache", JSON.toJSONString(proTypeList), RedisUtil.HOUR * 24);

				return new ResponseEntity<ApiCallResult>(callResult, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("search office error: " + ExceptionUtils.getFullStackTrace(e));
				return new ResponseEntity<ApiCallResult>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		/*} else {
			logger.info("get lt offices from redis successfully");
			return new ResponseEntity<List<OfficeMaster>>(proTypeList, HttpStatus.OK);
		}*/
	}


	@Override
	@ApiOperation(value = "Search LT Program API", produces = "application/json", response = Program.class, httpMethod = "GET")
	@TokenSecured
	@RequestMapping(value = "/parameter/lt-programs/{programId}", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> searchProgram(
			@RequestParam(value = "refresh", defaultValue = "false") boolean refresh,
			@PathVariable String programId) {
		ApiCallResult callResult = new ApiCallResult();
		/*if (!refresh) {
			logger.info("try to searchProgram from redis ...");
			String jsonStringTextileProductCategory = RedisUtil.get("ltProgramCache");
			program = JSON.parseObject(jsonStringTextileProductCategory, Program.class);
		}
		if (null == program) {*/
			try {				
				callResult = ltparameterService.searchProgram(programId);				
				logger.info("saving searchProgram");
				//RedisUtil.set("ltProgramCache", JSON.toJSONString(program), RedisUtil.HOUR * 24);

				return new ResponseEntity<ApiCallResult>(callResult, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("search Program error: " + ExceptionUtils.getFullStackTrace(e));
				callResult.setMessage("can't get LT program by programId:" + programId);
				return new ResponseEntity<ApiCallResult>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		/*} else {
			logger.info("get lt program from redis successfully");
			return new ResponseEntity<Program>(program, HttpStatus.OK);
		}*/
	}	
	
	@Override
	@ApiOperation(value = "Search LT Tests By tag Name API", produces = "application/json", response = TagTestMap.class, httpMethod = "GET", responseContainer = "List")
	@TokenSecured
	@RequestMapping(value = "/parameter/lt-tests-by-tag", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> searchTestsByTag(			
			@RequestParam(value = "refresh", defaultValue = "false") boolean refresh,
			@RequestParam(value = "countries", required = false) List<String> countries, 
			@RequestParam(value = "testNames", required = false) List<String> testNames,
			@RequestParam(value = "regions", required = false) List<String> regions, 
			@RequestParam(value = "tagLevel", required = false) String tagLevel,
			@RequestParam(value = "productCategory", required = false) String productCategory) {
		ApiCallResult callResult = new ApiCallResult();
		/*if (!refresh) {
			logger.info("try to searchTests from redis ...");
			String jsonStringTextileProductCategory = RedisUtil.get("ltTestsCache");
			tests = JSON.parseArray(jsonStringTextileProductCategory, TestMaster.class);
		}
		if (null == tests) {*/
			try {
				callResult = ltparameterService.searchTestsByTag(countries, testNames, regions, tagLevel, productCategory);
				logger.info("saving searchTests");
				//RedisUtil.set("ltTestsCache", JSON.toJSONString(tests), RedisUtil.HOUR * 24);

				return new ResponseEntity<ApiCallResult>(callResult, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("search LT Tests error: " + ExceptionUtils.getFullStackTrace(e));
				callResult.setMessage("can't get LT tests");
				return new ResponseEntity<ApiCallResult>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		/*} else {
			logger.info("get lt tests from redis successfully");
			return new ResponseEntity<List<TestMaster>>(tests, HttpStatus.OK);
		}*/
	}
		
	@Override
	@ApiOperation(value = "Search LT Test By Test Id API", produces = "application/json", response = TestMaster.class, httpMethod = "GET")
	@TokenSecured
	@RequestMapping(value = "/parameter/lt-tests/{testId}", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> searchTest(
			@RequestParam(value = "refresh", defaultValue = "false") boolean refresh,
			@PathVariable String testId) {
		ApiCallResult callResult = new ApiCallResult();
		/*if (!refresh) {
			logger.info("try to searchTest from redis ...");
			String jsonStringTextileProductCategory = RedisUtil.get("ltTestCache");
			test = JSON.parseObject(jsonStringTextileProductCategory, TestMaster.class);
		}
		if (null == test) {*/
			try {
				callResult = ltparameterService.searchTest(testId);
				logger.info("saving searchTest");
				//RedisUtil.set("ltTestCache", JSON.toJSONString(test), RedisUtil.HOUR * 24);

				return new ResponseEntity<ApiCallResult>(callResult, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("search LT Test error: " + ExceptionUtils.getFullStackTrace(e));
				callResult.setMessage("can't get LT test by testId:" + testId);
				return new ResponseEntity<ApiCallResult>(callResult, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		/*} else {
			logger.info("get lt test from redis successfully");
			return new ResponseEntity<TestMaster>(test, HttpStatus.OK);
		}*/
	}
	
	@Override
	@ApiOperation(value = "Search LT Test By Name API", produces = "application/json", response = TestMaster.class, httpMethod = "GET")
	@TokenSecured
	@RequestMapping(value = "/parameter/lt-tests-by-name", method = RequestMethod.GET)	
	public ResponseEntity<ApiCallResult> searchTestsByName(
			@RequestParam(value = "refresh", defaultValue = "false") boolean refresh,
			@RequestParam(value = "testName") String testName) {
		ApiCallResult callResult = new ApiCallResult();
		/*if (!refresh) {
			logger.info("try to searchTest y name from redis ...");
			String jsonStringTextileProductCategory = RedisUtil.get("ltTestByNameCache");
			test = JSON.parseObject(jsonStringTextileProductCategory, TestMaster.class);
		}
		if (null == test) {*/
			try {
				callResult = ltparameterService.searchTestsByName(testName);
				logger.info("saving searchTest by name");
				//RedisUtil.set("ltTestByNameCache", JSON.toJSONString(test), RedisUtil.HOUR * 24);

				return new ResponseEntity<ApiCallResult>(callResult, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("search LT Test error: " + ExceptionUtils.getFullStackTrace(e));
				callResult.setMessage("can't get LT test by test name:" + testName);
				return new ResponseEntity<ApiCallResult>(callResult, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		/*} else {
			logger.info("get lt test from redis successfully");
			return new ResponseEntity<TestMaster>(test, HttpStatus.OK);
		}*/		
	}
}
