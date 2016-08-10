package com.ai.api.dao.impl;

import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.ReportDao;
import com.ai.commons.HttpUtil;
import com.ai.commons.JsonUtil;
import com.ai.commons.beans.GetRequest;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.report.ReportSearchCriteriaBean;
import com.ai.commons.beans.report.ReportSearchResultBean;
import com.ai.commons.beans.report.ReportsForwardingBean;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * Created by yan on 2016/7/25.
 */
@Component
public class ReportDaoImpl implements ReportDao {

    private static final Logger logger = LoggerFactory.getLogger(ReportDaoImpl.class);

    @Autowired
    @Qualifier("serviceConfig")
    private ServiceConfig config;

    @Override
    public List<ReportSearchResultBean> getUserReportsByCriteria(ReportSearchCriteriaBean criteria) {
        String url = config.getMwServiceUrl() + "/service/report/search";
        try {
            ServiceCallResult result = HttpUtil.issuePostRequest(url, null, criteria);
            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {

                return JsonUtil.mapToObject(result.getResponseString(),
                        new TypeReference<List<ReportSearchResultBean>>() {
                        });

            } else {
                logger.error("get reports from middleware error: " + result.getStatusCode() +
                        ", " + result.getResponseString());
            }

        } catch (IOException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    @Override
    public boolean forwardReports(ReportsForwardingBean reportsForwardingBean){
        String url = config.getMwServiceUrl() + "/service/report/forward";
        try {
            ServiceCallResult result = HttpUtil.issuePostRequest(url, null, reportsForwardingBean);
            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
                return true;
            } else {
                logger.error("forward reports from middleware error: " + result.getStatusCode() +
                        ", " + result.getResponseString());
                return false;
            }
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    @Override
    public boolean undoDecision(String login,String reportDetailId){
        String url = config.getMwServiceUrl() + "/service/report/undo";
        try {
            url = url+"/"+reportDetailId+"?login="+login;
            GetRequest request = GetRequest.newInstance().setUrl(url);
            logger.info("get!!! Url:"+url );
            ServiceCallResult result = HttpUtil.issueGetRequest(request);
            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
                return true;
            } else {
                logger.error("forward reports from middleware error: " + result.getStatusCode() +
                        ", " + result.getResponseString());
                return false;
            }
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
            return false;
        }
    }
}
