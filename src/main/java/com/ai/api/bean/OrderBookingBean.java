package com.ai.api.bean;

import com.ai.commons.beans.customer.FavoriteTestBean;

import java.util.List;

/**
 * Created by KK on 3/21/2016.
 */
public class OrderBookingBean {

    private int clc_percentage;

    private int psi_percentage;

    private int dupro_percentage;

    private int ipc_percentage;

    private int pm_percentage;

    private int clc_percentage_packed;

    private int psi_percentage_packed;

    private int dupro_percentage_packed;

    private int ipc_percentage_packed;

    private int pm_percentage_packed;

    private String send_modification_mail;

    private String send_sample_to_factory;

    private String po_compulsory;

    private String require_drop_testing;

    private String share_perferred_tests;

    private String share_checklist;

    private String turn_off_ai_access;

    private List<FavoriteTestBean> favorite_tests;

    private String allow_change_aql;

    private String custom_aql_level;

    private String critical_defects;

    private String major_defects;

    private String minor_defects;

    private String max_mea_defects;

    private String customized_sample_level;

    private String measurement_sample_level;

    public int getClc_percentage() {
        return clc_percentage;
    }

    public void setClc_percentage(int clc_percentage) {
        this.clc_percentage = clc_percentage;
    }

    public int getPsi_percentage() {
        return psi_percentage;
    }

    public void setPsi_percentage(int psi_percentage) {
        this.psi_percentage = psi_percentage;
    }

    public int getDupro_percentage() {
        return dupro_percentage;
    }

    public void setDupro_percentage(int dupro_percentage) {
        this.dupro_percentage = dupro_percentage;
    }

    public int getIpc_percentage() {
        return ipc_percentage;
    }

    public void setIpc_percentage(int ipc_percentage) {
        this.ipc_percentage = ipc_percentage;
    }

    public int getPm_percentage() {
        return pm_percentage;
    }

    public void setPm_percentage(int pm_percentage) {
        this.pm_percentage = pm_percentage;
    }

    public int getClc_percentage_packed() {
        return clc_percentage_packed;
    }

    public void setClc_percentage_packed(int clc_percentage_packed) {
        this.clc_percentage_packed = clc_percentage_packed;
    }

    public int getPsi_percentage_packed() {
        return psi_percentage_packed;
    }

    public void setPsi_percentage_packed(int psi_percentage_packed) {
        this.psi_percentage_packed = psi_percentage_packed;
    }

    public int getDupro_percentage_packed() {
        return dupro_percentage_packed;
    }

    public void setDupro_percentage_packed(int dupro_percentage_packed) {
        this.dupro_percentage_packed = dupro_percentage_packed;
    }

    public int getIpc_percentage_packed() {
        return ipc_percentage_packed;
    }

    public void setIpc_percentage_packed(int ipc_percentage_packed) {
        this.ipc_percentage_packed = ipc_percentage_packed;
    }

    public int getPm_percentage_packed() {
        return pm_percentage_packed;
    }

    public void setPm_percentage_packed(int pm_percentage_packed) {
        this.pm_percentage_packed = pm_percentage_packed;
    }

    public String getSend_modification_mail() {
        return send_modification_mail;
    }

    public void setSend_modification_mail(String send_modification_mail) {
        this.send_modification_mail = send_modification_mail;
    }

    public String getSend_sample_to_factory() {
        return send_sample_to_factory;
    }

    public void setSend_sample_to_factory(String send_sample_to_factory) {
        this.send_sample_to_factory = send_sample_to_factory;
    }

    public String getPo_compulsory() {
        return po_compulsory;
    }

    public void setPo_compulsory(String po_compulsory) {
        this.po_compulsory = po_compulsory;
    }

    public String getRequire_drop_testing() {
        return require_drop_testing;
    }

    public void setRequire_drop_testing(String require_drop_testing) {
        this.require_drop_testing = require_drop_testing;
    }

    public String getShare_perferred_tests() {
        return share_perferred_tests;
    }

    public void setShare_perferred_tests(String share_perferred_tests) {
        this.share_perferred_tests = share_perferred_tests;
    }

    public String getShare_checklist() {
        return share_checklist;
    }

    public void setShare_checklist(String share_checklist) {
        this.share_checklist = share_checklist;
    }

    public String getTurn_off_ai_access() {
        return turn_off_ai_access;
    }

    public void setTurn_off_ai_access(String turn_off_ai_access) {
        this.turn_off_ai_access = turn_off_ai_access;
    }

    public List<FavoriteTestBean> getFavorite_tests() {
        return favorite_tests;
    }

    public void setFavorite_tests(List<FavoriteTestBean> favorite_tests) {
        this.favorite_tests = favorite_tests;
    }

    public String getAllow_change_aql() {
        return allow_change_aql;
    }

    public void setAllow_change_aql(String allow_change_aql) {
        this.allow_change_aql = allow_change_aql;
    }

    public String getCustom_aql_level() {
        return custom_aql_level;
    }

    public void setCustom_aql_level(String custom_aql_level) {
        this.custom_aql_level = custom_aql_level;
    }

    public String getCritical_defects() {
        return critical_defects;
    }

    public void setCritical_defects(String critical_defects) {
        this.critical_defects = critical_defects;
    }

    public String getMajor_defects() {
        return major_defects;
    }

    public void setMajor_defects(String major_defects) {
        this.major_defects = major_defects;
    }

    public String getMinor_defects() {
        return minor_defects;
    }

    public void setMinor_defects(String minor_defects) {
        this.minor_defects = minor_defects;
    }

    public String getMax_mea_defects() {
        return max_mea_defects;
    }

    public void setMax_mea_defects(String max_mea_defects) {
        this.max_mea_defects = max_mea_defects;
    }

    public String getCustomized_sample_level() {
        return customized_sample_level;
    }

    public void setCustomized_sample_level(String customized_sample_level) {
        this.customized_sample_level = customized_sample_level;
    }

    public String getMeasurement_sample_level() {
        return measurement_sample_level;
    }

    public void setMeasurement_sample_level(String measurement_sample_level) {
        this.measurement_sample_level = measurement_sample_level;
    }
}
