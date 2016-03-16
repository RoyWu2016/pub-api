/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.service.impl;

import java.io.IOException;

import com.ai.api.model.UserBean;
import com.ai.api.service.CustomerService;
import com.ai.api.service.ServiceConfig;
import com.ai.commons.HttpUtil;
import com.ai.commons.JsonUtil;
import com.ai.commons.beans.GetRequest;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.customer.CustomerBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/***************************************************************************
 *<PRE>
 *  Project Name    : api
 *
 *  Package Name    : com.ai.api.service
 *
 *  File Name       : CustomerService.java
 *
 *  Creation Date   : Mar 16, 2016
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

@Service("customerService")
public class CustomerServiceImpl implements CustomerService {

	//TODO: fix the injection here
//	@Value("${customer.service.url}")
//	private String customerServiceUrl;

	@Autowired
	@Qualifier("serviceConfig")
	private ServiceConfig config;

	@Override
	public UserBean getByLogin(String login) throws IOException {
		//TODO: find customer by login, hard code it for now
		String customer_id = "0273AC1639A6254248257DCE0012DC40";

//		String url = customerServiceUrl + "/customer/";
		String url = config.getCustomerServiceUrl() + "/customer/" + customer_id;

		System.out.println("will get from: " + url );

		//TODO: pass session with USM
//		final UserSecurityModel usm = (UserSecurityModel) session.getAttribute(SSOConstant.ATTR_USM);
//
//		ServiceCallResult result = HttpUtil.issueGetRequest(url, new HashMap<String, String>() {
//			private static final long serialVersionUID = 2623229072511782593L; {
//				put("Authorization", "Bearer " + usm.getToken());
//			}
//		}, session);

		//do below for now
		GetRequest request = GetRequest.newInstance().setUrl(url);
		ServiceCallResult result = HttpUtil.issueGetRequest(request) ;

		CustomerBean customer;
		UserBean user = new UserBean();
		if (result.getStatusCode() == 200 || result.getStatusCode() == 202) {
			customer = JsonUtil.mapToObject(result.getResponseString(),
					CustomerBean.class);

			user.setLogin(login);
			user.setUser_id(customer.getCustomerId());
			user.setSic(customer.getOverview().getSic());
			user.setClient_id(customer.getOverview().getSic());
			return user;
		}
		return null;
	}
}
