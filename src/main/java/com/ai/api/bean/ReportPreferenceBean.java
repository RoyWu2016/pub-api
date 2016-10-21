package com.ai.api.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yan on 2016/8/2.
 */
public class ReportPreferenceBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean withAttachment;
    private String attType;
    private String reportContactName;
    private String reportTemplate;
    private int maxReportSize;
    private String disApproverName;
    private String sendMailToSupplier;
    private boolean sameDayReport;
    private boolean allowReportApprover;
    private List<ReportApproverBean> approvers;
    private List<ReportRejectCategoryBean> rejectCategories;
    private String rejectReasonSortBy;
    private String rejectReasonOther;

    public ReportPreferenceBean(){}

    public boolean isWithAttachment() {
        return withAttachment;
    }

    public void setWithAttachment(boolean withAttachment) {
        this.withAttachment = withAttachment;
    }

    public String getAttType() {
        return attType;
    }

    public void setAttType(String attType) {
        this.attType = attType;
    }

    public String getReportContactName() {
        return reportContactName;
    }

    public void setReportContactName(String reportContactName) {
        this.reportContactName = reportContactName;
    }

    public String getReportTemplate() {
        return reportTemplate;
    }

    public void setReportTemplate(String reportTemplate) {
        this.reportTemplate = reportTemplate;
    }

    public int getMaxReportSize() {
        return maxReportSize;
    }

    public void setMaxReportSize(int maxReportSize) {
        this.maxReportSize = maxReportSize;
    }

    public String getDisApproverName() {
        return disApproverName;
    }

    public void setDisApproverName(String disApproverName) {
        this.disApproverName = disApproverName;
    }

    public String getSendMailToSupplier() {
        return sendMailToSupplier;
    }

    public void setSendMailToSupplier(String sendMailToSupplier) {
        this.sendMailToSupplier = sendMailToSupplier;
    }

    public boolean isSameDayReport() {
        return sameDayReport;
    }

    public void setSameDayReport(boolean sameDayReport) {
        this.sameDayReport = sameDayReport;
    }

    public boolean isAllowReportApprover() {
        return allowReportApprover;
    }

    public void setAllowReportApprover(boolean allowReportApprover) {
        this.allowReportApprover = allowReportApprover;
    }

    public List<ReportApproverBean> getApprovers() {
        return approvers;
    }

    public void setApprovers(List<ReportApproverBean> approvers) {
        this.approvers = approvers;
    }

    public List<ReportRejectCategoryBean> getRejectCategories() {
        return rejectCategories;
    }

    public void setRejectCategories(List<ReportRejectCategoryBean> rejectCategories) {
        this.rejectCategories = rejectCategories;
    }

    public String getRejectReasonSortBy() {
        return rejectReasonSortBy;
    }

    public void setRejectReasonSortBy(String rejectReasonSortBy) {
        this.rejectReasonSortBy = rejectReasonSortBy;
    }

    public String getRejectReasonOther() {
        return rejectReasonOther;
    }

    public void setRejectReasonOther(String rejectReasonOther) {
        this.rejectReasonOther = rejectReasonOther;
    }

    @Override
    public String toString() {
        return "ReportPreferenceBean{" +
                "withAttachment=" + withAttachment +
                ", attType='" + attType + '\'' +
                ", reportContactName='" + reportContactName + '\'' +
                ", reportTemplate='" + reportTemplate + '\'' +
                ", maxReportSize=" + maxReportSize +
                ", disApproverName='" + disApproverName + '\'' +
                ", sendMailToSupplier='" + sendMailToSupplier + '\'' +
                ", sameDayReport=" + sameDayReport +
                ", allowReportApprover=" + allowReportApprover +
                ", approvers=" + approvers +
                ", rejectCategories=" + rejectCategories +
                ", rejectReasonSortBy='" + rejectReasonSortBy + '\'' +
                ", rejectReasonOther='" + rejectReasonOther + '\'' +
                '}';
    }
}
