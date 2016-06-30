package com.ai.api.bean.legacy;

/**
 * Created by Administrator on 2016/6/30 0030.
 */

import java.io.Serializable;

public class ContactBean implements Serializable {
    private static final long serialVersionUID = -6550262009812706326L;

    private String name = "";
    private String phone = "";
    private String mobile = "";
    private String email = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "ContactBean [name=" + name + ", phone=" + phone + ", mobile="
                + mobile + ", email=" + email + "]";
    }

}
