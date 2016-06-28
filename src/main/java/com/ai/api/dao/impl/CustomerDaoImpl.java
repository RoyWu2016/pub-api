/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.dao.impl;

import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletResponse;

import com.ai.api.bean.SupplierContactInfoBean;
import com.ai.api.bean.SupplierContactInfoMainAlternateBean;
import com.ai.api.bean.FileDetailBean;
import com.ai.api.bean.SupplierDetailBean;
import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.CustomerDao;
import com.ai.commons.HttpUtil;
import com.ai.commons.JsonUtil;
import com.ai.commons.StringUtils;
import com.ai.commons.beans.GetRequest;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.customer.GeneralUserViewBean;
import com.ai.commons.beans.fileservice.FileMetaBean;
import com.ai.commons.beans.user.GeneralUserBean;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

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
	private static final Logger LOGGER = Logger.getLogger(CustomerDaoImpl.class);

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
	public ServiceCallResult getUserSupplierById(String userId){
		String url = config.getFactoryServiceUrl() + "/search?universalId="+userId+"&criteria=";
		GetRequest request = GetRequest.newInstance().setUrl(url);
		ServiceCallResult result = new ServiceCallResult();
		try {
			result = HttpUtil.issueGetRequest(request);

		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
			result.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			result.setReasonPhase("error when getting user suppliers.");
			result.setResponseString("error when getting user suppliers.");
		}
		return result;
	}

	@Override
	public SupplierDetailBean getUserSupplierDetailInfoById(String userId, String supplierId){
		String url = config.getFactoryServiceUrl() + "/getSupplierBySupplierId/"+supplierId;
		GetRequest request = GetRequest.newInstance().setUrl(url);
		ServiceCallResult result = new ServiceCallResult();
		SupplierDetailBean supplierDetailBean = new SupplierDetailBean();

		try {
			result = HttpUtil.issueGetRequest(request);
			//ClientFactoryBean clientFactoryBean = JsonUtil.mapToObject(result.getResponseString(), ClientFactoryBean.class);
			//JsonUtil.mapToJson(result.getResponseString());
			if (StringUtils.isNotBlank(result.getResponseString())) {
				JSONObject jsonObj = new JSONObject(result.getResponseString());
				supplierDetailBean.setId(jsonObj.optString("supplierId"));
				supplierDetailBean.setEntityName(jsonObj.optString("supplierNameEn"));
				supplierDetailBean.setChineseName(jsonObj.optString("supplierNameCn"));
				supplierDetailBean.setCity(jsonObj.optString("supplierCity"));
				supplierDetailBean.setCountry(jsonObj.optString("supplierCountry"));
				supplierDetailBean.setAddress(jsonObj.optString("supplierAddress"));
				supplierDetailBean.setPostcode(jsonObj.optString("supplierPostcode"));
				supplierDetailBean.setNearestOffice(jsonObj.optString("supplierAiOffice"));
				supplierDetailBean.setWebsite(jsonObj.optString("supplierWebsite"));
				supplierDetailBean.setSalesTurnover(jsonObj.optString("supplierSalesTurnover"));
				supplierDetailBean.setNoOfEmployees(jsonObj.optString("supplierNbEmployees"));
				supplierDetailBean.setUserId(userId);

				JSONArray arrJson= jsonObj.getJSONArray("supplierProducts");
				String[] arr=new String[arrJson.length()];
				for(int i=0;i<arrJson.length();i++)
					arr[i]=arrJson.getString(i);
				supplierDetailBean.setMainProductLines(Arrays.asList(arr));

				SupplierContactInfoBean contactInfoBean = new SupplierContactInfoBean();
				JSONObject supplierManager = jsonObj.getJSONObject("supplierManager");
				SupplierContactInfoMainAlternateBean mainBean = new SupplierContactInfoMainAlternateBean();
				mainBean.setName(supplierManager.optString("name"));
				mainBean.setPhoneNumber(supplierManager.optString("phone"));
				mainBean.setMobileNumber(supplierManager.optString("mobile"));
				mainBean.setEmail(supplierManager.optString("email"));
				contactInfoBean.setMain(mainBean);

				JSONObject supplierQualityManager = jsonObj.getJSONObject("supplierQualityManager");
				SupplierContactInfoMainAlternateBean alternateBean = new SupplierContactInfoMainAlternateBean();
				alternateBean.setName(supplierQualityManager.optString("name"));
				alternateBean.setPhoneNumber(supplierQualityManager.optString("phone"));
				alternateBean.setMobileNumber(supplierQualityManager.optString("mobile"));
				alternateBean.setEmail(supplierQualityManager.optString("email"));
				contactInfoBean.setAlternate(alternateBean);

				supplierDetailBean.setContactInfo(contactInfoBean);


				JSONArray accessMap = jsonObj.getJSONArray("accessMap");
				List<FileDetailBean> accessMapList = new ArrayList<FileDetailBean>();
				for( int i=0;i< accessMap.length();i++){
					JSONObject obj = accessMap.getJSONObject(i);
					String fileId = obj.optString("id");
					if(fileId!=null && fileId!="") {
						FileDetailBean accessMapBean = new FileDetailBean();
						String fileUrl = config.getFileServiceUrl() + "/getFileInfoById?id="+fileId;
						GetRequest fileRequest = GetRequest.newInstance().setUrl(fileUrl);
						ServiceCallResult fileResult = HttpUtil.issueGetRequest(fileRequest);
						FileMetaBean fileMetaBean = JsonUtil.mapToObject(fileResult.getResponseString(), FileMetaBean.class);
						accessMapBean.setId(fileId);
						accessMapBean.setDocType(fileMetaBean.getFileType());
						accessMapBean.setFileName(fileMetaBean.getFileName());
						accessMapBean.setFilesize(fileMetaBean.getFileSize());
						accessMapBean.setUrl(config.getFileServiceUrl() + "/getFile?id="+fileId);

						accessMapList.add(accessMapBean);
					}
				}
				supplierDetailBean.setAccessMaps(accessMapList);

				List<FileDetailBean> qualityDocList = new ArrayList<FileDetailBean>();
				JSONObject busLicDocObj = jsonObj.getJSONObject("busLicDocBean");
				FileDetailBean busLicDocBean = new FileDetailBean();
				busLicDocBean.setDocType(busLicDocObj.optString("docType"));
				setFileInfo(busLicDocBean, busLicDocObj.optString("id"));
				qualityDocList.add(busLicDocBean);

				JSONObject isoCertDocObj = jsonObj.getJSONObject("isoCertDocBean");
				FileDetailBean isoCertDocBean = new FileDetailBean();
				isoCertDocBean.setDocType(isoCertDocObj.optString("docType"));
				setFileInfo(isoCertDocBean, isoCertDocObj.optString("id"));
				qualityDocList.add(isoCertDocBean);

				JSONObject exportLicDocObj = jsonObj.getJSONObject("exportLicDocBean");
				FileDetailBean exportLicDocBean = new FileDetailBean();
				exportLicDocBean.setDocType(exportLicDocObj.optString("docType"));
				setFileInfo(exportLicDocBean, exportLicDocObj.optString("id"));
				qualityDocList.add(exportLicDocBean);

				JSONObject rohsCertDocObj = jsonObj.getJSONObject("rohsCertDocBean");
				FileDetailBean rohsCertDocBean = new FileDetailBean();
				rohsCertDocBean.setDocType(rohsCertDocObj.optString("docType"));
				setFileInfo(rohsCertDocBean, rohsCertDocObj.optString("id"));
				qualityDocList.add(rohsCertDocBean);

				JSONObject testReportDocObj = jsonObj.getJSONObject("testReportDocBean");
				FileDetailBean testReportDocBean = new FileDetailBean();
				testReportDocBean.setDocType(testReportDocObj.optString("docType"));
				setFileInfo(testReportDocBean, testReportDocObj.optString("id"));
				qualityDocList.add(testReportDocBean);

				JSONObject otherDocObj = jsonObj.getJSONObject("otherDocBean");
				FileDetailBean otherDocBean = new FileDetailBean();
				otherDocBean.setDocType(otherDocObj.optString("docType"));
				setFileInfo(otherDocBean, otherDocObj.optString("id"));
				qualityDocList.add(otherDocBean);

				supplierDetailBean.setQualityDocs(qualityDocList);
			}

		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
			result.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			result.setReasonPhase("error when getting supplier detail info.");
			result.setResponseString("error when getting supplier detail info.");
		} catch(Exception e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return supplierDetailBean;
	}

	private void setFileInfo(FileDetailBean bean, String id){
		try{
			if(id!=null && id!="") {
				String fileUrl = config.getFileServiceUrl() + "/getFileInfoById?id=" + id;
				GetRequest fileRequest = GetRequest.newInstance().setUrl(fileUrl);
				ServiceCallResult fileResult = HttpUtil.issueGetRequest(fileRequest);
				FileMetaBean fileMetaBean = JsonUtil.mapToObject(fileResult.getResponseString(), FileMetaBean.class);
				//bean.setDocType(fileMetaBean.getFileType());
				bean.setId(id);
				bean.setFileName(fileMetaBean.getFileName());
				bean.setFilesize(fileMetaBean.getFileSize());
				bean.setUrl(config.getFileServiceUrl() + "/getFile?id=" + id);
			}
		}catch(Exception e){
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
	}
}
