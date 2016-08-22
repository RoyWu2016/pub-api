package com.ai.api.dao;

import com.ai.commons.beans.payment.api.PaymentItemParamBean;
import com.ai.commons.beans.payment.api.PaypalInfoBean;

import java.io.InputStream;
import java.util.List;

/**
 * Project Name    : Public-API
 * Package Name    : com.ai.api.dao
 * Creation Date   : 2016/8/12 16:04
 * Author          : Jianxiong.Cai
 * Purpose         : TODO
 * History         : TODO
 */
public interface PaymentDao {
    InputStream downloadProformaInvoicePDF(String login, String invoiceId);
    boolean markAsPaid(String userId, PaymentItemParamBean paymentItemParamBean);
    List<PaypalInfoBean> getPaypalPayment(String userId, String login, String orders);
}
