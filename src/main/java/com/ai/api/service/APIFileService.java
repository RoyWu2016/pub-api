package com.ai.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class APIFileService {
	
	@Autowired
	@Qualifier("fileService")
	private com.ai.commons.services.FileService fileService;

	public com.ai.commons.services.FileService getFileService() {
		return fileService;
	}

	public void setFileService(com.ai.commons.services.FileService fileService) {
		this.fileService = fileService;
	}
}
