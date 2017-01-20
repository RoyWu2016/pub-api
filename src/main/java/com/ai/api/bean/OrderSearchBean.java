package com.ai.api.bean;

import java.io.Serializable;
import java.util.List;

import com.ai.commons.beans.order.SimpleOrderSearchBean;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("rawtypes")
public class OrderSearchBean extends SimpleOrderSearchBean implements Serializable {

	private static final long serialVersionUID = -8209861354874434803L;
    
    private String testStartDate;
    private String office;
    private String program;
    private String reportDueDate;
    private String decision;
    private String labOrderNo;
    private Integer bookingStatus;
    private String manufacturerStyleNo;
    private String overallRating;
    private String clientStatus;
    private String attachmentId;
	private List tests;
    
    public OrderSearchBean() {
    	super();
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

	public String getReportDueDate() {
		return reportDueDate;
	}

	public void setReportDueDate(String reportDueDate) {
		this.reportDueDate = reportDueDate;
	}

	public String getDecision() {
		return decision;
	}

	public void setDecision(String decision) {
		this.decision = decision;
	}

	public String getLabOrderNo() {
		return labOrderNo;
	}

	public void setLabOrderNo(String labOrderNo) {
		this.labOrderNo = labOrderNo;
	}

	public Integer getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(Integer bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

	public String getManufacturerStyleNo() {
		return manufacturerStyleNo;
	}

	public void setManufacturerStyleNo(String manufacturerStyleNo) {
		this.manufacturerStyleNo = manufacturerStyleNo;
	}

	public String getOverallRating() {
		return overallRating;
	}

	public void setOverallRating(String overallRating) {
		this.overallRating = overallRating;
	}

	public String getClientStatus() {
		return clientStatus;
	}

	public void setClientStatus(String clientStatus) {
		this.clientStatus = clientStatus;
	}

	public String getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}

	public List getTests() {
		return tests;
	}

	public void setTests(List tests) {
		this.tests = tests;
	}
}
