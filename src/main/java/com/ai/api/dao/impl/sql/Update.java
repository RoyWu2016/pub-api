package com.ai.api.dao.impl.sql;

/**
 * Created by KK on 5/11/2016.
 */
public class Update {
    public static final String User_Profile_Company = "UPDATE CRM_COMPANY "
            + "SET COMPANY_NAME_CN  = ?, " + "  INDUSTRY     = ?, " + "  COUNTRY_REGION  = ?, "
            + "ADDRESS1             = ?, " + "  CITY         = ?, " + "  POST_CODE       = ?"
            + "WHERE ID             = ?";


    public static final String User_Profile_Contact = "UPDATE CUST_CONTACT "
            + "SET MAIN_GENDER     = ?, " + "  MAIN_GIVEN_NAME  = ?, " + "  MAIN_FAMILY_NAME  = ?, "
            + "MAIN_MAILS          = ?, " + "  MAIN_TELEPHONE   = ?, " + "  MAIN_MOBILE       = ?,"
            + "MAIN_POSITION       = ?, " + "  ACCOUNT_GENDER   = ?, " + "  ACCOUNT_NAME      = ?,"
            + "ACCOUNT_GIVEN_NAME  = ?, " + "  ACCOUNT_EMAIL    = ?"
            + "WHERE CUST_ID       = ?";

    public static final String User_Profile_Booking_Preference = "UPDATE CRM_BOOKING_SETTINGS_BAK "
            + "SET SEND_SAMPLE_TO_FACTORY     = ?, " + "  PO_COMPULSORY      = ?,"
            + "IPC_PERCENTAGE     = ?, " + "  DUPRO_PERCENTAGE  = ?, " + "  PSI_PERCENTAGE     = ?,"
            + "CLC_PERCENTAGE     = ?, " + "  PM_PERCENTAGE     = ?, " + "  ALLOW_CHANGE_AQL   = ?,"
            + "CUST_SAMPLE_LEVEL  = ?, " + "  CUST_AQL_LEVEL    = ?, " + "  CRITICAL_DEFECTS   = ?,"
            + "MAJOR_DEFECTS      = ?, " + "  MINOR_DEFECTS     = ?, " + "  MAX_MEA_DEFECTS     = ?"
            + "WHERE ID  = ?";
}
