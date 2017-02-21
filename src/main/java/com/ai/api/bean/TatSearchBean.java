package com.ai.api.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TatSearchBean implements Serializable {

	private static final long serialVersionUID = 5999550073031242124L;
	
	private String id;

	private String name;
	
	private Integer tat;
	
	private boolean isExpService;
	
	private Integer internalDue;
	
	private String status;
	
	private String office;
	
	private Double surcharge;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getTat() {
		return tat;
	}

	public void setTat(Integer tat) {
		this.tat = tat;
	}

	public boolean getIsExpService() {
		return isExpService;
	}

	public void setIsExpService(boolean isExpService) {
		this.isExpService = isExpService;
	}

	public Integer getInternalDue() {
		return internalDue;
	}

	public void setInternalDue(Integer internalDue) {
		this.internalDue = internalDue;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public Double getSurcharge() {
		return surcharge;
	}

	public void setSurcharge(Double surcharge) {
		this.surcharge = surcharge;
	}
}
