package com.ai.api.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderSearchBean implements Serializable {

	private static final long serialVersionUID = -8209861354874434803L;

    private String orderId;
    private String serviceType;
    private String serviceTypeText;
    private String status;   //order status
    private String statusText;
    private String supplierName;
    private String refNumber;
    private String clientReference;
    private String productNames;
    private String poNumbers;
    private String serviceDate;
    private String bookingDate;
    
    private String testStartDate;
    private String office;
    private String program;
    private String reportIssueDate;
    private String decision;
    
    public OrderSearchBean() {}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getServiceTypeText() {
		return serviceTypeText;
	}

	public void setServiceTypeText(String serviceTypeText) {
		this.serviceTypeText = serviceTypeText;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusText() {
		return statusText;
	}

	public void setStatusText(String statusText) {
		this.statusText = statusText;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getRefNumber() {
		return refNumber;
	}

	public void setRefNumber(String refNumber) {
		this.refNumber = refNumber;
	}

	public String getClientReference() {
		return clientReference;
	}

	public void setClientReference(String clientReference) {
		this.clientReference = clientReference;
	}

	public String getProductNames() {
		return productNames;
	}

	public void setProductNames(String productNames) {
		this.productNames = productNames;
	}

	public String getPoNumbers() {
		return poNumbers;
	}

	public void setPoNumbers(String poNumbers) {
		this.poNumbers = poNumbers;
	}

	public String getServiceDate() {
		return serviceDate;
	}

	public void setServiceDate(String serviceDate) {
		this.serviceDate = serviceDate;
	}

	public String getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(String bookingDate) {
		this.bookingDate = bookingDate;
	}

	public String getTestStartDate() {
		return testStartDate;
	}

	public void setTestStartDate(String testStartDate) {
		this.testStartDate = testStartDate;
	}

	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
	}

	public String getReportIssueDate() {
		return reportIssueDate;
	}

	public void setReportIssueDate(String reportIssueDate) {
		this.reportIssueDate = reportIssueDate;
	}

	public String getDecision() {
		return decision;
	}

	public void setDecision(String decision) {
		this.decision = decision;
	}
}
