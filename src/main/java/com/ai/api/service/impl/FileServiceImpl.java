package com.ai.api.service.impl;

import com.ai.api.bean.FileDetailBean;
import com.ai.api.dao.FileDao;
import com.ai.api.exception.AIException;
import com.ai.api.service.FileService;
import com.ai.commons.beans.fileservice.FileMetaBean;
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
        Map<String,String> bucketMap = new HashMap<>();
        bucketMap.put("access-map","ACCESS_MAP");
        bucketMap.put("supplier-certs","BUS_LIC, EXPORT_LIC, ROHS_CERT, TAX_CERT, ISO_CERT, OTHER_DOC");
        bucketMap.put("dm-general-instruction","GI_COORDINATION, GI_SAMPLE_REF, GI_SAMPLE_PROD, GI_PROTOCAL, GI_INSP_RPT, GI_LAB_TEST");
        bucketMap.put("order-attachments","ORDER_ATT");

        logger.info("uploadFile-userId:"+userId);
        logger.info("uploadFile-docType:"+docType);
        logger.info("uploadFile-sourceId:"+sourceId);
        List<MultipartFile> uploadFiles = new ArrayList<>();
        Map<String, String> requestMap = new HashMap<>();
        String bucket = "";
        String createBy = "";

        for (Map.Entry<String,String> entry:bucketMap.entrySet()){
            if (entry.getValue().indexOf(docType.toUpperCase())<0){
                bucket = entry.getKey();
                break;
            }
        }
        logger.info("uploadFile-bucket:"+bucket);
        requestMap.put("srcId", sourceId);
        requestMap.put("fileType", docType);
        requestMap.put("bucket", bucket);
        requestMap.put("createBy", createBy);
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        Iterator<String> fileNames = multipartHttpServletRequest.getFileNames();
        if (fileNames == null) {
            throw new IllegalArgumentException("No upload file!");
        }

        while (fileNames.hasNext()){
            String fileName = fileNames.next();
            logger.info("upload file !fileName:"+fileName);
            MultipartFile file = multipartHttpServletRequest.getFile(fileName);
            uploadFiles.add(file);
        }
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


}
