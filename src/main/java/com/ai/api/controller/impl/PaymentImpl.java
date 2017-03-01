package com.ai.api.controller.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ai.api.controller.Payment;
import com.ai.api.service.PaymentService;
import com.ai.api.service.UserService;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.PageBean;
import com.ai.commons.beans.PageParamBean;
import com.ai.commons.beans.payment.PaymentPaidBean;
import com.ai.commons.beans.payment.PaymentSearchResultBean;
import io.swagger.annotations.Api;
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

/***************************************************************************
 * <PRE>
 * Project Name    : Public-API
 * <p>
 * Package Name    : com.ai.api.controller.impl
 * <p>
 * Creation Date   : 2016/7/27 18:57
 * <p>
 * Author          : Jianxiong Cai
 * <p>
 * Purpose         : TODO
 * <p>
 * <p>
 * History         : TODO
 * <p>
 * </PRE>
 ***************************************************************************/

@RestController
@Api(tags = {"Payment"}, description = "Payment APIs")
public class PaymentImpl implements Payment {
	private static final Logger logger = LoggerFactory.getLogger(PaymentImpl.class);

	@Autowired
	UserService userService;

	@Autowired
	PaymentService paymentService;

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/payments", method = RequestMethod.GET)
	public ResponseEntity<PageBean<PaymentSearchResultBean>> getPaymentList(@PathVariable("userId") String userId,
			@RequestParam(value = "paid", required = false, defaultValue = "false") boolean paid,
			@RequestParam(value = "start", required = false, defaultValue = "") String start,
			@RequestParam(value = "end", required = false, defaultValue = "") String end,
			@RequestParam(value = "keyword", required = false, defaultValue = "") String keywords,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "pagesize", required = false, defaultValue = "20") Integer pagesize) {
		logger.info("get PaymentList----userId[" + userId + "] | paid[" + paid + "] | start[" + start + "] | end[" + end
				+ "] | keyword[" + keywords + "] | page[" + page + "]");
		Map<String, String[]> criterias = new HashMap<String, String[]>();
		List<String> orderItems = new ArrayList<String>();
		orderItems.add("inspectionDate");
		if (null != keywords && !"".equals(keywords)) {
			criterias.put("CLIENT_REFERENCE", new String[] { keywords });
		}
		if (!("".equals(start) && "".equals(end))) {
			String inspectionPeriod = start + " - " + end;
			criterias.put("INSPECTION_DATE", new String[] { inspectionPeriod });
		}

		PageParamBean criteriaBean = new PageParamBean();
		criteriaBean.setCriterias(criterias);
		criteriaBean.setOrderItems(orderItems);
		criteriaBean.setPageNo(page);
		criteriaBean.setPageSize(pagesize);
		try {
			PageBean<PaymentSearchResultBean> result = paymentService.searchPaymentList(criteriaBean, userId,
					paid ? "yes" : "no");
			if (null != result) {
				return new ResponseEntity<>(result, HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("getPaymentList error: ", e);
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// @Override
	// @TokenSecured
	// @RequestMapping(value = "/user/{userId}/proforma-invoice", method =
	// RequestMethod.POST)
	// public ResponseEntity<String>
	// createProformaInvoice(@PathVariable("userId") String userId,
	// @RequestParam("orders") String orders) {
	// String result = userService.createProformaInvoice(userId, orders);
	// if (result != null) {
	// return new ResponseEntity<>(result, HttpStatus.OK);
	// } else {
	// return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	// }
	// }

	// @Override
	// @TokenSecured
	// @RequestMapping(value = "/user/{userId}/proforma-invoice", method =
	// RequestMethod.PUT)
	// public ResponseEntity<Boolean>
	// reissueProFormaInvoice(@PathVariable("userId") String userId,
	// @RequestParam("orders") String orders) {
	// boolean result = userService.reissueProFormaInvoice(userId, orders);
	// if (result) {
	// return new ResponseEntity<>(HttpStatus.OK);
	// } else {
	// return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	// }
	// }

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/payment-type/{paymentType}/payment", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> generatePayment(@PathVariable("userId") String userId,
			@PathVariable("paymentType") String paymentType, @RequestParam("orderIds") String orderIds) {
		logger.info("invoke: " + "/user/" + userId + "/payment-type/" + paymentType + "payment?orderIds=" + orderIds);
		ApiCallResult result = new ApiCallResult();
		try {
			result = paymentService.generateGlobalPayment(userId, paymentType, orderIds);
			if (null == result.getMessage()) {
				return new ResponseEntity<>(result, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setMessage("Unknow Error: " + e.toString());
		}
		return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// @Override
	// @TokenSecured
	// @RequestMapping(value = "/user/{userId}/payment-log", method =
	// RequestMethod.POST)
	// public ResponseEntity<Boolean> logPaymentAction(@PathVariable("userId")
	// String userId,
	// @RequestBody PaymentActionLogBean logBean) {
	// boolean result = userService.logPaymentAction(userId, logBean);
	// if (result) {
	// return new ResponseEntity<>(HttpStatus.OK);
	// } else {
	// return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	// }
	// }

	// @Override
	// @TokenSecured
	// @RequestMapping(value =
	// "/user/{userId}/proforma-invoice/{invoiceId}/pdf", method =
	// RequestMethod.GET)
	// public ResponseEntity<String>
	// downloadProformaInvoicePDF(@PathVariable("userId") String userId,
	// @PathVariable("invoiceId") String invoiceId, HttpServletResponse
	// httpResponse) {
	// logger.info("downloadProformaInvoicePDF ... ");
	// logger.info("userId ：" + userId);
	// logger.info("invoiceId : " + invoiceId);
	// boolean b = paymentService.downloadProformaInvoicePDF(userId, invoiceId,
	// httpResponse);
	// if (b) {
	// return new ResponseEntity<>(HttpStatus.OK);
	// } else {
	// return new ResponseEntity<>("no invoice pdf file found",
	// HttpStatus.BAD_REQUEST);
	// }
	// }

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/payment", method = RequestMethod.PUT)
	public ResponseEntity<ApiCallResult> markAsPaid(@PathVariable("userId") String userId,
			@RequestBody PaymentPaidBean orders) {
		logger.info("invoke: " + "/user/" + userId + "/payment");
		ApiCallResult result = paymentService.markAsPaid(userId, orders);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/payment-history", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> findPaymentMarkAsPaidByUserId(@PathVariable("userId") String userId) {
		logger.info("invoke: " + "/user/" + userId + "/payment-history");
		ApiCallResult result = new ApiCallResult();
		try {
			result = paymentService.findPaymentMarkAsPaidByUserId(userId);
			if (null == result.getMessage()) {
				return new ResponseEntity<>(result, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result.setMessage("Unknow Error: " + e.toString());
		}
		return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// @Override
	// @TokenSecured
	// @RequestMapping(value = "/user/{userId}/paypal-payment", method =
	// RequestMethod.GET)
	// public ResponseEntity<List<PaypalInfoBean>>
	// getPaypalPayment(@PathVariable("userId") String userId,
	// @RequestParam("orders") String orders) {
	// List<PaypalInfoBean> result = paymentService.getPaypalPayment(userId,
	// orders);
	// if (result != null) {
	// return new ResponseEntity<>(result, HttpStatus.OK);
	// } else {
	// return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	// }
	// }
}
