package com.ai.api.controller;

import com.ai.api.bean.finance.NSCreditDebitMemo;
import com.ai.api.bean.finance.NSInvoice;
import com.ai.api.bean.finance.NSLog;
import com.ai.commons.beans.ApiCallResult;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * Project Name    : Public-API
 * Package Name    : com.ai.api.controller
 * Creation Date   : 2016/12/7 10:40
 * Author          : Jianxiong.Cai
 * Purpose         : TODO
 * History         : TODO
 */
public interface Finance {

    ResponseEntity<ApiCallResult> processNSLog(List<NSLog> nsLogs);

    ResponseEntity<ApiCallResult> processCreditOrDebitMemos(Map<String, List<NSCreditDebitMemo>> cndnMap);

    ResponseEntity<ApiCallResult> processInvoice(Map<String, List<NSInvoice>> invMap);
}
