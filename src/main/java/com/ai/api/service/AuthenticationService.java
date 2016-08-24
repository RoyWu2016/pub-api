package com.ai.api.service;

import com.ai.commons.beans.ServiceCallResult;

/**
 * Project Name    : Public-API
 * Package Name    : com.ai.api.service
 * Creation Date   : 2016/8/23 17:48
 * Author          : Jianxiong.Cai
 * Purpose         : TODO
 * History         : TODO
 */
public interface AuthenticationService {

    ServiceCallResult userLogin(String userName,String password,String userType);
}
