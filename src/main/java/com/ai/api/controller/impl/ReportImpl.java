package com.ai.api.controller.impl;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.ai.api.controller.Report;
import com.ai.api.service.ReportService;
import com.ai.api.util.AIUtil;
import com.ai.commons.StringUtils;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.PageBean;
import com.ai.commons.beans.PageParamBean;
import com.ai.commons.beans.fileservice.ApiFileMetaBean;
import com.ai.commons.beans.order.SimpleOrderSearchBean;
import com.ai.commons.beans.psi.report.ApprovalCertificateBean;
import com.ai.commons.beans.psi.report.ClientReportSearchBean;
import com.ai.commons.beans.report.ReportsForwardingBean;
import com.ai.commons.beans.sync.LotusSyncBean;
import com.ai.commons.constants.AuditConstants;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yan on 2016/7/25.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "restriction" })
@RestController
@Api(tags = { "Report" }, description = "Report APIs")
public class ReportImpl implements Report {
	protected Logger logger = LoggerFactory.getLogger(ReportImpl.class);

	@Autowired
	ReportService reportService;

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/audit-reports", method = RequestMethod.GET)
	@ApiOperation(value = "Get User's Audit Reports List", response = PageBean.class)
	public ResponseEntity<ApiCallResult> getAuditReports(
			@ApiParam(required = true) @PathVariable("userId") String userId,
			@ApiParam(value = "search period start date(format like 2016-12-01)", required = false) @RequestParam(value = "start", required = false) String startDate,
			@ApiParam(value = "search period end date(format like 2016-12-01)", required = false) @RequestParam(value = "end", required = false) String endDate,
			@ApiParam(value = "search keyword", required = false) @RequestParam(value = "keyword", required = false) String keywords,
			@RequestParam(value = "page", required = false) Integer pageNumber,
			@RequestParam(value = "page-size", required = false) Integer pageSize) {
		PageParamBean paramBean = new PageParamBean();
		if (null != pageNumber && pageNumber > 0)
			paramBean.setPageNo(pageNumber);
		if (null != pageSize && pageSize > 0)
			paramBean.setPageSize(pageSize);
		Map<String, String[]> criterias = new HashMap<String, String[]>();
		if (null != startDate && null != endDate) {
			String[] inspectionDate = new String[] { startDate + " - " + endDate };
			criterias.put(AuditConstants.AUDIT_DATE, inspectionDate);
		}
		if (StringUtils.isNotBlank(keywords)) {
			String[] orderOrPo = new String[] { keywords };
			criterias.put(AuditConstants.ORDER_NUMBER, orderOrPo);
		}
		if (criterias.size() > 0) {
			paramBean.setCriterias(criterias);
		}
		// List<String> item = new ArrayList<String>();
		// item.add("inspectionDate");
		// paramBean.setOrderItems(item);

		ApiCallResult result = reportService.getAuditReports(userId, paramBean);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/reports", method = RequestMethod.GET)
	@ApiOperation(value = "Get User's Reports List", response = PageBean.class)
	public ResponseEntity<PageBean<ClientReportSearchBean>> getPSIReports(
			@ApiParam(required = true) @PathVariable("userId") String userId,
			@ApiParam(value = "search period start date(format like 2016-12-01)", required = false) @RequestParam(value = "start", required = false) String startDate,
			@ApiParam(value = "search period end date(format like 2016-12-01)", required = false) @RequestParam(value = "end", required = false) String endDate,
			@ApiParam(value = "search keyword matches to Order number, or PO number or product id", required = false) @RequestParam(value = "keyword", required = false) String keywords,
			@RequestParam(value = "page", required = false) Integer pageNumber,
			@RequestParam(value = "page-size", required = false) Integer pageSize) {
		PageParamBean paramBean = new PageParamBean();
		if (null != pageNumber && pageNumber > 0)
			paramBean.setPageNo(pageNumber);
		if (null != pageSize && pageSize > 0)
			paramBean.setPageSize(pageSize);
		Map<String, String[]> criterias = new HashMap<String, String[]>();
		if (null != startDate && null != endDate) {
			String[] inspectionDate = new String[] { startDate + " - " + endDate };
			criterias.put("INSPECTION_DATE", inspectionDate);
		}
		if (StringUtils.isNotBlank(keywords)) {
			String[] orderOrPo = new String[] { keywords };
			criterias.put("ORDERNO-PONUMBER", orderOrPo);
		}
		if (criterias.size() > 0) {
			paramBean.setCriterias(criterias);
		}
		List<String> item = new ArrayList<String>();
		item.add("inspectionDate");
		paramBean.setOrderItems(item);

		PageBean<ClientReportSearchBean> result = reportService.getPSIReports(userId, paramBean);
		if (result != null) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/report/{productId}/certificate/{certType}", method = RequestMethod.GET)
	@ApiOperation(value = "Get User's Report Certificate Info", response = ApprovalCertificateBean.class)
	public ResponseEntity<ApprovalCertificateBean> getApprovalCertificate(
			@ApiParam(required = true) @PathVariable("userId") String userId,
			@ApiParam(required = true) @PathVariable("productId") String productId,
			@ApiParam(value = "can only be 'approve' or 'reject'", required = true) @PathVariable("certType") String certType) {
		ApprovalCertificateBean approvalCertificateBean = reportService.getApprovalCertificate(userId, productId,
				certType);
		if (null != approvalCertificateBean) {
			return new ResponseEntity<>(approvalCertificateBean, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/report", method = RequestMethod.PUT)
	@ApiOperation(value = "User Confirm Report Result", response = String.class)
	public ResponseEntity<String> confirmApprovalCertificate(
			@ApiParam(required = true) @PathVariable("userId") String userId,
			@ApiParam(required = true) @RequestBody ApprovalCertificateBean cert) {
		logger.info("confirmApprovalCertificate ...");
		logger.info(cert.toString());
		boolean b = reportService.confirmApprovalCertificate(userId, cert);
		if (b) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/report/{productId}/pdf-info", method = RequestMethod.GET)
	@ApiOperation(value = "Get User's PDF Report Info", response = List.class)
	public ResponseEntity<List<String>> getUserReportPdfInfo(
			@ApiParam(required = true) @PathVariable("userId") String userId,
			@ApiParam(required = true) @PathVariable("productId") String productId) {

		List<String> result = reportService.getUserReportPdfInfo(userId, productId);
		if (result != null) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	// @TokenSecured
	@RequestMapping(value = "/user/{userId}/report/{productId}/filename/{fileName}/pdf", method = RequestMethod.GET)
	@ApiOperation(value = "Download User's PDF Report", response = String.class)
	public ResponseEntity<String> downloadPDF(@ApiParam(required = true) @PathVariable("userId") String userId,
			@ApiParam(required = true) @PathVariable("productId") String productId,
			@ApiParam(required = true) @PathVariable("fileName") String fileName,
											  @ApiParam(value = "user token sessionId", required = true) @RequestParam("sessionId") String sessionId,
											  @ApiParam(value = "last 50 chars of the user token", required = true) @RequestParam("code") String verifiedCode,HttpServletResponse httpResponse) {
		logger.info("downloadPDF ...");
		logger.info("userId : " + userId);
		logger.info("reportId : " + productId);
		logger.info("fileName : " + fileName);
		if(AIUtil.verifiedAccess(userId, verifiedCode, sessionId)) {
			boolean b = reportService.downloadPDF(productId, fileName, httpResponse);
			if (b) {
				return new ResponseEntity<>(HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}else {
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
	}

//	@Override
//	@TokenSecured
//	@RequestMapping(value = "/user/{userId}/report/{productId}/filename/{fileName}/pdf-base64", method = RequestMethod.GET)
//	@ApiOperation(value = "Get User's PDF Report Base64 Code", response = String.class)
//	public ResponseEntity<ApiCallResult> downloadPDFBase64(
//			@ApiParam(required = true) @PathVariable("userId") String userId,
//			@ApiParam(required = true) @PathVariable("productId") String productId,
//			@ApiParam(required = true) @PathVariable("fileName") String fileName, HttpServletResponse httpResponse) {
//		logger.info("invoke: " + "/user/" + userId + "/report/" + productId + "/filename/" + fileName + "/pdf-base64");
//		InputStream input = reportService.downloadPDFBase64(productId, fileName, httpResponse);
//		ApiCallResult result = new ApiCallResult();
//		if (null == input) {
//			result.setMessage("ERROR!!! downloadPDFBase64");
//			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
//		} else {
//			try {
//				byte[] data = IOUtils.toByteArray(input);
//				String fileStr = Base64.encode(data);
//
//				result.setContent(fileStr);
//				return new ResponseEntity<>(result, HttpStatus.OK);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//				result.setMessage(e.toString());
//				return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
//			}
//		}
//	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/export-audit-reports", method = RequestMethod.GET)
	@ApiOperation(value = "Export Audit Report As Excle API", response = String.class)
	public ResponseEntity<ApiCallResult> exportAuditReports(
			@ApiParam(required = true) @PathVariable("userId") String userId,
			@ApiParam(value = "must be in format like 2016-12-01", required = false) @RequestParam(value = "start", required = false, defaultValue = "") String start,
			@ApiParam(value = "must be in format like 2016-12-01", required = false) @RequestParam(value = "end", required = false, defaultValue = "") String end) {
		// TODO Auto-generated method stub
		logger.info("invoke: " + "/user/" + userId + "/export-audit-reports");
		Map<String, String[]> criterias = new HashMap<String, String[]>();
		List<String> orderItems = new ArrayList<String>();
		orderItems.add("inspectionDate");

		String inspectionPeriod = null;
		if (!("".equals(start) && "".equals(end))) {
			inspectionPeriod = start + " - " + end;
			criterias.put("INSPECTION_DATE", new String[] { inspectionPeriod });
		} else {
			// get last 3 month
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar rightNow = Calendar.getInstance();
			String endStr = sf.format(rightNow.getTime());
			rightNow.add(Calendar.MONTH, -3);
			String startStr = sf.format(rightNow.getTime());

			inspectionPeriod = startStr + " - " + endStr;
		}

		PageParamBean criteriaBean = new PageParamBean();
		criteriaBean.setCriterias(criterias);
		criteriaBean.setOrderItems(orderItems);
		criteriaBean.setIsShowAll(true);
		ApiCallResult result = reportService.exportAuditReport(userId, criteriaBean, inspectionPeriod);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@ApiOperation(value = "Export Inspection Report As Excle API", response = InputStream.class)
	@RequestMapping(value = "/user/{userId}/export-reports", method = RequestMethod.GET)
	public ResponseEntity<Map<String, String>> exportReports(
			@ApiParam(required = true) @PathVariable("userId") String userId,
			@ApiParam(value = "must be in format like 2016-12-01", required = false) @RequestParam(value = "start", required = false, defaultValue = "") String start,
			@ApiParam(value = "must be in format like 2016-12-01", required = false) @RequestParam(value = "end", required = false, defaultValue = "") String end,
			HttpServletResponse httpResponse) {
		logger.info("export reports ... ");
		logger.info("userId : " + userId);
		logger.info("start : " + start + "   end : " + end);
		Map<String, String[]> criterias = new HashMap<String, String[]>();
		List<String> orderItems = new ArrayList<String>();
		orderItems.add("inspectionDate");

		String inspectionPeriod = null;
		if (!("".equals(start) && "".equals(end))) {
			inspectionPeriod = start + " - " + end;
			criterias.put("INSPECTION_DATE", new String[] { inspectionPeriod });
		} else {
			// get last 3 month
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar rightNow = Calendar.getInstance();
			String endStr = sf.format(rightNow.getTime());
			rightNow.add(Calendar.MONTH, -3);
			String startStr = sf.format(rightNow.getTime());

			inspectionPeriod = startStr + " - " + endStr;
		}

		PageParamBean criteriaBean = new PageParamBean();
		criteriaBean.setCriterias(criterias);
		criteriaBean.setOrderItems(orderItems);
		criteriaBean.setIsShowAll(true);
		InputStream inputStream = reportService.exportReports(userId, criteriaBean, inspectionPeriod);
		Map<String, String> result = new HashMap<String, String>();
		String fileStr = null;
		if (inputStream != null) {
			try {
				byte[] data = IOUtils.toByteArray(inputStream);
				fileStr = Base64.encode(data);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		result.put("xlsx_base64", fileStr);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/report-reference/{referenceId}/certificate/{certType}", method = RequestMethod.GET)
	@ApiOperation(value = "Get User's Report Certificate Info By Reference", response = ApprovalCertificateBean.class)
	public ResponseEntity<ApprovalCertificateBean> getReferenceApproveCertificate(
			@ApiParam(required = true) @PathVariable("userId") String userId,
			@ApiParam(required = true) @PathVariable("referenceId") String referenceId,
			@ApiParam(value = "can only be 'approve' or 'reject'", required = true) @PathVariable("certType") String certType) {
		// TODO Auto-generated method stub
		ApprovalCertificateBean approvalCertificateBean = reportService.getReferenceApproveCertificate(userId,
				referenceId, certType);
		if (null != approvalCertificateBean) {
			return new ResponseEntity<>(approvalCertificateBean, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/report/{productId}/undone", method = RequestMethod.PUT)
	@ApiOperation(value = "Undo Decesion in User's Report", response = Boolean.class)
	public ResponseEntity<Boolean> undoDecisionForReport(
			@ApiParam(required = true) @PathVariable("userId") String userId,
			@ApiParam(required = true) @PathVariable("productId") String productId) {
		// TODO Auto-generated method stub
		boolean result = reportService.undoDecisionForReport(userId, productId);
		if (result) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/report-reference/{referenceId}/undone", method = RequestMethod.PUT)
	@ApiOperation(value = "Undo Decesion in User's Report Reference", response = Boolean.class)
	public ResponseEntity<Boolean> undoDecisionForReference(
			@ApiParam(required = true) @PathVariable("userId") String userId,
			@ApiParam(required = true) @PathVariable("referenceId") String referenceId) {
		// TODO Auto-generated method stub
		boolean result = reportService.undoDecisionForReference(userId, referenceId);
		if (result) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/reports/{reportIds}/forwarded", method = RequestMethod.POST)
	@ApiOperation(value = "Forward User's Reports By Email", response = String.class)
	public ResponseEntity<String> forwardReports(@ApiParam(required = true) @PathVariable("userId") String userId,
			@ApiParam(value = "comma delimited report ids", required = true) @PathVariable("reportIds") String reportIds,
			@ApiParam(required = true) @RequestBody ReportsForwardingBean reportsForwardingBean) {
		reportIds = reportIds.replace(",", ";");
		if (StringUtils.isBlank(reportsForwardingBean.getTo())) {
			return new ResponseEntity<>("the field 'to' can not be null!", HttpStatus.BAD_REQUEST);
		}
		boolean b = reportService.clientForwardReport(reportsForwardingBean, userId, reportIds);
		if (b) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/report/{productId}/pdf-certificate-base64", method = RequestMethod.GET)
	@ApiOperation(value = "Get Certificated PDF Base64 Code", response = String.class)
	public ResponseEntity<ApiCallResult> getPDFCertificate(
			@ApiParam(required = true) @PathVariable("userId") String userId,
			@ApiParam(required = true) @PathVariable("productId") String productId, HttpServletResponse httpResponse) {
		// TODO Auto-generated method stub
		logger.info("invoke: " + "/user/" + userId + "/report/" + productId + "/pdf-Certificate");
		List<LotusSyncBean> list = reportService.listAllSyncObjByOracleId(productId, "report_detail");
		JSONObject joson = JSON.parseObject(JSON.toJSONString(list.get(0)));
		String lotusId = joson.getString("lotusId");
		String result = reportService.getPDFCertificate(lotusId);
		ApiCallResult finalResult = new ApiCallResult();
		if (null != result) {
			// byte[] data = IOUtils.toByteArray(result);
			// String fileStr = Base64.encode(data);
			if (result.contains("<!DOCTYPE HTML PUBLIC")) {
				finalResult.setMessage("Inspection certificate is generating, please wait.");
				return new ResponseEntity<>(finalResult, HttpStatus.OK);
			} else {
				finalResult.setContent(result);
				return new ResponseEntity<>(finalResult, HttpStatus.OK);
			}
		} else {
			finalResult.setMessage("Can not get Certificate from MW");
			return new ResponseEntity<>(finalResult, HttpStatus.OK);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/audit-reports/{reportIds}/forwarded", method = RequestMethod.POST)
	@ApiOperation(value = "Forward Audit Reports by E-mail", response = String.class)
	public ResponseEntity<String> forwardedAuditReports(@PathVariable("userId") String userId,
			@PathVariable("reportIds") String reportIds,
			@ApiParam(required = true) @RequestBody ReportsForwardingBean reportsForwardingBean) {
		// TODO Auto-generated method stub
		reportIds = reportIds.replace(",", ";");
		logger.info("invoke: " + "/user/" + userId + "/audit-reports/" + reportIds + "forwarded");
		reportIds = reportIds.replace(",", ";");
		if (StringUtils.isBlank(reportsForwardingBean.getTo())) {
			return new ResponseEntity<>("the field 'to' can not be null!", HttpStatus.BAD_REQUEST);
		}
		boolean b = reportService.forwardedAuditReports(userId, reportIds, reportsForwardingBean);
		if (b) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/audit-report/{orderId}/pdf-info", method = RequestMethod.GET)
	@ApiOperation(value = "Get Audit Report PDF Info API", response = ApiFileMetaBean.class,responseContainer = "List")
	public ResponseEntity<ApiCallResult> getAuditReportPDFInfo(
			@ApiParam(value = "userId", required = true)
			@PathVariable("userId") String userId,
			@ApiParam(value = "orderId", required = true)
			@PathVariable("orderId") String orderId) {
		logger.info("invoke: " + "/user/" + userId + "/audit-report/"+orderId+"/pdf-info");
		ApiCallResult result = reportService.getAuditReportPDFInfo(userId,orderId);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
