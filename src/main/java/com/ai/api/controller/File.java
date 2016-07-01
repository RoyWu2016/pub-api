package com.ai.api.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.ai.api.bean.FileDetailBean;
import com.ai.api.exception.AIException;
import org.springframework.http.ResponseEntity;

/***************************************************************************
 * <PRE>
 * Project Name    : api
 * <p>
 * Package Name    : com.ai.api.controller
 * <p>
 * Creation Date   : 2016/6/30 12:15
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

public interface File {
    ResponseEntity<FileDetailBean> getFileDetailInfo(String userId,String fileId) throws IOException, AIException;

    void getFile(String userId,String fileId,HttpServletResponse httpResponse);
}
