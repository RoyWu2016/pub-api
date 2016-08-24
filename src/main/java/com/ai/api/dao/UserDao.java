package com.ai.api.dao;

import com.ai.api.bean.UserForToken;
import com.ai.commons.beans.user.GeneralUserBean;

/**
 * Project Name    : Public-API
 * Package Name    : com.ai.api.dao
 * Creation Date   : 2016/8/23 17:58
 * Author          : Jianxiong.Cai
 * Purpose         : TODO
 * History         : TODO
 */
public interface UserDao  {
    GeneralUserBean getClientUser(String login);
    UserForToken getEmployeeUser(String login);
}
