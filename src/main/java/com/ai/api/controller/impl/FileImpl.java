package com.ai.api.controller.impl;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ai.api.bean.FileDetailBean;
import com.ai.api.controller.File;
import com.ai.api.exception.AIException;
import com.ai.api.service.FileService;
import com.ai.commons.annotation.TokenSecured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
public class FileImpl implements File {

	private static final Logger logger = LoggerFactory.getLogger(FileImpl.class);

	@Autowired
	private FileService fileService;

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/file/{fileId}/detail", method = RequestMethod.GET)
	public ResponseEntity<FileDetailBean> getFileDetailInfo(@PathVariable("userId") String userId,
	                                                        @PathVariable("fileId") String fileId)
			throws IOException, AIException {
		FileDetailBean fileDetailBean = null;
		try {
			fileDetailBean = fileService.getFileDetailInfo(userId, fileId);
		} catch (Exception e) {
			logger.error("ERROR! from[getFileDetailInfo]:", e);
		}

		if (null == fileDetailBean) {
			logger.info("Null result!from[getFileDetailInfo]");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(fileDetailBean, HttpStatus.OK);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/file/{fileId}", method = RequestMethod.GET)
	public void getFile(@PathVariable("userId") String userId, @PathVariable("fileId") String fileId,
	                         HttpServletResponse httpResponse) {
		try {
			boolean b = fileService.downloadFile(userId, fileId, httpResponse);
			logger.info("success!");
		} catch (Exception e) {
			logger.error("ERROR! from[downloadFile]:", e);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/docType/{docType}/source/{sourceId}/file", method = RequestMethod.POST)
	public ResponseEntity<List<FileDetailBean>> uploadFile(@PathVariable("userId") String userId,
									 @PathVariable("docType") String docType,
									 @PathVariable("sourceId") String sourceId,
									 HttpServletRequest request, HttpServletResponse response) {
		List<FileDetailBean> fileDetailBeans = null;
		try {
			fileDetailBeans = fileService.uploadFile(userId,docType,sourceId,request,response);
		}catch (Exception e){
			logger.error("ERROR! from[uploadFile]",e);
		}
		if (null == fileDetailBeans||fileDetailBeans.size()==0) {
			logger.info("Null result!from[uploadFile]");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(fileDetailBeans, HttpStatus.OK);
	}
}
