package com.ai.api.bean;

import java.io.Serializable;

/**
 * Created by Henry Yue on 2016/8/8.
 */
public class CompanyLogoBean implements Serializable {

    private String userId;
    private String companyId;
    private String fileName;
    private String fileOriginalName;
    private String encodedImageStr;
    private long fileSize;

    public CompanyLogoBean(){}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileOriginalName() {
        return fileOriginalName;
    }

    public void setFileOriginalName(String fileOriginalName) {
        this.fileOriginalName = fileOriginalName;
    }

    public String getEncodedImageStr() {
        return encodedImageStr;
    }

    public void setEncodedImageStr(String encodedImageStr) {
        this.encodedImageStr = encodedImageStr;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
}
