package com.ai.api.bean;

/**
 * Created by KK on 4/25/2016.
 */
public class BookingBean {
    private AqlAndSamplingSizeBean aqlAndSamplingSize;

    private MinQuantityToBeReadyBean minQuantityToBeReady;

    private String shouldSendRefSampleToFactory;

    private String isPoMandatory;

    private String useQuickFormByDefault;

    public AqlAndSamplingSizeBean getAqlAndSamplingSize() {
        return aqlAndSamplingSize;
    }

    public void setAqlAndSamplingSize(AqlAndSamplingSizeBean aqlAndSamplingSize) {
        this.aqlAndSamplingSize = aqlAndSamplingSize;
    }

    public MinQuantityToBeReadyBean getMinQuantityToBeReady() {
        return minQuantityToBeReady;
    }

    public void setMinQuantityToBeReady(MinQuantityToBeReadyBean minQuantityToBeReady) {
        this.minQuantityToBeReady = minQuantityToBeReady;
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

    public String getUseQuickFormByDefault() {
        return useQuickFormByDefault;
    }

    public void setUseQuickFormByDefault(String useQuickFormByDefault) {
        this.useQuickFormByDefault = useQuickFormByDefault;
    }
}
