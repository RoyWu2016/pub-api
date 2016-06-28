package com.ai.api.dao;

import java.util.HashMap;

import com.ai.api.bean.SupplierDetailBean;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.customer.GeneralUserViewBean;
import com.ai.commons.beans.user.GeneralUserBean;

public interface CustomerDao {

//    String getCustomerIdByCustomerLogin(String login) throws AIException;

	GeneralUserViewBean getGeneralUserViewBean(String customer_id);

	GeneralUserBean getGeneralUser(String userId);

	boolean updateGeneralUser(GeneralUserBean newUser);

	ServiceCallResult updateGeneralUserPassword(String userId, HashMap<String,String> pwdMap);

	ServiceCallResult getUserSupplierById(String userId);

	SupplierDetailBean getUserSupplierDetailInfoById(String userId, String supplierId);
}
