package com.ai.api.controller.impl;

import com.ai.api.bean.consts.ConstMap;
import com.ai.api.util.RedisUtil;
import com.ai.commons.beans.fileservice.FileMetaBean;
import com.ai.commons.services.FileService;
import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * Project Name    : Public-API
 * Package Name    : com.ai.api.job
 * Creation Date   : 2017/2/22 17:27
 * Author          : Hugo Choi
 * Purpose         : TODO
 * History         : TODO
 */
@RestController
public class UploadPictureTool {
    protected  Logger logger = LoggerFactory.getLogger(getClass());

    private static String storeKey = "auditPreviewImages";
    private static String storeContentKey = "auditPreviewImagesContent";
    private static String docType = "AUDIT_PREVIEW_DOC";

    @Autowired
    @Qualifier("fileService")
    private FileService fileService;

    @RequestMapping(value = "/run-upload", method = RequestMethod.GET)
    public ResponseEntity run() {

        try {
            String bucket = ConstMap.bucketMap.get(docType);
            String userName = "uploadAuditImgJob";
            File baseFolder = new File("d:/auditPreviewImages");

            Set<String> keys = RedisUtil.hkeys(storeKey);
            if (null!=keys&&keys.size()>0) {
                for (String key : keys) {
                    String fileIds = RedisUtil.hget(storeKey,key);
                    List<String> idList = JSON.parseArray(fileIds,String.class);
                    for(String fId:idList){
                        fileService.deleteFile(fId,userName);
                        logger.info("deleted file! fId:"+fId);
                    }
                    RedisUtil.hdel(storeKey, key);
//                    RedisUtil.hdel(storeContentKey, key);
                }
                logger.info("flushed auditPreviewImages!!!");
            }

            if (!baseFolder.isDirectory()){
                throw new Exception("Wrong path,please check!!!!!!!!!!");
            }
            for (File fieldFolder:baseFolder.listFiles()) {
                if (!fieldFolder.isDirectory()) {
                    throw new Exception("can't get fieldFolder,please check!!!!!!!!!!");
                }
                if (fieldFolder.listFiles().length<1){
                    logger.info("fieldFolder["+fieldFolder+"] is empty!!!!!!!!!");
                    continue;
                }
                String field = fieldFolder.getName();
                List<String> fileIds = new ArrayList<>();
//                List<String> fileContents = new ArrayList<>();
                for (File file:fieldFolder.listFiles()){
//                    InputStream inputStream = new FileInputStream(file);
//                    final byte[] bytes64bytes = Base64.getEncoder().encode(IOUtils.toByteArray(inputStream));
//                    final String fileContent = new String(bytes64bytes);
//                    fileContents.add(fileContent);
                    FileMetaBean fileMetaBean = fileService.upload(field, docType, bucket, userName, file);
                    logger.info("uploaded file! sourceId:"+field+" || docType:"+docType+" || bucket:"+bucket+" || fileName:"+file.getName());
                    fileIds.add(fileMetaBean.getId());
                }
                RedisUtil.hset(storeKey, field, JSON.toJSONString(fileIds),RedisUtil.HOUR*24*30*12);
//                RedisUtil.hset(storeContentKey, field, JSON.toJSONString(fileContents),RedisUtil.HOUR*24*30*12);
                logger.info("add field["+field+"] success!!!!!!!");
            }
        }catch (Exception e){
            logger.error("",e);
        }
        return new ResponseEntity<>( HttpStatus.OK);
    }
}
