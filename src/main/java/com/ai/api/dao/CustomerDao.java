package com.ai.api.dao;

import com.ai.api.exception.AIException;
import com.ai.commons.beans.customer.ContactBean;
import com.ai.commons.beans.customer.ExtraBean;
import com.ai.commons.beans.customer.GeneralUserViewBean;
import com.ai.commons.beans.customer.OrderBookingBean;
import com.ai.commons.beans.customer.OverviewBean;
import com.ai.commons.beans.customer.ProductFamilyBean;
import com.ai.commons.beans.customer.QualityManualBean;

public interface CustomerDao {

    String getCustomerIdByCustomerLogin(String login) throws AIException;

	GeneralUserViewBean getGeneralUserViewBean(String customer_id);

	OverviewBean getCompanyOverview(String customer_id);

	ContactBean getCompanyContact(String compId);

	OrderBookingBean getCompanyOrderBooking(String compId);

	ExtraBean getCompanyExtra(String compId);

	ProductFamilyBean getCompanyProductFamily(String compId);

	QualityManualBean getCompanyQualityManual(String compId);

}
