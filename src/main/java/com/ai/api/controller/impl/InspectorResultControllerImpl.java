package com.ai.api.controller.impl;

import com.ai.api.config.ServiceConfig;
import com.ai.api.controller.InspectorResultController;
import com.ai.commons.HttpUtil;
import com.ai.commons.StringUtils;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.psi.InspResultForm;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Project Name    : Public-API
 * Package Name    : com.ai.api.controller.impl
 * Creation Date   : 2017/1/4 15:53
 * Author          : Jianxiong.Cai
 * Purpose         : TODO
 * History         : TODO
 */
@RestController
public class InspectorResultControllerImpl implements InspectorResultController {

    private static Logger logger = LoggerFactory.getLogger(InspectorResultControllerImpl.class);

    @Autowired
    @Qualifier("serviceConfig")
    private ServiceConfig config;

    @Override
    @RequestMapping(value = "/results/{productId}", method = RequestMethod.POST)
    public ResponseEntity<ApiCallResult> saveResult(@PathVariable("productId") String productId,
                                                    @RequestBody InspResultForm aiResultForm,
                                                    @RequestParam(value="username", required=false)  String username) {
        ApiCallResult callResult = new ApiCallResult();
//        StringBuilder url = new StringBuilder("http://127.0.0.1:8888")
        StringBuilder url = new StringBuilder(config.getIpServiceBaseUrl())
                .append("/results/save/").append(productId);
        if (StringUtils.isNotBlank(username)){
            url.append("?username=").append(username);
        }
        try {
            logger.info("saveResult requesting: " + url.toString());
            ServiceCallResult result = HttpUtil.issuePostRequest(url.toString(),null,aiResultForm);
            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
                callResult.setContent(result.getResponseString());
                return new ResponseEntity<>(callResult, HttpStatus.OK);
            }else {
                callResult.setMessage(result.getResponseString());
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            callResult.setMessage("Exception: " + e.toString());
        }
        return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    @RequestMapping(value = "/results/{productId}", method = RequestMethod.PUT)
    public ResponseEntity<ApiCallResult> updateResult(@PathVariable("productId") String productId,
                                                    @RequestBody InspResultForm aiResultForm,
                                                    @RequestParam(value="username", required=false)  String username) {
        ApiCallResult callResult = new ApiCallResult();
        StringBuilder url = new StringBuilder(config.getIpServiceBaseUrl())
                .append("/results/save/").append(productId);
        if (StringUtils.isNotBlank(username)){
            url.append("?username=").append(username);
        }
        try {
            logger.info("updateResult requesting: " + url.toString());
            ServiceCallResult result = HttpUtil.issuePutRequest(url.toString(),null,aiResultForm);
            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
                callResult.setContent(result.getResponseString());
                return new ResponseEntity<>(callResult, HttpStatus.OK);
            }else {
                callResult.setMessage(result.getResponseString());
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            callResult.setMessage("Exception: " + e.toString());
        }
        return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    @RequestMapping(value = "/results/userData/{productId}", method = RequestMethod.POST)
    public ResponseEntity<ApiCallResult> saveUserData(@PathVariable("productId") String productId,
                                                    @RequestBody InspResultForm aiResultForm,
                                                    @RequestParam(value="username", required=false)  String username) {
        ApiCallResult callResult = new ApiCallResult();
        StringBuilder url = new StringBuilder(config.getIpServiceBaseUrl())
                .append("/results/saveUserData/").append(productId);
        if (StringUtils.isNotBlank(username)){
            url.append("?username=").append(username);
        }
        try {
            logger.info("saveUserData requesting: " + url.toString());
            ServiceCallResult result = HttpUtil.issuePostRequest(url.toString(),null,aiResultForm);
            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
                callResult.setContent(result.getResponseString());
                return new ResponseEntity<>(callResult, HttpStatus.OK);
            }else {
                callResult.setMessage(result.getResponseString());
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            callResult.setMessage("Exception: " + e.toString());
        }
        return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    @RequestMapping(value = "/results/{orderId}/{reportType}", method = RequestMethod.GET)
    public ResponseEntity<ApiCallResult> searchByOrderId(@PathVariable("orderId") String orderId,@PathVariable("reportType") String reportType ) {
        ApiCallResult callResult = new ApiCallResult();
        StringBuilder url = new StringBuilder(config.getIpServiceBaseUrl())
                .append("/list-insp-result/order/").append(orderId).append("/").append(reportType);
        try {
            logger.info("searchByOrderId requesting: " + url.toString());
            ServiceCallResult result = HttpUtil.issueGetRequest(url.toString(),null);
            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
                callResult.setContent(result.getResponseString());
                return new ResponseEntity<>(callResult, HttpStatus.OK);
            }else {
                callResult.setMessage(result.getResponseString());
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            callResult.setMessage("Exception: " + e.toString());
        }
        return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    @RequestMapping(value = "/results/product/{productId}/{reportType}", method = RequestMethod.GET)
    public ResponseEntity<ApiCallResult> searchByProductId(@PathVariable("productId") String productId,@PathVariable("reportType") String reportType ) {
        ApiCallResult callResult = new ApiCallResult();
        StringBuilder url = new StringBuilder(config.getIpServiceBaseUrl())
                .append("/list-insp-result/product/").append(productId).append("/").append(reportType);
        try {
            logger.info("searchByProductId requesting: " + url.toString());
            ServiceCallResult result = HttpUtil.issueGetRequest(url.toString(),null);
            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
                callResult.setContent(result.getResponseString());
                return new ResponseEntity<>(callResult, HttpStatus.OK);
            }else {
                callResult.setMessage(result.getResponseString());
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            callResult.setMessage("Exception: " + e.toString());
        }
        return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    @RequestMapping(value = "/results/product/{productId}", method = RequestMethod.GET)
    public ResponseEntity<ApiCallResult> searchByProductId(@PathVariable("productId") String productId) {
        ApiCallResult callResult = new ApiCallResult();
        StringBuilder url = new StringBuilder(config.getIpServiceBaseUrl())
                .append("/map-list-insp-result/product/").append(productId);
        try {
            logger.info("searchByProductId  requesting: " + url.toString());
            ServiceCallResult result = HttpUtil.issueGetRequest(url.toString(),null);
            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
                callResult.setContent(result.getResponseString());
                return new ResponseEntity<>(callResult, HttpStatus.OK);
            }else {
                callResult.setMessage(result.getResponseString());
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            callResult.setMessage("Exception: " + e.toString());
        }
        return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    @RequestMapping(value = "/results/source/{sourceId}/{reportType}", method = RequestMethod.GET)
    public ResponseEntity<ApiCallResult> searchBySourceId(@PathVariable("sourceId") String sourceId,@PathVariable("reportType") String reportType ) {
        ApiCallResult callResult = new ApiCallResult();
        StringBuilder url = new StringBuilder(config.getIpServiceBaseUrl())
                .append("/list-insp-result/source-object/").append(sourceId).append("/").append(reportType);
        try {
            logger.info("searchBySourceId requesting: " + url.toString());
            ServiceCallResult result = HttpUtil.issueGetRequest(url.toString(),null);
            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
                callResult.setContent(result.getResponseString());
                return new ResponseEntity<>(callResult, HttpStatus.OK);
            }else {
                callResult.setMessage(result.getResponseString());
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            callResult.setMessage("Exception: " + e.toString());
        }
        return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    @RequestMapping(value = "/results/source/{sourceId}/{sourceType}/{reportType}", method = RequestMethod.GET)
    public ResponseEntity<ApiCallResult> searchBySourceType(@PathVariable("sourceId") String sourceId,
                                                            @PathVariable("sourceType") String sourceType,
                                                            @PathVariable("reportType") String reportType ) {
        ApiCallResult callResult = new ApiCallResult();
        StringBuilder url = new StringBuilder(config.getIpServiceBaseUrl())
                .append("/list-insp-result/source-object/").append(sourceId).append("/").append(sourceType).append("/").append(reportType);
        try {
            logger.info("searchByReportType requesting: " + url.toString());
            ServiceCallResult result = HttpUtil.issueGetRequest(url.toString(),null);
            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
                callResult.setContent(result.getResponseString());
                return new ResponseEntity<>(callResult, HttpStatus.OK);
            }else {
                callResult.setMessage(result.getResponseString());
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            callResult.setMessage("Exception: " + e.toString());
        }
        return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    @RequestMapping(value = "/supervisor/product/{productId}", method = RequestMethod.GET)
    public ResponseEntity<ApiCallResult> getAllIpSupervisorData(@PathVariable("productId") String productId) {
        ApiCallResult callResult = new ApiCallResult();
        StringBuilder url = new StringBuilder(config.getIpServiceBaseUrl())
                .append("/map-list-ipsupervisor-result/product/").append(productId);
        try {
            logger.info("getAllIpSupervisorData  requesting: " + url.toString());
            ServiceCallResult result = HttpUtil.issueGetRequest(url.toString(),null);
            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
                callResult.setContent(result.getResponseString());
                return new ResponseEntity<>(callResult, HttpStatus.OK);
            }else {
                callResult.setMessage(result.getResponseString());
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            callResult.setMessage("Exception: " + e.toString());
        }
        return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
