package com.ai.api.dao.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.PaymentDao;
import com.ai.api.util.FTPUtil;
import com.ai.commons.HttpUtil;
import com.ai.commons.JsonUtil;
import com.ai.commons.beans.GetRequest;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.payment.api.PaymentItemParamBean;
import com.ai.commons.beans.payment.api.PaypalInfoBean;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Project Name    : Public-API
 * Package Name    : com.ai.api.dao.impl
 * Creation Date   : 2016/8/12 16:04
 * Author          : Jianxiong.Cai
 * Purpose         : TODO
 * History         : TODO
 */
@Component
public class PaymentDaoImpl implements PaymentDao {
    private static final Logger logger = LoggerFactory.getLogger(PaymentDaoImpl.class);

    @Autowired
    @Qualifier("serviceConfig")
    private ServiceConfig config;

    @Override
    public InputStream downloadProformaInvoicePDF(String login,String invoiceId){
        String url = config.getMwServiceUrl() + "/service/payment/proformaInvoicePDF?login="+login+"&invoiceId="+invoiceId;
        try {
            GetRequest request = GetRequest.newInstance().setUrl(url);
            logger.info("get!!! Url:"+url );
            ServiceCallResult result = HttpUtil.issueGetRequest(request);
            logger.info("get done!");
            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
                logger.info("get OK!");
                String remotePath = "/CACHE/";
                String fileName = result.getResponseString();
//                String host = config.getMwFTPHost();
	            String host = "";
                int port = 21;
                String username = "";
                String password = "";
//	            String username = config.getMwFTPUsername();
//	            String password = config.getMwFTPPassword();
                String tempPath = "/tmp";
                logger.info(remotePath);
                logger.info(fileName);
                logger.info(host+":"+port);
                logger.info(username+" || "+password);
                logger.info(tempPath);
                boolean b = FTPUtil.downloadFile(host,port,username,password,remotePath,fileName,tempPath);
                if (b){
                    logger.info("success downloadFile to /tmp ");
                    File tempFile = new File(tempPath + fileName);
                    InputStream inputStream = new FileInputStream(tempFile);
                    return inputStream;
                }else {
                    logger.error("ERROR! fail to download PDF from FTP server. ");
                    return null;
                }
            } else {
                logger.error("downloadProformaInvoicePDF from middleware error: " + result.getStatusCode() +
                        ", " + result.getResponseString());
                return null;
            }
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    @Override
    public boolean markAsPaid(String userId, PaymentItemParamBean paymentItemParamBean) {
        try{
            String url = config.getMwServiceUrl() + "/service/payment/markAsPaid";
            ServiceCallResult result = HttpUtil.issuePostRequest(url, null, paymentItemParamBean);
            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
                return true;
            } else {
                logger.error("Mark Payment As Paid from middleware error: " + result.getStatusCode() + ", " + result.getResponseString());
            }
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return false;
    }

    @Override
    public List<PaypalInfoBean> getPaypalPayment(String userId, String login, String orders) {
        try{
            String url = config.getMwServiceUrl() + "/service/payment/paypalPayment?userId="+userId+"&login="+login+"&order_ids_array="+orders;
            GetRequest request = GetRequest.newInstance().setUrl(url);
            ServiceCallResult result = HttpUtil.issueGetRequest(request);
            return JsonUtil.mapToObject(result.getResponseString(), new TypeReference<List<PaypalInfoBean>>(){});
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }
}
