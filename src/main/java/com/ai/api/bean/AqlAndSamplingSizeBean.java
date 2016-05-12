package com.ai.api.bean;

/**
 * Created by KK on 4/25/2016.
 */
public class AqlAndSamplingSizeBean {


    private String canModify;

    private String customDefaultSampleLevel;

    private String useCustomAQL;

    private CustomAQLBean customAQL;

    public String getCanModify() {
        return canModify;
    }

    public void setCanModify(String canModify) {
        this.canModify = canModify;
    }

    public String getCustomDefaultSampleLevel() {
        return customDefaultSampleLevel;
    }

    public void setCustomDefaultSampleLevel(String customDefaultSampleLevel) {
        this.customDefaultSampleLevel = customDefaultSampleLevel;
    }

    public String getUseCustomAQL() {
        return useCustomAQL;
    }

    public void setUseCustomAQL(String useCustomAQL) {
        this.useCustomAQL = useCustomAQL;
    }

    public CustomAQLBean getCustomAQL() {
        return customAQL;
    }

    public void setCustomAQL(CustomAQLBean customAQL) {
        this.customAQL = customAQL;
    }
}
