package com.ai.api.dao.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.DraftDao;
import com.ai.commons.HttpUtil;
import com.ai.commons.JsonUtil;
import com.ai.commons.beans.PageBean;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.order.draft.DraftOrder;
import com.ai.commons.beans.order.price.OrderPriceMandayViewBean;
import com.ai.commons.beans.psi.InspectionBookingBean;
import com.ai.commons.beans.psi.InspectionProductBookingBean;

/***************************************************************************
 * <PRE>
 * Project Name    : Public-API
 * <p>
 * Package Name    : com.ai.api.dao.impl
 * <p>
 * Creation Date   : 2016/8/1 16:49
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
public class DraftDaoImpl implements DraftDao {

	private static final Logger logger = LoggerFactory.getLogger(DraftDaoImpl.class);

	@Autowired
	@Qualifier("serviceConfig")
	private ServiceConfig config;

	@Override
	public boolean deleteDrafts(Map<String, String> params) {
		String url = config.getMwServiceUrl() + "/service/draft/delete";
		try {
			ServiceCallResult result = HttpUtil.issuePostRequest(url, null, params);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				return true;

			} else {
				logger.error("delete drafts from middleware error: " + result.getStatusCode() +
						", " + result.getResponseString());
			}

		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return false;
	}


	@Override
	public boolean deleteDraftsFromPsi(String userId,String compId, String parentId, String draftIds) {
        StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
        url.append("/draft/api/deleteDrafts");
        url.append("?userId=").append(userId);
        url.append("&companyId=").append(compId);
        url.append("&parentId=").append(parentId);
        url.append("&draftIds=").append(draftIds);
		try {
			ServiceCallResult result = HttpUtil.issueDeleteRequest(url.toString(), null);

			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK") && result.getResponseString().equals("true")) {
				return true;
			} else {
				logger.error("delete drafts from psi error: " + result.getStatusCode() +
						", " + result.getResponseString());
			}

		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return false;
	}


	@Override
	public InspectionBookingBean createDraft(String userId, String compId, String parentId, String serviceTypeStrValue) {
		StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
		url.append("/draft/api/createDraft");
        url.append("?userId=").append(userId);
		url.append("&companyId=").append(compId);
        url.append("&parentId=").append(parentId);
		url.append("&serviceType=").append(serviceTypeStrValue);
		try {
			ServiceCallResult result = HttpUtil.issuePostRequest(url.toString(), null, new HashMap<>());
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				return JsonUtil.mapToObject(result.getResponseString(), InspectionBookingBean.class);
			} else {
				logger.error("create draft error from psi service : " + result.getStatusCode() +
						", " + result.getResponseString());
			}

		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public InspectionBookingBean createDraftFromPreviousOrder(String userId, String companyId, String parentId, String orderId) {
		StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
        url.append("/draft/api/createDraftFromPreviousOrder");
        url.append("?userId=").append(userId);
        url.append("&companyId=").append(companyId);
        url.append("&parentId=").append(parentId);
        url.append("&orderId=").append(orderId);
		try {
			ServiceCallResult result = HttpUtil.issuePostRequest(url.toString(), null,orderId);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				return JsonUtil.mapToObject(result.getResponseString(), InspectionBookingBean.class);
			} else {
				logger.error("createDraftFromPreviousOrder error from psi service : " + result.getStatusCode() +
						", " + result.getResponseString());
			}

		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public InspectionBookingBean getDraft(String userId,String compId, String parentId, String draftId) {
		StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
		url.append("/draft/api/getDraft/").append(userId).append("/").append(draftId);
		try {
			ServiceCallResult result = HttpUtil.issueGetRequest(url.toString(), null);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				return JsonUtil.mapToObject(result.getResponseString(), InspectionBookingBean.class);
			} else {
				logger.error("create draft error from psi service : " + result.getStatusCode() +
						", " + result.getResponseString());
			}

		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public boolean saveDraft(String userId,String companyId,String parentId,InspectionBookingBean draft) {
		StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
		url.append("/draft/api/updateDraft/");
        url.append("?userId=").append(userId);
        url.append("&companyId=").append(companyId);
        url.append("&parentId=").append(parentId);
		try {
			ServiceCallResult result = HttpUtil.issuePostRequest(url.toString(), null,draft);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				return true;
			} else {
				logger.error("save draft error from psi service : " + result.getStatusCode() +
						", " + result.getResponseString());
			}

		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

    @Override
    public boolean addProduct(String userId,String companyId,String parentId,String draftId) {
        StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
        url.append("/draft/api/addProduct");
        url.append("?userId=").append(userId);
        url.append("&companyId=").append(companyId);
        url.append("&parentId=").append(parentId);
        url.append("&draftId=").append(draftId);
        try {
            logger.info("addProduct POST! URL : "+url.toString());
            ServiceCallResult result = HttpUtil.issuePostRequest(url.toString(), null,draftId);
            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
                return true;
            } else {
                logger.error("add product error from psi service : " + result.getStatusCode() +
                        ", " + result.getResponseString());
            }

        } catch (IOException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return false;
    }

	@Override
	public boolean saveProduct(String userId,String companyId,String parentId,InspectionProductBookingBean draftProduct) {
		StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
		url.append("/draft/api/updateProduct");
        url.append("?userId=").append(userId);
        url.append("&companyId=").append(companyId);
        url.append("&parentId=").append(parentId);
		try {
			logger.info("saveProduct POST! URL : "+url.toString());
			ServiceCallResult result = HttpUtil.issuePostRequest(url.toString(), null,draftProduct);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				return true;
			} else {
				logger.error("save product error from psi service : " + result.getStatusCode() +
						", " + result.getResponseString());
			}

		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

    @Override
    public boolean deleteProduct(String userId,String companyId,String parentId,String productId) {
        StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
        url.append("/draft/api/deleteProduct");
        url.append("?userId=").append(userId);
        url.append("&companyId=").append(companyId);
        url.append("&parentId=").append(parentId);
        url.append("&productDraftId=").append(productId);
        try {
            logger.info("deleteProduct DELETE! URL : "+url.toString());
            ServiceCallResult result = HttpUtil.issueDeleteRequest(url.toString(),null);
            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
                return true;
            } else {
                logger.error("delete product error from psi service : " + result.getStatusCode() +
                        ", " + result.getResponseString());
            }

        } catch (IOException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return false;
    }
    
	@Override
	public OrderPriceMandayViewBean calculatePricing(String userId, String companyId,String parentId,
			String draftId,String samplingLevel,String measurementSamplingSize) {
		// TODO Auto-generated method stub
		StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
		url.append("/draft/api/calculatePricing")
			.append("?userId=").append(userId)
			.append("&companyId=").append(companyId)
			.append("&parentId=").append(parentId)
			.append("&draftId=").append(draftId)
			.append("&samplingLevel=").append(samplingLevel)
			.append("&measurementSamplingLevel=").append(measurementSamplingSize);
		try {
			logger.info("Invoking: " + url.toString());
			ServiceCallResult result = HttpUtil.issuePostRequest(url.toString(),null,new HashMap<>());
			if (result.getStatusCode() == HttpStatus.OK.value() 
					&& result.getReasonPhase().equalsIgnoreCase("OK")) {
				return JsonUtil.mapToObject(result.getResponseString(), OrderPriceMandayViewBean.class);
			} else {
				logger.error("calculate Pricing error from psi service : " 
						+ result.getStatusCode() + ", "
						+ result.getResponseString());
			}

		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	@Override
	public List<DraftOrder> searchDraft(String userId, String compId, String parentId, String serviceType,
										String startDate, String endDate, String keyWord, String pageSize, String pageNumber) {
		try {

			  StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
			   url.append("/draft/api/search?userId=")
			   	  .append(userId)
			   	  .append("&companyId=").append(compId)
			   	  .append("&parentId=").append(parentId)
			   	  .append("&inspectionType=").append(serviceType)
			   	  .append("&startDate=").append(startDate)
			   	  .append("&endDate=").append(endDate)
			   	  .append("&keyWord=").append(keyWord)
			      .append("&pageSize=").append(pageSize)
			      .append("&pageNo=").append(pageNumber);
			   
			   ServiceCallResult result = HttpUtil.issueGetRequest(url.toString(), null);
				if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
					@SuppressWarnings("unchecked")
					PageBean<DraftOrder> pageBeanList = JsonUtil.mapToObject(result.getResponseString(),PageBean.class);
					
					return pageBeanList.getPageItems();

				} else {
					logger.error("searchDraftOrder from PSI error: " + result.getStatusCode() + ", " + result.getResponseString());
				}
			
		}catch(IOException e){
			logger.error(ExceptionUtils.getStackTrace(e));
			
		}
		
		return null;
		
	}
}

