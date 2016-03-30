package com.ai.api.bean;

/**
 * Created by KK on 3/21/2016.
 */
public class MultiReferenceBookingBean {

    private String approve_references;

    private String ask_number_of_references;

    private int number_of_reference_per_product;

    private int number_of_reference_per_report;

    private int number_of_reference_per_manday;

    private int number_of_pcs_per_reference;

    private int number_of_report_per_manday;

    private int clc_number_of_reports;

    private int clc_number_of_container;

    private String peo_calculation;

    private int container_rate;

    public String getApprove_references() {
        return approve_references;
    }

    public void setApprove_references(String approve_references) {
        this.approve_references = approve_references;
    }

    public String getAsk_number_of_references() {
        return ask_number_of_references;
    }

    public void setAsk_number_of_references(String ask_number_of_references) {
        this.ask_number_of_references = ask_number_of_references;
    }

    public int getNumber_of_reference_per_product() {
        return number_of_reference_per_product;
    }

    public void setNumber_of_reference_per_product(int number_of_reference_per_product) {
        this.number_of_reference_per_product = number_of_reference_per_product;
    }

    public int getNumber_of_reference_per_report() {
        return number_of_reference_per_report;
    }

    public void setNumber_of_reference_per_report(int number_of_reference_per_report) {
        this.number_of_reference_per_report = number_of_reference_per_report;
    }

    public int getNumber_of_reference_per_manday() {
        return number_of_reference_per_manday;
    }

    public void setNumber_of_reference_per_manday(int number_of_reference_per_manday) {
        this.number_of_reference_per_manday = number_of_reference_per_manday;
    }

    public int getNumber_of_pcs_per_reference() {
        return number_of_pcs_per_reference;
    }

    public void setNumber_of_pcs_per_reference(int number_of_pcs_per_reference) {
        this.number_of_pcs_per_reference = number_of_pcs_per_reference;
    }

    public int getNumber_of_report_per_manday() {
        return number_of_report_per_manday;
    }

    public void setNumber_of_report_per_manday(int number_of_report_per_manday) {
        this.number_of_report_per_manday = number_of_report_per_manday;
    }

    public int getClc_number_of_reports() {
        return clc_number_of_reports;
    }

    public void setClc_number_of_reports(int clc_number_of_reports) {
        this.clc_number_of_reports = clc_number_of_reports;
    }

    public int getClc_number_of_container() {
        return clc_number_of_container;
    }

    public void setClc_number_of_container(int clc_number_of_container) {
        this.clc_number_of_container = clc_number_of_container;
    }

    public String getPeo_calculation() {
        return peo_calculation;
    }

    public void setPeo_calculation(String peo_calculation) {
        this.peo_calculation = peo_calculation;
    }

    public int getContainer_rate() {
        return container_rate;
    }

    public void setContainer_rate(int container_rate) {
        this.container_rate = container_rate;
    }
}
