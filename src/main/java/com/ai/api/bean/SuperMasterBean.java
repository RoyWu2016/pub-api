package com.ai.api.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SuperMasterBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1243667755701550304L;
	
	private boolean isSuperMaster;
	
	private List<MasterCompanyBean> masterCompanies = new ArrayList<MasterCompanyBean>();

	public List<MasterCompanyBean> getMasterCompanies() {
		return masterCompanies;
	}

	public void setMasterCompanies(List<MasterCompanyBean> masterCompanies) {
		this.masterCompanies = masterCompanies;
	}

	public boolean isSuperMaster() {
		return isSuperMaster;
	}

	public void setSuperMaster(boolean isSuperMaster) {
		this.isSuperMaster = isSuperMaster;
	}


}
