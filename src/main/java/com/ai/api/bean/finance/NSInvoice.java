package com.ai.api.bean.finance;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

/**
 * Project Name    : Public-API
 * Package Name    : com.ai.api.bean.finance
 * Creation Date   : 2016/12/28 16:07
 * Author          : Jianxiong.Cai
 * Purpose         : TODO
 * History         : TODO
 */
public class NSInvoice implements Serializable {

    private String id;
    private int recordStatus;
    private Date modifiedDate;

    private Long invId;
    private String invNm;
    private String invoiceCompany;
    private String invoiceLogin;
    private Date invDate;
    private String login; // invoiceCustID
    private String orderIdList;
    private String cnList;
    private String dnList;
    private Date createDate;
    private String orderSyncFailure;
    private String cndnSyncFailure;

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

    public Long getInvId() {
        return invId;
    }

    public void setInvId(Long invId) {
        this.invId = invId;
    }

    public String getInvNm() {
        return invNm;
    }

    public void setInvNm(String invNm) {
        this.invNm = invNm;
    }

    public String getInvoiceCompany() {
        return invoiceCompany;
    }

    public void setInvoiceCompany(String invoiceCompany) {
        this.invoiceCompany = invoiceCompany;
    }

    public String getInvoiceLogin() {
        return invoiceLogin;
    }

    public void setInvoiceLogin(String invoiceLogin) {
        this.invoiceLogin = invoiceLogin;
    }

    @JsonFormat(pattern = "d/M/yyyy", timezone = "GMT+8")
    public Date getInvDate() {
        return invDate;
    }

    public void setInvDate(Date invDate) {
        this.invDate = invDate;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getOrderIdList() {
        return orderIdList;
    }

    public void setOrderIdList(String orderIdList) {
        this.orderIdList = orderIdList;
    }

    public String getCnList() {
        return cnList;
    }

    public void setCnList(String cnList) {
        this.cnList = cnList;
    }

    public String getDnList() {
        return dnList;
    }

    public void setDnList(String dnList) {
        this.dnList = dnList;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getOrderSyncFailure() {
        return orderSyncFailure;
    }

    public void setOrderSyncFailure(String orderSyncFailure) {
        this.orderSyncFailure = orderSyncFailure;
    }

    public String getCndnSyncFailure() {
        return cndnSyncFailure;
    }

    public void setCndnSyncFailure(String cndnSyncFailure) {
        this.cndnSyncFailure = cndnSyncFailure;
    }

    @Override
    public String toString() {
        return "NSInvoice{" +
                "id='" + id + '\'' +
                ", recordStatus=" + recordStatus +
                ", modifiedDate=" + modifiedDate +
                ", invId=" + invId +
                ", invNm='" + invNm + '\'' +
                ", invoiceCompany='" + invoiceCompany + '\'' +
                ", invoiceLogin='" + invoiceLogin + '\'' +
                ", invDate=" + invDate +
                ", login='" + login + '\'' +
                ", orderIdList='" + orderIdList + '\'' +
                ", cnList='" + cnList + '\'' +
                ", dnList='" + dnList + '\'' +
                ", createDate=" + createDate +
                ", orderSyncFailure='" + orderSyncFailure + '\'' +
                ", cndnSyncFailure='" + cndnSyncFailure + '\'' +
                '}';
    }
}
