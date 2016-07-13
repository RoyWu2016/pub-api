/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.util;

import com.ai.commons.beans.customer.ExtraBean;
import com.ai.commons.beans.customer.GeneralUserViewBean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
	public static String getUserBusinessUnit(GeneralUserViewBean user, ExtraBean extra) {
		if (extra.getIsChb().equalsIgnoreCase("Yes")) {
			return "CHB";
		} else if (extra.getIsFI().equalsIgnoreCase("Yes")) {
			return "AFI";
		} else if (user.getSettingBean().getBusinessUnitText().equals("AG")) {
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
			e.printStackTrace();
		}
		return null;
	}
}
