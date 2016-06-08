package com.ai.api.dao.impl.sql;

public final class Get {
//	public static final String GET_CUSTOMER_ID_BY_LOGIN = "SELECT ID FROM GENERAL_USER WHERE LOGIN =?";

//    public static final String GET_OVERVIEW = "SELECT * FROM CUST_PROFILE WHERE CUST_ID = ?";
//
//    public static final String GET_OVERVIEW_BY_LOGIN = "SELECT * FROM CUST_PROFILE WHERE UPPER(LOGIN) = ?";
//
//    public static final String GET_TOTAL_BY_LOGIN = "SELECT COUNT(LOGIN) FROM CUST_PROFILE WHERE UPPER(LOGIN) = UPPER(?) "
//            + "AND  UPPER(LOGIN) NOT LIKE '%CANCELLED%' ";
//
//    public static final String GET_TOTAL_BY_EMAIL = "SELECT COUNT(LOGIN) FROM CUST_PROFILE JOIN CUST_CONTACT "
//            + "ON CUST_PROFILE.CUST_ID = CUST_CONTACT.CUST_ID "
//            + "WHERE UPPER(LOGIN) NOT LIKE '%CANCELLED%' "
//            + "AND  (UPPER(MAIN_MAILS) = UPPER(?) "
//            + " OR UPPER(MAIN_MAILS) LIKE UPPER(?) "
//            + " OR UPPER(MAIN_MAILS) LIKE UPPER(?))";
//
//    public static final String GET_LEAD_QUALIFICATION = "SELECT * FROM CUST_LEAD_QUAL WHERE CUST_ID = ?";
//
//    public static final String GET_CONTACT = "SELECT * FROM CUST_CONTACT WHERE CUST_ID = ?";
//
//    public static final String GET_COMPANY_INFO = "SELECT * FROM CUST_CONTACT WHERE CUST_ID = ?";
//
//    public static final String GET_PRODUCT_FAMILY = "SELECT * FROM CUST_PROD_FAMILY_SETTINGS WHERE CUST_ID = ?";
//
//    public static final String GET_PRODUCT_FAMILY_INFO = "SELECT * FROM CUST_PROD_FAMILY_EXT_FIELDS "
//            + "WHERE CUST_ID = ? ORDER BY PROD_SEQ";
//
//    public static final String GET_RELEVANT_CATEGORY_INFO = "SELECT * FROM CUST_PROD_FAMILY_EXT_FIELDS_2 "
//            + "WHERE CUST_ID = ? ORDER BY FAVO_PROD_SEQ";
//
//    public static final String GET_ORDER_BOOKING = "SELECT * FROM CUST_ORDERBOOKING_SETTINGS WHERE CUST_ID = ?";
//
//    public static final String GET_FAVORITE_TESTS = "SELECT * FROM CUST_ORD_BKS_EXT_FIELDS "
//            + "WHERE CUST_ID = ? ORDER BY FAV_TEST_SEQ";
//
//    public static final String GET_MULTI_REF_BOOKING = "SELECT * FROM CUST_MULTI_REFBKS_SETTINGS WHERE CUST_ID = ?";
//
//    public static final String GET_REPORT_CERTIFICATE = "SELECT * FROM CUST_RPT_CERT_SETTINGS WHERE CUST_ID = ?";
//
//    public static final String GET_APPROVERS = "SELECT * FROM CUST_RPT_CERT_EXT_FIELDS_2 "
//            + "WHERE CUST_ID = ? ORDER BY APPROVER_SEQ";
//
//    public static final String GET_REJECT_CATEGORIES = "SELECT * FROM CUST_RPT_CERT_EXT_FIELDS "
//            + "WHERE CUST_ID = ? ORDER BY REJ_CATEGORY_SEQ";
//
//    public static final String GET_REJECT_CATEGORY_REASONS = "SELECT * FROM CUST_RPT_CERT_REJ_REASONS"
//            + " WHERE CUST_ID = ? AND REJ_CATEGORY_SEQ = ? ORDER BY REJ_REASON_SEQ";
//
//    public static final String GET_ONLINE_STORAGE = "SELECT * FROM CUST_OL_STORAGE_SETTINGS WHERE CUST_ID = ?";
//
//    public static final String GET_MASTER = "SELECT * FROM CUST_MASTER_ACC_SETTINGS WHERE CUST_ID = ?";
//
//    public static final String GET_MASTER_ID = "SELECT DISTINCT CUST_ID FROM CUST_MASTER_SUB_COMPANY "
//            + "WHERE UPPER(SUB_COMPANY) = UPPER(?)";
//
//    public static final String GET_KPI = "SELECT * FROM CUST_KPI_SETTINGS WHERE CUST_ID = ?";
//
//    public static final String GET_RATE = "SELECT * FROM CUST_INSP_AUDIT_RATE WHERE CUST_ID = ?";
//
//    public static final String GET_COUNTRY_PRICING_RATES = "SELECT * FROM CUST_INSP_AUDIT_EXT_FIELDS "
//            + "WHERE CUST_ID = ? ORDER BY COUNTRY_SEQ";
//
//    public static final String GET_LAB_TEST_RATE = "SELECT * FROM CUST_LAB_TEST_RATE WHERE CUST_ID = ?";
//
//    public static final String GET_LAB_TEST_SPECIAL_RATES = "SELECT * FROM CUST_LAB_TEST_EXT_FIELDS "
//            + "WHERE CUST_ID = ? ORDER BY TEST_SEQ";
//
//    public static final String GET_INVOICING = "SELECT * FROM CUST_INV_SETTINGS WHERE CUST_ID = ?";
//
//    public static final String GET_INVOICING_SAME_COMPANYNAME = "SELECT inv.* "
//            + " FROM CUST_INV_SETTINGS inv " + "LEFT OUTER JOIN CUST_PROFILE "
//            + "ON inv.CUST_ID    = CUST_PROFILE.CUST_ID "
//            + "WHERE inv.CUST_ID != ? AND CUST_PROFILE.COMPANY_NAME = ?";
//
//    public static final String GET_GENERAL_INSTRUCTION = "SELECT * FROM CUST_GENERAL_INFO WHERE CUST_ID = ?";
//
//    public static final String GET_MARKETING = "SELECT * FROM CUST_MARKETING_DATA WHERE CUST_ID = ?";
//
//    public static final String GET_HISTORY_LOG = "SELECT * FROM CUST_HISTORY WHERE CUST_ID = ? ORDER BY CREATE_TIME";
//
//    public static final String GET_MODIFIED = "SELECT * FROM CUST_MODIFIED "
//            + "WHERE SYNC = 'N' AND ROWNUM <= ? ORDER BY CREATE_TIME ASC";
//
//    public static final String GET_AUTO_CANCEL_PENDING_ORDER = "SELECT * FROM CUST_PENDING_ORDER_SETTINGS "
//            + "WHERE CUST_ID = ?";
//
//    public static final String GET_DASHBOARD = "SELECT * FROM CUST_DASHBOARD_SETTINGS WHERE CUST_ID = ?";
//
//    public static final String GET_QUALITY_MANUAL = "SELECT HAS_READ_QM, " + "  QM_READ_TIME, "
//            + "  QM_RELEASE_DATE, " + "  QM_DUE_DATE, " + "  QM_REMINDER_REL, " + "  QM_REMINDER_DUE, "
//            + "  QM_READ_HISTORY, " + "  QM_FILE_NAME, " + "  CUST_QUAL_MANUAL_SETTINGS.CREATE_TIME, "
//            + "  CUST_QUAL_MANUAL_SETTINGS.UPDATE_TIME " + "FROM CUST_QUAL_MANUAL_SETTINGS "
//            + "LEFT OUTER JOIN CUST_QM_FILE "
//            + "ON CUST_QUAL_MANUAL_SETTINGS.CUST_ID    = CUST_QM_FILE.CUST_ID "
//            + "WHERE CUST_QUAL_MANUAL_SETTINGS.CUST_ID = ?";
//
//    public static final String GET_SALES_COMMISSION = "SELECT * FROM CUST_SALES_COMMISSION WHERE CUST_ID = ?";
//
//    public static final String GET_QUALITY_MANUAL_FILE = "SELECT QM_FILE, QM_FILE_NAME FROM CUST_QM_FILE "
//            + "WHERE CUST_ID = ?";
//
//    public static final String GET_EXTRA = "SELECT * FROM CUST_PROFILE_EXT_FIELDS WHERE CUST_ID = ?";
//
//    public static final String GET_CUSTOMER_ID_BY_QUICKBOOKING_BY_COMPANY = "SELECT CUST_PROFILE.CUST_ID "
//            + "FROM CUST_PROFILE "
//            + "JOIN CUST_PROFILE_EXT_FIELDS "
//            + "ON CUST_PROFILE.CUST_ID = CUST_PROFILE_EXT_FIELDS.CUST_ID "
//            + "WHERE CUST_PROFILE_EXT_FIELDS.IS_DETAILED_BOOKING_FORM = ? AND CUST_PROFILE.COMPANY_NAME = ?";
//
//    public static final String GET_FOOD_LAB_DESTINATION = "SELECT * FROM CUST_AFI WHERE CUST_ID = ?";
//
//    public static final String GET_MASTER_ID_BY_SUB_COMPANY_NAME = "SELECT DISTINCT CUST_MASTER_ACC_SETTINGS.CUST_ID "
//            + "FROM CUST_MASTER_ACC_SETTINGS "
//            + "JOIN CUST_PROFILE "
//            + "ON CUST_MASTER_ACC_SETTINGS.CUST_ID = CUST_PROFILE.CUST_ID "
//            + "JOIN CUST_MASTER_SUB_COMPANY "
//            + "ON CUST_MASTER_SUB_COMPANY.CUST_ID = CUST_MASTER_ACC_SETTINGS.CUST_ID "
//            + "WHERE UPPER(CUST_PROFILE.LOGIN) NOT LIKE UPPER('%CANCELLED%') "
//            + "AND UPPER(CUST_MASTER_ACC_SETTINGS.IS_MASTER) = UPPER('Yes') "
//            + "AND UPPER(CUST_MASTER_SUB_COMPANY.SUB_COMPANY) = UPPER(?)";

//    public static final String GET_CUSTOMER_ID_BY_LOGIN = "SELECT CUST_PROFILE.CUST_ID "
//            + "FROM CUST_PROFILE " + "JOIN CUST_PROFILE_EXT_FIELDS "
//            + "ON CUST_PROFILE.CUST_ID = CUST_PROFILE_EXT_FIELDS.CUST_ID "
//            + "WHERE UPPER(IS_DELETED) = UPPER('No') " + "AND UPPER(LOGIN)        = UPPER(?)";



//    public static final String GET_MASTER_SUB_COMPANIES = "SELECT SUB_COMPANY FROM CUST_MASTER_SUB_COMPANY "
//            + "WHERE CUST_ID = ?";
//
//    public static final String GET_ONLINE_INQUIRY = "SELECT * FROM CUST_ONLINE_QUERY WHERE ONLINE_QUERY_ID = ?";
//
//    public static final String GET_ONLINE_INQUIRY_REPLIED_MESSAGE = "SELECT * FROM CUST_ONLINE_REPLIED_MESSAGE "
//            + "WHERE ONLINE_QUERY_ID = ?";
//
//    public static final String GET_ONLINE_INQUIRY_HISTORY = "SELECT * FROM CUST_ONLINE_QUERY_HISTORY "
//            + "WHERE ONLINE_QUERY_ID = ? ORDER BY CREATE_TIME";
//
//    public static final String GET_CLIENT_LOGIN_INFO = "SELECT COMPANY_NAME, " + "  LOGIN, "
//            + "  PASSWORD " + "FROM CUST_PROFILE " + "JOIN CUST_CONTACT "
//            + "ON CUST_PROFILE.CUST_ID = CUST_CONTACT.CUST_ID " + "WHERE CUST_CONTACT.MAIN_MAILS LIKE ? "
//            + "AND CUST_PROFILE.PASSWORD = ?";
//
//    public static final String GET_LOST_LOGIN_PASSWORD_INFO = "SELECT LOGIN, " + "  PASSWORD, "
//            + "  MAIN_MAILS, " + "  MAIN_GIVEN_NAME, " + "  MAIN_FAMILY_NAME " + "FROM CUST_PROFILE "
//            + "JOIN CUST_CONTACT " + "ON CUST_PROFILE.CUST_ID = CUST_CONTACT.CUST_ID "
//            + "JOIN CUST_PROFILE_EXT_FIELDS "
//            + "ON CUST_PROFILE.CUST_ID = CUST_PROFILE_EXT_FIELDS.CUST_ID "
//            + "WHERE UPPER(LOGIN) NOT LIKE '%CANCELLED%' " + "AND UPPER(IS_DELETED) = 'NO' "
//            + "AND  (UPPER(MAIN_MAILS) = UPPER(:email_1) "
//            + " OR UPPER(MAIN_MAILS) LIKE UPPER(:email_2) "
//            + " OR UPPER(MAIN_MAILS) LIKE UPPER(:email_3))";
//
//    public static final String GET_CUSTOMER_LOGIN_PASSWORD_INFO = "SELECT LOGIN, " + "  PASSWORD, "
//            + "  MAIN_MAILS, " + "  MAIN_GIVEN_NAME, " + "  MAIN_FAMILY_NAME " + "FROM CUST_PROFILE "
//            + "JOIN CUST_CONTACT " + "ON CUST_PROFILE.CUST_ID = CUST_CONTACT.CUST_ID "
//            + "JOIN CUST_PROFILE_EXT_FIELDS "
//            + "ON CUST_PROFILE.CUST_ID = CUST_PROFILE_EXT_FIELDS.CUST_ID "
//            + "WHERE UPPER(LOGIN) NOT LIKE '%CANCELLED%' " + "AND UPPER(IS_DELETED) = 'NO' "
//            + "AND CUST_PROFILE.CUST_ID = :customerId";
//
//    public static final String GET_LOGO_FILE = "SELECT LOGO, LOGO_NAME FROM CUST_COMP_LOGO WHERE CUST_ID = ?";
//
//    public static final String GET_CLIENT_UNIQUE_ID = "SELECT * FROM CUST_CLIENT_FRIEND_MAP WHERE CLIENT_ID = ?";
//
//    public static final String SEARCH_CLIENT_UNIQUE_ID = "SELECT * FROM CUST_CLIENT_FRIEND_MAP WHERE "
//            + "UPPER(COMPANY_NAME) LIKE UPPER(:keyword) ORDER BY CLIENT_ID ";
//
//    public static final String GET_CUSTOMER_IDS_BY_COMPANY_NAME = "SELECT CUST_PROFILE.CUST_ID "
//            + "FROM CUST_PROFILE " + "JOIN CUST_PROFILE_EXT_FIELDS "
//            + "ON CUST_PROFILE.CUST_ID              = CUST_PROFILE_EXT_FIELDS.CUST_ID "
//            + "WHERE UPPER(IS_DELETED)              = UPPER('No') "
//            + "AND UPPER(CUST_PROFILE.COMPANY_NAME) = UPPER(?)";
//
//    public static final String GET_KPI_CLIENT_INFO = "SELECT CUST_PROFILE.LOGIN, "
//            + "  CUST_PROFILE.PASSWORD, " + "  CUST_PROFILE.COMPANY_NAME " + "FROM CUST_PROFILE "
//            + "JOIN CUST_CONTACT " + "ON CUST_PROFILE.CUST_ID = CUST_CONTACT.CUST_ID "
//            + "JOIN CUST_PROFILE_EXT_FIELDS "
//            + "ON CUST_PROFILE.CUST_ID   = CUST_PROFILE_EXT_FIELDS.CUST_ID "
//            + "WHERE UPPER(IS_ACTIVATED) = UPPER('Yes') " + "AND UPPER(KPI_RPT_NEW)   != UPPER('No')";
//
//    public static final String GET_KPI_EXTRA = "SELECT * FROM CUST_KPI WHERE CUST_ID = ?";
//
//    public static final String GET_INVOICE_REMINDER = "SELECT * FROM CUST_INVOICE_REMINDER WHERE CUST_ID = ?";
//
//    public static final String GET_LAB_PROFILE = "SELECT * FROM LAB_PROFILE WHERE UPPER(LOGIN) = UPPER(?)";
//
//    public static final String GET_ALL_LAB_LOGINS = "SELECT LOGIN FROM LAB_PROFILE WHERE LOGIN IS NOT NULL";
//
//    public static final String GET_MOBILE_DEVICE_IDS_BY_LOGIN = "SELECT DEVICE_ID "
//            + "FROM CUST_PROFILE " + "JOIN CUST_PROFILE_EXT_FIELDS "
//            + "ON CUST_PROFILE.CUST_ID = CUST_PROFILE_EXT_FIELDS.CUST_ID " + "JOIN CUST_DEVICE_ID "
//            + "ON CUST_PROFILE.CUST_ID       = CUST_DEVICE_ID.CUST_ID "
//            + "WHERE UPPER(IS_DELETED)       = UPPER('No') " + "AND UPPER(CUST_PROFILE.LOGIN) = UPPER(?)";
//
//    public static final String GET_MOBILE_DEVICE_IDS = "SELECT DEVICE_ID FROM CUST_DEVICE_ID WHERE CUST_ID = ?";
//
//    public static final String GET_CUSTOMER_ALL_FEATURE = "SELECT A.*,B.FEATURE_CODE,B.FEATURE_NAME FROM CUST_FEATURE A LEFT JOIN GENERAL_FEATURE B ON A.FEATURE_ID = B.FEATURE_ID WHERE A.CUST_ID = ?";
//
//    public static final String GET_CUSTOMER_FEATURE = "SELECT A.*,B.FEATURE_CODE,B.FEATURE_NAME FROM CUST_FEATURE A LEFT JOIN GENERAL_FEATURE B ON A.FEATURE_ID = B.FEATURE_ID WHERE A.CUST_ID = ? AND B.FEATURE_CODE = ?";
//
//    public static final String GET_GENERAL_FEATURE = "SELECT * FROM GENERAL_FEATURE WHERE FEATURE_CODE = ?";
//
//    public static final String GET_LAB_ALL_COUNTRY = "SELECT DISTINCT COUNTRY from LAB_PROFILE WHERE COUNTRY IS NOT NULL";


    //-----------------------------------



}
