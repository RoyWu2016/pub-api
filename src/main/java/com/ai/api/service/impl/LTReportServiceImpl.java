package com.ai.api.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.ai.aims.constants.OrderStatus;
import com.ai.aims.services.model.OrderAttachment;
import com.ai.aims.services.model.OrderMaster;
import com.ai.api.bean.OrderSearchBean;
import com.ai.api.bean.UserBean;
import com.ai.api.dao.LTOrderDao;
import com.ai.api.exception.AIException;
import com.ai.api.service.APIFileService;
import com.ai.api.service.LTReportService;
import com.ai.api.service.UserService;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.fileservice.FileMetaBean;

@SuppressWarnings("rawtypes")
@Service
@Qualifier("ltreportservice")
public class LTReportServiceImpl implements LTReportService {

	protected Logger logger = LoggerFactory.getLogger(LTOrderServiceImpl.class);

	@Autowired
	@Qualifier("ltorderDao")
	private LTOrderDao ltOrderDao;

	@Autowired
	@Qualifier("userService")
	private UserService userService;
	
	@Autowired
	private APIFileService apiFileService;

	@Override
	public List<OrderSearchBean> findReports(String userId, Integer pageNumber,
			Integer pageSize) throws IOException, AIException {
		String companyId = "";
		String parentId = "";
		UserBean user = userService.getCustById(userId);
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null)
				parentId = "";
			companyId = user.getCompany().getId();
		}
		if (null==companyId){
		    logger.info("use incorrect userId["+userId+"] to search LT orders");
			throw new AIException("incorrect userId");
		}
		return ltOrderDao.searchLTOrders(companyId, OrderStatus.COMPLETED_CODE, pageNumber, pageSize, Sort.Direction.DESC.name().toLowerCase());
	}

	@Override
	public OrderMaster findReport(String reportId) throws IOException {
		OrderMaster order = ltOrderDao.findOrder(reportId);
		if (null != order.getAttachments()) {
			// SET REPORT ATTACHMENTS
			Set<OrderAttachment> attachments = order.getAttachments();
			OrderAttachment attachment = attachments.parallelStream()
					.filter(a -> "Client report".equalsIgnoreCase(a.getLabel())).findFirst().get();
			attachments.clear();
			attachments.add(attachment);
			order.setAttachments(attachments);
		}
		return order;
	}
	
	@Override
	public ApiCallResult editReportStatus(String userId, String orderId, String status) throws IOException {
		OrderMaster order = new OrderMaster(orderId);
		order.setClientStatus(status);
		return ltOrderDao.editOrderStatus(userId, order);
	}
	
	@Override
	public ApiCallResult editReportTestStatus(String userId, String orderId, String testId, String status) throws IOException {
		return ltOrderDao.editOrderTestAssignmentStatus(orderId, testId, userId, status);
	}
	
	@Override
	public void downloadReportAttachment(String attachmentId, HttpServletResponse response) throws IOException {
		OrderAttachment attachment = ltOrderDao.getOrderAttachment(attachmentId);
		if (null != attachment) {
			FileMetaBean fileInfo = apiFileService.getFileService().getFileInfoById(attachment.getFileHashcode());
			InputStream inputStream = apiFileService.getFileService().getFile(attachment.getFileHashcode());
			IOUtils.copy(inputStream, response.getOutputStream());
			response.addHeader(HttpHeaders.CONTENT_TYPE, fileInfo.getFileType());
			response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+ fileInfo.getFileName() + "\"");
			response.addHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileInfo.getFileSize()));
		}
	}
}
