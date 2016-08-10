package com.ai.api.dao.impl;

import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.ReportDao;
import com.ai.commons.HttpUtil;
import com.ai.commons.JsonUtil;
import com.ai.commons.beans.GetRequest;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.report.ReportPdfFileInfoBean;
import com.ai.commons.beans.report.ReportSearchCriteriaBean;
import com.ai.commons.beans.report.ReportSearchResultBean;
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
    public List<ReportPdfFileInfoBean> getUserReportPdfInfo(String userId, String reportId) {
        String url = config.getReportServiceUrl() + "/list-pdf-names-and-size/"+reportId;
        try{
            GetRequest request = GetRequest.newInstance().setUrl(url);
            ServiceCallResult result = HttpUtil.issueGetRequest(request);
            List<ReportPdfFileInfoBean> reportPdfInfo = JsonUtil.mapToObject(result.getResponseString(), new TypeReference<List<ReportPdfFileInfoBean>>(){});
            return reportPdfInfo;
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }
}
