package com.ai.api.controller;

import com.ai.commons.beans.ApiCallResult;
import org.springframework.http.ResponseEntity;

/**
 * Project Name    : Public-API
 * Package Name    : com.ai.api.controller
 * Creation Date   : 2016/12/7 10:40
 * Author          : Jianxiong.Cai
 * Purpose         : TODO
 * History         : TODO
 */
public interface Finance {

    ResponseEntity<ApiCallResult> processNSLog(String nsLogs);

    ResponseEntity<ApiCallResult> processCreditOrDebitMemos(String cndnMap);

    ResponseEntity<ApiCallResult> processInvoice(String invMap);
}
