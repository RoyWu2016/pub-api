package com.ai.api.service.impl;

import com.ai.api.bean.UserBean;
import com.ai.api.config.ServiceConfig;
import com.ai.api.service.SurveyService;
import com.ai.api.service.UserService;
import com.ai.commons.DateUtils;
import com.ai.commons.HttpUtil;
import com.ai.commons.JsonUtil;
import com.ai.commons.StringUtils;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.NetPromoterScoreClientInfoBean;
import com.ai.commons.beans.NetPromoterScoreResponseBean;
import com.ai.commons.beans.ServiceCallResult;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * Project Name    : Public-API
 * Package Name    : com.ai.api.service.impl
 * Creation Date   : 2017/2/9 17:25
 * Author          : Hugo Choi
 * Purpose         : TODO
 * History         : TODO
 */

@Service
public class SurveyServiceImpl implements SurveyService{
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    @Qualifier("userService")
    private UserService userService;
    @Autowired
    @Qualifier("serviceConfig")
    private ServiceConfig config;

    @Override
    public ApiCallResult hasSeenRecently(String userId) {
        UserBean user = this.getUserBeanByUserId(userId);
        StringBuilder url = new StringBuilder(config.getCustomerServiceUrl());
        ApiCallResult<Boolean> apiCallResult = new ApiCallResult();
        url.append("/nps-response/get-nps-client-info").append("?login=").append(user.getLogin());
        try {
            ServiceCallResult result = HttpUtil.issueGetRequest(url.toString(), null);
            logger.info(result.getReasonPhase()+"||"+result.getResponseString());
            if(result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
                if (StringUtils.isNotBlank(result.getResponseString())) {
                    apiCallResult.setContent("true".equalsIgnoreCase(result.getResponseString()));
                } else {
                    logger.info("Error!!!seen-nps-survey-in-last-7-days return blank message from customerService");
                    apiCallResult.setContent(false);
                    apiCallResult.setMessage("Error from customerService,blank return!");
                }
            }else {
                apiCallResult.setMessage("error from customerService!"+result.getResponseString());
            }
        } catch (IOException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            apiCallResult.setMessage("Exception: " + e.toString());
        }
        return apiCallResult;
    }

    @Override
    public ApiCallResult updateSurveyDate(String userId, NetPromoterScoreClientInfoBean scoreInfo) {
        UserBean user = this.getUserBeanByUserId(userId);
        StringBuilder url = new StringBuilder(config.getCustomerServiceUrl());
        ApiCallResult<Boolean> apiCallResult = new ApiCallResult();
        url.append("/nps-response/save-nps-client-info");
        try {
            scoreInfo.setLogin(user.getLogin());
            ServiceCallResult result = HttpUtil.issuePostRequest(url.toString(), null,scoreInfo);
            logger.info(result.getReasonPhase()+"||"+result.getResponseString());
            if(result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
                apiCallResult.setContent(JsonUtil.mapToObject(result.getResponseString(), Boolean.class));
            }else {
                apiCallResult.setMessage("error from customerService !"+result.getResponseString());
            }
        } catch (IOException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            apiCallResult.setMessage("Exception: " + e.toString());
        }
        return apiCallResult;
    }

    @Override
    public ApiCallResult saveSurvey(String userId, NetPromoterScoreResponseBean score) {
        UserBean user = this.getUserBeanByUserId(userId);
        StringBuilder url = new StringBuilder(config.getCustomerServiceUrl());
        ApiCallResult<Boolean> apiCallResult = new ApiCallResult();
        url.append("/nps-response/save-nps-response");
        try {
            score.setLoginName(user.getLogin());
            ServiceCallResult result = HttpUtil.issuePostRequest(url.toString(), null,score);
            logger.info(result.getReasonPhase()+"||"+result.getResponseString());
            if(result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
                apiCallResult.setContent(JsonUtil.mapToObject(result.getResponseString(), Boolean.class));
            }else {
                apiCallResult.setMessage("error from customerService! "+result.getResponseString());
            }
        } catch (IOException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            apiCallResult.setMessage("Exception: " + e.toString());
        }
        return apiCallResult;
    }

    private UserBean getUserBeanByUserId(String userId) {
        UserBean user = null;
        try {
            user = userService.getCustById(userId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
}
