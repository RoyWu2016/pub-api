package com.ai.api.bean.legacy;

/**
 * Created by Administrator on 2016/6/30 0030.
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ClientFactoryBean implements Serializable {

    private static final long serialVersionUID = -6653696172657329808L;

    private String supplierId = "";
    private String supplierAddress  = "";
    private String supplierAiOffice = "";
    private String supplierCity = "";
    private String supplierCountry = "";
    private String supplierPostcode = "";
    private String supplierNameCn = "";
    private String supplierNameEn = "";
    private ContactBean supplierQualityManager = new ContactBean();
    private ContactBean supplierManager  = new ContactBean();
    private String isNewSupplier  = "";
    private List<String> supplierProducts = new ArrayList<String>();

    private String supplierWebsite = "";
    private String supplierSalesTurnover = "";
    private String supplierNbEmployees = "";

    private ContactBean supplierSalesManager = new ContactBean();

    private String supplierOrderId  = "";
    private String supplierCreatedBy = "";

	/*
	private String busLic;
	private String taxCert;
	private String isoCert;
	private String exportLic;
	private String rohsCert;
	private String testReport;
	private List<String> otherName;
	*/

    private AttachmentDocBean busLicDocBean = new AttachmentDocBean();
    private AttachmentDocBean taxCertDocBean  = new AttachmentDocBean();
    private AttachmentDocBean isoCertDocBean = new AttachmentDocBean();
    private AttachmentDocBean exportLicDocBean = new AttachmentDocBean();
    private AttachmentDocBean rohsCertDocBean = new AttachmentDocBean();
    private AttachmentDocBean testReportDocBean = new AttachmentDocBean();
    private AttachmentDocBean otherDocBean = new AttachmentDocBean();

    private String factoryId = "";
    private String factoryAddress = "";
    private String factoryAiOffice = "";
    private String factoryCity = "";
    private String factoryCountry = "";
    private String factoryPostcode = "";
    private String factoryNameCn = "";
    private String factoryNameEn = "";
    private String factoryContinent = "";
    private String factoryProvince = "";
    private ContactBean factoryQualityManager = new ContactBean();
    private ContactBean factoryManager = new ContactBean();
    private List<String> factoryProducts = new ArrayList<String>();

//	private String factorySize;

    private String supplierAttachmentIdAccessMap = "";

    private String orderFactoryIsMultilocation = "";

    private List<String> clientCompanyNames = new ArrayList<String>();

    private boolean saveSupplier = true;
    private boolean saveFactory = true;

    private String url = "";

    private List<AttachmentDocBean> accessMap = new ArrayList<AttachmentDocBean>();

    private String custId = "";
    private String custLogin = "";

    public ClientFactoryBean() {
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierAddress() {
        return supplierAddress;
    }

    public void setSupplierAddress(String supplierAddress) {
        this.supplierAddress = supplierAddress;
    }

    public String getSupplierAiOffice() {
        return supplierAiOffice;
    }

    public void setSupplierAiOffice(String supplierAiOffice) {
        this.supplierAiOffice = supplierAiOffice;
    }

    public String getSupplierCity() {
        return supplierCity;
    }

    public void setSupplierCity(String supplierCity) {
        this.supplierCity = supplierCity;
    }

    public String getSupplierCountry() {
        return supplierCountry;
    }

    public void setSupplierCountry(String supplierCountry) {
        this.supplierCountry = supplierCountry;
    }

    public String getSupplierPostcode() {
        return supplierPostcode;
    }

    public void setSupplierPostcode(String supplierPostcode) {
        this.supplierPostcode = supplierPostcode;
    }

    public String getSupplierNameCn() {
        return supplierNameCn;
    }

    public void setSupplierNameCn(String supplierNameCn) {
        this.supplierNameCn = supplierNameCn;
    }

    public String getSupplierNameEn() {
        return supplierNameEn;
    }

    public void setSupplierNameEn(String supplierNameEn) {
        this.supplierNameEn = supplierNameEn;
    }

    public ContactBean getSupplierQualityManager() {
        return supplierQualityManager;
    }

    public void setSupplierQualityManager(ContactBean qualityManager) {
        this.supplierQualityManager = qualityManager;
    }

    public ContactBean getSupplierManager() {
        return supplierManager;
    }

    public void setSupplierManager(ContactBean supplierManager) {
        this.supplierManager = supplierManager;
    }

    public String getIsNewSupplier() {
        return isNewSupplier;
    }

    public void setIsNewSupplier(String isNewSupplier) {
        this.isNewSupplier = isNewSupplier;
    }

    public List<String> getSupplierProducts() {
        return supplierProducts;
    }

    public void setSupplierProducts(List<String> supplierProducts) {
        this.supplierProducts = supplierProducts;
    }

    public String getFactoryId() {
        return factoryId;
    }

    public void setFactoryId(String factoryId) {
        this.factoryId = factoryId;
    }

    public String getFactoryAddress() {
        return factoryAddress;
    }

    public void setFactoryAddress(String factoryAddress) {
        this.factoryAddress = factoryAddress;
    }

    public String getFactoryAiOffice() {
        return factoryAiOffice;
    }

    public void setFactoryAiOffice(String factoryAiOffice) {
        this.factoryAiOffice = factoryAiOffice;
    }

    public String getFactoryCity() {
        return factoryCity;
    }

    public void setFactoryCity(String factoryCity) {
        this.factoryCity = factoryCity;
    }

    public String getFactoryCountry() {
        return factoryCountry;
    }

    public void setFactoryCountry(String factoryCountry) {
        this.factoryCountry = factoryCountry;
    }

    public String getFactoryPostcode() {
        return factoryPostcode;
    }

    public void setFactoryPostcode(String factoryPostcode) {
        this.factoryPostcode = factoryPostcode;
    }

    public String getFactoryNameCn() {
        return factoryNameCn;
    }

    public void setFactoryNameCn(String factoryNameCn) {
        this.factoryNameCn = factoryNameCn;
    }

    public String getFactoryNameEn() {
        return factoryNameEn;
    }

    public void setFactoryNameEn(String factoryNameEn) {
        this.factoryNameEn = factoryNameEn;
    }

    public String getFactoryContinent() {
        return factoryContinent;
    }

    public void setFactoryContinent(String factoryContinent) {
        this.factoryContinent = factoryContinent;
    }

    public String getFactoryProvince() {
        return factoryProvince;
    }

    public void setFactoryProvince(String factoryProvince) {
        this.factoryProvince = factoryProvince;
    }

    public ContactBean getFactoryQualityManager() {
        return factoryQualityManager;
    }

    public void setFactoryQualityManager(ContactBean factoryQualityManager) {
        this.factoryQualityManager = factoryQualityManager;
    }

    public ContactBean getFactoryManager() {
        return factoryManager;
    }

    public void setFactoryManager(ContactBean factoryManager) {
        this.factoryManager = factoryManager;
    }

    public List<String> getFactoryProducts() {
        return factoryProducts;
    }

    public void setFactoryProducts(List<String> factoryProducts) {
        this.factoryProducts = factoryProducts;
    }

    public String getSupplierAttachmentIdAccessMap() {
        return supplierAttachmentIdAccessMap;
    }

    public void setSupplierAttachmentIdAccessMap(
            String supplierAttachmentIdAccessMap) {
        this.supplierAttachmentIdAccessMap = supplierAttachmentIdAccessMap;
    }

    public String getOrderFactoryIsMultilocation() {
        return orderFactoryIsMultilocation;
    }

    public void setOrderFactoryIsMultilocation(String orderFactoryIsMultilocation) {
        this.orderFactoryIsMultilocation = orderFactoryIsMultilocation;
    }

    public String getSupplierWebsite() {
        return supplierWebsite;
    }

    public void setSupplierWebsite(String supplierWebsite) {
        this.supplierWebsite = supplierWebsite;
    }

    public String getSupplierSalesTurnover() {
        return supplierSalesTurnover;
    }

    public void setSupplierSalesTurnover(String supplierSalesTurnover) {
        this.supplierSalesTurnover = supplierSalesTurnover;
    }

    public String getSupplierNbEmployees() {
        return supplierNbEmployees;
    }

    public void setSupplierNbEmployees(String supplierNbEmployees) {
        this.supplierNbEmployees = supplierNbEmployees;
    }


    public ContactBean getSupplierSalesManager() {
        return supplierSalesManager;
    }

    public void setSupplierSalesManager(ContactBean supplierSalesManager) {
        this.supplierSalesManager = supplierSalesManager;
    }

    public String getSupplierOrderId() {
        return supplierOrderId;
    }

    public void setSupplierOrderId(String supplierOrderId) {
        this.supplierOrderId = supplierOrderId;
    }

    public String getSupplierCreatedBy() {
        return supplierCreatedBy;
    }

    public void setSupplierCreatedBy(String supplierCreatedBy) {
        this.supplierCreatedBy = supplierCreatedBy;
    }

    public List<String> getClientCompanyNames() {
        return clientCompanyNames;
    }

    public void setClientCompanyNames(List<String> clientCompanyNames) {
        this.clientCompanyNames = clientCompanyNames;
    }

    public boolean isSaveSupplier() {
        return saveSupplier;
    }

    public void setSaveSupplier(boolean saveSupplier) {
        this.saveSupplier = saveSupplier;
    }

    public boolean isSaveFactory() {
        return saveFactory;
    }

    public void setSaveFactory(boolean saveFactory) {
        this.saveFactory = saveFactory;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public AttachmentDocBean getBusLicDocBean() {
        return busLicDocBean;
    }

    public void setBusLicDocBean(AttachmentDocBean busLicDocBean) {
        this.busLicDocBean = busLicDocBean;
    }

    public AttachmentDocBean getTaxCertDocBean() {
        return taxCertDocBean;
    }

    public void setTaxCertDocBean(AttachmentDocBean taxCertDocBean) {
        this.taxCertDocBean = taxCertDocBean;
    }

    public AttachmentDocBean getIsoCertDocBean() {
        return isoCertDocBean;
    }

    public void setIsoCertDocBean(AttachmentDocBean isoCertDocBean) {
        this.isoCertDocBean = isoCertDocBean;
    }

    public AttachmentDocBean getExportLicDocBean() {
        return exportLicDocBean;
    }

    public void setExportLicDocBean(AttachmentDocBean exportLicDocBean) {
        this.exportLicDocBean = exportLicDocBean;
    }

    public AttachmentDocBean getRohsCertDocBean() {
        return rohsCertDocBean;
    }

    public void setRohsCertDocBean(AttachmentDocBean rohsCertDocBean) {
        this.rohsCertDocBean = rohsCertDocBean;
    }

    public AttachmentDocBean getTestReportDocBean() {
        return testReportDocBean;
    }

    public void setTestReportDocBean(AttachmentDocBean testReportDocBean) {
        this.testReportDocBean = testReportDocBean;
    }

    public AttachmentDocBean getOtherDocBean() {
        return otherDocBean;
    }

    public void setOtherDocBean(AttachmentDocBean otherDocBean) {
        this.otherDocBean = otherDocBean;
    }

    public List<AttachmentDocBean> getAccessMap() {
        return accessMap;
    }

    public void setAccessMap(List<AttachmentDocBean> accessMap) {
        this.accessMap = accessMap;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustLogin() {
        return custLogin;
    }

    public void setCustLogin(String custLogin) {
        this.custLogin = custLogin;
    }

    @Override
    public String toString() {
        return "ClientFactoryBean [supplierId=" + supplierId
                + ", supplierAddress=" + supplierAddress
                + ", supplierAiOffice=" + supplierAiOffice + ", supplierCity="
                + supplierCity + ", supplierCountry=" + supplierCountry
                + ", supplierPostcode=" + supplierPostcode
                + ", supplierNameCn=" + supplierNameCn + ", supplierNameEn="
                + supplierNameEn + ", supplierQualityManager="
                + supplierQualityManager + ", supplierManager="
                + supplierManager + ", isNewSupplier=" + isNewSupplier
                + ", supplierProducts=" + supplierProducts
                + ", supplierWebsite=" + supplierWebsite
                + ", supplierSalesTurnover=" + supplierSalesTurnover
                + ", supplierNbEmployees=" + supplierNbEmployees
                + ", supplierSalesManager=" + supplierSalesManager
                + ", supplierOrderId=" + supplierOrderId
                + ", supplierCreatedBy=" + supplierCreatedBy
                + ", busLicDocBean=" + busLicDocBean + ", taxCertDocBean="
                + taxCertDocBean + ", isoCertDocBean=" + isoCertDocBean
                + ", exportLicDocBean=" + exportLicDocBean
                + ", rohsCertDocBean=" + rohsCertDocBean
                + ", testReportDocBean=" + testReportDocBean
                + ", otherDocBean=" + otherDocBean + ", factoryId=" + factoryId
                + ", factoryAddress=" + factoryAddress + ", factoryAiOffice="
                + factoryAiOffice + ", factoryCity=" + factoryCity
                + ", factoryCountry=" + factoryCountry + ", factoryPostcode="
                + factoryPostcode + ", factoryNameCn=" + factoryNameCn
                + ", factoryNameEn=" + factoryNameEn + ", factoryContinent="
                + factoryContinent + ", factoryProvince=" + factoryProvince
                + ", factoryQualityManager=" + factoryQualityManager
                + ", factoryManager=" + factoryManager + ", factoryProducts="
                + factoryProducts + ", supplierAttachmentIdAccessMap="
                + supplierAttachmentIdAccessMap
                + ", orderFactoryIsMultilocation="
                + orderFactoryIsMultilocation + ", clientCompanyNames="
                + clientCompanyNames + ", saveSupplier=" + saveSupplier
                + ", saveFactory=" + saveFactory + ", url=" + url
                + ", accessMap=" + accessMap + ", custId=" + custId
                + ", custLogin=" + custLogin + "]";
    }

}
