package com.ai.api.bean.finance;

import com.ai.api.util.DateDeserialize;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.Serializable;
import java.util.Date;

/**
 * Project Name    : Public-API
 * Package Name    : com.ai.api.bean.finance
 * Creation Date   : 2016/12/7 10:44
 * Author          : Jianxiong.Cai
 * Purpose         : TODO
 * History         : TODO
 */
public class NSCreditDebitMemo implements Serializable {

    private static final long serialVersionUID = 7555739397260895545L;


    private String id;
    private int recordStatus;
    private Date modifiedDate;
    private String orderNo;
    private String companyId;
    private String companyName;
    private String login;
    private Date invoiceDate;
    private String debitNoteNo;
    private String sic;
    private String isCHB;
    private String invoiceNum;
    private double total;
    private double exactPayment;
    private double outstanding;
    private double exchangeRate;
    private String currency;
    private String category;
    private String categoryType;
    private int count; // just for mybatis saveOrUpdate

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


    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @JsonFormat(pattern = "d/M/yyyy", timezone = "GMT+8")
    @JsonDeserialize(using = DateDeserialize.class)
    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getDebitNoteNo() {
        return debitNoteNo;
    }

    public void setDebitNoteNo(String debitNoteNo) {
        this.debitNoteNo = debitNoteNo;
    }

    public String getSic() {
        return sic;
    }

    public void setSic(String sic) {
        this.sic = sic;
    }

    public String getIsCHB() {
        return isCHB;
    }

    public void setIsCHB(String isCHB) {
        this.isCHB = isCHB;
    }

    public String getInvoiceNum() {
        return invoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getExactPayment() {
        return exactPayment;
    }

    public void setExactPayment(double exactPayment) {
        this.exactPayment = exactPayment;
    }

    public double getOutstanding() {
        return outstanding;
    }

    public void setOutstanding(double outstanding) {
        this.outstanding = outstanding;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    @JsonIgnore
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "NSCreditDebitMemo{" +
                "id='" + id + '\'' +
                ", recordStatus=" + recordStatus +
                ", modifiedDate=" + modifiedDate +
                ", orderNo='" + orderNo + '\'' +
                ", companyId='" + companyId + '\'' +
                ", companyName='" + companyName + '\'' +
                ", login='" + login + '\'' +
                ", invoiceDate=" + invoiceDate +
                ", debitNoteNo='" + debitNoteNo + '\'' +
                ", sic='" + sic + '\'' +
                ", isCHB='" + isCHB + '\'' +
                ", invoiceNum='" + invoiceNum + '\'' +
                ", total=" + total +
                ", exactPayment=" + exactPayment +
                ", outstanding=" + outstanding +
                ", exchangeRate=" + exchangeRate +
                ", currency='" + currency + '\'' +
                ", category='" + category + '\'' +
                ", categoryType='" + categoryType + '\'' +
                ", count=" + count +
                '}';
    }
}
