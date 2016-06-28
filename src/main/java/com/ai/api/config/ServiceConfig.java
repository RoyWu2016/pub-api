/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.config;

/***************************************************************************
 * <PRE>
 * Project Name    : publicAPI
 * <p>
 * Package Name    : com.ai.api.service
 * <p>
 * File Name       : ServiceConfig.java
 * <p>
 * Creation Date   : Mar 10, 2016
 * <p>
 * Author          : Allen Zhang
 * <p>
 * Purpose         : TODO
 * <p>
 * <p>
 * History         : TODO
 * <p>
 * </PRE>
 ***************************************************************************/

public class ServiceConfig {

    private String baseURL;

    private String customerServiceUrl;

	private String paramServiceUrl;

	private String ssoUserServiceUrl;

    private String factoryServiceUrl;

    private String fileServiceUrl;

    /**
     * @return the baseURL
     */
    public String getBaseURL() {
        return baseURL;
    }

    /**
     * @param baseURL the baseURL to set
     */
    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

	public String getParamServiceUrl() {
		return paramServiceUrl;
	}

	public void setParamServiceUrl(String paramServiceUrl) {
		this.paramServiceUrl = paramServiceUrl;
	}

	public String getCustomerServiceUrl() {
        return customerServiceUrl;
    }

    public void setCustomerServiceUrl(String customerServiceUrl) {
        this.customerServiceUrl = customerServiceUrl;
    }

	public String getSsoUserServiceUrl() {
		return ssoUserServiceUrl;
	}

	public void setSsoUserServiceUrl(String ssoUserServiceUrl) {
		this.ssoUserServiceUrl = ssoUserServiceUrl;
	}

    public String getFactoryServiceUrl() {
        return factoryServiceUrl;
    }

    public void setFactoryServiceUrl(String factoryServiceUrl) {
        this.factoryServiceUrl = factoryServiceUrl;
    }

    public String getFileServiceUrl() {
        return fileServiceUrl;
    }

    public void setFileServiceUrl(String fileServiceUrl) {
        this.fileServiceUrl = fileServiceUrl;
    }
}
