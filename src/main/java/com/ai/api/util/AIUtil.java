/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.util;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.ai.api.bean.InspectionDraftBean;
import com.ai.api.bean.InspectionDraftProductBean;
import com.ai.commons.beans.customer.CompanyEntireBean;
import com.ai.commons.beans.customer.ExtraBean;
import com.ai.commons.beans.order.Draft;
import com.ai.commons.beans.order.draft.DraftOrder;
import com.ai.commons.beans.order.draft.DraftProduct;
import com.ai.commons.beans.order.draft.DraftProductInfo;
import com.ai.commons.beans.psi.InspectionOrderBookingBean;
import com.ai.commons.beans.psi.InspectionProductBookingBean;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;


/***************************************************************************
 *<PRE>
 *  Project Name    : api
 *
 *  Package Name    : com.ai.api.util
 *
 *  File Name       : AIUtil.java
 *
 *  Creation Date   : May 25, 2016
 *
 *  Author          : Allen Zhang
 *
 *  Purpose         : TODO
 *
 *
 *  History         : TODO
 *
 *</PRE>
 ***************************************************************************/

public class AIUtil {

	protected static Logger logger = LoggerFactory.getLogger(AIUtil.class);


	public static String getCompanyBusinessUnit(CompanyEntireBean companyEntire, ExtraBean extra) {
		if (extra.getIsChb() != null && extra.getIsChb().equalsIgnoreCase("Yes")) {
			return "CHB";
		} else if (extra.getIsFI() != null && extra.getIsFI().equalsIgnoreCase("Yes")) {
			return "AFI";
		} else if (companyEntire.getOverview().getBusinessUnitText() != null &&
				companyEntire.getOverview().getBusinessUnitText().equals("AG")) {
			return "AG";
		} else {
			return "AI";
		}

	}

	public static String convertDateFormat(String input){
		try{
			SimpleDateFormat sdf = (input.matches(".*?[a-zA-Z]+.*?") == false) ? new SimpleDateFormat("yyyy-MM-dd", Locale.US) : new SimpleDateFormat("yyyy-MMMM-dd", Locale.US);
			Date inputDate = sdf.parse(input);
			DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
			return dateFormat.format(inputDate);
		} catch(Exception e){
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	public static InspectionDraftBean convertPSIDraftBeanToAPIDraftBean(Draft psiDraft) throws IOException {
		DraftOrder d = psiDraft.getDraftOrder();
		ObjectMapper mapper = new ObjectMapper();


		InspectionDraftBean draft = new InspectionDraftBean();
		draft.setCompanyId(d.getCompanyId());
		draft.setCompleted(d.getCompleted());
		draft.setCreateUnixTimestamp(d.getCreateTime().getTime());
		draft.setUpdateUnixTimestamp(d.getUpdateTime().getTime());
		draft.setId(d.getDraftId());
		draft.setInspectionDate(convertDateFormat(d.getInspectionDate()));
		draft.setInspectionType(d.getInspectionType());
		draft.setOrderId(d.getOrderId());
		draft.setOrderInfo(mapper.readValue(d.getOrderInfo(), InspectionOrderBookingBean.class));
		draft.setParentCompanyId(d.getParentCompanyId());
		draft.setUserId(d.getUserId());

		for (DraftProduct psi : psiDraft.getProductList()) {
			InspectionDraftProductBean p = new InspectionDraftProductBean();
			p.setDraftId(psi.getDraftId());
			p.setDraftProductId(psi.getDraftProductId());
			p.setOrderProductId(psi.getProductId());
			p.setCreateUnixTimestamp(psi.getCreateTime().getTime());
			p.setPoNumber(psi.getPoNumber());
			p.setProductName(psi.getProductName());
			p.setReferenceNumber(psi.getReferenceNumber());
			p.setProgress(mapper.readValue(psi.getDraftProductInfo(), DraftProductInfo.class));
			p.setProductInfo(mapper.readValue(psi.getProductInfo(), InspectionProductBookingBean.class ));

			draft.getProducts().add(p);
		}
		return draft;
	}

	public static Draft  convertAPIDraftBean2PSIDraftBean(InspectionDraftBean inspectionDraftBean) throws IOException {
		Draft draft = new Draft();
        DraftOrder order = new DraftOrder();
        order.setCompanyId(inspectionDraftBean.getCompanyId());
        order.setCompleted(inspectionDraftBean.getCompleted());
        order.setDraftId(inspectionDraftBean.getId());
        order.setInspectionType(inspectionDraftBean.getInspectionType());
        order.setOrderId(inspectionDraftBean.getOrderId());
        order.setOrderInfo(JSON.toJSONString(inspectionDraftBean.getOrderInfo()));
        order.setParentCompanyId(inspectionDraftBean.getParentCompanyId());
        order.setUserId(inspectionDraftBean.getUserId());
        draft.setDraftOrder(order);

        List<DraftProduct> productList = new ArrayList<>();
        for (InspectionDraftProductBean product:inspectionDraftBean.getProducts()){
            DraftProduct p = new DraftProduct();
            p.setDraftId(product.getDraftId());
            p.setDraftProductId(product.getDraftProductId());
            p.setProductId(product.getOrderProductId());
            p.setPoNumber(product.getPoNumber());
            p.setProductName(product.getProductName());
            p.setReferenceNumber(product.getReferenceNumber());
            p.setDraftProductInfo(JSON.toJSONString(product.getProgress()));
            p.setProductInfo(JSON.toJSONString(product.getProductInfo()));
            productList.add(p);
        }
        draft.setProductList(productList);
        return draft;
	}
	
	public static void addRestTemplateMessageConverter(RestTemplate restTemplate) {
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.add(new MappingJackson2HttpMessageConverter());
		restTemplate.setMessageConverters(messageConverters);		
	}
	
	public static void setMessageConverters(RestTemplate restTemplate) {
		restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
	    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
	}
}
