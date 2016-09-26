/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.client.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.ai.aims.services.model.OrderMaster;
import com.ai.api.bean.OrderSearchBean;
import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.OrderDao;
import com.ai.api.util.AIUtil;
import com.ai.commons.HttpUtil;
import com.ai.commons.JsonUtil;
import com.ai.commons.beans.GetRequest;
import com.ai.commons.beans.PageBean;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.order.SimpleOrderSearchBean;
import com.ai.commons.beans.psi.InspectionBookingBean;

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

	/*
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
	*/

	@Override
	public Boolean cancelOrder(String userId, String orderId, String reason, String reason_options) {
		
		try {
			StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
			url.append("/order/api/cancelOrder?userId=").append(userId)
			.append("&orderId=").append(orderId)
			.append("&reason=").append(reason)
			.append("&reasonOption=").append(reason_options);
			 ServiceCallResult result = HttpUtil.issuePostRequest(url.toString(), null,orderId);
			 if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
					return true;
				} else {
					logger.error("cancel Order error from psi service : " + result.getStatusCode() +
							", " + result.getResponseString());
					return false;
				}
			
		}catch (Exception e){
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

	@Override
	public InspectionBookingBean getOrderDetail(String userId, String orderId){
		try{
			String url = config.getPsiServiceUrl()+"/order/api/getOrder/"+userId+"/"+orderId;
            logger.info("Get!!! url :"+url);
			GetRequest request = GetRequest.newInstance().setUrl(url);
			ServiceCallResult result = HttpUtil.issueGetRequest(request);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")){
				return JsonUtil.mapToObject(result.getResponseString(), InspectionBookingBean.class);
			} else {
				logger.error("getOrder error from psi service : " + result.getStatusCode() +
						", " + result.getResponseString());
			}
		} catch(Exception e){
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public InspectionBookingBean createOrderByDraft(String userId, String draftId,String companyId,String parentId){
		try{
			StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
			url.append("/order/api/createOrderByDraft?userId=").append(userId).
					append("&companyId=").append(companyId).
					append("&parentId=").append(parentId).
					append("&draftId=").append(draftId);
            logger.info("Post!!! url :"+url);
			ServiceCallResult result = HttpUtil.issuePostRequest(url.toString(), null,draftId);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				return JsonUtil.mapToObject(result.getResponseString(), InspectionBookingBean.class);
			} else {
				logger.error("createOrderByDraft error from psi service : " + result.getStatusCode() +
						", " + result.getResponseString());
			}
		} catch(Exception e){
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

    @Override
    public InspectionBookingBean editOrder(String userId, String orderId,String companyId,String parentId){
        try{
            StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
            url.append("/order/api/editOrder?userId=").append(userId).
                    append("&companyId=").append(companyId).
                    append("&parentId=").append(parentId).
                    append("&draftId=").append(orderId);
            logger.info("Get!!! url :"+url);
            GetRequest request = GetRequest.newInstance().setUrl(url.toString());
            ServiceCallResult result = HttpUtil.issueGetRequest(request);
            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")){
                return JsonUtil.mapToObject(result.getResponseString(), InspectionBookingBean.class);
            } else {
                logger.error("editOrder error from psi service : " + result.getStatusCode() +
                        ", " + result.getResponseString());
            }
        } catch(Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    @Override
    public InspectionBookingBean saveOrderByDraft(String userId, String draftId,String companyId,String parentId){
        try{
            StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
            url.append("/order/api/saveOrderByDraft?userId=").append(userId).
                    append("&companyId=").append(companyId).
                    append("&parentId=").append(parentId).
                    append("&draftId=").append(draftId);
            logger.info("Post!!! url :"+url);
            ServiceCallResult result = HttpUtil.issuePostRequest(url.toString(), null,draftId);
            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
                return JsonUtil.mapToObject(result.getResponseString(), InspectionBookingBean.class);
            } else {
                logger.error("saveOrderByDraft error from psi service : " + result.getStatusCode() +
                        ", " + result.getResponseString());
            }
        } catch(Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

	@Override
	public List<SimpleOrderSearchBean> searchOrders(String userId, String compId, String parentId, String serviceType,
			String startDate, String endDate, String keyWord, String orderStatus, String pageSize, String pageNumber) {
		try {

			  StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
			   url.append("/order/api/search?userId=")
			   	  .append(userId)
			   	  .append("&companyId=").append(compId)
			   	  .append("&parentId=").append(parentId)
			   	  .append("&serviceType=").append(serviceType)
			   	  .append("&startDate=").append(startDate)
			   	  .append("&endDate=").append(endDate)
			   	  .append("&keyWord=").append(keyWord)
			   	  .append("&orderStatus=").append(orderStatus)
			      .append("&pageSize=").append(pageSize)
			      .append("&pageNo=").append(pageNumber);
			   
			   GetRequest request = GetRequest.newInstance().setUrl(url.toString());
				ServiceCallResult result = HttpUtil.issueGetRequest(request);
				if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
					@SuppressWarnings("unchecked")
					PageBean<SimpleOrderSearchBean> pageBeanList = JsonUtil.mapToObject(result.getResponseString(),PageBean.class);
					
					return pageBeanList.getPageItems();

				} else {
					logger.error("searchOrders from PSI error: " + result.getStatusCode() + ", " + result.getResponseString());
				}
			
		}catch(IOException e){
			logger.error(ExceptionUtils.getStackTrace(e));
			
		}
		
		return null;
		
	}
	
	@Override
	public List<OrderSearchBean> searchLTOrders(String compId, String orderStatus, String pageSize, String pageNumber, String direction) {
		
		RestTemplate restTemplate = new RestTemplate();
		List<OrderSearchBean> orderSearchList = new ArrayList<OrderSearchBean>();
		List<OrderMaster> orders = new ArrayList<OrderMaster>();

		try {
			AIUtil.addRestTemplateMessageConverter(restTemplate);
			orders = Arrays.asList(restTemplate.getForObject(
					buildTestSearchCriteria(compId, orderStatus, pageSize, pageNumber, direction, config.getAimsServiceBaseUrl() + "/api/ordermanagement/search/all").build().encode().toUri(), 
					OrderMaster[].class));
			
			for(OrderMaster order: orders) {
				OrderSearchBean orderSearch = new OrderSearchBean();
				orderSearch.setOrderId(order.getId());
				orderSearch.setSupplierName(StringUtils.stripToEmpty(order.getSupplier().getCompanyName()));
				orderSearch.setServiceType("LT");
				orderSearch.setServiceTypeText("LT");
				orderSearch.setPoNumbers(order.getClientPONo());
				orderSearch.setStatus(order.getOrderStatus());
				orderSearch.setStatusText(order.getOrderStatus());
				orderSearch.setBookingDate("Pending".equalsIgnoreCase(order.getOrderStatus()) ? DateUtils.formatDate(order.getUpdateTime(), "MM/dd/yyyy") : null );
				orderSearch.setProductNames(StringUtils.stripToEmpty(order.getDescription()));
				orderSearchList.add(orderSearch);
			}			
		} catch (Exception ex) {
			logger.error("Exception", ex);
			ex.printStackTrace();
		}
		
		return orderSearchList;//pageBeanList.getPageItems();
	}
	
	private UriComponentsBuilder buildTestSearchCriteria(String compId, String orderStatus, String pageSize, String pageNumber, String direction, String url) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
		        .queryParam("page", (Integer.parseInt(pageNumber) - 1))
		        .queryParam("size", pageSize)
		        .queryParam("direction", (null != direction ? direction : "desc") );	
		
		if(!StringUtils.stripToEmpty(compId).trim().isEmpty())
			builder.queryParam("clientId", compId.trim());
		
		return builder;
	}

}
