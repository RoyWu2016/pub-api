package com.ai.api.dao.impl;

import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.FileDao;
import com.ai.commons.HttpUtil;
import com.ai.commons.JsonUtil;
import com.ai.commons.beans.GetRequest;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.fileservice.FileMetaBean;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/***************************************************************************
 * <PRE>
 * Project Name    : api
 * <p>
 * Package Name    : com.ai.api.dao.impl
 * <p>
 * Creation Date   : 2016/6/30 12:49
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
@Component
public class FileDaoImpl implements FileDao {

    private static final Logger logger = LoggerFactory.getLogger(FileDaoImpl.class);

    @Autowired
    @Qualifier("serviceConfig")
    private ServiceConfig config;

    @Override
    public FileMetaBean getFileDetailInfo(String fileId) {
        String url = config.getFileServiceUrl()+ "/getFileInfoById?id=" + fileId;
        GetRequest request = GetRequest.newInstance().setUrl(url);
        ServiceCallResult result;
        FileMetaBean fileMetaBean;
        try {
            result = HttpUtil.issueGetRequest(request);
            fileMetaBean = JsonUtil.mapToObject(result.getResponseString(), FileMetaBean.class);
            return fileMetaBean;
        }catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }

    @Override
    public byte[] downloadFile(String fileId) {
        String url = config.getFileServiceUrl()+ "/getFile?id=" + fileId;

//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        HttpGet httpGet = new HttpGet(url);
//        RequestConfig requestConfig = RequestConfig.custom()
//                .setConnectionRequestTimeout(60000)
//                .setSocketTimeout(60000)
//                .setConnectTimeout(60000).build();//设置请求和传输超时时间
//        httpGet.setConfig(requestConfig);
//
//        CloseableHttpResponse response = null;
//        String responseStr = "";
//        try {
//            response = httpClient.execute(httpGet);
//            responseStr = EntityUtils.toString(response.getEntity());
//        } catch (Exception e) {
//
//        } finally {
//            try {
//                if (response != null) {
//                    response.close();
//                }
//                if (httpClient != null) {
//                    httpClient.close();
//                }
//            } catch (IOException e) {
//            }
//            return responseStr.getBytes();
//        }



        GetRequest request = GetRequest.newInstance().setUrl(url);
        ServiceCallResult result;
        try {
            result = HttpUtil.issueGetRequest(request);
            return result.getResponseString().getBytes();
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        return new byte[0];
    }
}
