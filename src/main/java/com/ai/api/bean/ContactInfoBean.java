package com.ai.api.bean;

/**
 * Created by KK on 4/25/2016.
 */
public class ContactInfoBean {
    private MainBean main;

    private BillingBean billing;

    public BillingBean getBilling() {
        return billing;
    }

    public void setBilling(BillingBean billing) {
        this.billing = billing;
    }

    public MainBean getMain() {
        return main;
    }

    public void setMain(MainBean main) {
        this.main = main;
    }
}
