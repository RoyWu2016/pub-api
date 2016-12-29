package com.ai.api.controller.impl;

import com.ai.api.bean.finance.NSCreditDebitMemo;
import com.ai.api.bean.finance.NSInvoice;
import com.ai.api.bean.finance.NSLog;
import com.ai.api.config.ServiceConfig;
import com.ai.api.controller.Finance;
import com.ai.commons.HttpUtil;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.ServiceCallResult;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Project Name    : Public-API
 * Package Name    : com.ai.api.controller.impl
 * Creation Date   : 2016/12/7 10:41
 * Author          : Jianxiong.Cai
 * Purpose         : TODO
 * History         : TODO
 */
@RestController
public class FinanceImpl implements Finance {

    private static Logger logger = LoggerFactory.getLogger(FinanceImpl.class);

    @Autowired
    @Qualifier("serviceConfig")
    private ServiceConfig config;

    @Override
    @TokenSecured
    @RequestMapping(value = "/finance/net-suite/logs", method = RequestMethod.POST)
    public ResponseEntity<ApiCallResult> processNSLog(@RequestBody List<NSLog> nsLogs) {
        StringBuilder url = new StringBuilder(config.getFinanceServiceBaseUrl()).append("/netsuite/interface/log/save");
        ApiCallResult callResult = new ApiCallResult();
        try {
            logger.info("processNSLog requesting: " + url.toString());
            ServiceCallResult result = HttpUtil.issuePostRequest(url.toString(), null, nsLogs);
            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
                callResult.setMessage(result.getResponseString());
                return new ResponseEntity<>(callResult, HttpStatus.OK);
            } else {
                logger.error("finance-service failed : " + result.getStatusCode() + ", "+ result.getResponseString());
                callResult.setMessage("failed! : [" + result.getStatusCode() + "] "+ result.getResponseString());
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            callResult.setMessage("Exception: " + e.toString());
        }
        return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    @TokenSecured
    @RequestMapping(value = "/finance/net-suite/memo", method = RequestMethod.POST)
    public ResponseEntity<ApiCallResult> processCreditOrDebitMemos(@RequestBody Map<String, List<NSCreditDebitMemo>> cndnMap) {
        StringBuilder url = new StringBuilder(config.getFinanceServiceBaseUrl()).append("/netsuite/cndn/save");
        ApiCallResult callResult = new ApiCallResult();
        try {
            logger.info("processCreditOrDebitMemos requesting: " + url.toString());
            ServiceCallResult result = HttpUtil.issuePostRequest(url.toString(), null, cndnMap);
            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
                logger.info("processCreditOrDebitMemos done!");
                callResult.setMessage(result.getResponseString());
                return new ResponseEntity<>(callResult, HttpStatus.OK);
            } else {
                logger.error("finance-service failed : " + result.getStatusCode() + ", "+ result.getResponseString());
                callResult.setMessage("failed! : [" + result.getStatusCode() + "] "+ result.getResponseString());
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            callResult.setMessage("Exception: " + e.toString());
        }
        return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    @TokenSecured
    @RequestMapping(value = "/finance/net-suite/invoice", method = RequestMethod.POST)
    public ResponseEntity<ApiCallResult> processInvoice(@RequestBody Map<String, List<NSInvoice>> invMap){
        StringBuilder url = new StringBuilder(config.getFinanceServiceBaseUrl()).append("/netsuite/invoice/save");
        ApiCallResult callResult = new ApiCallResult();
        try {
            logger.info("processInvoice requesting: " + url.toString());
            ServiceCallResult result = HttpUtil.issuePostRequest(url.toString(), null, invMap);
            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
                logger.info("processInvoice done!");
                callResult.setMessage(result.getResponseString());
                return new ResponseEntity<>(callResult, HttpStatus.OK);
            } else {
                logger.error("finance-service failed : " + result.getStatusCode() + ", "+ result.getResponseString());
                callResult.setMessage("failed! : [" + result.getStatusCode() + "] "+ result.getResponseString());
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            callResult.setMessage("Exception: " + e.toString());
        }
        return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
