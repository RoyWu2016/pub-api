package com.ai.api.controller;

import com.ai.api.bean.LoginBean;
import com.ai.commons.beans.ApiCallResult;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Hugo on 2017/4/12.
 */
public interface AuthenticationV2 {

    ResponseEntity<ApiCallResult> getAPIToken(LoginBean loginBean, HttpServletRequest request,HttpServletResponse response);

    ResponseEntity<ApiCallResult>  refreshAPIToken(HttpServletRequest request, HttpServletResponse response);

    ResponseEntity<ApiCallResult>  removeAPIToken(HttpServletRequest request, HttpServletResponse response);

    ResponseEntity<ApiCallResult>  verifyAPIToken(HttpServletRequest request, HttpServletResponse response);
}
