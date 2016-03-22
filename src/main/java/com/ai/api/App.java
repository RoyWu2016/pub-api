/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api;

import org.springframework.beans.factory.annotation.Value;

/***************************************************************************
 * <PRE>
 *  Project Name    : public-api
 *
 *  Package Name    : com.ai.restful
 *
 *  File Name       : App.java
 *
 *  Creation Date   : 2016-03-01
 *
 *  Author          : Allen Zhang
 *
 *  Purpose         : TODO
 *
 *
 *  History         : TODO
 *
 * </PRE>
 ***************************************************************************/

//@Component
public class App {

    @Value("${user.service.url}")
    private String userServiceUrl;

    public static void main(String[] args) {
        System.out.println("Hello World!");
    }

    public String returnURL() {
        return userServiceUrl;
    }
}
