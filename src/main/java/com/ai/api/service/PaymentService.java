package com.ai.api.service;

import java.io.IOException;

import com.ai.api.exception.AIException;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.PageBean;
import com.ai.commons.beans.PageParamBean;
import com.ai.commons.beans.payment.PaymentSearchResultBean;

import src.main.java.com.ai.commons.beans.payment.PaymentPaidBean;

/**
 * Project Name : Public-API Package Name : com.ai.api.service Creation Date :
 * 2016/8/12 17:35 Author : Jianxiong.Cai Purpose : TODO History : TODO
 */
public interface PaymentService {
	
//	boolean downloadProformaInvoicePDF(String userId, String invoiceId, HttpServletResponse httpResponse);


	ApiCallResult markAsPaid(String userId, PaymentPaidBean orders);

	PageBean<PaymentSearchResultBean> searchPaymentList(PageParamBean criteria, String userId, String paid)
			throws IOException, AIException;

	ApiCallResult generateGlobalPayment(String userId, String paymentType, String orderIds)
			throws IOException, AIException;
}
