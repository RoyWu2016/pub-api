package com.ai.api.controller.impl;

import java.io.IOException;

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
	@RequestMapping(value = "/user/{userId}/file/{fileId}", method = RequestMethod.GET)
	public void downloadFile(@PathVariable("userId") String userId, @PathVariable("fileId") String fileId,
	                         HttpServletResponse httpResponse) {
		try {
			boolean b = fileService.downloadFile(userId, fileId, httpResponse);
			logger.info("success!");
		} catch (Exception e) {
			logger.error("ERROR! from[downloadFile]:", e);
		}
	}
}
