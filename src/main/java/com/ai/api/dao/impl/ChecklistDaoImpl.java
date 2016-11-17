package com.ai.api.dao.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.ChecklistDao;
import com.ai.commons.HttpUtil;
import com.ai.commons.StringUtils;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.GetRequest;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.checklist.api.ChecklistBean;
import com.ai.commons.beans.checklist.api.ChecklistSearchCriteriaBean;
import com.ai.commons.beans.checklist.api.SimpleChecklistBean;
import com.ai.commons.beans.checklist.vo.CKLChecklistSearchVO;
import com.ai.commons.beans.checklist.vo.CKLChecklistVO;
import com.ai.commons.beans.checklist.vo.CKLDefectVO;
import com.ai.commons.beans.checklist.vo.CKLTestVO;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/***************************************************************************
 * <PRE>
 * Project Name    : Public-API
 * <p>
 * Package Name    : com.ai.api.dao.impl
 * <p>
 * Creation Date   : 2016/7/28 10:52
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

@Component
public class ChecklistDaoImpl implements ChecklistDao {

	private static final Logger logger = LoggerFactory.getLogger(ChecklistDaoImpl.class);

	@Autowired
	@Qualifier("serviceConfig")
	private ServiceConfig config;

	@Override
	public List<CKLChecklistSearchVO> searchPrivateChecklist(String userId, String keyword, int pageNumber) {
		String url = config.getChecklistServiceUrl() + "/ws/" + userId + "/private/checklists?pageNumber=" + pageNumber;
		if (StringUtils.isNotBlank(keyword))
			url = url + "&keyword=" + keyword;
		try {
			GetRequest request = GetRequest.newInstance().setUrl(url);
			logger.info("searchPrivateChecklist - get!!! Url:" + url);
			ServiceCallResult result = HttpUtil.issueGetRequest(request);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {

				return JSON.parseArray(result.getResponseString(), CKLChecklistSearchVO.class);

			} else {
				logger.error("searchChecklist from checklist-service error: " + result.getStatusCode() + ", "
						+ result.getResponseString());
			}
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public List<CKLChecklistSearchVO> searchPublicChecklist(String userId, String keyword, int pageNumber) {
		String url = config.getChecklistServiceUrl() + "/ws/" + userId + "/public/checklists?pageNumber=" + pageNumber;
		if (StringUtils.isNotBlank(keyword))
			url = url + "&keyword=" + keyword;
		try {
			GetRequest request = GetRequest.newInstance().setUrl(url);
			logger.info("searchPublicChecklist - get!!! Url:" + url);
			ServiceCallResult result = HttpUtil.issueGetRequest(request);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {

				return JSON.parseArray(result.getResponseString(), CKLChecklistSearchVO.class);

			} else {
				logger.error("searchPublicChecklist from checklist-service error: " + result.getStatusCode() + ", "
						+ result.getResponseString());
			}
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public String createChecklist(String userId, CKLChecklistVO checklistVO) {
		// // for test ..................
		// logger.info("for test ..............");
		// String url ="http://192.168.2.133:8888/checklist-service" +
		// "/ws/"+userId+"/checklist/create";
		String url = config.getChecklistServiceUrl() + "/ws/" + userId + "/checklist/create";
		try {
			logger.info(
					"createChecklist - POST Url:" + url + " || userId:" + userId + " || checklistVO:" + checklistVO);
			ServiceCallResult result = HttpUtil.issuePostRequest(url, null, checklistVO);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				return result.getResponseString();
			} else {
				logger.error("createChecklist from checklist-service error: " + result.getStatusCode() + ", "
						+ result.getResponseString());
			}
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public String updateChecklist(String userId, String checklistId, CKLChecklistVO checklist) {
		String url = config.getChecklistServiceUrl() + "/ws/" + userId + "/checklist/" + checklistId + "/update";
		try {
			logger.info("updateChecklist - POST  Url:" + url + " || checklist:" + checklist);
			ServiceCallResult result = HttpUtil.issuePostRequest(url, null, checklist);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				return result.getResponseString();
			} else {
				logger.error("updateChecklist from checklist-service error: " + result.getStatusCode() + ", "
						+ result.getResponseString());
			}
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public CKLChecklistVO getChecklist(String userId, String checklistId) {
		String url = config.getChecklistServiceUrl() + "/ws/" + userId + "/checklist/" + checklistId + "/getById";
		try {
			GetRequest request = GetRequest.newInstance().setUrl(url);
			logger.info("getChecklist - get!!! Url:" + url + " userId:" + userId + " checklistId:" + checklistId);
			ServiceCallResult result = HttpUtil.issueGetRequest(request);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				return JSON.parseObject(result.getResponseString(), CKLChecklistVO.class);

			} else {
				logger.error("GET Checklist from checklist-service error: " + result.getStatusCode() + ", "
						+ result.getResponseString());
			}
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public boolean deleteChecklist(String userId, String ids) {
		String url = config.getChecklistServiceUrl() + "/ws/" + userId + "/checklist/" + ids + "/delete";
		try {
			GetRequest request = GetRequest.newInstance().setUrl(url);
			logger.info("deleteChecklist - get!!! Url:" + url);
			ServiceCallResult result = HttpUtil.issueGetRequest(request);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				return true;
			} else {
				logger.error("updateChecklist from checklist-service error: " + result.getStatusCode() + ", "
						+ result.getResponseString());
			}
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

	@Override
	public boolean checklistNameExist(String userId, String checklistName) {
		String url = config.getChecklistServiceUrl() + "/ws/" + userId + "/checklistName/" + checklistName
				+ "/exisitName";
		try {
			GetRequest request = GetRequest.newInstance().setUrl(url);
			logger.info("checklistNameExist - get!!! Url:" + url);
			ServiceCallResult result = HttpUtil.issueGetRequest(request);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				if ("true".equalsIgnoreCase(result.getResponseString())) {
					logger.info("checklistNameExist --->> true");
					return true;
				} else {
					logger.info("checklistNameExist --->> false");
				}
			} else {
				logger.error("checklistNameExist from checklist-service error: " + result.getStatusCode() + ", "
						+ result.getResponseString());
			}
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

	@Override
	public boolean saveFeedback(String userId, String checklistId, String feedback) {
		String url = config.getChecklistServiceUrl() + "/ws/" + userId + "/checklist/" + checklistId + "/feedback";
		try {
			logger.info("saveFeedback - POST!!! Url:" + url + " || feedback:" + feedback);
			ServiceCallResult result = HttpUtil.issuePostRequest(url, null, feedback);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				if ("true".equalsIgnoreCase(result.getResponseString())) {
					logger.info("saveFeedback --->> ok");
					return true;
				} else {
					logger.info("saveFeedback --->> fail");
				}
			} else {
				logger.error("saveFeedback from checklist-service error: " + result.getStatusCode() + ", "
						+ result.getResponseString());
			}
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

	@Override
	public boolean approved(String userId, String checklistId) {
		String url = config.getChecklistServiceUrl() + "/ws/" + userId + "/checklist/" + checklistId + "/approved";
		try {
			logger.info("approved - POST!!! Url:" + url + " || checklistId:" + checklistId);
			ServiceCallResult result = HttpUtil.issuePostRequest(url, null, checklistId);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				if ("true".equalsIgnoreCase(result.getResponseString())) {
					logger.info("approved --->> pass");
					return true;
				} else {
					logger.info("approved --->> fail");
				}
			} else {
				logger.error("approved from checklist-service error: " + result.getStatusCode() + ", "
						+ result.getResponseString());
			}
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

	@Override
	public ApiCallResult calculateChecklistSampleSize(Integer productQty, String sampleLevel, String unit,
			String criticalDefects, String majorDefects, String minorDefects, Integer piecesNumberPerSet) {
		// TODO Auto-generated method stub
		StringBuilder url = new StringBuilder(config.getPsiServiceUrl() + "/order/api/checklistTestSampleSize");
		url.append("?productQuantity=" + productQty).append("&sampleLevel=" + sampleLevel)
				.append("&criticalDefects=" + criticalDefects).append("&majorDefects=" + majorDefects)
				.append("&minorDefects=" + minorDefects).append("&unit=" + unit);
		if (null != piecesNumberPerSet) {
			url.append("&nbOfPcsPerSet=" + piecesNumberPerSet);
		} else {
			url.append("&nbOfPcsPerSet=" + "");
		}
		logger.info("requesting Url:" + url.toString());
		GetRequest request = GetRequest.newInstance().setUrl(url.toString());
		ApiCallResult temp = new ApiCallResult();
		try {
			ServiceCallResult result = HttpUtil.issueGetRequest(request);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				temp = JSON.parseObject(result.getResponseString(), ApiCallResult.class);

				return temp;
			} else {
				logger.error("calculateChecklistSampleSize from psi-service error: " + result.getStatusCode() + ", "
						+ result.getResponseString());
				temp.setMessage("calculateChecklistSampleSize from psi-service error: " + result.getStatusCode() + ", "
						+ result.getResponseString());

				return temp;
			}
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			temp.setMessage(e.toString());

			return temp;
		}
	}

	@Override
	public ApiCallResult createTest(String userId, String testId) {
		// TODO Auto-generated method stub
		StringBuilder url = new StringBuilder(config.getChecklistServiceUrl() + "/ws/" + userId + "/createTest/" + testId);
		ApiCallResult temp = new ApiCallResult();
		try {
			logger.info("requesting: " + url.toString());
			ServiceCallResult result = HttpUtil.issuePostRequest(url.toString(), null, "");
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				temp.setContent(JSON.parseObject(result.getResponseString(), CKLTestVO.class));
				return temp;
			} else {
				logger.error("ChecklistService error: " + result.getStatusCode() + ", "+ result.getResponseString());
				temp.setMessage("ChecklistService error: " + result.getStatusCode() + ", "+ result.getResponseString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			temp.setMessage(e.toString());
			e.printStackTrace();
		}
		return temp;
	}

	@Override
	public ApiCallResult createDefect(String userId,  String defectId) {
		// TODO Auto-generated method stub
		StringBuilder url = new StringBuilder(config.getChecklistServiceUrl() + "/ws/" + userId + "/createDefect/" + defectId);
		ApiCallResult temp = new ApiCallResult();
		try {
			logger.info("requesting: " + url.toString());
			ServiceCallResult result = HttpUtil.issuePostRequest(url.toString(), null, "");
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				temp.setContent(JSON.parseObject(result.getResponseString(), CKLDefectVO.class));
				return temp;
			} else {
				logger.error("ChecklistService error: " + result.getStatusCode() + ", "+ result.getResponseString());
				temp.setMessage("ChecklistService error: " + result.getStatusCode() + ", "+ result.getResponseString());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			temp.setMessage(e.toString());
			e.printStackTrace();
		}
		return temp;
	}
}
