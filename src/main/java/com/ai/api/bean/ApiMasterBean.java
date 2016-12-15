package com.ai.api.bean;

import java.io.Serializable;
import java.util.List;

public class ApiMasterBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1416347337552100339L;
	
    private SuperMasterBean superMaster;
	private String masterList;
//	private String isMaster;
//	private List<String> subCompanies;
	private boolean canCreateOrder;
	private boolean shareFactoryLib;
	private boolean seePendingOrders;
	private boolean seeOnlineReports;
	private boolean seeAllFactories;
	private boolean receiveAllMails;
	private boolean sendAllMailsToSub;
	private boolean allMailsToSubCcBcc;
	private boolean sendReportMailsToMaster;
	private boolean sendReportMailsToSub;
	private String reportMailsToSubCcBcc;
	private String disClientName;
	private boolean hideApproveButton;
	private boolean hideCcFields;
	private String whoPayOrder;
	private String whoPayReorder;
	private String whoPayLt;
	private String whoPayAudit;
	private String whoPayReAudit;
	private String consolidatedInvoice;
	private String suspendOrdersBy;
	private boolean isReadMasterChecklist;
	
	public boolean isCanCreateOrder() {
		return canCreateOrder;
	}
	public void setCanCreateOrder(boolean canCreateOrder) {
		this.canCreateOrder = canCreateOrder;
	}
	public boolean isShareFactoryLib() {
		return shareFactoryLib;
	}
	public void setShareFactoryLib(boolean shareFactoryLib) {
		this.shareFactoryLib = shareFactoryLib;
	}
	public boolean isSeePendingOrders() {
		return seePendingOrders;
	}
	public void setSeePendingOrders(boolean seePendingOrders) {
		this.seePendingOrders = seePendingOrders;
	}
	public boolean isSeeOnlineReports() {
		return seeOnlineReports;
	}
	public void setSeeOnlineReports(boolean seeOnlineReports) {
		this.seeOnlineReports = seeOnlineReports;
	}
	public boolean isSeeAllFactories() {
		return seeAllFactories;
	}
	public void setSeeAllFactories(boolean seeAllFactories) {
		this.seeAllFactories = seeAllFactories;
	}
	public boolean isReceiveAllMails() {
		return receiveAllMails;
	}
	public void setReceiveAllMails(boolean receiveAllMails) {
		this.receiveAllMails = receiveAllMails;
	}
	public boolean isSendAllMailsToSub() {
		return sendAllMailsToSub;
	}
	public void setSendAllMailsToSub(boolean sendAllMailsToSub) {
		this.sendAllMailsToSub = sendAllMailsToSub;
	}
	public boolean isAllMailsToSubCcBcc() {
		return allMailsToSubCcBcc;
	}
	public void setAllMailsToSubCcBcc(boolean allMailsToSubCcBcc) {
		this.allMailsToSubCcBcc = allMailsToSubCcBcc;
	}
	public boolean isSendReportMailsToMaster() {
		return sendReportMailsToMaster;
	}
	public void setSendReportMailsToMaster(boolean sendReportMailsToMaster) {
		this.sendReportMailsToMaster = sendReportMailsToMaster;
	}
	public boolean isSendReportMailsToSub() {
		return sendReportMailsToSub;
	}
	public void setSendReportMailsToSub(boolean sendReportMailsToSub) {
		this.sendReportMailsToSub = sendReportMailsToSub;
	}
	public void setReportMailsToSubCcBcc(String reportMailsToSubCcBcc) {
		this.reportMailsToSubCcBcc = reportMailsToSubCcBcc;
	}
	public String isDisClientName() {
		return disClientName;
	}
	public void setDisClientName(String disClientName) {
		this.disClientName = disClientName;
	}
	public boolean isHideApproveButton() {
		return hideApproveButton;
	}
	public void setHideApproveButton(boolean hideApproveButton) {
		this.hideApproveButton = hideApproveButton;
	}
	public boolean isHideCcFields() {
		return hideCcFields;
	}
	public void setHideCcFields(boolean hideCcFields) {
		this.hideCcFields = hideCcFields;
	}
	public String getWhoPayOrder() {
		return whoPayOrder;
	}
	public void setWhoPayOrder(String whoPayOrder) {
		this.whoPayOrder = whoPayOrder;
	}
	public String getWhoPayReorder() {
		return whoPayReorder;
	}
	public void setWhoPayReorder(String whoPayReorder) {
		this.whoPayReorder = whoPayReorder;
	}
	public String getWhoPayLt() {
		return whoPayLt;
	}
	public void setWhoPayLt(String whoPayLt) {
		this.whoPayLt = whoPayLt;
	}
	public String getWhoPayAudit() {
		return whoPayAudit;
	}
	public void setWhoPayAudit(String whoPayAudit) {
		this.whoPayAudit = whoPayAudit;
	}
	public String getWhoPayReAudit() {
		return whoPayReAudit;
	}
	public void setWhoPayReAudit(String whoPayReAudit) {
		this.whoPayReAudit = whoPayReAudit;
	}
	public String getConsolidatedInvoice() {
		return consolidatedInvoice;
	}
	public void setConsolidatedInvoice(String consolidatedInvoice) {
		this.consolidatedInvoice = consolidatedInvoice;
	}
	public String getSuspendOrdersBy() {
		return suspendOrdersBy;
	}
	public void setSuspendOrdersBy(String suspendOrdersBy) {
		this.suspendOrdersBy = suspendOrdersBy;
	}
	public boolean isReadMasterChecklist() {
		return isReadMasterChecklist;
	}
	public void setReadMasterChecklist(boolean isReadMasterChecklist) {
		this.isReadMasterChecklist = isReadMasterChecklist;
	}
	public SuperMasterBean getSuperMaster() {
		return superMaster;
	}
	public void setSuperMaster(SuperMasterBean superMaster) {
		this.superMaster = superMaster;
	}
	public String getMasterList() {
		return masterList;
	}
	public void setMasterList(String masterList) {
		this.masterList = masterList;
	}
	public String getReportMailsToSubCcBcc() {
		return reportMailsToSubCcBcc;
	}
	public String getDisClientName() {
		return disClientName;
	}

}
