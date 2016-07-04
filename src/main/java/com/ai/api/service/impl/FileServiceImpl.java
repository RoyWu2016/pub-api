package com.ai.api.service.impl;

import com.ai.api.bean.FileDetailBean;
import com.ai.api.bean.consts.BucketMap;
import com.ai.api.dao.FileDao;
import com.ai.api.exception.AIException;
import com.ai.api.service.FileService;
import com.ai.commons.beans.fileservice.FileMetaBean;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/***************************************************************************
 * <PRE>
 * Project Name    : api
 * <p>
 * Package Name    : com.ai.api.service.impl
 * <p>
 * Creation Date   : 2016/6/30 15:58
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

@Service
public class FileServiceImpl implements FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    private FileDao fileDao;

    @Override
    public FileDetailBean getFileDetailInfo(String userId,String fileId) throws IOException, AIException {
        FileMetaBean fileMetaBean = fileDao.getFileDetailInfo(fileId);
        FileDetailBean fileDetailBean = new FileDetailBean();
        if(null==fileMetaBean)return null;
        fileDetailBean.setId(fileMetaBean.getId());
        fileDetailBean.setDocType(fileMetaBean.getFileType());
        fileDetailBean.setFileName(fileMetaBean.getFileName());
        fileDetailBean.setFileSize(fileMetaBean.getFileSize());
        fileDetailBean.setUrl("/api/user/"+userId+"/file/"+fileId);
        return fileDetailBean;
    }

    @Override
    public boolean downloadFile(String userId, String fileId,HttpServletResponse httpResponse) throws IOException, AIException  {
        FileMetaBean fileMetaBean = fileDao.getFileDetailInfo(fileId);
        String fileName = "attachment";
        if(null!=fileMetaBean){
            fileName = URLEncoder.encode(fileMetaBean.getFileName(), "UTF-8");
            fileName = "attachment-"+ new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date()) +fileName.substring(fileName.lastIndexOf("."),fileName.length());
//            if (fileName.length() > 150) {
//                fileName = new String(fileMetaBean.getFileName().getBytes("UTF-8"), "ISO8859-1");
//            }
        }
        httpResponse.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        InputStream inputStream =  fileDao.downloadFile(fileId);
        ServletOutputStream output = httpResponse.getOutputStream();
        httpResponse.setStatus(HttpServletResponse.SC_OK);
        byte[] buffer = new byte[10240];
        for (int length = 0; (length = inputStream.read(buffer)) > 0;) {
            output.write(buffer, 0, length);
        }
        return  true;
    }

    @Override
    public List<FileDetailBean> uploadFile(String userId, String docType, String sourceId,HttpServletRequest request, HttpServletResponse response) throws IOException, AIException {

        List<MultipartFile> uploadFiles = new ArrayList<>();
        Map<String, String> requestMap = new HashMap<>();
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        Iterator<String> fileNames = multipartHttpServletRequest.getFileNames();
        int a = BucketMap.bucketMap.size();
        logger.info(a+"------------------------------");
        String bucket = BucketMap.bucketMap.get(docType.toUpperCase());
        String createBy = "";//Get info by userId...............................
        if (fileNames == null) {
            throw new IllegalArgumentException("No upload file!");
        }
        while (fileNames.hasNext()){
            String fileName = fileNames.next();
            MultipartFile file = multipartHttpServletRequest.getFile(fileName);
            uploadFiles.add(file);
        }
        if (uploadFiles.size()>1){
            requestMap.put("srcIds", sourceId);
        }else {
            requestMap.put("srcId", sourceId);
        }
        requestMap.put("fileType", docType);
        requestMap.put("bucket", bucket);
        requestMap.put("createBy", createBy);
        logger.info("uploadFile requestMap:"+ JSON.toJSONString(requestMap));
        List<FileMetaBean> fileMetaBeans = fileDao.uploadFile(requestMap,uploadFiles);
        List<FileDetailBean> fileDetailBeans = new ArrayList<>();
        for(FileMetaBean fileMetaBean:fileMetaBeans){
            FileDetailBean fileDetailBean = new FileDetailBean();
            fileDetailBean.setId(fileMetaBean.getId());
            fileDetailBean.setDocType(fileMetaBean.getFileType());
            fileDetailBean.setFileName(fileMetaBean.getFileName());
            fileDetailBean.setFileSize(fileMetaBean.getFileSize());
            fileDetailBean.setUrl("/api/user/"+userId+"/file/"+fileMetaBean.getId());
            fileDetailBeans.add(fileDetailBean);
        }
        return fileDetailBeans;
    }

//    public final static Map<String,String> bucketMap = new HashMap() {{
//
//        bucketMap.put("ACCESS_MAP","access-map");
//        bucketMap.put("BUS_LIC","supplier-certs");
//        bucketMap.put("EXPORT_LIC","supplier-certs");
//        bucketMap.put("ROHS_CERT","supplier-certs");
//        bucketMap.put("TAX_CERT","supplier-certs");
//        bucketMap.put("ISO_CERT","supplier-certs");
//        bucketMap.put("OTHER_DOC","supplier-certs");
//        bucketMap.put("GI_COORDINATION","dm-general-instruction");
//        bucketMap.put("GI_SAMPLE_REF","dm-general-instruction");
//        bucketMap.put("GI_SAMPLE_PROD","dm-general-instruction");
//        bucketMap.put("GI_PROTOCAL","dm-general-instruction");
//        bucketMap.put("GI_INSP_RPT","dm-general-instruction");
//        bucketMap.put("GI_LAB_TEST","dm-general-instruction");
//        bucketMap.put("ORDER_ATT","order-attachments");
//    }};
}
