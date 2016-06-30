package com.ai.api.service;

import com.ai.api.bean.FileDetailBean;
import com.ai.api.exception.AIException;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;

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

    byte[] downloadFile(String userId, String fileId);
}
