package com.ai.api.bean;

import java.io.Serializable;

public class SubordinateSettingsBean implements Serializable {

	/**
	 * settings controlled by master account
	 */
	private static final long serialVersionUID = -1416347337552100339L;
	
//	private String masterList;
//	private String isMaster;
//	private List<String> subCompanies;
//	private boolean canCreateOrder;
//	private boolean shareFactoryLib;
//	private boolean seePendingOrders;
//	private boolean seeOnlineReports;
//	private boolean seeAllFactories;
//	private boolean receiveAllMails;
//	private boolean sendAllMailsToSub;
//	private boolean allMailsToSubCcBcc;
//	private boolean sendReportMailsToMaster;
//	private boolean sendReportMailsToSub;
//	private String reportMailsToSubCcBcc;
//	private String disClientName;
	private boolean canSeeReportActionButtons;
	private boolean canSeeCcOptionInBookingForm;
	private boolean canSeeReportsPage;
	private boolean frozenIC=false;
//	private String whoPayOrder;
//	private String whoPayReorder;
//	private String whoPayLt;
//	private String whoPayAudit;
//	private String whoPayReAudit;
//	private String consolidatedInvoice;
//	private String suspendOrdersBy;
//	private boolean isReadMasterChecklist;


	public boolean getCanSeeReportActionButtons() {
		return canSeeReportActionButtons;
	}

	public void setCanSeeReportActionButtons(boolean canSeeReportActionButtons) {
		this.canSeeReportActionButtons = canSeeReportActionButtons;
	}

	public boolean getCanSeeCcOptionInBookingForm() {
		return canSeeCcOptionInBookingForm;
	}

	public void setCanSeeCcOptionInBookingForm(boolean canSeeCcOptionInBookingForm) {
		this.canSeeCcOptionInBookingForm = canSeeCcOptionInBookingForm;
	}

	public boolean getCanSeeReportsPage() {
		return canSeeReportsPage;
	}

	public void setCanSeeReportsPage(boolean canSeeReportsPage) {
		this.canSeeReportsPage = canSeeReportsPage;
	}

	public boolean isFrozenIC() {
		return frozenIC;
	}

	public void setFrozenIC(boolean frozenIC) {
		this.frozenIC = frozenIC;
	}
}
