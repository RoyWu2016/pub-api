package com.ai.api.bean;


import java.io.Serializable;

/**
 * Created by KK on 4/25/2016.
 */
public class BookingPreferenceBean implements Serializable {

	private String useQuickFormByDefault;

	//private String shouldSendRefSampleToFactory;

	//private String isPoMandatory;

	private boolean showProductDivision;

	private boolean sendEmailAfterModification;

	private boolean sendReferenceSampleAlways;

	private boolean isFieldPoCompulsory;

	private boolean showFactoryDetailsToMaster;

	private boolean requireDropTesting;

	private boolean allowPostponementBySuppliers;

	private boolean sendSupplierConfirmationEmailToClientAlways;

	private boolean shareFavoriteLabTestsWithSubAccounts;

	private boolean shareChecklistWithSubAccounts;

	private boolean turnOffAiWebsiteDirectAccess;

	private boolean bookOrdersWithMultipleFactories;

	private String productDivisions;

	private MultiReferenceBean multiReference;

	private MinQuantityToBeReadyBean[] minQuantityToBeReady;

	private AqlAndSamplingSizeBean aqlAndSamplingSize;

	private PreferredProductFamilies[] preferredProductFamilies;

	private QualityManual qualityManual;

	public String getUseQuickFormByDefault() {
		return useQuickFormByDefault;
	}

	public void setUseQuickFormByDefault(String useQuickFormByDefault) {
		this.useQuickFormByDefault = useQuickFormByDefault;
	}
/*
	public String getShouldSendRefSampleToFactory() {
		return shouldSendRefSampleToFactory;
	}

	public void setShouldSendRefSampleToFactory(String shouldSendRefSampleToFactory) {
		this.shouldSendRefSampleToFactory = shouldSendRefSampleToFactory;
	}

	public String getIsPoMandatory() {
		return isPoMandatory;
	}

	public void setIsPoMandatory(String isPoMandatory) {
		this.isPoMandatory = isPoMandatory;
	}
*/
	public MinQuantityToBeReadyBean[] getMinQuantityToBeReady() {
		return minQuantityToBeReady;
	}

	public void setMinQuantityToBeReady(MinQuantityToBeReadyBean[] minQuantityToBeReady) {
		this.minQuantityToBeReady = minQuantityToBeReady;
	}

	public AqlAndSamplingSizeBean getAqlAndSamplingSize() {
		return aqlAndSamplingSize;
	}

	public void setAqlAndSamplingSize(AqlAndSamplingSizeBean aqlAndSamplingSize) {
		this.aqlAndSamplingSize = aqlAndSamplingSize;
	}

	public PreferredProductFamilies[] getPreferredProductFamilies() {
		return preferredProductFamilies;
	}

	public void setPreferredProductFamilies(PreferredProductFamilies[] preferredProductFamilies) {
		this.preferredProductFamilies = preferredProductFamilies;
	}

	public QualityManual getQualityManual() {
		return qualityManual;
	}

	public void setQualityManual(QualityManual qualityManual) {
		this.qualityManual = qualityManual;
	}

	public boolean isShowProductDivision() {
		return showProductDivision;
	}

	public void setShowProductDivision(boolean showProductDivision) {
		this.showProductDivision = showProductDivision;
	}

	public boolean isSendEmailAfterModification() {
		return sendEmailAfterModification;
	}

	public void setSendEmailAfterModification(boolean sendEmailAfterModification) {
		this.sendEmailAfterModification = sendEmailAfterModification;
	}

	public boolean isSendReferenceSampleAlways() {
		return sendReferenceSampleAlways;
	}

	public void setSendReferenceSampleAlways(boolean sendReferenceSampleAlways) {
		this.sendReferenceSampleAlways = sendReferenceSampleAlways;
	}

	public boolean isFieldPoCompulsory() {
		return isFieldPoCompulsory;
	}

	public void setFieldPoCompulsory(boolean fieldPoCompulsory) {
		isFieldPoCompulsory = fieldPoCompulsory;
	}

	public boolean isShowFactoryDetailsToMaster() {
		return showFactoryDetailsToMaster;
	}

	public void setShowFactoryDetailsToMaster(boolean showFactoryDetailsToMaster) {
		this.showFactoryDetailsToMaster = showFactoryDetailsToMaster;
	}

	public boolean isRequireDropTesting() {
		return requireDropTesting;
	}

	public void setRequireDropTesting(boolean requireDropTesting) {
		this.requireDropTesting = requireDropTesting;
	}

	public boolean isAllowPostponementBySuppliers() {
		return allowPostponementBySuppliers;
	}

	public void setAllowPostponementBySuppliers(boolean allowPostponementBySuppliers) {
		this.allowPostponementBySuppliers = allowPostponementBySuppliers;
	}

	public boolean isSendSupplierConfirmationEmailToClientAlways() {
		return sendSupplierConfirmationEmailToClientAlways;
	}

	public void setSendSupplierConfirmationEmailToClientAlways(boolean sendSupplierConfirmationEmailToClientAlways) {
		this.sendSupplierConfirmationEmailToClientAlways = sendSupplierConfirmationEmailToClientAlways;
	}

	public boolean isShareFavoriteLabTestsWithSubAccounts() {
		return shareFavoriteLabTestsWithSubAccounts;
	}

	public void setShareFavoriteLabTestsWithSubAccounts(boolean shareFavoriteLabTestsWithSubAccounts) {
		this.shareFavoriteLabTestsWithSubAccounts = shareFavoriteLabTestsWithSubAccounts;
	}

	public boolean isShareChecklistWithSubAccounts() {
		return shareChecklistWithSubAccounts;
	}

	public void setShareChecklistWithSubAccounts(boolean shareChecklistWithSubAccounts) {
		this.shareChecklistWithSubAccounts = shareChecklistWithSubAccounts;
	}

	public boolean isTurnOffAiWebsiteDirectAccess() {
		return turnOffAiWebsiteDirectAccess;
	}

	public void setTurnOffAiWebsiteDirectAccess(boolean turnOffAiWebsiteDirectAccess) {
		this.turnOffAiWebsiteDirectAccess = turnOffAiWebsiteDirectAccess;
	}

	public boolean isBookOrdersWithMultipleFactories() {
		return bookOrdersWithMultipleFactories;
	}

	public void setBookOrdersWithMultipleFactories(boolean bookOrdersWithMultipleFactories) {
		this.bookOrdersWithMultipleFactories = bookOrdersWithMultipleFactories;
	}

	public String getProductDivisions() {
		return productDivisions;
	}

	public void setProductDivisions(String productDivisions) {
		this.productDivisions = productDivisions;
	}

	public MultiReferenceBean getMultiReference() {
		return multiReference;
	}

	public void setMultiReference(MultiReferenceBean multiReference) {
		this.multiReference = multiReference;
	}
}
