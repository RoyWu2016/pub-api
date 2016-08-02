package com.ai.api.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by yan on 2016/8/2.
 */
public class ReportRejectCategoryBean implements Serializable {
    private static final long serialVersionUID = 1815302353551491071L;
    private int rejectCategorySeq;
    private String rejectCategory;
    private List<ReportRejectCategoryReasonBean> rejectCategoryReasons;
    private Date createTime;
    private Date updateTime;

    public ReportRejectCategoryBean() {
    }

    public int getRejectCategorySeq() {
        return this.rejectCategorySeq;
    }

    public void setRejectCategorySeq(int rejectCategorySeq) {
        this.rejectCategorySeq = rejectCategorySeq;
    }

    public String getRejectCategory() {
        return this.rejectCategory;
    }

    public void setRejectCategory(String rejectCategory) {
        this.rejectCategory = rejectCategory;
    }

    public List<ReportRejectCategoryReasonBean> getRejectCategoryReasons() {
        return this.rejectCategoryReasons;
    }

    public void setRejectCategoryReasons(List<ReportRejectCategoryReasonBean> rejectCategoryReasons) {
        this.rejectCategoryReasons = rejectCategoryReasons;
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
        return "ReportRejectCategoryBean [rejectCategorySeq=" + this.rejectCategorySeq + ", rejectCategory=" + this.rejectCategory + ", rejectCategoryReasons=" + this.rejectCategoryReasons + ", createTime=" + this.createTime + ", updateTime=" + this.updateTime + "]";
    }
}
