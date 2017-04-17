package com.ai.api.controller.impl;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ai.api.bean.LoginBean;
import com.ai.api.controller.AuthenticationV2;
import com.ai.api.dao.SSOUserServiceDao;
import com.ai.api.service.AuthenticationService;
import com.ai.commons.Consts;
import com.ai.commons.HttpUtil;
import com.ai.commons.StringUtils;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.user.TokenSession;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Hugo on 2017/4/12.
 */
@RestController
@Api(tags = { "AuthenticationV2" }, description = "Authentication tokens V2 APIs")
public class AuthenticationV2Impl implements AuthenticationV2 {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationV2Impl.class);

    @Autowired
    SSOUserServiceDao ssoUserServiceDao;
    @Autowired
    AuthenticationService authenticationService;

    @Override
    @RequestMapping(method = RequestMethod.POST, value = "/auth/v2/token")
    @ApiOperation(value = "User Login Then Get Api Token", response = TokenSession.class)
    public ResponseEntity<ApiCallResult> getAPIToken(@ApiParam(required = true) @RequestBody LoginBean loginBean, HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("getAPIToken...");
        ApiCallResult result = new ApiCallResult();
        String account = loginBean.getAccount();
        String password = loginBean.getPassword();
        String userType = loginBean.getUserType();
        if ((StringUtils.isBlank(account)) || (StringUtils.isBlank(password))) {
            result.setMessage("username/password empty");
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
        if (!Consts.Http.USER_TYPES.contains(userType)) {
            result.setMessage("wrong user type: " + userType);
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
        boolean validateResult = HttpUtil.validatePublicAPICallToken(request);
        if (!validateResult) {
            result.setMessage("AI API call token not present or invalid for login.");
            return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
        }
        ServiceCallResult callResult = authenticationService.userLogin(account, password, userType, request);
        if (callResult.getStatusCode()==HttpServletResponse.SC_OK){
	        ObjectMapper mapper = new ObjectMapper();
	        TokenSession ts = mapper.readValue(callResult.getResponseString(), TokenSession.class);
            result.setContent(ts);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }else {
            logger.error("Fail!getAPIToken..."+callResult.getReasonPhase()+callResult.getResponseString());
            result.setMessage(callResult.getReasonPhase());
            return new ResponseEntity<>(result, HttpStatus.valueOf(callResult.getStatusCode()));
        }
    }

    @Override
    @RequestMapping(method = RequestMethod.PUT, value = "/auth/v2/refresh-token")
    @ApiOperation(value = "Refresh Api Token", response = TokenSession.class)
    public ResponseEntity<ApiCallResult> refreshAPIToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("refreshAPIToken...");
        ApiCallResult result = new ApiCallResult();
        ServiceCallResult callResult = ssoUserServiceDao.refreshAPIToken(request, response);
        if (callResult.getStatusCode()==HttpServletResponse.SC_OK){
	        ObjectMapper mapper = new ObjectMapper();
	        TokenSession ts = mapper.readValue(callResult.getResponseString(), TokenSession.class);
            result.setContent(ts);
            return new ResponseEntity<>(result, HttpStatus.OK);
        }else {
            logger.error("Fail!refreshAPIToken..."+callResult.getReasonPhase()+callResult.getResponseString());
            result.setMessage(callResult.getReasonPhase());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @RequestMapping(method = RequestMethod.PUT, value = "/auth/v2/remove-token")
    @ApiOperation(value = "Remove Api Token", response = String.class)
    public ResponseEntity<ApiCallResult> removeAPIToken(HttpServletRequest request, HttpServletResponse response) {
        logger.info("removeAPIToken...");
        ApiCallResult result = new ApiCallResult();
        ServiceCallResult callResult = authenticationService.removeAPIToken(request, response);
        if (callResult.getStatusCode()==HttpServletResponse.SC_OK){
	        result.setMessage("");
	        result.setContent("");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }else {
            logger.error("Fail!removeAPIToken..."+callResult.getReasonPhase()+callResult.getResponseString());
            result.setMessage(callResult.getReasonPhase());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @RequestMapping(method = RequestMethod.GET, value = "/auth/v2/verify-token")
    @ApiOperation(value = "Verify Public Api Token", response = String.class)
    public ResponseEntity<ApiCallResult> verifyAPIToken(HttpServletRequest request, HttpServletResponse response) {
        logger.info("verifyAPIToken...");
        ApiCallResult result = new ApiCallResult();
        ServiceCallResult callResult = ssoUserServiceDao.verifyAPIToken(request, response);
        if (callResult.getStatusCode()==HttpServletResponse.SC_OK){
	        result.setMessage("");
            result.setContent("");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }else {
            logger.error("Fail!verifyAPIToken..."+callResult.getReasonPhase()+callResult.getResponseString());
            result.setMessage(callResult.getReasonPhase());
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
