package com.ai.api.bean;

/**
 * Created by KK on 4/25/2016.
 */
public class AqlAndSamplingSizeBean {
    private CustomAQLBean customAQL;

    private String customDefaultSampleLevel;

    private String canModify;

    private String useCustomAQL;

    public CustomAQLBean getCustomAQL() {
        return customAQL;
    }

    public void setCustomAQL(CustomAQLBean customAQL) {
        this.customAQL = customAQL;
    }

    public String getCustomDefaultSampleLevel() {
        return customDefaultSampleLevel;
    }

    public void setCustomDefaultSampleLevel(String customDefaultSampleLevel) {
        this.customDefaultSampleLevel = customDefaultSampleLevel;
    }

    public String getCanModify() {
        return canModify;
    }

    public void setCanModify(String canModify) {
        this.canModify = canModify;
    }

    public String getUseCustomAQL() {
        return useCustomAQL;
    }

    public void setUseCustomAQL(String useCustomAQL) {
        this.useCustomAQL = useCustomAQL;
    }

}
