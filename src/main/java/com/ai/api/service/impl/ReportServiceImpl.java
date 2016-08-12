package com.ai.api.service.impl;

import com.ai.api.dao.CustomerDao;
import com.ai.api.dao.ReportDao;
import com.ai.api.service.ReportService;
import com.ai.commons.beans.report.ReportPdfFileInfoBean;
import com.ai.commons.beans.report.ReportSearchCriteriaBean;
import com.ai.commons.beans.report.ReportSearchResultBean;
import com.ai.commons.beans.report.ReportsForwardingBean;
import com.ai.commons.beans.report.api.ReportCertificateBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;

/**
 * Created by yan on 2016/7/25.
 */
@Service
public class ReportServiceImpl implements ReportService {
    protected Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    @Autowired
    @Qualifier("reportDao")
    private ReportDao reportDao;

    @Autowired
    @Qualifier("customerDao")
    private CustomerDao customerDao;

    @Override
    public List<ReportSearchResultBean> getUserReportsByCriteria(ReportSearchCriteriaBean criteria){
        if(criteria.getLogin()==null){
            String login = customerDao.getGeneralUser(criteria.getUserID()).getLogin();
            criteria.setLogin(login);
        }
        return reportDao.getUserReportsByCriteria(criteria);
    }

    @Override
    public boolean forwardReports(ReportsForwardingBean reportsForwardingBean){
        if(reportsForwardingBean.getLogin()==null){
            String login = customerDao.getGeneralUser(reportsForwardingBean.getUserId()).getLogin();
            reportsForwardingBean.setLogin(login);
        }
        return reportDao.forwardReports(reportsForwardingBean);
    }

    @Override
    public boolean undoDecision(String userId,String reportDetailId){
        String login = customerDao.getGeneralUser(userId).getLogin();
        return reportDao.undoDecision(login,reportDetailId);
    }
    @Override
    public ReportCertificateBean getApprovalCertificate(String reportId, String userId, String certType, String reference) {
        String login = customerDao.getGeneralUser(userId).getLogin();
        return reportDao.getApprovalCertificate(reportId,login,certType,reference);
    }

    @Override
    public boolean confirmApprovalCertificate(String userId,ReportCertificateBean reportCertificateBean){
        String login = customerDao.getGeneralUser(userId).getLogin();
        return reportDao.confirmApprovalCertificate(reportCertificateBean,login);
    }

    @Override
    public List<ReportPdfFileInfoBean> getUserReportPdfInfo(String userId, String reportId) {
        String login = customerDao.getGeneralUser(userId).getLogin();
        return reportDao.getUserReportPdfInfo(userId, login, reportId);
    }

    @Override
    public boolean downloadPDF(String reportId,String fileName,HttpServletResponse httpResponse){
        boolean b = false;
        try {

            httpResponse.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            InputStream inputStream =  reportDao.downloadPDF(reportId,fileName);
            ServletOutputStream output = httpResponse.getOutputStream();
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            byte[] buffer = new byte[10240];
            for (int length = 0; (length = inputStream.read(buffer)) > 0;) {
                output.write(buffer, 0, length);
            }
            b = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return b;
    }
}