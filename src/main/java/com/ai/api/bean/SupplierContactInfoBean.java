package com.ai.api.bean;

/**
 * Created by Administrator on 2016/6/27 0027.
 */
public class SupplierContactInfoBean {

    private SupplierContactInfoMainAlternateBean main;
    private SupplierContactInfoMainAlternateBean alternate;

    public SupplierContactInfoBean(){}

    public SupplierContactInfoMainAlternateBean getMain() {
        return main;
    }

    public void setMain(SupplierContactInfoMainAlternateBean main) {
        this.main = main;
    }

    public SupplierContactInfoMainAlternateBean getAlternate() {
        return alternate;
    }

    public void setAlternate(SupplierContactInfoMainAlternateBean alternate) {
        this.alternate = alternate;
    }
}
