package com.ai.api.bean;

import java.io.Serializable;

/**
 * Created by KK on 4/25/2016.
 */
public class MinQuantityToBeReadyBean implements Serializable {

    private String serviceType;

    private int minQty;


    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public int getMinQty() {
        return minQty;
    }

    public void setMinQty(int minQty) {
        this.minQty = minQty;
    }
}
