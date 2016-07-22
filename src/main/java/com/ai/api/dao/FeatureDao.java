package com.ai.api.dao;

import com.ai.commons.beans.customer.CustomerFeatureBean;

/**
 * Created by yan on 2016/7/21.
 */
public interface FeatureDao {
    CustomerFeatureBean getCustomerFeatureBean(String customerId, String featureCode);
}
