package com.ai.api.dao.impl;

import com.ai.api.config.ServiceConfig;
import com.ai.commons.HttpUtil;
import com.ai.commons.IDGenerator;
import com.ai.commons.beans.user.GeneralUserBean;
import com.ai.commons.beans.user.TokenSession;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.*;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jose4j.jwt.JwtClaims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.Key;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

@Service
public class TokenJWTDaoImpl {

    private static final Logger logger = LoggerFactory.getLogger(TokenJWTDaoImpl.class);

    private static final String ISSUER_NAME = "http://asiainspection.com";
    private static final Integer TOKEN_EXPIRATION_TIME = 120;
    private static final String TOKEN_SUBJECT = "AI API token";
    private static final String AES_KEY_PATH = "/usr/local/tomcat7_8091/conf/sso-sig/server-token.aes";
    private static final String ECC_PRIV_KEY_PATH = "/usr/local/tomcat7_8091/conf/sso-sig/server-sig.ecc";
    private static final String ECC_PUB_KEY_PATH = "/usr/local/tomcat7_8091/conf/sso-sig/server-sig.ecc.pub";
//    private static final String AES_KEY_PATH = "D:/AllProjects/AI-Projects/server-token.aes";
//    private static final String ECC_PRIV_KEY_PATH = "D:/AllProjects/AI-Projects/server-sig.ecc";
//    private static final String ECC_PUB_KEY_PATH = "D:/AllProjects/AI-Projects/server-sig.ecc.pub";

    private final String seperator = "~~~";
    private ObjectMapper mapper = new ObjectMapper();
    private Map<String, Key> keys = new HashMap<String, Key>();

    @Autowired
    @Qualifier("serviceConfig")
    private ServiceConfig config;


    //token for client account(general user)
    public String generatePublicAPIToken(final String login, final String userId, String sessionId) {
        String jwt = null;
        try {
            logger.info("Start token generation...");
            Map<String, String> obj = new HashMap<>();
            TokenSession tokenSession = new TokenSession();
            if (sessionId.isEmpty()) {
                tokenSession.setId(IDGenerator.uuid());
                obj.put("curl", "insert");
            } else {
                tokenSession.setId(sessionId);
                obj.put("curl", "update");
            }
            tokenSession.setUserId(userId);

            String [] temp = this.innerEncryption(login, userId, tokenSession).split(seperator);
            String innerJwt = temp[0];
            jwt = this.outerEncryption(innerJwt);
            tokenSession.setToken(jwt);
            tokenSession.setValidBefore(temp[1]);

//            obj.put("sessionId", sessionId);
//            obj.put("jwt", jwt);
            //store to database  -------------------------------------------
            String tokenUrl = config.getSsoUserServiceUrl()+"/user/updateToken";
            logger.info("ready post to url:"+tokenUrl);
            String updateStr = HttpUtil.issuePostRequest(tokenUrl,obj,tokenSession).getResponseString();
            logger.info("New token '" + jwt + "' generated for client login: " + login);
            logger.info("Update response:"+updateStr);
            HashMap<String, String> result = new HashMap<>();
            result.put("userId", userId);
            result.put("token", jwt);
            result.put("validBefore", tokenSession.getValidBefore());
            return mapper.writeValueAsString(result);

        } catch (JoseException e) {
            logger.error("Problem during client account token generation " +
                    Arrays.asList(e.getStackTrace()));
        } catch (JsonProcessingException e) {
            logger.error("Problem during write user id/token into json " +
                    Arrays.asList(e.getStackTrace()));
        }catch (IOException e){
            logger.error("error",e);
        }
        return "";
    }
    public String refreshPublicAPIToken(String jwt, final String login) {
        TokenSession sess = getTokenSession(jwt, false);
        if (sess == null) {
            return "";
        } else {
            logger.info("Start refresh token.");
            GeneralUserBean client = new GeneralUserBean();
            client.setUserId(sess.getUserId());
            return generatePublicAPIToken(login, sess.getUserId(), sess.getId());
        }
    }

    public TokenSession getTokenSession(final String jwt, final boolean checkIfExpired) {
        Key key = this.retrieveKey(AES_KEY_PATH);
        logger.info("Verify client account token validity...");

        JwtConsumer firstPassJwtConsumer = new JwtConsumerBuilder()
                .setSkipAllValidators()
                .setDecryptionKey(key)
                .setDisableRequireSignature()
                .setSkipSignatureVerification()
                .build();

        //The first JwtConsumer is basically just used to parse the JWT into a JwtContext object.
        JwtContext jwtContext = null;
        try {
            jwtContext = firstPassJwtConsumer.process(jwt);

            logger.info("Token has he right decryption key...");

            // From the JwtContext we can get the issuer, or whatever else we might need,
            // to lookup or figure out the kind of validation policy to apply
            JwtClaims tmpClaim = jwtContext.getJwtClaims();
            String userId = (String) tmpClaim.getClaimValue("userId");
            String sessId = (String) tmpClaim.getClaimValue("sessId");

            TokenSession sess = new TokenSession();
            sess.setId(sessId);
            sess.setUserId(userId);
            //get userById + verify username == audience then get user keys
            String tokenUrl = config.getSsoUserServiceUrl()+"/user/getPublicAPIToken";
            String tokenStr = HttpUtil.issuePostRequest(tokenUrl,null,sess).getResponseString();
            TokenSession foundSess = JSON.parseObject(tokenStr,TokenSession.class);
            if (foundSess == null) {
                logger.error("Login or token doesn't match with database!");
                return null;
            }
            //compare login and token with database
            if (!jwt.equals(foundSess.getToken())) {
                logger.error("Token doesn't match with database!");
                return null;
            } else {
                if (checkIfExpired) {
                    NumericDateValidator validator = new NumericDateValidator();
                    if (validator.validate(jwtContext) != null) {
                        logger.info("Token expired now.");
                        return null;
                    } else {
                        //still valid, return session
                        sess.setToken(jwt);
                        return sess;
                    }
                } else {
                    //no need to check if expired, just return session
                    sess.setToken(jwt);
                    return sess;
                }
            }
        }catch (InvalidJwtException e) {
            logger.error("Parsing client account token error: " + ExceptionUtils.getStackTrace(e));
        } catch (MalformedClaimException e) {
            logger.error("JWT claim is malformed" + ExceptionUtils.getStackTrace(e));
        }catch (IOException e){
            logger.error("error! IOException",e);
        }
        return null;
    }

    public String removePublicAPIToken(String jwt) {
        TokenSession sess = getTokenSession(jwt, false);
        String result = "";
        try {

            if (sess == null) {
                return "";
            } else {
                logger.info("Removing token.");
                String tokenUrl = config.getSsoUserServiceUrl() + "/user/removePublicAPIToken";
                String tokenStr = HttpUtil.issuePostRequest(tokenUrl, null, sess).getResponseString();
                int deleteRow = JSON.parseObject(tokenStr,Integer.TYPE);
                if(deleteRow==1){
                    result = "DELETED";
                }else {
                    result = "DELETE_FAILED";
                }
            }
        }catch (Exception e){
            logger.error("",e);
        }
        return result;
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
