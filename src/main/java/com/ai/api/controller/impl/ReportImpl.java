package com.ai.api.controller.impl;

import com.ai.api.controller.Report;
import com.ai.api.service.ReportService;
import com.ai.commons.beans.report.ReportSearchCriteriaBean;
import com.ai.commons.beans.report.ReportSearchResultBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by yan on 2016/7/25.
 */
@RestController
public class ReportImpl implements Report {
    protected Logger logger = LoggerFactory.getLogger(ReportImpl.class);

    @Autowired
    ReportService reportService;

    @Override
    @RequestMapping(value = "/user/{userId}/reports", method = RequestMethod.GET)
    public ResponseEntity<List<ReportSearchResultBean>> getUserReportsByCriteria(@PathVariable("userId") String userId,
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

        try {
            if ((starts == null && ends == null) || (starts.equals("") && ends.equals(""))) {
                Date current = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                ends = sdf.format(current);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(current);
                calendar.add(Calendar.MONTH, -3);
                starts = sdf.format(calendar.getTime());
            }
            if (starts == null && ends != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(sdf.parse(ends));
                calendar.add(Calendar.MONTH, -3);
                starts = sdf.format(calendar.getTime());
            }
            if (starts != null && ends == null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(sdf.parse(starts));
                calendar.add(Calendar.MONTH, +3);
                Date current = new Date();
                if(calendar.getTime().getTime()>current.getTime()){
                    ends = sdf.format(current);
                }else {
                    ends = sdf.format(calendar.getTime());
                }
            }
            criteriaBean.setStartDate(starts);
            criteriaBean.setEndDate(ends);
        }catch (Exception e){
            e.printStackTrace();
        }

        List<ReportSearchResultBean> result = reportService.getUserReportsByCriteria(criteriaBean);
        if(result!=null){
            return new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
