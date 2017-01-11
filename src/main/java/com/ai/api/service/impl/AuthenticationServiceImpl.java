package com.ai.api.service.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ai.api.bean.UserForToken;
import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.SSOUserServiceDao;
import com.ai.api.dao.impl.TokenJWTDaoImpl;
import com.ai.api.service.AuthenticationService;
import com.ai.api.service.UserService;
import com.ai.commons.Consts;
import com.ai.commons.HttpUtil;
import com.ai.commons.IDGenerator;
import com.ai.commons.beans.LoginLogBean;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.user.GeneralUserBean;
import com.ai.commons.beans.user.TokenSession;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.jose4j.jwt.JwtClaims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    private final String ssoToken = "4d09a5553e772093a7fea071b54cc510";

//    @Autowired
//    private UserDao userDBDao;
//    @Autowired
//    private LoginLogDao loginLogDao;

    @Autowired
    private TokenJWTDaoImpl tokenJWTDao;

	@Autowired
	private SSOUserServiceDao ssoUserServiceDao;

	@Autowired
	private UserService userService;

    @Autowired
    @Qualifier("serviceConfig")
    private ServiceConfig config;


    @Override
    public ServiceCallResult userLogin(String userName, String password, String userType,HttpServletRequest request) {
        logger.info("userLogin ... userName:{}, userType:{}" ,userName,userType);
        ServiceCallResult result = new ServiceCallResult();
        if (userType.toLowerCase().equals(Consts.Http.USER_TYPE_CLIENT)){
	        //password should be in MD5 format
            String pwdMd5 = DigestUtils.shaHex(password);
            logger.info("getting client from DB ... userName:"+userName);
            GeneralUserBean client = null;
            try {
                StringBuilder url = new StringBuilder(config.getSsoUserServiceUrl())
                        .append("/auth/client-account-auth")
                        .append("?username=").append(userName)
                        .append("&pwd=").append(password);
                Map<String, String> headers = new HashMap<>();
                headers.put("service-call-token",ssoToken);
                ServiceCallResult httpResult = HttpUtil.issueGetRequest(url.toString(),headers);
                if (httpResult.getStatusCode() == HttpStatus.OK.value() && httpResult.getReasonPhase().equalsIgnoreCase("OK")) {
                    ApiCallResult callResult = JSON.parseObject(httpResult.getResponseString(), ApiCallResult.class);
                    client = JSON.toJavaObject((JSONObject)callResult.getContent(),GeneralUserBean.class);
                }
//                client = userDBDao.getClientUser(userName);
            }catch (Exception e){
                logger.error("can not get client!",e);
            }
            if (null!=client && null!=client.getUserId()) {
                if (password.equalsIgnoreCase(client.getPassword()) || pwdMd5.equalsIgnoreCase(client.getPassword())){
                    //Generate the token based on the User
                    TokenSession tokenSession = tokenJWTDao.generateToken(client.getLogin(), client.getUserId(),
                            IDGenerator.uuid(), userType);
                    if (tokenSession != null) {
                        String token = JSON.toJSONString(tokenSession);
                        result.setResponseString(token);
                        result.setStatusCode(HttpServletResponse.SC_OK);
                        result.setReasonPhase("User credential verified and token generated.");
                        try {
                            LoginLogBean loginLog = new LoginLogBean();
                            loginLog.setLogin(userName);
                            loginLog.setLoginType("Login");
                            loginLog.setLoginTime(new Date());
                            loginLog.setLoginServer("PublicAPI");
                            loginLog.setLoginIP(HttpUtil.getIpAddr(request));
                            StringBuilder url = new StringBuilder(config.getSsoUserServiceUrl())
                                    .append("/history/login-history");
                            ServiceCallResult httpResult = HttpUtil.issuePostRequest(url.toString(),null,loginLog);
                            if (httpResult.getStatusCode() == HttpStatus.OK.value() && httpResult.getReasonPhase().equalsIgnoreCase("OK")) {
                                ApiCallResult<Boolean> callResult = JSON.parseObject(httpResult.getResponseString(), ApiCallResult.class);
                                boolean b = callResult.getContent();
                                logger.info("add loginLog done! "+(b?"success!":"failed!"+callResult.getMessage()));
                            }
//                            int i =loginLogDao.addLog(loginLog);
//                            if (i>0){
//                                logger.info("add loginLog done! "+loginLog);
//                            }
                        }catch (Exception e){
                            logger.error("add loginLog failed.",e);
                        }
                    } else {
                        result.setResponseString("");
                        result.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        result.setReasonPhase("Error occurred while generating token.");
                    }
                }else {
                    logger.info("The username and password doesn't match! input-PW:{"+password+"},SHA-PW:{"+pwdMd5+"},DB-PW:{"+client.getPassword()+"}");
                    result.setResponseString("The username and password doesn't match");
                    result.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
                    result.setReasonPhase("The username and password doesn't match.");
                }
            }else {
                result.setResponseString("The user doesn't exist");
                result.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
                result.setReasonPhase("The user doesn't exist.");
            }
        }else if(userType.toLowerCase().equals(Consts.Http.USER_TYPE_EMPLOYEE)){
            logger.info("getting employee from DB ... userName:"+userName);
            UserForToken user = null;
            try {
                StringBuilder url = new StringBuilder(config.getSsoUserServiceUrl())
                        .append("/auth/employee-account-auth")
                        .append("?username=").append(userName)
                        .append("&pwd=").append(password);
                Map<String, String> headers = new HashMap<>();
                headers.put("service-call-token",ssoToken);
                ServiceCallResult httpResult = HttpUtil.issueGetRequest(url.toString(),headers);
                if (httpResult.getStatusCode() == HttpStatus.OK.value() && httpResult.getReasonPhase().equalsIgnoreCase("OK")) {
                    ApiCallResult callResult = JSON.parseObject(httpResult.getResponseString(), ApiCallResult.class);
                    user = JSON.toJavaObject((JSONObject)callResult.getContent(),UserForToken.class);
                }
//                user = userDBDao.getEmployeeUser(userName);
            }catch (Exception e){
                logger.error("can not get user!",e);
            }
//            logger.info("employee-----userId-[ "+user.getUserId()+"] pw-["+user.getPassword()+"]");
	        //password should be in MD5 format
            if (null!=user && null != user.getUserId() && password.equalsIgnoreCase(user.getPassword())){
                //Generate the token based on the User
                TokenSession tokenSession = tokenJWTDao.generateToken(user.getLogin(), user.getUserId(),
		                IDGenerator.uuid(), userType);
                if (tokenSession != null) {
                    result.setResponseString(JSON.toJSONString(tokenSession));
                    result.setStatusCode(HttpServletResponse.SC_OK);
                    result.setReasonPhase("User credential verified and token generated.");
                    try {
                        LoginLogBean loginLog = new LoginLogBean();
                        loginLog.setLogin(userName);
                        loginLog.setLoginType("EmployeeLogin");
                        loginLog.setLoginTime(new Date());
                        loginLog.setLoginServer("PublicAPI");
                        loginLog.setLoginIP(HttpUtil.getIpAddr(request));
                        StringBuilder url = new StringBuilder(config.getSsoUserServiceUrl())
                                .append("/history/login-history");
                        ServiceCallResult httpResult = HttpUtil.issuePostRequest(url.toString(),null,loginLog);
                        if (httpResult.getStatusCode() == HttpStatus.OK.value() && httpResult.getReasonPhase().equalsIgnoreCase("OK")) {
                            ApiCallResult<Boolean> callResult = JSON.parseObject(httpResult.getResponseString(), ApiCallResult.class);
                            boolean b = callResult.getContent();
                            logger.info("add loginLog done! "+(b?"success!":"failed!"+callResult.getMessage()));
                        }
//                        int i =loginLogDao.addLog(loginLog);
//                        if (i>0){
//                            logger.info("add loginLog done! "+loginLog);
//                        }
                    }catch (Exception e){
                        logger.error("add loginLog failed.",e);
                    }
                } else {
                    result.setResponseString("");
                    result.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    result.setReasonPhase("Error occurred while generating token.");
                }
            }else {
                result.setResponseString("The username and password doesn't match or user doesn't exist");
                result.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
                result.setReasonPhase("The username and password doesn't match or user doesn't exist.");
            }
        }else {
            logger.error("wrong user type got: " + userType);
            result.setResponseString("wrong user type!");
            result.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
            result.setReasonPhase("wrong user type!");
        }
        return result;
    }

	@Override
	public ServiceCallResult removeAPIToken(HttpServletRequest request, HttpServletResponse response) {
		String authorization = request.getHeader("authorization");
		String apiAccessToken = request.getHeader("ai-api-access-token");

		try {
			//check api access token in header
			ServiceCallResult result = ssoUserServiceDao.checkAccessHeader(apiAccessToken);
			if (result.getStatusCode() != HttpServletResponse.SC_OK) {
				return result;
			}

			//find the token in redis
			String token = ssoUserServiceDao.getToken(authorization, response);

			if (token != null) {
				JwtClaims claims = tokenJWTDao.getClaimsByJWT(token);
				//remove token session
				tokenJWTDao.removePublicAPIToken((String)claims.getClaimValue("sessId"));

				String userType = (String)claims.getClaimValue("userType");
				if (userType != null && userType.equals(Consts.Http.USER_TYPE_CLIENT)) {
					//if user is client
					userService.removeUserProfileCache((String)claims.getClaimValue("userId"));
				} else if (userType != null && userType.equals(Consts.Http.USER_TYPE_EMPLOYEE)) {
					//if user is employee
					userService.removeEmployeeProfileCache((String) claims.getClaimValue("userId"));
				} else {
					logger.error("wrong user type get from token or token is null: " + userType);
					logger.error("user/employee profile not removed, user id: " + claims.getClaimValue("userId"));
				}
				result.setStatusCode(HttpServletResponse.SC_OK);
				result.setReasonPhase("Token removed.");
				result.setResponseString("Token removed");
			} else {
				result.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
				result.setReasonPhase("Bad token.");
				result.setResponseString("Bad token.");
			}
			return result;
		} catch (Exception e) {
			logger.error("remove token error: " + ExceptionUtils.getFullStackTrace(e));
		}
		return null;
	}
}
