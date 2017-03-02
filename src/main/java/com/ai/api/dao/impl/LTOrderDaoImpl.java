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

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.ai.aims.services.dto.LabFilterDTO;
import com.ai.aims.services.dto.order.OrderDTO;
import com.ai.aims.services.dto.order.TestAssignmentDTO;
import com.ai.aims.services.dto.test.TestDTO;
import com.ai.aims.services.model.LabMaster;
import com.ai.aims.services.model.OrderAttachment;
import com.ai.aims.services.model.OrderMaster;
import com.ai.aims.services.model.OrderTestAssignment;
import com.ai.aims.services.model.TestMaster;
import com.ai.aims.services.model.TestPricingDetail;
import com.ai.api.bean.OrderSearchBean;
import com.ai.api.bean.OrderTestBean;
import com.ai.api.bean.TestSearchBean;
import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.LTOrderDao;
import com.ai.api.util.AIUtil;
import com.ai.commons.Consts;
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
		
		List<OrderSearchBean> orderSearchList = new ArrayList<OrderSearchBean>();
		
		List<OrderDTO> orders = Arrays.asList(restTemplate.getForObject(buildOrderSearchCriteria(compId, orderStatus, pageSize,
				pageNumber, direction, config.getAimsServiceBaseUrl() + "/api/ordermanagement/search").build()
						.encode().toUri(),
				OrderDTO[].class));

		String dateFormat = "dd-MMM-yyyy";
		for (OrderDTO order : orders) {
			OrderSearchBean orderSearch = new OrderSearchBean();
			orderSearch.setOrderId(order.getId());
			orderSearch.setSupplierName(null != order.getSupplier() ? StringUtils.stripToEmpty(order.getSupplier().getName()) : null );
			orderSearch.setServiceType("LT");
			orderSearch.setServiceTypeText("LT");
			orderSearch.setPoNumbers(order.getClientPONo());
			orderSearch.setStatus(order.getStatusCode());
			orderSearch.setBookingStatus(order.getBookingStatusCode());
			orderSearch.setBookingDate(null != order.getBookingDate() ? 
					DateUtils.formatDate(order.getBookingDate(), dateFormat) : null);
			orderSearch.setReportDueDate(null != order.getReportDueDate() ? 
					DateUtils.formatDate(order.getReportDueDate(), dateFormat) : null);
			orderSearch.setReportIssuedDate(null != order.getReportIssuedDate() ?
					DateUtils.formatDate(order.getReportIssuedDate(), dateFormat) : null);
			orderSearch.setTestStartDate(null != order.getTestStartDate() ? 
					DateUtils.formatDate(order.getTestStartDate(), dateFormat) : null);
			orderSearch.setOffice(null != order.getOffice() ? order.getOffice().getName() : null);
			orderSearch.setProductNames(StringUtils.stripToEmpty(order.getDescription()));
			orderSearch.setLabOrderNo(order.getLabOrderno());
			orderSearch.setManufacturerStyleNo(!CollectionUtils.isEmpty(order.getStyleInfo()) ? 
					order.getStyleInfo().get(0).getManufacturerStyleNo() : null);
			orderSearch.setProgram(null != order.getProgram() ? order.getProgram().getProgramName() : null);
			orderSearch.setOverallRating(order.getOverallRating());
			orderSearch.setClientStatus(order.getClientStatus());
			orderSearchList.add(orderSearch);
		}
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
		
		builder.queryParam("requestor", "external");
		return builder;
	}

	@Override
	public OrderDTO findOrder(String orderId) throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		AIUtil.addRestTemplateMessageConverter(restTemplate);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(config.getAimsServiceBaseUrl() + "/api/ordermanagement/search/" + orderId).queryParam("requestor", "external");
		OrderDTO order = restTemplate.getForObject(builder.build().encode().toUri(), OrderDTO.class);
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
		OrderDTO orderDTO = restTemplate.postForObject(url, order, OrderDTO.class, vars);
		callResult.setContent(orderDTO);					
		return callResult;
	}

	@Override
	public ApiCallResult editOrder(String userId, OrderMaster order) throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		AIUtil.addRestTemplateMessageConverter(restTemplate);
		
		ApiCallResult callResult = new ApiCallResult();
		String url = new StringBuilder(config.getAimsServiceBaseUrl()).append("/api/ordermanagement/order/")
 				.append(userId).toString();
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url.toString()).queryParam("requestor", "external");
		restTemplate.put(builder.build().encode().toUri(), order);
		callResult.setContent(findOrder(order.getId()));
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

	@Override
	public ApiCallResult deleteOrders(String userId, String orderIds) throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		AIUtil.addRestTemplateMessageConverter(restTemplate);
		ApiCallResult callResult = new ApiCallResult();
		String url = new StringBuilder(config.getAimsServiceBaseUrl()).append("/api/ordermanagement/orders/user/")
 				.append(userId).toString();
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("orderIds", orderIds);
		restTemplate.delete(builder.build().toUri());
		callResult.setMessage("Orders deleted successfully");
		return callResult;
	}
	
	@Override
	public ApiCallResult findOrderTestAssignments(String orderId) throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		AIUtil.addRestTemplateMessageConverter(restTemplate);
		ApiCallResult callResult = new ApiCallResult();
		String url = new StringBuilder(config.getAimsServiceBaseUrl()).append("/api/ordermanagement/order/testassignments/")
 				.append(orderId).toString();
		List<OrderTestAssignment> testAssignments = Arrays.asList(restTemplate.getForObject(url, OrderTestAssignment[].class, new HashMap<String, Object>()));
		List<OrderTestBean> orderTests = new ArrayList<OrderTestBean>(testAssignments.size());
		for (OrderTestAssignment testAssign : testAssignments) {
			OrderTestBean orderTest = new OrderTestBean();
			orderTest.setId(testAssign.getId());
			orderTest.setFailureStmt(testAssign.getFailureStatement());
			orderTest.setName(testAssign.getTest().getName());
			orderTest.setRating(null != testAssign.getTestAssignRating() ? testAssign.getTestAssignRating().getDescription() : null);
			orderTest.setClientStatus(testAssign.getClientStatus());
			if (null != testAssign.getTest()) {
				TestMaster test = testAssign.getTest();
				TestSearchBean testDto = new TestSearchBean();
				testDto.setTestCode(test.getCode());
				testDto.setVersion(test.getVersion());
				testDto.setTestCategory(test.getTestCategory());
				testDto.setTestId(test.getId());
				testDto.setTestItem(test.getTestItem());
				testDto.setTestName(test.getName());
				testDto.setStandardName(test.getStandardName());
				testDto.setStandardNo(test.getStandardNo());
				TestPricingDetail priceDetails = null;
				if (!CollectionUtils.isEmpty(test.getPricingDetails())) {
					OrderDTO order = findOrder(orderId);
					priceDetails = test.getPricingDetails().parallelStream().filter(
							p -> (null != p.getOffice() && null != order.getTestingLocation() &&
								p.getOffice().getId().equals(order.getTestingLocation().getId())))
							.findFirst().orElse(null);
				}
				testDto.setPrice(null != priceDetails && null != priceDetails.getPrice() ? priceDetails.getPrice() : 0);
				testDto.setMandatory(testAssign.getMandatory());
				orderTest.setTest(testDto);
			}
			if (null != testAssign.getLab()) {
				LabMaster lab = testAssign.getLab();
				LabFilterDTO labDto = new LabFilterDTO();
				labDto.setId(lab.getId());
				labDto.setCode(lab.getCode());
				labDto.setName(lab.getName());
				labDto.setZipCode(lab.getZipCode());
				orderTest.setLab(labDto);
			}
			orderTests.add(orderTest);
		}
		callResult.setContent(orderTests);
		return callResult;
	}

	@Override
	public ApiCallResult updateOrderTestAssignments(String userId, String orderId, String testIds) throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		AIUtil.addRestTemplateMessageConverter(restTemplate);
		if (!StringUtils.stripToEmpty(testIds).isEmpty()) {
			Map<String, String> vars = new HashMap<String, String>();
			vars.put("userId", userId);
			String url = new StringBuilder(config.getAimsServiceBaseUrl()).append("/api/ordermanagement/order/")
					.append(userId).toString();
			OrderDTO order = findOrder(orderId);
			List<TestAssignmentDTO> testAssignments = new ArrayList<TestAssignmentDTO>();
			for (String testId : testIds.split(Consts.COMMA)) {
				TestAssignmentDTO testAssign = new TestAssignmentDTO();
				TestDTO testDTO = new TestDTO();
				testDTO.setId(testId.trim());
				testAssign.setTest(testDTO);
				testAssignments.add(testAssign);
			}
			order.setTestAssignments(testAssignments);
			restTemplate.put(url, order, vars);
		}
		ApiCallResult result = new ApiCallResult();
		result.setMessage("Test Assignments updated successfully");
		return result;
	}
	
	@Override
	public ApiCallResult deleteOrderTestAssignment(String userId, String testId) throws IOException {
		RestTemplate restTemplate = new RestTemplate();
		AIUtil.addRestTemplateMessageConverter(restTemplate);
		String url = new StringBuilder(config.getAimsServiceBaseUrl()).append("/api/ordermanagement/order/testassign/")
				.append(testId).append("/")
				.append(userId).toString();
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
		restTemplate.delete(builder.build().toUri());
		ApiCallResult result = new ApiCallResult();
		result.setMessage("Test Assignment deleted successfully");
		return result;
	}
}
