package com.ai.api.dao.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.FileDao;
import com.ai.commons.HttpUtil;
import com.ai.commons.JsonUtil;
import com.ai.commons.beans.GetRequest;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.fileservice.FileMetaBean;
import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


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
        String url = config.getFileServiceUrl() + "/getFileInfoById?id=" + fileId;
        GetRequest request = GetRequest.newInstance().setUrl(url);
        ServiceCallResult result;
        FileMetaBean fileMetaBean;
        try {
            result = HttpUtil.issueGetRequest(request);
            fileMetaBean = JsonUtil.mapToObject(result.getResponseString(), FileMetaBean.class);
            return fileMetaBean;
        } catch (Exception e) {
            logger.error("ERROR from-Dao[getFileDetailInfo]", e);
        }
        return null;
    }

    @Override
    public InputStream downloadFile(String fileId) {
        String url = config.getFileServiceUrl() + "/getFile?id=" + fileId;
        InputStream inputStream = null;
        try {
            HttpClient httpclient = HttpClients.createDefault();
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            inputStream = entity.getContent();
        } catch (Exception e) {
            logger.error("ERROR from-Dao[downloadFile]", e);
        }
        return inputStream;
    }

    @Override
    public List<FileMetaBean> uploadFile(Map<String, String> paramMap,List<MultipartFile> files) {

        String url = config.getFileServiceUrl() + "/createFile";
        if(files.size()>1){
            url = config.getFileServiceUrl() + "/createFiles";
        }

        String responseJson;
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(60000)
                    .setConnectTimeout(60000)
                    .setSocketTimeout(60000).build();
            MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
            multipartEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            if (paramMap != null && paramMap.size() > 0) {
                for (String key : paramMap.keySet()) {
                    String value = paramMap.get(key);
                    multipartEntity.addTextBody(key, value);
                }
            }

            if (files != null && files.size() > 0) {
                for (MultipartFile file : files) {
                    multipartEntity.addBinaryBody(file.getOriginalFilename(), file.getInputStream(), ContentType.create(file.getContentType()), file.getOriginalFilename());
                }
            }
            httpPost.setEntity(multipartEntity.build());
            httpPost.setConfig(requestConfig);
            logger.info("uploadFile POST-url:["+url+"] POST-paramMap:"+JSON.toJSONString(paramMap));
            HttpEntity entity = httpClient.execute(httpPost).getEntity();
            responseJson = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
            List<FileMetaBean> fileMetaBeanList = new ArrayList<>();
            if(files.size()>1){
                fileMetaBeanList = JSON.parseArray(responseJson,FileMetaBean.class);
            }else{
                FileMetaBean fileMetaBean = JSON.parseObject(responseJson,FileMetaBean.class);
                fileMetaBeanList.add(fileMetaBean);
            }

            return fileMetaBeanList;
        } catch (Exception e) {
            logger.error("ERROR from-Dao[uploadFile]", e);
        }
        return null;
    }

    @Override
    public boolean deleteFile(String fileId,String userName) {
        String url = config.getFileServiceUrl() + "/deleteFile?id="+fileId;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        boolean b = false;
        try {
            HttpDelete httpDelete = new HttpDelete(url);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(60000)
                    .setSocketTimeout(60000)
                    .setConnectTimeout(60000).build();
            logger.info("delete file doDelete----url:["+url+"]");
            httpDelete.setConfig(requestConfig);
            response = httpClient.execute(httpDelete);
            if (response.getStatusLine().getStatusCode() == 200) {
                b = true;
            }
            String responseJson = EntityUtils.toString(response.getEntity(), "UTF-8");
            logger.info("delete logo responseJson:"+responseJson);

        }catch (Exception e){
            logger.error("ERROR",e);
        }
        return b;
    }
}
