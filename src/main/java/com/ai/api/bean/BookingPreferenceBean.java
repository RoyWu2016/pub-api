package com.ai.api.bean;


import java.io.Serializable;

/**
 * Created by KK on 4/25/2016.
 */
public class BookingPreferenceBean implements Serializable {

	private String useQuickFormByDefault;

	private boolean shouldSendRefSampleToFactory;

	private boolean isPoMandatory;

	private boolean showProductDivision;

	private boolean sendEmailAfterModification;

	//private boolean sendReferenceSampleAlways;

	//private boolean isFieldPoCompulsory;

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

	private PreferredProductFamilies preferredProductFamilies;

	private AqlAndSamplingSizeBean aqlAndSamplingSize;

	private QualityManual qualityManual;

	public String getUseQuickFormByDefault() {
		return useQuickFormByDefault;
	}

	public void setUseQuickFormByDefault(String useQuickFormByDefault) {
		this.useQuickFormByDefault = useQuickFormByDefault;
	}

	public MinQuantityToBeReadyBean[] getMinQuantityToBeReady() {
		return minQuantityToBeReady;
	}

	public void setMinQuantityToBeReady(MinQuantityToBeReadyBean[] minQuantityToBeReady) {
		this.minQuantityToBeReady = minQuantityToBeReady;
	}

	public PreferredProductFamilies getPreferredProductFamilies() {
		return preferredProductFamilies;
	}

	public void setPreferredProductFamilies(PreferredProductFamilies preferredProductFamilies) {
		this.preferredProductFamilies = preferredProductFamilies;
	}

	public AqlAndSamplingSizeBean getAqlAndSamplingSize() {
		return aqlAndSamplingSize;
	}

	public void setAqlAndSamplingSize(AqlAndSamplingSizeBean aqlAndSamplingSize) {
		this.aqlAndSamplingSize = aqlAndSamplingSize;
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

	public boolean isShouldSendRefSampleToFactory() {
		return shouldSendRefSampleToFactory;
	}

	public void setShouldSendRefSampleToFactory(boolean shouldSendRefSampleToFactory) {
		this.shouldSendRefSampleToFactory = shouldSendRefSampleToFactory;
	}

	public boolean getIsPoMandatory() {
		return isPoMandatory;
	}

	public void setIsPoMandatory(boolean poMandatory) {
		isPoMandatory = poMandatory;
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
