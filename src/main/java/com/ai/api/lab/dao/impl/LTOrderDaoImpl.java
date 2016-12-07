/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.lab.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.ai.aims.services.model.OrderMaster;
import com.ai.api.bean.OrderSearchBean;
import com.ai.api.config.ServiceConfig;
import com.ai.api.lab.dao.LTOrderDao;
import com.ai.api.util.AIUtil;

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

@Qualifier("ltorderDao")
@Component
public class LTOrderDaoImpl implements LTOrderDao {

	@Autowired
	@Qualifier("serviceConfig")
	private ServiceConfig config;

	@Override
	public List<OrderSearchBean> searchLTOrders(String compId, String orderStatus, Integer pageNumber, Integer pageSize,
			String direction) {

		RestTemplate restTemplate = new RestTemplate();
		List<OrderSearchBean> orderSearchList = new ArrayList<OrderSearchBean>();
		
		AIUtil.addRestTemplateMessageConverter(restTemplate);
		List<OrderMaster> orders = Arrays.asList(restTemplate.getForObject(buildOrderSearchCriteria(compId, orderStatus, pageSize,
				pageNumber, direction, config.getAimsServiceBaseUrl() + "/api/ordermanagement/search/all").build()
						.encode().toUri(),
				OrderMaster[].class));

		for (OrderMaster order : orders) {
			OrderSearchBean orderSearch = new OrderSearchBean();
			orderSearch.setOrderId(order.getId());
			orderSearch.setSupplierName(StringUtils.stripToEmpty(order.getSupplier().getCompanyName()));
			orderSearch.setServiceType("LT");
			orderSearch.setServiceTypeText("LT");
			orderSearch.setPoNumbers(order.getClientPONo());
			orderSearch.setStatus(order.getOrderStatus());
			orderSearch.setStatusText(order.getOrderStatus());
			orderSearch.setBookingDate("Pending".equalsIgnoreCase(order.getOrderStatus())
					? DateUtils.formatDate(order.getUpdateTime(), "MM/dd/yyyy") : null);
			orderSearch.setProductNames(StringUtils.stripToEmpty(order.getDescription()));
			orderSearchList.add(orderSearch);
		}

		return orderSearchList;
	}

	private UriComponentsBuilder buildOrderSearchCriteria(String compId, String orderStatus, Integer pageSize,
			Integer pageNumber, String direction, String url) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("page", pageNumber - 1)
				.queryParam("size", pageSize).queryParam("direction", direction);

		if (!StringUtils.stripToEmpty(orderStatus).trim().isEmpty())
			builder.queryParam("orderStatus", orderStatus.trim());

		if (!StringUtils.stripToEmpty(compId).trim().isEmpty())
			builder.queryParam("clientId", compId.trim());

		return builder;
	}

	@Override
	public OrderMaster findOrder(String orderId) {
		RestTemplate restTemplate = new RestTemplate();
		AIUtil.addRestTemplateMessageConverter(restTemplate);
		OrderMaster order = restTemplate.getForObject(config.getAimsServiceBaseUrl() + "/api/ordermanagement/search/" + orderId,
				OrderMaster.class);

		return order;
	}

	@Override
	public OrderMaster saveOrder(String userId, OrderMaster order) {
		RestTemplate restTemplate = new RestTemplate();
		AIUtil.addRestTemplateMessageConverter(restTemplate);
		Map<String, String> vars = new HashMap<String, String>();
		vars.put("userId", userId);

		String url = new StringBuilder(config.getAimsServiceBaseUrl()).append("/api/ordermanagement/order/")
				.append(userId).toString();
		order = restTemplate.postForObject(url, order, OrderMaster.class, vars);

		return order;
	}

	@Override
	public OrderMaster editOrder(String userId, OrderMaster order) {
		RestTemplate restTemplate = new RestTemplate();
		AIUtil.addRestTemplateMessageConverter(restTemplate);
		Map<String, String> vars = new HashMap<String, String>();
		vars.put("userId", userId);

		String url = new StringBuilder(config.getAimsServiceBaseUrl()).append("/api/ordermanagement/order/")
				.append(userId).toString();
		restTemplate.put(url, order, vars);

		return order;
	}
}
