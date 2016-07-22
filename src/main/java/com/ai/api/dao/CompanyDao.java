/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.dao;

import com.ai.commons.beans.customer.*;

/***************************************************************************
 *<PRE>
 *  Project Name    : api
 *
 *  Package Name    : com.ai.api.dao
 *
 *  File Name       : CompanyDao.java
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

public interface CompanyDao {

	CrmCompanyBean getCrmCompany(String compId);

	boolean updateCrmCompany(CrmCompanyBean company);

	OverviewBean getCompanyOverview(String compId);

	ContactBean getCompanyContact(String compId);

	boolean updateCompanyContact(String compId, ContactBean newContact);

	OrderBookingBean getCompanyOrderBooking(String compId);

	ExtraBean getCompanyExtra(String compId);

	QualityManualBean getCompanyQualityManual(String compId);

	boolean updateCompanyExtra(String compId, ExtraBean extra);

	boolean updateCompanyOrderBooking(String compId, OrderBookingBean booking);

	ProductFamilyBean getCompanyProductFamily(String compId);

	boolean updateCompanyProductFamily(String compId, ProductFamilyBean prodFamily);

	MultiRefBookingBean getCompanyMultiRefBooking(String compId);
}
