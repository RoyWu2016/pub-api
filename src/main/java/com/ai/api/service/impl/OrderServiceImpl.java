/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.service.impl;

import java.util.List;

import com.ai.api.dao.CustomerDao;
import com.ai.api.dao.OrderDao;
import com.ai.api.service.OrderService;
import com.ai.api.service.UserService;
import com.ai.commons.beans.legacy.order.OrderCancelBean;
import com.ai.commons.beans.legacy.order.OrderSearchCriteriaBean;
import com.ai.commons.beans.order.api.SimpleOrderBean;
import com.ai.commons.beans.psi.InspectionOrderBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/***************************************************************************
 *<PRE>
 *  Project Name    : api
 *
 *  Package Name    : com.ai.api.service.impl
 *
 *  File Name       : OrderService.java
 *
 *  Creation Date   : Jul 13, 2016
 *
 *  Author          : Allen Zhang
 *
 *  Purpose         : TODO
 *
 *
 *  History         : TODO
 *
 *</PRE>
 ***************************************************************************/

@Service
public class OrderServiceImpl implements OrderService {

	protected Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Autowired
	@Qualifier("orderDao")
	private OrderDao orderDao;

//	@Autowired
//	@Qualifier("customerDao")
//	private CustomerDao customerDao;

	@Autowired
	@Qualifier("userService")
	private UserService userService;

	@Override
	public List<SimpleOrderBean> getOrdersByUserId(OrderSearchCriteriaBean criteria) {
		if(criteria.getLogin()==null){
			String login = userService.getLoginByUserId(criteria.getUserID());//customerDao.getGeneralUser(criteria.getUserID()).getLogin();
			criteria.setLogin(login);
		}
		return orderDao.getOrdersByUserId(criteria);
	}

	@Override
	public List<SimpleOrderBean> getDraftsByUserId(OrderSearchCriteriaBean criteria) {
		if(criteria.getLogin()==null){
			String login = userService.getLoginByUserId(criteria.getUserID());//customerDao.getGeneralUser(criteria.getUserID()).getLogin();
			criteria.setLogin(login);
		}
		return orderDao.getDraftsByUserId(criteria);
	}

	@Override
	public Boolean cancelOrder(OrderCancelBean orderCancelBean){
		return orderDao.cancelOrder(orderCancelBean);
	}

	@Override
	public InspectionOrderBean getOrderDetail(String userId, String orderId){
		return orderDao.getOrderDetail(userId, orderId);
	}


}
