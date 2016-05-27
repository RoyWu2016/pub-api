package com.ai.api.dao.impl.db;

import org.apache.log4j.Logger;
//public class UserDaoImpl extends JdbcDaoSupport implements CustomerDao {
public class UserDaoImpl  {
    private static final Logger LOGGER = Logger.getLogger(UserDaoImpl.class);

//    @Override
//    public String getMasterAccountIdBySubAccountCompanyName(String companyName) throws AIException {
//        try {
//            return getJdbcTemplate().queryForObject(GET_MASTER_ID_BY_SUB_COMPANY_NAME,
//                    new Object[]{companyName}, String.class);
//        } catch (EmptyResultDataAccessException ee) {
//            LOGGER.info("Master account not found for the company name " + companyName);
//            return "";
//        } catch (Exception e) {
//            throw new AIException(UserDaoImpl.class, e.getMessage(), e);
//        }
//    }
//
//    @Override
//    public String getCustomerIdByCustomerLogin(String login) throws AIException {
//
//        try {
//            return getJdbcTemplate().queryForObject(GET_CUSTOMER_ID_BY_LOGIN, new Object[]{login},
//                    String.class);
//        } catch (EmptyResultDataAccessException ee) {
//            LOGGER.info("Customer " + login + " not found");
//            return "";
//        } catch (Exception e) {
//            throw new AIException(UserDaoImpl.class, e.getMessage(), e);
//        }
//    }
//
//    @Override
//    public List<String> getCustomerIdsByCompanyName(String companyName) throws AIException {
//        try {
//            return getJdbcTemplate().queryForList(GET_CUSTOMER_IDS_BY_COMPANY_NAME,
//                    new Object[]{companyName}, String.class);
//        } catch (EmptyResultDataAccessException ee) {
//            LOGGER.info("Customer not found for the company name " + companyName);
//            return new ArrayList<String>();
//        } catch (Exception e) {
//            throw new AIException(UserDaoImpl.class, e.getMessage(), e);
//        }
//    }
//
//    @Override
//    public List<String> getMobileDeviceIdsByLogin(String login) throws AIException {
//        try {
//            return getJdbcTemplate().queryForList(GET_MOBILE_DEVICE_IDS_BY_LOGIN, new Object[]{login},
//                    String.class);
//        } catch (EmptyResultDataAccessException ee) {
//            LOGGER.info("Mobile IDs not found for the login " + login);
//            return new ArrayList<String>();
//        } catch (Exception e) {
//            throw new AIException(UserDaoImpl.class, e.getMessage(), e);
//        }
//    }
////---------------------------- BY KK -------------
//
//    //---------------------------- Update Profile Company ----------------------
//
//    public void updateProfileCompany(CrmCompanyBean crmCompanyBean, String user_id) throws AIException {
//        try {
//
//            System.out.println("--DaoImpl---NameCN------" + crmCompanyBean.getCompanyNameCN());
//            System.out.println("-----Industry------" + crmCompanyBean.getIndustry());
//            System.out.println("-----CountryRegion------" + crmCompanyBean.getCountryRegion());
//            System.out.println("-----Address1------" + crmCompanyBean.getAddress1());
//            System.out.println("-----City------" + crmCompanyBean.getCity());
//            System.out.println("-----PostCode------" + crmCompanyBean.getPostCode());
//            System.out.println("-----ID-----" + user_id);
//
//            getJdbcTemplate().update(
//                    User_Profile_Company,
//                    new Object[]{crmCompanyBean.getCompanyNameCN(), crmCompanyBean.getIndustry(),
//                            crmCompanyBean.getCountryRegion(), crmCompanyBean.getAddress1(),
//                            crmCompanyBean.getCity(), crmCompanyBean.getPostCode(),
//                            user_id});
////            getJdbcTemplate().update(
////                    User_Profile_Company,
////                    new Object[]{"Prakash Mehta","Lue info services","Bihar", "Patna","Patna", "803201", "002F7C45A47FC2E3C1256F81006893B1"});
//
//        } catch (Exception e) {
//            throw new AIException(UserDaoImpl.class, e.getMessage(), e);
//        }
//    }
//
//    //---------------------------- Update Profile Contact----------------------
//
//    public void updateProfileContact(GeneralUserViewBean generalUserViewBean, ContactBean contactBean, String user_id) throws AIException {
//        try {
//
//            getJdbcTemplate().update(
//                    User_Profile_Contact,
//                    new Object[]{generalUserViewBean.getUser().getFollowName(), generalUserViewBean.getUser().getLastName(),
//                            generalUserViewBean.getUser().getFirstName(), generalUserViewBean.getUser().getPersonalEmail(),
//                            generalUserViewBean.getUser().getLandline(), generalUserViewBean.getUser().getMobile(),
//                            contactBean.getMainPosition(), contactBean.getAccountingGender(), contactBean.getAccountingName(),
//                            contactBean.getAccountingGivenName(), contactBean.getAccountingEmail(), user_id});
//        } catch (Exception e) {
//            throw new AIException(UserDaoImpl.class, e.getMessage(), e);
//        }
//
//    }
//
//    //---------------------------- Update Profile Booking Preference----------------------
//
//    public void updateBookingPreference(OrderBookingBean orderBookingBean, String user_id) throws AIException {
//        try {
//
//            getJdbcTemplate().update(
//                    User_Profile_Booking_Preference,
//                    new Object[]{orderBookingBean.getSendSampleToFactory(),
//                            orderBookingBean.getPoCompulsory(), orderBookingBean.getPsiPercentage(),
//                            orderBookingBean.getDuproPercentage(), orderBookingBean.getIpcPercentage(),
//                            orderBookingBean.getClcPercentage(), orderBookingBean.getPmPercentage(), orderBookingBean.getAllowChangeAql(),
//                            orderBookingBean.getCustomizedSampleLevel(), orderBookingBean.getCustAqlLevel(), orderBookingBean.getCriticalDefects(),
//                            orderBookingBean.getMajorDefects(), orderBookingBean.getMinorDefects(), orderBookingBean.getMaxMeaDefects(), user_id});
//        } catch (Exception e) {
//            throw new AIException(UserDaoImpl.class, e.getMessage(), e);
//        }
//    }
}
