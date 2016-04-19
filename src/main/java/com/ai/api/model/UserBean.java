/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.model;

import com.ai.api.bean.*;

/***************************************************************************
 * <PRE>
 * Project Name    : api
 * <p>
 * Package Name    : com.ai.api.model
 * <p>
 * File Name       : UserBean.java
 * <p>
 * Creation Date   : Mar 16, 2016
 * <p>
 * Author          : Allen Zhang
 * <p>
 * Purpose         : TODO
 * <p>
 * <p>
 * History         : TODO
 * <p>
 * </PRE>
 ***************************************************************************/

public class UserBean {

    private String userId;

    private String login;

    private String clientId;

    private String salesInCharge;

    private String companyType;

    private String businessUnit;

    private ContactBean contact;

    private CompanyInfoBean companyInfo;

    private ProductFamilyBean productFamily;

    private OrderBookingBean orderBooking;

    private MultiReferenceBookingBean multiReferenceBooking;

    private ExtraBean extra;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSalesInCharge() {
        return salesInCharge;
    }

    public void setSalesInCharge(String salesInCharge) {
        this.salesInCharge = salesInCharge;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public String getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
    }

    public ContactBean getContact() {
        return contact;
    }

    public void setContact(ContactBean contact) {
        this.contact = contact;
    }

    public CompanyInfoBean getCompanyInfo() {
        return companyInfo;
    }

    public void setCompanyInfo(CompanyInfoBean companyInfo) {
        this.companyInfo = companyInfo;
    }

    public ProductFamilyBean getProductFamily() {
        return productFamily;
    }

    public void setProductFamily(ProductFamilyBean productFamily) {
        this.productFamily = productFamily;
    }

    public OrderBookingBean getOrderBooking() {
        return orderBooking;
    }

    public void setOrderBooking(OrderBookingBean orderBooking) {
        this.orderBooking = orderBooking;
    }

    public MultiReferenceBookingBean getMultiReferenceBooking() {
        return multiReferenceBooking;
    }

    public void setMultiReferenceBooking(MultiReferenceBookingBean multiReferenceBooking) {
        this.multiReferenceBooking = multiReferenceBooking;
    }

    public ExtraBean getExtra() {
        return extra;
    }

    public void setExtra(ExtraBean extra) {
        this.extra = extra;
    }
}
