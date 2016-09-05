package com.ai.api.controller.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.ai.api.bean.consts.ConstMap;
import com.ai.api.config.ServiceConfig;
import com.ai.api.controller.FileAPI;
import com.ai.api.exception.AIException;
import com.ai.api.service.APIFileService;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.fileservice.FileMetaBean;
import com.ai.dto.JsonResponse;

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
public class FileAPIImpl implements FileAPI {

	private static final Logger logger = LoggerFactory.getLogger(FileAPIImpl.class);

	@Autowired
	private APIFileService myFileService;
	
	@Autowired
	private ServiceConfig serviceConfig;



	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/file/{fileId}/detail", method = RequestMethod.GET)
	public ResponseEntity<FileMetaBean> getFileDetailInfo(@PathVariable("userId") String userId,
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
	public boolean getFile(@PathVariable("userId") String userId, @PathVariable("fileId") String fileId,
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
	@RequestMapping(value = "/user/{userId}/doc-type/{docType}/source/{sourceId}/file", method = RequestMethod.POST)
	public ResponseEntity<FileMetaBean> uploadFile(@PathVariable("userId") String userId,
			@PathVariable("docType") String docType, @PathVariable("sourceId") String sourceId,
			MultipartHttpServletRequest request, HttpServletResponse response) throws IOException {
		JsonResponse jsonRes = new JsonResponse(JsonResponse.STATUS_SUC);
		String bucket = ConstMap.bucketMap.get(docType.toUpperCase());
		FileMetaBean bean = new FileMetaBean();
		try {
			Iterator<String> itr = request.getFileNames();
			MultipartFile mpf = null;

			// Iterate through the uploaded files
			while (itr.hasNext()) {
				mpf = request.getFile(itr.next());
				double sizeM = mpf.getSize() / (1024 * 1000);
				// 10M default
				if (sizeM > serviceConfig.getFileMaximumSize()) {
					jsonRes.setStatus(JsonResponse.STATUS_FAIL);
					jsonRes.setMsg("Max of file size is " + serviceConfig.getFileMaximumSize() + "M");
				} else {
					File tempDir = new File(myFileService.getFileService().getLocalTempDir() + sourceId);

					if (!tempDir.exists()) {
						tempDir.mkdir();
					}
					String filePath = com.ai.commons.FileUtils.copyFileToDirectory(mpf, tempDir);
					File fileUploded = new File(tempDir + System.getProperty("file.separator") + filePath);

					bean = myFileService.getFileService().upload(sourceId, docType, bucket, userId, fileUploded);
					if (fileUploded.exists()) {
						fileUploded.delete();
					}

				}
			}
		} catch (Exception e) {
			logger.error("Error in update", e);
			jsonRes.setStatus(JsonResponse.STATUS_FAIL);
			jsonRes.setMsg("'File attached failed, please contact the IT department! >> Error: " + e.getMessage());
		}
		return new ResponseEntity<FileMetaBean>(bean, HttpStatus.OK);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/file/{fileId}/delete", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteFile(@PathVariable("userId") String userId,
			@PathVariable("fileId") String fileId) throws IOException {
		try {
			myFileService.getFileService().deleteFile(fileId, userId);
			return new ResponseEntity<String>("Delete Successfully", HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error in update", e);
			return new ResponseEntity<String>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
