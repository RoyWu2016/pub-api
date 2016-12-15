package com.ai.api.bean;

import java.io.Serializable;

public class MasterCompanyBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1365125573287942605L;
	
	private String userId;

	private String companyId;

	private String companyName;

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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
