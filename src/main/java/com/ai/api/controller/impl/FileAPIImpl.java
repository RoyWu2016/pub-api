package com.ai.api.controller.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ai.api.bean.consts.ConstMap;
import com.ai.api.config.ServiceConfig;
import com.ai.api.controller.FileAPI;
import com.ai.api.exception.AIException;
import com.ai.api.service.APIFileService;
import com.ai.api.service.UserService;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.fileservice.FileMetaBean;
import com.ai.commons.beans.order.SimpleOrderSearchBean;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/***************************************************************************
 * <PRE>
 * Project Name    : api
 * <p/>
 * Package Name    : com.ai.api.controller.impl
 * <p/>
 * Creation Date   : 2016/6/30 12:20
 * <p/>
 * Author          : Jianxiong Cai
 * <p/>
 * Purpose         : TODO
 * <p/>
 * <p/>
 * History         : TODO
 * <p/>
 * </PRE>
 ***************************************************************************/

@RestController
@Api(tags = {"File"}, description = "File Operation APIs")
public class FileAPIImpl implements FileAPI {

	private static final Logger logger = LoggerFactory.getLogger(FileAPIImpl.class);

	@Autowired
	private APIFileService myFileService;

	@Autowired
	private ServiceConfig serviceConfig;

	@Autowired
	UserService userService; // Service which will do all data

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/file/{fileId}/detail", method = RequestMethod.GET)
	@ApiOperation(value = "Get File Detail Info API", response = FileMetaBean.class)
	public ResponseEntity<FileMetaBean> getFileDetailInfo(
			@ApiParam(value = "userId", required = true)
			@PathVariable("userId") String userId,
			@ApiParam(value = "fileId", required = true)
			@PathVariable("fileId") String fileId) throws IOException, AIException {
		FileMetaBean fileInfo = null;
		try {
			fileInfo = myFileService.getFileService().getFileInfoById(fileId);
		} catch (Exception e) {
			logger.error("ERROR! from[getFileDetailInfo]:", e);
		}

		if (null == fileInfo) {
			logger.info("Null result!from[getFileDetailInfo]");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(fileInfo, HttpStatus.OK);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/file/{fileId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get File API", response = String.class)
	public boolean getFile(
			@ApiParam(value = "userId", required = true)
			@PathVariable("userId") String userId,
			@ApiParam(value = "fileId", required = true)
			@PathVariable("fileId") String fileId,
			HttpServletRequest request, HttpServletResponse response) throws IOException {

		FileMetaBean fileMetaBean = myFileService.getFileService().getFileInfoById(fileId);
		response.setHeader("Content-Disposition", fileMetaBean.getFileName());
		InputStream inputStream = myFileService.getFileService().getFile(fileId);
		ServletOutputStream output = response.getOutputStream();
		response.setStatus(HttpServletResponse.SC_OK);
		byte[] buffer = new byte[10240];
		for (int length = 0; (length = inputStream.read(buffer)) > 0;) {
			output.write(buffer, 0, length);
		}
		return true;
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/file/{fileId}/base64", method = RequestMethod.GET)
	@ApiOperation(value = "Get File Base64 API", response = String.class,responseContainer = "Map")
	public ResponseEntity<Map<String, String>> getFileBase64(
			@ApiParam(value = "userId", required = true)
			@PathVariable("userId") String userId,
			@ApiParam(value = "fileId", required = true)
			@PathVariable("fileId") String fileId) throws IOException {
		Map<String, String> result = new HashMap<String, String>();
		InputStream inputStream = myFileService.getFileService().getFile(fileId);
		String fileStr = null;
		if (inputStream != null) {
			byte[] data = IOUtils.toByteArray(inputStream);
			fileStr = Base64.encode(data);
		} else {
			fileStr = "";
		}
		result.put("base64", fileStr);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/doc-type/{docType}/source/{sourceId}/file", method = RequestMethod.POST)
	@ApiOperation(value = "Upload File API", response = FileMetaBean.class)
	public ResponseEntity<FileMetaBean> uploadFile(
			@ApiParam(value = "userId", required = true)
			@PathVariable("userId") String userId,
			@ApiParam(value = "docType", required = true)
			@PathVariable("docType") String docType,
			@ApiParam(value = "sourceId", required = true)
			@PathVariable("sourceId") String sourceId,
			MultipartHttpServletRequest request, HttpServletResponse response) throws IOException {

		String bucket = ConstMap.bucketMap.get(docType.toUpperCase());
		FileMetaBean bean = new FileMetaBean();

		String username = userService.getLoginByUserId(userId);

		try {
			Iterator<String> itr = request.getFileNames();
			MultipartFile mpf = null;

			// Iterate through the uploaded files
			File tempDir = null;
			while (itr.hasNext()) {
				mpf = request.getFile(itr.next());
				double sizeM = mpf.getSize() / (1024 * 1000);
				// 10M default
				if (sizeM > serviceConfig.getFileMaximumSize()) {
					return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				} else {
					tempDir = new File(myFileService.getFileService().getLocalTempDir() + File.separator + sourceId);

					if (!tempDir.exists()) {
						tempDir.mkdir();
					}
					
					String filePath = com.ai.commons.FileUtils.copyFileToDirectory(mpf, tempDir);
					File fileUploded = new File(tempDir + System.getProperty("file.separator") + filePath);

					if (docType.equalsIgnoreCase("LT_BOOKING")){
						bean = myFileService.getFileService().upload(sourceId, request.getContentType(), serviceConfig.getAimsBucket(), userId, fileUploded);
					}else {
						bean = myFileService.getFileService().upload(sourceId, docType, bucket, username, fileUploded);
					}
					if (fileUploded.exists()) {
						fileUploded.delete();
					}

				}
				if(null!= tempDir && tempDir.exists()) {
					tempDir.delete();
				}
			}
		} catch (Exception e) {
			logger.error("Error in update", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<FileMetaBean>(bean, HttpStatus.OK);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/file/{fileId}", method = RequestMethod.DELETE)
	@ApiOperation(value = "Delete File API", response = String.class)
	public ResponseEntity<String> deleteFile(
			@ApiParam(value = "userId", required = true)
			@PathVariable("userId") String userId,
			@ApiParam(value = "fileId", required = true)
			@PathVariable("fileId") String fileId) throws IOException {
		try {
			myFileService.getFileService().deleteFile(fileId, userId);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error in update", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/files/{srcId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get File List API", response = FileMetaBean.class,responseContainer = "List")
	public ResponseEntity<List<FileMetaBean>> getFilesList(
			@ApiParam(value = "userId", required = true)
			@PathVariable("userId") String userId,
			@ApiParam(value = "srcId", required = true)
			@PathVariable("srcId") String srcId, @RequestParam(value = "docType", required = false) String docType)
			throws IOException {
		// TODO Auto-generated method stub
		List<FileMetaBean> result = null;
		try {
			result = myFileService.getFileService().getFileInfoBySrcIdAndFileType(srcId, docType);
			if (null != result) {
				return new ResponseEntity<>(result, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			logger.error("Error in getFilesList", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/copy-files", method = RequestMethod.GET)
	@ApiOperation(value = "Copy Files API", response = FileMetaBean.class,responseContainer = "List")
	public ResponseEntity<ApiCallResult> copyFiles(
			@ApiParam(value = "userId", required = true)
			@PathVariable("userId") String userId,
			@ApiParam(value = "if multiple,separated by semicolon", required = true) @RequestParam(value = "fileIds") String fileIds,
			@RequestParam(value = "newSrcId") String newSrcId,
			@RequestParam(value = "userName", required = false, defaultValue = "") String userName) throws IOException {
		// TODO Auto-generated method stub
		logger.info("invoke: " + "/user/" + userId + "/copy-files?newSrcId=" + newSrcId + "&userName=" + userName
				+ "&fileIds" + fileIds);
		ApiCallResult finalResult = new ApiCallResult();
		List<FileMetaBean> result = null;
		try {
			result = myFileService.getFileService().copyFileLinkByIds(fileIds, newSrcId, userName);
			if (null != result) {
				finalResult.setContent(result);
				return new ResponseEntity<>(finalResult, HttpStatus.OK);
			} else {
				finalResult.setMessage("Copy files from file-service get null");
				return new ResponseEntity<>(finalResult, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			logger.error("Error in getFilesList", e);
			finalResult.setMessage("Exception: " + e.toString());
			return new ResponseEntity<>(finalResult, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
