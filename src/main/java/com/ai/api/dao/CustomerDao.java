package com.ai.api.dao;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ai.api.bean.EmployeeBean;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.customer.GeneralUserViewBean;
import com.ai.commons.beans.legacy.customer.ClientInfoBean;
import com.ai.commons.beans.payment.GlobalPaymentInfoBean;
import com.ai.commons.beans.payment.PaymentSearchCriteriaBean;
import com.ai.commons.beans.payment.PaymentSearchResultBean;
import com.ai.commons.beans.payment.api.PaymentActionLogBean;
import com.ai.commons.beans.user.GeneralUserBean;

public interface CustomerDao {

//    String getCustomerIdByCustomerLogin(String login) throws AIException;

	GeneralUserViewBean getGeneralUserViewBean(String customer_id);

	GeneralUserBean getGeneralUser(String userId);

	boolean updateGeneralUser(GeneralUserBean newUser);

	ServiceCallResult updateGeneralUserPassword(String userId, HashMap<String,String> pwdMap);

	InputStream getCompanyLogo(String companyId);

	boolean updateCompanyLogo(String companyId, MultipartFile file);

	boolean deleteCompanyLogo(String companyId);

	boolean createNewAccount(ClientInfoBean clientInfoBean);

	List<PaymentSearchResultBean> searchPaymentList(PaymentSearchCriteriaBean criteria) ;

	String createProformaInvoice(String userId, String login, String orders);

	boolean reissueProFormaInvoice(String userId, String login, String orders);

	List<GlobalPaymentInfoBean> generateGlobalPayment(String userId, String login, String orders);

	boolean logPaymentAction(String userId, PaymentActionLogBean logBean);

	EmployeeBean getEmployeeProfile(String employeeId);
}
