package com.ai.api.bean.legacy;

/**
 * Created by Administrator on 2016/6/30 0030.
 */

import java.io.Serializable;

public class FactorySearchBean implements Serializable {

    private static final long serialVersionUID = -7779390029319826687L;

    private String supplierId;
    private String supplierName;
    private String city;
    private String country;
    private String contact;
    private String telephone;
    private String email;
    private String createdDate;
    private String updateDate;

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((supplierId == null) ? 0 : supplierId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        FactorySearchBean other = (FactorySearchBean) obj;
        if (supplierId == null) {
            if (other.supplierId != null) {
                return false;
            }
        } else if (!supplierId.equals(other.supplierId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FactorySearchBean [supplierId=" + supplierId
                + ", supplierName=" + supplierName + ", city=" + city
                + ", country=" + country + ", contact=" + contact
                + ", telephone=" + telephone + ", email=" + email
                + ", createdDate=" + createdDate + ", updateDate=" + updateDate
                + "]";
    }

}
