package com.ai.api.controller.impl;

import com.ai.api.controller.SurveyController;
import com.ai.api.service.SurveyService;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.NetPromoterScoreClientInfoBean;
import com.ai.commons.beans.NetPromoterScoreResponseBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Project Name    : Public-API
 * Package Name    : com.ai.api.controller.impl
 * Creation Date   : 2017/2/9 16:03
 * Author          : Hugo Choi
 * Purpose         : TODO
 * History         : TODO
 */

@RestController
public class SurveyControllerImpl implements SurveyController {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SurveyService surveyService;

    @Override
    @TokenSecured
    @RequestMapping(value = " /user/{userId}/seen-nps-survey-in-last-7-days", method = RequestMethod.GET)
    public ResponseEntity<ApiCallResult> hasSeenRecently(@PathVariable("userId") String userId) {
        ApiCallResult<Boolean> result = surveyService.hasSeenRecently(userId);
        if (null == result.getMessage()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @TokenSecured
    @RequestMapping(value = "/user/{userId}/inspection-draft", method = RequestMethod.PUT)
    public ResponseEntity<ApiCallResult> updateSurveyDate(@PathVariable("userId") String userId, NetPromoterScoreClientInfoBean scoreInfo) {
        ApiCallResult<Boolean> result = surveyService.updateSurveyDate(userId,scoreInfo);
        if (null == result.getMessage()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @TokenSecured
    @RequestMapping(value = "/user/{userId}/nps-survey", method = RequestMethod.POST)
    public ResponseEntity<ApiCallResult> saveSurvey(@PathVariable("userId") String userId, NetPromoterScoreResponseBean score) {
        ApiCallResult<Boolean> result = surveyService.saveSurvey(userId,score);
        if (null == result.getMessage()) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
