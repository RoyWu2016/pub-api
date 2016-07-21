package com.ai.api.bean;

/***************************************************************************
 * <PRE>
 * Project Name    : Public-API
 * <p>
 * Package Name    : com.ai.api.bean
 * <p>
 * Creation Date   : 2016/7/21 15:09
 * <p>
 * Author          : Jianxiong Cai
 * <p>
 * Purpose         : get data from ProductFamilyBean.RelevantCategoryInfoBean
 * <p>
 * <p>
 * History         : TODO
 * <p>
 * </PRE>
 ***************************************************************************/


public class PublicProductType {

	private String productCategoryId;

	private String productCategoryName;

	private String productFamilyId;

	private String productFamilyName;

	public String getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(String productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public String getProductCategoryName() {
		return productCategoryName;
	}

	public void setProductCategoryName(String productCategoryName) {
		this.productCategoryName = productCategoryName;
	}

	public String getProductFamilyId() {
		return productFamilyId;
	}

	public void setProductFamilyId(String productFamilyId) {
		this.productFamilyId = productFamilyId;
	}

	public String getProductFamilyName() {
		return productFamilyName;
	}

	public void setProductFamilyName(String productFamilyName) {
		this.productFamilyName = productFamilyName;
	}

	@Override
	public String toString() {
		return "PublicProductType{" +
				"productCategoryId='" + productCategoryId + '\'' +
				", productCategoryName='" + productCategoryName + '\'' +
				", productFamilyId='" + productFamilyId + '\'' +
				", productFamilyName='" + productFamilyName + '\'' +
				'}';
	}
}
