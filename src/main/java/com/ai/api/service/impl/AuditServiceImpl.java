package com.ai.api.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.ai.commons.beans.psi.api.ApiOrderFactoryBean;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ai.api.bean.UserBean;
import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.AuditDao;
import com.ai.api.service.AuditService;
import com.ai.api.service.UserService;
import com.ai.commons.Consts;
import com.ai.commons.JsonUtil;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.PageBean;
import com.ai.commons.beans.audit.AuditReportsSearchBean;
import com.ai.commons.beans.audit.api.ApiAuditBookingBean;
import com.ai.consts.ConstMap;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class AuditServiceImpl implements AuditService {

	protected Logger logger = LoggerFactory.getLogger(AuditServiceImpl.class);

	@Autowired
	private AuditDao auditorDao;

	@Autowired
	@Qualifier("serviceConfig")
	private ServiceConfig config;

	@Autowired
	@Qualifier("userService")
	private UserService userService;

	@Override
	public ApiCallResult getDraft(String userId, String draftId) {
		ApiCallResult result = auditorDao.getDraft(userId, draftId);
		return result;
	}

	@Override
	public ApiCallResult createDraft(String userId, String serviceType) {
		String companyId = "";
		String parentId = "";
		UserBean user = this.getUserBeanByUserId(userId);
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null)
				parentId = "";
			companyId = user.getCompany().getId();
		}
		ApiCallResult result = auditorDao.createDraft(userId, serviceType, companyId, parentId);
		return result;
	}

	@Override
	public ApiCallResult saveDraft(String userId, ApiAuditBookingBean draft) {
		String companyId = "";
		String parentId = "";
		UserBean user = this.getUserBeanByUserId(userId);
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null)
				parentId = "";
			companyId = user.getCompany().getId();
		}
		ApiCallResult result = auditorDao.saveDraft(userId, companyId, parentId, draft);
		return result;
	}

	@Override
	public ApiCallResult deleteDrafts(String userId, String draftIds) {
		String companyId = "";
		String parentId = "";
		UserBean user = this.getUserBeanByUserId(userId);
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null)
				parentId = "";
			companyId = user.getCompany().getId();
		}
		ApiCallResult result = auditorDao.deleteDrafts(userId, companyId, parentId, draftIds);
		return result;
	}

	@Override
	public ApiCallResult searchDrafts(String userId, String serviceType, String startDate, String endDate,
			String keyWord, int pageSize, int pageNo) {
		String companyId = "";
		String parentId = "";
		UserBean user = this.getUserBeanByUserId(userId);
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null)
				parentId = "";
			companyId = user.getCompany().getId();
		}
		try {
			if (!serviceType.isEmpty()) {
				serviceType = URLDecoder.decode(serviceType, "utf-8");
				serviceType = ConstMap.convertServiceType(serviceType, Consts.COMMA);
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("decoding service type: " + serviceType + "got error!");
		}
		ApiCallResult result = auditorDao.searchDrafts(userId, companyId, parentId, serviceType, startDate, endDate,
				keyWord, pageSize, pageNo);
		return result;
	}

	@Override
	public ApiCallResult searchOrders(String userId, String serviceType, String startDate, String endDate,
			String orderStatus, String keyWord, int pageSize, int pageNo) {
		String companyId = "";
		String parentId = "";
		UserBean user = this.getUserBeanByUserId(userId);
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null)
				parentId = "";
			companyId = user.getCompany().getId();
		}
		try {
			if (!serviceType.isEmpty()) {
				serviceType = URLDecoder.decode(serviceType, "utf-8");
				serviceType = ConstMap.convertServiceType(serviceType, Consts.COMMA);
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("decoding service type: " + serviceType + "got error!");
		}
		ApiCallResult result = auditorDao.searchOrders(userId, companyId, parentId, serviceType, startDate, endDate,
				orderStatus, keyWord, pageSize, pageNo);
		return result;
	}

	@Override
	public ApiCallResult createDraftFromPreviousOrder(String userId, String orderId, String serviceType) {
		String companyId = "";
		String parentId = "";
		UserBean user = this.getUserBeanByUserId(userId);
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null)
				parentId = "";
			companyId = user.getCompany().getId();
		}
		ApiCallResult result = auditorDao.createDraftFromPreviousOrder(userId, orderId, serviceType, companyId,
				parentId);
		return result;
	}

	@Override
	public ApiCallResult createOrderByDraft(String userId, String draftId) {
		String companyId = "";
		String parentId = "";
		UserBean user = this.getUserBeanByUserId(userId);
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null)
				parentId = "";
			companyId = user.getCompany().getId();
		}
		ApiCallResult result = auditorDao.createOrderByDraft(userId, draftId, companyId, parentId);
		return result;
	}

	@Override
	public ApiCallResult editOrder(String userId, String orderId) {
		String companyId = "";
		String parentId = "";
		UserBean user = this.getUserBeanByUserId(userId);
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null)
				parentId = "";
			companyId = user.getCompany().getId();
		}
		ApiCallResult result = auditorDao.editOrder(userId, orderId, companyId, parentId);
		return result;
	}

	@Override
	public ApiCallResult getOrderDetail(String userId, String orderId) {
		String companyId = "";
		String parentId = "";
		UserBean user = this.getUserBeanByUserId(userId);
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null)
				parentId = "";
			companyId = user.getCompany().getId();
		}
		ApiCallResult result = auditorDao.getOrderDetail(userId, orderId, companyId, parentId);
		return result;
	}

	@Override
	public ApiCallResult saveOrderByDraft(String userId, String draftId, String orderId) {
		String companyId = "";
		String parentId = "";
		UserBean user = this.getUserBeanByUserId(userId);
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null)
				parentId = "";
			companyId = user.getCompany().getId();
		}
		ApiCallResult result = auditorDao.saveOrderByDraft(userId, draftId, companyId, parentId);

		return result;
	}

	private UserBean getUserBeanByUserId(String userId) {
		UserBean user = null;
		try {
			user = userService.getCustById(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public ApiCallResult calculatePricing(String userId, String draftId, String employeeCount) {
		String companyId = "";
		String parentId = "";
		UserBean user = this.getUserBeanByUserId(userId);
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null)
				parentId = "";
			companyId = user.getCompany().getId();
		}
		ApiCallResult result = auditorDao.calculatePricing(userId, companyId, parentId, draftId, employeeCount);

		return result;
	}

	@Override
	public ApiCallResult reAudit(String userId, String draftId, String orderId) {
		String companyId = "";
		String parentId = "";
		UserBean user = this.getUserBeanByUserId(userId);
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null)
				parentId = "";
			companyId = user.getCompany().getId();
		}
		ApiCallResult result = auditorDao.reAudit(userId, companyId, parentId, draftId, orderId);

		return result;
	}

	@Override
	public ApiCallResult cancelOrder(String userId, String orderId, String reason, String reasonOption) {
		String companyId = "";
		String parentId = "";
		UserBean user = this.getUserBeanByUserId(userId);
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null)
				parentId = "";
			companyId = user.getCompany().getId();
		}
		ApiCallResult result = auditorDao.cancelOrder(userId, companyId, parentId, orderId, reason, reasonOption);

		return result;
	}

	@Override
	public ApiCallResult exportAuditReport(String userId) {
		String companyId = "";
		String parentId = "";
		UserBean user = this.getUserBeanByUserId(userId);
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null)
				parentId = "";
			companyId = user.getCompany().getId();
		}
		ApiCallResult result = new ApiCallResult();
		PageBean<AuditReportsSearchBean> bean = auditorDao.exportAuditReport(userId, companyId, parentId);
		if (null == bean) {
			result.setMessage("Error from psi AuditReportApiController");
		} else {
			try {
				String resultStr = bean.getPageItems().toString();
				List<AuditReportsSearchBean> list = JsonUtil.mapToObject(resultStr,
						new TypeReference<List<AuditReportsSearchBean>>() {
						});
				String fileStr = null;
				XSSFWorkbook wb = new XSSFWorkbook();
				InputStream inputStream = createExcleFile(wb, list, user.getLogin());
				if (inputStream != null) {
					byte[] data = IOUtils.toByteArray(inputStream);
					fileStr = Base64.encode(data);

					result.setContent(fileStr);
				}
			} catch (Exception e) {
				e.printStackTrace();
				result.setMessage("Error when creating excle file in api: " + e.toString());
			}
		}
		return result;
	}

	@Override
	public ApiCallResult supplierConfirmOrder(String orderId, String auditDate, String containReadyTime,
			ApiOrderFactoryBean orderFactoryBean) {
		return auditorDao.supplierConfirmOrder(orderId, auditDate, containReadyTime, orderFactoryBean);
	}

	private InputStream createExcleFile(XSSFWorkbook wb, List<AuditReportsSearchBean> list, String clientLogin)
			throws IOException {
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

		Sheet sheet = wb.createSheet("AI Audit Reports List");
		Row row = null;
		for (i = 0; i <= 10; i++) {
			row = sheet.createRow(i);
			for (int j = 0; j < 9; j++) {
				row.createCell(j).setCellStyle(tileCS);
			}
		}

		sheet.addMergedRegion(new CellRangeAddress(4, 4, 0, 23));

		String fileName = config.getExcleLoggoCommonSource() + File.separator + "logo.png";
		// String fileName = "e:" + File.separator + "logo.jpg";
		logger.info("Found the logo resource: " + fileName);
		InputStream is = new FileInputStream(fileName);
		byte[] bytes = IOUtils.toByteArray(is);

		int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);

		CreationHelper helper = wb.getCreationHelper();
		Drawing drawing = sheet.createDrawingPatriarch();
		ClientAnchor anchor = helper.createClientAnchor();

		anchor.setCol1(0);
		anchor.setRow1(0);

		Picture pict = drawing.createPicture(anchor, pictureIdx);
		pict.resize();

		row = sheet.getRow(4);
		Cell cell = row.createCell(0);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("List of Audit Reports");
		cell.setCellStyle(tileCS);

		row = sheet.getRow(6);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MMMM-dd", Locale.ENGLISH);
		cell = row.createCell(0);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Date: " + sf.format(new Date()));
		cell.setCellStyle(dateCS);

		row = sheet.getRow(7);
		cell = row.createCell(0);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue("Client login: " + clientLogin);
		cell.setCellStyle(dateCS);

		String[] title = new String[] { "Order Id", "Order Number", "Status", "Audit Type", "Sic Name", "ManDay",
				"If Key Account", "If Re-Inspection", "Client Name", "Frozen Date", "Date Confirmed", "Date Confirmed",
				"Inspectors Names List", "Number Of Workers", "Factory Name", "Charge", "Supplier Name", "Booking Date",
				"Client Reference", "Order Placer", "Ai Office", "Status Text", "Service Text", "Sub-Service Type" };
		row = sheet.createRow(10);
		for (int k = 0; k < title.length; k++) {
			cell = row.createCell(k);
			cell.setCellStyle(tableHeadeCS);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(title[k]);
			sheet.autoSizeColumn((short) k);
		}

		int rowid = 11;
		for (AuditReportsSearchBean each : list) {
			row = sheet.createRow(rowid);
			for (int cellid = 0; cellid < title.length; cellid++) {
				cell = row.createCell(cellid);
				cell.setCellStyle(tableCS);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				switch (cellid) {
				case 0:
					cell.setCellValue(each.getOrderId());
					break;
				case 1:
					cell.setCellValue(each.getOrderNumber());
					break;
				case 2:
					cell.setCellValue(each.getStatus());
					break;
				case 3:
					cell.setCellValue(each.getAuditType());
					break;
				case 4:
					cell.setCellValue(each.getSicName());
					break;
				case 5:
					cell.setCellValue(each.getManDay());
					break;
				case 6:
					cell.setCellValue(each.getIsKeyAccount());
					break;
				case 7:
					cell.setCellValue(each.getIsReInspection());
					break;
				case 8:
					cell.setCellValue(each.getClientName());
					break;
				case 9:
					cell.setCellValue(each.getFrozenDate());
					break;
				case 10:
					cell.setCellValue(each.getDateConfirmed());
					break;
				case 11:
					cell.setCellValue(each.getInspectorsNamesList().toString());
					break;
				case 12:
					cell.setCellValue(each.getNumberOfWorkers());
					break;
				case 13:
					cell.setCellValue(each.getFactoryName());
					break;
				case 14:
					cell.setCellValue(each.getCharge());
					break;
				case 15:
					cell.setCellValue(each.getSupplierName());
					break;
				case 16:
					cell.setCellValue(each.getBookingDate());
					break;
				case 17:
					cell.setCellValue(each.getClientReference());
					break;
				case 18:
					cell.setCellValue(each.getOrderPlacer());
					break;
				case 19:
					cell.setCellValue(each.getAiOffice());
					break;
				case 20:
					cell.setCellValue(each.getStatusText());
					break;
				case 21:
					cell.setCellValue(each.getServiceText());
					break;
				case 22:
					cell.setCellValue(each.getSubServiceType());
					break;
				}
			}
			rowid++;
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		wb.write(out);
		InputStream excelStream = new ByteArrayInputStream(out.toByteArray());
		out.close();

		return excelStream;
	}

}
