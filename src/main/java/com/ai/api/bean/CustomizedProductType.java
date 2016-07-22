package com.ai.api.bean;

/***************************************************************************
 * <PRE>
 * Project Name    : Public-API
 * <p>
 * Package Name    : com.ai.api.bean
 * <p>
 * Creation Date   : 2016/7/21 15:13
 * <p>
 * Author          : Jianxiong Cai
 * <p>
 * Purpose         : get data from ProductFamilyBean.ProductFamilyInfoBean
 * <p>
 * <p>
 * History         : TODO
 * <p>
 * </PRE>
 ***************************************************************************/


public class CustomizedProductType {

	private String productId;
	private String productCategory;
	private String productFamily;
	private String productName;
	private String bookingType;
	private int maxPiecesPerMd;
	private int maxPointsPerMd;
	private int maxRefPerMd;
	private int prodSeq;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(String productCategory) {
		this.productCategory = productCategory;
	}

	public String getProductFamily() {
		return productFamily;
	}

	public void setProductFamily(String productFamily) {
		this.productFamily = productFamily;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getBookingType() {
		return bookingType;
	}

	public void setBookingType(String bookingType) {
		this.bookingType = bookingType;
	}

	public int getMaxPiecesPerMd() {
		return maxPiecesPerMd;
	}

	public void setMaxPiecesPerMd(int maxPiecesPerMd) {
		this.maxPiecesPerMd = maxPiecesPerMd;
	}

	public int getMaxPointsPerMd() {
		return maxPointsPerMd;
	}

	public void setMaxPointsPerMd(int maxPointsPerMd) {
		this.maxPointsPerMd = maxPointsPerMd;
	}

	public int getMaxRefPerMd() {
		return maxRefPerMd;
	}

	public void setMaxRefPerMd(int maxRefPerMd) {
		this.maxRefPerMd = maxRefPerMd;
	}

	public int getProdSeq() {
		return prodSeq;
	}

	public void setProdSeq(int prodSeq) {
		this.prodSeq = prodSeq;
	}

	@Override
	public String toString() {
		return "CustomizedProductType{" +
				"productId='" + productId + '\'' +
				", productCategory='" + productCategory + '\'' +
				", productFamily='" + productFamily + '\'' +
				", productName='" + productName + '\'' +
				", bookingType='" + bookingType + '\'' +
				", maxPiecesPerMd=" + maxPiecesPerMd +
				", maxPointsPerMd=" + maxPointsPerMd +
				", maxRefPerMd=" + maxRefPerMd +
				", prodSeq=" + prodSeq +
				'}';
	}
}
