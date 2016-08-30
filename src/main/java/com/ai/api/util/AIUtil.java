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
import java.util.Date;
import java.util.Locale;

import com.ai.api.bean.InspectionDraftBean;
import com.ai.api.bean.InspectionDraftProductBean;
import com.ai.commons.beans.customer.CompanyEntireBean;
import com.ai.commons.beans.customer.ExtraBean;
import com.ai.commons.beans.customer.GeneralUserViewBean;
import com.ai.commons.beans.order.Draft;
import com.ai.commons.beans.order.draft.DraftOrder;
import com.ai.commons.beans.order.draft.DraftProduct;
import com.ai.commons.beans.order.draft.DraftProductInfo;
import com.ai.commons.beans.psi.InspectionOrderBookingBean;
import com.ai.commons.beans.psi.InspectionProductBookingBean;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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


	public static String getUserBusinessUnit(GeneralUserViewBean user, ExtraBean extra) {
		if (extra.getIsChb() != null && extra.getIsChb().equalsIgnoreCase("Yes")) {
			return "CHB";
		} else if (extra.getIsFI() != null && extra.getIsFI().equalsIgnoreCase("Yes")) {
			return "AFI";
		} else if (user.getSettingBean().getBusinessUnitText() != null &&
				user.getSettingBean().getBusinessUnitText().equals("AG")) {
			return "AG";
		} else {
			return "AI";
		}

	}

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
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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
//		draft.setCreateUnixTimestamp(d.getCreateTime());
//		draft.setUpdateUnixTimestamp(d.getUpdateTime());
		draft.setId(d.getDraftId());
//		draft.setInspectionDate(d.getInspectionDate());
		draft.setInspectionType(d.getInspectionType());
		draft.setOrderId(d.getOrderId());
		draft.setOrderInfo(mapper.readValue(d.getOrderInfo(), InspectionOrderBookingBean.class));
		draft.setParentCompanyId(d.getParentCompanyId());
		draft.setUserId(d.getUserId());

		for (DraftProduct psi : psiDraft.getPrdocutList()) {
			InspectionDraftProductBean p = new InspectionDraftProductBean();
			p.setDraftId(psi.getDraftId());
			p.setDraftProductId(psi.getDraftProductId());
			p.setOrderProductId(psi.getProductId());
//			p.setCreateUnixTimestamp(psi.getCreateTime());
			p.setPoNumber(psi.getPoNumber());
			p.setProductName(psi.getProductName());
			p.setReferenceNumber(psi.getReferenceNumber());
			p.setProgress(mapper.readValue(psi.getDraftProductInfo(), DraftProductInfo.class));
			p.setProductInfo(mapper.readValue(psi.getProductInfo(), InspectionProductBookingBean.class ));

			draft.getProducts().add(p);
		}
		return draft;
	}
}
