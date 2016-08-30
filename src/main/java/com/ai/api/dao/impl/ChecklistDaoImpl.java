package com.ai.api.dao.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.ChecklistDao;
import com.ai.commons.HttpUtil;
import com.ai.commons.StringUtils;
import com.ai.commons.beans.GetRequest;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.checklist.api.ChecklistBean;
import com.ai.commons.beans.checklist.api.ChecklistSearchCriteriaBean;
import com.ai.commons.beans.checklist.api.SimpleChecklistBean;
import com.ai.commons.beans.checklist.vo.CKLChecklistSearchVO;
import com.ai.commons.beans.checklist.vo.CKLChecklistVO;
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
	public List<CKLChecklistSearchVO> searchPrivateChecklist(String userId,String keyword,int pageNumber) {
        String url = config.getChecklistServiceUrl() + "/ws/"+userId+"/private/checklists?pageNumber="+pageNumber;
        if (StringUtils.isNotBlank(keyword))
            url = url+"&keyword="+keyword;
		try {
            GetRequest request = GetRequest.newInstance().setUrl(url);
            logger.info("searchPrivateChecklist - get!!! Url:"+url);
            ServiceCallResult result = HttpUtil.issueGetRequest(request);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {

				return JSON.parseArray(result.getResponseString(),CKLChecklistSearchVO.class);

			} else {
				logger.error("searchChecklist from checklist-service error: " +
                        result.getStatusCode() +", " +
                        result.getResponseString());
			}
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public List<CKLChecklistSearchVO> searchPublicChecklist(String userId,String keyword,int pageNumber) {
        String url = config.getChecklistServiceUrl() + "/ws/"+userId+"/public/checklists?pageNumber="+pageNumber;
        if (StringUtils.isNotBlank(keyword))
            url = url+"&keyword="+keyword;
		try {
            GetRequest request = GetRequest.newInstance().setUrl(url);
            logger.info("searchPublicChecklist - get!!! Url:"+url);
            ServiceCallResult result = HttpUtil.issueGetRequest(request);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {

				return JSON.parseArray(result.getResponseString(),CKLChecklistSearchVO.class);

			} else {
				logger.error("searchPublicChecklist from checklist-service error: " +
                        result.getStatusCode() +", " +
                        result.getResponseString());
			}
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public String createChecklist(String userId,CKLChecklistVO checklistVO) {
		String url = config.getChecklistServiceUrl() + "/ws/"+userId+"/checklist/create";
		try {
			logger.info("createChecklist - POST Url:"+url+" || userId:"+userId+" || checklistVO:"+checklistVO);
			ServiceCallResult result = HttpUtil.issuePostRequest(url, null, checklistVO);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				return result.getResponseString();
			} else {
				logger.error("createChecklist from checklist-service error: " +
						result.getStatusCode() + ", " +
						result.getResponseString());
			}
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public String updateChecklist(String userId,String checklistId,CKLChecklistVO checklist) {
        String url = config.getChecklistServiceUrl() + "/ws/"+userId+"/checklist/"+checklistId+"/update";
		try {
			logger.info("updateChecklist - POST  Url:"+url+" || checklist:"+checklist);
			ServiceCallResult result = HttpUtil.issuePostRequest(url, null, checklist);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				return result.getResponseString();
			} else {
				logger.error("updateChecklist from checklist-service error: " +
                        result.getStatusCode() + ", " +
                        result.getResponseString());
			}
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public CKLChecklistVO getChecklist(String userId,String checklistId) {
        String url = config.getChecklistServiceUrl() + "/ws/"+userId+"/checklist/"+checklistId+"/getById";
		try {
			GetRequest request = GetRequest.newInstance().setUrl(url);
			logger.info("getChecklist - get!!! Url:"+url+" userId:"+userId+" checklistId:"+checklistId);
			ServiceCallResult result = HttpUtil.issueGetRequest(request);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				return  JSON.parseObject(result.getResponseString(),CKLChecklistVO.class);

			} else {
				logger.error("GET Checklist from checklist-service error: " +
                        result.getStatusCode() +", " +
                        result.getResponseString());
			}
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public  boolean deleteChecklist(String userId,String ids) {
        String url = config.getChecklistServiceUrl() + "/ws/"+userId+"/checklist/"+ids+"/delete";
		try {
            GetRequest request = GetRequest.newInstance().setUrl(url);
            logger.info("deleteChecklist - get!!! Url:"+url);
            ServiceCallResult result = HttpUtil.issueGetRequest(request);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
					return true;
			} else {
				logger.error("updateChecklist from checklist-service error: " +
                        result.getStatusCode() + ", " +
                        result.getResponseString());
			}
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

	@Override
	public  boolean checklistNameExist(String userId,String checklistName) {
        String url = config.getChecklistServiceUrl() + "/ws/"+userId+"/checklistName/"+checklistName+"/exisitName";
		try {
            GetRequest request = GetRequest.newInstance().setUrl(url);
            logger.info("checklistNameExist - get!!! Url:"+url);
            ServiceCallResult result = HttpUtil.issueGetRequest(request);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				if ("true".equals(result.getResponseString())){
                    logger.info("checklistNameExist --->> true");
				    return true;
				}else {
				    logger.info("checklistNameExist --->> false");
                }
			} else {
				logger.error("checklistNameExist from checklist-service error: " +
                        result.getStatusCode() +", " +
                        result.getResponseString());
			}
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

    @Override
    public  boolean saveFeedback(String userId,String checklistId,String feedback) {
        String url = config.getChecklistServiceUrl() + "/ws/"+userId+"/checklist/"+checklistId+"/feedback";
        try {
            logger.info("saveFeedback - POST!!! Url:"+url+" || feedback:" +feedback);
            ServiceCallResult result = HttpUtil.issuePostRequest(url, null, feedback);
            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
                if ("true".equals(result.getResponseString())){
                    logger.info("saveFeedback --->> ok");
                    return true;
                }else {
                    logger.info("saveFeedback --->> fail");
                }
            } else {
                logger.error("saveFeedback from checklist-service error: " +
                        result.getStatusCode() + ", " +
                        result.getResponseString());
            }
        } catch (IOException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return false;
    }

    @Override
    public  boolean approved(String userId,String checklistId) {
        String url = config.getChecklistServiceUrl() + "/ws/"+userId+"/checklist/"+checklistId+"/approved";
        try {
            logger.info("approved - POST!!! Url:"+url+" || checklistId:" +checklistId);
            ServiceCallResult result = HttpUtil.issuePostRequest(url, null, checklistId);
            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
                if ("true".equals(result.getResponseString())){
                    logger.info("approved --->> pass");
                    return true;
                }else {
                    logger.info("approved --->> fail");
                }
            } else {
                logger.error("approved from checklist-service error: " +
                        result.getStatusCode() + ", " +
                        result.getResponseString());
            }
        } catch (IOException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return false;
    }
}
