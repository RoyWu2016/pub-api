package com.ai.api.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ai.api.bean.UserBean;
import com.ai.api.dao.CustomerDao;
import com.ai.api.dao.PaymentDao;
import com.ai.api.exception.AIException;
import com.ai.api.service.PaymentService;
import com.ai.api.service.UserService;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.payment.api.PaypalInfoBean;

import src.main.java.com.ai.commons.beans.payment.PaymentPaidBean;

/**
 * Project Name : Public-API Package Name : com.ai.api.service.impl Creation
 * Date : 2016/8/12 17:36 Author : Jianxiong.Cai Purpose : TODO History : TODO
 */
@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private PaymentDao paymentDao;

	@Autowired
	@Qualifier("customerDao")
	private CustomerDao customerDao;

	@Autowired
	@Qualifier("userService")
	private UserService userService;

	@Override
	public boolean downloadProformaInvoicePDF(String userId, String invoiceId, HttpServletResponse httpResponse) {
		boolean b = false;
		try {
			String login = userService.getLoginByUserId(userId);// customerDao.getGeneralUser(userId).getLogin();
			httpResponse.setHeader("Content-Disposition",
					"attachment; filename=attachment-" + new Date().getTime() + ".pdf");
			InputStream inputStream = paymentDao.downloadProformaInvoicePDF(login, invoiceId);
			ServletOutputStream output = httpResponse.getOutputStream();
			httpResponse.setStatus(HttpServletResponse.SC_OK);
			if (null != inputStream) {
				byte[] buffer = new byte[10240];
				for (int length = 0; (length = inputStream.read(buffer)) > 0;) {
					output.write(buffer, 0, length);
				}
			}
			b = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	@Override
	public ApiCallResult markAsPaid(String userId, PaymentPaidBean orders){
		UserBean userBean;
		String parentId = "";
		String companyId = "";
		try {
			userBean = userService.getCustById(userId);
			if (null != userBean) {
				parentId = userBean.getCompany().getParentCompanyId();
				if (null == parentId) {
					parentId = "";
				}
				companyId = userBean.getCompany().getId();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return paymentDao.markAsPaid(userId, parentId, companyId,orders);
	}

	@Override
	public List<PaypalInfoBean> getPaypalPayment(String userId, String orders) {
		String login = customerDao.getGeneralUser(userId).getLogin();
		return paymentDao.getPaypalPayment(userId, login, orders);
	}
}
