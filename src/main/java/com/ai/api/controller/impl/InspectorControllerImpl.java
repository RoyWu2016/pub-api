package com.ai.api.controller.impl;

import com.ai.api.config.ServiceConfig;
import com.ai.api.controller.InspectorController;
import com.ai.commons.StringUtils;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.psi.OrderReportList;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Project Name    : Public-API
 * Package Name    : com.ai.api.controller.impl
 * Creation Date   : 2017/1/4 10:44
 * Author          : Jianxiong.Cai
 * Purpose         : TODO
 * History         : TODO
 */

@RestController
public class InspectorControllerImpl implements InspectorController {

    private static Logger logger = LoggerFactory.getLogger(InspectorControllerImpl.class);

    @Autowired
    @Qualifier("serviceConfig")
    private ServiceConfig config;

    @Override
    @RequestMapping(value = "/inspector/order/{orderId}", method = RequestMethod.GET)
    public ResponseEntity<ApiCallResult> getAllSummaryDetails(@PathVariable("orderId") String orderId) {
        StringBuilder url = new StringBuilder(config.getIpServiceBaseUrl())
                .append("/inspector/order/").append(orderId);
        ApiCallResult callResult = new ApiCallResult();
        try {
            logger.info("getAllSummaryDetails requesting: " + url.toString());
            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(url.toString(),String.class);
            if (StringUtils.isNotBlank(result)) {
                callResult.setContent(result);
            }else {
                callResult.setMessage("get empty result from ip-service!");
            }
            return new ResponseEntity<>(callResult, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            callResult.setMessage("Exception: " + e.toString());
            return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @RequestMapping(value = "/inspector/product/{productId}", method = RequestMethod.GET)
    public ResponseEntity<ApiCallResult> getProductDetails(@PathVariable("productId") String productId) {
        StringBuilder url = new StringBuilder(config.getIpServiceBaseUrl())
                .append("/inspector/product/").append(productId);
        ApiCallResult callResult = new ApiCallResult();
        try {
            logger.info("getProductDetails requesting: " + url.toString());
            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(url.toString(),String.class);
            if (StringUtils.isNotBlank(result)) {
                callResult.setContent(result);
            }else {
                callResult.setMessage("get empty result from ip-service!");
            }
            return new ResponseEntity<>(callResult, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            callResult.setMessage("Exception: " + e.toString());
            return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @RequestMapping(value = "/inspector/ip-guideline/{productId}", method = RequestMethod.GET)
    public ResponseEntity<ApiCallResult> getIpGuideline(@PathVariable("productId") String productId) {
        StringBuilder url = new StringBuilder(config.getIpServiceBaseUrl())
                .append("/inspector/ip-guideline/").append(productId);
        ApiCallResult callResult = new ApiCallResult();
        try {
            logger.info("getIpGuideline requesting: " + url.toString());
            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(url.toString(),String.class);
            if (StringUtils.isNotBlank(result)) {
                callResult.setContent(result);
            }else {
                callResult.setMessage("get empty result from ip-service!");
            }
            return new ResponseEntity<>(callResult, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            callResult.setMessage("Exception: " + e.toString());
            return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @RequestMapping(value = "/report/reports-for-inspector/{inspectorId}", method = RequestMethod.GET)
    public ResponseEntity<ApiCallResult> getAllReportsByInspectorId(@PathVariable("inspectorId") String inspectorId) {
        StringBuilder url = new StringBuilder(config.getIpServiceBaseUrl())
                .append("/report/reports-for-inspector/").append(inspectorId);
        ApiCallResult callResult = new ApiCallResult();
        try {
            logger.info("getAllReportsByInspectorId requesting: " + url.toString());
            RestTemplate restTemplate = new RestTemplate();
//            OrderReportList[] result = restTemplate.getForObject(url.toString(),OrderReportList[].class);
//            if (null!=result && result.length>0) {
            JSONObject result = restTemplate.getForObject(url.toString(),JSONObject.class);
            if (null!=result) {
                callResult.setContent(result);
            }else {
                callResult.setMessage("get empty result from ip-service!");
            }
            return new ResponseEntity<>(callResult, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            callResult.setMessage("Exception: " + e.toString());
            return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @RequestMapping(value = "/report/{orderId}/{productId}/{orderNumber}", method = RequestMethod.GET)
    public ResponseEntity<ApiCallResult> getReportDetail(@PathVariable("orderId") String orderId,
                                                         @PathVariable("productId") String productId,
                                                         @PathVariable("orderNumber") String orderNumber) {
        StringBuilder url = new StringBuilder(config.getIpServiceBaseUrl())
                .append("/report/").append(orderId).append("/").append(productId).append("/").append(orderNumber);
        ApiCallResult callResult = new ApiCallResult();
        try {
            logger.info("getReportDetail requesting: " + url.toString());
            RestTemplate restTemplate = new RestTemplate();
//            Map<String,String> result = restTemplate.getForObject(url.toString(),HashMap.class);
            JSONObject result = restTemplate.getForObject(url.toString(),JSONObject.class);
            if (null!=result) {
                callResult.setContent(result);
            }else {
                callResult.setMessage("get empty result from ip-service!");
            }
            return new ResponseEntity<>(callResult, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            callResult.setMessage("Exception: " + e.toString());
            return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @RequestMapping(value = "/report/list-reports-for-inspector/{inspectorId}", method = RequestMethod.GET)
    public ResponseEntity<ApiCallResult> getAllReports(@PathVariable("inspectorId") String inspectorId) {
        StringBuilder url = new StringBuilder(config.getIpServiceBaseUrl())
                .append("/report/list-reports-for-inspector/").append(inspectorId);
        ApiCallResult callResult = new ApiCallResult();
        try {
            logger.info("getAllReports requesting: " + url.toString());
            RestTemplate restTemplate = new RestTemplate();
//            Map<String,Map<String,String>> result = restTemplate.getForObject(url.toString(),HashMap.class);
            JSONObject result = restTemplate.getForObject(url.toString(),JSONObject.class);
            if (null!=result) {
                callResult.setContent(result);
            }else {
                callResult.setMessage("get empty result from ip-service!");
            }
            return new ResponseEntity<>(callResult, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            callResult.setMessage("Exception: " + e.toString());
            return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @RequestMapping(value = "/inspector/test/{productId}", method = RequestMethod.GET)
    public ResponseEntity<ApiCallResult> getTestDetails(@PathVariable("productId") String productId) {
        StringBuilder url = new StringBuilder(config.getIpServiceBaseUrl())
                .append("/inspector/test/").append(productId);
        ApiCallResult callResult = new ApiCallResult();
        try {
            logger.info("getTestDetails requesting: " + url.toString());
            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(url.toString(),String.class);
            if (StringUtils.isNotBlank(result)) {
                callResult.setContent(result);
            }else {
                callResult.setMessage("get empty result from ip-service!");
            }
            return new ResponseEntity<>(callResult, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            callResult.setMessage("Exception: " + e.toString());
            return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @RequestMapping(value = "/inspector/defects/{productId}", method = RequestMethod.GET)
    public ResponseEntity<ApiCallResult> getDefectsDetails(@PathVariable("productId") String productId) {
        StringBuilder url = new StringBuilder(config.getIpServiceBaseUrl())
                .append("/inspector/defects/").append(productId);
        ApiCallResult callResult = new ApiCallResult();
        try {
            logger.info("getDefectsDetails requesting: " + url.toString());
            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(url.toString(),String.class);
            if (StringUtils.isNotBlank(result)) {
                callResult.setContent(result);
            }else {
                callResult.setMessage("get empty result from ip-service!");
            }
            return new ResponseEntity<>(callResult, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            callResult.setMessage("Exception: " + e.toString());
            return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @RequestMapping(value = "/inspector/products-for-protocol", method = RequestMethod.GET)
    public ResponseEntity<ApiCallResult> getAllProductsForProtocol() {
        StringBuilder url = new StringBuilder(config.getIpServiceBaseUrl())
                .append("/report/products-for-protocol");
        ApiCallResult callResult = new ApiCallResult();
        try {
            logger.info("getAllProductsForProtocol requesting: " + url.toString());
            RestTemplate restTemplate = new RestTemplate();
            JSONObject result = restTemplate.getForObject(url.toString(),JSONObject.class);
            if (null!=result) {
                callResult.setContent(result);
            }else {
                callResult.setMessage("get empty result from ip-service!");
            }
            return new ResponseEntity<>(callResult, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            callResult.setMessage("Exception: " + e.toString());
            return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @RequestMapping(value = "/inspector/reports-for-supervisor", method = RequestMethod.GET)
    public ResponseEntity<ApiCallResult> getAllReportsForSupervisor() {
        StringBuilder url = new StringBuilder(config.getIpServiceBaseUrl())
                .append("/report/reports-for-supervisor");
        ApiCallResult callResult = new ApiCallResult();
        try {
            logger.info("getAllReportsForSupervisor requesting: " + url.toString());
            RestTemplate restTemplate = new RestTemplate();
            JSONObject result = restTemplate.getForObject(url.toString(),JSONObject.class);
            if (null!=result) {
                callResult.setContent(result);
            }else {
                callResult.setMessage("get empty result from ip-service!");
            }
            return new ResponseEntity<>(callResult, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            callResult.setMessage("Exception: " + e.toString());
            return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
