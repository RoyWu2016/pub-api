package com.ai.api.dao.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.ChecklistDao;
import com.ai.commons.HttpUtil;
import com.ai.commons.beans.GetRequest;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.checklist.api.ChecklistBean;
import com.ai.commons.beans.checklist.api.ChecklistSearchCriteriaBean;
import com.ai.commons.beans.checklist.api.SimpleChecklistBean;
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
	public List<SimpleChecklistBean> searchChecklist(ChecklistSearchCriteriaBean criteria) {
		String url = config.getMwServiceUrl() + "/service/checklist/private";
		try {
			ServiceCallResult result = HttpUtil.issuePostRequest(url, null, criteria);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {

				return JSON.parseArray(result.getResponseString(),SimpleChecklistBean.class);

			} else {
				logger.error("searchChecklist from middleware error: " + result.getStatusCode() +
						", " + result.getResponseString());
			}

		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public List<SimpleChecklistBean> searchPublicChecklist(ChecklistSearchCriteriaBean criteria) {
		String url = config.getMwServiceUrl() + "/service/checklist/public";
		try {
			ServiceCallResult result = HttpUtil.issuePostRequest(url, null, criteria);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {

				return JSON.parseArray(result.getResponseString(),SimpleChecklistBean.class);

			} else {
				logger.error("searchPublicChecklist from middleware error: " + result.getStatusCode() +
						", " + result.getResponseString());
			}

		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public String createChecklist(String login,ChecklistBean checklistBean) {
		String url = config.getMwServiceUrl() + "/service/checklist/create?login="+login;
		try {
//			Map<String,Object> dataMap = new HashMap<>();
//			dataMap.put("login",login);
//			dataMap.put("checklistBean",checklistBean);
			logger.info("create!!! Url:"+url+" login:"+login+" checklistBean:"+checklistBean.toString());
			ServiceCallResult result = HttpUtil.issuePostRequest(url, null, checklistBean);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				return result.getResponseString();
			} else {
				logger.error("createChecklist from middleware error: " + result.getStatusCode() +
						", " + result.getResponseString());
			}

		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public String updateChecklist(String login,ChecklistBean checklistBean) {
		String url = config.getMwServiceUrl() + "/service/checklist/update?login="+login;
		try {
//			Map<String,Object> dataMap = new HashMap<>();
//			dataMap.put("login",login);
//			dataMap.put("checklistBean",checklistBean);
			logger.info("update!!! Url:"+url+" login:"+login+" checklistBean:"+checklistBean.toString());
			ServiceCallResult result = HttpUtil.issuePostRequest(url, null, checklistBean);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				return result.getResponseString();
			} else {
				logger.error("updateChecklist from middleware error: " + result.getStatusCode() +
						", " + result.getResponseString());
			}

		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public ChecklistBean getChecklist(String login,String checklistId) {
		String url = config.getMwServiceUrl() + "/service/checklist/get/"+checklistId+"?login="+login;
		try {
			GetRequest request = GetRequest.newInstance().setUrl(url);
			logger.debug("get!!! Url:"+url+" login:"+login+" checklistId:"+checklistId);
			ServiceCallResult result = HttpUtil.issueGetRequest(request);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				return  JSON.parseObject(result.getResponseString(),ChecklistBean.class);

			} else {
				logger.error("GET Checklist from middleware error: " + result.getStatusCode() +
						", " + result.getResponseString());
			}

		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public  boolean deleteChecklist(String login,String ids) {
		String url = config.getMwServiceUrl() + "/service/checklist/delete";
		try {
			Map<String,Object> dataMap = new HashMap<>();
			dataMap.put("login",login);
			dataMap.put("ids",ids);
			logger.debug("delete!!! post Url:"+url+" login:"+login+" ids:"+ids);
			ServiceCallResult result = HttpUtil.issuePostRequest(url, null, dataMap);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
					return true;
			} else {
				logger.error("updateChecklist from middleware error: " + result.getStatusCode() +
						", " + result.getResponseString());
			}

		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

	@Override
	public  boolean checklistNameExist(String login,String checklistName) {
		String url = config.getMwServiceUrl() + "/service/checklist/checklistNameExist";
		try {
			Map<String,Object> dataMap = new HashMap<>();
			dataMap.put("login",login);
			dataMap.put("checklistName",checklistName);
			ServiceCallResult result = HttpUtil.issuePostRequest(url, null, dataMap);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				if ("true".equals(result.getResponseString())){
                    logger.info("checklistNameExist --->> true");
				    return true;
				}else {
				    logger.info("checklistNameExist --->> false");
                }
			} else {
				logger.error("checklistNameExist from middleware error: " + result.getStatusCode() +
						", " + result.getResponseString());
			}
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

    @Override
    public  boolean saveFeedback(String login,String checklistId,String feedback) {
        String url = config.getMwServiceUrl() + "/service/checklist/"+checklistId+"/feedback";
        try {
            Map<String,Object> dataMap = new HashMap<>();
            dataMap.put("login",login);
            dataMap.put("checklistId",checklistId);
            dataMap.put("feedback",feedback);
			logger.info("do post url : "+url);
			logger.info("dataMap :"+dataMap);
            ServiceCallResult result = HttpUtil.issuePostRequest(url, null, dataMap);
            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
                if ("true".equals(result.getResponseString())){
                    logger.info("saveFeedback --->> ok");
                    return true;
                }else {
                    logger.info("saveFeedback --->> fail");
                }
            } else {
                logger.error("saveFeedback from middleware error: " + result.getStatusCode() +
                        ", " + result.getResponseString());
            }
        } catch (IOException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return false;
    }

    @Override
    public  boolean approved(String login,String checklistId) {
        String url = config.getMwServiceUrl() + "/service/checklist/"+checklistId+"/approved";
        try {
            Map<String,Object> dataMap = new HashMap<>();
            dataMap.put("login",login);
            dataMap.put("checklistId",checklistId);
            ServiceCallResult result = HttpUtil.issuePostRequest(url, null, dataMap);
            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
                if ("true".equals(result.getResponseString())){
                    logger.info("approved --->> pass");
                    return true;
                }else {
                    logger.info("approved --->> fail");
                }
            } else {
                logger.error("approved from middleware error: " + result.getStatusCode() +
                        ", " + result.getResponseString());
            }
        } catch (IOException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return false;
    }
}
