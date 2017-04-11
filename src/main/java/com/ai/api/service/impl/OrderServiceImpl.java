/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ai.api.bean.OrderSearchBean;
import com.ai.api.bean.UserBean;
import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.OrderDao;
import com.ai.api.exception.AIException;
import com.ai.api.service.OrderService;
import com.ai.api.service.UserService;
import com.ai.commons.JsonUtil;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.order.SimpleOrderSearchBean;
import com.ai.commons.beans.psi.InspectionBookingBean;
import com.ai.commons.beans.psi.ProductBean;
import com.fasterxml.jackson.core.type.TypeReference;

/***************************************************************************
 * <PRE>
 *  Project Name    : api
 *
 *  Package Name    : com.ai.api.service.impl
 *
 *  File Name       : OrderService.java
 *
 *  Creation Date   : Jul 13, 2016
 *
 *  Author          : Allen Zhang
 *
 *  Purpose         : TODO
 *
 *
 *  History         : TODO
 *
 * </PRE>
 ***************************************************************************/

@SuppressWarnings("rawtypes")
@Service
public class OrderServiceImpl implements OrderService {

	protected Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Autowired
	@Qualifier("orderDao")
	private OrderDao orderDao;

	@Autowired
	@Qualifier("serviceConfig")
	private ServiceConfig config;

	@Autowired
	@Qualifier("userService")
	private UserService userService;

	/*
	 * @Override public List<SimpleOrderBean>
	 * getOrdersByUserId(OrderSearchCriteriaBean criteria) {
	 * if(criteria.getLogin()==null){ String login =
	 * userService.getLoginByUserId(criteria.getUserID());//customerDao.
	 * getGeneralUser(criteria.getUserID()).getLogin();
	 * criteria.setLogin(login); } return orderDao.getOrdersByUserId(criteria);
	 * }
	 * 
	 * @Override public List<SimpleOrderBean>
	 * getDraftsByUserId(OrderSearchCriteriaBean criteria) {
	 * if(criteria.getLogin()==null){ String login =
	 * userService.getLoginByUserId(criteria.getUserID());//customerDao.
	 * getGeneralUser(criteria.getUserID()).getLogin();
	 * criteria.setLogin(login); } return orderDao.getDraftsByUserId(criteria);
	 * }
	 */

	@Override
	public Boolean cancelOrder(String userId, String orderId, String reason, String reason_options) {
		return orderDao.cancelOrder(userId, orderId, reason, reason_options);
	}

	@Override
	public InspectionBookingBean getOrderDetail(String userId, String orderId) {
		return orderDao.getOrderDetail(userId, orderId);
	}

	@Override
	public InspectionBookingBean createOrderByDraft(String userId, String draftId) {
		String companyId = "";
		String parentId = "";
		UserBean user = this.getUserBeanByUserId(userId);
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null)
				parentId = "";
			companyId = user.getCompany().getId();
		}
		return orderDao.createOrderByDraft(userId, draftId, companyId, parentId);
	}

	@Override
	public InspectionBookingBean editOrder(String userId, String orderId) {
		String companyId = "";
		String parentId = "";
		UserBean user = this.getUserBeanByUserId(userId);
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null)
				parentId = "";
			companyId = user.getCompany().getId();
		}
		return orderDao.editOrder(userId, orderId, companyId, parentId);
	}

	@Override
	public InspectionBookingBean saveOrderByDraft(String userId, String draftId) {
		String companyId = "";
		String parentId = "";
		UserBean user = this.getUserBeanByUserId(userId);
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null)
				parentId = "";
			companyId = user.getCompany().getId();
		}
		return orderDao.saveOrderByDraft(userId, draftId, companyId, parentId);
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
	public List<SimpleOrderSearchBean> searchOrders(String userId, String serviceType, String startDate, String endDate,
			String keyWord, String orderStatus, String pageSize, String pageNumber) throws IOException, AIException {
		String companyId = "";
		String parentId = "";
		UserBean user = userService.getCustById(userId);
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null)
				parentId = "";
			companyId = user.getCompany().getId();
		}
		return orderDao.searchOrders(userId, companyId, parentId, serviceType, startDate, endDate, keyWord, orderStatus,
				pageSize, pageNumber);
	}

	@Override
	public List<OrderSearchBean> searchLTOrders(String userId, String orderStatus, String pageSize,
			String pageNumber) throws IOException, AIException {
		String companyId = "";
		String parentId = "";
		UserBean user = userService.getCustById(userId);
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null)
				parentId = "";
			companyId = user.getCompany().getId();
		}
		return orderDao.searchLTOrders(companyId, orderStatus, pageSize, pageNumber, Sort.Direction.DESC.name().toLowerCase());
	}

	@Override
	public InputStream exportOrders(String userId, String serviceType, String startDate, String endDate,
			String orderStatus, String inspectionPeriod) throws IOException, AIException {
		// TODO Auto-generated method stub
		String companyId = "";
		String parentId = "";
		UserBean user = userService.getCustById(userId);
		String clientLogin = userService.getLoginByUserId(userId);
		if (null != user) {
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null)
				parentId = "";
			companyId = user.getCompany().getId();
		}
		String keyWord = "";
		String pageSize = "10000";// hard code in order to return all of the
									// orders in page 1;
		String pageNumber = "1";
		List<SimpleOrderSearchBean> list = orderDao.searchOrders(userId, companyId, parentId, serviceType, startDate,
				endDate, keyWord, orderStatus, pageSize, pageNumber);
		if (null == list) {
			logger.info("Order is not found from psi-service");
			return null;
		} else {
			XSSFWorkbook wb = new XSSFWorkbook();
			logger.info("Orders are found and begin to generate excle.");
			return createExcleFile(wb, list, clientLogin, inspectionPeriod);
		}
	}

	private InputStream createExcleFile(XSSFWorkbook wb, List<SimpleOrderSearchBean> list, String clientLogin,
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
		tableHeadeCS.setWrapText(true);

		CellStyle tableCS = wb.createCellStyle();
		tableCS.setAlignment(CellStyle.ALIGN_CENTER);
		tableCS.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		tableCS.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		tableCS.setBorderLeft(HSSFCellStyle.BORDER_MEDIUM);
		tableCS.setBorderRight(HSSFCellStyle.BORDER_MEDIUM);
		tableCS.setBorderTop(HSSFCellStyle.BORDER_MEDIUM);
		tableCS.setWrapText(true);

		Sheet sheet = wb.createSheet("AI Orders List (" + inspectionPeriod + ")");
		Row row = null;
		for (i = 0; i <= 10; i++) {
			row = sheet.createRow(i);
			for (int j = 0; j < 12; j++) {
				row.createCell(j).setCellStyle(tileCS);
			}
		}

		sheet.addMergedRegion(new CellRangeAddress(4, 4, 0, 8));

		String fileName = config.getExcleLoggoCommonSource() + File.separator + "logo.png";
		// String fileName = "E:" + File.separator + "logo.png";
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
		cell.setCellValue("List of Orders from " + inspectionPeriod);
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

		String[] title = new String[] { "Type", "Product Name", "P/O Number", "Product Quantity", "Product Reference",
				"Report expected on", "Factory Name", "Status", "AI Rerence", "Samples Collection(Lab Testing)",
				"Samples Collection(Onward Shipment)", "Status of samplereceived?" };
		row = sheet.createRow(10);
		for (int k = 0; k < title.length; k++) {
			cell = row.createCell(k);
			cell.setCellStyle(tableHeadeCS);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(title[k]);
			// sheet.autoSizeColumn((short) k);
			sheet.setDefaultColumnWidth(15);
		}

		int rowid = 11;
		String resultStr = JsonUtil.mapToJson(list);
		List<SimpleOrderSearchBean> tempList = JsonUtil.mapToObject(resultStr,
				new TypeReference<List<SimpleOrderSearchBean>>() {
				});
		for (SimpleOrderSearchBean each : tempList) {
			row = sheet.createRow(rowid);
			for (int cellid = 0; cellid < title.length; cellid++) {
				cell = row.createCell(cellid);
				cell.setCellStyle(tableCS);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				switch (cellid) {
				case 0:
					cell.setCellValue(each.getServiceTypeText());
					break;
				case 1:
					cell.setCellValue(each.getProductNames());
					break;
				case 2:
					cell.setCellValue(each.getPoNumbers());
					break;
				case 3:
					cell.setCellValue(each.getClientReference());
					break;
				case 4:
					cell.setCellValue(each.getClientReference());
					break;
				case 5:
					cell.setCellValue(each.getBookingDate());
					break;
				case 6:
					cell.setCellValue(each.getSupplierName());
					break;
				case 7:
					cell.setCellValue(each.getStatusText());
					break;
				case 9:
					cell.setCellValue(each.getRefNumber());
					break;
				case 10:
					cell.setCellValue(each.getIsSupplierConfirmed());
					break;
				case 11:
					cell.setCellValue(each.getIsSupplierConfirmed());
					break;
				case 12:
					cell.setCellValue(each.getIsSupplierConfirmed());
					break;
				}
			}
			rowid++;
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		wb.write(out);
		InputStream excelStream = new ByteArrayInputStream(out.toByteArray());
		out.flush();
		out.close();

		return excelStream;
	}

	@Override
	public ApiCallResult getOrderActionEdit(String orderId) {
		// TODO Auto-generated method stub
		return orderDao.getOrderActionEdit(orderId);
	}

	@Override
	public ApiCallResult getOrderActionCancel(String orderId) {
		// TODO Auto-generated method stub
		return orderDao.getOrderActionCancel(orderId);
	}

	@Override
	public ApiCallResult getOrderPrice(String userId,String orderId){

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
	    return orderDao.getOrderPrice(userId,companyId,parentId,orderId);
    }

	@Override
	public InspectionBookingBean getInspectionOrder(String string, String orderId) {
		// TODO Auto-generated method stub
		return orderDao.getInspectionOrder(string,orderId);
	}

	@Override
	public ApiCallResult reInspection(String userId, String orderId, String draftId) {
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
        return orderDao.reInspection(userId,companyId,parentId,orderId,draftId);
	}

	@Override
	public List<ProductBean> listProducts(String orderId) {
		// TODO Auto-generated method stub
		return orderDao.listProducts(orderId);
	}

}
