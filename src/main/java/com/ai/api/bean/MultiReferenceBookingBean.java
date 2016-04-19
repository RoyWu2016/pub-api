package com.ai.api.bean;

/**
 * Created by KK on 3/21/2016.
 */
public class MultiReferenceBookingBean {

    private String approveReferences;

    private String askNumberOfReferences;

    private int numberOfReferencePerProduct;

    private int numberOfReferencePerReport;

    private int numberOfReferencePerManday;

    private int numberOfPcsPerReference;

    private int numberOfReportPerManday;

    private int clcNumberOfReports;

    private int clcNumberOfContainer;

    private String peoCalculation;

    private int containerRate;

    public String getApproveReferences() {
        return approveReferences;
    }

    public void setApproveReferences(String approveReferences) {
        this.approveReferences = approveReferences;
    }

    public String getAskNumberOfReferences() {
        return askNumberOfReferences;
    }

    public void setAskNumberOfReferences(String askNumberOfReferences) {
        this.askNumberOfReferences = askNumberOfReferences;
    }

    public int getNumberOfReferencePerProduct() {
        return numberOfReferencePerProduct;
    }

    public void setNumberOfReferencePerProduct(int numberOfReferencePerProduct) {
        this.numberOfReferencePerProduct = numberOfReferencePerProduct;
    }

    public int getNumberOfReferencePerReport() {
        return numberOfReferencePerReport;
    }

    public void setNumberOfReferencePerReport(int numberOfReferencePerReport) {
        this.numberOfReferencePerReport = numberOfReferencePerReport;
    }

    public int getNumberOfReferencePerManday() {
        return numberOfReferencePerManday;
    }

    public void setNumberOfReferencePerManday(int numberOfReferencePerManday) {
        this.numberOfReferencePerManday = numberOfReferencePerManday;
    }

    public int getNumberOfPcsPerReference() {
        return numberOfPcsPerReference;
    }

    public void setNumberOfPcsPerReference(int numberOfPcsPerReference) {
        this.numberOfPcsPerReference = numberOfPcsPerReference;
    }

    public int getNumberOfReportPerManday() {
        return numberOfReportPerManday;
    }

    public void setNumberOfReportPerManday(int numberOfReportPerManday) {
        this.numberOfReportPerManday = numberOfReportPerManday;
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
}
