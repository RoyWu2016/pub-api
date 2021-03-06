package com.ai.api.service.impl;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.client.utils.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.aims.constants.CommonConstants;
import com.ai.aims.services.dto.order.OrderDTO;
import com.ai.aims.services.model.order.StyleInfo;
import com.ai.api.dao.CustomerDao;
import com.ai.api.service.APIEmailService;
import com.ai.api.service.LTEmailService;
import com.ai.commons.beans.customer.ContactBean;
import com.ai.commons.beans.email.EmailBean;

@Service
public class LTEmailServiceImpl extends APIEmailService implements LTEmailService {

	private final static Logger logger = Logger.getLogger(LTEmailServiceImpl.class);
	
	@Autowired
	private CustomerDao customerDAO;
	
	@Override
	public boolean sendEmailAddOrder(OrderDTO order,String userId) {
		try {
			ContactBean customerContact = customerDAO.getCustomerContact(userId);
			String[] mailTo = customerContact.getMainEmail().split(";");
			EmailBean mailBean = new EmailBean();
			mailBean.setMailTo(mailTo);
			Map<String, Object> params = new HashMap<String, Object>();
			String name = new StringBuilder(customerContact.getMainGender()).append(" ")
					.append(customerContact.getMainGivenName()).toString();
			params.put("orderId", order.getId());
			params.put("userFirstName", name);
			params.put("bookingDate", DateUtils.formatDate(order.getBookingDate(), CommonConstants.DATE_DD_MM_YYYY));
			params.put("mainDescription",order.getDescription());
			params.put("testLocation",order.getTestingLocation().getName());
			params.put("supplierName",order.getClient().getName());
			params.put("programName",order.getProgram().getProgramName());
			params.put("labOrderno",order.getLabOrderno());
			params.put("currentYear",Calendar.getInstance().get(Calendar.YEAR));
			return sendEmail("AIMS_ORDER_SUCCESS", mailBean, params);
		} catch (Exception e) {
			logger.error("Exception in sendEmailAddOrder :: ");
			logger.error(ExceptionUtils.getStackTrace(e));
			return false;
		}
	}
	
	@Override
	public boolean sendEmailWaitingForCancellation(OrderDTO order, String userId) {
		try {
			ContactBean customerContact = customerDAO.getCustomerContact(userId);
			String[] mailTo = customerContact.getMainEmail().split(";");
			EmailBean mailBean = new EmailBean();
			mailBean.setMailTo(mailTo);
			Map<String, Object> params = new HashMap<String, Object>();
			String name = new StringBuilder(customerContact.getMainGender()).append(" ")
					.append(customerContact.getMainGivenName()).toString();
			params.put("orderId", order.getId());
			params.put("userFirstName", name);
			params.put("labOrderno", null != order.getLabOrderno() ? order.getLabOrderno() : "");
			params.put("clientPONo", null != order.getClientPONo() ? 
					order.getClientPONo() : CommonConstants.DELIMITER_DASH);
			params.put("reportNo", null != order.getReportNo() ? 
					order.getReportNo() : CommonConstants.DELIMITER_DASH);
			params.put("mainDescription", null != order.getDescription() ? 
					order.getDescription() : CommonConstants.DELIMITER_DASH);
			params.put("submitter", null != order.getSupplier() ? 
					order.getSupplier().getName() : CommonConstants.DELIMITER_DASH);
			params.put("submittingFor", null != order.getClient() ? 
					order.getClient().getName() : CommonConstants.DELIMITER_DASH);
			params.put("sampleReceivedDate", null != order.getSampleReceivedDate() ? 
					DateUtils.formatDate(order.getSampleReceivedDate(), CommonConstants.DATE_DD_MM_YYYY) : CommonConstants.DELIMITER_DASH);
			params.put("cancelDate", null != order.getCancelDate() ? 
					DateUtils.formatDate(order.getCancelDate(), CommonConstants.DATE_DD_MM_YYYY) : CommonConstants.DELIMITER_DASH);
			params.put("cancelReason", null != order.getCancelReason() ? 
					order.getCancelReason() : CommonConstants.DELIMITER_DASH);
			params.put("cancelRemark", null != order.getCancelRemark() ? 
					order.getCancelRemark() : CommonConstants.DELIMITER_DASH);
			params.put("styleInfoList", null != order.getStyleInfo() ? 
					order.getStyleInfo() : new ArrayList<StyleInfo>(0));
			params.put("currentYear",Calendar.getInstance().get(Calendar.YEAR));
			return sendEmail("AIMS_ORDER_WAITING_CANCELLATION", mailBean, params);
		} catch (Exception e) {
			logger.error("Exception in sendEmailWaitingForCancellation :: ");
			logger.error(ExceptionUtils.getStackTrace(e));
			return false;
		}
	}

}
