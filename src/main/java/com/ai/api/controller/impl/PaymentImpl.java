package com.ai.api.controller.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.ai.api.controller.Payment;
import com.ai.api.service.PaymentService;
import com.ai.api.service.UserService;
import com.ai.commons.StringUtils;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.payment.GlobalPaymentInfoBean;
import com.ai.commons.beans.payment.PaymentSearchCriteriaBean;
import com.ai.commons.beans.payment.PaymentSearchResultBean;
import com.ai.commons.beans.payment.api.PaymentActionLogBean;
import com.ai.commons.beans.payment.api.PaymentItemParamBean;
import com.ai.commons.beans.payment.api.PaypalInfoBean;
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
public class PaymentImpl implements Payment {
	private static final Logger logger = LoggerFactory.getLogger(PaymentImpl.class);

	@Autowired
	UserService userService;

    @Autowired
    PaymentService paymentService;

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/payments", method = RequestMethod.GET)
	public ResponseEntity<List<PaymentSearchResultBean>> getPaymentList(@PathVariable("userId") String userId,
	                                                                    @RequestParam(value = "paid",required = false) String paid,
	                                                                    @RequestParam(value = "start",required = false) String start,
	                                                                    @RequestParam(value = "end",required = false) String end,
	                                                                    @RequestParam(value = "keyword",required = false) String keywords,
	                                                                    @RequestParam(value = "page",required = false) Integer page) {
		logger.info("get PaymentList----userId["+userId+"] | paid["+paid+"] | start["+start+"] | end["+end+"] | keyword["+keywords+"] | page["+page+"]");
		List<PaymentSearchResultBean> resultList = new ArrayList<>();
		boolean b = false;
		try {
			PaymentSearchCriteriaBean criteriaBean = new PaymentSearchCriteriaBean();
			criteriaBean.setPaid(false);
			try {
				if (!StringUtils.isBlank(paid) && paid.equals("true")) {
					criteriaBean.setPaid(true);
				}

				if (page == null) {
					page = 1;
				}
				criteriaBean.setPageNumber(page);
				criteriaBean.setKeywords(keywords);
				criteriaBean.setStartDate(start);
				criteriaBean.setEndDate(end);
				criteriaBean.setUserID(userId);

			} catch (Exception e) {
				logger.error("", e);
			}
			resultList = userService.searchPaymentList(criteriaBean);
			b=true;
		}catch (Exception e){
			logger.error("", e);
		}
		if (b){
			return new ResponseEntity<>(resultList, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/proforma-invoice", method = RequestMethod.POST)
	public ResponseEntity<String> createProformaInvoice(@PathVariable("userId") String userId,
																		@RequestParam("orders") String orders){
		String result = userService.createProformaInvoice(userId, orders);
		if(result!=null){
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/proforma-invoice", method = RequestMethod.PUT)
	public ResponseEntity<Boolean> reissueProFormaInvoice(@PathVariable("userId") String userId,
														@RequestParam("orders") String orders){
		boolean result = userService.reissueProFormaInvoice(userId, orders);
		if(result){
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/global-payment", method = RequestMethod.GET)
	public ResponseEntity<List<GlobalPaymentInfoBean>> generateGlobalPayment(@PathVariable("userId") String userId,
																			  @RequestParam("orders") String orders){
		List<GlobalPaymentInfoBean> result = userService.generateGlobalPayment(userId, orders);
		if(result!=null){
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/payment-log", method = RequestMethod.POST)
	public ResponseEntity<Boolean> logPaymentAction(@PathVariable("userId") String userId,
													@RequestBody PaymentActionLogBean logBean){
		boolean result = userService.logPaymentAction(userId, logBean);
		if(result){
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/proforma-invoice/{invoiceId}/pdf", method = RequestMethod.GET)
	public ResponseEntity<String> downloadProformaInvoicePDF(@PathVariable("userId") String userId,
															 @PathVariable("invoiceId") String invoiceId,
                                                             HttpServletResponse httpResponse){
        logger.info("downloadProformaInvoicePDF ... ");
        logger.info("userId ："+userId);
        logger.info("invoiceId : "+invoiceId);
		boolean b = paymentService.downloadProformaInvoicePDF(userId,invoiceId,httpResponse);
		if(b){
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>("no invoice pdf file found",HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/payment", method = RequestMethod.PUT)
	public ResponseEntity<Boolean> markAsPaid(@PathVariable("userId") String userId,
											  @RequestBody PaymentItemParamBean paymentItemParamBean){
		boolean result = paymentService.markAsPaid(userId, paymentItemParamBean);
		if(result){
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/paypal-payment", method = RequestMethod.GET)
	public ResponseEntity<List<PaypalInfoBean>> getPaypalPayment(@PathVariable("userId") String userId,
																	  @RequestParam("orders") String orders){
		List<PaypalInfoBean> result = paymentService.getPaypalPayment(userId, orders);
		if(result!=null){
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}
