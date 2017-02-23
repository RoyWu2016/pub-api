/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.service.impl;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ai.aims.services.dto.order.OrderDTO;
import com.ai.aims.services.model.CrmCompany;
import com.ai.aims.services.model.OrderMaster;
import com.ai.api.bean.OrderSearchBean;
import com.ai.api.bean.UserBean;
import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.LTOrderDao;
import com.ai.api.exception.AIException;
import com.ai.api.service.LTEmailService;
import com.ai.api.service.LTOrderService;
import com.ai.api.service.UserService;
import com.ai.commons.beans.ApiCallResult;

/***************************************************************************
 * <PRE>
 *  Project Name    : api
 *
 *  Package Name    : com.ai.api.lab.service.impl
 *
 *  File Name       : LTOrderServiceImpl.java
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

@SuppressWarnings("rawtypes")
@Service
@Qualifier("ltorderService")
public class LTOrderServiceImpl implements LTOrderService {

	protected Logger logger = LoggerFactory.getLogger(LTOrderServiceImpl.class);

	@Autowired
	@Qualifier("ltorderDao")
	private LTOrderDao ltorderDao;

	@Autowired
	@Qualifier("serviceConfig")
	private ServiceConfig config;

	@Autowired
	@Qualifier("userService")
	private UserService userService;
	
	@Autowired
	@Qualifier("ltEmailService")
	private LTEmailService ltEmailService;

	@Override
	public List<OrderSearchBean> searchLTOrders(String userId, String orderStatus, Integer pageNumber, Integer pageSize) throws IOException, AIException {
		String companyId = "";
		String parentId = "";
		UserBean user = userService.getCustById(userId);
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null)
				parentId = "";
			companyId = user.getCompany().getId();
		}
		if (null==companyId){
		    logger.info("use incorrect userId["+userId+"] to search LT orders");
			throw new AIException("incorrect userId");
		}
		return ltorderDao.searchLTOrders(companyId, orderStatus, pageSize, pageNumber, Sort.Direction.DESC.name().toLowerCase());
	}
	
	@Override
	public OrderDTO findOrder(String orderId) throws IOException {
		return ltorderDao.findOrder(orderId);
	}

	@Override
	public ApiCallResult saveOrder(String userId, OrderMaster order) throws IOException, AIException {
		String companyId = null;
		UserBean user = userService.getCustById(userId);
		companyId = user.getCompany().getId();
		if (null != companyId){
			CrmCompany client = new CrmCompany();
			client.setId(companyId);
			order.setClient(client);
		}
		return ltorderDao.saveOrder(userId, order);
	}

	@Override
	public ApiCallResult editOrder(String userId, OrderMaster order, boolean sendEmail) {
		try{
			ApiCallResult apiCallResult = ltorderDao.editOrder(userId, order);
			OrderDTO orderDTO = (OrderDTO)apiCallResult.getContent();
			if(sendEmail){
				UserBean user = userService.getCustById(userId);
				String companyId = user.getCompany().getId();
				boolean sendEmailAddOrder = ltEmailService.sendEmailAddOrder(orderDTO, companyId);
				if(!sendEmailAddOrder) {
					logger.error(ExceptionUtils.getFullStackTrace(new AIException("Error during send mail after saving order")));
					apiCallResult.setMessage("Error during send mail after saving order");
				}
			}
			return apiCallResult;
		} catch (Exception e) {
			logger.error("Exception in edit Order :: ");
			logger.error(ExceptionUtils.getStackTrace(e));
			return new ApiCallResult<>();
		}
	}
	
	@Override
	public ApiCallResult deleteOrders(String userId, String orderIds) throws IOException {
		return ltorderDao.deleteOrders(userId, orderIds);
	}
	
	@Override
	public ApiCallResult findOrderTestAssignments(String orderId) throws IOException {
		return ltorderDao.findOrderTestAssignments(orderId);
	}
	
	@Override
	public ApiCallResult updateOrderTestAssignments(String userId, String orderId, String testIds) throws IOException {
		return ltorderDao.updateOrderTestAssignments(userId, orderId, testIds);
	}
	
	@Override
	public ApiCallResult deleteOrderTestAssignment(String userId, String testId) throws IOException {
		return ltorderDao.deleteOrderTestAssignment(userId, testId);
	}
	
	@Override
	public ApiCallResult cloneOrder(String userId, String orderId, String cloneType) throws IOException {
		return ltorderDao.cloneOrder(userId, orderId, cloneType);
}
}
