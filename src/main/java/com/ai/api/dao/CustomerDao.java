package com.ai.api.dao;

import java.io.InputStream;
import java.util.HashMap;

import org.springframework.web.multipart.MultipartFile;

import com.ai.api.bean.EmployeeBean;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.customer.DashboardBean;
import com.ai.commons.beans.customer.GeneralUserViewBean;
import com.ai.commons.beans.legacy.customer.ClientInfoBean;
import com.ai.commons.beans.user.GeneralUserBean;

public interface CustomerDao {

	GeneralUserViewBean getGeneralUserViewBean(String customer_id);

	GeneralUserBean getGeneralUser(String userId);

	boolean updateGeneralUser(GeneralUserBean newUser);

	ServiceCallResult updateGeneralUserPassword(String userId, HashMap<String, String> pwdMap);

	InputStream getCompanyLogo(String companyId);

	boolean updateCompanyLogo(String companyId, MultipartFile file);

	boolean deleteCompanyLogo(String companyId);

	boolean createNewAccount(ClientInfoBean clientInfoBean);

	EmployeeBean getEmployeeProfile(String employeeId, boolean refresh);

	boolean isACAUser(String login);

	DashboardBean getUserDashboard(String userId, String parentId, String companyId, String startDate, String endDate);

	ServiceCallResult resetPassword(String login);

}
