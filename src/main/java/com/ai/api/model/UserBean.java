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

    private String user_id;

    private String login;

    private String client_id;

    private String sales_in_charge;

    private String company_type;

    private String business_unit;

    private ContactBean contact;

    private CompanyInfoBean company_info;

    private ProductFamilyBean product_family;

    private OrderBookingBean order_booking;

    private MultiReferenceBookingBean multi_reference_booking;

    private ExtraBean extra;


    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getSales_in_charge() {
        return sales_in_charge;
    }

    public void setSales_in_charge(String sales_in_charge) {
        this.sales_in_charge = sales_in_charge;
    }

    public String getCompany_type() {
        return company_type;
    }

    public void setCompany_type(String company_type) {
        this.company_type = company_type;
    }

    public String getBusiness_unit() {
        return business_unit;
    }

    public void setBusiness_unit(String business_unit) {
        this.business_unit = business_unit;
    }

    public ContactBean getContact() {
        return contact;
    }

    public void setContact(ContactBean contact) {
        this.contact = contact;
    }

    public CompanyInfoBean getCompany_info() {
        return company_info;
    }

    public void setCompany_info(CompanyInfoBean company_info) {
        this.company_info = company_info;
    }

    public ProductFamilyBean getProduct_family() {
        return product_family;
    }

    public void setProduct_family(ProductFamilyBean product_family) {
        this.product_family = product_family;
    }

    public OrderBookingBean getOrder_booking() {
        return order_booking;
    }

    public void setOrder_booking(OrderBookingBean order_booking) {
        this.order_booking = order_booking;
    }

    public MultiReferenceBookingBean getMulti_reference_booking() {
        return multi_reference_booking;
    }

    public void setMulti_reference_booking(MultiReferenceBookingBean multi_reference_booking) {
        this.multi_reference_booking = multi_reference_booking;
    }

    public ExtraBean getExtra() {
        return extra;
    }

    public void setExtra(ExtraBean extra) {
        this.extra = extra;
    }
}
