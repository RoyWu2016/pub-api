package com.ai.api.bean.legacy;

/**
 * Created by Administrator on 2016/6/30 0030.
 */

import java.io.Serializable;

public class AttachmentDocBean implements Serializable {

    private static final long serialVersionUID = -4618253030720659674L;

    private String docType = "";
    private String isSelected = "";
    private String fileName = "";
    private String otherName = "";

    private String id = ""; //FS_META: ID

    public AttachmentDocBean() {
    }

    public AttachmentDocBean(String docType, String fileName) {
        super();
        this.docType = docType;
        this.fileName = fileName;
    }

    public AttachmentDocBean(String docType, String isSelected, String fileName) {
        super();
        this.docType = docType;
        this.isSelected = isSelected;
        this.fileName = fileName;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }

    public String getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AttachmentDocBean [docType=" + docType + ", isSelected="
                + isSelected + ", fileName=" + fileName + ", otherName="
                + otherName + ", id=" + id + "]";
    }

}

