/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ai.api.bean.OrderSearchBean;
import com.ai.api.bean.UserBean;
import com.ai.api.dao.LTOrderDao;
import com.ai.api.exception.AIException;
import com.ai.api.service.LTOrderService;
import com.ai.api.service.UserService;

/***************************************************************************
 *<PRE>
 *  Project Name    : api
 *
 *  Package Name    : com.ai.api.service
 *
 *  File Name       : LTOrderServiceImpl.java
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

@Service
public class LTOrderServiceImpl implements LTOrderService {

	protected Logger logger = LoggerFactory.getLogger(LTOrderServiceImpl.class);

	@Autowired
	@Qualifier("ltOrderDao")
	private LTOrderDao ltOrderDao;

	@Autowired
	@Qualifier("userService")
	private UserService userService;

	@Override
	public List<OrderSearchBean> searchLTOrders(String userId, String serviceType, String orderStatus, String pageSize, String pageNumber)  throws IOException, AIException {
		String companyId = "";
		String parentId = "";
		List<OrderSearchBean> orderSearchList = new ArrayList<OrderSearchBean>();
		UserBean user = userService.getCustById(userId);
		if (null!=user){
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null) parentId = "";
			companyId = user.getCompany().getId();
		}
		if(StringUtils.stripToEmpty(serviceType).isEmpty()) {
			orderSearchList.addAll(ltOrderDao.searchLTOrders(companyId, orderStatus, pageSize, pageNumber, "desc"));	
		}
		
		return orderSearchList;
	}
}
