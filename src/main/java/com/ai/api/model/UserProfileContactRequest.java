package com.ai.api.model;

/**
 * Created by KK on 5/12/2016.
 */
public class UserProfileContactRequest {

    private String mainSalutation;

    private String mainFamilyName;

    private String mainGivenName;

    private String mainPosition;

    private String mainEmail;

    private String mainPhoneNumber;

    private String mainMobileNumber;

    private String billingSalutation;

    private String billingFamilyName;

    private String billingGivenName;

    private String billingEmail;

    private String billinIsSameAsMainContact;

    public String getMainSalutation() {
        return mainSalutation;
    }

    public void setMainSalutation(String mainSalutation) {
        this.mainSalutation = mainSalutation;
    }

    public String getMainFamilyName() {
        return mainFamilyName;
    }

    public void setMainFamilyName(String mainFamilyName) {
        this.mainFamilyName = mainFamilyName;
    }

    public String getMainGivenName() {
        return mainGivenName;
    }

    public void setMainGivenName(String mainGivenName) {
        this.mainGivenName = mainGivenName;
    }

    public String getMainPosition() {
        return mainPosition;
    }

    public void setMainPosition(String mainPosition) {
        this.mainPosition = mainPosition;
    }

    public String getMainEmail() {
        return mainEmail;
    }

    public void setMainEmail(String mainEmail) {
        this.mainEmail = mainEmail;
    }

    public String getMainPhoneNumber() {
        return mainPhoneNumber;
    }

    public void setMainPhoneNumber(String mainPhoneNumber) {
        this.mainPhoneNumber = mainPhoneNumber;
    }

    public String getMainMobileNumber() {
        return mainMobileNumber;
    }

    public void setMainMobileNumber(String mainMobileNumber) {
        this.mainMobileNumber = mainMobileNumber;
    }

    public String getBillingSalutation() {
        return billingSalutation;
    }

    public void setBillingSalutation(String billingSalutation) {
        this.billingSalutation = billingSalutation;
    }

    public String getBillingFamilyName() {
        return billingFamilyName;
    }

    public void setBillingFamilyName(String billingFamilyName) {
        this.billingFamilyName = billingFamilyName;
    }

    public String getBillingGivenName() {
        return billingGivenName;
    }

    public void setBillingGivenName(String billingGivenName) {
        this.billingGivenName = billingGivenName;
    }

    public String getBillingEmail() {
        return billingEmail;
    }

    public void setBillingEmail(String billingEmail) {
        this.billingEmail = billingEmail;
    }

    public String getBillinIsSameAsMainContact() {
        return billinIsSameAsMainContact;
    }

    public void setBillinIsSameAsMainContact(String billinIsSameAsMainContact) {
        this.billinIsSameAsMainContact = billinIsSameAsMainContact;
    }
}
