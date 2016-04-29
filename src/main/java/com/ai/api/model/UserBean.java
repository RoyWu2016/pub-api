package com.ai.api.model;

import com.ai.api.bean.*;

/**
 *
 */
public class UserBean {

    private String id;

    private String login;

    private String sic;

    private String status;

    private String businessUnit;

    private CompanyBean company;

    private ContactInfoBean contactInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSic() {
        return sic;
    }

    public void setSic(String sic) {
        this.sic = sic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(String businessUnit) {
        this.businessUnit = businessUnit;
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
}
