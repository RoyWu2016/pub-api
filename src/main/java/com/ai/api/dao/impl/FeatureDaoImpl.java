package com.ai.api.dao.impl;

import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.FeatureDao;
import com.ai.commons.HttpUtil;
import com.ai.commons.JsonUtil;
import com.ai.commons.beans.GetRequest;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.customer.CustomerFeatureBean;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Created by Henry Yue on 2016/7/21.
 */
public class FeatureDaoImpl implements FeatureDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeatureDaoImpl.class);

    @Autowired
    @Qualifier("serviceConfig")
    private ServiceConfig config;

    @Override
    public CustomerFeatureBean getCustomerFeatureBean(String customerId, String featureCode){
        String url = config.getCustomerServiceUrl()+"/customer/" + customerId +  "/" + featureCode + "/customer-feature";
        GetRequest request = GetRequest.newInstance().setUrl(url);
        try{
            ServiceCallResult result = HttpUtil.issueGetRequest(request);
            CustomerFeatureBean customerFeature = JsonUtil.mapToObject(result.getResponseString(),CustomerFeatureBean.class);
            return customerFeature;
        }catch (Exception e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
        return null;
    }
}
