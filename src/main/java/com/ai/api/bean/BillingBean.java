package com.ai.api.bean;

import java.io.Serializable;

/**
 * Created by KK on 4/25/2016.
 */
public class BillingBean implements Serializable {

    private String salutation;

    private String familyName;

    private String givenName;

    private String email;

    private String isSameAsMainContact;

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIsSameAsMainContact() {
        return isSameAsMainContact;
    }

    public void setIsSameAsMainContact(String isSameAsMainContact) {
        this.isSameAsMainContact = isSameAsMainContact;
    }
}
