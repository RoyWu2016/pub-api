package com.ai.api.service.impl;

import com.ai.api.dao.FeatureDao;
import com.ai.api.service.FeatureService;
import com.ai.commons.beans.customer.CustomerFeatureBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by yan on 2016/7/21.
 */

@Service
public class FeatureServiceImpl implements FeatureService {
    private static final Logger logger = LoggerFactory.getLogger(FeatureServiceImpl.class);

    @Autowired
    @Qualifier("featureDao")
    private FeatureDao featureDao;

    @Override
    public CustomerFeatureBean getCustomerFeatureBean(String customerId, String featureCode){
        return featureDao.getCustomerFeatureBean(customerId,featureCode);
    }

}
