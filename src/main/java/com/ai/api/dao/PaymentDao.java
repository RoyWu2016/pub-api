package com.ai.api.dao;

import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.PageBean;
import com.ai.commons.beans.PageParamBean;
import com.ai.commons.beans.payment.PaymentSearchResultBean;

import com.ai.commons.beans.payment.PaymentPaidBean;

/**
 * Project Name : Public-API Package Name : com.ai.api.dao Creation Date :
 * 2016/8/12 16:04 Author : Jianxiong.Cai Purpose : TODO History : TODO
 */
public interface PaymentDao {
//	InputStream downloadProformaInvoicePDF(String login, String invoiceId);

	ApiCallResult markAsPaid(String userId, String parentId, String companyId, PaymentPaidBean orders);

	PageBean<PaymentSearchResultBean> searchPaymentList(PageParamBean criteria, String userId, String parentId,
			String companyId, String paid);

	ApiCallResult generateGlobalPayment(String userId, String parentId, String companyId, String paymentType,
			String orderIds);

}
