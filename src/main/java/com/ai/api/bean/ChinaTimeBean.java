package com.ai.api.bean;

import java.io.Serializable;

public class ChinaTimeBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2635835883423153108L;
	
	private String datetime;
	


	private String timezone;
	
	private Integer unixTimeStamp;


	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public Integer getUnixTimeStamp() {
		return unixTimeStamp;
	}

	public void setUnixTimeStamp(Integer unixTimeStamp) {
		this.unixTimeStamp = unixTimeStamp;
	}

}
