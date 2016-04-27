package com.ai.api.dao.impl.db;

import com.ai.api.dao.CustomerDao;
import com.ai.api.exception.AIException;
import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.util.ArrayList;
import java.util.List;

import static com.ai.api.dao.impl.sql.Get.*;

public class UserDaoImpl extends JdbcDaoSupport implements CustomerDao {
    private static final Logger LOGGER = Logger.getLogger(UserDaoImpl.class);

    @Override
    public String getMasterAccountIdBySubAccountCompanyName(String companyName) throws AIException {
        try {
            return getJdbcTemplate().queryForObject(GET_MASTER_ID_BY_SUB_COMPANY_NAME,
                    new Object[]{companyName}, String.class);
        } catch (EmptyResultDataAccessException ee) {
            LOGGER.info("Master account not found for the company name " + companyName);
            return "";
        } catch (Exception e) {
            throw new AIException(UserDaoImpl.class, e.getMessage(), e);
        }
    }

    @Override
    public String getCustomerIdByCustomerLogin(String login) throws AIException {
        try {
            return getJdbcTemplate().queryForObject(GET_CUSTOMER_ID_BY_LOGIN, new Object[]{login},
                    String.class);
        } catch (EmptyResultDataAccessException ee) {
            LOGGER.info("Customer " + login + " not found");
            return "";
        } catch (Exception e) {
            throw new AIException(UserDaoImpl.class, e.getMessage(), e);
        }
    }

    @Override
    public List<String> getCustomerIdsByCompanyName(String companyName) throws AIException {
        try {
            return getJdbcTemplate().queryForList(GET_CUSTOMER_IDS_BY_COMPANY_NAME,
                    new Object[]{companyName}, String.class);
        } catch (EmptyResultDataAccessException ee) {
            LOGGER.info("Customer not found for the company name " + companyName);
            return new ArrayList<String>();
        } catch (Exception e) {
            throw new AIException(UserDaoImpl.class, e.getMessage(), e);
        }
    }

    @Override
    public List<String> getMobileDeviceIdsByLogin(String login) throws AIException {
        try {
            return getJdbcTemplate().queryForList(GET_MOBILE_DEVICE_IDS_BY_LOGIN, new Object[]{login},
                    String.class);
        } catch (EmptyResultDataAccessException ee) {
            LOGGER.info("Mobile IDs not found for the login " + login);
            return new ArrayList<String>();
        } catch (Exception e) {
            throw new AIException(UserDaoImpl.class, e.getMessage(), e);
        }
    }
}
