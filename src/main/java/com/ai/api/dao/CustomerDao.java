package com.ai.api.dao;

import com.ai.api.exception.AIException;
import com.ai.commons.beans.customer.GeneralUserViewBean;
import com.ai.commons.beans.user.GeneralUserBean;

public interface CustomerDao {

    String getCustomerIdByCustomerLogin(String login) throws AIException;

	GeneralUserViewBean getGeneralUserViewBean(String customer_id);

	GeneralUserBean getGeneralUser(String userId);

	boolean updateGeneralUser(GeneralUserBean newUser);

}
