package com.ai.api.service.impl;

import com.ai.api.bean.FileDetailBean;
import com.ai.api.dao.FileDao;
import com.ai.api.exception.AIException;
import com.ai.api.service.FileService;
import com.ai.commons.beans.fileservice.FileMetaBean;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

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
}
