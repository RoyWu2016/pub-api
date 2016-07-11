package com.ai.api.bean;

import java.io.Serializable;

/**
 * Created by KK on 4/25/2016.
 */
public class CompanyBean implements Serializable {
	private String id;

    private String name;

    private String nameCN;

    private String industry;

    private String address;

    private String city;

    private String postcode;

	private String country;

    private String website;

    private String logo;

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameCN() {
        return nameCN;
    }

    public void setNameCN(String nameCN) {
        this.nameCN = nameCN;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountry() {
		return country;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
