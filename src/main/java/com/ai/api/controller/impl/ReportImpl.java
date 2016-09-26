package com.ai.api.controller.impl;

import java.util.*;

import javax.servlet.http.HttpServletResponse;

import com.ai.api.controller.Report;
import com.ai.api.service.ReportService;
import com.ai.commons.DateUtils;
import com.ai.commons.StringUtils;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.PageBean;
import com.ai.commons.beans.PageParamBean;
import com.ai.commons.beans.psi.report.ClientReportSearchBean;
import com.ai.commons.beans.report.ReportSearchCriteriaBean;
import com.ai.commons.beans.report.ReportSearchResultBean;
import com.ai.commons.beans.report.ReportsForwardingBean;
import com.ai.commons.beans.report.api.ReportCertificateBean;
import com.ai.commons.beans.report.api.ReportDetail;
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
@RestController
public class ReportImpl implements Report {
    protected Logger logger = LoggerFactory.getLogger(ReportImpl.class);

    @Autowired
    ReportService reportService;

    @Override
    @TokenSecured
    @RequestMapping(value = "/user/{userId}/psi-reports", method = RequestMethod.GET)
    public ResponseEntity<PageBean<ClientReportSearchBean>> getPSIReports (@PathVariable("userId") String userId,
                                                                         @RequestParam(value = "start",required = false) String startDate,
                                                                         @RequestParam(value = "end",required = false) String endDate,
                                                                         @RequestParam(value = "keyword",required = false) String keywords,
                                                                         @RequestParam(value = "page",required = false) Integer pageNumber,
                                                                         @RequestParam(value = "page-size",required = false) Integer pageSize) {
        PageParamBean paramBean = new PageParamBean();
        if (null!=pageNumber&&pageNumber>0)paramBean.setPageNo(pageNumber);
        if (null!=pageSize&&pageSize>0)paramBean.setPageSize(pageSize);
        Map<String, String[]> criterias = new HashMap<String, String[]>();
        Date now = Calendar.getInstance().getTime();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -30);
        String[] inspectionDate = new String[]{DateUtils.date2String(cal.getTime(),DateUtils.Format.AI_DATE_FORMAT_JSON.getValue())+" - "+DateUtils.date2String(now,DateUtils.Format.AI_DATE_FORMAT_JSON.getValue())};
        if (null!=startDate&&null!=endDate){
            inspectionDate = new String[]{startDate+" - "+endDate};
        }
        String[] orderOrPo = new String[]{keywords};
        criterias.put("ORDERNO-PONUMBER",orderOrPo);
        criterias.put("INSPECTION_DATE",inspectionDate);
        paramBean.setCriterias(criterias);
        List<String> item = new ArrayList<String>();
        item.add("inspectionDate");
        paramBean.setOrderItems(item);

        PageBean<ClientReportSearchBean> result = reportService.getPSIReports(userId,paramBean);
        if(result!=null){
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @TokenSecured
    @RequestMapping(value = "/user/{userId}/reports/{ids}/forwarded", method = RequestMethod.POST)
    public ResponseEntity<String> forwardReports(@PathVariable("userId") String userId, @PathVariable("ids") String ids,
                                                 @RequestBody ReportsForwardingBean reportsForwardingBean) {
        if (StringUtils.isBlank(reportsForwardingBean.getTo())){
            return new ResponseEntity<>("the field 'to' can not be null!",HttpStatus.BAD_REQUEST);
        }
        reportsForwardingBean.setUserId(userId);
        reportsForwardingBean.setIds(ids);
        boolean b = reportService.forwardReports(reportsForwardingBean);
        if(b){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @TokenSecured
    @RequestMapping(value = "/user/{userId}/report/{id}/undone", method = RequestMethod.PUT)
    public ResponseEntity<String> undoDecision(@PathVariable("userId") String userId, @PathVariable("id") String id){
        boolean b = reportService.undoDecision(userId,id);
        if(b){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @TokenSecured
    @RequestMapping(value = "/user/{userId}/report/{reportId}/certificate/{certType}", method = RequestMethod.GET)
    public ResponseEntity<ReportCertificateBean> getApprovalCertificate(@PathVariable("userId") String userId,
                                               @PathVariable("reportId") String reportId,
                                               @PathVariable("certType") String certType,
                                               @RequestParam(value = "reference",required = false) String reference){
        ReportCertificateBean reportCertificateBean = reportService.getApprovalCertificate(reportId,userId,certType,reference);
        if(null!=reportCertificateBean){
            return new ResponseEntity<>(reportCertificateBean,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @TokenSecured
    @RequestMapping(value = "/user/{userId}/report/{reportId}", method = RequestMethod.PUT)
    public ResponseEntity<String> confirmApprovalCertificate(@PathVariable("userId") String userId, @PathVariable("reportId") String reportId,
                                                 @RequestBody ReportCertificateBean reportCertificateBean) {
        logger.info("confirmApprovalCertificate ...");
        logger.info(reportCertificateBean.toString());
        ReportDetail reportDetail = reportCertificateBean.getReportDetail();
        if (null==reportDetail) {
            reportDetail = new ReportDetail();
        }
        reportDetail.setUuid(reportId);
        reportCertificateBean.setReportDetail(reportDetail);
        boolean b = reportService.confirmApprovalCertificate(userId,reportCertificateBean);
        if(b){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @TokenSecured
    @RequestMapping(value = "/user/{userId}/report/{reportId}/pdfInfo", method = RequestMethod.GET)
    public ResponseEntity<List<String>> getUserReportPdfInfo(@PathVariable("userId") String userId,
                                                                            @PathVariable("reportId") String reportId){

        List<String> result = reportService.getUserReportPdfInfo(userId, reportId);
        if(result!=null){
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @TokenSecured
    @RequestMapping(value = "/user/{userId}/report/{reportId}/filename/{fileName}/pdf", method = RequestMethod.GET)
    public ResponseEntity<String> downloadPDF(@PathVariable("userId") String userId,
                                              @PathVariable("reportId") String reportId,
                                              @PathVariable("fileName") String fileName,
                                              HttpServletResponse httpResponse) {
        logger.info("downloadPDF ...");
        logger.info("userId : "+userId);
        logger.info("reportId : "+reportId);
        logger.info("fileName : "+fileName);
        boolean b = reportService.downloadPDF(reportId,fileName,httpResponse);
        if(b){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/reports", method = RequestMethod.POST)
	public ResponseEntity<String> exportReports(@PathVariable("userId") String userId,
	                                            @RequestParam(value = "start",required = false) String start,
	                                            @RequestParam(value = "end",required = false) String end,
                                                HttpServletResponse httpResponse) {
		logger.info("export reports ... ");
		logger.info("userId : "+userId);
		logger.info("start : "+start+ "   end : "+end);
		ReportSearchCriteriaBean criteriaBean = new ReportSearchCriteriaBean();
		criteriaBean.setUserID(userId);
		criteriaBean.setStartDate(start);
		criteriaBean.setEndDate(end);
		boolean b = reportService.exportReports(criteriaBean,httpResponse);
		if(b){
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>("no report pdf file found",HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
