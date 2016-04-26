package com.ai.api.controller.impl;

import com.ai.api.controller.Customer;
import com.ai.api.exception.AIException;
import com.ai.api.model.CustomerBean;
import com.ai.api.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Created by KK on 4/23/2016.
 */
@RestController
public class CustomerImpl implements Customer {

    @Autowired
    CustomerService customerService;  //Service which will do all data retrieval/manipulation work

    @Override
    @RequestMapping(value = "/users/{login}/profile", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomerBean> getCustomerByLogin(@PathVariable("login") String login) throws IOException, AIException {
        System.out.println("/users/{login} login: " + login);
        CustomerBean cust = null;

        System.out.println("------inside CustomerImpl /users/{login}------");

        try {
            cust = customerService.getCustByLogin(login);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AIException e) {
            e.printStackTrace();
        }
        if (cust == null) {
            System.out.println("User with login " + login + " not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(cust, HttpStatus.OK);
    }

}
