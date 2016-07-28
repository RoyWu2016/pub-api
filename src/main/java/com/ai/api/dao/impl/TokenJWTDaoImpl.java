package com.ai.api.dao.impl;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

import com.ai.api.util.RedisUtil;
import com.ai.commons.IDGenerator;
import com.ai.commons.StringUtils;
import com.ai.commons.beans.user.TokenSession;
import com.alibaba.fastjson.JSON;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.jwt.consumer.JwtContext;
import org.jose4j.jwt.consumer.NumericDateValidator;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/***************************************************************************
 * <PRE>
 * Project Name    : Public-API
 * <p>
 * Package Name    : com.ai.api.util
 * <p>
 * Creation Date   : 2016/7/11 10:47
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

@Component
public class TokenJWTDaoImpl {

    private static final Logger logger = LoggerFactory.getLogger(TokenJWTDaoImpl.class);

    private static final String ISSUER_NAME = "http://asiainspection.com";
    private static final Integer TOKEN_EXPIRATION_TIME = 1;
    private static final String TOKEN_SUBJECT = "AI API token";
    private static final String AES_KEY_PATH = "/usr/local/tomcat7_8091/conf/sso-sig/server-token.aes";
    private static final String ECC_PRIV_KEY_PATH = "/usr/local/tomcat7_8091/conf/sso-sig/server-sig.ecc";
    private static final String ECC_PUB_KEY_PATH = "/usr/local/tomcat7_8091/conf/sso-sig/server-sig.ecc.pub";
//    private static final String AES_KEY_PATH = "D:/AllProjects/AI-Projects/server-token.aes";
//    private static final String ECC_PRIV_KEY_PATH = "D:/AllProjects/AI-Projects/server-sig.ecc";
//    private static final String ECC_PUB_KEY_PATH = "D:/AllProjects/AI-Projects/server-sig.ecc.pub";

    private final String seperator = "~~~";
    private Map<String, Key> keys = new HashMap<String, Key>();
	private String TOKENKEY = "publicAPIToken";


//	@CachePut(value = "publicAPIToken",key = "#sessionId")//create or update-----function will run every called
	public TokenSession generateToken(final String login, final String userId, String sessionId){
		TokenSession tokenSession = new TokenSession();
		String jwt = null;
		try {
			if (sessionId.isEmpty()) {
				tokenSession.setId(IDGenerator.uuid());
			} else {
				tokenSession.setId(sessionId);
			}
			tokenSession.setUserId(userId);
			String[] temp = this.innerEncryption(login, userId, tokenSession).split(seperator);
			String innerJwt = temp[0];
			jwt = this.outerEncryption(innerJwt);
			tokenSession.setToken(jwt);
			tokenSession.setValidBefore(temp[1]);
			RedisUtil redisUtil = RedisUtil.getInstance();
			redisUtil.hset(TOKENKEY,sessionId,JSON.toJSONString(tokenSession));
		}catch (Exception e){
			logger.error("error generateToken",e);
			tokenSession = null;
		}
		return tokenSession;
	}

	public JwtClaims getClaimsByJWT(final String jwt){
		Key key = this.retrieveKey(AES_KEY_PATH);
		logger.info("get tokenId from jwt......");
		JwtConsumer firstPassJwtConsumer = new JwtConsumerBuilder()
				.setSkipAllValidators()
				.setDecryptionKey(key)
				.setDisableRequireSignature()
				.setSkipSignatureVerification()
				.build();
		TokenSession session = new TokenSession();
//		JwtContext jwtContext = null;
		JwtClaims tmpClaim = null;
		try {
			JwtContext jwtContext = firstPassJwtConsumer.process(jwt);
			tmpClaim = jwtContext.getJwtClaims();
			String userId = (String) tmpClaim.getClaimValue("userId");
			String sessionId = (String) tmpClaim.getClaimValue("sessId");
			logger.info("getTokenByJWT userId:"+userId);
			logger.info("getTokenByJWT sessionId:"+sessionId);
			logger.info("getTokenByJWT jwt:"+jwt);
			session.setId(sessionId);
			session.setUserId(userId);
		}catch (Exception e){
			logger.error("",e);
		}
		return tmpClaim;
	}

//	@Cacheable(value = "publicAPIToken",key = "#sessionId")//get data from redis and the function will not run
	public TokenSession getTokenSessionFromRedis(String sessionId){
//		logger.error("this message is not supposed to be saw!  id:"+sessionId);
		RedisUtil redisUtil = RedisUtil.getInstance();
		String resultStr = redisUtil.hget(TOKENKEY,sessionId);
		if (StringUtils.isBlank(resultStr))return null;
		return JSON.parseObject(resultStr).toJavaObject(TokenSession.class);
	}

//	@CacheEvict(value = "publicAPIToken",key = "#sessionId")
	public boolean removePublicAPIToken(String sessionId) {
		logger.info("remove tokenSession sessionId:" +sessionId);
		RedisUtil redisUtil = RedisUtil.getInstance();
		Long count = redisUtil.hdel(TOKENKEY,sessionId);
		if (count==1) {
			logger.info("success remove tokenSession sessionId[" + sessionId + "]");
			return true;
		}else {
			logger.info("fail to remove tokenSession sessionId["+sessionId+"]");
			return false;
		}
	}

    public boolean checkIfExpired(final String jwt) {
        Key key = this.retrieveKey(AES_KEY_PATH);
        logger.info("check token expired......");
        JwtConsumer firstPassJwtConsumer = new JwtConsumerBuilder()
                .setSkipAllValidators()
                .setDecryptionKey(key)
                .setDisableRequireSignature()
                .setSkipSignatureVerification()
                .build();
        try {
	        JwtContext jwtContext = firstPassJwtConsumer.process(jwt);
            NumericDateValidator validator = new NumericDateValidator();
            if (validator.validate(jwtContext) != null) {
                logger.info("Token expired now.");
                return false;
            } else {
	            logger.info("token alive");
                return true;
            }
        }catch (Exception e){
            logger.error("error!",e);
        }
        return false;
    }




    private String innerEncryption(final String login, final String userId, TokenSession sess) throws JoseException {
        logger.info("JWT being sign...");

        // Create Claims to add to the token
        logger.info("JWT being configured with client account user specific claims");

        // Create the Claims, which will be the content of the JWT
        JwtClaims claims = new JwtClaims();
        claims.setIssuer(ISSUER_NAME);  // who creates the token and signs it
        claims.setAudience(login); // to whom the token is intended to be sent
        claims.setExpirationTimeMinutesInTheFuture(TOKEN_EXPIRATION_TIME); // time when the token will expire in minutes
        claims.setGeneratedJwtId(); // a unique identifier for the token
        claims.setIssuedAtToNow();  // when the token was issued/created (now)
        claims.setNotBeforeMinutesInThePast(2); // time before which the token is not yet valid (2 minutes ago)
        claims.setSubject(TOKEN_SUBJECT); // the subject/principal is whom the token is about
        claims.setClaim("userId", userId); // additional claims, store user id
        claims.setClaim("sessId", sess.getId()); // additional claims, store session id


        // Create a JsonWebSignature object.
        JsonWebSignature jws = new JsonWebSignature();

        // The payload of the JWS is JSON content of the JWT Claims
        jws.setPayload(claims.toJson());

        // The JWT is signed using the sender's private key
        jws.setKey(this.retrieveKey(ECC_PRIV_KEY_PATH));

        // Set the signature algorithm on the JWT/JWS that will integrity protect the claims
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.ECDSA_USING_P256_CURVE_AND_SHA256);

        String validBefore = "";
        try {
            validBefore = String.valueOf(claims.getExpirationTime().getValue());
        } catch (MalformedClaimException e) {
            e.printStackTrace();
        }

        // Sign the JWS and produce the compact serialization, which will be the inner JWT/JWS
        return jws.getCompactSerialization() + seperator + validBefore;
    }

    private String outerEncryption(String innerJwt) throws JoseException {
        Key key = this.retrieveKey(AES_KEY_PATH);
        logger.info("JWT being encrypted...");

        // The outer JWT is a JWE
        JsonWebEncryption jwe = new JsonWebEncryption();

        // Set the "alg" header, which indicates the key management mode for this JWE.
        // In this example we are using the direct key management mode, which means
        // the given key will be used directly as the content encryption key.
        jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.DIRECT);

        // Set the "enc" header, which indicates the content encryption algorithm to be used.
        // This example is using AES_128_CBC_HMAC_SHA_256 which is a composition of AES CBC
        // and HMAC SHA2 that provides authenticated encryption.
        jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256);

        // We encrypt to the receiver using their public key
        jwe.setKey(key);

        // A nested JWT requires that the cty (Content Type) header be set to "JWT" in the outer JWT
        jwe.setContentTypeHeaderValue("JWT");

        // The inner JWT is the payload of the outer JWT
        jwe.setPayload(innerJwt);

        // Produce the JWE compact serialization, which is the complete JWT/JWE representation,
        return jwe.getCompactSerialization();
    }

    /**
     * Retrieve security key on the server
     *
     * @author Xavier Besnault
     * @param keyType
     * @return
     */
    private Key retrieveKey(String keyType) {
        if (!this.keys.containsKey(keyType)) {
            FileInputStream fileIn;
            try {
                fileIn = new FileInputStream(keyType);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                this.keys.put(keyType, (Key) in.readObject());
                in.close();
                fileIn.close();
            } catch (Exception e) {
                logger.error("Problem during getting the decryption key :" + keyType, e);
            }
        }

        return this.keys.get(keyType);
    }
}
