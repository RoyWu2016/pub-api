package com.ai.api.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/***************************************************************************
 * <PRE>
 * Project Name    : api
 * <p>
 * Package Name    : com.ai.api.util
 * <p>
 * Creation Date   : 2016/6/30 18:18
 * <p>
 * Author          : Jianxiong Cai
 * <p>
 * Purpose         : TODO
 * <p>
 * <p>
 * History         : TODO
 * <p>
 * </PRE>
 ***************************************************************************/


public class HttpClientUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    public static String doGet(String url) throws Exception {
        logger.info("doGet - url:" + url);
        String respJson = ""; // 响应内容
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(url);
//        httpGet.addHeader("Content-Type", "application/json");
//        httpGet.addHeader("charset", "UTF-8");
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(60000)
                .setSocketTimeout(60000)
                .setConnectTimeout(60000).build();//设置请求和传输超时时间
        httpGet.setConfig(requestConfig);

        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                respJson = EntityUtils.toString(entity, "UTF-8");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                throw e;
            }
        }

        logger.info("doGet - respJson:" + respJson);
        return respJson;
    }


    public static String doPost(String url, String jsonData) throws Exception {
        logger.info("doPost - url:" + url);
        logger.info("doPost - jsonData:" + jsonData);
        String respJson = ""; // 响应内容
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(60000)
                .setConnectTimeout(60000)
                .setSocketTimeout(60000).build();
        CloseableHttpResponse response = null;
        try {
            StringEntity myEntity = new StringEntity(jsonData,  ContentType.APPLICATION_JSON);// 构造请求数据
            httpPost.setEntity(myEntity);// 设置请求体

            httpPost.setConfig(requestConfig);
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            respJson = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                throw e;
            }
        }

        logger.info("doPost - respJson:" + respJson);
        return respJson;
    }

    public static String doPost(String url, String jsonData, String sign) throws Exception {
        logger.info("doPost2 - url:" + url);
        logger.info("doPost2 - jsonData:" + jsonData);
        logger.info("doPost2 - sign:" + sign);
        String respJson = ""; // 响应内容
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(60000)
                .setConnectTimeout(60000)
                .setSocketTimeout(60000).build();
        CloseableHttpResponse response = null;
        try {
            List<NameValuePair> formparams = new ArrayList<NameValuePair>();
            formparams.add(new BasicNameValuePair("data", jsonData));
            formparams.add(new BasicNameValuePair("sign", sign));
            httpPost.setEntity(new UrlEncodedFormEntity(formparams, "GBK"));//UTF-8

            httpPost.setConfig(requestConfig);
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            respJson = EntityUtils.toString(entity, "UTF-8");
            EntityUtils.consume(entity);
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                throw e;
            }
        }

        logger.info("doPost2 - respJson:" + respJson);
        return respJson;
    }
}
