package com.ai.api.dao.impl;

import com.ai.api.dao.LoginLogDao;
import com.ai.commons.beans.LoginLogBean;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * Project Name    : Public-API
 * Package Name    : com.ai.api.dao.impl
 * Creation Date   : 2016/12/16 10:37
 * Author          : Jianxiong.Cai
 * Purpose         : TODO
 * History         : TODO
 */
public class LoginLogDaoImpl extends JdbcDaoSupport implements LoginLogDao {

    @Override
    public int addLog(LoginLogBean loginLog) {
        String sql = "INSERT INTO LOG_TRANSACTION " +
                "( LOGIN, ACTION_TYPE, ACTION_TIME, LOGIN_SERVER, IP_ADDRESS ) " +
                "VALUES ( ?, ?, ?, ?, ?)";
        return getJdbcTemplate().update(sql,loginLog.getLogin(),loginLog.getLoginType(),
                loginLog.getLoginTime(),loginLog.getLoginServer(),loginLog.getLoginIP());


    }
}
