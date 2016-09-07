package com.ai.api.service.impl;

import javax.servlet.http.HttpServletResponse;

import com.ai.api.bean.UserForToken;
import com.ai.api.dao.UserDao;
import com.ai.api.dao.impl.TokenJWTDaoImpl;
import com.ai.api.service.AuthenticationService;
import com.ai.commons.IDGenerator;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.user.GeneralUserBean;
import com.ai.commons.beans.user.TokenSession;
import com.ai.userservice.common.util.MD5;
import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Project Name    : Public-API
 * Package Name    : com.ai.api.service.impl
 * Creation Date   : 2016/8/23 17:53
 * Author          : Jianxiong.Cai
 * Purpose         : TODO
 * History         : TODO
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    @Autowired
    private UserDao userDao;
    @Autowired
    private TokenJWTDaoImpl tokenJWTDao;

    @Override
    public ServiceCallResult userLogin(String userName, String password, String userType) {
        logger.info("userLogin ... userName: " +userName + ", userType:" + userType);
        ServiceCallResult result = new ServiceCallResult();
        if (userType.toLowerCase().equals("client")){
            String pwdMd5 = DigestUtils.shaHex(MD5.toMD5(password));
            logger.info("client-----pwdMd5 :"+pwdMd5);
            logger.info("getting client from DB ... userName:"+userName);
            GeneralUserBean client = userDao.getClientUser(userName);
//            logger.info("client-----userId-[ "+client.getUserId()+"] pw-["+client.getPassword()+"]");
            if (client != null && client.getUserId() != null && pwdMd5.equals(client.getPassword())) {
                //Generate the token based on the User
                TokenSession tokenSession = tokenJWTDao.generateToken(client.getLogin(), client.getUserId(), IDGenerator.uuid());
                if (tokenSession != null) {
                    String token = JSON.toJSONString(tokenSession);
                    result.setResponseString(token);
                    result.setStatusCode(HttpServletResponse.SC_OK);
                    result.setReasonPhase("User credential verified and token generated.");
                } else {
                    result.setResponseString("");
                    result.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    result.setReasonPhase("Error occurred while generating token.");
                }
            }else {
                result.setResponseString("The username and password doesn't match OR user not exist");
                result.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
                result.setReasonPhase("The username and password doesn't match OR user not exist.");
            }
        }else if(userType.toLowerCase().equals("employee")){
            String pwdMd5 = MD5.toMD5(password);
//            logger.info("employee-----pwdMd5 :"+pwdMd5);
            logger.info("getting employee from DB ... userName:"+userName);
            UserForToken user = userDao.getEmployeeUser(userName);
            logger.info("employee-----userId-[ "+user.getUserId()+"] pw-["+user.getPassword()+"]");
            if (null != user.getUserId() && pwdMd5.equals(user.getPassword())){
                //Generate the token based on the User
                TokenSession tokenSession = tokenJWTDao.generateToken(user.getLogin(), user.getUserId(), IDGenerator.uuid());
                if (tokenSession != null) {
                    result.setResponseString(JSON.toJSONString(tokenSession));
                    result.setStatusCode(HttpServletResponse.SC_OK);
                    result.setReasonPhase("User credential verified and token generated.");
                } else {
                    result.setResponseString("");
                    result.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    result.setReasonPhase("Error occurred while generating token.");
                }
            }else {
                result.setResponseString("The username and password doesn't match OR user not exist");
                result.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
                result.setReasonPhase("The username and password doesn't match OR user not exist.");
            }
        }else {
            logger.error("wrong user type got: " + userType);
            result.setResponseString("wrong user type!");
            result.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
            result.setReasonPhase("wrong user type!");
        }
        return result;
    }
}
