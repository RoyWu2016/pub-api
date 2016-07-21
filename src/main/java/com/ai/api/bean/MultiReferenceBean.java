package com.ai.api.bean;

import java.io.Serializable;

/**
 * Created by yan on 2016/7/21.
 */
public class MultiReferenceBean implements Serializable {

    private boolean clientCanApproveRejectIndividualProductReferences;

    public MultiReferenceBean() { }

    public boolean isClientCanApproveRejectIndividualProductReferences() {
        return clientCanApproveRejectIndividualProductReferences;
    }

    public void setClientCanApproveRejectIndividualProductReferences(boolean clientCanApproveRejectIndividualProductReferences) {
        this.clientCanApproveRejectIndividualProductReferences = clientCanApproveRejectIndividualProductReferences;
    }
}
