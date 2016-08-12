package com.ai.api.controller.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ai.api.controller.Report;
import com.ai.api.service.ReportService;
import com.ai.commons.StringUtils;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.report.ReportPdfFileInfoBean;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

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
    @RequestMapping(value = "/user/{userId}/reports", method = RequestMethod.GET)
    public ResponseEntity<List<ReportSearchResultBean>> getUserReportsByCriteria(@PathVariable("userId") String userId,
                                                                                 @RequestParam(value = "types",required = false) String orderTypeArray,
                                                                                 @RequestParam(value = "page",required = false) Integer pageNumber,
                                                                                 @RequestParam(value = "archived",required = false) String archived,
                                                                                 @RequestParam(value = "start",required = false) String starts,
                                                                                 @RequestParam(value = "end",required = false) String ends,
                                                                                 @RequestParam(value = "keyword",required = false) String keywords) {

        ReportSearchCriteriaBean criteriaBean = new ReportSearchCriteriaBean();
        if(pageNumber==null){
            pageNumber = 1;
        }
        criteriaBean.setPageNumber(pageNumber);
        criteriaBean.setKeywords(keywords);
        criteriaBean.setUserID(userId);

        if(archived==null){
            criteriaBean.setArchived(false);
        } else {
            criteriaBean.setArchived(Boolean.valueOf(archived));
        }

	    ArrayList<String> typeList = new ArrayList<String>();
	    if(orderTypeArray==null || orderTypeArray.equals("")){
            String[] allTypes = {"PSI","LT","IPC","DUPRO","CLC","MA","PM","EA","StrA","CTPAT"};
            Collections.addAll(typeList, allTypes);
	    }else{
		    String[] types = orderTypeArray.split(",");
		    Collections.addAll(typeList, types);
	    }
	    criteriaBean.setServiceTypes(typeList);

        criteriaBean.setStartDate(starts);
        criteriaBean.setEndDate(ends);

        List<ReportSearchResultBean> result = reportService.getUserReportsByCriteria(criteriaBean);
        if(result!=null){
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @TokenSecured
    @RequestMapping(value = "/user/{userId}/report/{reportId}/pdfInfo", method = RequestMethod.GET)
    public ResponseEntity<List<ReportPdfFileInfoBean>> getUserReportPdfInfo(@PathVariable("userId") String userId,
                                                                            @PathVariable("reportId") String reportId){

        List<ReportPdfFileInfoBean> result = reportService.getUserReportPdfInfo(userId, reportId);
        if(result!=null){
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @TokenSecured
    @RequestMapping(value = "/user/{userId}/report/{reportId}/fileName/{fileName}/pdf", method = RequestMethod.GET)
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
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
