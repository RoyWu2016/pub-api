package com.ai.api.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderTestBean implements Serializable {

	private static final long serialVersionUID = -8209861354874434803L;
    
    private String id;
	private String name;
    private String failureStmt;
    private String rating;
    private String clientStatus;
    
    public OrderTestBean() {
    	super();
    }
    
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

	public String getFailureStmt() {
		return failureStmt;
	}

	public void setFailureStmt(String failureStmt) {
		this.failureStmt = failureStmt;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getClientStatus() {
		return clientStatus;
	}

	public void setClientStatus(String clientStatus) {
		this.clientStatus = clientStatus;
	}
}
