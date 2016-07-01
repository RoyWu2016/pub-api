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
import com.ai.commons.HttpUtil;
import com.ai.commons.JsonUtil;
import com.ai.commons.beans.GetRequest;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.fileservice.FileMetaBean;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Created by Administrator on 2016/6/29 0029.
 */
public class FactoryDaoImpl implements FactoryDao {

    private static final Logger LOGGER = Logger.getLogger(FactoryDaoImpl.class);

    @Autowired
    @Qualifier("serviceConfig")
    private ServiceConfig config;

    @Override
    public List<FactorySearchBean> getSuppliersByUserId(String userId) throws IOException, AIException{
        String url = config.getFactoryServiceUrl() + "/search?universalId="+userId+"&criteria=";
        GetRequest request = GetRequest.newInstance().setUrl(url);
        ServiceCallResult result = new ServiceCallResult();
        try {
            result = HttpUtil.issueGetRequest(request);
            return JsonUtil.mapToObject(result.getResponseString(), new TypeReference<List<FactorySearchBean>>() {});

        } catch (IOException e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            result.setStatusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.setReasonPhase("error when getting user suppliers.");
            result.setResponseString("error when getting user suppliers.");
        }
        return null;
    }

    @Override
    public SupplierDetailBean getUserSupplierDetailInfoById(String userId, String supplierId){
        String url = config.getFactoryServiceUrl() + "/getSupplierBySupplierId/"+supplierId;
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
            for(AttachmentDocBean docBean: attachmentDocList){
                String fileId = docBean.getId();
                if(fileId!=null && fileId!="") {
                    FileDetailBean accessMapBean = new FileDetailBean();
                    String fileUrl = config.getFileServiceUrl() + "/getFileInfoById?id="+fileId;
                    GetRequest fileRequest = GetRequest.newInstance().setUrl(fileUrl);
                    ServiceCallResult fileResult = HttpUtil.issueGetRequest(fileRequest);
                    FileMetaBean fileMetaBean = JsonUtil.mapToObject(fileResult.getResponseString(), FileMetaBean.class);
                    accessMapBean.setId(fileId);
                    accessMapBean.setDocType(fileMetaBean.getFileType());
                    accessMapBean.setFileName(fileMetaBean.getFileName());
                    accessMapBean.setFileSize(fileMetaBean.getFileSize());
                    accessMapBean.setUrl(config.getFileServiceUrl() + "/getFile?id="+fileId);
                    accessMapList.add(accessMapBean);
                }
            }
            supplierDetailBean.setAccessMaps(accessMapList);

            List<FileDetailBean> qualityDocList = new ArrayList<FileDetailBean>();

            addQualityDocList(qualityDocList,clientFactoryBean.getBusLicDocBean());
            addQualityDocList(qualityDocList,clientFactoryBean.getIsoCertDocBean());
            addQualityDocList(qualityDocList,clientFactoryBean.getExportLicDocBean());
            addQualityDocList(qualityDocList,clientFactoryBean.getRohsCertDocBean());
            addQualityDocList(qualityDocList,clientFactoryBean.getTestReportDocBean());
            addQualityDocList(qualityDocList,clientFactoryBean.getOtherDocBean());

            supplierDetailBean.setQualityDocs(qualityDocList);

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

    private void addQualityDocList(List<FileDetailBean> qualityDocList,AttachmentDocBean attachmentDocBean){
        FileDetailBean fileDetailBean = new FileDetailBean();
        fileDetailBean.setDocType(attachmentDocBean.getDocType());
        setFileInfo(fileDetailBean, attachmentDocBean.getId());
        qualityDocList.add(fileDetailBean);
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
                bean.setFileSize(fileMetaBean.getFileSize());
                bean.setUrl(config.getFileServiceUrl() + "/getFile?id=" + id);
            }
        }catch(Exception e){
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
    }
}

