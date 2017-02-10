package com.ai.api.service;

import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.NetPromoterScoreClientInfoBean;
import com.ai.commons.beans.NetPromoterScoreResponseBean;

/**
 * Project Name    : Public-API
 * Package Name    : com.ai.api.service
 * Creation Date   : 2017/2/9 17:23
 * Author          : Hugo Choi
 * Purpose         : TODO
 * History         : TODO
 */
public interface SurveyService {

    ApiCallResult hasSeenRecently(String userId);

    ApiCallResult updateSurveyDate(String userId,NetPromoterScoreClientInfoBean scoreInfo);

    ApiCallResult saveSurvey(String userId,NetPromoterScoreResponseBean score);
}
