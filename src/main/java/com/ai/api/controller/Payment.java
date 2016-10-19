package com.ai.api.controller;

import java.io.IOException;
import java.util.List;

import com.ai.api.exception.AIException;
import com.ai.commons.beans.PageBean;
import com.ai.commons.beans.payment.GlobalPaymentInfoBean;
import com.ai.commons.beans.payment.PaymentSearchResultBean;
import com.ai.commons.beans.payment.api.PaymentActionLogBean;
import com.ai.commons.beans.payment.api.PaymentItemParamBean;
import com.ai.commons.beans.payment.api.PaypalInfoBean;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;

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

	ResponseEntity<List<GlobalPaymentInfoBean>> generateGlobalPayment(String userId, String orders);

	ResponseEntity<Boolean> logPaymentAction(String userId, PaymentActionLogBean logBean);

	ResponseEntity<String> downloadProformaInvoicePDF(String userId, String invoiceId,
			HttpServletResponse httpResponse);

	ResponseEntity<Boolean> markAsPaid(String userId, PaymentItemParamBean paymentItemParamBean);

	ResponseEntity<List<PaypalInfoBean>> getPaypalPayment(String userId, String orders);
}
