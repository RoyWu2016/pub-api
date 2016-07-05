package com.ai.api.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/27 0027.
 */
public class SupplierDetailBean {

    private String id;
    private String entityName;
    private String chineseName;
    private String city;
    private String country;
    private String address;
    private String postcode;
    private String nearestOffice;
    private String website;
    private String salesTurnover;
    private String noOfEmployees;
    private String userId;

    private List<String> mainProductLines;
    private SupplierContactInfoBean contactInfo;
    private List<FileDetailBean> accessMaps;
    private List<FileDetailBean> qualityDocs;

    public SupplierDetailBean(){
        this.id = "";
        this.entityName = "";
        this.chineseName = "";
        this.city = "";
        this.country = "";
        this.address = "";
        this.postcode = "";
        this.nearestOffice = "";
        this.website = "";
        this.salesTurnover = "";
        this.noOfEmployees = "";
        this.userId = "";
        this.mainProductLines = new ArrayList<String>();
        this.contactInfo = new SupplierContactInfoBean();
        this.accessMaps = new ArrayList<FileDetailBean>();
        this.qualityDocs = new ArrayList<FileDetailBean>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getChineseName() {
        return chineseName;
    }

    public void setChineseName(String chineseName) {
        if(chineseName!=null)
            this.chineseName = chineseName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getNearestOffice() {
        return nearestOffice;
    }

    public void setNearestOffice(String nearestOffice) {
        this.nearestOffice = nearestOffice;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getSalesTurnover() {
        return salesTurnover;
    }

    public void setSalesTurnover(String salesTurnover) {
        this.salesTurnover = salesTurnover;
    }

    public String getNoOfEmployees() {
        return noOfEmployees;
    }

    public void setNoOfEmployees(String noOfEmployees) {
        this.noOfEmployees = noOfEmployees;
    }

    public List<String> getMainProductLines() {
        return mainProductLines;
    }

    public void setMainProductLines(List<String> mainProductLines) {
        this.mainProductLines = mainProductLines;
    }

    public SupplierContactInfoBean getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(SupplierContactInfoBean contactInfo) {
        this.contactInfo = contactInfo;
    }

    public List<FileDetailBean> getAccessMaps() {
        return accessMaps;
    }

    public void setAccessMaps(List<FileDetailBean> accessMaps) {
        this.accessMaps = accessMaps;
    }

    public List<FileDetailBean> getQualityDocs() {
        return qualityDocs;
    }

    public void setQualityDocs(List<FileDetailBean> qualityDocs) {
        this.qualityDocs = qualityDocs;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
