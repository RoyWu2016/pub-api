package com.ai.api.bean;

import java.io.Serializable;

/**
 * Created by KK on 4/25/2016.
 */
public class AqlAndSamplingSizeBean implements Serializable {


    /**
	 * 
	 */
	private static final long serialVersionUID = -903716730770653692L;

	private boolean canModify;

    private String customDefaultSampleLevel;

    private boolean useCustomAQL;

    private CustomAQLBean customAQL;
    
    private String measurementSampleLevel;

    public String getMeasurementSampleLevel() {
		return measurementSampleLevel;
	}

	public void setMeasurementSampleLevel(String measurementSampleLevel) {
		this.measurementSampleLevel = measurementSampleLevel;
	}

	public boolean isCanModify() {
        return canModify;
    }

    public void setCanModify(boolean canModify) {
        this.canModify = canModify;
    }

    public String getCustomDefaultSampleLevel() {
        return customDefaultSampleLevel;
    }

    public void setCustomDefaultSampleLevel(String customDefaultSampleLevel) {
        this.customDefaultSampleLevel = customDefaultSampleLevel;
    }

    public boolean isUseCustomAQL() {
        return useCustomAQL;
    }

    public void setUseCustomAQL(boolean useCustomAQL) {
        this.useCustomAQL = useCustomAQL;
    }

    public CustomAQLBean getCustomAQL() {
        return customAQL;
    }

    public void setCustomAQL(CustomAQLBean customAQL) {
        this.customAQL = customAQL;
    }

    @Override
    public String toString() {
        return "AqlAndSamplingSizeBean{" +
                "canModify=" + canModify +
                ", customDefaultSampleLevel='" + customDefaultSampleLevel + '\'' +
                ", useCustomAQL=" + useCustomAQL +
                ", customAQL=" + customAQL +
                ", measurementSampleLevel=" + measurementSampleLevel +
                '}';
    }
}
