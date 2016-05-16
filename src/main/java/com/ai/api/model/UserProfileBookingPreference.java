package com.ai.api.model;

/**
 * Created by kk on 5/12/2016.
 */

public class UserProfileBookingPreference {

    private String shouldSendRefSampleToFactory;

    private String isPoMandatory;

    private int minQuantityToBeReadyPsiPercentage;

    private int minQuantityToBeReadyDuproPercentage;

    private int minQuantityToBeReadyIpcPercentage;

    private int minQuantityToBeReadyClcPercentage;

    private int minQuantityToBeReadyPmPercentage;

    private String aleCanModify;

    private String aqlCustomDefaultSampleLevel;

    private String useCustomAQL;

    private String customAQLCriticalDefects;

    private String customAQLMajorDefects;

    private String custoMAQLMinorDefects;

    private String custoMAQLMaxMeasurementDefects;


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

    public int getMinQuantityToBeReadyPsiPercentage() {
        return minQuantityToBeReadyPsiPercentage;
    }

    public void setMinQuantityToBeReadyPsiPercentage(int minQuantityToBeReadyPsiPercentage) {
        this.minQuantityToBeReadyPsiPercentage = minQuantityToBeReadyPsiPercentage;
    }

    public int getMinQuantityToBeReadyDuproPercentage() {
        return minQuantityToBeReadyDuproPercentage;
    }

    public void setMinQuantityToBeReadyDuproPercentage(int minQuantityToBeReadyDuproPercentage) {
        this.minQuantityToBeReadyDuproPercentage = minQuantityToBeReadyDuproPercentage;
    }

    public int getMinQuantityToBeReadyIpcPercentage() {
        return minQuantityToBeReadyIpcPercentage;
    }

    public void setMinQuantityToBeReadyIpcPercentage(int minQuantityToBeReadyIpcPercentage) {
        this.minQuantityToBeReadyIpcPercentage = minQuantityToBeReadyIpcPercentage;
    }

    public int getMinQuantityToBeReadyClcPercentage() {
        return minQuantityToBeReadyClcPercentage;
    }

    public void setMinQuantityToBeReadyClcPercentage(int minQuantityToBeReadyClcPercentage) {
        this.minQuantityToBeReadyClcPercentage = minQuantityToBeReadyClcPercentage;
    }

    public int getMinQuantityToBeReadyPmPercentage() {
        return minQuantityToBeReadyPmPercentage;
    }

    public void setMinQuantityToBeReadyPmPercentage(int minQuantityToBeReadyPmPercentage) {
        this.minQuantityToBeReadyPmPercentage = minQuantityToBeReadyPmPercentage;
    }

    public String getAleCanModify() {
        return aleCanModify;
    }

    public void setAleCanModify(String aleCanModify) {
        this.aleCanModify = aleCanModify;
    }

    public String getAqlCustomDefaultSampleLevel() {
        return aqlCustomDefaultSampleLevel;
    }

    public void setAqlCustomDefaultSampleLevel(String aqlCustomDefaultSampleLevel) {
        this.aqlCustomDefaultSampleLevel = aqlCustomDefaultSampleLevel;
    }

    public String getUseCustomAQL() {
        return useCustomAQL;
    }

    public void setUseCustomAQL(String useCustomAQL) {
        this.useCustomAQL = useCustomAQL;
    }

    public String getCustomAQLCriticalDefects() {
        return customAQLCriticalDefects;
    }

    public void setCustomAQLCriticalDefects(String customAQLCriticalDefects) {
        this.customAQLCriticalDefects = customAQLCriticalDefects;
    }

    public String getCustomAQLMajorDefects() {
        return customAQLMajorDefects;
    }

    public void setCustomAQLMajorDefects(String customAQLMajorDefects) {
        this.customAQLMajorDefects = customAQLMajorDefects;
    }

    public String getCustoMAQLMinorDefects() {
        return custoMAQLMinorDefects;
    }

    public void setCustoMAQLMinorDefects(String custoMAQLMinorDefects) {
        this.custoMAQLMinorDefects = custoMAQLMinorDefects;
    }

    public String getCustoMAQLMaxMeasurementDefects() {
        return custoMAQLMaxMeasurementDefects;
    }

    public void setCustoMAQLMaxMeasurementDefects(String custoMAQLMaxMeasurementDefects) {
        this.custoMAQLMaxMeasurementDefects = custoMAQLMaxMeasurementDefects;
    }
}
