package com.ai.api.dao;

import java.io.InputStream;

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
}
