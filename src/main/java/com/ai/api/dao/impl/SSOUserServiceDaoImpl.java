/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.dao.impl;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ai.api.dao.SSOUserServiceDao;
import com.ai.commons.Consts;
import com.ai.commons.HttpUtil;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.user.TokenSession;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.jose4j.jwt.JwtClaims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
@Component
public class SSOUserServiceDaoImpl implements SSOUserServiceDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(SSOUserServiceDaoImpl.class);

	@Autowired
    private TokenJWTDaoImpl tokenJWTDao;

	@Override
	public ServiceCallResult refreshAPIToken(HttpServletRequest request,HttpServletResponse response) {
        String accessToken = request.getHeader("ai-api-access-token");
        String authorization = request.getHeader("authorization");

		try {
            ServiceCallResult result = this.checkAccessHeader(accessToken);
            if (result.getStatusCode() != HttpServletResponse.SC_OK) {
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
	            JwtClaims claims = tokenJWTDao.getClaimsByJWT(jwt);
	            TokenSession oldToken = tokenJWTDao.getTokenSessionFromRedis((String)claims.getClaimValue("sessId"));
	            final String userType = (String) claims.getClaimValue("userType");
	            TokenSession tokenSession = null;
	            if (null!=oldToken){
		            tokenSession = tokenJWTDao.generateToken("refresh", oldToken.getUserId(),
				            oldToken.getId(), userType);
	            }
                if (null==tokenSession) {
                    //not valid
                    result.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
                    result.setReasonPhase("Bad token to refresh");
                    result.setResponseString("Bad token to refresh");
                } else {
                    //valid
	                String resultJWT = JSON.toJSONString(tokenSession);
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
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public ServiceCallResult verifyAPIToken(HttpServletRequest request, HttpServletResponse response) {
		String authorization = request.getHeader("authorization");
		String apiAccessToken = request.getHeader("ai-api-access-token");
		String requestedURL = request.getHeader("requested-url").toLowerCase();
		try {
			//check api access token in header
			ServiceCallResult result = checkAccessHeader(apiAccessToken);
			if (result.getStatusCode() != HttpServletResponse.SC_OK) {
				return result;
			}
			String token = this.getToken(authorization, response);
//			LOGGER.info("get token  :"+token);
			if (token != null) {
				JwtClaims claims = tokenJWTDao.getClaimsByJWT(token);

				//user can only reqeust resource belong to that user
				final String tokenUserId = (String)claims.getClaimValue("userId");
				final String userType = (String)claims.getClaimValue("userType");
//				LOGGER.info("userid: " + tokenUserId + ", usertype: " + userType);
//				LOGGER.info("requested url: " + requestedURL);

//				LOGGER.info("checking: " + Consts.Http.PUBLIC_API_USER_RESOURCE_URL_PREFIX + tokenUserId.toLowerCase() );
				if (userType != null && userType.equals(Consts.Http.USER_TYPE_CLIENT) &&
						requestedURL.startsWith(Consts.Http.PUBLIC_API_USER_RESOURCE_URL_PREFIX)) {
					//need to check if user id in token is same as user id in reqeusted url
					if (!requestedURL.startsWith(Consts.Http.PUBLIC_API_USER_RESOURCE_URL_PREFIX + tokenUserId.toLowerCase())) {
						LOGGER.info("forbid to access:" + requestedURL);
						//access forbidden
						result.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
						result.setReasonPhase("Access of requested resource not allowed. ");
						result.setResponseString("You can only access resource belong to you.");
						return result;
					}
				} else {
//					LOGGER.info("let it go. " + requestedURL);
				}

				//check session in redis
				TokenSession oldToken = tokenJWTDao.getTokenSessionFromRedis((String)claims.getClaimValue("sessId"));
				if (null==oldToken||!token.equals(oldToken.getToken())){
					//not valid
					result.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
					result.setReasonPhase("Bad token");
					result.setResponseString("Bad token");
					return result;
				}

				boolean stillAlive = tokenJWTDao.checkIfExpired(token);
				if (stillAlive) {
					//valid
					result.setStatusCode(HttpServletResponse.SC_OK);
					result.setReasonPhase("Token verified");
					result.setResponseString("Token verified");
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

	@Override
	public ServiceCallResult checkAccessHeader(final String headerValue) {
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
//		LOGGER.info("checkAccessHeader result :"+JSON.toJSONString(result));
		return result;
	}


	@Override
    public String getToken(String authorizationHeader, HttpServletResponse response) throws IOException {
        String token = null;
        if (authorizationHeader == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Unauthorized: No Authorization header was found");
	        return null;
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

}
