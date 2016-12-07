package com.ai.api.bean.finance;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

/**
 * Project Name    : Public-API
 * Package Name    : com.ai.api.bean.finance
 * Creation Date   : 2016/12/7 10:50
 * Author          : Jianxiong.Cai
 * Purpose         : TODO
 * History         : TODO
 */
public class NSLog implements Serializable {

    private String id;
    private int recordStatus;
    private Date modifiedDate;
    private String etlId;// customerId, orderId, employeeId
    private Long nsInternalId;
    private String type;// customer, order, employee
    private String result;// Success or Failed
    private String errorCode;
    private String errorDetails;
    private Date createDate;


    @JsonIgnore
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonIgnore
    public int getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(int recordStatus) {
        this.recordStatus = recordStatus;
    }

    @JsonIgnore
    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getEtlId() {
        return etlId;
    }

    public void setEtlId(String etlId) {
        this.etlId = etlId;
    }

    public Long getNsInternalId() {
        return nsInternalId;
    }

    public void setNsInternalId(Long nsInternalId) {
        this.nsInternalId = nsInternalId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(String errorDetails) {
        this.errorDetails = errorDetails;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "NSLog{" +
                "id='" + id + '\'' +
                ", recordStatus=" + recordStatus +
                ", modifiedDate=" + modifiedDate +
                ", etlId='" + etlId + '\'' +
                ", nsInternalId=" + nsInternalId +
                ", type='" + type + '\'' +
                ", result='" + result + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", errorDetails='" + errorDetails + '\'' +
                ", createDate=" + createDate +
                '}';
    }
}
