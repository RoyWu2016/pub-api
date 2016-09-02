package com.ai.api.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ai.api.exception.AIException;
import com.ai.commons.beans.fileservice.FileMetaBean;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartHttpServletRequest;

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

public interface FileController {

    ResponseEntity<FileMetaBean> getFileDetailInfo(String userId,String fileId) throws IOException, AIException;

    boolean  getFile(String userId,String fileId,HttpServletRequest request, HttpServletResponse httpResponse) throws IOException;

    ResponseEntity<FileMetaBean> uploadFile(String userId, String docType, String sourceId, MultipartHttpServletRequest request, HttpServletResponse response)throws IOException;

    ResponseEntity<String> deleteFile(String userId,String fileId) throws IOException;

}