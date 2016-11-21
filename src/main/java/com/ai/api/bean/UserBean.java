package com.ai.api.bean;

import java.io.Serializable;

import com.ai.commons.beans.customer.RateBean;

/**
 *
 */
public class UserBean implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 22348442799126945L;

	private String id;

    private String login;

    private String sic;
    
    private String sicId;

    private String status;

    private String businessUnit;

    private CompanyBean company;

    private ContactInfoBean contacts;

    private PreferencesBean preferences;

    private Payment payment;
    
    private RateBean rate;

    public RateBean getRate() {
		return rate;
	}

	public void setRate(RateBean rate) {
		this.rate = rate;
	}

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

    public ContactInfoBean getContacts() {
        return contacts;
    }

    public void setContacts(ContactInfoBean contacts) {
        this.contacts = contacts;
    }

    public PreferencesBean getPreferences() {
        return preferences;
    }

    public void setPreferences(PreferencesBean preferences) {
        this.preferences = preferences;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public String getSicId() {
		return sicId;
	}

	public void setSicId(String sicId) {
		this.sicId = sicId;
	}

	@Override
    public String toString() {
        return "UserBean{" +
                "id='" + id + '\'' +
                ", login='" + login + '\'' +
                ", sic='" + sic + '\'' +
                ", status='" + status + '\'' +
                ", businessUnit='" + businessUnit + '\'' +
                ", company=" + company +
                ", contacts=" + contacts +
                ", preferences=" + preferences +
                ", payment=" + payment +
                '}';
    }
}
