package com.ai.api.dao.impl;

import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.ReportDao;
import com.ai.commons.HttpUtil;
import com.ai.commons.JsonUtil;
import com.ai.commons.StringUtils;
import com.ai.commons.beans.GetRequest;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.report.ReportPdfFileInfoBean;
import com.ai.commons.beans.report.ReportSearchCriteriaBean;
import com.ai.commons.beans.report.ReportSearchResultBean;
import com.ai.commons.beans.report.ReportsForwardingBean;
import com.ai.commons.beans.report.api.ReportCertificateBean;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public ReportCertificateBean getApprovalCertificate(String reportId, String login, String certType, String reference){
        StringBuilder url = new StringBuilder(config.getMwServiceUrl() + "/service/report/");
        try {
            url = url.append(reportId+"/certificate?login="+login+"&certType="+certType);
            if (StringUtils.isNotBlank(reference)){
                url = url.append("&reference="+reference);
            }
            GetRequest request = GetRequest.newInstance().setUrl(url.toString());
            logger.info("get!!! Url:"+url );
            ServiceCallResult result = HttpUtil.issueGetRequest(request);
            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
                return JSON.parseObject(result.getResponseString(),ReportCertificateBean.class);
            } else {
                logger.error("getApprovalCertificate from middleware error: " +
                        result.getStatusCode() +", " + result.getResponseString());
                return null;
            }
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    @Override
    public boolean confirmApprovalCertificate(ReportCertificateBean reportCertificateBean,String login){
        String url = config.getMwServiceUrl() + "/service/report/confirmApprovalCertificate";
        try {
            String jsonStr = JSON.toJSONString(reportCertificateBean);
            Map<String,String> paramsMap = new HashMap<>();
            paramsMap.put("login",login);
            paramsMap.put("reportCertificateBean",jsonStr);
            logger.info("POST URL:"+url);
            logger.info("login : "+login);
            logger.info("reportCertificateBean : "+reportCertificateBean.toString());
            ServiceCallResult result = HttpUtil.issuePostRequest(url, null, paramsMap);
            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
                return true;
            } else {
                logger.error("confirmApprovalCertificate from middleware error: " + result.getStatusCode() +
                        ", " + result.getResponseString());
                return false;
            }
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    @Override
    public List<ReportPdfFileInfoBean> getUserReportPdfInfo(String userId, String login, String reportId) {
        String url = config.getMwServiceUrl() + "/service/report/fileNames?reportDetailId="+reportId+"&login="+login+"&userId="+userId;
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

    @Override
    public InputStream downloadPDF(String reportId,String fileName){
        String url = config.getReportServiceUrl() + "/attachment/download-pdf/"+reportId+"?fileName=" + fileName;
        InputStream inputStream = null;
        try {
            HttpClient httpclient = HttpClients.createDefault();
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            inputStream = entity.getContent();
        } catch (Exception e) {
            logger.error("ERROR!!! downloadPDF", e);
        }
        return inputStream;

    }

	@Override
	public String exportReports(ReportSearchCriteriaBean criteria){
//		String url = config.getMwServiceUrl() + "/service/report/export";
		String url = "http://127.0.0.1:8888/service/report/export";
		try {
			logger.info("post url:"+url);
			logger.info(criteria.toString());
			ServiceCallResult result = HttpUtil.issuePostRequest(url, null, criteria);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {

				return result.getResponseString();

			} else {
				logger.error("get reports from middleware error: " + result.getStatusCode() +
						", " + result.getResponseString());
			}
		} catch (IOException e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;

	}
}
