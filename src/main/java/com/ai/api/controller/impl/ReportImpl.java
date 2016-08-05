package com.ai.api.controller.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.ai.api.controller.Report;
import com.ai.api.service.ReportService;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.report.ReportSearchCriteriaBean;
import com.ai.commons.beans.report.ReportSearchResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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
}
