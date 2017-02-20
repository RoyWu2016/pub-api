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
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

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

import com.ai.aims.services.model.TestMaster;
import com.ai.api.bean.OfficeSearchBean;
import com.ai.api.bean.ProductCategoryDtoBean;
import com.ai.api.bean.RegionSearchBean;
import com.ai.api.bean.TagSearchBean;
import com.ai.api.bean.TestSearchBean;
import com.ai.api.config.ServiceConfig;
import com.ai.api.controller.LTParameter;
import com.ai.api.service.LTParameterService;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.ApiCallResult;
import com.ai.program.model.Program;

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
	@ApiOperation(value = "Search Office API", produces = "application/json", response = OfficeSearchBean.class, httpMethod = "GET")
	@TokenSecured
	@RequestMapping(value = "/parameter/lt/offices", method = RequestMethod.GET)
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
	@RequestMapping(value = "/parameter/lt/programs/{programId}", method = RequestMethod.GET)
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
	@ApiOperation(value = "Search LT Tests with filters API", produces = "application/json", response = TestSearchBean.class, httpMethod = "GET", responseContainer = "List")
	@TokenSecured
	@RequestMapping(value = "/parameter/lt/tests", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> searchTestsWithFilters(			
			@ApiParam(value="Countries") @RequestParam(value = "countries", required = false, defaultValue = "") String countries, 
			@ApiParam(value="Regions") @RequestParam(value = "regions", required = false, defaultValue = "") String regions, 
			@ApiParam(value="Test Names") @RequestParam(value = "testNames", required = false, defaultValue = "") String testNames,
			@ApiParam(value="Tag IDs") @RequestParam(value = "tags", required = false, defaultValue = "") String tags,
			@ApiParam(value="Product Category ID") @RequestParam(value = "productCategory", required = false, defaultValue = "") String productCategory,
			@ApiParam(value="Office ID") @RequestParam(value = "office", required = false, defaultValue = "") String office) {
		ApiCallResult callResult = new ApiCallResult();
		/*if (!refresh) {
			logger.info("try to searchTests from redis ...");
			String jsonStringTextileProductCategory = RedisUtil.get("ltTestsCache");
			tests = JSON.parseArray(jsonStringTextileProductCategory, TestMaster.class);
		}
		if (null == tests) {*/
			try {
				callResult = ltparameterService.searchTestWithFilters(
						countries, regions, testNames, tags, productCategory, office);
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
	@RequestMapping(value = "/parameter/lt/test/{testId}", method = RequestMethod.GET)
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

	@ApiOperation(value = "Order Product Categories Get API", produces = "application/json", response = ProductCategoryDtoBean.class, httpMethod = "GET")
	@Override
	@TokenSecured
	@RequestMapping(value = "/parameter/lt/categories", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> searchCategories() {
		ApiCallResult callResult = new ApiCallResult();
		try {
			callResult = ltparameterService.searchCategories();
		} catch (Exception e) {
			logger.error("get product categories search error: " + ExceptionUtils.getFullStackTrace(e));
			callResult.setMessage("can't get LT product categories");
			return new ResponseEntity<ApiCallResult>(callResult, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ApiCallResult>(callResult, HttpStatus.OK);
	}

	@ApiOperation(value = "Order Tags Get API", produces = "application/json", response = TagSearchBean.class, httpMethod = "GET")
	@Override
	@TokenSecured
	@RequestMapping(value = "/parameter/lt/category/{categoryId}/tags", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> searchTagsByCategory(
			@ApiParam(value="Product Category ID") @PathVariable String categoryId) {
		ApiCallResult callResult = new ApiCallResult();
		try {
			callResult = ltparameterService.searchTags(categoryId);
		} catch (Exception e) {
			logger.error("get tags search error: " + ExceptionUtils.getFullStackTrace(e));
			callResult.setMessage("can't get LT tags");
			return new ResponseEntity<ApiCallResult>(callResult, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ApiCallResult>(callResult, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Search LT Regions API", produces = "application/json", response = RegionSearchBean.class, httpMethod = "GET")
	@Override
	@TokenSecured
	@RequestMapping(value = "/parameter/lt/regions", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> searchRegions() {
		ApiCallResult callResult = new ApiCallResult();
		try {
			callResult = ltparameterService.searchRegions();
		} catch (Exception e) {
			logger.error("get regions search error: " + ExceptionUtils.getFullStackTrace(e));
			callResult.setMessage("can't get LT regions");
			return new ResponseEntity<ApiCallResult>(callResult, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<ApiCallResult>(callResult, HttpStatus.OK);
	}
	
	@Override
	@ApiOperation(value = "Search Turnaround Time API", produces = "application/json", response = OfficeSearchBean.class, httpMethod = "GET")
	@TokenSecured
	@RequestMapping(value = "/parameter/lt/office/{officeId}/tats", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> searchTATs(
			@ApiParam(value="Office ID") @PathVariable String officeId) {
		ApiCallResult callResult = new ApiCallResult();
		try {
			callResult = ltparameterService.searchTATs(officeId);
			return new ResponseEntity<ApiCallResult>(callResult, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("search tat error: " + ExceptionUtils.getFullStackTrace(e));
			return new ResponseEntity<ApiCallResult>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
