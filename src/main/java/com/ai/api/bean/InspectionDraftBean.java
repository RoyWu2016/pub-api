/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ai.api.bean.consts.ConstMap;
import com.ai.commons.beans.order.draft.DraftOrderInfo;
import com.ai.commons.beans.psi.InspectionOrderBookingBean;

/***************************************************************************
 *<PRE>
 *  Project Name    : api
 *
 *  Package Name    : com.ai.api.bean
 *
 *  File Name       : DraftBean.java
 *
 *  Creation Date   : 2016-08-26
 *
 *  Author          : Allen Zhang
 *
 *  Purpose         : draft bean to interact with PSI draft bean
 *
 *
 *  History         : TODO
 *
 *</PRE>
 ***************************************************************************/

public class InspectionDraftBean implements Serializable{
	private static final long serialVersionUID = -6683696172654329898L;

	String id;     //draftId in psi bean
	String orderId;
	String userId;
	String companyId;
	String parentCompanyId;
	String inspectionType;
	String inspectionTypeText;      //no such field in PSI
	String completed;
	String inspectionDate;    //need format in DD-MMM-YYYY
	long createUnixTimestamp;
	long updateUnixTimestamp;
	DraftOrderInfo pregress;
	InspectionOrderBookingBean orderInfo;
	List<InspectionDraftPrdocutBean> prdocuts = new ArrayList<>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getParentCompanyId() {
		return parentCompanyId;
	}

	public void setParentCompanyId(String parentCompanyId) {
		this.parentCompanyId = parentCompanyId;
	}

	public String getInspectionType() {
		return inspectionType;
	}

	public void setInspectionType(String inspectionType) {
		this.inspectionType = inspectionType;
	}

	public String getCompleted() {
		return completed;
	}

	public void setCompleted(String completed) {
		this.completed = completed;
	}

	public String getInspectionDate() {
		return inspectionDate;
	}

	public void setInspectionDate(String inspectionDate) {
		this.inspectionDate = inspectionDate;
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

	public DraftOrderInfo getPregress() {
		return pregress;
	}

	public void setPregress(DraftOrderInfo pregress) {
		this.pregress = pregress;
	}

	public InspectionOrderBookingBean getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(InspectionOrderBookingBean orderInfo) {
		this.orderInfo = orderInfo;
	}

	public List<InspectionDraftPrdocutBean> getPrdocuts() {
		return prdocuts;
	}

	public void setPrdocuts(List<InspectionDraftPrdocutBean> prdocuts) {
		this.prdocuts = prdocuts;
	}

	public String getInspectionTypeText() {
		return ConstMap.serviceTypeMap.get(inspectionType);
	}

	@Override
	public String toString() {
		return "InspectionDraftBean{" +
				"id='" + id + '\'' +
				", orderId='" + orderId + '\'' +
				", userId='" + userId + '\'' +
				", companyId='" + companyId + '\'' +
				", parentCompanyId='" + parentCompanyId + '\'' +
				", inspectionType='" + inspectionType + '\'' +
				", inspectionTypeText='" + inspectionTypeText + '\'' +
				", completed='" + completed + '\'' +
				", inspectionDate='" + inspectionDate + '\'' +
				", createUnixTimestamp=" + createUnixTimestamp +
				", updateUnixTimestamp=" + updateUnixTimestamp +
				", pregress=" + pregress +
				", orderInfo=" + orderInfo +
				", prdocuts=" + prdocuts +
				'}';
	}


}
