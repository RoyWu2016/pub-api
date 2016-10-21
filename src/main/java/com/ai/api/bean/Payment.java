package com.ai.api.bean;

/**
 * Project Name    : Public-API
 * Package Name    : com.ai.api.bean
 * Creation Date   : 2016/10/21 17:14
 * Author          : Jianxiong.Cai
 * Purpose         : TODO
 * History         : TODO
 */
public class Payment {
    private int expressBookingFee;
    private String onlinePaymentTyep;

    public int getExpressBookingFee() {
        return expressBookingFee;
    }

    public void setExpressBookingFee(int expressBookingFee) {
        this.expressBookingFee = expressBookingFee;
    }

    public String getOnlinePaymentTyep() {
        return onlinePaymentTyep;
    }

    public void setOnlinePaymentTyep(String onlinePaymentTyep) {
        this.onlinePaymentTyep = onlinePaymentTyep;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "expressBookingFee=" + expressBookingFee +
                ", onlinePaymentTyep='" + onlinePaymentTyep + '\'' +
                '}';
    }
}
