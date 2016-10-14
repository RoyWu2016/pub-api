package com.ai.api.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ai.api.bean.UserBean;
import com.ai.api.dao.ReportDao;
import com.ai.api.service.ReportService;
import com.ai.api.service.UserService;
import com.ai.commons.JsonUtil;
import com.ai.commons.beans.PageBean;
import com.ai.commons.beans.PageParamBean;
import com.ai.commons.beans.payment.api.PaypalInfoBean;
import com.ai.commons.beans.psi.report.ApprovalCertificateBean;
import com.ai.commons.beans.psi.report.ClientReportSearchBean;
import com.ai.commons.beans.report.ReportsForwardingBean;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * Created by yan on 2016/7/25.
 */
@Service
public class ReportServiceImpl implements ReportService {
	protected Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

	@Autowired
	@Qualifier("reportDao")
	private ReportDao reportDao;

	// @Autowired
	// @Qualifier("customerDao")
	// private CustomerDao customerDao;

	@Autowired
	@Qualifier("userService")
	private UserService userService;

	@Override
	public PageBean<ClientReportSearchBean> getPSIReports(String useId, PageParamBean paramBean) {
		String companyId = "";
		String parentId = "";
		UserBean user = null;
		try {
			user = userService.getCustById(useId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null)
				parentId = "";
			companyId = user.getCompany().getId();
		}
		return reportDao.getPSIReports(useId,companyId,parentId, paramBean);
	}

	@Override
	public ApprovalCertificateBean getApprovalCertificate(String userId, String productId, String certType) {
		String companyId = "";
		String parentId = "";
		UserBean user = null;
		try {
			user = userService.getCustById(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null)
				parentId = "";
			companyId = user.getCompany().getId();
		}
		return reportDao.getApprovalCertificate(userId, companyId, parentId, productId, certType);
	}

	@Override
	public boolean confirmApprovalCertificate(String userId, ApprovalCertificateBean cert) {
		String companyId = "";
		String parentId = "";
		UserBean user = null;
		try {
			user = userService.getCustById(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null)
				parentId = "";
			companyId = user.getCompany().getId();
		}
		return reportDao.confirmApprovalCertificate(userId, companyId, parentId, cert);
	}

	@Override
	public List<String> getUserReportPdfInfo(String userId, String reportId) {
		String login = userService.getLoginByUserId(userId);// customerDao.getGeneralUser(userId).getLogin();
		return reportDao.getUserReportPdfInfo(userId, login, reportId);
	}

	@Override
	public boolean downloadPDF(String reportId, String fileName, HttpServletResponse httpResponse) {
		boolean b = false;
		try {

			httpResponse.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			InputStream inputStream = reportDao.downloadPDF(reportId, fileName);
			ServletOutputStream output = httpResponse.getOutputStream();
			httpResponse.setStatus(HttpServletResponse.SC_OK);
			byte[] buffer = new byte[10240];
			for (int length = 0; (length = inputStream.read(buffer)) > 0;) {
				output.write(buffer, 0, length);
			}
			b = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}

	@Override
	public InputStream exportReports(String userId,PageParamBean criteria,String inspectionPeriod) {
		try {
			UserBean userBean = userService.getCustById(userId);
			String clientLogin = userService.getLoginByUserId(userId);
			String parentId = "";
			String companyId = "";
			if(null != userBean) {
				parentId = userBean.getCompany().getParentCompanyId();
				if (null == parentId) {
					parentId = "";
				}
				companyId = userBean.getCompany().getId();
			}
			PageBean<ClientReportSearchBean> result = reportDao.getPSIReports(userId,companyId,parentId,criteria);
			if(null == result) {
				logger.info("Report is not found from psi-service");
				return null;
			}else {
				XSSFWorkbook wb = new XSSFWorkbook();
				logger.info("Reports are found and begin to generate excle.");
				return createExcleFile(wb,result,clientLogin,inspectionPeriod);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private InputStream createExcleFile(
			XSSFWorkbook wb, 
			PageBean<ClientReportSearchBean> result, 
			String clientLogin, 
			String inspectionPeriod) throws IOException {
		// TODO Auto-generated method stub
		int i = 0;
		Font font = wb.createFont();
		font.setFontName("Verdana");
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

		CellStyle tileCS = wb.createCellStyle();
		tileCS.setAlignment(CellStyle.ALIGN_CENTER);
		tileCS.setFillForegroundColor(HSSFColor.WHITE.index);
		tileCS.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		tileCS.setFont(font);

		CellStyle dateCS = wb.createCellStyle();
		dateCS.setFillForegroundColor(HSSFColor.WHITE.index);
		dateCS.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

		CellStyle tableHeadeCS = wb.createCellStyle();
		tableHeadeCS.setAlignment(CellStyle.ALIGN_CENTER);
		tableHeadeCS.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		tableHeadeCS.setFont(font);
		tableHeadeCS.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		tableHeadeCS.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		tableHeadeCS.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		tableHeadeCS.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		
		CellStyle tableCS = wb.createCellStyle();
		tableCS.setAlignment(CellStyle.ALIGN_CENTER);
		tableCS.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		tableCS.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		tableCS.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		tableCS.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		tableCS.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		tableCS.setWrapText(true);

		Sheet sheet = wb.createSheet("AI Orders List (2016-09-01 - 2016-10-01");
		Row row = null;
		for (i = 0; i <= 10; i++) {
			row = sheet.createRow(i);
			for (int j = 0; j < 9; j++) {
				row.createCell(j).setCellStyle(tileCS);
			}
		}

		sheet.addMergedRegion(new CellRangeAddress(4, 4, 0, 8));

		row = sheet.getRow(4);
		Cell cell = row.createCell(0);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("List of Orders from " + inspectionPeriod);
		cell.setCellStyle(tileCS);

		row = sheet.getRow(6);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MMMM-dd",Locale.ENGLISH);
		cell = row.createCell(0);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Date: " + sf.format(new Date()));
		cell.setCellStyle(dateCS);

		row = sheet.getRow(7);
		cell = row.createCell(0);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Client login: " + clientLogin);
		cell.setCellStyle(dateCS);

		String[] title = new String[] { "Type", "Product Name", "Product Ref / SKU", "P/O Number", "Report received on",
				"Factory Name", "Result", "Status", "AI Rerence" };
		row = sheet.createRow(10);
		for (int k = 0; k < 9; k++) {
			cell = row.createCell(k);
			cell.setCellStyle(tableHeadeCS);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(title[k]);
			sheet.autoSizeColumn((short) k);
		}
		
		int itemsRow = (int) result.getTotalSize();
		for(int rowid=11;rowid<10+itemsRow;rowid++) {
			row = sheet.createRow(rowid);
			for(int cellid=0;cellid<9;cellid++) {
				cell = row.createCell(cellid);
				cell.setCellStyle(tableCS);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				String lists =  result.getPageItems().toString();
				logger.info(lists);
				List<ClientReportSearchBean> list = JsonUtil.mapToObject(lists, new TypeReference<List<ClientReportSearchBean>>(){});
				switch (cellid){
				case 0: cell.setCellValue(list.get(rowid-11).getServiceTypeText());break;
				case 1: cell.setCellValue(list.get(rowid-11).getProductName());break;
				case 2: cell.setCellValue(list.get(rowid-11).getProdReference());break;
				case 3: cell.setCellValue(list.get(rowid-11).getPoNumber());break;
				case 4: cell.setCellValue(list.get(rowid-11).getInspectionDateMMMFormat());break;
				case 5: cell.setCellValue(list.get(rowid-11).getSupplierName());break;
				case 6: cell.setCellValue(list.get(rowid-11).getOverrallResult());break;
				case 7: cell.setCellValue(list.get(rowid-11).getStatus());break;
				case 8: cell.setCellValue(list.get(rowid-11).getOrderNumber());break;
				}
			}
		}
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		wb.write(out);
		InputStream excelStream = new ByteArrayInputStream(out.toByteArray());
		out.close();
		
		return excelStream;
	}

	@Override
	public ApprovalCertificateBean getReferenceApproveCertificate(String userId, String referenceId, String certType) {
		// TODO Auto-generated method stub
		String companyId = "";
		String parentId = "";
		UserBean user = null;
		try {
			user = userService.getCustById(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null) {
				parentId = "";
			}
			companyId = user.getCompany().getId();
		}
		return reportDao.getReferenceApproveCertificate(userId, referenceId, companyId, parentId, certType);
	}

	@Override
	public boolean undoDecisionForReport(String userId, String productId) {
		// TODO Auto-generated method stub
		String companyId = "";
		String parentId = "";
		UserBean user = null;
		try {
			user = userService.getCustById(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null) {
				parentId = "";
			}
			companyId = user.getCompany().getId();
		}
		return reportDao.undoDecisionForReport(userId, productId, companyId, parentId);
	}

	@Override
	public boolean undoDecisionForReference(String userId, String referenceId) {
		// TODO Auto-generated method stub
		String companyId = "";
		String parentId = "";
		UserBean user = null;
		try {
			user = userService.getCustById(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null) {
				parentId = "";
			}
			companyId = user.getCompany().getId();
		}
		return reportDao.undoDecisionForReference(userId, referenceId, companyId, parentId);
	}

	@Override
	public boolean clientForwardReport(ReportsForwardingBean reportsForwardingBean, String userId) {
		// TODO Auto-generated method stub
		String companyId = "";
		String parentId = "";
		UserBean user = null;
		try {
			user = userService.getCustById(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null) {
				parentId = "";
			}
			companyId = user.getCompany().getId();
		}
		return reportDao.clientForwardReport(reportsForwardingBean, companyId, parentId, userId);
	}
}
