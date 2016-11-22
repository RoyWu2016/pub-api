package com.ai.api.dao;

import java.io.InputStream;
import java.util.List;

import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.payment.api.PaypalInfoBean;

import src.main.java.com.ai.commons.beans.payment.PaymentPaidBean;

/**
 * Project Name : Public-API Package Name : com.ai.api.dao Creation Date :
 * 2016/8/12 16:04 Author : Jianxiong.Cai Purpose : TODO History : TODO
 */
public interface PaymentDao {
	InputStream downloadProformaInvoicePDF(String login, String invoiceId);

	List<PaypalInfoBean> getPaypalPayment(String userId, String login, String orders);

	ApiCallResult markAsPaid(String userId, String parentId, String companyId, PaymentPaidBean orders);

}
