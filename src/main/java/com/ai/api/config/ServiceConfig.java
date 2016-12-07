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

//    private String baseURL;

    private String customerServiceUrl;

	private String paramServiceUrl;

	private String ssoUserServiceUrl;

    private String factoryServiceUrl;

    private String fileServiceUrl;
   
	private int fileMaximumSize;
	
    private int cacheRefreshInterval;
 
	private String mwServiceUrl;

    private String checklistServiceUrl;

    private String reportServiceUrl;

//    private String mwFTPHost;
//    private String mwFTPUsername;
//    private String mwFTPPassword;

    private String psiServiceUrl;

    private String redisHost;
    private String redisPort;
    private String redisPassword;
    private String aimsServiceBaseUrl;
    private String programServiceBaseUrl;

	private String financeServiceBaseUrl;
    
    private String excleLoggoCommonSource;

    //    public String getBaseURL() {
//        return baseURL;
//    }

//    public void setBaseURL(String baseURL) {
//        this.baseURL = baseURL;
//    }

	public String getExcleLoggoCommonSource() {
		return excleLoggoCommonSource;
	}

	public void setExcleLoggoCommonSource(String excleLoggoCommonSource) {
		this.excleLoggoCommonSource = excleLoggoCommonSource;
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

    public String getFileServiceUrl() {
        return fileServiceUrl;
    }

    public void setFileServiceUrl(String fileServiceUrl) {
        this.fileServiceUrl = fileServiceUrl;
    }

    public String getFactoryServiceUrl() {
        return factoryServiceUrl;
    }

    public void setFactoryServiceUrl(String factoryServiceUrl) {
        this.factoryServiceUrl = factoryServiceUrl;
    }

	public String getMwServiceUrl() {
		return mwServiceUrl;
	}

	public void setMwServiceUrl(String mwServiceUrl) {
		this.mwServiceUrl = mwServiceUrl;
	}

    public String getChecklistServiceUrl() {
        return checklistServiceUrl;
    }

    public void setChecklistServiceUrl(String checklistServiceUrl) {
        this.checklistServiceUrl = checklistServiceUrl;
    }

    public String getReportServiceUrl() {
        return reportServiceUrl;
    }

    public void setReportServiceUrl(String reportServiceUrl) {
        this.reportServiceUrl = reportServiceUrl;
    }

//    public String getMwFTPHost() {
//        return mwFTPHost;
//    }
//
//    public void setMwFTPHost(String mwFTPHost) {
//        this.mwFTPHost = mwFTPHost;
//    }
//
//    public String getMwFTPUsername() {
//        return mwFTPUsername;
//    }
//
//    public void setMwFTPUsername(String mwFTPUsername) {
//        this.mwFTPUsername = mwFTPUsername;
//    }
//
//    public String getMwFTPPassword() {
//        return mwFTPPassword;
//    }
//
//    public void setMwFTPPassword(String mwFTPPassword) {
//        this.mwFTPPassword = mwFTPPassword;
//    }

    public String getPsiServiceUrl() {
        return psiServiceUrl;
    }

    public void setPsiServiceUrl(String psiServiceUrl) {
        this.psiServiceUrl = psiServiceUrl;
    }

	public int getCacheRefreshInterval() {
		return cacheRefreshInterval;
	}

	public void setCacheRefreshInterval(int cacheRefreshInterval) {
		this.cacheRefreshInterval = cacheRefreshInterval;
	}

	public int getFileMaximumSize() {
		return fileMaximumSize;
	}

	public void setFileMaximumSize(int fileMaximumSize) {
		this.fileMaximumSize = fileMaximumSize;
	}


    public String getRedisHost() {
        return redisHost;
    }

    public void setRedisHost(String redisHost) {
        this.redisHost = redisHost;
    }

    public String getRedisPort() {
        return redisPort;
    }

    public void setRedisPort(String redisPort) {
        this.redisPort = redisPort;
    }

    public String getRedisPassword() {
        return redisPassword;
    }

    public void setRedisPassword(String redisPassword) {
        this.redisPassword = redisPassword;
    }

	public String getAimsServiceBaseUrl() {
		return aimsServiceBaseUrl;
	}

	public void setAimsServiceBaseUrl(String aimsServiceBaseUrl) {
		this.aimsServiceBaseUrl = aimsServiceBaseUrl;
	}

	public String getProgramServiceBaseUrl() {
		return programServiceBaseUrl;
	}

	public void setProgramServiceBaseUrl(String programServiceBaseUrl) {
		this.programServiceBaseUrl = programServiceBaseUrl;
	}

	public String getFinanceServiceBaseUrl() {
		return financeServiceBaseUrl;
	}

	public void setFinanceServiceBaseUrl(String financeServiceBaseUrl) {
		this.financeServiceBaseUrl = financeServiceBaseUrl;
	}

	@Override
	public String toString() {
		return "ServiceConfig{" +
				"customerServiceUrl='" + customerServiceUrl + '\'' +
				", paramServiceUrl='" + paramServiceUrl + '\'' +
				", ssoUserServiceUrl='" + ssoUserServiceUrl + '\'' +
				", factoryServiceUrl='" + factoryServiceUrl + '\'' +
				", fileServiceUrl='" + fileServiceUrl + '\'' +
				", fileMaximumSize=" + fileMaximumSize +
				", cacheRefreshInterval=" + cacheRefreshInterval +
				", mwServiceUrl='" + mwServiceUrl + '\'' +
				", checklistServiceUrl='" + checklistServiceUrl + '\'' +
				", reportServiceUrl='" + reportServiceUrl + '\'' +
				", psiServiceUrl='" + psiServiceUrl + '\'' +
				", redisHost='" + redisHost + '\'' +
				", redisPort='" + redisPort + '\'' +
				", redisPassword='" + redisPassword + '\'' +
				", aimsServiceBaseUrl='" + aimsServiceBaseUrl + '\'' +
				", programServiceBaseUrl='" + programServiceBaseUrl + '\'' +
				", financeServiceBaseUrl='" + financeServiceBaseUrl + '\'' +
				", excleLoggoCommonSource='" + excleLoggoCommonSource + '\'' +
				'}';
	}
}
