package com.ai.api.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by yan on 2016/8/2.
 */
public class ReportRejectCategoryReasonBean implements Serializable {
    private static final long serialVersionUID = -2217740292447815168L;
    private int rejectCategorySeq;
    private int rejectReasonSeq;
    private String rejectReason;
    private Date createTime;
    private Date updateTime;

    public ReportRejectCategoryReasonBean() {
    }

    public int getRejectCategorySeq() {
        return this.rejectCategorySeq;
    }

    public void setRejectCategorySeq(int rejectCategorySeq) {
        this.rejectCategorySeq = rejectCategorySeq;
    }

    public int getRejectReasonSeq() {
        return this.rejectReasonSeq;
    }

    public void setRejectReasonSeq(int rejectReasonSeq) {
        this.rejectReasonSeq = rejectReasonSeq;
    }

    public String getRejectReason() {
        return this.rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
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
        return "ReportRejectCategoryReasonBean [rejectCategorySeq=" + this.rejectCategorySeq + ", rejectReasonSeq=" + this.rejectReasonSeq + ", rejectReason=" + this.rejectReason + ", createTime=" + this.createTime + ", updateTime=" + this.updateTime + "]";
    }
}
