package com.ai.api.model;

import com.ai.api.bean.*;

/**
 *
 */
public class CustomerBean {
    private PreferencesBean preferences;

    private CompanyBean company;

    private ContactInfoBean contactInfo;

    private String sic;

    private String userId;

    public PreferencesBean getPreferences() {
        return preferences;
    }

    public void setPreferences(PreferencesBean preferences) {
        this.preferences = preferences;
    }

    public CompanyBean getCompany() {
        return company;
    }

    public void setCompany(CompanyBean company) {
        this.company = company;
    }

    public ContactInfoBean getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfoBean contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getSic() {
        return sic;
    }

    public void setSic(String sic) {
        this.sic = sic;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
