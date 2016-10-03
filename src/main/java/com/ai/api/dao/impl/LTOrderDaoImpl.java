/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.ai.aims.services.model.OrderMaster;
import com.ai.api.bean.OrderSearchBean;
import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.LTOrderDao;
import com.ai.api.util.AIUtil;


/***************************************************************************
 *<PRE>
 *  Project Name    : api
 *
 *  Package Name    : com.ai.api.dao
 *
 *  File Name       : LTOrderDaoImpl.java
 *
 *  Creation Date   : Sep 3, 2016
 *
 *  Author          : Aashish Thakran
 *
 *  Purpose         : TODO
 *
 *
 *  History         : TODO
 *
 *</PRE>
 ***************************************************************************/

@Component
@Qualifier("ltOrderDao")
public class LTOrderDaoImpl implements LTOrderDao {

	private static final Logger logger = LoggerFactory.getLogger(LTOrderDaoImpl.class);

	@Autowired
	@Qualifier("serviceConfig")
	private ServiceConfig config;

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
