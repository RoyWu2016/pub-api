package com.ai.api.dao.impl;

import com.ai.api.bean.UserForToken;
import com.ai.api.dao.UserDao;
import com.ai.commons.beans.user.GeneralUserBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Project Name    : Public-API
 * Package Name    : com.ai.api.dao.impl
 * Creation Date   : 2016/8/23 18:31
 * Author          : Jianxiong.Cai
 * Purpose         : TODO
 * History         : TODO
 */
//@Component
public class UserDaoImpl extends JdbcDaoSupport implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    public GeneralUserBean getClientUser(String login) {
        String sql = "SELECT ID, LOGIN, PASSWORD FROM GENERAL_USER WHERE UPPER(LOGIN) = ?";
        login = login.toUpperCase();
        return getJdbcTemplate().queryForObject(sql, new Object[] { login },new GeneralUserBeanMapper());
    }

    @Override
    public UserForToken getEmployeeUser(String login) {
        String sql = "SELECT USER_ID,LOGIN,PASSWORD  FROM UR_USER  WHERE LOGIN = ?";
        return getJdbcTemplate().queryForObject(sql, new Object[] { login },new UserForTokenMapper());
    }





    private class GeneralUserBeanMapper implements RowMapper<GeneralUserBean> {
        @Override
        public GeneralUserBean mapRow(ResultSet rs, int rowNum) throws SQLException {
            logger.info("client row map to GeneralUserBean");
            GeneralUserBean user = new GeneralUserBean();
            user.setUserId(rs.getString("ID"));
            user.setLogin(rs.getString("LOGIN"));
            user.setPassword(rs.getString("PASSWORD"));
            logger.info("db-->bean!"+user);
            return user;
        }
    }
    private class UserForTokenMapper implements RowMapper<UserForToken>{
        @Override
        public UserForToken mapRow(ResultSet rs, int rowNum) throws SQLException {
            logger.info("employee row map to UserForToken");
            UserForToken user = new UserForToken();
            user.setUserId(rs.getString("USER_ID"));
            user.setLogin(rs.getString("LOGIN"));
            user.setPassword(rs.getString("PASSWORD"));
            logger.info("db-->bean!"+user);
            return user;
        }

    }
}
