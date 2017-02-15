package com.ai.api.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by KK on 4/25/2016.
 */
public class CompanyBean implements Serializable {
	private String id;

    private String type;

    private String parentCompanyId;

    private String parentCompanyName;

    private String name;

    private String nameCN;

    private String industry;

    private String address;

    private String city;

    private String postcode;

	private String country;

    private String website;

    private String logo;

    private String mainEmail;
    
//	private List<CompanyRelationshipBean> Parents;
	
	private List<CompanyRelationshipBean> subordinates;
	
	private SubordinateSettingsBean extraAccess;

	private SuperMasterBean superMaster;

	public SubordinateSettingsBean getExtraAccess() {
		return extraAccess;
	}

	public void setExtraAccess(SubordinateSettingsBean extraAccess) {
		this.extraAccess = extraAccess;
	}

//	public List<CompanyRelationshipBean> getParents() {
//		return Parents;
//	}
//
//	public void setParents(List<CompanyRelationshipBean> parents) {
//		Parents = parents;
//	}

	public List<CompanyRelationshipBean> getSubordinates() {
		return subordinates;
	}

	public void setSubordinates(List<CompanyRelationshipBean> subordinates) {
		this.subordinates = subordinates;
	}

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParentCompanyId() {
        return parentCompanyId;
    }

    public void setParentCompanyId(String parentCompanyId) {
        this.parentCompanyId = parentCompanyId;
    }

    public String getParentCompanyName() {
        return parentCompanyName;
    }

    public void setParentCompanyName(String parentCompanyName) {
        this.parentCompanyName = parentCompanyName;
    }

    public String getMainEmail() {
        return mainEmail;
    }

    public void setMainEmail(String mainEmail) {
        this.mainEmail = mainEmail;
    }

	public SuperMasterBean getSuperMaster() {
		return superMaster;
	}

	public void setSuperMaster(SuperMasterBean superMaster) {
		this.superMaster = superMaster;
	}

	@Override
    public String toString() {
        return "CompanyBean{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", parentCompanyId='" + parentCompanyId + '\'' +
                ", parentCompanyName='" + parentCompanyName + '\'' +
                ", name='" + name + '\'' +
                ", nameCN='" + nameCN + '\'' +
                ", industry='" + industry + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", postcode='" + postcode + '\'' +
                ", country='" + country + '\'' +
                ", website='" + website + '\'' +
                ", logo='" + logo + '\'' +
                ", mainEmail='" + mainEmail + '\'' +
                '}';
    }
}
