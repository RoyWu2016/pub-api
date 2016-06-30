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

import java.io.IOException;

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
    public byte[] downloadFile(String userId, String fileId) {
        return  fileDao.downloadFile(fileId);
    }
}
