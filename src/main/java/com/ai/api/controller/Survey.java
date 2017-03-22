package com.ai.api.controller;

import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.NetPromoterScoreClientInfoBean;
import com.ai.commons.beans.NetPromoterScoreResponseBean;
import org.springframework.http.ResponseEntity;

/**
 * Project Name    : Public-API
 * Package Name    : com.ai.api.controller
 * Creation Date   : 2017/2/9 15:54
 * Author          : Hugo Choi
 * Purpose         : TODO
 * History         : TODO
 */
public interface Survey {

    ResponseEntity<ApiCallResult> showSurvey(String userId);

    ResponseEntity<ApiCallResult> saveSurvey(String userId,NetPromoterScoreResponseBean score);
}
