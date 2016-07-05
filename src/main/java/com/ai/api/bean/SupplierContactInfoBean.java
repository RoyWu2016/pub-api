package com.ai.api.bean;

import com.ai.api.bean.legacy.ContactBean;

/**
 * Created by Administrator on 2016/6/27 0027.
 */
public class SupplierContactInfoBean {

    private ContactBean main;
    private ContactBean alternate;

    public SupplierContactInfoBean(){
        this.main = new ContactBean();
        this.alternate = new ContactBean();
    }

    public ContactBean getMain() {
        return main;
    }

    public void setMain(ContactBean main) {
        this.main = main;
    }

    public ContactBean getAlternate() {
        return alternate;
    }

    public void setAlternate(ContactBean alternate) {
        this.alternate = alternate;
    }
}
