package com.ai.api.service;

import com.ai.commons.beans.customer.CustomerFeatureBean;

/**
 * Created by yan on 2016/7/21.
 */
public interface FeatureService {
    CustomerFeatureBean getCustomerFeatureBean(String customerId, String featureCode);
}
