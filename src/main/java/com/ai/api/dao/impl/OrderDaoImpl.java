/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.dao.impl;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ai.aims.services.model.OrderMaster;
import com.ai.api.bean.OrderSearchBean;
import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.OrderDao;
import com.ai.api.util.AIUtil;
import com.ai.commons.HttpUtil;
import com.ai.commons.JsonUtil;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.GetRequest;
import com.ai.commons.beans.PageBean;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.order.SimpleOrderSearchBean;
import com.ai.commons.beans.psi.InspectionBookingBean;
import com.ai.commons.beans.psi.ProductBean;
import com.alibaba.fastjson.JSONObject;
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
@SuppressWarnings("rawtypes")
@Component
public class OrderDaoImpl implements OrderDao {

	private static final Logger logger = LoggerFactory.getLogger(OrderDaoImpl.class);

	@Autowired
	@Qualifier("serviceConfig")
	private ServiceConfig config;

	@Override
	public Boolean cancelOrder(String userId, String orderId, String reason, String reason_options) {

		try {
//			logger.info("reason before: " + reason);
			reason = URLEncoder.encode(reason, "UTF-8");
//			logger.info("reason after: " + reason);
			if (reason_options != null) {
				reason_options = URLEncoder.encode(reason_options, "UTF-8");
			}
			StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
			url.append("/order/api/cancelOrder?userId=").append(userId).append("&orderId=").append(orderId)
					.append("&reason=").append(reason).append("&reasonOption=").append(reason_options);
//			logger.info("Posting!!! url :" + url);

			ServiceCallResult result = HttpUtil.issuePostRequest(url.toString(), null, orderId);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				JSONObject object = JSONObject.parseObject(result.getResponseString());
				Object arrayStr = object.get("content");
				return JsonUtil.mapToObject(arrayStr + "", boolean.class);
			} else {
//				logger.error("cancel Order error from psi service : " + result.getStatusCode() + ", "
//						+ result.getResponseString());
				return false;
			}

		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

	@Override
	public InspectionBookingBean getOrderDetail(String userId, String orderId) {
		try {
			String url = config.getPsiServiceUrl() + "/order/api/getOrder/" + userId + "/" + orderId;
			logger.info("Get!!! url :" + url);
			GetRequest request = GetRequest.newInstance().setUrl(url);
			ServiceCallResult result = HttpUtil.issueGetRequest(request);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				JSONObject object = JSONObject.parseObject(result.getResponseString());
				Object arrayStr = object.get("content");
				return JsonUtil.mapToObject(arrayStr + "", InspectionBookingBean.class);
				// return JsonUtil.mapToObject(result.getResponseString(),
				// InspectionBookingBean.class);
			} else {
//				logger.error("getOrder error from psi service : " + result.getStatusCode() + ", "
//						+ result.getResponseString());
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public InspectionBookingBean createOrderByDraft(String userId, String draftId, String companyId, String parentId) {
		try {
			StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
			url.append("/order/api/createOrderByDraft?userId=").append(userId).append("&companyId=").append(companyId)
					.append("&parentId=").append(parentId).append("&draftId=").append(draftId);
//			logger.info("Post!!! url :" + url);
			ServiceCallResult result = HttpUtil.issuePostRequest(url.toString(), null, draftId);
			if (result.getStatusCode() == HttpStatus.OK.value() &&
					!result.getResponseString().contains("Create new order to database (by draft) faild!")) {
				JSONObject object = JSONObject.parseObject(result.getResponseString());
				Object arrayStr = object.get("content");
				return JsonUtil.mapToObject(arrayStr + "", InspectionBookingBean.class);
				// return JsonUtil.mapToObject(result.getResponseString(),
				// InspectionBookingBean.class);
			} else {
				logger.error("createOrderByDraft error from psi service : " + result.getStatusCode() + ", "
						+ result.getResponseString());
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public InspectionBookingBean editOrder(String userId, String orderId, String companyId, String parentId) {
		try {
			StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
			url.append("/order/api/editOrder?userId=").append(userId).append("&companyId=").append(companyId)
					.append("&parentId=").append(parentId).append("&orderId=").append(orderId);
//			logger.info("Get!!! url :" + url);
			GetRequest request = GetRequest.newInstance().setUrl(url.toString());
			ServiceCallResult result = HttpUtil.issueGetRequest(request);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				JSONObject object = JSONObject.parseObject(result.getResponseString());
				Object arrayStr = object.get("content");
				return JsonUtil.mapToObject(arrayStr + "", InspectionBookingBean.class);
				// return JsonUtil.mapToObject(result.getResponseString(),
				// InspectionBookingBean.class);
			} else {
//				logger.error("editOrder error from psi service : " + result.getStatusCode() + ", "
//						+ result.getResponseString());
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public InspectionBookingBean saveOrderByDraft(String userId, String draftId, String companyId, String parentId) {
		try {
			StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
			url.append("/order/api/saveOrderByDraft?userId=").append(userId).append("&companyId=").append(companyId)
					.append("&parentId=").append(parentId).append("&draftId=").append(draftId);
//			logger.info("Post!!! url :" + url);
			ServiceCallResult result = HttpUtil.issuePostRequest(url.toString(), null, draftId);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				JSONObject object = JSONObject.parseObject(result.getResponseString());
				Object arrayStr = object.get("content");
				return JsonUtil.mapToObject(arrayStr + "", InspectionBookingBean.class);
				// return JsonUtil.mapToObject(result.getResponseString(),
				// InspectionBookingBean.class);
			} else {
//				logger.error("saveOrderByDraft error from psi service : " + result.getStatusCode() + ", "
//						+ result.getResponseString());
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public PageBean<SimpleOrderSearchBean> searchOrders(String userId, String compId, String parentId, String serviceType,
			String startDate, String endDate, String keyWord, String orderStatus, String pageSize, String pageNumber) {
		try {
			StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
			url.append("/order/api/search?userId=").append(userId).append("&companyId=").append(compId)
					.append("&parentId=").append(parentId).append("&serviceType=").append(serviceType)
					.append("&startDate=").append(startDate).append("&endDate=").append(endDate).append("&keyWord=")
					.append(keyWord).append("&orderStatus=").append(orderStatus).append("&pageSize=").append(pageSize)
					.append("&pageNo=").append(pageNumber);

			GetRequest request = GetRequest.newInstance().setUrl(url.toString());
			ServiceCallResult result = HttpUtil.issueGetRequest(request);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				@SuppressWarnings("unchecked")
				PageBean<SimpleOrderSearchBean> pageBeanList = JsonUtil.mapToObject(result.getResponseString(),PageBean.class);
				// JSONObject object =
				// JSONObject.parseObject(result.getResponseString());
				// Object arrayStr = object.get("content");
				// PageBean<SimpleOrderSearchBean> pageBeanList =
				// JsonUtil.mapToObject(arrayStr + "", PageBean.class);
				return pageBeanList;

			} else {
//				logger.error(
//						"searchOrders from PSI error: " + result.getStatusCode() + ", " + result.getResponseString());
			}

		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));

		}

		return null;

	}

	@Override
	public List<OrderSearchBean> searchLTOrders(String compId, String orderStatus, String pageSize, String pageNumber,
			String direction) {

		RestTemplate restTemplate = new RestTemplate();
		List<OrderSearchBean> orderSearchList = new ArrayList<OrderSearchBean>();
		List<OrderMaster> orders = new ArrayList<OrderMaster>();

		try {
			AIUtil.addRestTemplateMessageConverter(restTemplate);
			orders = Arrays.asList(restTemplate.getForObject(buildTestSearchCriteria(compId, orderStatus, pageSize,
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
		} catch (Exception ex) {
			logger.error("Exception", ex);
			ex.printStackTrace();
		}

		return orderSearchList;// pageBeanList.getPageItems();
	}

	private UriComponentsBuilder buildTestSearchCriteria(String compId, String orderStatus, String pageSize,
			String pageNumber, String direction, String url) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
				.queryParam("page", (Integer.parseInt(pageNumber) - 1)).queryParam("size", pageSize)
				.queryParam("direction", (null != direction ? direction : "desc"));

		if (!StringUtils.stripToEmpty(compId).trim().isEmpty())
			builder.queryParam("clientId", compId.trim());

		return builder;
	}

	@Override
	public ApiCallResult getOrderActionEdit(String orderId) {
		// TODO Auto-generated method stub
		StringBuilder editableurl = new StringBuilder(
				config.getPsiServiceUrl() + "/order/api/actionCheck/" + orderId + "/edit");
		GetRequest request = GetRequest.newInstance().setUrl(editableurl.toString());
		ApiCallResult temp = new ApiCallResult();
		try {
//			logger.info("requesting url: " + editableurl.toString());
			ServiceCallResult result = HttpUtil.issueGetRequest(request);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				temp = JsonUtil.mapToObject(result.getResponseString(), ApiCallResult.class);
				return temp;
			} else {
//				logger.error("getOrderActionEdit from psi-service error: " + result.getStatusCode() + ", "
//						+ result.getResponseString());
				temp.setMessage("getOrderActionEdit from psi-service error: " + result.getStatusCode() + ", "
						+ result.getResponseString());

				return temp;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(ExceptionUtils.getStackTrace(e));
			temp.setMessage(e.toString());

			return temp;
		}

	}

	@Override
	public ApiCallResult getOrderActionCancel(String orderId) {
		// TODO Auto-generated method stub
		StringBuilder cancelablerurl = new StringBuilder(
				config.getPsiServiceUrl() + "/order/api/actionCheck/" + orderId + "/cancel");
		GetRequest request = GetRequest.newInstance().setUrl(cancelablerurl.toString());
		ApiCallResult temp = new ApiCallResult();
		try {
//			logger.info("requesting url: " + cancelablerurl.toString());
			ServiceCallResult result = HttpUtil.issueGetRequest(request);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				temp = JsonUtil.mapToObject(result.getResponseString(), ApiCallResult.class);
				return temp;
			} else {
//				logger.error("getOrderActionCancel from psi-service error: " + result.getStatusCode() + ", "
//						+ result.getResponseString());
				temp.setMessage("getOrderActionCancel from psi-service error: " + result.getStatusCode() + ", "
						+ result.getResponseString());

				return temp;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error(ExceptionUtils.getStackTrace(e));
			temp.setMessage(e.toString());

			return temp;
		}

	}

	@Override
	public ApiCallResult getOrderPrice(String userId, String compId, String parentId, String orderId) {
		try {

			StringBuilder url = new StringBuilder(config.getPsiServiceUrl());
			url.append("/order/api/orderPricing?userId=").append(userId);
			url.append("&companyId=").append(compId);
			url.append("&parentId=").append(parentId);
			url.append("&orderId=").append(orderId);

			GetRequest request = GetRequest.newInstance().setUrl(url.toString());
			ServiceCallResult result = HttpUtil.issueGetRequest(request);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				ApiCallResult apiCallResult = JsonUtil.mapToObject(result.getResponseString(), ApiCallResult.class);
				return apiCallResult;

			} else {
//				logger.error(
//						"searchOrders from PSI error: " + result.getStatusCode() + ", " + result.getResponseString());
			}

		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));

		}

		return null;

	}

	@Override
	public InspectionBookingBean getInspectionOrder(String string, String orderId) {
		// TODO Auto-generated method stub
		try {
			String url = config.getPsiServiceUrl() + "/order/api/getInspectionOrder/" + string + "/" + orderId;
//			logger.info("Get!!! url :" + url);
			GetRequest request = GetRequest.newInstance().setUrl(url);
			ServiceCallResult result = HttpUtil.issueGetRequest(request);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				JSONObject object = JSONObject.parseObject(result.getResponseString());
				Object arrayStr = object.get("content");
				return JsonUtil.mapToObject(arrayStr + "", InspectionBookingBean.class);
			} else {
//				logger.error("getOrder error from psi service : " + result.getStatusCode() + ", "
//						+ result.getResponseString());
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public ApiCallResult reInspection(String userId, String companyId, String parentId, String orderId,
			String draftId) {
		// TODO Auto-generated method stub
		StringBuilder url = new StringBuilder(config.getPsiServiceUrl() + "/order/api/reInspection");
		url.append("?userId=" + userId).append("&companyId=" + companyId).append("&parentId=" + parentId)
				.append("&orderId=" + orderId).append("&draftId=" + draftId);
		ApiCallResult temp = new ApiCallResult();
//		logger.info("requesting: " + url.toString());
		try {
			ServiceCallResult result = HttpUtil.issuePostRequest(url.toString(), null, "");
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				return JsonUtil.mapToObject(result.getResponseString(), ApiCallResult.class);
			} else {
//				logger.error("getOrder error from psi service : " + result.getStatusCode() + ", "
//						+ result.getResponseString());
				temp.setMessage("getOrder error from psi service : " + result.getStatusCode() + ", "
						+ result.getResponseString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(ExceptionUtils.getStackTrace(e));
			temp.setMessage(e.toString());
		}
		return temp;
	}

	@Override
	public List<ProductBean> listProducts(String orderId) {
		// TODO Auto-generated method stub
		StringBuilder url = new StringBuilder(config.getPsiServiceUrl() + "/product/general/list/" + orderId);
//		logger.info("requesting: " + url.toString());
		try {
			GetRequest request = GetRequest.newInstance().setUrl(url.toString());
			ServiceCallResult result = HttpUtil.issueGetRequest(request);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				return JsonUtil.mapToObject(result.getResponseString(), List.class);
			} else {
//				logger.error("listProducts error from psi service : " + result.getStatusCode() + ", "
//						+ result.getResponseString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

}
