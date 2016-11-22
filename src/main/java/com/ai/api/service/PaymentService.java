package com.ai.api.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.payment.api.PaypalInfoBean;

import src.main.java.com.ai.commons.beans.payment.PaymentPaidBean;

/**
 * Project Name : Public-API Package Name : com.ai.api.service Creation Date :
 * 2016/8/12 17:35 Author : Jianxiong.Cai Purpose : TODO History : TODO
 */
public interface PaymentService {
	boolean downloadProformaInvoicePDF(String userId, String invoiceId, HttpServletResponse httpResponse);

	List<PaypalInfoBean> getPaypalPayment(String userId, String orders);

	ApiCallResult markAsPaid(String userId, PaymentPaidBean orders);
}
