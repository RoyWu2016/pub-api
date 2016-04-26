package com.ai.api.bean;

/**
 * Created by KK on 4/25/2016.
 */
public class ContactInfoBean {
    private BillingBean billing;

    private MainBean main;

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
