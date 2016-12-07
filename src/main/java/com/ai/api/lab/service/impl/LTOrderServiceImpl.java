/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.lab.service.impl;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ai.aims.services.model.OrderMaster;
import com.ai.api.bean.UserBean;
import com.ai.api.config.ServiceConfig;
import com.ai.api.exception.AIException;
import com.ai.api.lab.dao.LTOrderDao;
import com.ai.api.lab.service.LTOrderService;
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

	@Override
	public ApiCallResult searchLTOrders(String userId, String orderStatus, Integer pageNumber, Integer pageSize) throws IOException, AIException {
		String companyId = "";
		String parentId = "";
		UserBean user = userService.getCustById(userId);
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null)
				parentId = "";
			companyId = user.getCompany().getId();
		}
		return ltorderDao.searchLTOrders(companyId, orderStatus, pageSize, pageNumber, Sort.Direction.DESC.name().toLowerCase());
	}
	
	@Override
	public ApiCallResult findOrder(String orderId) throws IOException {
		return ltorderDao.findOrder(orderId);
	}

	@Override
	public ApiCallResult saveOrder(String userId, OrderMaster order) throws IOException {
		return ltorderDao.saveOrder(userId, order);
	}

	@Override
	public ApiCallResult editOrder(String userId, OrderMaster order) throws IOException {
		return ltorderDao.editOrder(userId, order);
	}
}
