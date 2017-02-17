package com.ai.api.controller.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ai.api.config.ServiceConfig;
import com.ai.api.controller.InspectorResultController;
import com.ai.api.service.APIFileService;
import com.ai.commons.HttpUtil;
import com.ai.commons.HttpUtils;
import com.ai.commons.StringUtils;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.fileservice.FileMetaBean;
import com.ai.commons.beans.fileservice.FileType;
import com.ai.commons.helpers.http.beans.ServiceResponse;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * Project Name    : Public-API
 * Package Name    : com.ai.api.controller.impl
 * Creation Date   : 2017/1/4 15:53
 * Author          : Jianxiong.Cai
 * Purpose         : TODO
 * History         : TODO
 */
@RestController
public class InspectorResultControllerImpl implements InspectorResultController {

    private static Logger logger = LoggerFactory.getLogger(InspectorResultControllerImpl.class);

    @Autowired
    @Qualifier("serviceConfig")
    private ServiceConfig config;

    @Autowired
    private APIFileService myFileService;

    @Autowired
    private ServiceConfig serviceConfig;

	final int allowedSize =  config.getMaxRequestSize();
	final int allowedSizeInMB =  allowedSize/1024/1024;

    @Override
    @RequestMapping(value = "/results/{orderId}/{reportType}", method = RequestMethod.GET)
    public ResponseEntity<ApiCallResult> searchByOrderId(@PathVariable("orderId") String orderId,@PathVariable("reportType") String reportType ) {
        ApiCallResult callResult = new ApiCallResult();
        StringBuilder url = new StringBuilder(config.getIpServiceBaseUrl())
                .append("/list-insp-result/order/").append(orderId).append("/").append(reportType);
        try {
            logger.info("searchByOrderId requesting: " + url.toString());
            ServiceCallResult result = HttpUtil.issueGetRequest(url.toString(),null);
            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
                callResult.setContent(result.getResponseString());
                return new ResponseEntity<>(callResult, HttpStatus.OK);
            }else {
                callResult.setMessage(result.getResponseString());
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            callResult.setMessage("Exception: " + e.toString());
        }
        return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @Override
    @RequestMapping(value = "/results/product/{productId}/{reportType}", method = RequestMethod.GET)
    public ResponseEntity<ApiCallResult> searchByProductId(@PathVariable("productId") String productId,@PathVariable("reportType") String reportType ) {
        ApiCallResult callResult = new ApiCallResult();
        StringBuilder url = new StringBuilder(config.getIpServiceBaseUrl())
                .append("/list-insp-result/product/").append(productId).append("/").append(reportType);
        try {
            logger.info("searchByProductId requesting: " + url.toString());
            ServiceCallResult result = HttpUtil.issueGetRequest(url.toString(),null);
            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
                callResult.setContent(result.getResponseString());
                return new ResponseEntity<>(callResult, HttpStatus.OK);
            }else {
                callResult.setMessage(result.getResponseString());
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            callResult.setMessage("Exception: " + e.toString());
        }
        return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    @RequestMapping(value = "/results/source/{sourceId}/{reportType}", method = RequestMethod.GET)
    public ResponseEntity<ApiCallResult> searchBySourceId(@PathVariable("sourceId") String sourceId,@PathVariable("reportType") String reportType ) {
        ApiCallResult callResult = new ApiCallResult();
        StringBuilder url = new StringBuilder(config.getIpServiceBaseUrl())
                .append("/list-insp-result/source-object/").append(sourceId).append("/").append(reportType);
        try {
            logger.info("searchBySourceId requesting: " + url.toString());
            ServiceCallResult result = HttpUtil.issueGetRequest(url.toString(),null);
            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
                callResult.setContent(result.getResponseString());
                return new ResponseEntity<>(callResult, HttpStatus.OK);
            }else {
                callResult.setMessage(result.getResponseString());
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            callResult.setMessage("Exception: " + e.toString());
        }
        return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    @RequestMapping(value = "/results/source/{sourceId}/{sourceType}/{reportType}", method = RequestMethod.GET)
    public ResponseEntity<ApiCallResult> searchBySourceType(@PathVariable("sourceId") String sourceId,
                                                            @PathVariable("sourceType") String sourceType,
                                                            @PathVariable("reportType") String reportType ) {
        ApiCallResult callResult = new ApiCallResult();
        StringBuilder url = new StringBuilder(config.getIpServiceBaseUrl())
                .append("/list-insp-result/source-object/").append(sourceId).append("/").append(sourceType).append("/").append(reportType);
        try {
            logger.info("searchByReportType requesting: " + url.toString());
            ServiceCallResult result = HttpUtil.issueGetRequest(url.toString(),null);
            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
                callResult.setContent(result.getResponseString());
                return new ResponseEntity<>(callResult, HttpStatus.OK);
            }else {
                callResult.setMessage(result.getResponseString());
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            callResult.setMessage("Exception: " + e.toString());
        }
        return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    @RequestMapping(value = "/supervisor/product/{productId}", method = RequestMethod.GET)
    public ResponseEntity<ApiCallResult> getSupervisorData(@PathVariable("productId") String productId) {
        ApiCallResult callResult = new ApiCallResult();
        StringBuilder url = new StringBuilder(config.getIpServiceBaseUrl())
                .append("/report-supervisor/product/").append(productId);
        try {
            logger.info("getSupervisorData  requesting: " + url.toString());
            ServiceCallResult result = HttpUtil.issueGetRequest(url.toString(),null);
            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
                callResult.setContent(result.getResponseString());
                return new ResponseEntity<>(callResult, HttpStatus.OK);
            }else {
                callResult.setMessage(result.getResponseString());
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            callResult.setMessage("Exception: " + e.toString());
        }
        return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    @RequestMapping(value = "/protocol-supervisor/product/{productId}", method = RequestMethod.GET)
    public ResponseEntity<ApiCallResult> getProtocolSupervisorData(@PathVariable("productId") String productId) {
        ApiCallResult callResult = new ApiCallResult();
        StringBuilder url = new StringBuilder(config.getIpServiceBaseUrl())
                .append("/protocol-supervisor/product/").append(productId);
        try {
            logger.info("getProtocolSupervisorData  requesting: " + url.toString());
            ServiceCallResult result = HttpUtil.issueGetRequest(url.toString(),null);
            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
                callResult.setContent(result.getResponseString());
                return new ResponseEntity<>(callResult, HttpStatus.OK);
            }else {
                callResult.setMessage(result.getResponseString());
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            callResult.setMessage("Exception: " + e.toString());
        }
        return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    @RequestMapping(value = "/results/uploadMapWithFileids/{sourceId}", method = RequestMethod.POST)
    public ResponseEntity<ApiCallResult> createMapWithFileids(@PathVariable("sourceId") String sourceId,
                                                              @RequestParam(value="username", required=false)  String username,
                                                              @RequestBody String map,HttpServletRequest request) {
        ApiCallResult callResult = new ApiCallResult();
        StringBuilder url = new StringBuilder(config.getIpServiceBaseUrl())
                .append("/results/").append(sourceId).append("/").append("uploadMapWithFileids");
        if (StringUtils.isNotBlank(username)){
            url.append("?username=").append(username);
        }
        try {
            logger.info("createMapWithFileids requesting: " + url.toString());
            ServiceResponse result = HttpUtils.postJson(url.toString(),null,map);
            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
                callResult.setContent(result.getResponseString());
                return new ResponseEntity<>(callResult, HttpStatus.OK);
            }else {
                callResult.setMessage(result.getResponseString());
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            callResult.setMessage("Exception: " + e.toString());
        }
        return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    @RequestMapping(value = "/results/updateDataWithFileids/{sourceId}", method = RequestMethod.POST)
    public ResponseEntity<ApiCallResult> approveReport(@PathVariable("sourceId") String sourceId,
                                                              @RequestParam(value="username", required=false)  String username,
                                                              @RequestBody String map,HttpServletRequest request) {
        ApiCallResult callResult = new ApiCallResult();
        StringBuilder url = new StringBuilder(config.getIpServiceBaseUrl())
                .append("/results/").append(sourceId).append("/").append("updateDataWithFileids");
        if (StringUtils.isNotBlank(username)){
            url.append("?username=").append(username);
        }
        try {
            logger.info("approveReport requesting: " + url.toString());
            ServiceResponse result = HttpUtils.postJson(url.toString(),null,map);
            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
                callResult.setContent(result.getResponseString());
                return new ResponseEntity<>(callResult, HttpStatus.OK);
            }else {
                callResult.setMessage(result.getResponseString());
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            callResult.setMessage("Exception: " + e.toString());
        }
        return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    @RequestMapping(value = "/results/protocolSupervisorData/{sourceId}", method = RequestMethod.POST)
    public ResponseEntity<ApiCallResult> saveProtocolSupervisorData(@PathVariable("sourceId") String sourceId,
                                                       @RequestParam(value="username", required=false)  String username,
                                                       @RequestBody String map,HttpServletRequest request) {
        ApiCallResult callResult = new ApiCallResult();
        StringBuilder url = new StringBuilder(config.getIpServiceBaseUrl())
                .append("/results/").append(sourceId).append("/").append("saveProtocolSupervisorData");
        if (StringUtils.isNotBlank(username)){
            url.append("?username=").append(username);
        }
        try {
            logger.info("saveProtocolSupervisorData requesting: " + url.toString());
            ServiceResponse result = HttpUtils.postJson(url.toString(),null,map);
            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
                callResult.setContent(result.getResponseString());
                return new ResponseEntity<>(callResult, HttpStatus.OK);
            }else {
                callResult.setMessage(result.getResponseString());
            }
        } catch (Exception e) {
            logger.error(ExceptionUtils.getStackTrace(e));
            callResult.setMessage("Exception: " + e.toString());
        }
        return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    @RequestMapping(value = "/results/file-upload/{sourceId}", method = RequestMethod.POST)
    public ResponseEntity<ApiCallResult> uploadFile(@PathVariable("sourceId") String sourceId,
                                                    @RequestParam(value="username", required=false) String username,
                                                    MultipartHttpServletRequest request) {
        ApiCallResult callResult = new ApiCallResult();
	    int reqSize = request.getContentLength();
	    if (reqSize > allowedSize) {
		    logger.error("Request size " + reqSize + "large than "
				    + allowedSize + ", " +allowedSizeInMB + " MB.");
		    callResult.setMessage("Uploading report data are larger than " + allowedSizeInMB + " MB.");
		    return new ResponseEntity<>(callResult, HttpStatus.PAYLOAD_TOO_LARGE);
	    } else {
		    logger.error("Request size " + reqSize + " is ok: ");
	    }

        try{
            logger.info("ready to uploadFile ...");
            Map<String,List<FileMetaBean>> fileMetaList = new HashMap<>();
            Map<String, List> mapFileList = new HashMap<>();
            Iterator<String> itr = request.getFileNames();
            while(itr.hasNext()) {
                MultipartFile mpf = request.getFile(itr.next());
                Map map = new HashMap<>();
                String keyName = mpf.getName();
                logger.info("keyName:"+keyName);
                String caption = request.getParameter(keyName);
                String[] key = keyName.split(":");
                List list2 = new ArrayList<>();
                map.put("multipartFile", mpf);
                map.put("caption", caption);
                if(mapFileList.get(key[0]) == null ){
                    list2.add(map);
                    mapFileList.put(key[0], list2);
                }else {
                    mapFileList.get(key[0]).add(map);
                }
            }
            logger.info("mapFileList size :"+mapFileList.size());

            for (Map.Entry<String, List> map : mapFileList.entrySet())
            {
                logger.info("get each mapFile " + map.getKey()+" -- "+map.getValue());
                List<FileMetaBean> beanList = new ArrayList<>();
                List<File>  toBeDeleted = new ArrayList<>();
                for(Object mpf : map.getValue()){
                    MultipartFile _mpf = ((CommonsMultipartFile)((Map)mpf).get("multipartFile"));
                    String caption = ((Map)mpf).get("caption").toString();
                    String[] keys = _mpf.getName().split(":")[0].split("@");
                    logger.info("get MultipartFile "+_mpf.getName()+" || caption:"+caption+" || keys:"+keys);
                    String fileType = FileType.INSP_RESULT_FILE.getType();
                    if(keys[1].equals("Upload_Product_Image")){
                        fileType = FileType.PROD_REPORT_PICTURE.getType();
                    }
                    double sizeM = _mpf.getSize() / (1024 * 1000);
                    if (sizeM > serviceConfig.getFileMaximumSize()) {
                        callResult.setMessage("Max of file size is :"+serviceConfig.getFileMaximumSize()+"M");
                        return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
                    }else {
                        File tempDir = new File(myFileService.getFileService().getLocalTempDir() + sourceId);
                        if (!tempDir.exists()) {
                            tempDir.mkdir();
                        }
                        logger.info("ready to copy file to api temp ...");
                        logger.info(_mpf.getName()+" || "+tempDir.getAbsolutePath());
                        String filePath = com.ai.commons.FileUtils.copyFileToDirectory(_mpf, tempDir);
                        File uploadedFile=new File(tempDir + System.getProperty("file.separator") + filePath);
                        logger.info("uploaded to API temp! ["+uploadedFile.getAbsolutePath()+"]");
//                        SimpleFileObject fileUploadedObject = new SimpleFileObject(uploadedFile);
                        FileMetaBean ftb = myFileService.getFileService().upload(sourceId, fileType, "insp-result-file", username, caption, uploadedFile);
                        toBeDeleted.add(uploadedFile);
                        beanList.add(ftb);
                    }
                }
                logger.info("file uploaded to FILE_SERVICE !");
                logger.info("tobeDeleted size :"+toBeDeleted.size());
                fileMetaList.put(map.getKey(), beanList);
                toBeDeleted.stream().filter(File::exists).forEach(File::delete);
                logger.info("delete done!");
            }
            callResult.setContent(fileMetaList);
            return new ResponseEntity<>(callResult,HttpStatus.OK);
        }catch(Exception e){
            logger.error("Error Exception!",e);
            callResult.setMessage("Error Exception! "+e);
        }
        return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @Override
    @RequestMapping(value = "/results/file/{fileIds}", method = RequestMethod.GET)
    public @ResponseBody void getFile(@PathVariable("fileIds") String fileIds,HttpServletResponse response) throws IOException {
        try {
            logger.info("getFile - fileIds : " + fileIds);
            InputStream is = myFileService.getFileService().getFile(fileIds);
            FileMetaBean fileDetails = myFileService.getFileService().getFileInfoById(fileIds);
            String fileName = fileDetails.getFileName();
            String fileType = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
            if(fileType.equalsIgnoreCase("gif")){
                response.setHeader("Content-Type", "image/gif");
            }else if(fileType.equalsIgnoreCase("png")){
                response.setHeader("Content-Type", "image/png");
            }else if(fileType.equalsIgnoreCase("xls")){
                response.setHeader("Content-Type", "application/vnd.ms-excel");
            }else if(fileType.equalsIgnoreCase("xlsx")){
                response.setHeader("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            }else {
                response.setHeader("Content-Type", "image/jpeg");
            }
            response.setHeader("X-File-Name",fileName);
            response.setHeader("X-File-Caption",fileDetails.getComments()+" ");
            response.setHeader("Access-Control-Expose-Headers","X-File-Name, X-File-Caption");
            FileCopyUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            logger.error("Failed to getFile. Error Exception! " ,e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,e.toString());
            throw new IOException(e);
        }
    }

    @Override
    @RequestMapping(value = "/results/download/{fileIds}", method = RequestMethod.GET)
    public void downloadFileV1(@PathVariable("fileIds") String fileIds,HttpServletResponse response) throws IOException  {
        try {
            logger.info("downloadFileV1 - fileIds : " + fileIds);
            InputStream is = myFileService.getFileService().getFile(fileIds);
            FileMetaBean fileDetails = myFileService.getFileService().getFileInfoById(fileIds);
            String fileName = fileDetails.getFileName();
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName );
            FileCopyUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            logger.error("Failed to downloadFileV1. Error Exception! " ,e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,e.toString());
            throw new IOException(e);
        }
    }

    @Override
    @RequestMapping(value = "/results/file/{fileId}", method = RequestMethod.DELETE)
    public ResponseEntity<ApiCallResult> deleteFile(@PathVariable("fileId") String fileId,@RequestParam("userName") String userName) {
        ApiCallResult callResult = new ApiCallResult();
        try {
            logger.info("deleteFile - fileId : " + fileId);
            boolean b = myFileService.getFileService().deleteFile(fileId,userName);
            callResult.setContent(b);
            return new ResponseEntity<>(callResult,HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Failed to deleteFile. Error Exception! " ,e);
            callResult.setMessage("Failed to deleteFile. Error Exception! "+e.toString());
        }
        return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    @RequestMapping(value = "/results/fileInfo/{fileIds}", method = RequestMethod.GET)
    public ResponseEntity<ApiCallResult> getFileInfo(@PathVariable("fileIds") String fileIds) {
        ApiCallResult callResult = new ApiCallResult();
        try {
            logger.info("getFileInfo - fileIds : " + fileIds);
            FileMetaBean result = myFileService.getFileService().getFileInfoById(fileIds);
            String caption = result.getComments();
            callResult.setContent(caption);
            return new ResponseEntity<>(callResult,HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Failed to getFileInfo. Error Exception! " ,e);
            callResult.setMessage("Failed to getFileInfo. Error Exception! "+e.toString());
        }
        return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    @RequestMapping(value = "/results/fileName/{fileIds}", method = RequestMethod.GET)
    public ResponseEntity<ApiCallResult> getFileName(@PathVariable("fileIds") String fileIds) {
        ApiCallResult callResult = new ApiCallResult();
        try {
            logger.info("getFileName - fileIds : " + fileIds);
            FileMetaBean result = myFileService.getFileService().getFileInfoById(fileIds);
            String caption = result.getFileName();
            callResult.setContent(caption);
            return new ResponseEntity<>(callResult,HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Failed to get FileName.Error Exception! " ,e);
            callResult.setMessage("Failed to get FileName.Error Exception! "+e.toString());
        }
        return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
    }


//    @Override
//    @RequestMapping(value = "/results/{productId}", method = RequestMethod.POST)
//    public ResponseEntity<ApiCallResult> saveResult(@PathVariable("productId") String productId,
//                                                    @RequestBody InspResultForm aiResultForm,
//                                                    @RequestParam(value="username", required=false)  String username) {
//        ApiCallResult callResult = new ApiCallResult();
////        StringBuilder url = new StringBuilder("http://127.0.0.1:8888")
//        StringBuilder url = new StringBuilder(config.getIpServiceBaseUrl())
//                .append("/results/save/").append(productId);
//        if (StringUtils.isNotBlank(username)){
//            url.append("?username=").append(username);
//        }
//        try {
//            logger.info("saveResult requesting: " + url.toString());
//            ServiceCallResult result = HttpUtil.issuePostRequest(url.toString(),null,aiResultForm);
//            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
//                callResult.setContent(result.getResponseString());
//                return new ResponseEntity<>(callResult, HttpStatus.OK);
//            }else {
//                callResult.setMessage(result.getResponseString());
//            }
//        } catch (Exception e) {
//            logger.error(ExceptionUtils.getStackTrace(e));
//            callResult.setMessage("Exception: " + e.toString());
//        }
//        return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    @Override
//    @RequestMapping(value = "/results/{productId}", method = RequestMethod.PUT)
//    public ResponseEntity<ApiCallResult> updateResult(@PathVariable("productId") String productId,
//                                                    @RequestBody InspResultForm aiResultForm,
//                                                    @RequestParam(value="username", required=false)  String username) {
//        ApiCallResult callResult = new ApiCallResult();
//        StringBuilder url = new StringBuilder(config.getIpServiceBaseUrl())
//                .append("/results/save/").append(productId);
//        if (StringUtils.isNotBlank(username)){
//            url.append("?username=").append(username);
//        }
//        try {
//            logger.info("updateResult requesting: " + url.toString());
//            ServiceCallResult result = HttpUtil.issuePutRequest(url.toString(),null,aiResultForm);
//            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
//                callResult.setContent(result.getResponseString());
//                return new ResponseEntity<>(callResult, HttpStatus.OK);
//            }else {
//                callResult.setMessage(result.getResponseString());
//            }
//        } catch (Exception e) {
//            logger.error(ExceptionUtils.getStackTrace(e));
//            callResult.setMessage("Exception: " + e.toString());
//        }
//        return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    @Override
//    @RequestMapping(value = "/results/userData/{productId}", method = RequestMethod.POST)
//    public ResponseEntity<ApiCallResult> saveUserData(@PathVariable("productId") String productId,
//                                                    @RequestBody InspResultForm aiResultForm,
//                                                    @RequestParam(value="username", required=false)  String username) {
//        ApiCallResult callResult = new ApiCallResult();
//        StringBuilder url = new StringBuilder(config.getIpServiceBaseUrl())
//                .append("/results/saveUserData/").append(productId);
//        if (StringUtils.isNotBlank(username)){
//            url.append("?username=").append(username);
//        }
//        try {
//            logger.info("saveUserData requesting: " + url.toString());
//            ServiceCallResult result = HttpUtil.issuePostRequest(url.toString(),null,aiResultForm);
//            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
//                callResult.setContent(result.getResponseString());
//                return new ResponseEntity<>(callResult, HttpStatus.OK);
//            }else {
//                callResult.setMessage(result.getResponseString());
//            }
//        } catch (Exception e) {
//            logger.error(ExceptionUtils.getStackTrace(e));
//            callResult.setMessage("Exception: " + e.toString());
//        }
//        return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    @Override
//    @RequestMapping(value = "/results/product/{productId}", method = RequestMethod.GET)
//    public ResponseEntity<ApiCallResult> searchByProductId(@PathVariable("productId") String productId) {
//        ApiCallResult callResult = new ApiCallResult();
//        StringBuilder url = new StringBuilder(config.getIpServiceBaseUrl())
//                .append("/map-list-insp-result/product/").append(productId);
//        try {
//            logger.info("searchByProductId  requesting: " + url.toString());
//            ServiceCallResult result = HttpUtil.issueGetRequest(url.toString(),null);
//            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
//                callResult.setContent(result.getResponseString());
//                return new ResponseEntity<>(callResult, HttpStatus.OK);
//            }else {
//                callResult.setMessage(result.getResponseString());
//            }
//        } catch (Exception e) {
//            logger.error(ExceptionUtils.getStackTrace(e));
//            callResult.setMessage("Exception: " + e.toString());
//        }
//        return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    @Override
//    @RequestMapping(value = "/supervisor/product/{productId}", method = RequestMethod.GET)
//    public ResponseEntity<ApiCallResult> getAllIpSupervisorData(@PathVariable("productId") String productId) {
//        ApiCallResult callResult = new ApiCallResult();
//        StringBuilder url = new StringBuilder(config.getIpServiceBaseUrl())
//                .append("/map-list-ipsupervisor-result/product/").append(productId);
//        try {
//            logger.info("getAllIpSupervisorData  requesting: " + url.toString());
//            ServiceCallResult result = HttpUtil.issueGetRequest(url.toString(),null);
//            if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
//                callResult.setContent(result.getResponseString());
//                return new ResponseEntity<>(callResult, HttpStatus.OK);
//            }else {
//                callResult.setMessage(result.getResponseString());
//            }
//        } catch (Exception e) {
//            logger.error(ExceptionUtils.getStackTrace(e));
//            callResult.setMessage("Exception: " + e.toString());
//        }
//        return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
//    }

//    @Override
//    @RequestMapping(value = "/results/file-uploads/{sourceId}", method = RequestMethod.POST)
//    public ResponseEntity<ApiCallResult> uploadFileWithCaption(@PathVariable("sourceId") String sourceId,
//                                                               @RequestParam(value="username", required=false) String username,
//                                                               @RequestBody Map<String,List<offlineVM>> map) {
//        ApiCallResult callResult = new ApiCallResult();
//        List<File> tobeDeleted = new ArrayList<File>();
//        Map<String, List<FileMetaBean>> fileMetaList = new HashMap<String, List<FileMetaBean>>();
//        try {
//            for (String key : map.keySet()) {
//                String[] keys = key.split("@");
//                String fileType = "";
//                if (keys[1].equals("Upload_Product_Image")) {
//                    fileType = FileType.PROD_REPORT_PICTURE.getType();
//                } else {
//                    fileType = FileType.INSP_RESULT_FILE.getType();
//                }
//                List<offlineVM> value = map.get(key);
//                if (value != null) {
//                    List<FileMetaBean> beanList = new ArrayList<FileMetaBean>();
//                    for (offlineVM element : value) {
//                        String[] base64 = element.getData().split(",");
//                        String sourceData = base64[1];
//
//                        try {
//                            byte[] imageByteArray = decodeImage(sourceData);
//                            File tempDir = new File(myFileService.getFileService().getLocalTempDir() + sourceId);
//                            if (!tempDir.exists()) {
//                                tempDir.mkdir();
//                            }
//                            String filePath = element.getFilename();
//                            FileOutputStream imageOutFile = new FileOutputStream(tempDir + "/" + filePath);
//                            File files = new File(tempDir + "/" + filePath);
//                            imageOutFile.write(imageByteArray);
//                            imageOutFile.close();
//                            tobeDeleted.add(files);
//                            FileMetaBean ftb = myFileService.getFileService().upload(sourceId, fileType, "insp-result-file", username, element.getCaption(), files);
//                            beanList.add(ftb);
//                        } catch (FileNotFoundException e) {
//                            logger.error("Uploading Failed  e " + e);
//                        } catch (IOException ioe) {
//                            logger.error("Uploading Failed  ioe " + ioe);
//                        }
//                    }
//                    fileMetaList.put(key, beanList);
//                    for (File f : tobeDeleted) {
//                        if (f.exists()) {
//                            f.delete();
//                        }
//                    }
//                }
//            }
//            callResult.setContent(fileMetaList);
//            return new ResponseEntity<>(callResult,HttpStatus.OK);
//        }catch (Exception e){
//            logger.error("Error exception!",e);
//            callResult.setMessage("Error exception!"+e);
//        }
//        return new ResponseEntity<>(callResult,HttpStatus.INTERNAL_SERVER_ERROR);
//    }




//    @JsonIgnoreProperties(ignoreUnknown = true)
//    public static class offlineVM implements Serializable {
//
//        private static final long serialVersionUID = 511137310600169111L;
//
//        private String id;
//        private String caption;
//        private String data;
//        private String filename;
//
//        public offlineVM() {
//        }
//        public String getId() {
//            return id;
//        }
//        public void setId(String id) {
//            this.id = id;
//        }
//        public String getCaption() {
//            return caption;
//        }
//        public void setCaption(String caption) {
//            this.caption = caption;
//        }
//        public String getData() {
//            return data;
//        }
//        public void setData(String data) {
//            this.data = data;
//        }
//        public String getFilename() {
//            return filename;
//        }
//        public void setFilename(String filename) {
//            this.filename = filename;
//        }
//
//        @Override
//        public String toString() {
//            return "InspResultForm [id=" + id + ", caption="
//                    + caption + ", data=" + data + ", filename=" + filename + "]";
//        }
//
//
//    }

//    private static byte[] decodeImage(String imageDataString) {
//        return org.apache.commons.codec.binary.Base64.decodeBase64(imageDataString);
//    }
//
//    private static String encodeImage(byte[] imageByteArray) {
//        return Base64.encodeBase64URLSafeString(imageByteArray);
//    }
}
