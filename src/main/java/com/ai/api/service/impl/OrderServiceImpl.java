/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.service.impl;

import java.io.IOException;
import java.util.List;

import com.ai.api.bean.UserBean;
import com.ai.api.dao.OrderDao;
import com.ai.api.exception.AIException;
import com.ai.api.service.OrderService;
import com.ai.api.service.UserService;
import com.ai.commons.beans.legacy.order.OrderCancelBean;
import com.ai.commons.beans.legacy.order.OrderSearchCriteriaBean;
import com.ai.commons.beans.order.SimpleOrderSearchBean;
import com.ai.commons.beans.order.api.SimpleOrderBean;
import com.ai.commons.beans.psi.InspectionBookingBean;
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
	public InspectionBookingBean getOrderDetail(String userId, String orderId){
		return orderDao.getOrderDetail(userId, orderId);
	}

    @Override
    public InspectionBookingBean createOrderByDraft(String userId, String draftId){
        String companyId = "";
        String parentId = "";
        UserBean user = this.getUserBeanByUserId(userId);
        if (null!=user){
            parentId = user.getCompany().getParentCompanyId();
            if (parentId == null) parentId = "";
            companyId = user.getCompany().getId();
        }
        return orderDao.createOrderByDraft(userId, draftId,companyId,parentId);
    }

    @Override
    public InspectionBookingBean editOrder(String userId, String orderId){
        String companyId = "";
        String parentId = "";
        UserBean user = this.getUserBeanByUserId(userId);
        if (null!=user){
            parentId = user.getCompany().getParentCompanyId();
            if (parentId == null) parentId = "";
            companyId = user.getCompany().getId();
        }
        return orderDao.editOrder(userId, orderId,companyId,parentId);
    }

    @Override
    public InspectionBookingBean saveOrderByDraft(String userId, String draftId){
        String companyId = "";
        String parentId = "";
        UserBean user = this.getUserBeanByUserId(userId);
        if (null!=user){
            parentId = user.getCompany().getParentCompanyId();
            if (parentId == null) parentId = "";
            companyId = user.getCompany().getId();
        }
        return orderDao.saveOrderByDraft(userId, draftId,companyId,parentId);
    }

    private UserBean getUserBeanByUserId(String userId){
        UserBean user = null;
        try {
            user = userService.getCustById(userId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }

	@Override
	public List<SimpleOrderSearchBean> searchOrders(String userId, String serviceType, String startDate, String endDate,
			String keyWord, String orderStatus, String pageSize, String pageNumber) throws IOException, AIException {
		String companyId = "";
		String parentId = "";
		UserBean user = userService.getCustById(userId);
		if (null!=user){
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null) parentId = "";
			companyId = user.getCompany().getId();
		}
		return orderDao.searchOrders(userId, companyId, parentId, serviceType, startDate, endDate, keyWord, orderStatus, pageSize, pageNumber);
	}


}
