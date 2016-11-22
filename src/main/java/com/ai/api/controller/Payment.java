package com.ai.api.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;

import com.ai.api.exception.AIException;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.PageBean;
import com.ai.commons.beans.payment.GlobalPaymentInfoBean;
import com.ai.commons.beans.payment.PaymentSearchResultBean;
import com.ai.commons.beans.payment.api.PaymentActionLogBean;
import com.ai.commons.beans.payment.api.PaypalInfoBean;

import src.main.java.com.ai.commons.beans.payment.PaymentPaidBean;

/***************************************************************************
 * <PRE>
 * Project Name    : Public-API
 * <p>
 * Package Name    : com.ai.api.controller
 * <p>
 * Creation Date   : 2016/7/27 18:56
 * <p>
 * Author          : Jianxiong Cai
 * <p>
 * Purpose         : TODO
 * <p>
 * <p>
 * History         : TODO
 * <p>
 * </PRE>
 ***************************************************************************/

public interface Payment {
	ResponseEntity<PageBean<PaymentSearchResultBean>> getPaymentList(String userId, String archived, String start,
			String end, String keywords, Integer page, Integer pagesize) throws IOException, AIException;

	ResponseEntity<String> createProformaInvoice(String userId, String orders);

	ResponseEntity<Boolean> reissueProFormaInvoice(String userId, String orders);

	ResponseEntity<Boolean> logPaymentAction(String userId, PaymentActionLogBean logBean);

	ResponseEntity<String> downloadProformaInvoicePDF(String userId, String invoiceId,
			HttpServletResponse httpResponse);

	ResponseEntity<List<PaypalInfoBean>> getPaypalPayment(String userId, String orders);

	ResponseEntity<ApiCallResult> markAsPaid(String userId, PaymentPaidBean orders);

	ResponseEntity<ApiCallResult> generateGlobalPayment(String userId, String paymentType, String orderIds);
}
