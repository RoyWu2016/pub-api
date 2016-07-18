/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.dao.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.servlet.http.HttpServletResponse;

import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.CustomerDao;
import com.ai.commons.HttpUtil;
import com.ai.commons.JsonUtil;
import com.ai.commons.beans.GetRequest;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.customer.GeneralUserViewBean;
import com.ai.commons.beans.legacy.customer.ClientInfoBean;
import com.ai.commons.beans.user.GeneralUserBean;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.web.multipart.MultipartFile;

/***************************************************************************
 * <PRE>
 * Project Name    : api
 * <p/>
 * Package Name    : com.ai.api.dao.impl
 * <p/>
 * File Name       : CustomerDaoImpl.java
 * <p/>
 * Creation Date   : May 24, 2016
 * <p/>
 * Author          : Allen Zhang
 * <p/>
 * Purpose         : all call to customer service should be done here,
 * we don't call database directly, we call services instead
 * <p/>
 * <p/>
 * History         : TODO
 * <p/>
 * </PRE>
 ***************************************************************************/

public class CustomerDaoImpl extends JdbcDaoSupport implements CustomerDao {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerDaoImpl.class);

	@Autowired
	@Qualifier("serviceConfig")
	private ServiceConfig config;

	@Override
	public GeneralUserViewBean getGeneralUserViewBean(String userId) {

		String generalUVBeanURL = config.getCustomerServiceUrl() + "/users/" + userId;
		GetRequest request = GetRequest.newInstance().setUrl(generalUVBeanURL);
		ServiceCallResult result;
		GeneralUserViewBean generalUserBean;
		try {
			result = HttpUtil.issueGetRequest(request);
			generalUserBean = JsonUtil.mapToObject(result.getResponseString(), GeneralUserViewBean.class);
			return generalUserBean;
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public GeneralUserBean getGeneralUser(String userId) {

		String generalUVBeanURL = config.getCustomerServiceUrl() + "/users/general-user/" + userId;
		GetRequest request = GetRequest.newInstance().setUrl(generalUVBeanURL);
		ServiceCallResult result;
		GeneralUserBean generalUserBean;
		try {
			result = HttpUtil.issueGetRequest(request);
			generalUserBean = JsonUtil.mapToObject(result.getResponseString(), GeneralUserBean.class);
			return generalUserBean;
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public boolean updateGeneralUser(GeneralUserBean newUser) {
		String url = config.getCustomerServiceUrl() + "/users/" + newUser.getUserId() + "/general-user";
		try {
			ServiceCallResult result = HttpUtil.issuePostRequest(url, null, newUser);
			if (result.getStatusCode() == HttpStatus.OK.value() &&
					result.getResponseString().isEmpty() &&
					result.getReasonPhase().equalsIgnoreCase("OK")) {

				return true;
			}
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

	@Override
	public ServiceCallResult updateGeneralUserPassword(String userId, HashMap<String,String> pwdMap) {
		String url = config.getCustomerServiceUrl() + "/users/general-user/" + userId + "/password";

		try {
			return HttpUtil.issuePostRequest(url, null, pwdMap);

		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
			ServiceCallResult result = new ServiceCallResult();
			result.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			result.setReasonPhase("error when updating user password.");
			result.setResponseString("error when updating user password.");
			return result;
		}

	}

    @Override
    public InputStream getCompanyLogo(String companyId) {
        String url = config.getCustomerServiceUrl()+"/customer/"+companyId+"/logo";
		CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        InputStream inputStream = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(60000)
                    .setSocketTimeout(60000)
                    .setConnectTimeout(60000).build();
            httpGet.setConfig(requestConfig);
			logger.info("get companyLogo doGet----url:["+url+"]");
            response = httpClient.execute(httpGet);
            if (null==response)return null;
            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                inputStream = entity.getContent();
//                EntityUtils.consume(entity);
            }
//			String responseJson = EntityUtils.toString(response.getEntity(), "UTF-8");
//			logger.info("get logo responseJson:"+responseJson);
        } catch (Exception e) {
            logger.error("ERROR from-Dao[getCompanyLogo]", e);
        }/*finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            }catch (Exception e){

            }
        }*/
        return inputStream;
    }

    @Override
    public boolean updateCompanyLogo(String companyId, MultipartFile file) {
        String url = config.getCustomerServiceUrl()+"/customer/"+companyId+"/logo-stream/file-name/"+file.getOriginalFilename()+"/file-size/"+file.getSize();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        boolean b = false;
        try {
            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(60000)
                    .setConnectTimeout(60000)
                    .setSocketTimeout(60000).build();
            EntityBuilder entity = EntityBuilder.create();
            entity.setStream(file.getInputStream());

            httpPost.setEntity(entity.build());
            httpPost.setConfig(requestConfig);
            logger.info("update companyLogo doPost----url:["+url+"]");
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                b = true;
            }
            String responseJson = EntityUtils.toString(response.getEntity(), "UTF-8");
            logger.info("update logo responseJson:"+responseJson);
        }catch (Exception e){
            logger.error("ERROR",e);
        }/*finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            }catch (Exception e){

            }
        }*/
        return b;
    }

	@Override
	public boolean deleteCompanyLogo(String companyId) {
		String url = config.getCustomerServiceUrl()+"/customer/"+companyId+"/logo";
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		boolean b = false;
		try {
			HttpDelete httpDelete = new HttpDelete(url);
			RequestConfig requestConfig = RequestConfig.custom()
					.setConnectionRequestTimeout(60000)
					.setSocketTimeout(60000)
					.setConnectTimeout(60000).build();
			httpDelete.setConfig(requestConfig);
			logger.info("delete companyLogo doDelete----url:["+url+"]");
			response = httpClient.execute(httpDelete);
			if (response.getStatusLine().getStatusCode() == 200) {
				b = true;
			}
			String responseJson = EntityUtils.toString(response.getEntity(), "UTF-8");
			logger.info("delete logo responseJson:"+responseJson);

		}catch (Exception e){
			logger.error("ERROR",e);
		}/*finally {
			try {
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			}catch (Exception e){

			}
		}*/
		return b;
	}


	@Override
	public boolean createNewAccount(ClientInfoBean clientInfoBean) {
		String url = config.getCustomerServiceUrl() + "/customer-legacy/create-new-client-account?clientType=AFI";
		try {
			//String jsonStr = JsonUtil.mapToJson(clientInfoBean);
			//url = url + "?clientInfo="+jsonStr;
			ServiceCallResult result = HttpUtil.issuePostRequest(url, null, clientInfoBean);
			//ServiceCallResult result = HttpUtil.issuePostRequest(url, null, jsonStr);
			if (result.getStatusCode() == HttpStatus.OK.value() &&
					result.getReasonPhase().equalsIgnoreCase("OK")) {
				return true;
			}
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

}
