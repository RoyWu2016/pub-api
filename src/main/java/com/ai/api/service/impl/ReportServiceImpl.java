package com.ai.api.service.impl;

import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ai.api.bean.UserBean;
import com.ai.api.dao.ReportDao;
import com.ai.api.service.ReportService;
import com.ai.api.service.UserService;
import com.ai.commons.beans.PageBean;
import com.ai.commons.beans.PageParamBean;
import com.ai.commons.beans.psi.report.ApprovalCertificateBean;
import com.ai.commons.beans.psi.report.ClientReportSearchBean;
import com.ai.commons.beans.report.ReportSearchCriteriaBean;
import com.ai.commons.beans.report.ReportsForwardingBean;

/**
 * Created by yan on 2016/7/25.
 */
@Service
public class ReportServiceImpl implements ReportService {
    protected Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    @Autowired
    @Qualifier("reportDao")
    private ReportDao reportDao;

//    @Autowired
//    @Qualifier("customerDao")
//    private CustomerDao customerDao;

    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @Override
    public PageBean<ClientReportSearchBean> getPSIReports(String useId, PageParamBean paramBean){
        return reportDao.getPSIReports(useId,paramBean);
    }

    @Override
    public boolean forwardReports(ReportsForwardingBean reportsForwardingBean){
        if(reportsForwardingBean.getLogin()==null){
            String login = userService.getLoginByUserId(reportsForwardingBean.getUserId());//customerDao.getGeneralUser(reportsForwardingBean.getUserId()).getLogin();
            reportsForwardingBean.setLogin(login);
        }
        return reportDao.forwardReports(reportsForwardingBean);
    }
    
    @Override
    public ApprovalCertificateBean getApprovalCertificate(String userId, String productId, String certType) {
        String companyId = "";
        String parentId = "";
        UserBean user = null;
        try {
            user = userService.getCustById(userId);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (null!=user){
            parentId = user.getCompany().getParentCompanyId();
            if (parentId == null) parentId = "";
            companyId = user.getCompany().getId();
        }
        return reportDao.getApprovalCertificate(userId,companyId,parentId,productId,certType);
    }

    @Override
    public boolean confirmApprovalCertificate(String userId,ApprovalCertificateBean cert){
        String companyId = "";
        String parentId = "";
        UserBean user = null;
        try {
            user = userService.getCustById(userId);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (null!=user){
            parentId = user.getCompany().getParentCompanyId();
            if (parentId == null) parentId = "";
            companyId = user.getCompany().getId();
        }
        return reportDao.confirmApprovalCertificate(userId,companyId,parentId,cert);
    }

    @Override
    public List<String> getUserReportPdfInfo(String userId, String reportId) {
        String login = userService.getLoginByUserId(userId);//customerDao.getGeneralUser(userId).getLogin();
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

	@Override
	public boolean exportReports(ReportSearchCriteriaBean criteria,HttpServletResponse httpResponse){
        boolean b = false;
        try{
            if(criteria.getLogin()==null){
                String login = userService.getLoginByUserId(criteria.getUserID());//customerDao.getGeneralUser(criteria.getUserID()).getLogin();
                criteria.setLogin(login);
            }
            InputStream inputStream = reportDao.exportReports(criteria);
            ServletOutputStream output = httpResponse.getOutputStream();
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            if (null!=inputStream){
                byte[] buffer = new byte[10240];
                for (int length = 0; (length = inputStream.read(buffer)) > 0;) {
                    output.write(buffer, 0, length);
                }
            }
            b = true;
        }catch (Exception e){
            e.printStackTrace();
        }
            return b;
        }

	@Override
	public ApprovalCertificateBean getReferenceApproveCertificate(String userId, String referenceId,String certType) {
		// TODO Auto-generated method stub
		String companyId = "";
        String parentId = "";
        UserBean user = null;
        try {
            user = userService.getCustById(userId);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (null!=user){
            parentId = user.getCompany().getParentCompanyId();
            if (parentId == null) {
            	parentId = "";
            }
            companyId = user.getCompany().getId();
        }
		return reportDao.getReferenceApproveCertificate(userId,referenceId,companyId,parentId,certType);
	}

	@Override
	public boolean undoDecisionForReport(String userId, String productId) {
		// TODO Auto-generated method stub
		String companyId = "";
        String parentId = "";
        UserBean user = null;
        try {
            user = userService.getCustById(userId);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (null!=user){
            parentId = user.getCompany().getParentCompanyId();
            if (parentId == null) {
            	parentId = "";
            }
            companyId = user.getCompany().getId();
        }
		return reportDao.undoDecisionForReport(userId,productId,companyId,parentId);
	}
}
