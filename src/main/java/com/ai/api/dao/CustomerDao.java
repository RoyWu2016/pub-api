package com.ai.api.dao;

import java.io.InputStream;
import java.util.HashMap;

import com.ai.api.bean.SupplierDetailBean;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.customer.GeneralUserViewBean;
import com.ai.commons.beans.user.GeneralUserBean;
import org.springframework.web.multipart.MultipartFile;

public interface CustomerDao {

//    String getCustomerIdByCustomerLogin(String login) throws AIException;

	GeneralUserViewBean getGeneralUserViewBean(String customer_id);

	GeneralUserBean getGeneralUser(String userId);

	boolean updateGeneralUser(GeneralUserBean newUser);

	ServiceCallResult updateGeneralUserPassword(String userId, HashMap<String,String> pwdMap);

	InputStream getCompanyLogo(String companyId);

	boolean updateCompanyLogo(String companyId, MultipartFile file);

}
