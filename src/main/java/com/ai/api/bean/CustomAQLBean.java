package com.ai.api.bean;

/**
 * Created by KK on 4/25/2016.
 */
public class CustomAQLBean {

    private String criticalDefects;

    private String majorDefects;

    private String minorDefects;

    private String maxMeasurementDefects;


    public String getCriticalDefects() {
        return criticalDefects;
    }

    public void setCriticalDefects(String criticalDefects) {
        this.criticalDefects = criticalDefects;
    }

    public String getMajorDefects() {
        return majorDefects;
    }

    public void setMajorDefects(String majorDefects) {
        this.majorDefects = majorDefects;
    }

    public String getMinorDefects() {
        return minorDefects;
    }

    public void setMinorDefects(String minorDefects) {
        this.minorDefects = minorDefects;
    }

    public String getMaxMeasurementDefects() {
        return maxMeasurementDefects;
    }

    public void setMaxMeasurementDefects(String maxMeasurementDefects) {
        this.maxMeasurementDefects = maxMeasurementDefects;
    }
}
