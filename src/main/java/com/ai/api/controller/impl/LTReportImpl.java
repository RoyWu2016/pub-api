package com.ai.api.controller.impl;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.client.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ai.aims.services.model.OrderMaster;
import com.ai.aims.services.model.OrderStyleInfo;
import com.ai.aims.services.model.OrderTestAssignment;
import com.ai.api.bean.OrderSearchBean;
import com.ai.api.bean.OrderTestBean;
import com.ai.api.controller.LTReport;
import com.ai.api.service.LTReportService;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.ApiCallResult;

@SuppressWarnings({ "rawtypes", "unchecked" })
@RestController
public class LTReportImpl implements LTReport {
	
	protected Logger logger = LoggerFactory.getLogger(LTOrderImpl.class);
	
	@Autowired
	@Qualifier("ltreportservice")
	private LTReportService ltReportService;

	@Override
	@ApiOperation(value = "Find Reports API", produces = "application/json", response = OrderSearchBean.class, responseContainer = "List", httpMethod = "GET")
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/lt/reports", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> findReports(
			@ApiParam(value="User ID") @PathVariable("userId") String userId,
			@ApiParam(value="Page Number") @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNumber,
			@ApiParam(value="Page Size") @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize) {
		ApiCallResult result = new ApiCallResult();
		HttpStatus status = HttpStatus.OK;
		try {
			List<OrderSearchBean> reports = ltReportService.findReports(userId, pageNumber, pageSize);
			if (null != reports && !reports.isEmpty()) {
				result.setContent(reports);
			} else {
				result.setContent(reports);
				result.setMessage("Empty LT reports list returned.");
                status = HttpStatus.NO_CONTENT;
			}
		} catch (Exception e) {
			logger.error("Reports search error: " + ExceptionUtils.getFullStackTrace(e));
            result.setMessage("Could not get LT reports. Error occurred!");
            status =  HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<ApiCallResult>(result, status);
	}

	@Override
	@ApiOperation(value = "Find Report API", produces = "application/json", response = OrderSearchBean.class, httpMethod = "GET")
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/lt/report/{reportId}", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> findReport(
			@ApiParam(value="User ID") @PathVariable("userId") String userId,
			@ApiParam(value="Report ID") @PathVariable("reportId") String reportId) {
		ApiCallResult result = new ApiCallResult();
		HttpStatus status = HttpStatus.OK;
		try {
			OrderMaster report = ltReportService.findReport(reportId);
			if (null != report) {
				result.setContent(wrapReportObj(report));
			} else {
				result.setContent(report);
				result.setMessage("Report not found.");
                status = HttpStatus.NO_CONTENT;
			}
		} catch (Exception e) {
			logger.error("Reports search error: " + ExceptionUtils.getFullStackTrace(e));
            result.setMessage("Could not get LT report. Error occurred!");
            status =  HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<ApiCallResult>(result, status);
	}

	@Override
	@ApiOperation(value = "Update Report Status API", produces = "application/json", httpMethod = "PUT")
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/lt/report/{reportId}/status/{status}", method = RequestMethod.PUT)
	public ResponseEntity<ApiCallResult> editReportStatus(
			@ApiParam(value="User ID") @PathVariable("userId") String userId,
			@ApiParam(value="Report ID") @PathVariable("reportId") String reportId, 
			@ApiParam(value="Report Status", allowableValues="Pending,Approved,Rejected") @PathVariable("status") String status) {
		HttpStatus respStatus = HttpStatus.OK;
		ApiCallResult result = new ApiCallResult();
		try {
			result = ltReportService.editReportStatus(userId, reportId, status);
		} catch (IOException e) {
			logger.error("Report update error: " + ExceptionUtils.getFullStackTrace(e));
            result.setMessage("Could not update LT report status. Error occurred!");
            respStatus =  HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<ApiCallResult>(result, respStatus);
	}
	
	@Override
	@ApiOperation(value = "Update Report Test Status API", produces = "application/json", httpMethod = "PUT")
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/lt/report/{reportId}/test/{testId}/status/{status}", method = RequestMethod.PUT)
	public ResponseEntity<ApiCallResult> editReportTestStatus(
			@ApiParam(value="User ID") @PathVariable("userId") String userId,
			@ApiParam(value="Report ID") @PathVariable("reportId") String reportId, 
			@ApiParam(value="Test Assignment ID") @PathVariable("testId") String testId, 
			@ApiParam(value="Test Assignment Status", allowableValues="Pending,Approved,Rejected") @PathVariable("status") String status) {
		HttpStatus respStatus = HttpStatus.OK;
		ApiCallResult result = new ApiCallResult();
		try {
			result = ltReportService.editReportTestStatus(userId, reportId, testId, status);
		} catch (IOException e) {
			logger.error("Report update error: " + ExceptionUtils.getFullStackTrace(e));
            result.setMessage("Could not update LT report status. Error occurred!");
            respStatus =  HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<ApiCallResult>(result, respStatus);
	}
	
	@Override
	@ApiOperation(value = "Download Report Attachment API", response = String.class, httpMethod = "GET")
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/lt/report/attachment/{attachmentId}", method = RequestMethod.GET)
	public ResponseEntity downloadPDF(
			@ApiParam(value="User ID") @PathVariable("userId") String userId,
			@ApiParam(value="Report Attachment ID") @PathVariable("attachmentId") String attachmentId,
			HttpServletResponse response) {
		HttpStatus respStatus = HttpStatus.OK;
		ApiCallResult result = new ApiCallResult();
		try {
			result = ltReportService.downloadReportAttachment(attachmentId, response);
		} catch (Exception e) {
			logger.error("Report update error: " + ExceptionUtils.getFullStackTrace(e));
            respStatus =  HttpStatus.INTERNAL_SERVER_ERROR;
		}
		return new ResponseEntity<ApiCallResult>(result, respStatus);
	}
	
	private OrderSearchBean wrapReportObj(OrderMaster order) {
		OrderSearchBean orderSearch = new OrderSearchBean();
		orderSearch.setOrderId(order.getId());
		orderSearch.setSupplierName(null != order.getSupplier() ? StringUtils.stripToEmpty(order.getSupplier().getCompanyName()) : null );
		orderSearch.setServiceType("LT");
		orderSearch.setServiceTypeText("LT");
		orderSearch.setPoNumbers(order.getClientPONo());
		orderSearch.setStatus(order.getStatusCode());
		orderSearch.setBookingStatus(order.getBookingStatusCode());
		orderSearch.setBookingDate("Pending".equalsIgnoreCase(order.getOrderStatus())
				? DateUtils.formatDate(order.getUpdateTime(), "dd-MMM-yyyy") : null);
		orderSearch.setReportDueDate(null != order.getReportDueDate() ? 
				DateUtils.formatDate(order.getReportDueDate(), "dd-MMM-yyyy") : null);
		orderSearch.setOffice(null != order.getOffice() ? order.getOffice().getName() : null);
		orderSearch.setProductNames(StringUtils.stripToEmpty(order.getDescription()));
		orderSearch.setLabOrderNo(order.getLabOrderno());
		Set<OrderStyleInfo> styleInfo = order.getStyleInfo();
		if (null != styleInfo && !styleInfo.isEmpty()) {
			orderSearch.setManufacturerStyleNo(styleInfo.iterator().next().getManufacturerStyleNo());
		}
		orderSearch.setProgram(null != order.getProgram() ? order.getProgram().getProgramName() : null);
		orderSearch.setTestStartDate(null != order.getTestStartDate() ? 
				DateUtils.formatDate(order.getTestStartDate(), "dd-MMM-yyyy") : null);
		orderSearch.setOverallRating(order.getOverallRating());
		orderSearch.setClientStatus(order.getClientStatus());
		if (null != order.getAttachments()) {
			orderSearch.setAttachmentId(order.getAttachments().stream().findFirst().get().getId());
		}
		if (null != order.getTestAssignments()) {
			List<OrderTestBean> orderTests = new ArrayList<OrderTestBean>();
			for (OrderTestAssignment testAssign : order.getTestAssignments()) {
				OrderTestBean orderTest = new OrderTestBean();
				orderTest.setId(testAssign.getId());
				orderTest.setFailureStmt(testAssign.getFailureStatement());
				orderTest.setName(testAssign.getTest().getName());
				orderTest.setRating(null != testAssign.getTestAssignRating() ? testAssign.getTestAssignRating().getDescription() : null);
				orderTest.setClientStatus(testAssign.getClientStatus());
				orderTests.add(orderTest);
			}
			orderSearch.setTests(orderTests);
		}
		return orderSearch;
	}
}
