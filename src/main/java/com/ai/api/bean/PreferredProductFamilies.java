package com.ai.api.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by KK on 5/12/2016..
 */
public class PreferredProductFamilies implements Serializable {


	private boolean customizedProductType;
	private List<PublicProductType> publicProductTypeList;
	private List<CustomizedProductType> customizedProductTypeList;

	public boolean isCustomizedProductType() {
		return customizedProductType;
	}

	public void setCustomizedProductType(boolean customizedProductType) {
		this.customizedProductType = customizedProductType;
	}

	public List<PublicProductType> getPublicProductTypeList() {
		return publicProductTypeList;
	}

	public void setPublicProductTypeList(List<PublicProductType> publicProductTypeList) {
		this.publicProductTypeList = publicProductTypeList;
	}

	public List<CustomizedProductType> getCustomizedProductTypeList() {
		return customizedProductTypeList;
	}

	public void setCustomizedProductTypeList(List<CustomizedProductType> customizedProductTypeList) {
		this.customizedProductTypeList = customizedProductTypeList;
	}
}
