package com.ai.api.controller.impl;

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
import com.ai.aims.services.model.TestMaster;
import com.ai.api.config.ServiceConfig;
import com.ai.api.controller.LTParameter;
import com.ai.api.lab.service.LTParameterService;
import com.ai.api.util.RedisUtil;
import com.ai.commons.annotation.TokenSecured;
import com.ai.program.model.Program;
import com.alibaba.fastjson.JSON;

import io.swagger.annotations.ApiOperation;

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
	public ResponseEntity<List<OfficeMaster>> searchOffice(
			@RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {
		List<OfficeMaster> proTypeList = null;
		/*if (!refresh) {
			logger.info("try to search lt Office from redis ...");
			String jsonStringTextileProductCategory = RedisUtil.get("ltOfficesCache");
			proTypeList = JSON.parseArray(jsonStringTextileProductCategory, OfficeMaster.class);
		}
		if (null == proTypeList) {*/
			logger.info("Can not find from redis search from aims service");
			try {
				proTypeList = ltparameterService.searchOffice();

				logger.info("saving searchOffice");
				RedisUtil.set("ltOfficesCache", JSON.toJSONString(proTypeList), RedisUtil.HOUR * 24);

				return new ResponseEntity<List<OfficeMaster>>(proTypeList, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("search office error: " + ExceptionUtils.getFullStackTrace(e));
				return new ResponseEntity<List<OfficeMaster>>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		/*} else {
			logger.info("get lt offices from redis successfully");
			return new ResponseEntity<List<OfficeMaster>>(proTypeList, HttpStatus.OK);
		}*/
	}

	@Override
	@ApiOperation(value = "Search LT Program API", produces = "application/json", response = Program.class, httpMethod = "GET", responseContainer = "List")
	@TokenSecured
	@RequestMapping(value = "/{userId}/parameter/lt-programs", method = RequestMethod.GET)
	public ResponseEntity<List<Program>> searchPrograms(
			@RequestParam(value = "refresh", defaultValue = "false") boolean refresh,
			@PathVariable String userId) {
		List<Program> programs = null;
		/*if (!refresh) {
			logger.info("try to searchPrograms from redis ...");
			String jsonStringTextileProductCategory = RedisUtil.get("ltProgramsCache");
			programs = JSON.parseArray(jsonStringTextileProductCategory, Program.class);
		}
		if (null == programs) {*/
			try {
				programs = ltparameterService.searchPrograms(userId);

				logger.info("saving searchPrograms");
				RedisUtil.set("ltProgramsCache", JSON.toJSONString(programs), RedisUtil.HOUR * 24);

				return new ResponseEntity<List<Program>>(programs, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("search Programs error: " + ExceptionUtils.getFullStackTrace(e));
				return new ResponseEntity<List<Program>>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		/*} else {
			logger.info("get lt programs from redis successfully");
			return new ResponseEntity<List<Program>>(programs, HttpStatus.OK);
		}*/
	}
	
	@Override
	@ApiOperation(value = "Search LT Program API", produces = "application/json", response = Program.class, httpMethod = "GET")
	@TokenSecured
	@RequestMapping(value = "/parameter/lt-programs/{programId}", method = RequestMethod.GET)
	public ResponseEntity<Program> searchProgram(
			@RequestParam(value = "refresh", defaultValue = "false") boolean refresh,
			@PathVariable String programId) {
		Program program = null;
		/*if (!refresh) {
			logger.info("try to searchProgram from redis ...");
			String jsonStringTextileProductCategory = RedisUtil.get("ltProgramCache");
			program = JSON.parseObject(jsonStringTextileProductCategory, Program.class);
		}
		if (null == program) {*/
			try {				
				program = ltparameterService.searchProgram(programId);

				logger.info("saving searchProgram");
				RedisUtil.set("ltProgramCache", JSON.toJSONString(program), RedisUtil.HOUR * 24);

				return new ResponseEntity<Program>(program, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("search Program error: " + ExceptionUtils.getFullStackTrace(e));
				return new ResponseEntity<Program>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		/*} else {
			logger.info("get lt program from redis successfully");
			return new ResponseEntity<Program>(program, HttpStatus.OK);
		}*/
	}	
	
	@Override
	@ApiOperation(value = "Search LT Tests API", produces = "application/json", response = TestMaster.class, httpMethod = "GET", responseContainer = "List")
	@TokenSecured
	@RequestMapping(value = "/parameter/lt-tests", method = RequestMethod.GET)
	public ResponseEntity<List<TestMaster>> searchTests(			
			@RequestParam(value = "refresh", defaultValue = "false") boolean refresh,
			@RequestParam(value = "countryName", required = false) String countryName, 
			@RequestParam(value = "testName", required = false) String testName) {
		List<TestMaster> tests = null;
		/*if (!refresh) {
			logger.info("try to searchTests from redis ...");
			String jsonStringTextileProductCategory = RedisUtil.get("ltTestsCache");
			tests = JSON.parseArray(jsonStringTextileProductCategory, TestMaster.class);
		}
		if (null == tests) {*/
			try {
				tests = ltparameterService.searchTests(countryName, testName);

				logger.info("saving searchTests");
				RedisUtil.set("ltTestsCache", JSON.toJSONString(tests), RedisUtil.HOUR * 24);

				return new ResponseEntity<List<TestMaster>>(tests, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("search LT Tests error: " + ExceptionUtils.getFullStackTrace(e));
				return new ResponseEntity<List<TestMaster>>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		/*} else {
			logger.info("get lt tests from redis successfully");
			return new ResponseEntity<List<TestMaster>>(tests, HttpStatus.OK);
		}*/
	}
	
	@Override
	@ApiOperation(value = "Search LT Test API", produces = "application/json", response = TestMaster.class, httpMethod = "GET")
	@TokenSecured
	@RequestMapping(value = "/parameter/lt-tests/{testId}", method = RequestMethod.GET)
	public ResponseEntity<TestMaster> searchTest(
			@RequestParam(value = "refresh", defaultValue = "false") boolean refresh,
			@PathVariable String testId) {
		TestMaster test = null;
		/*if (!refresh) {
			logger.info("try to searchTest from redis ...");
			String jsonStringTextileProductCategory = RedisUtil.get("ltTestCache");
			test = JSON.parseObject(jsonStringTextileProductCategory, TestMaster.class);
		}
		if (null == test) {*/
			try {
				test = ltparameterService.searchTest(testId);

				logger.info("saving searchTest");
				RedisUtil.set("ltTestCache", JSON.toJSONString(test), RedisUtil.HOUR * 24);

				return new ResponseEntity<TestMaster>(test, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("search LT Test error: " + ExceptionUtils.getFullStackTrace(e));
				return new ResponseEntity<TestMaster>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		/*} else {
			logger.info("get lt test from redis successfully");
			return new ResponseEntity<TestMaster>(test, HttpStatus.OK);
		}*/
	}	
}
