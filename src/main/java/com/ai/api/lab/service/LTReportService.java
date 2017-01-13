/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.lab.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.ai.aims.services.model.OrderMaster;
import com.ai.api.bean.OrderSearchBean;
import com.ai.api.exception.AIException;
import com.ai.commons.beans.ApiCallResult;

/***************************************************************************
 * <PRE>
 *  Project Name    : api
 *
 *  Package Name    : com.ai.api.lab.service
 *
 *  File Name       : LTOrderService.java
 *
 *  Creation Date   : Dec 6, 2016
 *
 *  Author          : Aashish Thakran
 *
 *  Purpose         : TODO
 *
 *
 *  History         : TODO
 *
 * </PRE>
 ***************************************************************************/

@SuppressWarnings("rawtypes")
public interface LTReportService {

	public List<OrderSearchBean> findReports(String userId, Integer pageNumber, Integer pageSize) throws IOException, AIException;
	
	public OrderMaster findReport(String reportId) throws IOException;

	public ApiCallResult editReportStatus(String userId, String orderId, String status) throws IOException;

	public ApiCallResult editReportTestStatus(String userId, String reportId, String testId, String status) throws IOException;

	public void downloadReportAttachment(String attachmentId, HttpServletResponse response) throws IOException;
}
