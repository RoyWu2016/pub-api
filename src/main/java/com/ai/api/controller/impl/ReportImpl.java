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

import com.ai.api.controller.Report;
import com.ai.api.service.ReportService;
import com.ai.commons.StringUtils;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.PageBean;
import com.ai.commons.beans.PageParamBean;
import com.ai.commons.beans.psi.report.ApprovalCertificateBean;
import com.ai.commons.beans.psi.report.ClientReportSearchBean;
import com.ai.commons.beans.report.ReportsForwardingBean;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

/**
 * Created by yan on 2016/7/25.
 */
@SuppressWarnings({ "rawtypes", "unchecked", "restriction" })
@RestController
public class ReportImpl implements Report {
	protected Logger logger = LoggerFactory.getLogger(ReportImpl.class);

	@Autowired
	ReportService reportService;

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/reports", method = RequestMethod.GET)
	public ResponseEntity<PageBean<ClientReportSearchBean>> getPSIReports(@PathVariable("userId") String userId,
			@RequestParam(value = "start", required = false) String startDate,
			@RequestParam(value = "end", required = false) String endDate,
			@RequestParam(value = "keyword", required = false) String keywords,
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
	public ResponseEntity<ApprovalCertificateBean> getApprovalCertificate(@PathVariable("userId") String userId,
			@PathVariable("productId") String productId, @PathVariable("certType") String certType) {
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
	public ResponseEntity<String> confirmApprovalCertificate(@PathVariable("userId") String userId,
			@RequestBody ApprovalCertificateBean cert) {
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
	public ResponseEntity<List<String>> getUserReportPdfInfo(@PathVariable("userId") String userId,
			@PathVariable("productId") String productId) {

		List<String> result = reportService.getUserReportPdfInfo(userId, productId);
		if (result != null) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
//	@TokenSecured
	@RequestMapping(value = "/user/{userId}/report/{productId}/filename/{fileName}/pdf", method = RequestMethod.GET)
	public ResponseEntity<String> downloadPDF(@PathVariable("userId") String userId,
			@PathVariable("productId") String productId, @PathVariable("fileName") String fileName,
			HttpServletResponse httpResponse) {
		logger.info("downloadPDF ...");
		logger.info("userId : " + userId);
		logger.info("reportId : " + productId);
		logger.info("fileName : " + fileName);
		boolean b = reportService.downloadPDF(productId, fileName, httpResponse);
		if (b) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/report/{productId}/filename/{fileName}/pdf-base64", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> downloadPDFBase64(@PathVariable("userId") String userId,
			@PathVariable("productId") String productId, @PathVariable("fileName") String fileName,
			HttpServletResponse httpResponse) {
		logger.info("invoke: " + "/user/" + userId + "/report/" + productId +"/filename/" + fileName + "/pdf-base64");
		InputStream input = reportService.downloadPDFBase64(productId, fileName, httpResponse);
		ApiCallResult result = new ApiCallResult();
		if(null == input) {
			result.setMessage("ERROR!!! downloadPDFBase64");
			return new ResponseEntity<>(result,HttpStatus.INTERNAL_SERVER_ERROR);
		}else {
			try {
				byte[] data = IOUtils.toByteArray(input);
				String fileStr = Base64.encode(data);
				
				result.setContent(fileStr);
				return new ResponseEntity<>(result,HttpStatus.OK);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result.setMessage(e.toString());
				return new ResponseEntity<>(result,HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/export-reports", method = RequestMethod.GET)
	public ResponseEntity<Map<String, String>> exportReports(@PathVariable("userId") String userId,
			@RequestParam(value = "start", required = false, defaultValue = "") String start,
			@RequestParam(value = "end", required = false, defaultValue = "") String end,
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
	public ResponseEntity<ApprovalCertificateBean> getReferenceApproveCertificate(@PathVariable("userId") String userId,
			@PathVariable("referenceId") String referenceId, @PathVariable("certType") String certType) {
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
	public ResponseEntity<Boolean> undoDecisionForReport(@PathVariable("userId") String userId,
			@PathVariable("productId") String productId) {
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
	public ResponseEntity<Boolean> undoDecisionForReference(@PathVariable("userId") String userId,
			@PathVariable("referenceId") String referenceId) {
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
	public ResponseEntity<String> forwardReports(@PathVariable("userId") String userId,
			@PathVariable("reportIds") String reportIds, @RequestBody ReportsForwardingBean reportsForwardingBean) {
		// TODO Auto-generated method stub
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

}
