package com.ai.api.dao;

import com.ai.api.exception.AIException;
import com.ai.commons.beans.customer.ContactBean;
import com.ai.commons.beans.customer.ExtraBean;
import com.ai.commons.beans.customer.GeneralUserViewBean;
import com.ai.commons.beans.customer.OrderBookingBean;
import com.ai.commons.beans.customer.OverviewBean;
import com.ai.commons.beans.customer.ProductFamilyBean;

public interface CustomerDao {

    /**
     * Gets the customer ID associated to the customer login given in parameter.
     *
     * @param login Customer login
     * @return Customer ID associated to the customer login given in parameter or an exception if
     * something wrong.
     * @throws AIException Database issue
     */
    String getCustomerIdByCustomerLogin(String login) throws AIException;


	GeneralUserViewBean getGeneralUserViewBean(String customer_id);

	OverviewBean getCompanyOverview(String customer_id);

	ContactBean getCompanyContact(String comp_id);

	OrderBookingBean getCompanyOrderBooking(String comp_id);

	ExtraBean getCompanyExtra(String comp_id);

	ProductFamilyBean getCompanyProductFamily(String comp_id);


}
