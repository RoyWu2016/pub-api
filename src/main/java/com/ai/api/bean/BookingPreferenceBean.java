package com.ai.api.bean;


import java.io.Serializable;

/**
 * Created by KK on 4/25/2016.
 */
public class BookingPreferenceBean implements Serializable {

	private String useQuickFormByDefault;

	private String shouldSendRefSampleToFactory;

	private String isPoMandatory;

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
}
