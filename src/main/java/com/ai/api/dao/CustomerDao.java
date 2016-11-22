package com.ai.api.dao;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ai.api.bean.EmployeeBean;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.PageBean;
import com.ai.commons.beans.PageParamBean;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.customer.DashboardBean;
import com.ai.commons.beans.customer.GeneralUserViewBean;
import com.ai.commons.beans.legacy.customer.ClientInfoBean;
import com.ai.commons.beans.payment.GlobalPaymentInfoBean;
import com.ai.commons.beans.payment.PaymentSearchResultBean;
import com.ai.commons.beans.payment.api.PaymentActionLogBean;
import com.ai.commons.beans.user.GeneralUserBean;

public interface CustomerDao {

	// String getCustomerIdByCustomerLogin(String login) throws AIException;

	GeneralUserViewBean getGeneralUserViewBean(String customer_id);

	GeneralUserBean getGeneralUser(String userId);

	boolean updateGeneralUser(GeneralUserBean newUser);

	ServiceCallResult updateGeneralUserPassword(String userId, HashMap<String, String> pwdMap);

	InputStream getCompanyLogo(String companyId);

	boolean updateCompanyLogo(String companyId, MultipartFile file);

	boolean deleteCompanyLogo(String companyId);

	boolean createNewAccount(ClientInfoBean clientInfoBean);

	PageBean<PaymentSearchResultBean> searchPaymentList(PageParamBean criteria, String userId, String parentId,
			String companyId, String paid);

	String createProformaInvoice(String userId, String login, String orders);

	boolean reissueProFormaInvoice(String userId, String login, String orders);

	boolean logPaymentAction(String userId, PaymentActionLogBean logBean);

	EmployeeBean getEmployeeProfile(String employeeId, boolean refresh);

	boolean isACAUser(String login);

	DashboardBean getUserDashboard(String userId, String parentId, String companyId, String startDate, String endDate);

	ServiceCallResult resetPassword(String login);

	ApiCallResult generateGlobalPayment(String userId, String parentId, String companyId, String paymentType,
			String orderIds);
}
