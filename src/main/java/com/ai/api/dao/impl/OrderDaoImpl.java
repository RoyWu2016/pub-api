/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.dao.impl;

import java.io.IOException;
import java.util.List;

import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.OrderDao;
import com.ai.commons.HttpUtil;
import com.ai.commons.JsonUtil;
import com.ai.commons.beans.GetRequest;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.legacy.order.OrderCancelBean;
import com.ai.commons.beans.legacy.order.OrderSearchCriteriaBean;
import com.ai.commons.beans.order.api.SimpleOrderBean;
import com.ai.commons.beans.psi.InspectionOrderBean;
import com.ai.dto.JsonResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/***************************************************************************
 * <PRE>
 * Project Name    : api
 * <p/>
 * Package Name    : com.ai.api.dao.impl
 * <p/>
 * File Name       : OrderDaoImpl.java
 * <p/>
 * Creation Date   : Jul 13, 2016
 * <p/>
 * Author          : Allen Zhang
 * <p/>
 * Purpose         : TODO
 * <p/>
 * <p/>
 * History         : TODO
 * <p/>
 * </PRE>
 ***************************************************************************/

@Component
public class OrderDaoImpl implements OrderDao {

	private static final Logger logger = LoggerFactory.getLogger(OrderDaoImpl.class);

	@Autowired
	@Qualifier("serviceConfig")
	private ServiceConfig config;

	@Override
	public List<SimpleOrderBean> getOrdersByUserId(OrderSearchCriteriaBean criteria) {
		String url = config.getMwServiceUrl() + "/service/order/search";
		try {
			ServiceCallResult result = HttpUtil.issuePostRequest(url, null, criteria);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {

				return JsonUtil.mapToObject(result.getResponseString(),
						new TypeReference<List<SimpleOrderBean>>() {
						});

			} else {
				logger.error("get orders from middleware error: " + result.getStatusCode() +
						", " + result.getResponseString());
			}

		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		return null;
	}

	@Override
	public List<SimpleOrderBean> getDraftsByUserId(OrderSearchCriteriaBean criteria) {
		String url = config.getMwServiceUrl() + "/service/draft/search";
		try {
			ServiceCallResult result = HttpUtil.issuePostRequest(url, null, criteria);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {

				return JsonUtil.mapToObject(result.getResponseString(),
						new TypeReference<List<SimpleOrderBean>>() {
						});

			} else {
				logger.error("get drafts from middleware error: " + result.getStatusCode() +
						", " + result.getResponseString());
			}

		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public Boolean cancelOrder(OrderCancelBean orderCancelBean) {
		String url = config.getMwServiceUrl() + "/service/order/cancel";
		try {
			ServiceCallResult result = HttpUtil.issuePostRequest(url, null, orderCancelBean);
			if (result.getResponseString().equalsIgnoreCase("true")) {
				return true;
			}else {
				logger.error("cancel order from middleware error: " + result.getStatusCode() +
						", " + result.getResponseString());
			}
		}catch (Exception e){
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public InspectionOrderBean getOrderDetail(String userId, String orderId){
		try{
			String url = config.getPsiServiceUrl()+"/order/api/getOrder/"+userId+"/"+orderId;
			GetRequest request = GetRequest.newInstance().setUrl(url);
			ServiceCallResult result = HttpUtil.issueGetRequest(request);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")){
				JsonResponse jsonResponse = JsonUtil.mapToObject(result.getResponseString(),JsonResponse.class);
				if(jsonResponse != null && jsonResponse.getStatus() == 1){
					InspectionOrderBean orderBean = (InspectionOrderBean)jsonResponse.getData();
					return orderBean;
				}
			}
		} catch(Exception e){
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

}
