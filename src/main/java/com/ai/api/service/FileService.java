package com.ai.api.service;

import com.ai.api.bean.FileDetailBean;
import com.ai.api.exception.AIException;
import org.apache.http.client.methods.CloseableHttpResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/***************************************************************************
 * <PRE>
 * Project Name    : api
 * <p>
 * Package Name    : com.ai.api.service
 * <p>
 * Creation Date   : 2016/6/30 14:17
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
public interface FileService {

    FileDetailBean getFileDetailInfo(String userId,String fileId) throws IOException, AIException;

    boolean downloadFile(String userId, String fileId,HttpServletResponse httpResponse) throws IOException, AIException;

    List<FileDetailBean> uploadFile(String userId, String docType, String sourceId, HttpServletRequest request, HttpServletResponse response) throws IOException, AIException;
}
