package com.ai.api.bean;

public class CountryBean extends DropdownListOptionBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String code;
	private String isoCode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public void setIsoCode(String isoCode) {
        this.isoCode = isoCode;
    }
}
