package com.ai.api.bean;

/**
 * Project Name : Public-API Package Name : com.ai.api.bean Creation Date :
 * 2016/10/21 17:14 Author : Jianxiong.Cai Purpose : TODO History : TODO
 */
public class Payment {
	private String onlinePaymentType;

	private String charge;

	public String getOnlinePaymentType() {
		return onlinePaymentType;
	}

	public void setOnlinePaymentType(String onlinePaymentType) {
		this.onlinePaymentType = onlinePaymentType;
	}

	public String getCharge() {
		return charge;
	}

	public void setCharge(String charge) {
		this.charge = charge;
	}

	@Override
	public String toString() {
		return "Payment{" + "onlinePaymentType='" + onlinePaymentType
				+ '\'' + '}';
	}
}
