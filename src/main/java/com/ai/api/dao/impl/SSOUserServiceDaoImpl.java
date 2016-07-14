/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.dao.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ai.api.bean.UserForToken;
import com.ai.api.dao.SSOUserServiceDao;
import com.ai.api.config.ServiceConfig;
import com.ai.commons.Consts;
import com.ai.commons.HttpUtil;
import com.ai.commons.IDGenerator;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.user.GeneralUserBean;
import com.ai.commons.beans.user.TokenSession;
import com.ai.userservice.common.util.MD5;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/***************************************************************************
 *<PRE>
 *  Project Name    : api
 *
 *  Package Name    : com.ai.api.dao.impl
 *
 *  File Name       : SSOUserServiceDaoImpl.java
 *
 *  Creation Date   : Jun 07, 2016
 *
 *  Author          : Allen Zhang
 *
 *  Purpose         : TODO
 *
 *
 *  History         : TODO
 *
 *</PRE>
 ***************************************************************************/

public class SSOUserServiceDaoImpl implements SSOUserServiceDao {

	private static final Logger LOGGER = Logger.getLogger(SSOUserServiceDaoImpl.class);
	private final ObjectMapper mapper = new ObjectMapper();

	@Autowired
	@Qualifier("serviceConfig")
	private ServiceConfig config;

    @Autowired
//    @Qualifier("tokenJWTDao")
    private TokenJWTDaoImpl tokenJWTDao;

	@Override
	public ServiceCallResult userLogin(final String username, final String password,
	                                   final String userType, final String accessToken){
		String clientUrl = config.getSsoUserServiceUrl()+"/user/client/"+username;
        String userUrl = config.getSsoUserServiceUrl()+"/user/"+username+"/user";
		Map<String, String> obj = new HashMap<>();
		obj.put("username", username);
		obj.put("password", password);
		obj.put("userType", userType);
		obj.put(Consts.Http.PUBLIC_API_ACCESS_TOKEN_HEADER, accessToken);
		try {
            ServiceCallResult result = this.checkAccessHeader(accessToken);
            if (result.getStatusCode() != HttpServletResponse.SC_OK) {
			    return result;
            }
            //Hash the password, then check if it's the same value in the DB
            if (userType.toLowerCase().equals("client")) {
                String pwdMd5 = DigestUtils.shaHex(MD5.toMD5(password));
                String clientStr = HttpUtil.issueGetRequest(clientUrl,obj).getResponseString();
                LOGGER.info("getClientAccountByUserName responseStr "+clientStr);
                GeneralUserBean client = JSON.parseObject(clientStr,GeneralUserBean.class);
                if (client != null && client.getUserId() != null && pwdMd5.equals(client.getPassword())) {
                    //Generate the token based on the User
	                TokenSession tokenSession = tokenJWTDao.generateToken(client.getLogin(),client.getUserId(), IDGenerator.uuid());
                    String token = JSON.toJSONString(tokenSession);
                    if (token != null && !token.isEmpty()) {
                        result.setResponseString(token);
                        result.setStatusCode(HttpServletResponse.SC_OK);
                        result.setReasonPhase("User credential verified and token generated.");
                    } else {
                        result.setResponseString("");
                        result.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        result.setReasonPhase("Error occurred while generating token.");
                    }
                } else {
                    result.setResponseString("");
                    result.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
                    result.setReasonPhase("The username and password doesn't match.");
                }
            } else if (userType.toLowerCase().equals("employee")) {
                String userStr = HttpUtil.issueGetRequest(userUrl,obj).getResponseString();
                LOGGER.info("getUserByUserName responseStr "+userStr);
                UserForToken user = JSON.parseObject(userStr,UserForToken.class);
                boolean checkPasswordUsername = (null!=user);
                if (checkPasswordUsername){
                    //Generate the token based on the User
	                TokenSession tokenSession = tokenJWTDao.generateToken(user.getLogin(),user.getUserId(),IDGenerator.uuid());
	                String token = JSON.toJSONString(tokenSession);
                    if (token != null) {
                        result.setResponseString(token);
                        result.setStatusCode(HttpServletResponse.SC_OK);
                        result.setReasonPhase("User credential verified and token generated.");
                    } else {
                        result.setResponseString("");
                        result.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        result.setReasonPhase("Error occurred while generating token.");
                    }
                } else {
                    result.setResponseString("");
                    result.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
                    result.setReasonPhase("The username and password doesn't match.");
                }
            }else {
                LOGGER.fatal("wrong user type got: " + userType);
                result.setResponseString("wrong user type!");
                result.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
                result.setReasonPhase("wrong user type!");
            }
            return result;

		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}

		return null;
	}

	@Override
	public ServiceCallResult refreshAPIToken(Map<String, String> data, HttpServletRequest request,
	                                         HttpServletResponse response) {
		Map<String, String> headers = new HashMap<>();
        String accessToken = request.getHeader("ai-api-access-token");
        String authorization = request.getHeader("authorization");
		headers.put("authorization", authorization);
		headers.put("ai-api-access-token", accessToken);
		headers.put("ai-api-refresh-key", request.getHeader("ai-api-refresh-key"));

		try {
            ServiceCallResult result = this.checkAccessHeader(accessToken);
            if (result.getStatusCode() != HttpServletResponse.SC_OK) {
                return result;
            }
            String username = data.get("username");
            if (username == null || username.isEmpty()) {
                result.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
                result.setReasonPhase("User name is empty.");
                result.setResponseString("Please send username filed in the reqeust.");
                return result;
            }
			if (!HttpUtil.validateRefreshTokenKey(request)) {

				result.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
				result.setReasonPhase("Refresh key invalid.");
				result.setResponseString("Please check your token refresh key.");
				return result;
			}

            String jwt = this.getToken(authorization, response);

            if (jwt != null) {
	            TokenSession oldToken = tokenJWTDao.getTokenSessionFromRedis(tokenJWTDao.getTokenId(jwt));
	            TokenSession tokenSession = tokenJWTDao.generateToken(username,oldToken.getUserId(),oldToken.getId());
                String resultJWT = JSON.toJSONString(tokenSession);
                if (resultJWT.isEmpty()) {
                    //not valid
                    result.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
                    result.setReasonPhase("Bad token to refresh");
                    result.setResponseString("Bad token to refresh");
                } else {
                    //valid
                    result.setStatusCode(HttpServletResponse.SC_OK);
                    result.setReasonPhase("Token refreshed");
                    result.setResponseString(resultJWT);
                }
            } else {
                result.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
                result.setReasonPhase("No token found");
                result.setResponseString("");
            }
            return result;
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public ServiceCallResult removeAPIToken(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> headers = new HashMap<>();
		String authorization = request.getHeader("authorization");
		String apiAccessToken = request.getHeader("ai-api-access-token");
		headers.put("authorization", authorization);
		headers.put("ai-api-access-token", apiAccessToken);
		headers.put("ai-api-refresh-key", request.getHeader("ai-api-refresh-key"));
		try {
			//check api access token in header
			ServiceCallResult result = checkAccessHeader(apiAccessToken);
			if (result.getStatusCode() != HttpServletResponse.SC_OK) {
				return result;
			}

			if (!HttpUtil.validateRefreshTokenKey(request)) {
				result.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
				result.setReasonPhase("Refresh key invalid.");
				result.setResponseString("Please check your token refresh key.");
				return result;
			}
			String token = this.getToken(authorization, response);

			if (token != null) {
				tokenJWTDao.removePublicAPIToken(tokenJWTDao.getTokenId(token));
					result.setStatusCode(HttpServletResponse.SC_OK);
					result.setReasonPhase("Token removed.");
					result.setResponseString("Token removed");
			} else {
				result.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
				result.setReasonPhase("Bad token.");
				result.setResponseString("Bad token.");
			}
			return result;
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public ServiceCallResult verifyAPIToken(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> headers = new HashMap<>();
		String authorization = request.getHeader("authorization");
		String apiAccessToken = request.getHeader("ai-api-access-token");
		headers.put("authorization", authorization);
		headers.put("ai-api-access-token", apiAccessToken);
		headers.put("ai-api-refresh-key", request.getHeader("ai-api-refresh-key"));
		try {
			//check api access token in header
			ServiceCallResult result = checkAccessHeader(apiAccessToken);
			if (result.getStatusCode() != HttpServletResponse.SC_OK) {
				return result;
			}
			String token = this.getToken(authorization, response);
			if (token != null) {
				TokenSession oldToken = tokenJWTDao.getTokenSessionFromRedis(tokenJWTDao.getTokenId(token));//.getTokenSession(token,true);
				if (null==oldToken){
					//not valid
					result.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
					result.setReasonPhase("Bad token");
					result.setResponseString("Bad token");
					return result;
				}

				boolean stillActive= tokenJWTDao.checkIfExpired(token);
				String resultToken = "";
				if (stillActive) {
					//valid
					result.setStatusCode(HttpServletResponse.SC_OK);
					result.setReasonPhase("Token verified");
					result.setResponseString(resultToken);
				} else  {
					//expired, please renew with refresh key
					result.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
					result.setReasonPhase("Expired token");
					result.setResponseString("Please renew your token using refresh key.");
				}
			} else {
				result.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
				result.setReasonPhase("No token found");
				result.setResponseString("");
			}
			return result;
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	private ServiceCallResult checkAccessHeader(final String headerValue) {
		ServiceCallResult result = new ServiceCallResult();
		if (headerValue == null || headerValue.isEmpty()) {
			result.setStatusCode(HttpServletResponse.SC_FORBIDDEN);
			result.setResponseString("");
			result.setReasonPhase("AI API call token not present.");
		} else if (!Consts.Http.PUBLIC_API_ACCESS_TOKENS.contains(headerValue)) {
			result.setStatusCode(HttpServletResponse.SC_FORBIDDEN);
			result.setResponseString("");
			result.setReasonPhase("AI public api access token found but not correct.");
		} else {
			result.setStatusCode(HttpServletResponse.SC_OK);
			result.setResponseString("OK");
			result.setReasonPhase("");
		}
		return result;
	}
    private String getToken(String authorizationHeader, HttpServletResponse response) throws IOException {
        String token = null;
        if (authorizationHeader == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Unauthorized: No Authorization header was found");
        }

        String[] parts = authorizationHeader.split(" ");
        if (parts.length != 2) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Unauthorized: Format is Authorization: Bearer [token]");
        } else {
            String scheme = parts[0];
            String credentials = parts[1];

            Pattern pattern = Pattern.compile("^Bearer$", Pattern.CASE_INSENSITIVE);
            if (pattern.matcher(scheme).matches()) {
                token = credentials;
            }
        }

        return token;
    }

//
//	@Override
//	public ServiceCallResult employeeAccountLogin(final String username, final String password,
//	                                              final String tokenCategory) {
//		String ssoUserServiceUrl = config.getSsoUserServiceUrl() + "/auth/employee-account-token";
//		Map<String, String> obj = new HashMap<>();
//		obj.put("username", username);
//		obj.put("password", password);
//		try {
//			ServiceCallResult result = HttpUtil.issuePostRequest(ssoUserServiceUrl, null, obj);
//			return mapper.readValue(result.getResponseString(), ServiceCallResult.class);
//		} catch (IOException e) {
//			LOGGER.error(Arrays.asList(e.getStackTrace()));
//		}
//		return null;
//	}

}
