/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.bean;

import java.io.Serializable;

import com.ai.commons.beans.order.draft.DraftProductInfo;
import com.ai.commons.beans.psi.InspectionProductBookingBean;

/***************************************************************************
 *<PRE>
 *  Project Name    : api
 *
 *  Package Name    : com.ai.api.bean
 *
 *  File Name       : InspectionDraftPrdocutBean.java
 *
 *  Creation Date   : 2016-09-26
 *
 *  Author          : Allen Zhang
 *
 *  Purpose         : TODO
 *
 *
 *  History         : TODO
 *
 *</PRE>
 ***************************************************************************/

public class InspectionDraftProductBean implements Serializable{
	private static final long serialVersionUID = -6683696172694029898L;

	String draftProductId;      //primary key in DRAFT_PRODUCT table
	String orderProductId;           //productId in psi bean, primary key in ORDER_PRODUCT table
	String draftId;
	String referenceNumber;
	String productName;
	String poNumber;
	long createUnixTimestamp;
	long updateUnixTimestamp;
	DraftProductInfo progress;
	InspectionProductBookingBean productInfo;

	public String getDraftProductId() {
		return draftProductId;
	}

	public void setDraftProductId(String draftProductId) {
		this.draftProductId = draftProductId;
	}

	public String getOrderProductId() {
		return orderProductId;
	}

	public void setOrderProductId(String orderProductId) {
		this.orderProductId = orderProductId;
	}

	public String getDraftId() {
		return draftId;
	}

	public void setDraftId(String draftId) {
		this.draftId = draftId;
	}

	public String getReferenceNumber() {
		return referenceNumber;
	}

	public void setReferenceNumber(String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public long getCreateUnixTimestamp() {
		return createUnixTimestamp;
	}

	public void setCreateUnixTimestamp(long createUnixTimestamp) {
		this.createUnixTimestamp = createUnixTimestamp;
	}

	public long getUpdateUnixTimestamp() {
		return updateUnixTimestamp;
	}

	public void setUpdateUnixTimestamp(long updateUnixTimestamp) {
		this.updateUnixTimestamp = updateUnixTimestamp;
	}

	public DraftProductInfo getProgress() {
		return progress;
	}

	public void setProgress(DraftProductInfo progress) {
		this.progress = progress;
	}

	public InspectionProductBookingBean getProductInfo() {
		return productInfo;
	}

	public void setProductInfo(InspectionProductBookingBean productInfo) {
		this.productInfo = productInfo;
	}

	@Override
	public String toString() {
		return "InspectionDraftPrdocutBean{" +
				"draftProductId='" + draftProductId + '\'' +
				", orderProductId='" + orderProductId + '\'' +
				", draftId='" + draftId + '\'' +
				", referenceNumber='" + referenceNumber + '\'' +
				", productName='" + productName + '\'' +
				", poNumber='" + poNumber + '\'' +
				", createUnixTimestamp=" + createUnixTimestamp +
				", updateUnixTimestamp=" + updateUnixTimestamp +
				", progress=" + progress +
				", productInfo=" + productInfo +
				'}';
	}
}
