package com.ai.api.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by yan on 2016/8/2.
 */
public class ReportApproverBean implements Serializable {
    private static final long serialVersionUID = 6710299036352213659L;
    private int approverSeq;
    private String approverName;
    private String approverPwd;
    private Date createTime;
    private Date updateTime;

    public ReportApproverBean() {
    }

    public int getApproverSeq() {
        return this.approverSeq;
    }

    public void setApproverSeq(int approverSeq) {
        this.approverSeq = approverSeq;
    }

    public String getApproverName() {
        return this.approverName;
    }

    public void setApproverName(String approverName) {
        this.approverName = approverName;
    }

    public String getApproverPwd() {
        return this.approverPwd;
    }

    public void setApproverPwd(String approverPwd) {
        this.approverPwd = approverPwd;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String toString() {
        return "ReportApproverBean [approverSeq=" + this.approverSeq + ", approverName=" + this.approverName + ", approverPwd=" + this.approverPwd + ", createTime=" + this.createTime + ", updateTime=" + this.updateTime + "]";
    }
}

