package com.ai.api.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ai.api.dao.SSOUserServiceDao;
import com.ai.api.dao.impl.TokenJWTDaoImpl;
import com.ai.api.service.SSOUserService;
import com.ai.api.service.UserService;
import com.ai.api.util.RedisUtil;
import com.ai.commons.HttpUtil;
import com.ai.commons.beans.ServiceCallResult;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.jose4j.jwt.JwtClaims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/***************************************************************************
 * <PRE>
 * Project Name    : Public-API
 * <p>
 * Package Name    : com.ai.api.service.impl
 * <p>
 * Creation Date   : 2016/8/2 10:30
 * <p>
 * Author          : Jianxiong Cai
 * <p>
 * Purpose         : TODO
 * <p>
 * <p>
 * History         : TODO
 * <p>
 * </PRE>
 ***************************************************************************/


@Service
public class SSOUserServiceImpl implements SSOUserService {
	private static final Logger LOGGER = LoggerFactory.getLogger(SSOUserServiceImpl.class);

	@Autowired
	private SSOUserServiceDao ssoUserServiceDao;
	@Autowired
	private TokenJWTDaoImpl tokenJWTDao;
	@Autowired
	private UserService userService;

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
			ServiceCallResult result = ssoUserServiceDao.checkAccessHeader(apiAccessToken);
			if (result.getStatusCode() != HttpServletResponse.SC_OK) {
				return result;
			}

			if (!HttpUtil.validateRefreshTokenKey(request)) {
				result.setStatusCode(HttpServletResponse.SC_UNAUTHORIZED);
				result.setReasonPhase("Refresh key invalid.");
				result.setResponseString("Please check your token refresh key.");
				return result;
			}
			String token = ssoUserServiceDao.getToken(authorization, response);

			if (token != null) {
				JwtClaims claims = tokenJWTDao.getClaimsByJWT(token);
				tokenJWTDao.removePublicAPIToken((String)claims.getClaimValue("sessId"));
				userService.removeUserProfileCache((String)claims.getClaimValue("userId"));
				RedisUtil.hdel("employeeCache",(String)claims.getClaimValue("userId"));
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
			LOGGER.error("remove token error: " + ExceptionUtils.getFullStackTrace(e));
		}
		return null;
	}
}
