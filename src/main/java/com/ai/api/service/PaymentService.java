package com.ai.api.service;

import com.ai.commons.beans.payment.api.PaymentItemParamBean;
import com.ai.commons.beans.payment.api.PaypalInfoBean;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Project Name    : Public-API
 * Package Name    : com.ai.api.service
 * Creation Date   : 2016/8/12 17:35
 * Author          : Jianxiong.Cai
 * Purpose         : TODO
 * History         : TODO
 */
public interface PaymentService {
    boolean downloadProformaInvoicePDF(String userId,String invoiceId,HttpServletResponse httpResponse);
    boolean markAsPaid(String userId, PaymentItemParamBean paymentItemParamBean);
    List<PaypalInfoBean> getPaypalPayment(String userId, String orders);
}
