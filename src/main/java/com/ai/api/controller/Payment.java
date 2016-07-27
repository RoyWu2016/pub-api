package com.ai.api.controller;

import java.io.IOException;
import java.util.List;

import com.ai.api.exception.AIException;
import com.ai.commons.beans.payment.PaymentSearchResultBean;
import org.springframework.http.ResponseEntity;

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
	ResponseEntity<List<PaymentSearchResultBean>> getPaymentList(String userId, String archived,
	                                                             String start, String end,
	                                                             String keywords, Integer page) throws IOException, AIException;

}
