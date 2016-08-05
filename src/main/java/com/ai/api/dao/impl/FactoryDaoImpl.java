package com.ai.api.dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.ai.api.bean.FileDetailBean;
import com.ai.api.bean.SupplierContactInfoBean;
import com.ai.api.bean.SupplierDetailBean;
import com.ai.api.bean.legacy.AttachmentDocBean;
import com.ai.api.bean.legacy.ClientFactoryBean;
import com.ai.api.bean.legacy.FactorySearchBean;
import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.FactoryDao;
import com.ai.api.exception.AIException;
import com.ai.api.util.AIUtil;
import com.ai.commons.HttpUtil;
import com.ai.commons.JsonUtil;
import com.ai.commons.beans.GetRequest;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.fileservice.FileMetaBean;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;

/**
 * Created by Administrator on 2016/6/29 0029.
 */
public class FactoryDaoImpl implements FactoryDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(FactoryDaoImpl.class);

	@Autowired
	@Qualifier("serviceConfig")
	private ServiceConfig config;

	@Override
	public List<FactorySearchBean> getSuppliersByUserId(String userId) throws IOException, AIException {
		String url = config.getFactoryServiceUrl() + "/search?universalId=" + userId + "&criteria=";
		GetRequest request = GetRequest.newInstance().setUrl(url);
		ServiceCallResult result = new ServiceCallResult();
		try {
			result = HttpUtil.issueGetRequest(request);
			List<FactorySearchBean> factorySearchBeanList = JsonUtil.mapToObject(result.getResponseString(), new TypeReference<List<FactorySearchBean>>() {
			});
			for (FactorySearchBean bean : factorySearchBeanList) {
				String createDate = bean.getCreatedDate();
				String updateDate = bean.getUpdateDate();
				bean.setCreatedDate(AIUtil.convertDateFormat(createDate));
				bean.setUpdateDate(AIUtil.convertDateFormat(updateDate));
			}
			return factorySearchBeanList;
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
			result.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			result.setReasonPhase("error when getting user suppliers.");
			result.setResponseString("error when getting user suppliers.");
		}
		return null;
	}

	@Override
	public SupplierDetailBean getUserSupplierDetailInfoById(String userId, String supplierId) {
		String url = config.getFactoryServiceUrl() + "/getSupplierBySupplierId/" + supplierId;
		GetRequest request = GetRequest.newInstance().setUrl(url);
		ServiceCallResult result = new ServiceCallResult();
		SupplierDetailBean supplierDetailBean = new SupplierDetailBean();

		try {
			result = HttpUtil.issueGetRequest(request);
			ClientFactoryBean clientFactoryBean = JsonUtil.mapToObject(result.getResponseString(), ClientFactoryBean.class);

			supplierDetailBean.setId(clientFactoryBean.getSupplierId());
			supplierDetailBean.setEntityName(clientFactoryBean.getSupplierNameEn());
			supplierDetailBean.setChineseName(clientFactoryBean.getSupplierNameCn());
			supplierDetailBean.setCity(clientFactoryBean.getSupplierCity());
			supplierDetailBean.setCountry(clientFactoryBean.getSupplierCountry());
			supplierDetailBean.setAddress(clientFactoryBean.getSupplierAddress());
			supplierDetailBean.setPostcode(clientFactoryBean.getSupplierPostcode());
			supplierDetailBean.setNearestOffice(clientFactoryBean.getSupplierAiOffice());
			supplierDetailBean.setWebsite(clientFactoryBean.getSupplierWebsite());
			supplierDetailBean.setSalesTurnover(clientFactoryBean.getSupplierSalesTurnover());
			supplierDetailBean.setNoOfEmployees(clientFactoryBean.getSupplierNbEmployees());
			supplierDetailBean.setUserId(userId);

			supplierDetailBean.setMainProductLines(clientFactoryBean.getSupplierProducts());

			SupplierContactInfoBean contactInfoBean = new SupplierContactInfoBean();
			contactInfoBean.setMain(clientFactoryBean.getSupplierManager());
			contactInfoBean.setAlternate(clientFactoryBean.getSupplierQualityManager());
			supplierDetailBean.setContactInfo(contactInfoBean);

			List<FileDetailBean> accessMapList = new ArrayList<FileDetailBean>();
			List<AttachmentDocBean> attachmentDocList = clientFactoryBean.getAccessMap();
			for (AttachmentDocBean docBean : attachmentDocList) {
				String fileId = docBean.getId();
				if (fileId != null && fileId != "") {
					FileDetailBean accessMapBean = new FileDetailBean();
					String fileUrl = config.getFileServiceUrl() + "/getFileInfoById?id=" + fileId;
					GetRequest fileRequest = GetRequest.newInstance().setUrl(fileUrl);
					ServiceCallResult fileResult = HttpUtil.issueGetRequest(fileRequest);
					FileMetaBean fileMetaBean = JsonUtil.mapToObject(fileResult.getResponseString(), FileMetaBean.class);
					accessMapBean.setId(fileId);
					accessMapBean.setDocType(fileMetaBean.getFileType());
					accessMapBean.setFileName(fileMetaBean.getFileName());
					accessMapBean.setFileSize(fileMetaBean.getFileSize());
					accessMapBean.setUrl("/user/" + userId + "/file/" + fileId);
					accessMapList.add(accessMapBean);
				}
			}
			supplierDetailBean.setAccessMaps(accessMapList);

			List<FileDetailBean> qualityDocList = new ArrayList<FileDetailBean>();

			addQualityDocList(userId, clientFactoryBean.getBusLicDocBean(), qualityDocList);
			addQualityDocList(userId, clientFactoryBean.getTaxCertDocBean(), qualityDocList);
			addQualityDocList(userId, clientFactoryBean.getIsoCertDocBean(), qualityDocList);
			addQualityDocList(userId, clientFactoryBean.getExportLicDocBean(), qualityDocList);
			addQualityDocList(userId, clientFactoryBean.getRohsCertDocBean(), qualityDocList);
			addQualityDocList(userId, clientFactoryBean.getTestReportDocBean(), qualityDocList);
			addQualityDocList(userId, clientFactoryBean.getOtherDocBean(), qualityDocList);

			supplierDetailBean.setQualityDocs(qualityDocList);

		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
			result.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			result.setReasonPhase("error when getting supplier detail info.");
			result.setResponseString("error when getting supplier detail info.");
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return supplierDetailBean;
	}

	@Override
	public boolean updateSupplierDetailInfo(SupplierDetailBean supplierDetailBean) throws IOException, AIException {
		//String url = config.getFactoryServiceUrl() + "/saveSupplier";
		String url = config.getFactoryServiceUrl() + "/saveSupplierOnly";

		try {
			ClientFactoryBean clientFactoryBean = convertToClientFactoryBean(supplierDetailBean);
			ServiceCallResult result = HttpUtil.issuePostRequest(url, null, clientFactoryBean);
			if (result.getStatusCode() == HttpStatus.OK.value() &&
					result.getReasonPhase().equalsIgnoreCase("OK")) {

				return true;
			}
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

	@Override
	public boolean deleteSuppliers(String supplierIds) throws IOException, AIException {
		String url = config.getFactoryServiceUrl() + "/deleteSupplier/" + supplierIds;
		try {
			ServiceCallResult result = HttpUtil.issueDeleteRequest(url, null);
			if (result.getStatusCode() == HttpStatus.OK.value() &&
					result.getReasonPhase().equalsIgnoreCase("OK")) {

				return true;
			}
		} catch (IOException e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
		return false;
	}


	private ClientFactoryBean convertToClientFactoryBean(SupplierDetailBean supplierDetailBean) {
		if (supplierDetailBean != null) {
			ClientFactoryBean clientFactoryBean = new ClientFactoryBean();
			clientFactoryBean.setSupplierId(supplierDetailBean.getId());
			clientFactoryBean.setSupplierAddress(supplierDetailBean.getAddress());
			clientFactoryBean.setSupplierAiOffice(supplierDetailBean.getNearestOffice());
			clientFactoryBean.setSupplierCity(supplierDetailBean.getCity());
			clientFactoryBean.setSupplierCountry(supplierDetailBean.getCountry());
			clientFactoryBean.setSupplierPostcode(supplierDetailBean.getPostcode());
			clientFactoryBean.setSupplierNameEn(supplierDetailBean.getEntityName());
			clientFactoryBean.setSupplierNameCn(supplierDetailBean.getChineseName());

			clientFactoryBean.setSupplierWebsite(supplierDetailBean.getWebsite());
			clientFactoryBean.setSupplierSalesTurnover(supplierDetailBean.getSalesTurnover());
			clientFactoryBean.setSupplierNbEmployees(supplierDetailBean.getNoOfEmployees());

			clientFactoryBean.setCustId(supplierDetailBean.getUserId());
			if (supplierDetailBean.getContactInfo() != null) {
				clientFactoryBean.setSupplierQualityManager(supplierDetailBean.getContactInfo().getAlternate());
				clientFactoryBean.setSupplierManager(supplierDetailBean.getContactInfo().getMain());
			}
			clientFactoryBean.setSupplierProducts(supplierDetailBean.getMainProductLines());

			List<FileDetailBean> accessMapList = supplierDetailBean.getAccessMaps();
			if (accessMapList != null) {
				List<AttachmentDocBean> attachmentDocList = new ArrayList<AttachmentDocBean>();
				for (FileDetailBean fileDetailBean : accessMapList) {
					AttachmentDocBean attachmentDocBean = createAttachmentDocBeanFromFileDetailBean(fileDetailBean);
					attachmentDocList.add(attachmentDocBean);
				}
				clientFactoryBean.setAccessMap(attachmentDocList);
			}
			List<FileDetailBean> qualityDocList = supplierDetailBean.getQualityDocs();
			if (qualityDocList != null) {
				for (FileDetailBean fileDetailBean : qualityDocList) {
					if (fileDetailBean != null) {
						AttachmentDocBean docBean = createAttachmentDocBeanFromFileDetailBean(fileDetailBean);
						if (fileDetailBean.getDocType().equalsIgnoreCase("BUS_LIC")) {
							clientFactoryBean.setBusLicDocBean(docBean);
						} else if (fileDetailBean.getDocType().equalsIgnoreCase("ISO_CERT")) {
							clientFactoryBean.setIsoCertDocBean(docBean);
						} else if (fileDetailBean.getDocType().equalsIgnoreCase("EXPORT_LIC")) {
							clientFactoryBean.setExportLicDocBean(docBean);
						} else if (fileDetailBean.getDocType().equalsIgnoreCase("ROHS_CERT")) {
							clientFactoryBean.setRohsCertDocBean(docBean);
						} else if (fileDetailBean.getDocType().equalsIgnoreCase("OTHER_DOC")) {
							clientFactoryBean.setOtherDocBean(docBean);
						} else if (fileDetailBean.getDocType().equalsIgnoreCase("TAX_CERT")) {
							clientFactoryBean.setTaxCertDocBean(docBean);
						}
					}
				}
			}
			return clientFactoryBean;
		}
		return null;
	}

	private AttachmentDocBean createAttachmentDocBeanFromFileDetailBean(FileDetailBean fileDetailBean) {
		AttachmentDocBean attachmentDocBean = new AttachmentDocBean();
		if (fileDetailBean != null) {
			attachmentDocBean.setDocType(fileDetailBean.getDocType());
			attachmentDocBean.setFileName(fileDetailBean.getFileName());
			attachmentDocBean.setId(fileDetailBean.getId());
		}
		return attachmentDocBean;
	}


	private void addQualityDocList(String userId, AttachmentDocBean attachmentDocBean, List<FileDetailBean> qualityDocList) {
		FileDetailBean fileDetailBean = new FileDetailBean();
		fileDetailBean.setDocType(attachmentDocBean.getDocType());
		setFileInfo(userId, attachmentDocBean.getId(), fileDetailBean);
		qualityDocList.add(fileDetailBean);
	}

	private void setFileInfo(String userId, String fileId, FileDetailBean bean) {
		try {
			if (fileId != null && fileId != "") {
				String fileUrl = config.getFileServiceUrl() + "/getFileInfoById?id=" + fileId;
				GetRequest fileRequest = GetRequest.newInstance().setUrl(fileUrl);
				ServiceCallResult fileResult = HttpUtil.issueGetRequest(fileRequest);
				FileMetaBean fileMetaBean = JsonUtil.mapToObject(fileResult.getResponseString(), FileMetaBean.class);
				//bean.setDocType(fileMetaBean.getFileType());
				bean.setId(fileId);
				bean.setFileName(fileMetaBean.getFileName());
				bean.setFileSize(fileMetaBean.getFileSize());
				//bean.setUrl(config.getFileServiceUrl() + "/getFile?id=" + id);
				bean.setUrl("/user/" + userId + "/file/" + fileId);
			}
		} catch (Exception e) {
			LOGGER.error(ExceptionUtils.getStackTrace(e));
		}
	}
}

