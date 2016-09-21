package com.ai.api.bean;

import java.io.Serializable;

public class LabelBean implements Serializable {

	/**
	 * created by RoyWu 2016/09/21
	 */
	private static final long serialVersionUID = 1L;
	
	private String label;
	
	private String value;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
