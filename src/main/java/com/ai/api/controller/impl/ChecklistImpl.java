package com.ai.api.controller.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import com.ai.api.controller.Checklist;
import com.ai.api.service.ChecklistService;
import com.ai.commons.StringUtils;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.PageBean;
import com.ai.commons.beans.checklist.vo.CKLChecklistSearchVO;
import com.ai.commons.beans.checklist.vo.CKLChecklistVO;
import com.ai.commons.beans.checklist.vo.CKLDefectVO;
import com.ai.commons.beans.checklist.vo.CKLTestVO;
import com.ai.commons.util.JsonUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/***************************************************************************
 * <PRE>
 * Project Name    : Public-API
 * <p>
 * Package Name    : com.ai.api.controller.impl
 * <p>
 * Creation Date   : 2016/7/28 10:13
 * <p>
 * Author          : Jianxiong Cai
 * <p>
 * Purpose         : TODO
 * <p>
 * <p>
 * History         : TODO
 * <p>
 * </PRE>
 ***************************************************************************/

@RestController
@Api(tags = { "Checklist" }, description = "Checklist APIs")
public class ChecklistImpl implements Checklist {

	protected Logger logger = LoggerFactory.getLogger(ChecklistImpl.class);

	@Autowired
	private ChecklistService checklistService;

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/checklists", method = RequestMethod.GET)
	@ApiOperation(value = "Search User's Private Chcecklist", response = CKLChecklistSearchVO.class)
	public ResponseEntity<JSONArray> searchPrivateChecklist(
			@ApiParam(required = true) @PathVariable("userId") String userId,
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "pageNumber", required = false) String pageNumber) {
		logger.info("searchChecklist ...");
		logger.info("userId :" + userId);
		logger.info("keyword :" + keyword);
		logger.info("pageNumber :" + pageNumber);
		List<CKLChecklistSearchVO> result = checklistService.searchPrivateChecklist(userId, keyword,
				StringUtils.isBlank(pageNumber) ? 0 : Integer.valueOf(pageNumber));
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-YYYY", Locale.ENGLISH);
		JSONArray ja = new JSONArray();
		if (result != null) {
			ja = (JSONArray) JSON.toJSON(result);
			for (int i = 0; i < ja.size(); i++) {
				JSONObject myjObject = ja.getJSONObject(i);
				CKLChecklistSearchVO vo = null;
				try {
					vo = JsonUtils.toBean(myjObject.toJSONString(), CKLChecklistSearchVO.class);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.error("convert to bean error: " + e);
					return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
				String str = null;
				if (null != vo) {
					str = df.format(vo.getUpdateTime());
				}
				myjObject.put("updateTime", str);
			}
			return new ResponseEntity<>(ja, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/public-checklists", method = RequestMethod.GET)
	@ApiOperation(value = "Search User's Public Chcecklist", response = CKLChecklistSearchVO.class)
	public ResponseEntity<List<CKLChecklistSearchVO>> searchPublicChecklist(
			@ApiParam(required = true) @PathVariable("userId") String userId,
			@RequestParam(value = "keyword", required = false) String keyword,
			@RequestParam(value = "pageNumber", required = false) String pageNumber) {
		logger.info("searchPublicChecklist ...");
		logger.info("userId :" + userId);
		logger.info("keyword :" + keyword);
		logger.info("pageNumber :" + pageNumber);
		List<CKLChecklistSearchVO> result = checklistService.searchPublicChecklist(userId, keyword,
				StringUtils.isBlank(pageNumber) ? 0 : Integer.valueOf(pageNumber));
		if (result != null) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/checklist", method = RequestMethod.POST)
	@ApiOperation(value = "Create User's New Chcecklist", response = CKLChecklistVO.class)
	public ResponseEntity<ApiCallResult<CKLChecklistVO>> createChecklist(
			@ApiParam(required = true) @PathVariable("userId") String userId, @RequestBody CKLChecklistVO checklistVO) {
		logger.info("createChecklist ...");
		logger.info("userId :" + userId);
		logger.info("checklistBean :" + checklistVO.toString());
		ApiCallResult<CKLChecklistVO> result = checklistService.createChecklist(userId, checklistVO);
		if (result.getContent() != null) {
			logger.info("create result : " + result);
			return new ResponseEntity<ApiCallResult<CKLChecklistVO>>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<ApiCallResult<CKLChecklistVO>>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/checklist/{checklistId}", method = RequestMethod.PUT)
	@ApiOperation(value = "Update User's Checklist", response = CKLChecklistVO.class)
	public ResponseEntity<ApiCallResult<CKLChecklistVO>> updateChecklist(
			@ApiParam(required = true) @PathVariable("userId") String userId,
			@ApiParam(required = true) @PathVariable("checklistId") String checklistId,
			@ApiParam(required = true) @RequestBody CKLChecklistVO checklist) {
		logger.info("updateChecklist ...");
		logger.info("userId :" + userId);
		logger.info("checklistId :" + checklistId);
		logger.info("checklist :" + checklist);

		checklist.setCheckListId(checklistId);
		ApiCallResult<CKLChecklistVO> result = checklistService.updateChecklist(userId, checklistId, checklist);
		if (result.getContent() != null) {
			logger.info("update result : " + result);
			return new ResponseEntity<ApiCallResult<CKLChecklistVO>>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<ApiCallResult<CKLChecklistVO>>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/checklist/{checklistId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get User's Checklist", response = CKLChecklistVO.class)
	public ResponseEntity<CKLChecklistVO> getChecklist(@ApiParam(required = true) @PathVariable("userId") String userId,
			@ApiParam(required = true) @PathVariable("checklistId") String checklistId) {
		logger.info("getChecklist ...");
		logger.info("userId :" + userId);
		logger.info("checklistId :" + checklistId);
		CKLChecklistVO result = checklistService.getChecklist(userId, checklistId);
		if (result != null) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/checklists/{checklistIds}", method = RequestMethod.DELETE)
	@ApiOperation(value = "Delete User's Checklist", response = Boolean.class)
	public ResponseEntity deleteChecklist(@ApiParam(required = true) @PathVariable("userId") String userId,
			@ApiParam(required = true) @PathVariable("checklistIds") String checklistIds) {
		logger.info("deleteChecklist ...");
		logger.info("userId :" + userId);
		logger.info("checklistIds :" + checklistIds);

		boolean b = checklistService.deleteChecklist(userId, checklistIds);
		if (b) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/checklist-name/{checklistName}", method = RequestMethod.GET)
	@ApiOperation(value = "Check If User's Checklist Name Already Exist", response = Boolean.class)
	public ResponseEntity checklistNameExist(@ApiParam(required = true) @PathVariable("userId") String userId,
			@ApiParam(required = true) @PathVariable("checklistName") String checklistName) {
		logger.info("checklistNameExist ...");
		logger.info("userId :" + userId);
		logger.info("checklistName :" + checklistName);

		boolean b = checklistService.checklistNameExist(userId, checklistName);
		if (b) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/checklist/{checklistId}/feedback", method = RequestMethod.PUT)
	@ApiOperation(value = "User Give Feedback to a Chcecklist", response = Boolean.class)
	public ResponseEntity saveFeedback(@ApiParam(required = true) @PathVariable("userId") String userId,
			@ApiParam(required = true) @PathVariable("checklistId") String checklistId,
			@ApiParam(required = true) @RequestBody String feedback) {
		logger.info("saveFeedback ...");
		logger.info("userId :" + userId);
		logger.info("checklistId :" + checklistId);
		logger.info("feedback :" + feedback);
		boolean b = checklistService.saveFeedback(userId, checklistId, feedback);
		if (b) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/checklist/{checklistId}/approved", method = RequestMethod.PUT)
	@ApiOperation(value = "User Approve a Checklist", response = Boolean.class)
	public ResponseEntity approved(@ApiParam(required = true) @PathVariable("userId") String userId,
			@ApiParam(required = true) @PathVariable("checklistId") String checklistId) {
		logger.info("approved ...");
		logger.info("userId :" + userId);
		logger.info("checklistId :" + checklistId);
		boolean b = checklistService.approved(userId, checklistId);
		if (b) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/checklist/product-qty/{productQty}/sample-size", method = RequestMethod.GET)
	@ApiOperation(value = "Calculate Checklist Sample Size", response = Integer.class)
	public ResponseEntity<ApiCallResult> calculateChecklistSampleSize(
			@ApiParam(required = true) @PathVariable("userId") String userId,
			@ApiParam(required = true) @PathVariable("productQty") Integer productQty,
			@RequestParam(value = "sampleLevel", required = false, defaultValue = "") String sampleLevel,
			@RequestParam(value = "unit", required = false, defaultValue = "") String unit,
			@RequestParam(value = "criticalDefects", required = false, defaultValue = "") String criticalDefects,
			@RequestParam(value = "majorDefects", required = false, defaultValue = "") String majorDefects,
			@RequestParam(value = "minorDefects", required = false, defaultValue = "") String minorDefects,
			@RequestParam(value = "piecesNumberPerSet", required = false, defaultValue = "") Integer piecesNumberPerSet) {
		logger.info("invoke: " + "/user/" + userId + "/checklist/product-qty/" + productQty + "/sample-size");
		ApiCallResult result = new ApiCallResult();
		result = checklistService.calculateChecklistSampleSize(productQty, sampleLevel, unit, criticalDefects,
				majorDefects, minorDefects, piecesNumberPerSet);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/public-test/{testId}", method = RequestMethod.POST)
	@ApiOperation(value = "Create User's Private Test", response = CKLTestVO.class)
	public ResponseEntity<ApiCallResult> createTest(@ApiParam(required = true) @PathVariable("userId") String userId,
			@ApiParam(required = true) @PathVariable("testId") String testId) {
		logger.info("invoke: " + "/user/" + userId + "/test?testId=" + testId);
		ApiCallResult result = checklistService.createTest(userId, testId);
		if (null != result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			return new ResponseEntity<>(result, HttpStatus.OK);
		}

	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/public-defect/{defectId}", method = RequestMethod.POST)
	@ApiOperation(value = "Create User's Private Defect", response = CKLDefectVO.class)
	public ResponseEntity<ApiCallResult> createDefect(@ApiParam(required = true) @PathVariable("userId") String userId,
			@ApiParam(required = true) @PathVariable("defectId") String defectId) {
		logger.info("invoke: " + "/user/" + userId + "/defect?defectId=" + defectId);
		ApiCallResult result = checklistService.createDefect(userId, defectId);
		if (null != result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/public-checklist/{checklistId}", method = RequestMethod.POST)
	@ApiOperation(value = " Import Public Checklist To Draft In Booking", response = CKLChecklistVO.class)
	public ResponseEntity<ApiCallResult> importChecklist(
			@ApiParam(required = true) @PathVariable("userId") String userId,
			@ApiParam(required = true) @PathVariable("checklistId") String checklistId) {
		logger.info("invoke: " + "/user/" + userId + "/public-checklist/" + checklistId);
		ApiCallResult result = checklistService.importChecklist(userId, checklistId);
		if (null != result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
	}

}
