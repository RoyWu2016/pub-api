package com.ai.api.controller.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.ai.api.controller.Payment;
import com.ai.api.service.UserService;
import com.ai.commons.DateUtils;
import com.ai.commons.StringUtils;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.payment.PaymentSearchCriteriaBean;
import com.ai.commons.beans.payment.PaymentSearchResultBean;
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
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/proformaInvoice", method = RequestMethod.POST)
	public ResponseEntity<String> createProformaInvoice(@PathVariable("userId") String userId,
																		@RequestParam("orders") String orders){
		String result = userService.createProformaInvoice(userId, orders);
		if(result!=null){
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}
