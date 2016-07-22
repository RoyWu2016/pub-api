package com.ai.api.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by KK on 5/12/2016..
 */
public class PreferredProductFamilies implements Serializable {


	private boolean useCustomizedProductType;
	private List<PublicProductType> publicProductTypeList;
	private List<CustomizedProductType> customizedProductTypeList;

	public boolean isUseCustomizedProductType() {
		return useCustomizedProductType;
	}

	public void setUseCustomizedProductType(boolean useCustomizedProductType) {
		this.useCustomizedProductType = useCustomizedProductType;
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
