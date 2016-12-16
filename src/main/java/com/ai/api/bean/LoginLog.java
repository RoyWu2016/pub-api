package com.ai.api.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Project Name    : Public-API
 * Package Name    : com.ai.api.bean
 * Creation Date   : 2016/12/16 10:32
 * Author          : Jianxiong.Cai
 * Purpose         : TODO
 * History         : TODO
 */
public class LoginLog implements Serializable {
    private String login;
    private String loginType;
    private Date loginTime;
    private String loginServer;
    private String loginIP;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getLoginServer() {
        return loginServer;
    }

    public void setLoginServer(String loginServer) {
        this.loginServer = loginServer;
    }

    public String getLoginIP() {
        return loginIP;
    }

    public void setLoginIP(String loginIP) {
        this.loginIP = loginIP;
    }

    @Override
    public String toString() {
        return "LoginLog{" +
                "login='" + login + '\'' +
                ", loginType='" + loginType + '\'' +
                ", loginTime=" + loginTime +
                ", loginServer='" + loginServer + '\'' +
                ", loginIP='" + loginIP + '\'' +
                '}';
    }
}
