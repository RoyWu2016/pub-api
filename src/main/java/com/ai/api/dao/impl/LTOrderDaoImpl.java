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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.ai.aims.services.model.OrderAttachment;
import com.ai.aims.services.model.OrderMaster;
import com.ai.aims.services.model.OrderStyleInfo;
import com.ai.api.bean.OrderSearchBean;
import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.LTOrderDao;
import com.ai.api.util.AIUtil;
import com.ai.commons.beans.ApiCallResult;

/***************************************************************************
 * <PRE>
 *  Project Name    : api
 *
 *  Package Name    : com.ai.api.lab.dao.impl
 *
 *  File Name       : LTOrderDaoImpl.java
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

@SuppressWarnings({"rawtypes", "unchecked"})
@Qualifier("ltorderDao")
@Component
public class LTOrderDaoImpl implements LTOrderDao {

	@Autowired
	@Qualifier("serviceConfig")
	private ServiceConfig config;

	@Override
	public List<OrderSearchBean> searchLTOrders(String compId, String orderStatus, Integer pageNumber, Integer pageSize,
			String direction) throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		AIUtil.addRestTemplateMessageConverter(restTemplate);
		
//		ApiCallResult callResult = new ApiCallResult();
		List<OrderSearchBean> orderSearchList = new ArrayList<OrderSearchBean>();
		
		List<OrderMaster> orders = Arrays.asList(restTemplate.getForObject(buildOrderSearchCriteria(compId, orderStatus, pageSize,
				pageNumber, direction, config.getAimsServiceBaseUrl() + "/api/ordermanagement/search").build()
						.encode().toUri(),
				OrderMaster[].class));

		for (OrderMaster order : orders) {
			OrderSearchBean orderSearch = new OrderSearchBean();
			orderSearch.setOrderId(order.getId());
			orderSearch.setSupplierName(null != order.getSupplier() ? StringUtils.stripToEmpty(order.getSupplier().getCompanyName()) : null );
			orderSearch.setServiceType("LT");
			orderSearch.setServiceTypeText("LT");
			orderSearch.setPoNumbers(order.getClientPONo());
			orderSearch.setStatus(order.getStatusCode());
			orderSearch.setBookingStatus(order.getBookingStatusCode());
			orderSearch.setBookingDate("Pending".equalsIgnoreCase(order.getOrderStatus())
					? DateUtils.formatDate(order.getUpdateTime(), "dd-MMM-yyyy") : null);
			orderSearch.setReportDueDate(null != order.getReportDueDate() ? 
					DateUtils.formatDate(order.getReportDueDate(), "dd-MMM-yyyy") : null);
			orderSearch.setOffice(null != order.getOffice() ? order.getOffice().getName() : null);
			orderSearch.setProductNames(StringUtils.stripToEmpty(order.getDescription()));
			orderSearch.setLabOrderNo(order.getLabOrderno());
			Set<OrderStyleInfo> styleInfo = order.getStyleInfo();
			if (null != styleInfo && !styleInfo.isEmpty()) {
				orderSearch.setManufacturerStyleNo(styleInfo.iterator().next().getManufacturerStyleNo());
			}
			orderSearch.setProgram(null != order.getProgram() ? order.getProgram().getProgramName() : null);
			orderSearch.setTestStartDate(null != order.getTestStartDate() ? 
					DateUtils.formatDate(order.getTestStartDate(), "dd-MMM-yyyy") : null);
			orderSearch.setOverallRating(order.getOverallRating());
			orderSearch.setClientStatus(order.getClientStatus());
			orderSearchList.add(orderSearch);
		}
//		callResult.setContent(orderSearchList);
		return orderSearchList;
	}

	private UriComponentsBuilder buildOrderSearchCriteria(String compId, String orderStatus, Integer pageSize,
			Integer pageNumber, String direction, String url) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("page", pageNumber - 1)
				.queryParam("size", pageSize).queryParam("direction", direction);

		if (!StringUtils.stripToEmpty(orderStatus).trim().isEmpty())
			builder.queryParam("statusCode", orderStatus.trim());

		if (!StringUtils.stripToEmpty(compId).trim().isEmpty())
			builder.queryParam("clientId", compId.trim());

		return builder;
	}

	@Override
	public OrderMaster findOrder(String orderId) throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		AIUtil.addRestTemplateMessageConverter(restTemplate);
		
//		ApiCallResult callResult = new ApiCallResult();
		OrderMaster order = restTemplate.getForObject(config.getAimsServiceBaseUrl() + "/api/ordermanagement/search/" + orderId, OrderMaster.class);
//		callResult.setContent(order);
		return order;
	}

	@Override
	public ApiCallResult saveOrder(String userId, OrderMaster order) throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		AIUtil.addRestTemplateMessageConverter(restTemplate);
		
		ApiCallResult callResult = new ApiCallResult();
		String url = new StringBuilder(config.getAimsServiceBaseUrl()).append("/api/ordermanagement/order/")
				.append(userId).toString();
		
		Map<String, String> vars = new HashMap<String, String>();
		vars.put("userId", userId);		
		order = restTemplate.postForObject(url, order, OrderMaster.class, vars);
		callResult.setContent(order);					
		return callResult;
	}

	@Override
	public ApiCallResult editOrder(String userId, OrderMaster order) throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		AIUtil.addRestTemplateMessageConverter(restTemplate);
		
		ApiCallResult callResult = new ApiCallResult();
		Map<String, String> vars = new HashMap<String, String>();
		vars.put("userId", userId);
		String url = new StringBuilder(config.getAimsServiceBaseUrl()).append("/api/ordermanagement/order/")
 				.append(userId).toString();
		restTemplate.put(url, order, vars);
		order = findOrder(order.getId());
		callResult.setContent(order);
		return callResult;
	}
	
	@Override
	public ApiCallResult editOrderStatus(String userId, OrderMaster order) throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		AIUtil.addRestTemplateMessageConverter(restTemplate);
		
		ApiCallResult callResult = new ApiCallResult();
		Map<String, String> vars = new HashMap<String, String>();
		vars.put("userId", userId);
		String url = new StringBuilder(config.getAimsServiceBaseUrl()).append("/api/ordermanagement/order/status/")
 				.append(userId).toString();
		restTemplate.put(url, order, vars);
		callResult.setMessage("Status updated successfully");
		return callResult;
	}
	
	@Override
	public ApiCallResult editOrderTestAssignmentStatus(String orderId, String testAssignmentId, String userId, String status) throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		AIUtil.addRestTemplateMessageConverter(restTemplate);
		
		ApiCallResult callResult = new ApiCallResult();
		Map<String, String> vars = new HashMap<String, String>();
		vars.put("userId", userId);
		String url = new StringBuilder(config.getAimsServiceBaseUrl()).append("/api/ordermanagement/order/").append(orderId)
				.append("/testassign/").append(testAssignmentId)
				.append("/user/").append(userId)
				.append("/status/").append(status).toString();
		restTemplate.put(url, null, vars);
		callResult.setMessage("Status updated successfully");
		return callResult;
	}
	
	@Override
	public OrderAttachment getOrderAttachment(String attachmentId) throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		AIUtil.addRestTemplateMessageConverter(restTemplate);
		OrderAttachment attachment = restTemplate.getForObject(config.getAimsServiceBaseUrl() + "/api/ordermanagement/order/attachment/" + attachmentId, OrderAttachment.class);
		return attachment;
	} 
}
