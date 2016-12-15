package com.ai.api.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SuperMasterBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1243667755701550304L;
	
	private boolean isSupperMaster;
	
	private List<MasterCompanyBean> masterCompanies = new ArrayList<MasterCompanyBean>();

	public boolean getIsSupperMaster() {
		return isSupperMaster;
	}

	public List<MasterCompanyBean> getMasterCompanies() {
		return masterCompanies;
	}

	public void setMasterCompanies(List<MasterCompanyBean> masterCompanies) {
		this.masterCompanies = masterCompanies;
	}

	public void setIsSupperMaster(boolean isSupperMaster) {
		this.isSupperMaster = isSupperMaster;
	}


}
