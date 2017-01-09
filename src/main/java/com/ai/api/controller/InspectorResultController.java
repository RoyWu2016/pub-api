package com.ai.api.controller;

import com.ai.api.controller.impl.InspectorResultControllerImpl;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.psi.InspResultForm;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Project Name    : Public-API
 * Package Name    : com.ai.api.controller
 * Creation Date   : 2017/1/4 15:20
 * Author          : Jianxiong.Cai
 * Purpose         : TODO
 * History         : TODO
 */
public interface InspectorResultController {
    ResponseEntity<ApiCallResult> saveResult(String productId, InspResultForm aiResultForm,String username);
    ResponseEntity<ApiCallResult> updateResult(String productId, InspResultForm aiResultForm,String username);
    ResponseEntity<ApiCallResult> saveUserData(String productId, InspResultForm aiResultForm,String username);
    ResponseEntity<ApiCallResult> searchByOrderId(String orderId, String reportType);
    ResponseEntity<ApiCallResult> searchByProductId(String productId, String reportType);
    ResponseEntity<ApiCallResult> searchByProductId(String productId);
    ResponseEntity<ApiCallResult> searchBySourceId(String sourceId, String reportType);
    ResponseEntity<ApiCallResult> searchBySourceType(String sourceId, String sourceType, String reportType);
    ResponseEntity<ApiCallResult> getAllIpSupervisorData(String productId);
    ResponseEntity<ApiCallResult> uploadFile(String sourceId,String username,MultipartHttpServletRequest request);
    ResponseEntity<ApiCallResult> uploadFileWithCaption(String sourceId, String username, Map<String,List<InspectorResultControllerImpl.offlineVM>> map);
    void getFile(String fileIds,HttpServletResponse response) throws IOException;
    void downloadFileV1(String fileIds,HttpServletResponse response) throws IOException;
    ResponseEntity<ApiCallResult> deleteFile(String fileId,String userName);
    ResponseEntity<ApiCallResult> getFileInfo(String fileIds);
    ResponseEntity<ApiCallResult> getFileName(String fileIds);
}
