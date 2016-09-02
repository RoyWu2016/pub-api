package com.ai.api.dao.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.ai.api.bean.InspectionDraftBean;
import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.DraftDao;
import com.ai.api.util.AIUtil;
import com.ai.commons.HttpUtil;
import com.ai.commons.JsonUtil;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.order.Draft;
import com.ai.commons.beans.psi.InspectionBookingBean;
import com.ai.commons.beans.psi.InspectionProductBookingBean;
import com.ai.dto.JsonResponse;
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
	public boolean deleteDraftsFromPsi(String userId, String draftIds) {
		String url = config.getPsiServiceUrl() + "/draft/api/deleteDrafts/"+ userId + "/"+ draftIds;
		try {
			ServiceCallResult result = HttpUtil.issueDeleteRequest(url, null);

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
		url.append("/draft/api/createDraft").append("?userId=").append(userId);
		url.append("&companyId=").append(compId).append("&parentId=").append(parentId);
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
		StringBuilder url = new StringBuilder(config.getPsiServiceUrl()).
				append("/draft/api/createDraftFromPreviousOrder").
				append("?userId=").append(userId).
				append("&companyId=").append(companyId).
				append("&parentId=").append(parentId).
				append("&orderId=").append(orderId);
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
	public InspectionBookingBean getDraft(String userId, String draftId) {
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
	public boolean saveDraft(String userId,InspectionBookingBean draft) {
		StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
		url.append("/draft/api/updateDraft/").append(userId);
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
    public boolean addProduct(String userId,String draftId) {
        StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
        url.append("/draft/api/addProduct?userId=").append(userId);
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
	public boolean saveProduct(String userId,InspectionProductBookingBean draftProduct) {
		StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
		url.append("/draft/api/updateProduct?userId=").append(userId);
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
    public boolean deleteProduct(String userId,String productId) {
        StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
        url.append("/draft/api/deleteProduct/").append(userId).append("/").append(productId);
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
}
