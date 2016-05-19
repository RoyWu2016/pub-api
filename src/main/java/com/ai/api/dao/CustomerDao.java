package com.ai.api.dao;

import com.ai.api.exception.AIException;
import com.ai.commons.beans.customer.ContactBean;
import com.ai.commons.beans.customer.GeneralUserViewBean;
import com.ai.commons.beans.customer.OrderBookingBean;

import java.util.List;

public interface CustomerDao {

    /**
     * Gets the customer ID of the master account associated to the sub account company name given in
     * parameter.
     *
     * @param companyName Sub account company name
     * @return Customer ID of the master account associated to the sub account company name given in
     * parameter or an exception if something wrong.
     * @throws AIException Database issue
     */
    public String getMasterAccountIdBySubAccountCompanyName(String companyName) throws AIException;

    /**
     * Gets the customer ID associated to the customer login given in parameter.
     *
     * @param login Customer login
     * @return Customer ID associated to the customer login given in parameter or an exception if
     * something wrong.
     * @throws AIException Database issue
     */
    public String getCustomerIdByCustomerLogin(String login) throws AIException;


    //------------------kk updateProfileCompany----------------------


    public void updateProfileCompany(GeneralUserViewBean generalUserViewBean, String user_id) throws AIException;

    public void updateProfileContact(GeneralUserViewBean generalUserViewBean, ContactBean contactBean, String user_id) throws AIException;

    public void updateBookingPreference(OrderBookingBean orderBookingBean, String user_id) throws AIException;


    /**
     * Gets the customer IDs associated to the customer company name given in parameter.
     *
     * @param companyName Customer company name
     * @return List of customer IDs associated to the customer company name given in parameter or an
     * exception if something wrong.
     * @throws AIException Database issue
     */
    public List<String> getCustomerIdsByCompanyName(String companyName) throws AIException;

    /**
     * Gets all mobile device IDs saved for a login given.
     *
     * @param login Customer login
     * @return List of mobile device IDs.
     * @throws AIException Database issue
     */
    public List<String> getMobileDeviceIdsByLogin(String login) throws AIException;

    //======================KK



}
