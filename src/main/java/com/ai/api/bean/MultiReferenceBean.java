package com.ai.api.bean;

import java.io.Serializable;

/**
 * Created by yan on 2016/7/21.
 */
public class MultiReferenceBean implements Serializable {

    private boolean clientCanApproveRejectIndividualProductReferences;
    private boolean askNumberOfReferences;
    private int numberOfRefPerProduct;
    private int numberOfRefPerReport;
    private int numberOfRefPerMd;
    private int numberOfPcsPerRef;
    private int numberOfReportPerMd;
    private int clcNumberOfReports;
    private int clcNumberOfContainer;
    private String peoCalculation;
    private int containerRate;
    private String showRefResultOnline;

    public MultiReferenceBean() { }

    public boolean isClientCanApproveRejectIndividualProductReferences() {
        return clientCanApproveRejectIndividualProductReferences;
    }

    public void setClientCanApproveRejectIndividualProductReferences(boolean clientCanApproveRejectIndividualProductReferences) {
        this.clientCanApproveRejectIndividualProductReferences = clientCanApproveRejectIndividualProductReferences;
    }

    public boolean isAskNumberOfReferences() {
        return askNumberOfReferences;
    }

    public void setAskNumberOfReferences(boolean askNumberOfReferences) {
        this.askNumberOfReferences = askNumberOfReferences;
    }

    public int getNumberOfRefPerProduct() {
        return numberOfRefPerProduct;
    }

    public void setNumberOfRefPerProduct(int numberOfRefPerProduct) {
        this.numberOfRefPerProduct = numberOfRefPerProduct;
    }

    public int getNumberOfRefPerReport() {
        return numberOfRefPerReport;
    }

    public void setNumberOfRefPerReport(int numberOfRefPerReport) {
        this.numberOfRefPerReport = numberOfRefPerReport;
    }

    public int getNumberOfRefPerMd() {
        return numberOfRefPerMd;
    }

    public void setNumberOfRefPerMd(int numberOfRefPerMd) {
        this.numberOfRefPerMd = numberOfRefPerMd;
    }

    public int getNumberOfPcsPerRef() {
        return numberOfPcsPerRef;
    }

    public void setNumberOfPcsPerRef(int numberOfPcsPerRef) {
        this.numberOfPcsPerRef = numberOfPcsPerRef;
    }

    public int getNumberOfReportPerMd() {
        return numberOfReportPerMd;
    }

    public void setNumberOfReportPerMd(int numberOfReportPerMd) {
        this.numberOfReportPerMd = numberOfReportPerMd;
    }

    public int getClcNumberOfReports() {
        return clcNumberOfReports;
    }

    public void setClcNumberOfReports(int clcNumberOfReports) {
        this.clcNumberOfReports = clcNumberOfReports;
    }

    public int getClcNumberOfContainer() {
        return clcNumberOfContainer;
    }

    public void setClcNumberOfContainer(int clcNumberOfContainer) {
        this.clcNumberOfContainer = clcNumberOfContainer;
    }

    public String getPeoCalculation() {
        return peoCalculation;
    }

    public void setPeoCalculation(String peoCalculation) {
        this.peoCalculation = peoCalculation;
    }

    public int getContainerRate() {
        return containerRate;
    }

    public void setContainerRate(int containerRate) {
        this.containerRate = containerRate;
    }

    public String getShowRefResultOnline() {
        return showRefResultOnline;
    }

    public void setShowRefResultOnline(String showRefResultOnline) {
        this.showRefResultOnline = showRefResultOnline;
    }

    @Override
    public String toString() {
        return "MultiReferenceBean{" +
                "clientCanApproveRejectIndividualProductReferences=" + clientCanApproveRejectIndividualProductReferences +
                ", askNumberOfReferences=" + askNumberOfReferences +
                ", numberOfRefPerProduct=" + numberOfRefPerProduct +
                ", numberOfRefPerReport=" + numberOfRefPerReport +
                ", numberOfRefPerMd=" + numberOfRefPerMd +
                ", numberOfPcsPerRef=" + numberOfPcsPerRef +
                ", numberOfReportPerMd=" + numberOfReportPerMd +
                ", clcNumberOfReports=" + clcNumberOfReports +
                ", clcNumberOfContainer=" + clcNumberOfContainer +
                ", peoCalculation='" + peoCalculation + '\'' +
                ", containerRate=" + containerRate +
                ", showRefResultOnline='" + showRefResultOnline + '\'' +
                '}';
    }
}
