package com.ai.api.controller;

import com.ai.api.exception.AIException;
import com.ai.api.model.CustomerBean;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

/**
 * Created by KK on 4/23/2016.
 */
public interface Customer {
    ResponseEntity<CustomerBean> getCustomerByLogin(String login) throws IOException, AIException;
}
