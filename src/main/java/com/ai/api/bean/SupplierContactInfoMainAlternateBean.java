package com.ai.api.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/27 0027.
 */
public class SupplierContactInfoMainAlternateBean implements Serializable {

    private String name;
    private String phoneNumber;
    private String mobileNumber;
    private String email;

    public SupplierContactInfoMainAlternateBean() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
