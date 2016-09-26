package com.ai.api.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ai.commons.beans.order.draft.DraftStepBean;
import com.ai.commons.beans.psi.InspectionProductBookingBean;

public class InspectionDraftProductsAndStepBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5720317781355694507L;
	
	private List<InspectionProductBookingBean> draftProductsList = new ArrayList<InspectionProductBookingBean>();
	
	private List<DraftStepBean> draftSteps = new ArrayList<DraftStepBean>();

	public List<InspectionProductBookingBean> getDraftProductsList() {
		return draftProductsList;
	}

	public void setDraftProductsList(List<InspectionProductBookingBean> draftProductsList) {
		this.draftProductsList = draftProductsList;
	}

	public List<DraftStepBean> getDraftSteps() {
		return draftSteps;
	}

	public void setDraftSteps(List<DraftStepBean> draftSteps) {
		this.draftSteps = draftSteps;
	}

}
